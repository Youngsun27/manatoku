package com.app;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;

import dao.MemberMapper;
import util.MyBatisUtil;

//.do로 끝나는 모든 요청, 즉, 모든 사용자 관련 액션(로그아웃, 수정, 탈퇴)을 한 곳에서 처리
@WebServlet("*.do")
public class UserActionServlet extends HttpServlet {
 private static final long serialVersionUID = 1L;

 // GET 방식 요청 처리 (로그아웃 등)
 protected void doGet(HttpServletRequest request, HttpServletResponse response) 
         throws ServletException, IOException {
     process(request, response);
 }

 // POST 방식 요청 처리 (정보 수정, 회원 탈퇴 등)
 protected void doPost(HttpServletRequest request, HttpServletResponse response) 
         throws ServletException, IOException {
     process(request, response);
 }

 // 공통 로직 처리 메서드
 private void process(HttpServletRequest request, HttpServletResponse response) 
         throws ServletException, IOException {
     
     // 1. 어떤 경로로 들어왔는지 분석
     String uri = request.getRequestURI();
     String path = uri.substring(uri.lastIndexOf("/"));
     
     HttpSession session = request.getSession(false); // 기존 세션이 없으면 null 반환
     
     request.setCharacterEncoding("utf-8");
     response.setContentType("application/json; charset=UTF-8");
     
     // 2. 경로(Path)에 따른 분기 처리
     if (path.equals("/logout.do")) {
         // [로그아웃]
         if (session != null) {
             session.invalidate();
         }
         response.sendRedirect("/manatoku/user/login.jsp");

     } else if (path.equals("/updateUser.do")) {
         // [프로필 수정]
         String name = request.getParameter("name");
         try (SqlSession sql = MyBatisUtil.getFactory().openSession(false)) {
             MemberMapper mapper = sql.getMapper(MemberMapper.class);
             int ucode = (int)session.getAttribute("ucode");
             mapper.updateName(ucode, name);
         } catch(Exception e) {
        	 e.printStackTrace();
         }
         session.setAttribute("name", name); // 세션 정보도 갱신
         System.out.println(name + "으로 정보 수정 완료");
         response.sendRedirect("mypage.jsp");

     } else if (path.equals("/withdraw.do")) {
         // [회원 탈퇴]
         
         
         
         try (SqlSession sql = MyBatisUtil.getFactory().openSession(false)) {
             MemberMapper mapper = sql.getMapper(MemberMapper.class);
             
             int ucode = (int)session.getAttribute("ucode");
             String userPw = mapper.getPassWithUcode(ucode).trim();
             String inputPw = request.getParameter("pw").trim();
             if(inputPw.equals(userPw)) {
            	 mapper.deleteMember(ucode);
            	 sql.commit();
            	 session.invalidate(); // 탈퇴 후 세션 종료
            	 request.getSession().setAttribute("flashMsg", "退会が完了しました。");
            	 response.sendRedirect("/manatoku/user/login.jsp");
            	 System.out.println("회원 탈퇴 완료");
            	 return;
             } else {
            	request.getSession().setAttribute("flashMsg", "パスワードが正しくありません。");
                response.sendRedirect(request.getContextPath() + "/main/layout/mypage/withdrawForm.jsp");
             	return;
             }
         } catch(Exception e) {
        	e.printStackTrace();
        	request.getSession().setAttribute("flashMsg", "退会が失敗しました。");
            response.sendRedirect(request.getContextPath() + "/main/layout/mypage/withdrawForm.jsp");
          	return;
         }
     }
 }
}