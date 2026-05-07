<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>銀河チャットルーム</title>
</head>
<body>

    <div id="chatWindow" class="chat-window">

        <div class="galaxy-bg"></div>
        <div class="stars"></div>

        <div class="chat-header">
            <button type="button" class="theme-btn" onclick="toggleTheme()">
                <span id="theme-icon">✨</span>
                <span id="theme-text">銀河テーマに切り替え</span>
            </button>
        </div>

        <div class="chat-content" id="messageArea">
            <p>こんにちは！基本テーマです。</p>
            <p class="me">上段のボタンを押すと、銀河テーマに変わります！</p>
        </div>

        <div class="input-area">
            <input type="text" id="msg-in" placeholder="メッセージを入力してください...">
            <button type="button" id="send" onclick="sendMessage()">送信</button>
        </div>
	</div>
   
  
</body>
</html>