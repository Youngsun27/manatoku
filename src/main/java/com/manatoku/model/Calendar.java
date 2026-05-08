package com.manatoku.model;

import org.apache.ibatis.type.Alias;

public class Calendar {
    private int calendar_id;
    private int user_code;
    private String title;
    private String content;
    private String start;         // FullCalendar 전송용 (ISO 8601 형식)
    private String end;           // FullCalendar 전송용
    private String calendar_type; // 'WORK'(업무), 'PERSONAL'(개인), 'HOLIDAY'(공휴일)
    private String status;        // 'ACTIVE'(활성), 'DELETED'(삭제)
    private String created_at;    // [추가] 등록일자 (선택사항)

    // Getter & Setter (게터 및 세터)
    public int getCalendar_id() { return calendar_id; }
    public void setCalendar_id(int calendar_id) { this.calendar_id = calendar_id; }

    public int getUser_code() { return user_code; }
    public void setUser_code(int user_code) { this.user_code = user_code; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getStart() { return start; }
    public void setStart(String start) { this.start = start; }

    public String getEnd() { return end; }
    public void setEnd(String end) { this.end = end; }

    public String getCalendar_type() { return calendar_type; }
    public void setCalendar_type(String calendar_type) { this.calendar_type = calendar_type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }
}
