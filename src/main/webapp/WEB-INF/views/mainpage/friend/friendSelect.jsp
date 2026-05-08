<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>マナトーク 受けた申請</title>
<link href="${pageContext.request.contextPath}/resources/css/friendSelect.css" rel="stylesheet" type="text/css">
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

<div class="login-card" align="center">
<div class="login-title">受けた申請</div>

<c:if test="${not empty flashMsg}">
    <script>
        alert("${flashMsg}");
    </script>
</c:if>

<c:choose>

    <%--받은 신청 없음--%>
    <c:when test="${empty list}">
        <p>受けた友達申請がありません。</p>
    </c:when>

    <%--받은 신청 있음--%>
    <c:otherwise>
        <c:forEach var="f" items="${list}">
            <div class="friend-card">
                <img src="${pageContext.request.contextPath}/resources/img/manatoku.png">

                <div class="friend-info">
                    <div class="name">${f.friendName}</div>
                    <div class="id">@${f.friendId}</div>
                </div>

                <!-- 수락 / 거절 -->
                <form action="${pageContext.request.contextPath}/friends/process"
                      method="post"
                      class="friend-actions">
                    <input type="hidden" name="fcode" value="${f.fcode}">
                    <button class="accept" name="action" value="ACCEPT">承諾</button>
                    <button class="reject" name="action" value="REJECT">拒絶</button>
                </form>
            </div>
        </c:forEach>
    </c:otherwise>

</c:choose>

<br>
<a href="javascript:window.close()">close</a>
</div>
</div>
</body>
</html>