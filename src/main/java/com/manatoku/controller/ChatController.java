package com.manatoku.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.manatoku.model.ChatRoomMember;
import com.manatoku.serviceModel.ServiceResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.manatoku.model.ChatMessage;
import com.manatoku.model.MemberResponse;
import com.manatoku.service.ChatService;

@Controller
public class ChatController {
	
	private final ChatService chatService;
	public ChatController(ChatService chatService) {
		this.chatService = chatService;
	}
	
	/* 채팅 로그 조회 */
	@GetMapping("/chat/logs")
	@ResponseBody
	public List<ChatMessage> loadChatLogs(int roomId) {
		List<ChatMessage> list = chatService.selectRecentMessages(roomId, 50);
		
		return list;
	}
	
	/* 친구 채팅 정보 조회 */
	@PostMapping("/chat/getFriendChat")
	@ResponseBody
	public int getFriendChat(@RequestParam int friendUcode, HttpSession session) {
		
		MemberResponse member = null;
		int ucode;
		int roomId;
		/* 사용자 ucode 가져오기 */
		if(session != null) { // session 정보가 있을 경우
			member = (MemberResponse)session.getAttribute("member");
			ucode = member.getUcode();
		} else { return -1; } // session 정보가 없은 경우
		
		roomId = chatService.findDirectRoomId(ucode, friendUcode);
		
		return roomId;
	}
	
	@PostMapping("/chat/createGroup")
	@ResponseBody
	public boolean createGroup(@RequestParam String[] friendUcodes,HttpSession session) {
		boolean res = false;
		MemberResponse member = null;
		int ucode;
		String uname;
		if(session != null) { // session 정보가 있을 경우
			member = (MemberResponse)session.getAttribute("member");
			ucode = member.getUcode();
			uname = member.getName();
		} else { return res; } // session 정보가 없은 경우
		
		if(friendUcodes == null) {return res;} // 친구를 선택하지 않았을 경우
		
		res = chatService.createGroup(ucode,uname,friendUcodes);
		
		return res;
	}

	@PostMapping("/chat/groupMemAdd")
	@ResponseBody
	public ServiceResult<Void> groupMemAdd(@RequestParam String[] memberUcodes,@RequestParam int roomId, HttpSession session) {
		MemberResponse member = null;
		int ucode;
		String uname;
		if(session != null) { // session 정보가 있을 경우
			member = (MemberResponse)session.getAttribute("member");
		} else { return ServiceResult.fail("login failed"); } // session 정보가 없은 경우

		if(member == null) {
			return ServiceResult.fail("login failed");
		}

		if(memberUcodes == null) {return ServiceResult.fail("フレンドを選択してください。");} // 친구를 선택하지 않았을 경우

		ServiceResult res = chatService.groupMemAdd(roomId,memberUcodes);

		return res;
	}
	
	@GetMapping("/chat/groupAdd")
	public String groupAdd() {
		return "/mainpage/groupList/groupUserAdd";
	}

	@GetMapping("/chat/groupMember")
	public String groupMember(@RequestParam int roomId,Model model) {
		model.addAttribute(roomId);
		return "/mainpage/groupList/groupMemList";
	}

	@GetMapping("/chat/memberAdd")
	public String memberAdd(@RequestParam int roomId,Model model) {
		model.addAttribute(roomId);
		return "/mainpage/groupList/groupMemAdd";
	}

	@PostMapping("/chat/groupRename")
	@ResponseBody
	public ServiceResult<Void> groupRename(@RequestParam int roomId,@RequestParam String title){

		chatService.groupRename(roomId,title);

		return ServiceResult.success();
	}

	@PostMapping("/chat/groupMemList")
	@ResponseBody
	public List<ChatRoomMember> groupMemList(@RequestParam int roomId,HttpSession session) {
		MemberResponse member=null;
		if(session != null){
			member = (MemberResponse) session.getAttribute("member");
		}
		else {
			return null;
		}

		List<ChatRoomMember> list = chatService.getGroupMemList(roomId);

		return list;
	}

	@PostMapping("/chat/exitGroup")
	@ResponseBody
	public ServiceResult<Void> exitGroup(@RequestParam int roomId,HttpSession session) {

		MemberResponse member;

		if(session != null) {
			member = (MemberResponse)session.getAttribute("member");
		}
		else {
			return ServiceResult.fail("Login Error");
		}

		int ucode = member.getUcode();

		ServiceResult res = chatService.exitGroup(roomId,ucode);

		return res;
	}


	@GetMapping(value ="/chat/getOgData", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getOgData(@RequestParam String url) {

		try {
			String jsonResponse = chatService.fetchOgData(url);
			return jsonResponse;
		} catch (Exception e) {
			return "{\"error\":\"fail\"}";
		}
	}
	@PostMapping(value = "/translate", consumes = "application/json", produces = "application/json; charset=UTF-8"
	)
	public void translate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		JsonObject body = JsonParser.parseReader(req.getReader()).getAsJsonObject();

		String text = body.get("text").getAsString();
		String targetLang = body.get("targetLang").getAsString();

		// 🔽 여기서 Service 호출 (로직 이동)
		String translated = chatService.translate(text, targetLang);

		// ✅ Servlet과 동일한 비교
		JsonObject result = new JsonObject();
		if (translated.equals(text)) {
			result.addProperty("translatedText", text);
			resp.getWriter().write(result.toString());
			return;
		}
		result.addProperty("translatedText", translated);
		resp.getWriter().write(result.toString());
	}

}
