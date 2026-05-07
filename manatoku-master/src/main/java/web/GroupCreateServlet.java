package web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;

import dao.ChatMapper;
import util.MyBatisUtil;

@WebServlet("/chat/createGroup")
public class GroupCreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8"); // 한글/일어 깨짐 방지
		
		HttpSession session = request.getSession(false);
		
		if (session == null || session.getAttribute("ucode") == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
		
		String[] friendUcodesString;
		
		int ucode = (int)session.getAttribute("ucode");
		String userName = (String)session.getAttribute("name");
		
		friendUcodesString = request.getParameterValues("friendUcodes");
		
		if (friendUcodesString == null || friendUcodesString.length == 0) {
            // 친구를 선택하지 않았을 때 처리
            return; 
        }

		try (SqlSession sql = MyBatisUtil.getFactory().openSession(false)) {
            ChatMapper mapper = sql.getMapper(ChatMapper.class);
            
            Map<String,Object> p = new HashMap<>();
            
            mapper.createGroup(p);
            int groupId = (int)p.get("roomId");
            
            mapper.insertRoomMember(groupId, ucode, "owner");
            mapper.setGroupTitle(userName+" グループです。", groupId);

            for (String fStr : friendUcodesString) {
                int friendUcode = Integer.parseInt(fStr);
                mapper.insertRoomMember(groupId, friendUcode, "member"); // 친구는 보통 member로
            }
            
            sql.commit();
            
            response.getWriter().print("<script>alert('Success!'); window.opener.location.reload(); window.close();</script>");
		} catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
	}

}
