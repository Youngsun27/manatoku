package com.manatoku.model;

import org.apache.ibatis.type.Alias;

@Alias("Siritori02")
public class Siritori02 {
    private String status;
    private Siritori01 userWord;
    private Siritori01 botWord;
    private String charImg;
    private String charComment;
    private String charName;

    public Siritori02() {
        this.userWord = new Siritori01();
        this.botWord = new Siritori01();
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Siritori01 getUserWord() { return userWord; }
    public void setUserWord(Siritori01 userWord) { this.userWord = userWord; }

    public Siritori01 getBotWord() { return botWord; }
    public void setBotWord(Siritori01 botWord) { this.botWord = botWord; }

    public String getCharImg() { return charImg; }
    public void setCharImg(String charImg) { this.charImg = charImg; }

    public String getCharComment() { return charComment; }
    public void setCharComment(String charComment) { this.charComment = charComment; }
    
    public String getCharName() { return charName; }
    public void setCharName(String charName) { this.charName = charName; }
}