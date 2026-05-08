package com.manatoku.model;

import java.util.List;

public class Today {
    private Long id; // 한자와 뜻을 매칭하기 위한 키값
    private String kanji; // 한자 값
    private int grade; // 혹시 몰라 남겨둔 학년 값
    private List<String> meanings; // 뜻 목록을 담을 리스트

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getKanji() {
        return kanji;
    }
    public void setKanji(String kanji) {
        this.kanji = kanji;
    }
    public int getGrade() {
        return grade;
    }
    public void setGrade(int grade) {
        this.grade = grade;
    }
    public List<String> getMeanings() {
        return meanings;
    }
    public void setMeanings(List<String> meanings) {
        this.meanings = meanings;
    }


}