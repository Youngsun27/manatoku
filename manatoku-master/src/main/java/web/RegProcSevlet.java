package web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;

import dao.MemberMapper;
import model.Member;
import util.MyBatisUtil;

@WebServlet("/user/regProc")
public class RegProcSevlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		Member m = new Member();
		
		m.setEmail(request.getParameter("email"));
		m.setId(request.getParameter("id"));
		m.setName(request.getParameter("name"));
		m.setPass(request.getParameter("passwd"));
		String birth = request.getParameter("birth1")+"-"+request.getParameter("birth2")+"-"+request.getParameter("birth3");
		m.setBirth(birth);
		String phone = request.getParameter("phone1")+"-"+request.getParameter("phone2")+"-"+request.getParameter("phone3");
		m.setPhone(phone);
		
		try (SqlSession session = MyBatisUtil.getFactory().openSession(false)) { //DB 연결 autocommit: false;
            MemberMapper mapper = session.getMapper(MemberMapper.class);
            
            int resId = mapper.checkId(m.getId());
            if(resId > 0) { // id 중복확인 count(*) 쿼리문 실행 결과값이 0보다 클때
            	m.setPass("");
            	request.setAttribute("member", m);
    			request.setAttribute("error", "このIDは登録できません。");
    			request.getRequestDispatcher("/user/regForm.jsp").forward(request, response);
    			
    			return;
            }
            
            int resEmail = mapper.checkEmail(m.getEmail());
            
            if(resEmail > 0) { // email 중복확인 count(*) 쿼리문 실행 결과값이 0보다 클때
            	m.setPass("");
    			request.setAttribute("member", m);
    			request.setAttribute("error", "このメールアドレスは登録できません。");
    			request.getRequestDispatcher("/user/regForm.jsp").forward(request, response);
    			
    			return;
    		}
            
            mapper.insertMember(m);
            session.commit();
            request.getSession().setAttribute("flashMsg", "登録が完了しました。");
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            
		}catch (Exception e) {
			e.printStackTrace();
			m.setPass("");
            request.setAttribute("member", m);
            request.setAttribute("error", "アカウントの作成に失敗しました。");
            request.getRequestDispatcher("/user/regForm.jsp").forward(request, response);
            return;
        }
		
	}

}
