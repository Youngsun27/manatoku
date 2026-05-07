package com.siritori.model;

public class ResponseVO {
    private String status;
    private WordVO userWord;
    private WordVO botWord;
    private String charImg;
    private String charComment;
    private String charName;

    public ResponseVO() {
        this.userWord = new WordVO();
        this.botWord = new WordVO();
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public WordVO getUserWord() { return userWord; }
    public void setUserWord(WordVO userWord) { this.userWord = userWord; }

    public WordVO getBotWord() { return botWord; }
    public void setBotWord(WordVO botWord) { this.botWord = botWord; }

    public String getCharImg() { return charImg; }
    public void setCharImg(String charImg) { this.charImg = charImg; }

    public String getCharComment() { return charComment; }
    public void setCharComment(String charComment) { this.charComment = charComment; }
    
    public String getCharName() { return charName; }
    public void setCharName(String charName) { this.charName = charName; }
}