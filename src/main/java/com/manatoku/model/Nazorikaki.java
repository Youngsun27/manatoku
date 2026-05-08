package com.manatoku.model;

import org.apache.ibatis.type.Alias;

@Alias("Nazorikaki")
public class Nazorikaki {
    private String content;
    private String yomikun;
    private String exam;
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getYomikun() { return yomikun; }
    public void setYomikun(String yomikun) { this.yomikun = yomikun; }
    
    public String getExam() { return exam; }
    public void setExam(String exam) { this.exam = exam; }
}