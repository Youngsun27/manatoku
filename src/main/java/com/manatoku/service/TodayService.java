package com.manatoku.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manatoku.mapper.TodayMapper;
import com.manatoku.model.Today;

@Service
public class TodayService {

    private TodayMapper todayMapper;
    public TodayService(TodayMapper todayMapper){
        this.todayMapper = todayMapper;
    }

    // 오늘 학습할 한자 리스트를 담는 그릇
    private List<Today> dailyList;

    // 현재 사용자가 보고 있는 한자의 순서
    private int currentIndex = 0;

    @PostConstruct
    public void init() {
        loadDailyKanji();
    }

    /**
     * 서비스가 시작될 때 또는 필요할 때 10개의 한자와 그 뜻들을 DB에서 가져와 세팅합니다.
     */
    public void loadDailyKanji() {
        // 1. 오늘의 한자 10개 기본 정보 가져오기
        this.dailyList = todayMapper.getDailyKanjiList();

        // 2. 각 한자마다 대응하는 뜻(meanings) 리스트를 따로 채워넣기
        for (Today kanji : dailyList) {
            List<String> meanings = todayMapper.getMeaningsByKanjiId(kanji.getId());
            kanji.setMeanings(meanings);
        }

        // 인덱스 초기화
        this.currentIndex = 0;
    }

    // [다음] 버튼 로직: 마지막이면 0으로 이동
    public Today getNextKanji() {
        if (currentIndex >= dailyList.size() - 1) {
            currentIndex = 0;
        } else {
            currentIndex++;
        }
        return dailyList.get(currentIndex);
    }

    // [이전] 버튼 로직: 처음이면 마지막으로 이동
    public Today getPrevKanji() {
        if (currentIndex <= 0) {
            currentIndex = dailyList.size() - 1;
        } else {
            currentIndex--;
        }
        return dailyList.get(currentIndex);
    }

    // 처음 진입했을 때 보여줄 한자
    public Today getCurrentKanji() {
        return dailyList.get(currentIndex);
    }
}