<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="info">
        <div class="profile"> <a href="/user/my/mypage"><img src="${pageContext.request.contextPath}/resources/img/manatoku.png"></a></div>
        <div class="user-text">
        	<div class="name">${sessionScope.member.name}</div>
        	<div class="userid">@${sessionScope.member.id}</div>
        </div>
</div>
