<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
session.removeAttribute("GAME_ID");
session.removeAttribute("LAST_END_WORD");
%>
<%-- <link rel="stylesheet" href="${pageContext.request.contextPath}/css/chatbot.css"> --%>

        <div id="mainLayout" class="main-layout">
                <div id="gameBoard" class="game-board">
                        <div id="characterArea" class="character-area">
                                <div class="scene">
                                        <img src="${pageContext.request.contextPath}/img/JO.png" alt="" class="person">
                                                <div id="chatBubble" class="chat-bubble"><div id="chatDisplay" class="chat-display"></div></div>
                                </div>
                        </div>
                        <div id="meaningArea" class="meaning-area">
                                <div class="meaning-area-in">
                                        <div class="meaning-dp">
                                                <strong id="userTitle"></strong>
                                                <div id="userMeaningContent" class="meaning-dp-div"></div>
                                        </div>
                                </div>
                                <div class="meaning-area-in">
                                        <div class="meaning-dp">
                                                <strong id="botTitle"></strong>
                                                <div id="botMeaningContent" class="meaning-dp-div"></div>
                                        </div>
                                </div>
                        </div>
                        <div id="inputArea" class="input-area">
                                <input type="text" id="chatInput" class="chat-input" onkeypress="if(event.keyCode==13) sendMessage();">
                                <input type="button" value="入力" id="sendButton" class="button-one" onclick="sendMessage()">
                                <input type="button" value="単語履歴を見る" class="button-two" onclick="toggleLayout()">
                        </div>
                </div>
                <div id="extraColumn" class="extra-column">
                        <div class="meaning-dp">
                                <strong id="historyWord" class="history-word">単語履歴</strong>
                                <div id="extraContent" class="meaning-dp-div"></div>
                        </div>
                </div>
        </div>
        <div id="wordTooltip" class="meaning-dp-tooltip">
                <strong id="tooltipTitle" class="meaning-dp-tooltip-title"></strong>
                <div id="tooltipContent" class="meaning-dp-div-tooltip"></div>
        </div>
<%--         <script src="${pageContext.request.contextPath}/js/chatbot.js"></script> --%>
