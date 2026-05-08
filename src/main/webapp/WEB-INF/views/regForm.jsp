<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>マナトーク 会員登録</title>
<link href="${pageContext.request.contextPath}/resources/css/regForm.css" rel="stylesheet" type="text/css">
</head>

<body>
	
	<!-- 회원가입 에러 메세지 출력 -->
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

		<!-- 우주 배경, 별똥별 -->
		<div class="stars"></div>
		<div class="shooting-stars">
			<span class="shooting-star"></span> <span class="shooting-star"></span>
			<span class="shooting-star"></span> <span class="shooting-star"></span>
			<span class="shooting-star"></span> <span class="shooting-star"></span>
		</div>

		<!-- 회원가입 -->
		<div class="login-card">
			<div class="login-title">会員登録</div>

			<div class="regForm">
			<!-- 회원가입에 실패했을시 기존 입력 정보 유지를 위해 TL태그로 값을 채워주게 함 -->
				<input class="login-input" type="email" id="email" value="${form.email}" placeholder="E-Mail">
				<input class="login-input" type="text" id="id" value="${form.id}" placeholder="ID">
				<input class="login-input" type="text" id="name" value="${form.name}" placeholder="名前">
				<input class="login-input" type="password" id="passwd" placeholder="パスワード">
				<input class="login-input" type="password" id="passwd_2" placeholder="パスワード確認">
				<div class="birth-wrap">
					<select id="birth1">
						<%
						for (int year = java.time.Year.now().getValue(); year >= 1900; year--) {
						%>
						<option value="<%=year%>"><%=year%></option>
						<%
						}
						%>
					</select>
					<select id="birth2">
						<%
						for (int mon = 1; mon <= 12; mon++) {
						%>
						<option value="<%=mon%>"><%=mon%></option>
						<%
						}
						%>
					</select>
					<select id="birth3">
						<%
						for (int day = 1; day <= 31; day++) {
						%>
						<option value="<%=day%>"><%=day%></option>
						<%
						}
						%>
					</select>
				</div>
				<div class="phone-wrap">
					<input type="text" align="left" id="phone1" value="${form.phone1}" placeholder="010" required>
					<input type="text" align="left" id="phone2" value="${form.phone2}"placeholder="1234" required>
					<input type="text" align="left" id="phone3" value="${form.phone3}" placeholder="5678" required>
				</div>

				<input class="login-btn" type="button" value="アカウント作成" onclick="inputCheck()">
				<input class="join-btn" type="button" value="ログインへ戻る" onclick="location.href='/user/login'">
			</div>
		</div>

	</div>
	
	<!-- 페이지 종료 -->

</body>
<script src="${pageContext.request.contextPath}/resources/js/regForm.js"></script>
</html>