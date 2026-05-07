<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <!-- jQuery -->
<%--     <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <!-- FullCalendar -->
    <script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.20/index.global.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.20/locales-all.global.min.js"></script>

    <!-- Bootstrap -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/bootstrap.min.css">
    <script src="${pageContext.request.contextPath}/resource/bootstrap.bundle.min.js"></script> --%>

    <!-- Custom CSS -->
<%--     <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/style.css"> --%>

<div id="calMain">
    <div id="calendar"></div>

    <div class="modal fade" id="eventModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <h4 id="modalTitle">📅 予定の詳細情報</h4>
                <hr>

                <input type="hidden" id="eventId">

                <label>タイトル</label>
                <input type="text" id="eventTitle" placeholder="予定のタイトルを入力してください">

                <label>開始日時</label>
                <input type="datetime-local" id="eventStart">

                <label>終了日時</label>
                <input type="datetime-local" id="eventEnd">

                <label>内容</label>
                <textarea id="eventContent" rows="3" placeholder="詳細内容を入力してください"></textarea>

                <div class="d-flex justify-content-between mt-3">
                    <button id="deleteEvent" class="btn btn-danger">削除</button>
                    <div>
                        <button id="saveEvent" class="btn btn-primary">保存</button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">キャンセル</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
    <%-- <script src="${pageContext.request.contextPath}/js/calendar.js"></script> --%>

    <script>
        // 삭제 버튼 클릭 이벤트 로직 (참고용)
        // 실제 구현은 calendar.js 내부에 있는 calendar 객체를 참조해야 합니다.
        $(document).ready(function() {
            $('#deleteEvent').on('click', function() {
                const eventId = $('#eventId').val();
                
                if (!eventId) {
                    alert("削除したい予定を選択してください。");
                    return;
                }

                if (confirm("この予定を削除してもよろしいですか？")) {
                    // 1. 서버 통신 (AJAX 등)으로 DB 삭제 처리
                    // 2. FullCalendar에서 해당 이벤트 제거 (calendar.getEventById(eventId).remove())
                    // 3. 모달 닫기
                    
                    // ※ 구체적인 삭제 함수 호출은 calendar.js에 정의된 전역 함수나 
                    // 이벤트를 통해 처리하시기 바랍니다.
                    console.log("Delete Event ID:", eventId);
                    
                    // 예시: 
                    // deleteCalendarEvent(eventId); 
                }
            });
        });
    </script>