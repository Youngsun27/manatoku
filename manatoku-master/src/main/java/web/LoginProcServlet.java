package web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;

import dao.MemberMapper;
import model.Member;
import util.MyBatisUtil;

@WebServlet("/user/loginProc")
public class LoginProcServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		
		try (SqlSession session = MyBatisUtil.getFactory().openSession()) {
            MemberMapper mapper = session.getMapper(MemberMapper.class);
            
            int res = mapper.checkId(id);
            
            if(res<1) {
            	request.setAttribute("id", id);
            	request.setAttribute("error", "IDが正しくありません。");
    			request.getRequestDispatcher("/user/login.jsp").forward(request, response);
    			
    			return;
            }
            
            Member m = mapper.selectMemberById(id);
            
            String userPw = m.getPass();
            
            if(!pw.equals(userPw)) {
            	request.setAttribute("id", id);
            	request.setAttribute("error", "パスワードが正しくありません。");
    			request.getRequestDispatcher("/user/login.jsp").forward(request, response);
            	
            	return;
            }
            
            request.getSession().setAttribute("ucode", m.getUcode());
            request.getSession().setAttribute("loginId", id);
            request.getSession().setAttribute("name", m.getName());
            request.getSession().setAttribute("icon", m.getIcon());
            
            response.sendRedirect(request.getContextPath() + "/main/mainpage.jsp");
            
            return;
            
		}catch(Exception e) {
			request.setAttribute("id", id);
        	request.setAttribute("error", "ログインが失敗しました。");
			request.getRequestDispatcher("/user/login.jsp").forward(request, response);
        	
        	return;
			
		}
		
		
	}

}
