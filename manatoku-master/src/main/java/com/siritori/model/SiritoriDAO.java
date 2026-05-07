package com.siritori.model;

import java.sql.*;

public class SiritoriDAO {
    private Connection con;

    public SiritoriDAO(Connection con) {
        this.con = con;
    }

    // 새로운 게임 세션을 위한 시퀀스 번호 생성
    public long getNewGameId() {
        String sql = "SELECT game_seq.NEXTVAL FROM DUAL";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {return rs.getLong(1);}
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return -1;
    }

    // 사용자가 입력한 요미(읽기)로 단어 정보 검색
    public WordVO getWordByYomi(String yomi) throws SQLException {
        String sql = "SELECT WORD_ID, END_WORD, YOMI, CASE WHEN TRIM(YOMI) = TRIM(WORD) THEN CAST('' AS NVARCHAR2(10)) ELSE WORD END AS WORD FROM DICTIONARY WHERE TRIM(YOMI) = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, yomi);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    WordVO vo = new WordVO();
                    vo.setWordId(rs.getInt("WORD_ID"));
                    vo.setYomi(rs.getString("YOMI"));
                    vo.setWord(rs.getString("WORD"));
                    vo.setEndWord(rs.getString("END_WORD"));
                    return vo;
                }
            }
        }
        return null;
    }

    // 특정 단어의 모든 뜻을 가져와 HTML 형식(<br>)으로 결합
    public String getWordMeanings(int wordId) throws SQLException {
        StringBuilder meanings = new StringBuilder();
        int displayNum = 1; 
        
        String sql = "SELECT MEANING FROM DICTIONARY_MEANING WHERE WORD_ID = ? ORDER BY MEANING_ORDER ASC";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, wordId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    if (meanings.length() > 0) meanings.append("<br>");
                    
                    String formattedNum = String.format("%02d", displayNum++);
                    
                    meanings.append(formattedNum)
                            .append(". ")
                            .append(rs.getString("MEANING"));
                }
            }
        }
        return meanings.toString();
    }

    // 현재 게임에서 해당 단어가 이미 사용되었는지 확인
    public boolean isDuplicate(long gameId, int wordId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM DICTIONARY_GAME WHERE GAME_ID = ? AND WORD_ID = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setLong(1, gameId);
            pstmt.setInt(2, wordId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    // 사용된 단어를 게임 기록 테이블에 저장
    public void insertGameWord(long gameId, int wordId) throws SQLException {
        String sql = "INSERT INTO DICTIONARY_GAME (GAME_ID, WORD_ID) VALUES (?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setLong(1, gameId);
            pstmt.setInt(2, wordId);
            pstmt.executeUpdate();
        }
    }

    // 규칙에 맞는 봇의 응답 단어를 랜덤하게 하나 선택
    public WordVO getRandomBotWord(String startWord, long gameId) throws SQLException {
        String sql = "SELECT WORD_ID, YOMI, WORD, END_WORD FROM (" +
                     "  SELECT d.WORD_ID, d.YOMI, d.END_WORD, " +
                     "  CASE WHEN TRIM(d.YOMI) = TRIM(d.WORD) THEN CAST('' AS NVARCHAR2(10)) ELSE d.WORD END AS WORD " +
                     "  FROM DICTIONARY d WHERE d.START_WORD = ? AND d.END_WORD != 'ん' " +
                     "  AND d.WORD_ID NOT IN (SELECT WORD_ID FROM DICTIONARY_GAME WHERE GAME_ID = ?) " +
                     "  ORDER BY DBMS_RANDOM.VALUE " +
                     ") WHERE ROWNUM = 1";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, startWord);
            pstmt.setLong(2, gameId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    WordVO vo = new WordVO();
                    vo.setWordId(rs.getInt("WORD_ID"));
                    vo.setYomi(rs.getString("YOMI"));
                    vo.setWord(rs.getString("WORD"));
                    vo.setEndWord(rs.getString("END_WORD"));
                    return vo;
                }
            }
        }
        return null;
    }
}