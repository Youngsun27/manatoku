/* 1. 텍스트 반응형 함수 */
function TextSize(id, baseVw, maxPx, minPx) {
        try {
                var el = document.getElementById(id);
                if (!el || !el.innerText.trim()) return;

                // 1. 초기 크기 설정
                var startSize = window.innerWidth * (baseVw / 100);
                if (startSize > maxPx) startSize = maxPx;
                el.style.fontSize = startSize + "px";

                // 2. 부모 너비 측정 (말풍선 talk.png의 실제 안쪽 너비)
                var parent = el.parentElement;
                if (!parent) return;

                // 패딩 등 여유공간을 고려하여 부모 너비의 90%를 한계선으로 잡음
                var safeLimit = parent.clientWidth * 0.9;

                // 3. 글자 전체 길이(scrollWidth)가 박스 너비(safeLimit)보다 크면 축소 시작
                if (el.scrollWidth > safeLimit) {
                        var ratio = safeLimit / el.scrollWidth;
                        var finalSize = startSize * ratio;

                        // 4. 설정한 최소 크기(minPx)보다 작아지지는 않게 함
                        if (finalSize < minPx) finalSize = minPx;

                        el.style.fontSize = finalSize + "px";
                }
        } catch (e) {
        }
}

function resizeGameFonts() {
        TextSize('chatDisplay', 5, 40, 3);
        TextSize('userTitle', 2.5, 24, 3);
        TextSize('botTitle', 2.5, 24, 3);
        TextSize('userMeaningContent', 1.8, 18, 3);
        TextSize('botMeaningContent', 1.8, 18, 3);
        TextSize('extraContent', 2.2, 18, 3);
        TextSize('historyWord', 2.5, 24, 3);
}

window.onresize = resizeGameFonts;
window.onload = resizeGameFonts;

/* 2. 단어 전송 함수 */
function fetchSiritori(word) {
        return fetch(`${CTX}/shiritori`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: 'word=' + encodeURIComponent(word)
        }).then(function(response) {
                if (!response.ok) throw new Error("서버 응답 에러");
                return response.json();
        });
}

function updateGameState(data) {
        // [1] 구조 분해 할당 대신 직접 변수에 대입
        var status = data.status;
        var userWord = data.userWord;
        var botWord = data.botWord;

        // [2] 정상적인 게임 진행일 때만 기록 추가
        if (["NORMAL", "WIN", "END_N"].indexOf(status) !== -1) {

                // 1. Optional Chaining(?.) 대신 && 연산자 사용
                if (userWord && userWord.yomi) {
                        wordHistory.push(userWord);
                }

                // 2. botWord가 존재하고, 그 안에 yomi가 있는지 순차적으로 확인
                if (status === "NORMAL" && botWord && botWord.yomi) {
                        wordHistory.push(botWord);
                }
        }

        // 화면 갱신 호출
        renderGameView(data);
}

function renderGameView(data) {
        // [1] 구조 분해 할당을 개별 변수 대입(var)으로 변경
        var status = data.status;
        var charImg = data.charImg;
        var charComment = data.charComment;

        var display = document.getElementById('chatDisplay');
        var charImgTag = document.querySelector('.person');

        // [2] 캐릭터 및 말풍선 업데이트
        charImgTag.style.opacity = 0;
        setTimeout(function() {
                if (charImg) {
                        charImgTag.src = `${CTX}/img/` + charImg;
                }
                display.innerHTML = charComment ? charComment : "";

                // 1. 이미지를 먼저 나타나게 합니다 (글자 크기 조절보다 우선)
                charImgTag.style.opacity = 1;

                // 2. 글자 크기 조절은 별도의 에러 방지 함수로 호출
                try {
                        resizeGameFonts();
                } catch (e) {
                }
        }, 400);

        // [3] 게임 종료 처리
        if (status === "END_N" || status === "WIN") {
                var input = document.getElementById('chatInput');
                var btn = document.getElementById('sendButton');

                if (input) {
                        input.disabled = true;
                        input.placeholder = "ゲーム終了";
                }

                if (btn) {
                        btn.value = "やり直し";
                        btn.onclick = function() {
                                location.reload();
                        };
                }
        }

        // [4] 기록창 및 뜻풀이 갱신
        updateHistoryDisplay();
        renderMeaningArea(data);
}

function sendMessage() {
        const input = document.getElementById('chatInput');
        const word = input.value.trim();
        if (!word) return;

        // [1] 통신 (Promise 방식)
        fetchSiritori(word)
                .then(data => {
                        // 1. 데이터 처리 및 화면 갱신
                        updateGameState(data);

                        // 2. 후처리
                        document.querySelector('.scene').classList.add('visible');
                })
                .catch(err => {
                        console.error("게임 처리 중 오류:", err);
                })
                .finally(() => {
                        input.value = "";
                        input.focus();
                });
}

function renderMeaningArea(data) {
        var status = data.status;
        var userWord = data.userWord;
        var botWord = data.botWord;
        var charName = data.charName;

        // [1] 유저 영역 업데이트
        if (["NORMAL", "WIN", "END_N"].indexOf(status) !== -1) {
                if (userWord) {
                        var userTitleText = "プレイヤー： [" + userWord.yomi + "]" + (userWord.word ? "[" + userWord.word + "]" : "");
                        document.getElementById("userTitle").innerHTML = userTitleText;
                        document.getElementById("userMeaningContent").innerHTML = userWord.meaning;
                }
        }

        // [2] 봇 영역 업데이트 및 게임 진행 상황 파악
        if (status === "NORMAL" && botWord) {
                var botTitleText = charName + "： [" + botWord.yomi + "]" + (botWord.word ? "[" + botWord.word + "]" : "");
                document.getElementById("botTitle").innerHTML = botTitleText;
                document.getElementById("botMeaningContent").innerHTML = botWord.meaning;
        } else if (status === "WIN") {
                document.getElementById("botTitle").innerHTML = charName + "に勝ちました！";
                document.getElementById("botMeaningContent").innerHTML = "";
        } else if (status === "END_N") {
                document.getElementById("botTitle").innerHTML = charName + "に負けました。(「ん」 使用)";
                document.getElementById("botMeaningContent").innerHTML = "";
        }
}

/* 3. 단어 이력 관련 함수 */
let wordHistory = [];

// [1] 단어 이력창 함수
function toggleLayout() {
        // body에 클래스를 넣었다 빼는 것만으로 모든 요소 제어 가능
        document.body.classList.toggle('show-history');

        // 폰트 크기 조절 (애니메이션 시간을 고려해 300ms 후 실행)
        setTimeout(resizeGameFonts(), 300);
}

// [2] 툴팁 위치 업데이트 함수 (중복 제거를 위해 분리)
function updateTooltipPosition(event) {
        const tooltip = document.getElementById('wordTooltip');
        if (!tooltip) return;

        let x = event.clientX + 15;
        let y = event.clientY + 15;

        // 화면 오른쪽/하단 잘림 방지 로직
        if (x + 400 > window.innerWidth) x = event.clientX - 400;
        if (y + 350 > window.innerHeight) y = event.innerHeight - 350;

        tooltip.style.left = x + 'px';
        tooltip.style.top = y + 'px';
}

// [3] 툴팁 표시 함수
function showTooltip(index, event) {
        const data = wordHistory[index];
        const tooltip = document.getElementById('wordTooltip');
        const title = document.getElementById('tooltipTitle');
        const content = document.getElementById('tooltipContent');

        if (!data) return;

        let titleText = "[" + data.yomi + "]";
        if (data.word) {
                titleText += "[" + data.word + "]";
        }

        title.innerText = titleText;
        content.innerHTML = data.meaning;

        tooltip.style.display = 'flex';

        if (content) {
                content.scrollTop = 0;
        }

        updateTooltipPosition(event);
}

// [4] 툴팁 숨기기 함수
function hideTooltip() {
        var tooltip = document.getElementById('wordTooltip');
        if (tooltip) {
                tooltip.style.display = 'none';
        }
}

// [5] 기록창 업데이트 함수
function updateHistoryDisplay() {
        var extraContent = document.getElementById('extraContent');
        if (!extraContent) return;

        extraContent.innerHTML = "";

        for (var i = 0; i < wordHistory.length; i++) {
                var item = wordHistory[i];
                var span = document.createElement('span');
                span.innerText = item.yomi;

                // ★ 개별 span에 이벤트를 걸지 않습니다!
                // 대신 누구인지 식별할 수 있는 인덱스만 저장해둡니다.
                span.setAttribute('data-index', i);
                span.className = 'history-item'; // CSS 식별용 클래스 추가

                span.style.cursor = "help";
                span.style.textDecoration = "underline dashed #aaa";

                extraContent.appendChild(span);

                if (i < wordHistory.length - 1) {
                        extraContent.appendChild(document.createTextNode(" → "));
                }
        }
        extraContent.appendChild(document.createTextNode(" →"));
        extraContent.scrollTop = extraContent.scrollHeight;
}

// [6] 마우스 오버 관련 함수
(function() {
        var extraContent = document.getElementById('extraContent');

        if (extraContent) {
                // 1. 마우스가 올라갔을 때
                extraContent.onmouseover = function(e) {
                        var event = e || window.event;
                        var target = event.target;

                        if (target && target.className === 'history-item') {
                                var index = target.getAttribute('data-index');
                                showTooltip(index, event);
                        }
                };

                // 2. 마우스가 움직일 때 (따라다니기)
                extraContent.onmousemove = function(e) {
                        var event = e || window.event;
                        var target = event.target;

                        if (target && target.className === 'history-item') {
                                updateTooltipPosition(event);
                        }
                };

                // 3. 마우스가 나갈 때 (툴팁 숨기기)
                extraContent.onmouseout = function() {
                        hideTooltip();
                };

                // 4. 휠 스크롤 처리 (이전 코드의 span.onwheel 기능 통합)
                extraContent.onwheel = function(e) {
                        var event = e || window.event;
                        var target = event.target;

                        if (target && target.className === 'history-item') {
                                var content = document.getElementById('tooltipContent');
                                if (content) {
                                        content.scrollTop += event.deltaY;
                                        if (event.preventDefault) event.preventDefault();
                                        else event.returnValue = false;
                                }
                        }
                };
        }
})();