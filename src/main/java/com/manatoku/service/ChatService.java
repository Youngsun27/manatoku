package com.manatoku.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.manatoku.model.ChatRoomMember;
import com.manatoku.serviceModel.ServiceResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manatoku.mapper.ChatMapper;
import com.manatoku.model.ChatMessage;

@Service
public class ChatService {
	
	/* Mapper 의존성 주입 */
	private final ChatMapper chatMapper;
	public ChatService(ChatMapper chatMapper) {
		this.chatMapper = chatMapper;
	}

	private static final String API_KEY =
			"AIzaSyCN2bBD_jwREQAR_qOtQ9L0fjGm6Ll3K-8";
	
	/* 사용자가 채팅 참여자인지 검증 */
	@Transactional(readOnly = true)
	public int isUserInRoom(Integer ucode, Integer roomId) {
		int res = chatMapper.isUserInRoom(ucode, roomId);
		
		return res;
	}
	
	/* 사용자의 메세지 로그를 DB에 저장 */
	@Transactional
	public void insertMessage(Integer roomId, Integer ucode, String content) {
		chatMapper.insertMessage(roomId, ucode, content);
	}
	
	/* 채팅창에 채팅 로그를 조회 */
	@Transactional(readOnly = true)
	public List<ChatMessage> selectRecentMessages(int roomId, int limit) {
		List<ChatMessage> list = chatMapper.selectRecentMessages(roomId, limit);
		
		return list;
	}
	
	/* 친구 채팅 roomId 조회 */
	@Transactional(readOnly = true)
	public int findDirectRoomId(int ucode,int friendUcode) {
		Integer roomId = chatMapper.findDirectRoomId(ucode, friendUcode);
		
		if(roomId == null) { //조회 결과가 null이면,
			roomId = insertChatRoomDirect(ucode, friendUcode); // 채팅방을 생성해서 roomId를 리턴한다
		}
		
		return roomId;
	}
	
	/* 친구 채팅방 생성 */
	@Transactional
	public int insertChatRoomDirect(int ucode,int friendUcode) {
		
		Map<String,Object> p = new HashMap<>(); // 생성과 동시에 값을 리턴받기 위해 임시 Map 생성
		chatMapper.insertChatRoomDirect(p);
		int roomId = (int)p.get("roomId"); // Map에서 roomId를 키값으로 하는 값을 roomId에 넣는다
		/* roomId를 키값으로 채팅방에 멤버롤 넣는다 */
		chatMapper.insertRoomMember(roomId, ucode, "owner");
		chatMapper.insertRoomMember(roomId, friendUcode, "member");
		
		return roomId; // roomId 리턴
	}
	
	/* 그룹 채팅 생성 */
	@Transactional
	public boolean createGroup(int ucode, String uname, String[] friendUcodesString) {
		
		boolean res = false;
		int roomId;
		
		Map<String,Object> p = new HashMap<>(); // 생성과 동시에 값을 리턴받기 위해 임시 Map 생성
		chatMapper.createGroup(p);
		roomId = (int)p.get("roomId"); // Map에서 roomId를 키값으로 하는 값을 roomId에 넣는다
		chatMapper.insertRoomMember(roomId, ucode, "owner");
		
		
		int count=0;
		for(String friend : friendUcodesString) {
			int friendUcode = Integer.parseInt(friend);
			chatMapper.insertRoomMember(roomId, friendUcode, "member");
			count++;
		}
		
		chatMapper.setGroupTitle(uname+"様のほか"+count+"人様のチャット", roomId);
		
		res=true;
		
		return res;
	}

	/* 그룹 나가기 */
	@Transactional
	public ServiceResult<Void> exitGroup(int roomId, int ucode) {
			int count = chatMapper.groupMemberCount(roomId);
			if(count==1){

				chatMapper.deleteGroup(roomId);
				return ServiceResult.success();
			}
			chatMapper.exitGroup(roomId,ucode);

		return ServiceResult.success();
	}

	@Transactional
	public void groupRename(int roomId,String title){
		chatMapper.setGroupTitle(title, roomId);
	}

	/* 그룹 멤버 리스트 가져오기 */
	@Transactional(readOnly = true)
	public List<ChatRoomMember> getGroupMemList(int roomId){
		List<ChatRoomMember> list = chatMapper.getGroupMemList(roomId);

		return list;
	}

	@Transactional
	public ServiceResult<Void> groupMemAdd(int roomId,String[] friendUcodesString){

		for(String friend : friendUcodesString) {
			int friendUcode = Integer.parseInt(friend);
			chatMapper.insertRoomMember(roomId, friendUcode, "member");
		}

		return ServiceResult.success();

	}

	public String fetchOgData(String url) throws IOException {

		Document doc = Jsoup.connect(url)
				.userAgent(
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/110.0.0.0"
				)
				.timeout(5000)
				.get();

		String title = getMeta(doc, "og:title");
		if (title.isEmpty()) title = doc.title();

		String desc = getMeta(doc, "og:description");
		String image = getMeta(doc, "og:image");

		// 유튜브 특별 처리 (Servlet 그대로)
		if (url.contains("youtube.com") || url.contains("youtu.be")) {
			String vId = "";
			if (url.contains("v="))
				vId = url.split("v=")[1].split("&")[0];
			else if (url.contains("youtu.be/"))
				vId = url.split("youtu.be/")[1].split("\\?")[0];

			if (!vId.isEmpty())
				image = "https://img.youtube.com/vi/" + vId + "/mqdefault.jpg";
		}

		// JSON 문자열 직접 조립 (Servlet 그대로)
		return "{"
				+ "\"title\":\"" + clean(title) + "\","
				+ "\"desc\":\"" + clean(desc) + "\","
				+ "\"image\":\"" + clean(image) + "\""
				+ "}";
	}

	private String getMeta(Document doc, String attr) {
		Element tag = doc.select("meta[property=" + attr + "]").first();
		if (tag == null)
			tag = doc.select("meta[name=" + attr + "]").first();
		return (tag != null) ? tag.attr("content") : "";
	}

	private String clean(String str) {
		if (str == null) return "";
		return str.replace("\\", "\\\\")
				.replace("\"", "\\\"")
				.replace("\n", " ")
				.replace("\r", "");
	}

	public String translate(String text, String targetLang) throws IOException {

		URL url = new URL(
				"https://translation.googleapis.com/language/translate/v2?key=" + API_KEY
		);

		HttpURLConnection conn =
				(HttpURLConnection) url.openConnection();

		conn.setRequestMethod("POST");
		conn.setRequestProperty(
				"Content-Type", "application/json; charset=UTF-8"
		);
		conn.setDoOutput(true);

		JsonObject payload = new JsonObject();
		payload.addProperty("q", text);
		payload.addProperty("target", targetLang);

		try (OutputStream os = conn.getOutputStream()) {
			os.write(payload.toString().getBytes(StandardCharsets.UTF_8));
		}

		BufferedReader br = new BufferedReader(
				new InputStreamReader(
						conn.getInputStream(), StandardCharsets.UTF_8
				)
		);

		JsonObject response =
				JsonParser.parseReader(br).getAsJsonObject();

		return response.getAsJsonObject("data")
				.getAsJsonArray("translations")
				.get(0).getAsJsonObject()
				.get("translatedText")
				.getAsString();
	}

}
