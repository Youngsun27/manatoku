package com.manatoku.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manatoku.mapper.SiritoriMapper;
import com.manatoku.model.Siritori01;
import com.manatoku.model.Siritori02;

@Service
public class SiritoriService {
	
	@Autowired
    private SiritoriMapper mapper;

    public Siritori02 play(String userYomi, Long gameId, String lastEndWord) {
        Siritori02 response = new Siritori02();
        
        // 1. 끝말잇기 규칙 검사
        if (lastEndWord != null && !lastEndWord.isEmpty()) {
            String currentStart = userYomi.substring(0, 1);
            if (lastEndWord.equals("ん") || !lastEndWord.equals(currentStart)) {
                String status = lastEndWord.equals("ん") ? "END_N" : "RULE_ERROR";
                return fillCharacterResponse(response, status, lastEndWord, "");
            }
        }

        // 2. 사용자 단어 존재 여부 조회
        Siritori01 userVO = mapper.getWordByYomi(userYomi);
        if (userVO == null) {
            return fillCharacterResponse(response, "NOT_FOUND", userYomi, "");
        }
        
        List<String> meaningList = mapper.getWordMeanings(userVO.getWordId());
        
        if (meaningList != null && !meaningList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < meaningList.size(); i++) {
                if (i > 0) sb.append("<br>");
                sb.append(String.format("%02d. ", i + 1)).append(meaningList.get(i));
            }
            userVO.setMeaning(sb.toString()); 
        }
        
        response.setUserWord(userVO);

        // 3. 'ん' 종료 검사
        if ("ん".equals(userVO.getEndWord())) {
            return fillCharacterResponse(response, "END_N", userYomi, "");
        }

        // 4. 중복 단어 검사
        if (mapper.isDuplicate(gameId, userVO.getWordId()) > 0) {
            return fillCharacterResponse(response, "DUP_ERROR", "", "");
        }        

        mapper.insertGameWord(gameId, userVO.getWordId());

        // 5. 봇 응답
        Siritori01 botVO = mapper.getRandomBotWord(userVO.getEndWord(), gameId);
        
        if (botVO != null) {
            List<String> botMeanings = mapper.getWordMeanings(botVO.getWordId());
            
            if (botMeanings != null && !botMeanings.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < botMeanings.size(); i++) {
                    if (i > 0) sb.append("<br>");
                    sb.append(String.format("%02d. ", i + 1)).append(botMeanings.get(i));
                }
                botVO.setMeaning(sb.toString());
            }

            mapper.insertGameWord(gameId, botVO.getWordId());
            response.setBotWord(botVO);
            return fillCharacterResponse(response, "NORMAL", userYomi, botVO.getYomi());
        } else {
            return fillCharacterResponse(response, "WIN", userYomi, "");
        }
    }

    private Siritori02 fillCharacterResponse(Siritori02 res, String status, String word, String botYomi) {
        int dice = (int) (Math.random() * 100) + 1;
        int charId = (dice <= 25) ? 1 : (dice <= 50) ? 2 : (dice <= 75) ? 3 : 4;
        res.setStatus(status);

        switch (charId) {
            case 1:
                res.setCharImg("DK.png");
                res.setCharName("生意気な高校生");
                if (status.equals("NORMAL")) res.setCharComment("はぁ？「" + word + "」だと？…へっ、悪くねえな。<br>次は「" + botYomi + "」だ、かかってこいよ！");
                else if (status.equals("WIN")) res.setCharComment("チッ…マジかよ。「" + word + "」なんて反則だろ。<br>今回だけは勝ちを譲ってやるよ。");
                else if (status.equals("NOT_FOUND")) res.setCharComment("あ？悪いな、俺の頭に<br>「" + word + "」なんて言葉、ねんだよ！");
                else if (status.equals("RULE_ERROR")) res.setCharComment("おいおい、ルールも分かんねーのか？<br>「" + word + "」から始まる言葉を言えよ、ボケが！");
                else if (status.equals("DUP_ERROR")) res.setCharComment("さっき聞いたっつーの！<br>てめぇ、脳みそついてんのか？");
                else if (status.equals("END_N")) res.setCharComment("ヒャハハ！「ん」で終わっちまったな！<br>お前の負けだ、ザコー！");
                break;

            case 2:
                res.setCharImg("JK.png");
                res.setCharName("名門女子高生");
                if (status.equals("NORMAL")) res.setCharComment("あら、「" + word + "」ですのね。よろしいですわ、<br>わたくしの答えは「" + botYomi + "」ですわ。");
                else if (status.equals("WIN")) res.setCharComment("お見事ですわ。このわたくしが敗北するなんて…<br>あなたの勝ちを認めますわ。");
                else if (status.equals("NOT_FOUND")) res.setCharComment("おほほ、「" + word + "」なんて単語、聞いたこともございませんわ。<br>出鱈目はおやめになって？");
                else if (status.equals("RULE_ERROR")) res.setCharComment("はしたないですわよ。「" + word + "」から<br>始まる言葉を仰ってくださいませ。");
                else if (status.equals("DUP_ERROR")) res.setCharComment("あら、二度手間ですわ。同じ言葉を繰り返すなんて、<br>わたくしへの侮辱かしら？");
                else if (status.equals("END_N")) res.setCharComment("あらら…「ん」で終わってしまいましたのね。<br>残念ですけれど、あなたの負けですわ。");
                break;

            case 3:
                res.setCharImg("DO.png");
                res.setCharName("偏屈お爺ちゃん");
                if (status.equals("NORMAL")) res.setCharComment("ほう、「" + word + "」か！なかなかやりおる。<br>ならわしは「" + botYomi + "」といこうかのう！");
                else if (status.equals("WIN")) res.setCharComment("なっ、なんじゃと…そんな言葉を繰り出してくるとは…。<br>わしの完敗じゃわい。");
                else if (status.equals("NOT_FOUND")) res.setCharComment("喝！「" + word + "」などという単語、<br>このわしは聞いたことがない単語じゃ！");
                else if (status.equals("RULE_ERROR")) res.setCharComment("これ、なっとらんぞ！「" + word + "」<br>から始めるのが筋というものじゃろうが！");
                else if (status.equals("DUP_ERROR")) res.setCharComment("お主、それはさっきも出た言葉だぞ。<br>若いくせにボケておるのか？ん？");
                else if (status.equals("END_N")) res.setCharComment("カッカッカ！「ん」で終わるとは<br>修行が足りんわい！お主の負けじゃ！");
                break;

            case 4:
                res.setCharImg("JO.png");
                res.setCharName("上品な老婦人");
                if (status.equals("NORMAL")) res.setCharComment("ふふ、「" + word + "」ですね。素敵な語彙をお持ちね。<br>私は「" + botYomi + "」にしましょう。");
                else if (status.equals("WIN")) res.setCharComment("まいりましたわ. まさか私が負けるなんて…<br>あなたの知識量には脱帽です。 ");
                else if (status.equals("NOT_FOUND")) res.setCharComment("あらあら、「" + word + "」なんて単語は、<br>私にはわからない単語ですよ。");
                else if (status.equals("RULE_ERROR")) res.setCharComment("落ち着きなさいな。「" + word + "」から<br>始めるのがこの遊びの約束でしょう？");
                else if (status.equals("DUP_ERROR")) res.setCharComment("一度伺ったお話を何度も繰り返すのは、<br>あまり品が良いとは言えませんわね。");
                else if (status.equals("END_N")) res.setCharComment("まあまあ、「ん」で終わってしまいましたね。<br>ふふ、今回は私の勝ちのようね。");
                break;
        }
        return res;
    }
}