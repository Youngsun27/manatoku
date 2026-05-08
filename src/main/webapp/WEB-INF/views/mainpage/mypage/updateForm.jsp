<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.manatoku.model.MemberResponse" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8"> 
<title>マナトーク 会員情報修正</title>

<link href="${pageContext.request.contextPath}/resources/css/updateForm.css" rel="stylesheet" type="text/css">
</head>
<body>

<c:if test="${not empty error}">
<script>
alert("${error}");
</script>
</c:if>

<div class="login-wrapper">

<div class="stars"></div>

<div class="shooting-stars">
<span class="shooting-star"></span> <span class="shooting-star"></span>
<span class="shooting-star"></span> <span class="shooting-star"></span>
<span class="shooting-star"></span> <span class="shooting-star"></span>
</div>

<%
MemberResponse member = (MemberResponse) session.getAttribute("member");
String phone = member != null ? member.getPhone() : null;
String birth = member != null ? member.getBirth() : null;

String phone1 = "";
String phone2 = "";
String phone3 = "";

if(phone != null && phone.contains("-")) {
    String[] phoneArr = phone.split("-");
    if(phoneArr.length == 3) {
        phone1 = phoneArr[0];
        phone2 = phoneArr[1];
        phone3 = phoneArr[2];
    }
}

%>

<%


String birth1 = "";
String birth2 = "";
String birth3 = "";

if (birth != null) {

    // yyyy-MM-dd HH:mm:ss → yyyy-MM-dd
    if (birth.contains(" ")) {
        birth = birth.substring(0, birth.indexOf(" "));
    }

    // 구분자 통일
    birth = birth.replace("-", "/");

    String[] birthArr = birth.split("/");
    if (birthArr.length == 3) {
        birth1 = birthArr[0];
        birth2 = birthArr[1];
        birth3 = birthArr[2];
    }
}
%>

<!-- 회원수정 -->
<div class="login-card">
<div class="login-title">情報修正</div>
<input class="login-input" type="email" id="email" value="${sessionScope.member.email}" readonly>
<input class="login-input" type="text" id="id" value="${sessionScope.member.id}" readonly >
<input class="login-input" type="text" id="name" placeholder="名前" value="${sessionScope.member.name}">
<input class="login-input"  type="password" id="passwd" required placeholder="現在のパスワード">
<input class="login-input"  type="password" id="newPwd" placeholder="新しいパスワード" >
<input class="login-input" type="password" id="newPwdCheck" placeholder="パスワード確認">

<div class="birth-wrap">
<select id="birth1">
<%
for (int year = java.time.Year.now().getValue(); year >= 1900; year--) {
    String selected = year == Integer.parseInt(birth1.isEmpty() ? "0" : birth1)
        ? "selected" : "";
%>
<option value="<%=year%>" <%=selected%>><%=year%></option>
<%
}
%>
</select>
<select id="birth2">
<%
for (int mon = 1; mon <= 12; mon++) {
    String selected = mon == Integer.parseInt(birth2.isEmpty() ? "0" : birth2)
        ? "selected" : "";
%>
<option value="<%=mon%>" <%=selected%>><%=mon%></option>
<%
}
%>
</select>
<select id="birth3">
<%
for (int day = 1; day <= 31; day++) {
    String selected = day == Integer.parseInt(birth3.isEmpty() ? "0" : birth3)
        ? "selected" : "";
%>
<option value="<%=day%>" <%=selected%>><%=day%></option>
<%
}
%>
</select>
</div>

<div class="phone-wrap">
    <input type="text" id="phone1" value="<%=phone1%>" required>
    <input type="text" id="phone2" value="<%=phone2%>" required>
    <input type="text" id="phone3" value="<%=phone3%>" required>
</div>

<button class="login-btn" type="button" onclick="inputCheck()" >アカウント修正</button>

<br>
<a href="javascript:history.back()">マイページへ戻る</a>
</div>
</div>

</body>
</html>

<script src="/resources/js/update.js"></script>