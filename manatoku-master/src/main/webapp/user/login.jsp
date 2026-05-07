<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>マナトーク ログイン</title>

<style>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-family: "Lucida Sans Unicode", sans-serif;
}

body {
    height: 100vh;
    overflow: hidden;
}

/* ======================
   배경
====================== */
.login-wrapper {
    position: relative;
    width: 100%;
    height: 100vh;
    background: linear-gradient(135deg, #1a1a2e, #16213e, #0f3460);
    overflow: hidden;
}

/* 별 */
.stars {
    position: absolute;
    width: 100%;
    height: 100%;
    background-image:
        radial-gradient(2px 2px at 20px 30px, #fff, transparent),
        radial-gradient(1.5px 1.5px at 80px 120px, #fff, transparent),
        radial-gradient(2px 2px at 150px 200px, #fff, transparent);
    background-size: 250px 250px;
    animation: starsMove 120s linear infinite;
}

/* ======================
   별똥별
====================== */
.shooting-stars {
    position: absolute;
    width: 100%;
    height: 100%;
    pointer-events: none;
}

.shooting-star {
    position: absolute;
    top: -20%;
    right: -20%;
    width: 6px;
    height: 6px;
    background: white;
    border-radius: 50%;
    box-shadow: 0 0 15px white;

    transform-origin: left center;
    transform: rotate(-45deg);

    opacity: 0;
    animation: shooting 3.5s linear infinite;
}

/* 꼬리 */
.shooting-star::after {
    content: "";
    position: absolute;
    left: 6px;
    top: 50%;
    width: 160px;
    height: 2px;
    background: linear-gradient(to right, white, transparent);
    transform: translateY(-50%);
}

/* 기존 3개 */
.shooting-star:nth-child(2) {
    top: -10%;
    right: 10%;
    animation-delay: 1.5s;
}

.shooting-star:nth-child(3) {
    top: 10%;
    right: 30%;
    animation-delay: 3s;
}

/* ⭐ 추가된 3개 */
.shooting-star:nth-child(4) {
    top: -25%;
    right: 50%;
    animation-delay: 4.5s;
}

.shooting-star:nth-child(5) {
    top: -5%;
    right: 65%;
    animation-delay: 6s;
}

.shooting-star:nth-child(6) {
    top: 15%;
    right: 80%;
    animation-delay: 7.5s;
}

/* ======================
   로그인 카드
====================== */
.login-card {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 360px;
    padding: 40px;
    border-radius: 20px;

    background: rgba(255,255,255,0.15);
    backdrop-filter: blur(12px);
    border: 1px solid rgba(255,255,255,0.35);
    box-shadow: 0 0 40px rgba(255,255,255,0.3);

    z-index: 5;
    text-align: center;
}

.login-title {
    font-size: 36px;
    font-weight: bold;
    color: #ffffff;
    margin-bottom: 30px;
    text-shadow: 0 0 15px rgba(255,255,255,0.7);
}

.login-input {
    width: 100%;
    height: 42px;
    margin-bottom: 18px;
    padding-left: 14px;
    border-radius: 12px;
    border: 1px solid rgba(255,255,255,0.4);
    background: rgba(0,0,0,0.45);
    color: #fff;
}


/* ======================
   버튼 (심플 버전)
====================== */

.login-btn,
.join-btn {
    width: 100%;
    height: 45px;
    margin-top: 10px;

    border-radius: 14px;
    border: 2px solid transparent;

    background: #3f4db8;
    color: #ffffff;
    font-weight: bold;
    font-size: 15px;

    cursor: pointer;

    transition: all 0.25s ease;
}

/* 마우스 올렸을 때 */
.login-btn:hover,
.join-btn:hover {
    transform: scale(1.05);
    border: 2px solid #ffffff;
    background: #5b6cff;
}

/* 클릭 시 */
.login-btn:active,
.join-btn:active {
    transform: scale(0.97);
}


/* ======================
   애니메이션
====================== */
@keyframes shooting {
    0%   { transform: translate(0,0) rotate(-45deg); opacity: 0; }
    10%  { opacity: 1; }
    100% { transform: translate(-900px, 900px) rotate(-45deg); opacity: 0; }
}

@keyframes starsMove {
    from { background-position: 0 0; }
    to   { background-position: 1000px 500px; }
}
</style>
</head>
<body>

	<c:if test="${not empty error}">
		<script>
			alert("${error}");
		</script>
	</c:if>

	<c:if test="${not empty sessionScope.loginID}">
		<script>
			location.replace('manatoku/main/mainpage.jsp');
		</script>
	</c:if>

	<c:if test="${not empty sessionScope.flashMsg}">
		<script>
			alert("${sessionScope.flashMsg}");
		</script>
		<c:remove var="flashMsg" scope="session" />
	</c:if>

	<div class="login-wrapper">

		<div class="stars"></div>

		<!-- ⭐ 별똥별 총 6개 -->
		<div class="shooting-stars">
			<span class="shooting-star"></span> <span class="shooting-star"></span>
			<span class="shooting-star"></span> <span class="shooting-star"></span>
			<span class="shooting-star"></span> <span class="shooting-star"></span>
		</div>

		<!-- 로그인 -->
		<div class="login-card">
			<div class="login-title">マナトーク</div>

			<form action="loginProc" method="post">
				<input class="login-input" type="text" name="id" placeholder="ID">
				<input class="login-input" type="password" name="pw"
					placeholder="パスワード"> <input class="login-btn" type="submit"
					value="ログイン"> <input class="join-btn" type="button"
					value="会員登録" onclick="location.href='regForm.jsp'">
			</form>
		</div>

	</div>

</body>
</html>