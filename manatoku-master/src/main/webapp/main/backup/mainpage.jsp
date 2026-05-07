<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Manatoku mainpage</title>
<link href="${pageContext.request.contextPath}/css/mainpage.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/css/info.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/css/navTabs.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/css/panel.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/css/header.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/css/calendar.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/css/chatbot.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/css/buttonPanel.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/css/chat.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/css/star.css" rel="stylesheet" type="text/css">

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<!-- FullCalendar -->
<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.20/index.global.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.20/locales-all.global.min.js"></script>

<!-- Bootstrap -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/bootstrap.min.css">
<script src="${pageContext.request.contextPath}/resource/bootstrap.bundle.min.js"></script>

</head>
<body>
<div class="main-wrapper">
<div class="galaxy"></div>
 <div class="stars"></div>
	<div class="sidebar">
		<jsp:include page="/main/layout/info.jsp"/>
		<jsp:include page="/main/layout/buttonPanel.jsp"/>
		<jsp:include page="/main/layout/navTabs.jsp"/>
		<jsp:include page="/main/layout/panel.jsp"/>
	</div>
	<div class="content">	
		<div class="header" style="display:none;">
			<jsp:include page="/main/layout/header.jsp"/>			
		</div>
		<main class="main">
			<div id="mainpage">
				<div class="content-panel" id="siritoriPanel" style="display:none;">
    				<jsp:include page="/main/layout/chatbot/siritoriMain.jsp"/>
				</div>
				<div class="content-panel" id="calendarPanel" style="display:none;">
    				<jsp:include page="/main/layout/calendar/calendar.jsp"/>
				</div>
				<div class="content-panel" id="chatPanel" style="display:none;">
					<jsp:include page="/main/layout/chat.jsp"/>
				</div>
			</div>
		</main>
	</div>
</div>
<script>
 	const CTX = "${pageContext.request.contextPath}";
    const ucode = "${sessionScope.ucode}";
</script>
<script src="${pageContext.request.contextPath}/js/calendar.js"></script>
<script src="${pageContext.request.contextPath}/js/mainpage.js"></script>
<script src="${pageContext.request.contextPath}/js/chatbot.js"></script>
<script src="${pageContext.request.contextPath}/js/star.js"></script> 
</body>
</html>
