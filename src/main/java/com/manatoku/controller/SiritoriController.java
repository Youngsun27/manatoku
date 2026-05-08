package com.manatoku.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.manatoku.mapper.SiritoriMapper;
import com.manatoku.model.Siritori02;
import com.manatoku.service.SiritoriService;

@Controller
public class SiritoriController {

    @Autowired
    private SiritoriMapper mapper;

    @Autowired
    private SiritoriService service;

    @PostMapping("/siritori")
    @ResponseBody // 리턴되는 Siritori02 객체를 JSON으로 자동 변환
    public Siritori02 play(
            @RequestParam("word") String word,
            HttpSession session) { // 세션은 매개변수로 받으면 스프링이 넣어줌

        if (word == null || word.trim().isEmpty()) {
            return null; 
        }

        word = word.trim();
        Long currentGameId = (Long) session.getAttribute("GAME_ID");
        String lastEndWord = (String) session.getAttribute("LAST_END_WORD");

        // 1. 게임 ID 세션 관리
        if (currentGameId == null) {
            currentGameId = mapper.getNewGameId();
            session.setAttribute("GAME_ID", currentGameId);
        }

        // 2. 비즈니스 로직 수행
        Siritori02 result = service.play(word, currentGameId, lastEndWord);

        // 3. 세션 업데이트
        if (result.getBotWord() != null && result.getBotWord().getEndWord() != null) {
            session.setAttribute("LAST_END_WORD", result.getBotWord().getEndWord());
        }

        return result; // JSON 응답
    }
}