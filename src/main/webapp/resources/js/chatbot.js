let tipTimer = null;

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
	TextSize('extraContentSiritori', 2.2, 18, 3);
	TextSize('historyWord', 2.5, 24, 3);
}

window.onresize = resizeGameFonts;
window.onload = resizeGameFonts;

/* 2. 단어 전송 함수 */
function fetchSiritori(word) {
	return fetch(`${CTX}/siritori`, {
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
			charImgTag.src = `${CTX}/resources/img/` + charImg;
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
		
	initTipRotation();
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
    if (!tooltip || tooltip.style.display === 'none') return;

    // 1. 현재 툴팁의 실제 픽셀 크기 측정
    const rect = tooltip.getBoundingClientRect();
    const tWidth = rect.width;
    const tHeight = rect.height;

    // 2. 기본 위치 (마우스 커서에서 15px씩 띔)
    let x = event.clientX + 15;
    let y = event.clientY + 15;

    // 3. 화면 경계 체크 (오른쪽/하단 잘림 방지)
    if (x + tWidth > window.innerWidth) {
        x = event.clientX - tWidth;
    }
    if (y + tHeight > window.innerHeight) {
        y = event.clientY - tHeight;
    }

    // 4. 스타일 직접 주입 (translate를 쓰면 더 부드럽지만, 우선 left/top으로 수정)
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
	var extraContentSiritori = document.getElementById('extraContentSiritori');
	if (!extraContentSiritori) return;

	extraContentSiritori.innerHTML = "";

	for (var i = 0; i < wordHistory.length; i++) {
		var item = wordHistory[i];
		var span = document.createElement('span');
		span.innerText = item.yomi;

		// ★ 개별 span에 이벤트를 걸지 않습니다!
		// 대신 누구인지 식별할 수 있는 인덱스만 저장해둡니다.
		span.setAttribute('data-index', i);
		span.className = 'history-list'; // CSS 식별용 클래스 추가

		span.style.cursor = "help";
		span.style.textDecoration = "underline dashed #aaa";

		extraContentSiritori.appendChild(span);

		if (i < wordHistory.length - 1) {
			extraContentSiritori.appendChild(document.createTextNode(" → "));
		}
	}
	extraContentSiritori.appendChild(document.createTextNode(" →"));
	extraContentSiritori.scrollTop = extraContentSiritori.scrollHeight;
}

// [6] 마우스 오버 관련 함수
(function() {
	var extraContentSiritori = document.getElementById('extraContentSiritori');

	if (extraContentSiritori) {
		// 1. 마우스가 올라갔을 때
		extraContentSiritori.onmouseover = function(e) {
			var event = e || window.event;
			var target = event.target;

			if (target && target.className === 'history-list') {
				var index = target.getAttribute('data-index');
				showTooltip(index, event);
			}
		};

		// 2. 마우스가 움직일 때 (따라다니기)
		extraContentSiritori.onmousemove = function(e) {
			var event = e || window.event;
			var target = event.target;

			if (target && target.className === 'history-list') {
				updateTooltipPosition(event);
			}
		};

		// 3. 마우스가 나갈 때 (툴팁 숨기기)
		extraContentSiritori.onmouseout = function() {
			hideTooltip();
		};

		// 4. 휠 스크롤 처리 (이전 코드의 span.onwheel 기능 통합)
		extraContentSiritori.onwheel = function(e) {
			var event = e || window.event;
			var target = event.target;

			if (target && target.className === 'history-list') {
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

// 전역 변수로 타이머 상태만 관리 (중복 실행 방지용)


function initTipRotation() {
    // 이미 실행 중이면 중복 실행하지 않음
    if (tipTimer !== null) return;

    const tipElement = document.getElementById('tipText');
    if (!tipElement) return;

    const tips = [
        "ボットが入力した単語の「最後の文字」から始まる言葉を入力してください。",
        "促音「っ」や拗音「ゃ・ゅ・ょ」は 大文字として扱われます。",
		"「ん」で終わる単語を入力すると負けになります。",
		"DBに登録されている単語のみ使用可能です。",
		"最後の文字が「ー」の場合、その前の文字の母音で繋げます。(例：コピー → い)",
		"ひらがなのみ入力可能です。"
    ];
    
    let lastIndex = -1; // 직전에 나온 팁 번호를 기억

    tipTimer = setInterval(() => {
        let newIndex;
        
        // 직전과 겹치지 않을 때까지 랜덤 숫자를 뽑음
        do {
            newIndex = Math.floor(Math.random() * tips.length);
        } while (newIndex === lastIndex);

        lastIndex = newIndex; // 현재 번호를 저장

        // 애니메이션 및 텍스트 교체
        tipElement.classList.remove('tip-fade');
        void tipElement.offsetWidth; 
        
        tipElement.innerText = tips[newIndex];
        tipElement.classList.add('tip-fade');
        
    }, 5000);
}