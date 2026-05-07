package com.app;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/cal.do")
public class CalendarServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CalendarDAO dao = new CalendarDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String command = request.getParameter("command");

        if ("list".equals(command)) {
            response.setContentType("application/json; charset=UTF-8");
            
            // 🔥 [수정] 현재 로그인한 사용자의 정보를 세션에서 가져오거나 임시로 1 설정
            // DAO의 getEvents(userCode)에 맞춰 매개변수를 전달합니다.
            int userCode = (int)request.getSession().getAttribute("ucode");
            List<CalendarDTO> events = dao.getEvents(userCode);
            
            StringBuilder json = new StringBuilder();
            json.append("[");
            for (int i = 0; i < events.size(); i++) {
                CalendarDTO e = events.get(i);
                
                // [참고] DB에서 null로 올 수 있는 값들에 대한 방어 코드
                String content = (e.getContent() == null) ? "" : e.getContent();
                String type = (e.getCalendar_type() == null) ? "PERSONAL" : e.getCalendar_type();
                
                json.append("{")
                	.append("\"ucode\":\"").append(e.getUser_code()).append("\",")
                    .append("\"id\":\"").append(e.getCalendar_id()).append("\",")
                    .append("\"title\":\"").append(e.getTitle()).append("\",")
                    .append("\"start\":\"").append(e.getStart()).append("\",")
                    .append("\"end\":\"").append(e.getEnd()).append("\",")
                    .append("\"extendedProps\": { ")
                    .append("\"content\":\"").append(content).append("\",")
                    .append("\"calendar_type\":\"").append(type).append("\"")
                    .append(" }")
                    .append("}");
                if (i != events.size() - 1) json.append(",");
            }
            json.append("]");
            response.getWriter().print(json.toString());
        } else {
            response.sendRedirect("/manatoku/main/layout/calendar/calendar.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String command = request.getParameter("command");
        int result = 0;

        if ("insert".equals(command)) {
            String start = request.getParameter("start");
            String end = request.getParameter("end");
            int ucode = Integer.parseInt(request.getParameter("ucode"));

            if (dao.isOverlap(ucode,start, end)) {
                result = -1; 
            } else {
                CalendarDTO dto = new CalendarDTO();
                dto.setUser_code(ucode);
                dto.setTitle(request.getParameter("title"));
                dto.setContent(request.getParameter("content"));
                dto.setStart(start);
                dto.setEnd(end);
                dto.setCalendar_type("PERSONAL"); 
                result = dao.insertEvent(dto);
            }
        } 
        else if ("update".equals(command)) {
            String idStr = request.getParameter("id");
            if(idStr != null && !idStr.isEmpty()) {
                CalendarDTO dto = new CalendarDTO();
                dto.setCalendar_id(Integer.parseInt(idStr));
                dto.setTitle(request.getParameter("title"));
                dto.setContent(request.getParameter("content"));
                dto.setStart(request.getParameter("start"));
                dto.setEnd(request.getParameter("end"));
                result = dao.updateEvent(dto);
            }
        } 
        else if ("delete".equals(command)) {
            String idStr = request.getParameter("id");
            if(idStr != null && !idStr.isEmpty()) {
                result = dao.deleteEvent(Integer.parseInt(idStr));
            }
        }

        response.getWriter().print(result);
    }
}