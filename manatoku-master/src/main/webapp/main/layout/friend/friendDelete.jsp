<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%@ page import="com.model.*" %>
<jsp:useBean id="dao" class="com.model.FriendsDAO" />

<%
int myUcode = (Integer)session.getAttribute("ucode");
int friendUcode = Integer.parseInt(request.getParameter("friendUcode"));

dao.deleteFriend(myUcode, friendUcode);
response.sendRedirect("friendList.jsp");
%>
</body>
</html>