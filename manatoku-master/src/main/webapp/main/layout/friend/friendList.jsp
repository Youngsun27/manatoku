<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>



</head>
<body>
<%@ page import="java.util.*, com.model.*" %>
<jsp:useBean id="dao" class="com.model.FriendsDAO" />

<%
int myUcode = (Integer)session.getAttribute("ucode");
Vector<FriendsTWO> list = dao.getFriendList(myUcode);
%>

<table border="1">
<tr><th>아이디</th><th>이름</th><th>관리</th></tr>

<%
for(FriendsTWO f : list){
%>
<tr>
<td><%=f.getFriendId()%></td>
<td><%=f.getFriendName()%></td>
<td>
<form action="friendDelete.jsp" method="post">
<input type="hidden" name="friendUcode" value="<%=f.getFriendUcode()%>">
<button>삭제</button>
</form>
</td>
</tr>
<% } %>
</table>
</body>
</html>