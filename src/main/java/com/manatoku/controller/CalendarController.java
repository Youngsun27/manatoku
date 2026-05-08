package com.manatoku.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.manatoku.model.MemberResponse;
import com.manatoku.service.CalendarService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.manatoku.model.Calendar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Controller
@RequestMapping(value = "/cal.do", produces = "application/json; charset=UTF-8")
public class CalendarController {

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    // --- GET 처리: 리스트 조회 및 페이지 이동 ---
    @GetMapping(produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Object handleGet(HttpServletRequest request, HttpSession session) {
        String command = request.getParameter("command");

        // 세션 검증
        MemberResponse member = (MemberResponse) session.getAttribute("member");
        if (member == null) {
            System.out.println("❌ 세션 없음: 로그인 필요");
            return "[]";
        }

        // 사용자 정보 로그
        System.out.println("✅ 캘린더 조회 - 사용자: " + member.getUcode() + " (" + member.getName() + ")");

        // 리스트 조회
        if (command == null || "list".equals(command)) {
            return getListData(session);
        }

        return "[]";
    }

    // JSON 리스트 생성
    private String getListData(HttpSession session) {
        MemberResponse member = (MemberResponse) session.getAttribute("member");
        int userCode = member.getUcode();

        // ucode 유효성 검증
        if (userCode <= 0) {
            System.out.println("❌ 유효하지 않은 ucode: " + userCode);
            return "[]";
        }

        List<Calendar> events = calendarService.getEvents(userCode);

        JsonArray jsonArray = new JsonArray();
        for (Calendar dto : events) {
            JsonObject obj = new JsonObject();

            obj.addProperty("id", dto.getCalendar_id());
            obj.addProperty("calendar_id", dto.getCalendar_id());
            obj.addProperty("title", dto.getTitle());
            obj.addProperty("start", dto.getStart());
            obj.addProperty("end", dto.getEnd());

            JsonObject extendedProps = new JsonObject();
            extendedProps.addProperty("ucode", dto.getUser_code());
            extendedProps.addProperty("content", dto.getContent());
            extendedProps.addProperty("calendar_type", dto.getCalendar_type());
            obj.add("extendedProps", extendedProps);

            jsonArray.add(obj);
        }
        return jsonArray.toString();
    }

    // --- POST 처리: 등록, 수정, 삭제 ---
    @PostMapping(produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String handlePost(HttpServletRequest request) {
        String command = request.getParameter("command");
        int result = 0;

        // 세션 검증
        HttpSession session = request.getSession();
        MemberResponse member = (MemberResponse) session.getAttribute("member");

        if (member == null) {
            System.out.println("❌ POST 요청 거부: 세션 없음");
            return "0";
        }

        int sessionUcode = member.getUcode();
        System.out.println("✅ POST 요청 - 사용자: " + sessionUcode + " (" + member.getName() + "), 명령: " + command);

        try {
            // 1. 등록 (Insert)
            if ("insert".equals(command)) {
                int ucode = sessionUcode;

                // 파라미터 추출
                String title = request.getParameter("title");
                String content = request.getParameter("content");
                String start = request.getParameter("start");
                String end = request.getParameter("end");

                Calendar dto = new Calendar();
                dto.setUser_code(ucode);
                dto.setTitle(title);
                dto.setContent(content);
                dto.setStart(start);
                dto.setEnd(end);
                dto.setCalendar_type("PERSONAL");

                result = calendarService.insertEvent(dto);
            }
            // 2. 수정 (Update)
            else if ("update".equals(command)) {
                // 파라미터 추출
                String calendarIdStr = request.getParameter("calendarId");
                String title = request.getParameter("title");
                String content = request.getParameter("content");
                String start = request.getParameter("start");
                String end = request.getParameter("end");

                // 필수값 검증
                if (calendarIdStr == null || calendarIdStr.trim().isEmpty()) {
                    System.out.println("❌ 수정 실패: 일정 ID 없음");
                    return "0";
                }
                if (title == null || title.trim().isEmpty()) {
                    System.out.println("❌ 수정 실패: 제목 없음");
                    return "0";
                }

                int calendarId = Integer.parseInt(calendarIdStr.trim());
                System.out.println("🔍 수정 시도 ID: " + calendarId);


                // 권한 검증
                Calendar existingEvent = calendarService.getEventById(calendarId);

                if (existingEvent == null) {
                    System.out.println("❌ 수정 실패: 존재하지 않는 일정");
                    System.out.println("   - 일정 ID: " + calendarId);
                    return "0";
                }

                if (existingEvent.getUser_code() != sessionUcode) {
                    System.out.println("❌ 수정 실패: 권한 없음!");
                    System.out.println("   - 일정 소유자: " + existingEvent.getUser_code());
                    System.out.println("   - 요청자: " + sessionUcode);
                    System.out.println("   - IP: " + request.getRemoteAddr());
                    return "0";
                }

                // DTO 생성 및 데이터 설정
                Calendar dto = new Calendar();
                dto.setCalendar_id(calendarId);
                dto.setTitle(title);
                dto.setContent(content);
                dto.setStart(start);
                dto.setEnd(end);

                result = calendarService.updateEvent(dto);
                System.out.println("일정 수정 ID: " + calendarIdStr + ", 결과: " + result);
            }
            // 3. 삭제 (Delete)
            else if ("delete".equals(command)) {
                String calendarIdStr = request.getParameter("calendarId");

                // 필수값 검증
                if (calendarIdStr == null || calendarIdStr.trim().isEmpty()) {
                    System.out.println("❌ 삭제 실패: 일정 ID 없음");
                    return "0";
                }

                int calendarId = Integer.parseInt(calendarIdStr.trim());

                // 권한 검증
                Calendar existingEvent = calendarService.getEventById(calendarId);

                if (existingEvent == null) {
                    System.out.println("❌ 삭제 실패: 존재하지 않는 일정");
                    System.out.println("   - 일정 ID: " + calendarId);
                    return "0";
                }

                if (existingEvent.getUser_code() != sessionUcode) {
                    System.out.println("❌ 삭제 실패: 권한 없음!");
                    System.out.println("   - 일정 소유자: " + existingEvent.getUser_code());
                    System.out.println("   - 요청자: " + sessionUcode);
                    System.out.println("   - IP: " + request.getRemoteAddr());
                    return "0";
                }

                result = calendarService.deleteEvent(calendarId);
            }
            else {
                System.out.println("❌ 알 수 없는 명령: " + command);
                return "0";
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ 숫자 형식 오류: " + e.getMessage());
            e.printStackTrace();
            result = 0;
        } catch (Exception e) {
            System.out.println("❌ 예외 발생: " + e.getMessage());
            e.printStackTrace();
            result = 0;
        }

        String response = String.valueOf(result);
        System.out.println("📤 응답: " + response);

        return response;
    }
}