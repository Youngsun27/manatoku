package com.siritori.model;

public class WordVO {
    private int wordId;
    private String yomi;
    private String word;
    private String endWord;
    private String meaning;

    public WordVO() {}

    public int getWordId() { return wordId; }
    public void setWordId(int wordId) { this.wordId = wordId; }

    public String getYomi() { return yomi; }
    public void setYomi(String yomi) { this.yomi = yomi; }

    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public String getEndWord() { return endWord; }
    public void setEndWord(String endWord) { this.endWord = endWord; }

    public String getMeaning() { return meaning; }
    public void setMeaning(String meaning) { this.meaning = meaning; }
}