<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.model.*" %>
<jsp:useBean id="dao" class="com.model.FriendsDAO" />

<%
int sender = (Integer)session.getAttribute("ucode");
int receiver = Integer.parseInt(request.getParameter("receiverUcode"));

if (sender == receiver) {
%>
<script>
alert("自分には友達申請が出来ません");
history.back();
</script>
<%
    return;
}

boolean result = dao.sendFriendRequest(sender, receiver);

if(result){
%>
<script>alert("友達申請完了"); location.href="friendAdd.jsp";</script>
<%
}else{
%>
<script>alert("既に友達であるか、または承認待ちです"); history.back();</script>
<%
}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
</body>
</html>