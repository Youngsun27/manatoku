package com.manatoku.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import com.manatoku.model.Siritori01;

import java.util.List;

@Mapper
public interface SiritoriMapper {
    long getNewGameId();
    
    Siritori01 getWordByYomi(String yomi);
    
    List<String> getWordMeanings(int wordId);
    
    int isDuplicate(@Param("gameId") long gameId, @Param("wordId") int wordId);
    
    void insertGameWord(@Param("gameId") long gameId, @Param("wordId") int wordId);
    
    Siritori01 getRandomBotWord(@Param("startWord") String startWord, @Param("gameId") long gameId);
}