package com.manatoku.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.manatoku.model.Calendar;
import com.manatoku.mapper.CalendarMapper;

@Service
public class CalendarServiceImpl implements CalendarService {

    private final CalendarMapper mapper; // DAO 역할을 하는 매퍼 주입
    public CalendarServiceImpl(CalendarMapper mapper){
        this.mapper = mapper;
    }

    @Override
    public List<Calendar> getEvents(int userCode) {
        // 기존 서블릿 로직: MyBatis를 통해 데이터 조회
        return mapper.getEvents(userCode);
    }

    @Override
    public int insertEvent(Calendar dto) {
        // 기존 서블릿 로직: 일정 등록
        return mapper.insertEvent(dto);
    }

    @Override
    public int updateEvent(Calendar dto) {
        // 기존 서블릿 로직: 일정 수정
        return mapper.updateEvent(dto);
    }

    @Override
    public int deleteEvent(int calendar_id) {
        // 기존 서블릿 로직: 일정 삭제
        return mapper.deleteEvent(calendar_id);
    }

    @Override
    public Calendar getEventById(int calendarId) {
        return mapper.getEventById(calendarId);
    }
}