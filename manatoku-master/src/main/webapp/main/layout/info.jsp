<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="info">
        <div class="profile"> <a href="${pageContext.request.contextPath}/main/layout/mypage/mypage.jsp"><img src="${pageContext.request.contextPath}/img/manatoku.png"></a></div>
        <div class="user-text">
        	<div class="name">${sessionScope.name}</div>
        	<div class="userid">@${sessionScope.loginId}</div>
        </div>
</div>
