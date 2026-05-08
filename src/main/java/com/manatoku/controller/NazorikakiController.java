package com.manatoku.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.manatoku.mapper.NazorikakiMapper;
import com.manatoku.model.Nazorikaki;

/* 컨트롤러 의존성 주입 */
@Controller
public class NazorikakiController {
	
	/* 스프링이 객체 관리하게 권한 넘기기 (IOC) */
    @Autowired
    private NazorikakiMapper mapper;
    
    /* 다른 곳에서 이 컨트롤러를 호출 하면 이곳으로 안내해 주는 것 */
    @GetMapping("/nazorikaki/loadGrade")
    public String loadGrade(
            @RequestParam(value = "grade", required = false) String gradeParam, 
            Model model) {
    	
        List<Nazorikaki> list = null;
        boolean isMain = false;

        // 1. 기존 로직 유지 (학년별 데이터 조회)
        if (gradeParam == null || gradeParam.equals("undefined")) {
            isMain = true;
        } else if (gradeParam.isEmpty()) {
            list = mapper.getAllTexts();
        } else {
            try {
                int grade = Integer.parseInt(gradeParam);
                list = mapper.getNazorikakisByGrade(grade);
            } catch (NumberFormatException e) {
                isMain = true;
            }
        }

        // 2. 데이터 처리
        if (list != null && !list.isEmpty()) {
            Collections.shuffle(list);
            model.addAttribute("nazorikakiList", list);
            model.addAttribute("grade", gradeParam); // 
        }

        model.addAttribute("isMain", isMain);
        
        // 3. 리턴 값 수정
        return "mainpage/nazorikaki/nazorikaki"; 
    }
}