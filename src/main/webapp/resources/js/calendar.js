var calendar;
/* Bootstrap 관련 초기화 함수 삭제 */

$(document).ready(function() {
    /* 캘린더 초기화 함수 호출 */
    initCalendar(); 

    /* 모달 닫기 버튼 이벤트 (취소 버튼) */
    $("#btnClose").click(function() {
        $("#eventModal").hide();
    });
});

/*
================================
    캘린더 초기화 및 설정
================================ */
function initCalendar() {
    var calendarEl = document.getElementById('calendar');
    if (!calendarEl) return;

    calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        locale: 'ja',
        
        /* 높이 및 비율 설정 */
        height: 'auto',
        contentHeight: 'auto',
        aspectRatio: 1.35,     /* 가로 대비 세로 비율 고정 */
        expandRows: true,      /* 일정이 적어도 칸 크기 유지 */
        
        /* 일정 노출 제한 (틀 깨짐 방지) */
        dayMaxEvents: 5,
        dayMaxEventRows: 5,
        
        /* 헤더 툴바 설정 */
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,listWeek'
        },

      
  /* ================================
    멀티 소스 일정 데이터 조회
 ================================ */
        eventSources: [
            {
                /* 1. 내 DB 일정 조회 (AJAX) */
                events: function(info, successCallback, failureCallback) {
                    $.ajax({
                        url: '/cal.do',
                        type: 'GET',
                        data: { command: 'list' },
                        success: function(response) {
                            console.log("✅ 서버에서 일정 데이터 로드:", response);
                            successCallback(response);
                        },
                        error: function() {
                            console.error("❌ 일정 데이터 로드 실패");
                            failureCallback();
                        }
                    });
                }
            },
            {
                /* 2. 구글 공휴일 API 연동 */
                url: 'https://calendar.google.com/calendar/ical/ja.japanese%23holiday%40group.v.calendar.google.com/public/basic.ics',
                format: 'ics',
                className: 'holiday-event',
                editable: false,
                display: 'block'
            }
        ],

        
/* ================================
    이벤트 데이터 변환 및 스타일
================================ */
        eventDataTransform: function(event) {
            /* DB 필드 매핑 */
            event.id = event.calendar_id || event.id;

            /* 날짜 및 제목 정보 추출 */
            var eventDate = new Date(event.start);
            var month = eventDate.getMonth() + 1;
            var date = eventDate.getDate();
            var monthDay = month + "-" + date;
            var title = event.title || "";

            /* 1. 고정 날짜 공휴일 목록 (매년 반복) */
			var fixedHolidays = [
				"1-1",   // 元日
				"2-11",  // 建国記念の日
				"2-23",  // 天皇誕生日
				"4-29",  // 昭和の日
				"5-3",   // 憲法記念日
				"5-4",   // みどりの日
				"5-5",   // こどもの日
				"8-11",  // 山の日
				"11-3",  // 文化の日
				"11-23", // 勤労感謝の日
				"12-25"  // クリスマス
			];


            /* 2. 키워드 기반 공휴일 (해피먼데이 대응) */
            var holidayKeywords = [
				"祝日", "成人の日", "春分の日", "海の日", 
				"敬老の日", "秋分の日", "スポーツの日"
			];


            /* 공휴일 판별 로직 */
            var isGoogle = (event.source && event.source.url && event.source.url.includes('google.com'));
            var type = event.calendar_type || (event.extendedProps && event.extendedProps.calendar_type);

            var isHoliday = isGoogle || 
                            (type === 'HOLIDAY') || 
                            (holidayKeywords.some(function (kw) { return title.includes(kw); }));

            /* 결과에 따른 개별 스타일 적용 */
            if (isHoliday) {
                /* 공휴일: 살구색, 수정 불가 */
                event.classNames = ['holiday-event'];
                event.color = '#ff9f89';
                event.allDay = true;
                event.editable = false;
                event.display = 'block';
            } else {
                /* 개인 일정: ID 기반 자동 색상 할당 */
                var colors = ['#3788d8', '#e67e22', '#27ae60', '#8e44ad', '#c0392b'];
                var colorIndex = (event.calendar_id || event.id) % 5;

                event.classNames = ['personal-event'];
                event.color = colors[colorIndex];
                event.editable = true;
                event.display = 'block';
            }
            return event;
        },
        
/* ================================
 날짜 선택 (신규 일정 등록)
================================ */
        selectable: true,
        select: function(info) {
            $("#eventTitle").val('');
            $("#eventContent").val('');

            /* 시작/종료 시간 설정 */
            var start = info.startStr.includes("T") ? info.startStr.substring(0, 16) : info.startStr + "T09:00";
            var end = info.endStr && info.endStr.includes("T") ? info.endStr.substring(0, 16) : info.startStr + "T18:00";

            $("#eventStart").val(start);
            $("#eventEnd").val(end);

            /* 버튼 제어 및 모달 표시 */
            $("#btnInsert").show().data("mode", "insert").data("id", "");
            $("#btnUpdate, #btnDelete").hide();
            $("#modalTitle").text("📅 予定の登録");
            $("#eventModal").show();

            calendar.unselect();
        },

      
 /* ================================
  이벤트 클릭 (상세 보기 및 수정)
 ================================ */
        eventClick: function(info) {
            /* 공휴일은 클릭 이벤트 무시 */
            if (info.event.classNames.includes('holiday-event')) return;

            var eventId = info.event.id || info.event.extendedProps.calendar_id;

            /* 모달 필드에 데이터 주입 */
            $("#eventTitle").val(info.event.title);
            $("#eventContent").val(info.event.extendedProps.content || '');
            
            if (info.event.start) $("#eventStart").val(info.event.startStr.substring(0, 16));
            if (info.event.end) {
                $("#eventEnd").val(info.event.endStr.substring(0, 16));
            } else {
                $("#eventEnd").val('');
            }

            /* 버튼 제어 및 모달 표시 */
            $("#btnInsert").hide();
            $("#btnUpdate, #btnDelete").show().data("id", eventId);
            $("#modalTitle").text("📅 予定의 修正");
            $("#eventModal").show();
        }
    });

    calendar.render();
}


/* ================================
    저장 및 삭제 AJAX 핸들러
================================ */

/* 1. [등록] 버튼 클릭 핸들러 */
$(document).on('click', '#btnInsert', function() {
    var mode = $(this).data("mode") || "insert";
    var eventData = {
        command: mode,
        ucode : (typeof currentUcode !== 'undefined') ? currentUcode : "",
        calendarId: "",
        title: $('#eventTitle').val(),
        content: $('#eventContent').val(),
        start: $('#eventStart').val(),
        end: $('#eventEnd').val()
    };

    if (!eventData.title) {
        alert('タイトルを入力してください。');
        return;
    }
    ajaxCall(eventData);
});

/* 2. [수정] 버튼 클릭 핸들러 */
$(document).on('click', '#btnUpdate', function() {
    var mode = $(this).data("mode") || "update";
    var eventId = $(this).data("id");
    var eventData = {
        command: mode,
        ucode : (typeof currentUcode !== 'undefined') ? currentUcode : "",
        calendarId: eventId,
        title: $('#eventTitle').val(),
        content: $('#eventContent').val(),
        start: $('#eventStart').val(),
        end: $('#eventEnd').val()
    };

    if (!eventData.title) {
        alert('タイトルを入力してください。');
        return;
    }
    ajaxCall(eventData);
});

/* 3. [삭제] 버튼 클릭 핸들러 */
$(document).on('click', '#btnDelete', function() {
    var id = $(this).data("id");
    if (!id) {
        alert("削除する予定が見つかりません。");
        return;
    }

    if (confirm("この予定を削除しますか？")) {
        ajaxCall({
            command: "delete",
            calendarId: id
        });
    }
});

/* [공통] AJAX 호출 함수 */
function ajaxCall(data) {
    $.ajax({
        url: '/cal.do',
        type: 'POST',
        data: data,
        success: function(res) {
            var result = String(res).trim();
            if (result === "1") {
                alert("完了しました。");
                $("#eventModal").hide();
                /* 캘린더 새로고침 */
                if (calendar) { calendar.refetchEvents(); }
            } else if (result === "-1") {
                alert("該当時間に既に予定があります。（重複チェック）");
            } else {
                alert("失敗しました: " + res);
            }
        },
        error: function() {
            alert("通信エラーが発生しました. ");
        }
    });
}

/*================================
    채팅에서 캘린더 모달 열기
 ================================ */
window.OpenCalendarFromChat = function(messageContent, detectedDate) {
    console.log('openCalendarFromChat 호출:', messageContent, detectedDate);

    /* 날짜 검증 - undefined면 오늘 날짜 사용 */
    if (!detectedDate || detectedDate === 'undefined') {
        detectedDate = new Date().toISOString().substring(0, 10);
    }

    /* 메시지에서 날짜 부분 제거하여 제목 생성 */
    var title = messageContent.replace(/\d{4}[-/]\d{1,2}[-/]\d{1,2}/g, '').trim();

    /* 제목이 비어있으면 기본 메시지 */
    if (!title) {
        title = 'チャットで生成된 予定';
    }

    /* 모달 필드 자동 채우기 */
    $("#eventTitle").val(title);
    $("#eventContent").val("チャット에서 등록: " + messageContent);
    $("#eventStart").val(detectedDate + "T09:00");
    $("#eventEnd").val(detectedDate + "T10:00");

    /* 버튼 표시 제어 */
    $("#btnInsert").show().data("mode", "insert").data("id", "");
    $("#btnUpdate, #btnDelete").hide();
    $("#modalTitle").text("📅 予定의 登録");

    /* 모달 표시 */
    $("#eventModal").show();

    console.log('모달 설정 완료');
};
