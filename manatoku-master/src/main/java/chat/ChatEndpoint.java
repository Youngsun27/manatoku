package chat;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import org.apache.ibatis.session.SqlSession;

import com.google.gson.Gson;

import dao.ChatMapper;
import model.ChatMessage;
import util.MyBatisUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat", configurator = HttpSessionConfigurator.class)
public class ChatEndpoint {

    // roomId → 접속 세션들
    private static Map<Integer, Set<Session>> roomSessions = new ConcurrentHashMap<>();

    // 세션 → roomId (정리용)
    private static Map<Session, Integer> sessionRoomMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException {

        // 1️ HTTP 세션에서 로그인 유저 정보 가져오기
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        Integer ucode = (Integer)httpSession.getAttribute("ucode");
        String name = (String) httpSession.getAttribute("name");
        
        session.getUserProperties().put("name", name);
        session.getUserProperties().put("ucode", ucode);
        
        // 로그인 안 되어 있으면 차단
        if (ucode == null) {
            session.close(new CloseReason(
                CloseReason.CloseCodes.VIOLATED_POLICY, "로그인 필요"));
            return;
        }

        // 2️ URL 파라미터에서 roomId 가져오기
        Integer roomId = Integer.parseInt(session.getRequestParameterMap().get("roomId").get(0));

        // 3 DB에서 이 유저가 이 방 멤버인지 검증
        try (SqlSession sql = MyBatisUtil.getFactory().openSession()) {
            ChatMapper mapper = sql.getMapper(ChatMapper.class);
            
            if (mapper.isUserInRoom(ucode, roomId) != 1) {
                session.close(new CloseReason(
                    CloseReason.CloseCodes.VIOLATED_POLICY, "방 접근 권한 없음"));
                return;
            }
		}

        // 4️ roomId 기준으로 세션 등록
        roomSessions.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);
        sessionRoomMap.put(session, roomId);
        session.getUserProperties().put("ucode", ucode);
        System.out.println("유저 " + ucode + "가 방 " + roomId + "에 접속");
    }
    
    @OnMessage
    public void onMessage(String content, Session session) throws IOException {

        // 1) 이 세션이 어느 방인지, 누가 보냈는지 찾기
        Integer roomId = sessionRoomMap.get(session);
        Integer ucode  = (Integer)session.getUserProperties().get("ucode");
        String name = (String) session.getUserProperties().get("name");

        // 방/유저 정보가 없으면 안전하게 끊기
        if (roomId == null || ucode == null) {
            session.close(new CloseReason(
                CloseReason.CloseCodes.VIOLATED_POLICY, "세션 정보 없음"));
            return;
        }

        // 2) DB 저장 
        try (SqlSession sql = MyBatisUtil.getFactory().openSession(true)) {
            ChatMapper mapper = sql.getMapper(ChatMapper.class);
            mapper.insertMessage(roomId, ucode, content);
        } catch(Exception e) {
        	e.printStackTrace();
        	return;
        }

        // 3) 같은 방 세션들에게 브로드캐스트
        Set<Session> sessions = roomSessions.get(roomId);
        if (sessions == null) return;

        ChatMessage msg = new ChatMessage();
        msg.setRoomId(roomId);
        msg.setSenderUcode(ucode);
        msg.setSenderName(name);
        msg.setContent(content);
        
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