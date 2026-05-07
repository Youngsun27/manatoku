<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.model.*" %>
<jsp:useBean id="dao" class="com.model.FriendsDAO" />

<%
int fcode = Integer.parseInt(request.getParameter("fcode"));
String action = request.getParameter("action");

dao.updateFriendStatus(fcode, action);

response.sendRedirect("friendSelect.jsp");
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