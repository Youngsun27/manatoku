<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.*, com.model.*" %>
<jsp:useBean id="dao" class="com.model.FriendsDAO" />


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body bgcolor = "#f0f3f3">
<br>
<div align = "center">
<h1 style="color:#61c3f4;"><b>受けた申請</b></h1>

<%
int myUcode = (Integer)session.getAttribute("ucode");
Vector<FriendsTWO> list = dao.getReceiveRequest(myUcode);
%>


<table align="center">
                                <tr align="center" width="500">
                                        <td bgcolor="#a9def9" width="100">ID</td>
                                        <td bgcolor="#a9def9" width="100">名前</td>
                                        <td bgcolor="#a9def9" width="100">処理</td>
                                </tr>
                                

<%
for(FriendsTWO f : list){
%>
<tr align="center">
<td><%=f.getFriendId()%></td>
<td><%=f.getFriendName()%></td>
<td>
<form action="friendProcess.jsp" method="post">
<input type="hidden" name="fcode" value="<%=f.getFcode()%>">
<button name="action" value="ACCEPT">承諾</button>
<button name="action" value="REJECT">拒絶</button>
</form>
</td>
</tr>
<% } %>
</table>
</div>
</body>
</html>