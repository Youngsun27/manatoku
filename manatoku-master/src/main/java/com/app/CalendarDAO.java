 package com.app;

import java.sql.*;
import java.util.*;

public class CalendarDAO {

    // DB 연결 공통 메서드
    private Connection getConnection() throws Exception {
        return DBManager.getConnection();
    }

    /**
     * 1. 일정 목록 조회 (FullCalendar 전송용)
     * 가상 뷰(v_active_calendar)를 조회합니다.
     * @param userCode 현재 로그인한 사용자의 코드
     */
    public List<CalendarDTO> getEvents(int userCode) {
        List<CalendarDTO> list = new ArrayList<>();
        
        // [중요] 사용자의 개인 일정(userCode)과 뷰에서 생성된 가상 공휴일(user_code=0)을 모두 조회합니다.
        String sql = "SELECT * FROM v_active_calendar " +
                     "WHERE user_code = ? OR user_code = 0 " +
                     "ORDER BY start_datetime ASC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userCode);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    CalendarDTO dto = new CalendarDTO();
                    dto.setCalendar_id(rs.getInt("calendar_id"));
                    dto.setTitle(rs.getString("title"));
                    dto.setContent(rs.getString("content"));
                    dto.setCalendar_type(rs.getString("calendar_type"));
                    dto.setUser_code(rs.getInt("user_code"));
                    
                    // Timestamp를 ISO 8601 형식(YYYY-MM-DDTHH:mm:ss)으로 변환
                    if (rs.getTimestamp("start_datetime") != null) {
                        dto.setStart(rs.getTimestamp("start_datetime").toLocalDateTime().toString());
                    }
                    if (rs.getTimestamp("end_datetime") != null) {
                        dto.setEnd(rs.getTimestamp("end_datetime").toLocalDateTime().toString());
                    }
                    
                    list.add(dto);
                }
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    /**
     * 2. 일정 중복 체크
     * 공휴일(HOLIDAY)과는 겹쳐도 되므로 calendar_type != 'HOLIDAY' 조건을 추가했습니다.
     */
    public boolean isOverlap(int userCode, String start, String end) {
		/*
		 * boolean overlap = false; String sql =
		 * "SELECT COUNT(*) FROM v_active_calendar " + "WHERE user_code = ? " +
		 * "AND calendar_type != 'HOLIDAY' " +
		 * "AND start_datetime < TO_TIMESTAMP(?, 'YYYY-MM-DD\"T\"HH24:MI:SS') " +
		 * "AND end_datetime > TO_TIMESTAMP(?, 'YYYY-MM-DD\"T\"HH24:MI:SS')";
		 * 
		 * try (Connection conn = getConnection(); PreparedStatement pstmt =
		 * conn.prepareStatement(sql)) {
		 * 
		 * pstmt.setInt(1, userCode); pstmt.setString(2, end); pstmt.setString(3,
		 * start);
		 * 
		 * try (ResultSet rs = pstmt.executeQuery()) { if (rs.next() && rs.getInt(1) >
		 * 0) { overlap = true; } } } catch (Exception e) { e.printStackTrace(); }
		 */
        return false;
    }

    /**
     * 3. 일정 추가 (Create)
     */
    public int insertEvent(CalendarDTO dto) {
        int result = 0;
        String sql = "INSERT INTO calendar (calendar_id, user_code, title, content, start_datetime, end_datetime, calendar_type, status) " +
                     "VALUES (seq_calendar_id.NEXTVAL, ?, ?, ?, " +
                     "TO_TIMESTAMP(?, 'YYYY-MM-DD\"T\"HH24:MI:SS'), " +
                     "TO_TIMESTAMP(?, 'YYYY-MM-DD\"T\"HH24:MI:SS'), ?, 'ACTIVE')";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, dto.getUser_code());
            pstmt.setString(2, dto.getTitle());
            pstmt.setString(3, dto.getContent());
            pstmt.setString(4, dto.getStart()); 
            pstmt.setString(5, dto.getEnd());
            pstmt.setString(6, dto.getCalendar_type() == null ? "PERSONAL" : dto.getCalendar_type());
            
            result = pstmt.executeUpdate();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return result;
    }

    /**
     * 4. 일정 수정 (Update)
     */
    public int updateEvent(CalendarDTO dto) {
        int result = 0;
        String sql = "UPDATE calendar SET title = ?, content = ?, " +
                     "start_datetime = TO_TIMESTAMP(?, 'YYYY-MM-DD\"T\"HH24:MI:SS'), " +
                     "end_datetime = TO_TIMESTAMP(?, 'YYYY-MM-DD\"T\"HH24:MI:SS'), " +
                     "updated_at = CURRENT_TIMESTAMP " +
                     "WHERE calendar_id = ? AND status = 'ACTIVE'";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, dto.getTitle());
            pstmt.setString(2, dto.getContent());
            pstmt.setString(3, dto.getStart());
            pstmt.setString(4, dto.getEnd());
            pstmt.setInt(5, dto.getCalendar_id());
            
            result = pstmt.executeUpdate();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return result;
    }

    /**
     * 5. 일정 삭제 (Soft Delete)
     */
    public int deleteEvent(int calendarId) {
        int result = 0;
        String sql = "UPDATE calendar SET status = 'DELETED', deleted_at = CURRENT_TIMESTAMP " +
                     "WHERE calendar_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, calendarId);
            result = pstmt.executeUpdate();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return result;
    }
}