package com.manatoku.service;

import java.util.List;
import com.manatoku.model.Calendar;

/*
 * 현재는 단순 CRUD라 CalendarMapper.java와 똑같아 보이지만,
 * 나중에 "일정 등록 시 포인트를 지급한다"거나 "삭제 시 로그를 남긴다"는 등의
 * 비즈니스 로직이 추가되면 Mapper와는 완전히 다른 코드가 됩니다.
 */
public interface CalendarService {

    // 일정 목록 조회
    List<Calendar> getEvents(int userCode);

    // 일정 등록
    int insertEvent(Calendar dto);

    // 일정 수정
    int updateEvent(Calendar dto);

    // 일정 삭제
    int deleteEvent(int calendar_id);

    // ✅ [추가] 단일 일정 조회 (권한 검증용)
    Calendar getEventById(int calendarId);
}