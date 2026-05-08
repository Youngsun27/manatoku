package com.manatoku.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.manatoku.model.MemberResponse;
import com.manatoku.serviceModel.ServiceResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.manatoku.model.Friends;
import com.manatoku.model.Member;
import com.manatoku.service.FriendsService;

@Controller
@RequestMapping("/friends")
public class FriendsController {

    /* 의존성 주입 */
    private final FriendsService friendsService;
    public FriendsController(FriendsService friendsService) {
        this.friendsService = friendsService;
    }

    /* 1. 친구 검색 페이지 */
    @GetMapping("/search")
    public String searchPage(HttpSession session) {
        if (getLoginMember(session) == null) {
            return "friendspopclose";
        }
        return "/mainpage/friend/friendAdd";
    }



    /* 2. 친구 ID 검색 */
    @PostMapping("/search")
    public String search(@RequestParam("id") String id, HttpSession session, Model model) {
        MemberResponse member;
        /* 로그인 되어있는지 확인 */
        if(session != null) {
            member = getLoginMember(session);
        }
        else {
            return "friendspopclose";
        }

        /* FreindsService-searchMemberById 메소드에 파라미터 id 입력하여 검색 및 list로 저장  */
        model.addAttribute("list", friendsService.searchMemberById(id));
        model.addAttribute("keyword", id);

        return "/mainpage/friend/friendAdd";
    }

    /*  3. 친구 신청 */
    @PostMapping("/request")
    public String request(@RequestParam("receiverUcode") int receiverUcode, HttpSession session, RedirectAttributes ra) {

        /* 로그인 되어있는지 확인 */
        MemberResponse member;
        if(session != null) {
            member = getLoginMember(session);
        }
        else {
            return "friendspopclose";
        }

        /* 세션에 저장된 ucode(로그인 사용자)를 보내는 사람으로 저장 */
        int senderUcode = member.getUcode();

        /* 보내는 사람 = 받는사람 > 신청불가 메시지(사실 신청 자체를 못하게 막아둬서 필요없긴 함) */
        if (senderUcode == receiverUcode) {
            ra.addFlashAttribute("flashMsg", "自分には友達申請が出来ません");
            return "redirect:/friends/search";
        }

        /* false: 이미 친구/친구신청중/거절 상태 // true: 친구신청 가능 */
        boolean result = friendsService.sendFriendRequest(senderUcode, receiverUcode);

        /* true면 친구신청완료, false면 친구신청 불가 */
        ra.addFlashAttribute("flashMsg", result ? "友達申請完了" : "既に友達であるか、または承認待ちです");

        return "redirect:/friends/search";
    }

    /* 4. 받은 친구 신청 목록 */
    @GetMapping("/receive")
    public String receive(HttpSession session, Model model) {

        /* 로그인 되어있는지 확인 */
        MemberResponse member;
        if(session != null) {
            member = getLoginMember(session);
        }
        else {
            return "friendspopclose";
        }

        /* 사용자 ucode를 세션에 받아서 받은 신청 메소드로 넘겨서 list에 저장 */
        List<Friends> list = friendsService.getReceiveRequest(member.getUcode());

        model.addAttribute("list", list);
        return "/mainpage/friend/friendSelect";
    }



    /*  5. 친구 신청 수락 / 거절 */
    @PostMapping("/process")
    public String process(@RequestParam("fcode") int fcode, @RequestParam("action") String action, HttpSession session, Model model) {

        /* 로그인 되어있는지 확인 */
        MemberResponse member;
        if(session != null) {
            member = getLoginMember(session);
        }else {
            return "friendspopclose";
        }

        /* 승인 버튼 또는 거절 버튼 외의 것을 눌렀다면? 다시 받은 신청 화면 칸으로 */
        if (!"ACCEPT".equals(action) && !"REJECT".equals(action)) {
            return "redirect:/friends/receive";
        }

        /* 친구코드, 사용자코드, 승인/거절 버튼(action)값을 업데이트 메소드에 입력*/
        friendsService.updateFriendStatus(fcode, member.getUcode(), action);

        /* 메시지를 띄우는게 아니라 굳이 써야할지 모르겠는 코드*/
        model.addAttribute("msg", "ACCEPT".equals(action) ? "友達になりました。" : "拒絶しました。");
        model.addAttribute("url", "/friends/receive");

        return "friendspopclose";
    }


    /* 6. 친구 삭제 - 버튼만 만들면 됨*/
    @PostMapping("/delete")
    @ResponseBody
    public ServiceResult<Void> delete(@RequestParam("friendUcode") int friendUcode, HttpSession session) {

        /* 로그인 되어있는지 확인 */
        MemberResponse member = null;
        if (session != null) {
            member = getLoginMember(session);
        } else {
            return ServiceResult.fail("Login Error");
        }

        int myUcode = member.getUcode();

        System.out.println(myUcode);

        if (myUcode == friendUcode) {
            return ServiceResult.fail("Wrong request");
        }

        friendsService.deleteFriend(myUcode, friendUcode);

        return ServiceResult.success();
    }


    /*  7. 공통 로그인 체크 메서드 */
    private MemberResponse getLoginMember(HttpSession session) {
        return (MemberResponse) session.getAttribute("member");
    }
}