package chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;

import com.google.gson.Gson;

import dao.ChatMapper;
import model.ChatMessage;
import util.MyBatisUtil;

@WebServlet("/chat/logs")
public class GetChatLogSevlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Gson gson = new Gson();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String roomIdStr = request.getParameter("roomId");
		if(roomIdStr == null)
			return;
		
		int roomId = Integer.parseInt(roomIdStr);
		try (SqlSession sql = MyBatisUtil.getFactory().openSession()) {
            ChatMapper mapper = sql.getMapper(ChatMapper.class);
            List<ChatMessage> list = mapper.selectRecentMessages(roomId, 50);
            
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(gson.toJson(list));
		}
	}

}
