<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="model.*" %>
<%@ page import="com.model.*" %>
<jsp:useBean id="dao" class="com.model.FriendsDAO" />
<!DOCTYPE html>
<html>
<head>
<style>
.friend-card {
    background: white;
    border-radius: 15px;
    padding: 12px 16px;
    margin: 10px auto;
    width: 320px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    display: flex;
    align-items: center;
    font-size: 14px;
}

.friend-card img {
    width: 45px;
    height: 45px;
    border-radius: 50%;
    margin-right: 12px;
    object-fit: cover;
}

.friend-info {
    flex: 1;
}

.friend-name {
    font-weight: bold;
    font-size: 15px;
}

.friend-id {
    color: gray;
    font-size: 13px;
}
</style>
<meta charset="UTF-8">
<title>友達申請</title>
<script>
function idCheck(){
        if(document.friendForm.id.value == ""){
                alert("IDを入力してください。");
                document.friendForm.id.focus();
                return ;
        }
        document.friendForm.submit();
}

function sendAddress(icon,id,name){
        var address = icon+" "+id+" "+name;
        opener.document.regForm.zipcode.value=zipcode;
        opener.document.regForm.address1.value=address;
        self.close();
}

</script>
<%
request.setCharacterEncoding("utf-8");
String check = request.getParameter("check");
String id = request.getParameter("id");

Vector<Member> idList = dao.idRead(id);
int totalList = idList.size();
//System.out.println("검색 수 : "+totalList);


%>
</head>


<body bgcolor = "#f0f3f3">
<br>
<div align = "center">
<h1 style="color:#61c3f4;"><b>友達申請</b></h1>

<form action="friendAdd.jsp" method = "post" name="friendForm">
<table>
<tr>
<td>
会員ID入力　: <input type="text" name = "id">
<input type = "button" value = "検索" onclick = "idCheck()">
</td>
</tr>
<input type="hidden" name="check" value ="n">
</table>
</form>




<table>
<%
if (check != null && check.equals("n")) {
        if (idList.isEmpty()){ // 검색 데이터가 없는 경우
%>
<tr>
<td align = "center"><br>検索の結果がありません。</td>
<tr>

<%
} else { // 검색 데이터가 있는 경우
%>

<tr>
<td align = "center">
※<b><%=id%></b> 検索結果
</td>
<tr>

<%
for(int i = 0; i < totalList; i++) {
        Member vo = idList.elementAt(i);
        int tempUcode = vo.getUcode();
        String tempId = vo.getId();
        String tempName = vo.getName();
        String tempIcon = vo.getIcon();
        String tempPass = vo.getPass();
        String tempBirth = vo.getBirth();
        String tempEmail = vo.getEmail();
        String tempPhone = vo.getPhone();
%>
<tr>
<td>
<%
Integer loginUcode = (Integer) session.getAttribute("ucode");
%>
<div class="friend-card">

    <img src="images/icon/<%=tempIcon%>" alt="icon">

    <div class="friend-info">
        <div class="friend-name"><%=tempName %></div>
        <div class="friend-id">@<%=tempId %></div>
    </div>

    <% if (loginUcode != null && loginUcode != tempUcode) { %>
        <form action="friendRequest.jsp" method="post" style="margin:0;">
            <input type="hidden" name="receiverUcode" value="<%=tempUcode%>">
            <input type="submit" value="申請">
        </form>
    <% } else { %>
        <span style="color:gray;">本人</span>
    <% } %>

</div>

<%
}// end for
}// end else
}// end if

%>
</td>
</tr>

<tr>

<td align = "center">
<a href= "javascript:this.close()">close</a>
</td>
</tr>
</table>


</div>
</body>
</html>