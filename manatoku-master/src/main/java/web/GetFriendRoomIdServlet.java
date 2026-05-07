package web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;

import com.google.gson.Gson;

import dao.ChatMapper;
import util.MyBatisUtil;

@WebServlet("/api/getRoom")
public class GetFriendRoomIdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Gson gson = new Gson();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Integer ucode = (Integer)request.getSession().getAttribute("ucode");
		
		if (ucode == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("{\"ok\":false,\"error\":\"LOGIN_REQUIRED\"}");
			return;
		}

	Integer friendUcode = Integer.parseInt(request.getParameter("friendUcode"));
	try (SqlSession session = MyBatisUtil.getFactory().openSession(false)) {
		ChatMapper mapper = session.getMapper(ChatMapper.class);
		
		Integer roomId = mapper.findDirectRoomId(ucode, friendUcode);
			if (roomId == null) {
				Map<String,Object> p = new HashMap<>();
				mapper.insertChatRoomDirect(p);
				roomId = (Integer)p.get("roomId");
				mapper.insertRoomMember(roomId, ucode, "owner");
				mapper.insertRoomMember(roomId, friendUcode, "member");

				session.commit();
			}
			
			response.setContentType("application/json; charset=utf-8");
		    response.getWriter().write(gson.toJson(roomId));
		}
	
	}

}
