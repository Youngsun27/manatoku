package com.manatoku.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.manatoku.model.Calendar;

@Mapper
public interface CalendarMapper {

    // 1. 일정 조회 (parameterType="int" 대응)
    List<Calendar> getEvents(@Param("userCode") int userCode);

    // 2. 일정 등록 (parameterType="CalendarDTO" 대응)
    int insertEvent(Calendar dto);

    // 3. 일정 수정 (parameterType="CalendarDTO" 대응)
    int updateEvent(Calendar dto);

    // 4. 일정 삭제 (parameterType="int" 대응)
    int deleteEvent(@Param("calendar_id") int calendarId);
    // ✅ [추가] ID로 단일 일정 조회
    Calendar getEventById(@Param("calendarId") int calendarId);
}
