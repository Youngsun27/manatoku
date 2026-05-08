package com.manatoku.chat;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.manatoku.model.ChatMessage;
import com.manatoku.model.MemberResponse;
import com.manatoku.service.ChatService;

import java.io.IOException;
import java.util.Set;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* js에서 연결 요청하기 위한 url '/chat'과 세션 정보를 가져오기 위한 configurator 객체 HttpSessionConfigurator을 선언 */
/* Component 선언 이유는 스프링에서 객체 관리를 하게 설정하기 위함 */
@Component
@ServerEndpoint(value = "/chat", configurator = HttpSessionConfigurator.class)
public class ChatEndpoint {

    /* Service 의존성 주입 */
	private final ChatService chatService;
	public ChatEndpoint(ChatService chatService) {
		this.chatService = chatService;
	}

    /* Map 함수로 Integer로 roomId를 키값으로 웹소캣 Session 정보 보관 */
	private static Map<Integer, Set<Session>> roomSessions = new ConcurrentHashMap<>();

    /* 위와 반대로 Map 함수로 웹소캣 Session 정보를 키값으로 roomId 정보 보관 */
	private static Map<Session, Integer> sessionRoomMap = new ConcurrentHashMap<>();

    // 채팅창=>캘린더 일정 : 날짜 패턴 (YYYY-MM-DD 또는 YYYY/MM/DD)
    private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{4})[-/](\\d{1,2})[-/](\\d{1,2})");
    
    /* 웹소캣 세션을 열 때 동작 */
	@OnOpen
	public void onOpen(Session session, EndpointConfig config) throws IOException {
    	
        /* 웹소캣은 세션 정보를 모르기 때문에 EndpointConfig 객체에 구성한 사용자 세션 정보를 가져온다 */
    	HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        
    	Integer ucode = null;
    	String name = null;
    	
        /* 세션에서 사용자의 정보를 가져온다 */
    	/* 사용자의 세션이 없을 경우 Member 객체를 채울 수 없기 때문에 session값이 null이라면 걸러준다 
    	 * (*보통은 로그인 필터선에서 걸러져서 들어오긴 한다 2중 방어*) */
        if(httpSession != null ) {
        	MemberResponse member = (MemberResponse)httpSession.getAttribute("member"); // 세션 파라미터를 Member 객체로 변환
        	
        	/* Member 객체에서 유저코드와 이름 추출 */
        	ucode = member.getUcode();
        	name = member.getName();
        }
        
        /* ucode 정보가 잘못 들어왔을 경우 차단 */
        if (ucode == null) {
            session.close(new CloseReason(
                CloseReason.CloseCodes.VIOLATED_POLICY, "로그인 필요"));
            return;
        }
        
        /* HttpSessionConfigurator 객체에 파라미터 저장 */
        session.getUserProperties().put("name", name);
        session.getUserProperties().put("ucode", ucode);

        /* 세션에서 roomId 파라미터를 가져온다 */
        Integer roomId = Integer.parseInt(session.getRequestParameterMap().get("roomId").get(0));

        /* 사용자가 room의 참가자인지 검증 */    
            if (chatService.isUserInRoom(ucode, roomId) != 1) { // 사용자가 room의 참가자가 아닌경우
            	/* 웹소캣 세션을 닫는다 */
                session.close(new CloseReason(
                    CloseReason.CloseCodes.VIOLATED_POLICY, "방 접근 권한 없음"));
                return;
            }

        /* roomId를 키값으로 웹소캣 세션 정보를 Map에 맵핑 */
        roomSessions.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);
        sessionRoomMap.put(session, roomId);
        session.getUserProperties().put("ucode", ucode);
    }
    
	/* 메세지가 입력되었을 경우의 동작 */
    @OnMessage
    public void onMessage(String content, Session session) throws IOException {

        /* 사용자의 세션 정보를 가져온다 */
        Integer roomId = sessionRoomMap.get(session); //세션을 키값으로 roomId를 가져오기
        Integer ucode  = (Integer)session.getUserProperties().get("ucode"); //configurator 객체에서 ucode 파라미터를 가져오기
        String name = (String) session.getUserProperties().get("name"); //configurator 객체에서 name 파라미터를 가져오기

        /* 방 정보나 유저 정보가 존재하지 않는 경우 세션 종료 */
        if (roomId == null || ucode == null) {
            session.close(new CloseReason(
                CloseReason.CloseCodes.VIOLATED_POLICY, "세션 정보 없음"));
            return;
        }

        /* 사용자가 보낸 메세지 로그를 DB에 저장 */
        chatService.insertMessage(roomId, ucode, content);

        // 3) 같은 방 세션들에게 브로드캐스트
        Set<Session> sessions = roomSessions.get(roomId);
        if (sessions == null) return;

        // 채팅창=>캘린더 일정 :  날짜 감지
        String detectedDate = extractDate(content);

        ChatMessage msg = new ChatMessage();
        msg.setRoomId(roomId);
        msg.setSenderUcode(ucode);
        msg.setSenderName(name);
        msg.setContent(content);
        if (detectedDate != null) {
            msg.setDetectedDate(detectedDate);
        }
        else { msg.setDetectedDate(""); }
        
        String jsonPayload = new Gson().toJson(msg);
        
        for (Session s : sessions) {
            if (s != null && s.isOpen()) {
                try {
                    s.getBasicRemote().sendText(jsonPayload);
                } catch (Exception ignore) {
                    // 전송 실패는 개별 세션 문제일 수 있으니 전체를 죽이지 않음
                }
            }
        }
    }

    // 채팅창=>캘린더 일정 :  날짜 추출 함수
    private String extractDate(String text) {
        Matcher matcher = DATE_PATTERN.matcher(text);
        if (matcher.find()) {
            return matcher.group(0).replace("/", "-");
        }
        return null;
    }


    @OnClose
	public void onClose(Session session, CloseReason reason) {

    	Integer roomId = sessionRoomMap.remove(session);
    	if (roomId == null) return;

    	Set<Session> sessions = roomSessions.get(roomId);
    	if (sessions != null) {
    		sessions.remove(session);

    		// 방에 아무도 없으면 Map에서 방 자체 제거(메모리 정리)
    		if (sessions.isEmpty()) {
    			roomSessions.remove(roomId);
    		}
    	}

    	System.out.println("연결 종료: roomId=" + roomId + " reason=" + reason);
	}
	
	@OnError
	public void onError(Session session, Throwable throwable) {

	    // onClose와 동일한 정리 로직 수행(중복 호출돼도 괜찮게 작성)
	    try {
	        if (session != null) {
	            Integer roomId = sessionRoomMap.remove(session);
	            if (roomId != null) {
	                Set<Session> sessions = roomSessions.get(roomId);
	                if (sessions != null) {
	                    sessions.remove(session);
	                    if (sessions.isEmpty()) roomSessions.remove(roomId);
	                }
	            }
	        }
	    } catch (Exception ignore) {}

	    System.out.println("에러 발생: " + throwable);
	}
    
    
    
}
