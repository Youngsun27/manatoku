<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<div id="calMain">
    <div id="calendar"></div>


    <div id="eventModal" class="modal" lang="ja">
        <div class="modal-content">
            <h3 id="modalTitle">📅 予定の修正</h3>

            <label for="eventTitle">タイトル</label> <input type="text"
                                                            id="eventTitle" placeholder="予定のタイトルを入力してください"> <label
                for="eventContent">内容</label>
            <textarea id="eventContent" rows="3" placeholder="予定の詳細内容"></textarea>
            <label for="eventStart">開始日時</label> <input type="datetime-local"
                                                            id="eventStart"> <label for="eventEnd">終了日時</label> <input
                type="datetime-local" id="eventEnd">

            <div class="modal-footer">
                <button type="button" id="btnInsert" class="btn btn-primary"
                        data-mode="insert">登録</button>
                <button type="button" id="btnUpdate" class="btn btn-success"
                        data-mode="update">修正</button>
                <button type="button" id="btnDelete" class="btn btn-danger">削除</button>
                <button type="button" id="btnClose" class="btn btn-secondary">キャンセル</button>
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


<script>
    /* =====================
    日本語ロケール強制適用
    ===================== */
    $(document).ready(function() {
        // すべてのdatetime-local入力フィールドに日本語属性を適用
        $('input[type="datetime-local"]').each(function() {
            this.setAttribute('lang', 'ja-JP');
        });

        // モーダル内のすべての要素に日本語フォントを適用
        $('#eventModal *').css('font-family', '"Noto Sans JP", "Hiragino Kaku Gothic ProN", "Meiryo", sans-serif');
    });


</script>
