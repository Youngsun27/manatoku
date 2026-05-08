package com.manatoku.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.manatoku.model.Nazorikaki;

@Mapper
public interface NazorikakiMapper {
    List<Nazorikaki> getAllTexts();
    List<Nazorikaki> getNazorikakisByGrade(int grade);
}
