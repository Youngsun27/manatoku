package web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;

import com.google.gson.Gson;
import model.Friends;

import dao.ChatMapper;
import model.ChatMessage;
import util.MyBatisUtil;

@WebServlet("/chat/friends")
public class GetFriendChatSevlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Gson gson = new Gson();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
	request.setCharacterEncoding("utf-8");
	response.setContentType("application/json; charset=UTF-8");

	Integer myUcode = (Integer)request.getSession().getAttribute("ucode");
		if (myUcode == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("{\"ok\":false,\"error\":\"LOGIN_REQUIRED\"}");
			return;
		}

	Integer friendUcode = Integer.parseInt(request.getParameter("friendUcode"));

	try (SqlSession session = MyBatisUtil.getFactory().openSession(false)) {
		ChatMapper mapper = session.getMapper(ChatMapper.class);

		Integer roomId = mapper.findDirectRoomId(myUcode, friendUcode);

		if (roomId == null) {
			Map<String,Object> p = new HashMap<>();
			mapper.insertChatRoomDirect(p);
			roomId = (Integer)p.get("roomId");
			mapper.insertRoomMember(roomId, myUcode, "owner");
			mapper.insertRoomMember(roomId, friendUcode, "member");

			session.commit();
		}
		
		List<ChatMessage> msgs = mapper.selectRecentMessages(roomId, 50);

		for (ChatMessage m : msgs) {
			m.setMine(m.getSenderUcode() == myUcode.intValue());
			}

		
		Friends friend = mapper.selectUserSummary(friendUcode);

		Map<String, Object> out = new HashMap<>();
		out.put("ok", true);
		out.put("roomId", roomId);
		out.put("friend", friend);
		out.put("messages", msgs);

		response.getWriter().write(gson.toJson(out));

	
	    }
	  }
	}