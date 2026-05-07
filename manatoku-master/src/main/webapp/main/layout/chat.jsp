<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="chat-wrapper"> <div class="galaxy-bg"></div>
    <div class="stars"></div><div class="shooting-star"></div>
	<div class="chat-header">
		<div class="header-left">
			<!-- <button type="button" class="head-btn" id="memberAdd">그룹생성</button>
            <button type="button" class="head-btn" id="exitChat">나가기</button> -->
			<%-- <jsp:include page="starbutton.jsp"/> --%>
			<button type="button" class="theme-btn" onclick="toggleTheme()">
				<span id="theme-icon">✨</span> <span id="theme-text">銀河テーマに切り替え</span>
			</button>
		</div>
		<div class="header-center">
			<h3>Manatoku 실시간 채팅</h3>
		</div>
		<div class="header-right">
			<span class="status-dot"></span>
		</div>
	</div>

	<div id="chatMessageArea" class="chat-message-area">
		<div class="stars"></div>
		<div class="message-bubble system">
			<p>채팅방에 연결되었습니다.</p>
		</div>
	</div>

	<div class="chat-input-area">
		<textarea id="chatInputArea" placeholder="メッセージを入力してください" rows="1"></textarea>
		<button type="button" id="chatSendBtn">入力</button>
	</div>
</div>
