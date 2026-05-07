package com.siritori.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.siritori.ing.SiritoriIng;
import com.siritori.model.ResponseVO;
import com.siritori.model.SiritoriDAO;
import com.siritori.util.ConnUtil;
import com.google.gson.Gson;

@WebServlet("/shiritori")
public class SiritoriServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String word = request.getParameter("word");
        if (word != null) word = word.trim();
        
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        
        if (word == null || word.isEmpty()) return;

        HttpSession session = request.getSession();
        Long currentGameId = (Long) session.getAttribute("GAME_ID");
        String lastEndWord = (String) session.getAttribute("LAST_END_WORD");

        try (Connection con = ConnUtil.getConnection()) {
            
            SiritoriIng service = new SiritoriIng(con);
            SiritoriDAO dao = new SiritoriDAO(con);

            // 1. 게임 ID 세션 관리
            if (currentGameId == null) {
                currentGameId = dao.getNewGameId();
                session.setAttribute("GAME_ID", currentGameId);
            }

            // 2. 비즈니스 로직 수행
            ResponseVO result = service.play(word, currentGameId, lastEndWord);

            // 3. 세션 업데이트 (봇 응답 기반)
            if (result.getBotWord() != null && result.getBotWord().getEndWord() != null) {
                session.setAttribute("LAST_END_WORD", result.getBotWord().getEndWord());
            }

            // 4. JSON 출력
            out.print(gson.toJson(result));

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            out.close();
        }
    }
}