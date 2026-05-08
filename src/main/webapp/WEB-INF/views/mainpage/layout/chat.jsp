<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>

<div class="chat-wrapper"> <div class="galaxy-bg"></div>
	<div class="stars"></div><div class="shooting-star"></div>

	<!-- ⭐ 별똥별 컨테이너 추가 -->
	<%--	<div class="shooting-stars">
            <div class="shooting-star"></div>
            <div class="shooting-star"></div>
            &lt;%&ndash;<div class="shooting-star"></div>
             <div class="shooting-star"></div>
            <div class="shooting-star"></div>
            <div class="shooting-star"></div>
            <div class="shooting-star"></div>
            <div class="shooting-star"></div>&ndash;%&gt;
        </div>--%>

	<div class="chat-header">
		<div class="header-left">
			<!-- <button type="button" class="head-btn" id="memberAdd">그룹생성</button>
            <button type="button" class="head-btn" id="exitChat">나가기</button> -->
			<%-- <jsp:include page="starbutton.jsp"/> --%>
			<div class="chatArea-btn">
				<div id="friendButton" data-friend-ucode="" style="display: none;">
					<button type="button" id="friendDelButton" onclick="friendDel()">フレンド削除</button>
				</div>
				<div id="groupButton" data-room-id="" style="display: none;">
					<button type="button" id="groupExitButton" onclick="groupExit()">脱出</button>
					<button type="button" id="groupMemAddButton" onclick="groupMemAdd()">メンバー追加</button>
					<button type="button" id="groupMemberListButton" onclick="groupMemList()">参加者</button>
					<button type="button" id="groupRenameButton" onclick="groupRename()">グルーブ名</button>
				</div>
			</div>
		</div>
		<div class="header-center">
			<h3>マナトークチャット</h3>
		</div>
		<div class="header-right">
			<span class="status-dot"></span>
		</div>
	</div>

	<div id="chatMessageArea" class="chat-message-area">
		<div class="stars"></div>
		<div class="message-bubble system">
			<p>チャットローディング中</p>
		</div>
	</div>

	<div class="chat-input-area">
		<textarea id="chatInputArea" placeholder="メッセージを入力してください" rows="1"></textarea>
		<button type="button" id="chatSendBtn">入力</button>
	</div>
</div>




