<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>マナトーク グループ</title>
  <link href="${pageContext.request.contextPath}/resources/css/groupMem.css" rel="stylesheet" type="text/css">
</head>

<body>

<div class="back-wrapper">

  <div class="stars"></div>

  <div class="shooting-stars">
    <div class="shooting-star"></div>
    <div class="shooting-star"></div>
    <div class="shooting-star"></div>
    <div class="shooting-star"></div>
    <div class="shooting-star"></div>
    <div class="shooting-star"></div>
  </div>

  <!-- 중앙 정렬 영역 -->
  <div style="
        position: relative;
        z-index: 10;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 100vh;
    ">

    <h1><b>グループメンバーリスト</b></h1>

    <div id="friend-list-container">
      <p style="text-align:center; color:white; margin-top:150px;">
        フレンドを呼び出し中...
      </p>
    </div>

  </div>

</div>

<script>
  const CTX = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/resources/js/groupMemList.js"></script>

</body>
</html>