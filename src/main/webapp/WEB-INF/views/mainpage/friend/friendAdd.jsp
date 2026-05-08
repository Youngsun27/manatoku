<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>マナトーク 友達申請</title>
    <link href="${pageContext.request.contextPath}/resources/css/friendAdd.css" rel="stylesheet" type="text/css">
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

    <div class="login-card">
        <div class="login-title">友達申請</div>

        <c:if test="${not empty flashMsg}">
            <script>
                alert("${flashMsg}");
            </script>
        </c:if>

        <form action="${pageContext.request.contextPath}/friends/search" method="post">
            <table>
                <tr>
                    <td>
                        <input type="text" name="id" placeholder="会員ID入力" value="${keyword}">
                        <input type="submit" value="検索" class="friendadd-search">
                    </td>
                </tr>
            </table>
        </form>

        <table>
            <c:choose>
                <c:when test="${empty keyword}">
                    <!-- 아무것도 출력 안 함 -->
                </c:when>
                <c:when test="${empty list}">
                    <div id="resMessage">検索の結果がありません。</div>
                </c:when>

                <c:otherwise>
                    <br>
                    <tr>
                        <td>※ <b>${keyword}</b> 検索結果</td>
                    </tr>

                    <c:forEach var="vo" items="${list}">
                        <tr>
                            <td>
                                <div class="friend-card">
                                    <img src="${pageContext.request.contextPath}/resources/img/manatoku.png">

                                    <div class="friend-info">
                                        <div><b>${vo.name}</b></div>
                                        <div>@${vo.id}</div>
                                    </div>

                                    <c:choose>
                                        <c:when test="${sessionScope.member.ucode ne vo.ucode}">
                                            <form action="${pageContext.request.contextPath}/friends/request" method="post">
                                                <input type="hidden" name="receiverUcode" value="${vo.ucode}">
                                                <button type="submit" class="friendadd-request">申請</button>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <span>本人</span>
                                        </c:otherwise>
                                    </c:choose>

                                </div>
                            </td>
                        </tr>
                    </c:forEach>

                </c:otherwise>
            </c:choose>
        </table>

        <br>
        <a href="javascript:window.close()">close</a>

    </div>
</div>
</body>
</html>