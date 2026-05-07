<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<link href="group.css" rel="stylesheet" type="text/css">
<body bgcolor = "#f0f3f3">
<br>
<h1 style="color:#61c3f4;"><b>グループ作成</b></h1>

<form action="${pageContext.request.contextPath}/chat/createGroup" method = "post" name="groupUserAddForm">
<div id="friend-list-container">
    <p>フレンドを呼び出し中...</p>
</div>
<button type="submit">作成</button>
</form>

<script>
	const CTX = "${pageContext.request.contextPath}";
</script>
<script src="group.js"></script> 