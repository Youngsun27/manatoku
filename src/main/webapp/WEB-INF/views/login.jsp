<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>マナトーク ログイン</title>
	<link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet" type="text/css">
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>

<!-- 알림 메세지 출력 -->
<c:if test="${not empty flashMsg}">
	<!-- flashMsg라는 이름의 파라미터가 비어 있지 않다면 아래 동작 수행 -->
	<script>
		alert("${flashMsg}");
		/* flashMsg안의 텍스트를 경고창으로 띄움 */
	</script>
	<!-- flashMsg를 세션에서 삭제 -->
	<c:remove var="flashMsg" scope="session"/>
</c:if>

<!-- 페이지 시작 -->
<div class="login-wrapper">

	<!-- 별똥별 -->
	<div class="stars"></div>

	<div class="shooting-stars">
		<span class="shooting-star"></span> <span class="shooting-star"></span>
		<span class="shooting-star"></span> <span class="shooting-star"></span>
		<span class="shooting-star"></span> <span class="shooting-star"></span>
	</div>

	<div class="cards-container">

		<!-- 로그인 카드 -->
		<div class="login-card">
			<div class="login-title">マナトーク</div>

			<div>
				<input class="login-input" type="text" id="id" placeholder="ID">
				<input class="login-input" type="password" id="pw" placeholder="パスワード">
				<input class="login-btn" type="button" onclick="inputLoginCheck()" value="ログイン">
				<input class="join-btn" type="button" value="会員登録" onclick="location.href='/user/reg'">
				<input class="credits-btn" type="button" value="クレジット" onclick="location.href='credits'">
			</div>
		</div>

		<!-- 오늘의 단어 카드 -->
		<div class="today-container">
			<br>
			<div class="today-title">今日の単語</div>
			<div class="today-main">
				<span id="todayKanji"></span>
			</div>
			<div class="today-kanji-meaning">
				<ol id="todayKanjiMeaning"></ol>
			</div>
			<div class="today-bottom">
				<input type="button" class="today-next" value="&lt;" onclick="fetchKanji('prev')">
				<input type="button" class="today-next" value="&gt;" onclick="fetchKanji('next')">
			</div>
		</div>

	</div>

</div>
<!-- 페이지 종료 -->

<script src="${pageContext.request.contextPath}/resources/js/login.js"></script>

<script>
	const CTX= "${pageContext.request.contextPath}";
</script>
</body>
</html>