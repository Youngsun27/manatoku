let isAnswerVisible = false;
let writer;
let score = 100;

// 초기 로드 (첫 진입 시)
$(document).ready(function() {
    loadHistory();
    if (typeof targetNazorikaki !== 'undefined' && targetNazorikaki !== "") {
        initNazorikaki(targetNazorikaki);
    }
});

// 한자 라이브러리 초기화 함수 (인자를 받도록 수정)
function initNazorikaki(char) {
    if (!char) return;

    // 기존 영역 초기화 (캔버스 중복 방지)
    const target = document.getElementById('nazorikaki-target');
    if (!target) return;
    target.innerHTML = '';

    score = 100;
    updateScoreDisplay();

    writer = HanziWriter.create('nazorikaki-target', char, {
        width: 250,
        height: 250,
        padding: 15,
        showOutline: false,
        showCharacter: false,
        drawingStrokeColor: '#FFFF00',
        drawingLineWidth: 30,
        strokeColor: '#2980b9',
        strokeAnimationSpeed: 2,
        delayBetweenStrokes: 150
    });

    startQuiz();
}

// 학년 선택 버튼 클릭 시 (AJAX)
function loadNazorikakiGrade(grade) {
    $.ajax({
        url: CTX + "/nazorikaki/loadGrade",
        type: "GET",
        data: { grade: grade },
        success: function(response) {
            // 1. HTML 교체: 데이터를 뿌려줄 컨테이너에 서버에서 받은 JSP 조각을 삽입
            const container = $("#nazorikakiAjaxArea");
            if (container.length > 0) {
                container.html(response);
            } else {
                console.error("#nazorikakiAjaxArea 요소를 찾을 수 없습니다.");
            }

            // 2. 패널 노출
            $(".content-panel").hide();
            $("#nazorikakiPanel").show();

            // 3. 기록 다시 그리기
            renderHistory();

            // 4. 한자 라이브러리 재설정
            // JSP에서 <script>window.targetNazorikaki = '...';</script>로 데이터를 줘야 합니다.
            if (typeof targetNazorikaki !== 'undefined' && targetNazorikaki !== "") {
                console.log("한자 초기화 시작:", targetNazorikaki);
                initNazorikaki(targetNazorikaki);
            }
        },
        error: function(xhr) {
            console.error("AJAX 에러 발생:", xhr.status);
            alert("データの読み込み中にエラーが発生しました。");
        }
    });
}

function startQuiz() {
    score = 100;
    updateScoreDisplay();
    if (!writer) return;

    writer.hideOutline();

    // 첫 번째 획 가이드 애니메이션
    writer.animateStroke(0, {
        onComplete: function() {
            writer.quiz({
                showHint: false,
                leniency: 1.5,
                drawingStrokeColor: '#FFFF00',
                drawingLineWidth: 30,
                onMistake: function(strokeData) {
                    score -= 10;
                    if (score < 0) score = 0;
                    updateScoreDisplay();
                },
                onComplete: function(summaryData) {
                    addHistory(targetNazorikaki, score);
                }
            });
        }
    });
}

// --- 기록 관련 함수 (작성하신 내용 유지) ---
function addHistory(nazorikaki, finalScore) {
    let history = JSON.parse(localStorage.getItem('nazorikakiHistory') || '[]');
    const newRecord = { nazorikaki: nazorikaki, score: finalScore, date: new Date().getTime() };

    history.unshift(newRecord);
    if (history.length > 10) history.pop();

    localStorage.setItem('nazorikakiHistory', JSON.stringify(history));
    renderHistory();
}

function renderHistory() {
    const list = document.getElementById('history-list');
    if (!list) return; // 요소가 없을 경우 에러 방지

    let history = JSON.parse(localStorage.getItem('nazorikakiHistory') || '[]');

    if (history.length === 0) {
        list.innerHTML = '<p class="empty-msg">まだ記録がありません。</p>';
        return;
    }

    list.innerHTML = history.map(item => `
        <div class="history-item" style="display: flex; justify-content: space-between; padding: 10px; border-bottom: 1px solid #ddd; background: white; margin-bottom: 5px; border-radius: 4px;">
            <span class="nazorikaki-char" style="font-weight: bold; font-size: 1.2rem;">${item.nazorikaki}</span>
            <span class="nazorikaki-score" style="color: #2980b9; font-weight: bold;">${item.score}点</span>
        </div>
    `).join('');
}

function loadHistory() {
    renderHistory();
}

function updateScoreDisplay() {
    const scoreElement = document.getElementById('current-score');
    if (scoreElement) scoreElement.innerText = score;
}

// --- 버튼 기능들 ---
function toggleAnswer() {
    if (isAnswerVisible) {
        writer.hideOutline();
        writer.hideCharacter();
        isAnswerVisible = false;
    } else {
        writer.showOutline();
        isAnswerVisible = true;
    }
}

function animateNazorikaki() {
    writer.showOutline();
    isAnswerVisible = true;
    writer.animateCharacter();
}