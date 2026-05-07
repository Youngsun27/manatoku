<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<body bgcolor="#f0f3f3">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container" style="width:400px; margin:auto; background:white; padding:20px; border:1px solid #ff9999;">
    <h2 style="color:red;">회원 탈퇴</h2>
    <p>탈퇴를 위해 비밀번호를 입력해주세요.<br>탈퇴 시 모든 데이터는 복구되지 않습니다.</p>
    
    <c:if test="${not empty sessionScope.flashMsg}">
    	<script>
       		alert("${sessionScope.flashMsg}");
    	</script>
    	<c:remove var="flashMsg" scope="session"/>
	</c:if>
    
    <form action="withdraw.do" method="post" onsubmit="return confirm('정말로 탈퇴하시겠습니까?');">
        <table width="100%">
            <tr>
                <td>비밀번호</td>
                <td><input type="password" name="pw" required style="width:90%;"></td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <br>
                    <input type="submit" value="탈퇴하기" style="background:red; color:white; border:none; padding:10px;">
                    <input type="button" value="취소" onclick="history.back()" style="padding:10px;">
                </td>
            </tr>
        </table>
    </form>
</div>
</body>