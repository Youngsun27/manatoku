<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>マナトーク 会員脱退</title>
<link href="${pageContext.request.contextPath}/resources/css/mypage.css" rel="stylesheet" type="text/css">
</head>

<body>

<div class="login-wrapper">

<div class="stars"></div>

<div class="shooting-stars">
<span class="shooting-star"></span>
<span class="shooting-star"></span>
<span class="shooting-star"></span>
<span class="shooting-star"></span>
<span class="shooting-star"></span>
<span class="shooting-star"></span>
</div>

<!-- 회원 탈퇴 카드-->
<div class="mypage-card">
<div class="mypage-title">会員脱退</div>


<div class="name">
脱退のためにパスワードを入力してください。
<br>脱退した後、全ての会員データは復旧出来ません。
<br>
</div>

 <c:if test="${not empty sessionScope.flashMsg}">
<script>
alert("${flashMsg}");
</script>
<c:remove var="flashMsg" scope="session"/>
</c:if>


<table width="100%">
<tr>
<td>パスワード</td>
<td><input type="password" id="pw" required style="width:90%;"></td>
</tr>
</table>

<div class="menu">
<button type="button" onclick="deleteUser()">脱退する</button>
<button type="button" onclick="history.back()">キャンセル</button>
</div> 

</div>


</div>
</body>

<script src="/resources/js/withdraw.js"></script>