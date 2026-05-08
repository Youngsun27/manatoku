package com.manatoku.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.manatoku.model.ChatRoom;
import com.manatoku.model.FriendInfo;
import com.manatoku.model.MemberResponse;
import com.manatoku.service.ApiService;

/* 컨트롤러 의존성 주입 */
@Controller
@RequestMapping("/api")
public class ApiController {
	
	/* 서비스 의존성 주입 */
	private final ApiService apiService;
	public ApiController(ApiService apiService) {
		this.apiService = apiService;
	}
	
	/* 사용자의 친구 목록 불러오기 */
	/* @ResponseBody => json으로 변환해서 리턴 */
	@GetMapping("/friends")
	@ResponseBody
	public List<FriendInfo> friendsList(HttpSession session) { 
		
		/* 사용자 정보를 가져오기 위해 파라미터를 Member 객체로 변환 */
		MemberResponse member = (MemberResponse)session.getAttribute("member");
		
		/* session 정보가 없으면 리턴 */
		if(member == null) {
			return null;
		}
		
		/* Session에서 사용자 코드 불러오기 */
		int ucode = member.getUcode();
		
		/* 사용자 코드로 친구목록 조회 */
		List<FriendInfo> list = apiService.getFriendList(ucode);
		
		/* 결과 List 리턴 */
		return list;
	}
	
	/* 사용자의 그룹채팅 목록 불러오기 */
	@GetMapping("/groups")
	@ResponseBody
	public List<ChatRoom> groupsList(HttpSession session) {
		
		/* 사용자 정보를 가져오기 위해 파라미터를 Member 객체로 변환 */
		MemberResponse member = (MemberResponse)session.getAttribute("member");
		
		/* session 정보가 없으면 리턴 */
		if(member == null) {
			return null;
		}
		
		/* Session에서 사용자 코드 불러오기 */
		int ucode = member.getUcode();
		
		/* 사용자 코드로 그룹채팅 목록 조회 */
		List<ChatRoom> list = apiService.getGroupList(ucode);
		
		/* 결과 List 리턴 */
		return list;
	}

	@GetMapping("/groupMemAddList")
	@ResponseBody
	public List<FriendInfo> groupMemberAdd(@RequestParam int roomId, HttpSession session) {

		/* 사용자 정보를 가져오기 위해 파라미터를 Member 객체로 변환 */
		MemberResponse member = (MemberResponse)session.getAttribute("member");

		/* session 정보가 없으면 리턴 */
		if(member == null) {
			return null;
		}

		int ucode = member.getUcode();

		/* 사용자 코드로 친구목록 조회 */
		List<FriendInfo> list = apiService.groupMemAddList(roomId,ucode);

		/* 결과 List 리턴 */
		return list;
	}
}
