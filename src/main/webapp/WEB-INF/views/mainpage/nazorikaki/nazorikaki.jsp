<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="grade-selector">
	<button type="button" onclick="loadNazorikakiGrade(1)">第1学年</button>
	<button type="button" onclick="loadNazorikakiGrade(2)">第2学年</button>
	<button type="button" onclick="loadNazorikakiGrade(3)">第3学年</button>
	<button type="button" onclick="loadNazorikakiGrade(4)">第4学年</button>
	<button type="button" onclick="loadNazorikakiGrade(5)">第5学年</button>
	<button type="button" onclick="loadNazorikakiGrade(6)">第6学年</button>
	<button type="button" onclick="loadNazorikakiGrade(7)">常用漢字</button>
	<button type="button" onclick="loadNazorikakiGrade('')">全部</button>
</div>

<div id="NazorikakiContentArea">
	<c:choose>
		<c:when test="${empty nazorikakiList}">
			<div class="welcome-container">
				<h2>日本語漢字学習</h2>
				<p>上の学年ボタンを押して学習を始めてください</p>
			</div>
		</c:when>

		<c:otherwise>
			<div class="main-layout-nazorikaki">
			
				<div class="learning-area">
					<div class="nazorikaki-info-card">
						<div class="score-container">
							Score: <span id="current-score">100</span>
						</div>
						<div class="yomikun">${nazorikakiList[0].yomikun}</div>
						<div class="exam">${nazorikakiList[0].exam}</div>
						<div id="nazorikaki-target"></div>
					</div>
					
					<div class="btn-group">
						<button class="toggle-btn" onclick="toggleAnswer()">正解を表示 / 非表示</button>
						<button onclick="animateNazorikaki()">書き順を見る</button>
						<button onclick="startQuiz()">なぞり書き</button>
						<button onclick="loadNazorikakiGrade('${grade}')">次の漢字へ</button>
					</div>
				</div>

				<div class="history-sidebar">
					<h3 style="margin-top: 0;">学習記録</h3>
					<div id="history-list">
						<p class="empty-msg">まだ記録がありません。</p>
					</div>
				</div>				
			</div>

			<script>
				// 1. 전역 변수에 현재 한자 저장
				window.targetNazorikaki = "${nazorikakiList[0].content}";

				// 2. JS 파일에 정의된 초기화 함수 호출
				if (typeof initNazorikaki === 'function') {
					initNazorikaki(window.targetNazorikaki);
				}
			</script>
		</c:otherwise>
	</c:choose>
</div>