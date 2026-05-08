<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>マナトーク マイページ</title>
    <link href="${pageContext.request.contextPath}/resources/css/mypage.css" rel="stylesheet" type="text/css">
</head>

<body>

<!-- 페이지 시작 -->
<div class="login-wrapper">

    <!-- 우주 배경, 별똥별 -->
    <div class="stars"></div>
    <div class="shooting-stars">
        <span class="shooting-star"></span> <span class="shooting-star"></span>
        <span class="shooting-star"></span> <span class="shooting-star"></span>
        <span class="shooting-star"></span> <span class="shooting-star"></span>
    </div>

    <!-- 마이페이지 카드 -->
    <div class="mypage-card">

        <div class="mypage-title">マイページ</div>

        <div class="profile">
            <img src="${pageContext.request.contextPath}/resources/img/manatoku.png">
        </div>

        <div class="name">${sessionScope.member.name}</div>
        <div class="userid">@${sessionScope.member.id}</div>


        <div class="menu">
            <button onclick="location.href='update'">情報修正</button>
            <button onclick="askWithdraw()">会員脱退</button>
            <button onclick="askLogout()" style="color:#ff8080;">ログアウト</button>

            <a href="${pageContext.request.contextPath}/main/home" style="color:#ffffff; text-decoration:none; font-weight:bold;"> メインページへ戻る </a>
        </div>
    </div>

</div>

<!-- 페이지 종료 -->

<script>
    function askLogout() {
        if(confirm("ログアウトしますか?")) {
            location.href = "logout";
        }
    }

    function askWithdraw() {
        if(confirm("脱退を希望しますか?\n一度脱退したアカウントは回復できません.")) {
            if(confirm("本当にアカウントを削除してもよろしいですか？")) {
                location.href = "/user/my/withdraw";
            }
        }
    }
</script>

</body>
</html>