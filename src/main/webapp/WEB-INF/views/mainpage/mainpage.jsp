<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>マナトーク</title>

<link href="${pageContext.request.contextPath}/resources/css/mainpage.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/resources/css/info.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/resources/css/navTabs.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/resources/css/panel.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/resources/css/header.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/resources/css/calendar.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/resources/css/chatbot.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/resources/css/buttonPanel.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/resources/css/chat.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/resources/css/star.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/resources/css/nazorikaki.css" rel="stylesheet" type="text/css">

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<!-- FullCalendar -->
<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.20/index.global.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.20/locales-all.global.min.js"></script>

<!-- Bootstrap -->
<%--<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">
<script src="${pageContext.request.contextPath}/resources/js/bootstrap.bundle.min.js"></script>--%>

<!-- KanjiWriter -->
<script src="https://cdn.jsdelivr.net/npm/hanzi-writer@3.5/dist/hanzi-writer.min.js"></script>

</head>
<!-- 알림 메세지 출력 -->
	<c:if test="${not empty flashMsg}">
	<!-- flashMsg라는 이름의 파라미터가 비어 있지 않다면 아래 동작 수행 -->
		<script>
			alert("${flashMsg}");
			/* flashMsg안의 텍스트를 경고창으로 띄움 */
		</script>
		<!-- flashMsg를 세션에서 삭제 -->
		<c:remove var="flashMsg" scope="session"/>
	</c:if>

<body>
<!-- 페이지 시작 -->
<div class="main-wrapper">
	
	<!-- 배경 우주,별 -->
	<div class="galaxy"></div>
	<div class="stars"></div>


<!-- ⭐ 별똥별 컨테이너 추가 -->
	<div class="shooting-stars">
		<div class="shooting-star"></div>
		<div class="shooting-star"></div>
		<div class="shooting-star"></div>
		<div class="shooting-star"></div>
		<div class="shooting-star"></div>
		<div class="shooting-star"></div>
	</div>
	
 	<!-- 사이드 바 시작 -->
	<div class="sidebar">
		<jsp:include page="layout/info.jsp"/>
		<jsp:include page="layout/buttonPanel.jsp"/>
		<jsp:include page="layout/navTabs.jsp"/>
		<jsp:include page="layout/panel.jsp"/>
	</div>
	<!-- 사이드 바 끝 -->
	
	<!-- 메인 페이지 시작 -->
	<div class="content">	
	
		<!-- 메인 페이지 헤더 -->
		<div class="header" style="display:none;">
			<jsp:include page="layout/header.jsp"/>			
		</div>
		
		<!-- 메인 콘텐츠 창 시작 -->
		<main class="main">
			<div id="mainpage">
				<div class="content-panel" id="siritoriPanel" style="display:none;">
    				<jsp:include page="chatbot/siritori.jsp"/>
				</div>
				<div class="content-panel" id="calendarPanel" style="display:none;">
    				<jsp:include page="calendar/calendar.jsp"/>
				</div>
				<div class="content-panel" id="chatPanel" style="display:none;">
					<jsp:include page="layout/chat.jsp"/>
				</div>
				<div class="content-panel" id="nazorikakiPanel" style="display:none;">
					<div id="nazorikakiAjaxArea">
    				    <jsp:include page="nazorikaki/nazorikaki.jsp"/>
    				</div>
				</div>
			</div>
		</main>
		<!-- 메인 콘텐츠 창 종료 -->
	</div>
	
	<div id="wordTooltip" class="meaning-dp-tooltip">
	<strong id="tooltipTitle" class="meaning-dp-tooltip-title"></strong>
	<div id="tooltipContent" class="meaning-dp-div-tooltip"></div>
</div>
	
</div>
<!-- 페이지 종료 -->

<script>
	/* js 경로 변수 저장 */
 	const CTX = "${pageContext.request.contextPath}";
 	/* js 사용자 코드 변수 저장 */
    const ucode = "${sessionScope.member.ucode}";
</script>

<script src="${pageContext.request.contextPath}/resources/js/calendar.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/mainpage_chat_controller.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/mainpage_panel_controller.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/mainpage_etc_controller.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/chatbot.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/star.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/nazorikaki.js"></script>

</body>
</html>

