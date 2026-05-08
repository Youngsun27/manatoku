package com.manatoku.mapper;

import java.util.List;

import com.manatoku.model.Today;

public interface TodayMapper {
    List<Today> getDailyKanjiList();
    List<String> getMeaningsByKanjiId(Long id);
}