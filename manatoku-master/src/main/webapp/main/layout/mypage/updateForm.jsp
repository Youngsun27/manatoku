<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head> 
<meta charset="UTF-8"> 
<title>프로필 수정</title> 
<script> // 1. 서버에서 보낸 메시지(flashMsg)가 있다면 출력 
window.onload = function() { <% String msg = (String)session.getAttribute("flashMsg"); 
if(msg != null) { %> alert("<%= msg %>"); <% session.removeAttribute("flashMsg"); // 메시지 출력 후 삭제 %> 
<% } %> }; 

// 2. 입력 유효성 검사 
function validateForm() { 
var name = document.getElementsByName("name")[0].value; 
if(name.trim() == "") { 
alert("변경할 이름을 입력해주세요."); 
return false; 
} 
return true; 
} 

// 새 비밀번호 입력 시 일치 확인
            if(form.newPwd.value.length > 0) {
                if(form.newPwd.value !== form.newPwdCheck.value) {
                    alert("새 비밀번호 확인이 일치하지 않습니다.");
                    return false;
                }
            }
            // 현재 비밀번호 필수 입력 체크
            if(form.pw.value.trim() == "") {
                alert("본인 확인을 위해 현재 비밀번호를 입력해주세요.");
                return false;
            }
            return true;
        }

        // 3. 로그아웃 처리 함수
        function askLogout() {
            if(confirm("로그아웃 하시겠습니까?")) {
                location.href = "logout.do";
            }
        }

        // 4. 회원 탈퇴 처리 함수
        function askWithdraw() {
            var password = prompt("정말 탈퇴하시겠습니까? 본인 확인을 위해 비밀번호를 입력해주세요.");
            if(password) {
                // 서블릿의 /withdraw.do로 이동하며 pw 파라미터 전달
                location.href = "withdraw.do?pw=" + encodeURIComponent(password);
            }
        }
        
</script> 
</head>
<body bgcolor="#f0f3f3">
<div class="container"> <h2>${sessionScope.userName}님의 마이페이지</h2> 
<form name="updateForm" action="updateUser.do" method="post" enctype="multipart/form-data" onsubmit="return validateForm()"> 
<c:if test="${not empty sessionScope.userImage}"> 
<img src="${pageContext.request.contextPath}/resources/upload/${sessionScope.userImage}" class="profile-img-preview"> </c:if>

    <h2 style="color:#61c3f4;">프로필 수정</h2>
        <table>
            <tr>
                <td>아이디</td>
                <td><input type="text" name="id" value="${sessionScope.id}" readonly style="background:#eee;"></td>
            </tr>
            <tr>
                <td>이름</td>
                <td><input type="text" name="name" placeholder="새 이름 입력"></td>
            </tr>
            <tr>
                <td style="color: #666;">새 비밀번호</td>
                <td><input type="password" name="newPwd" placeholder="변경 시에만 입력"></td>
            </tr>
            <tr>
                <td style="color: #666;">새 비번 확인</td>
                <td><input type="password" name="newPwdCheck" placeholder="새 비밀번호 재입력"></td>
            </tr>
            
         <tr style="background-color: #fff4f4;">
                <td>현재 비밀번호 <b>*</b></td>
                <td><input type="password" name="pw" required placeholder="본인 확인 필수"></td>
            </tr>
            
            <tr>
                <td colspan="2" align="center" style="padding-top:20px;">
                    <button type="submit" class="btn btn-blue" style="width: 120px;">프로필 수정</button>
                    <button type="button" class="btn btn-gray" onclick="history.back()" style="width: 80px; margin-left: 10px;">취소</button>
                </td>
            </tr>
        </table>
    
<div class="button-group">
            <button type="submit" class="btn btn-blue">정보 수정</button>
            <button type="button" class="btn btn-gray" onclick="askLogout()">로그아웃</button>
            <button type="button" class="btn btn-red" onclick="askWithdraw()">회원탈퇴</button>
        </div>
        
        <div style="text-align: center; margin-top: 15px;">
            <a href="javascript:history.back()" style="color: #aaa; text-decoration: none; font-size: 12px;">이전 페이지로 돌아가기</a>
        </div>
</form>
</div>
</body>
</html>