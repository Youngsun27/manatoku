package web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;

import com.google.gson.Gson;

import dao.ChatMapper;
import model.ChatRoom;
import util.MyBatisUtil;

@WebServlet("/api/groups")
public class GetGroupListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Gson gson = new Gson();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession(false);
		
		if (session == null) {
		      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		      return;
		    }

		Integer ucode = (Integer)session.getAttribute("ucode");
		
		if (ucode == null) {
		      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		      return;
		    }
		
		try (SqlSession sql = MyBatisUtil.getFactory().openSession()) {
            ChatMapper mapper = sql.getMapper(ChatMapper.class);
            
            List<ChatRoom> list = mapper.findRoom(ucode);
            
    		response.setContentType("application/json; charset=utf-8");
    	    response.getWriter().write(gson.toJson(list));
		}
	}
}
