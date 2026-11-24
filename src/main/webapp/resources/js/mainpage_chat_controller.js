/* 전역 변수 설정 */
var ws = null; //웹소캣
var chatTitle = null; //채팅창 타이틀
var urlRegex = /(https?:\/\/[^\s]+)/g; //http 인식 형식

/* 시스템 언어 가져오기 */
const systemLang = navigator.language.split("-")[0]; //시스템언어가 ko, jp, en일시 번역창띄우지 않기
function isKorean(text) { //한국어
	return /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/.test(text);
}

function isJapanese(text) {//일본어
	return /[\u3040-\u30ff\u4e00-\u9faf]/.test(text);
}

function isEnglish(text) {//영어
	return /^[A-Za-z0-9\s.,!?'"()-]+$/.test(text);
}

function detectLang(text) {
	if (isKorean(text)) return "ko";
	if (isJapanese(text)) return "ja";
	if (isEnglish(text)) return "en";
	return "unknown";
}

/* 채팅창 출력 */
/* 채팅창 출력(1단계) */
function loadChat(type,btn) {
	showPanel('chatPanel'); //showPanel 함수 호출 (*채팅 패널 활성화)
	if(type === "friend") { //roomType이 친구일때
		const friendUcode = btn.dataset.friendUcode;//버튼에서 친구코드 가져오기
		const friend = document.getElementById("friendButton");
		friend.dataset.friendUcode = friendUcode;
		chatTitle = btn.dataset.chatTitle; //버튼에서 친구이름 가져오기
		fetch(`${CTX}/chat/getFriendChat`,{ //친구채팅 가져오기
			method: "POST",
			headers: {"Content-Type": "application/x-www-form-urlencoded"},
			body: `friendUcode=${friendUcode}`
		})
			.then(res => res.json())
			.then(res => { renderChat(res,type)});
	}
	if(type === "group") { //roomType이 그룹일때
		const roomId = btn.dataset.roomId; //버튼에서 룸아이디 가져오기
		const group  = document.getElementById("groupButton");
		group.dataset.roomId = roomId;
		chatTitle = btn.dataset.chatTitle; //버튼에서 그룹명 가져오기
		renderChat(roomId,type); //룸아이디 renderChat 함수로 보내기
	}
}

/* 채팅창 출력(2단계) */
function renderChat(roomId,type) {
	/* 채팅창 활성화 시 기존 패널 'active'상태 비활성화 */
	document.querySelectorAll(".tabs .nav button").forEach(b => b.classList.remove("active"));
	const friend = document.getElementById("friendButton");
	const group  = document.getElementById("groupButton");

	friend.style.display = "none";
	group.style.display  = "none";

	if (type === "friend") {
		friend.style.display = "block";
	} else {
		group.style.display = "block";
	}

	const titleElement = document.querySelector('.header-center h3'); //채팅창 이름칸 인식
	titleElement.innerText = chatTitle; //채팅창 이름 변경

	/* 채팅창 요소들을 최초 호출시 숨겨져 있어서 인식을 못하기 때문에 채팅창 랜더링과 동시에 버튼 설정 */
	const btnSend = document.getElementById('chatSendBtn'); //채팅 전송 버튼 인식
	if(btnSend) {
		btnSend.onclick = function() { //전송 버튼 클릭시 sendChatMessage함수 호출
			sendChatMessage();
		};
	}
	const chatInput = document.getElementById('chatInputArea'); //입력창 인식
	if(chatInput) {
		chatInput.onkeypress = function(e) {
			if (e.keyCode === 13 && !e.shiftKey) { //입력창 안에서 엔터를 누를시 동작
				e.preventDefault(); //엔터시 줄바꿈 방지
				sendChatMessage(); //sendChatMessage함수 호출
			}
		};
	}

	/* 웹소캣 연결 */
	if(ws != null) { //웹소캣이 이미 연결되어 있는 경우
		try {ws.close(); } catch(e) {} //연결 해제
	}
	/* 웹소캣 세션 연결 */
	const scheme = (location.protocol === "https:") ? "wss://" : "ws://";
	ws = new WebSocket(scheme + location.host + CTX + "/chat?roomId=" + roomId);
	console.log(location.host,CTX,ws.url);
	setSocketEvents();

	/* roomId를 키값으로 기존 채팅 로그 불러오기 */
	fetch(`${CTX}/chat/logs?roomId=${roomId}`)
		.then(res => res.json())
		.then(log => {
			const area = document.getElementById('chatMessageArea'); // 채팅 로그를 출력할 공간 인식
			area.innerHTML = '';
			log.forEach(log => {const type = (log.senderUcode == ucode) ? 'me' : 'other'; // 자신이 보낸 채팅인지 구별
				logRender(type, log.senderName, log.content,"", log.createdAt)}); // 채팅 로그 렌더링 함수 호출
		});
}
/* 채팅창 출력 (3단계) */
/* 채팅 로그 렌더링 */
function logRender(type, name, content, detectedDate, createdAt) {
	const area = document.getElementById('chatMessageArea'); // 채팅 출력 공간 인식
	const div = document.createElement('div'); // div 생성
	div.className = `message-bubble ${type}`; // div class이름 지정
	/* 채팅 내용 입력 */
	div.dataset.original = content;
	div.dataset.translated = "false";

	//  링크 포함 여부 검사
	const hasLink = content.includes("http://") || content.includes("https://");

	/* 채팅 내용 입력 */
	div.innerHTML = `
	<div class="sender-name">${name}</div>
	<div class="message-text">${content}</div>
	${hasLink ? "" : `
		<div class="message-actions">
			<span class="translate-btn">翻訳</span>
		</div>
	`}
`;

	//시스템이랑 같은 언어일시 번역버튼 없애는것
	const systemLang = navigator.language.split("-")[0];
	const msgLang = detectLang(content);
	const translateBtn = div.querySelector(".translate-btn");
	if (translateBtn && msgLang === systemLang) {
		translateBtn.remove();
	}

	area.appendChild(div); // 채팅 공간에 자식 요소로 생성
	const urlMatch = content.match(urlRegex); // 채팅 메세지가 url형식인지 확인
	renderLink(div);
	if (hasLink && typeof renderYoutubePreview === "function") {
		renderYoutubePreview(div);
	}

	/*if (detectedDate!= "") { //캘린더 버튼 생성
		const calDiv = document.createElement('div');
		calDiv.className = `message-bubble me`;
		calDiv.innerHTML = '<div class="msg-footer"><button class="btn-cal" onclick="openCalendarFromChat(\'' +
			content.substring(0, 20) + '\', \'' + content.detectedDate + '\')">🗓 予定に登録</button></div>';
		area.appendChild(calDiv);
		area.scrollTop = area.scrollHeight;


	}*/
	/* 날짜가 감지되면 캘린더 버튼 추가 */

	if (detectedDate) {
		var actionsDiv = document.createElement('div');
		actionsDiv.className = 'message-actions';

		var calBtn = document.createElement('button');
		calBtn.className = 'btn-cal';                  /* document 감시자가 이 class로 찾아요 */
		calBtn.textContent = '🗓 予定に登録';
		calBtn.setAttribute('data-title', content);    /* document 감시자가 이 값을 꺼내요 */
		calBtn.setAttribute('data-date', detectedDate);

		actionsDiv.appendChild(calBtn);
		div.appendChild(actionsDiv);
	}

	area.appendChild(div);
	area.scrollTop = area.scrollHeight;

}

/* 비속어 검사 함수 (추가) */
function containsBadWord(text) {
	if (!text) return false;

	const badWords = ["ばか", "あほ"];

	for (let word of badWords) {
		if (text.toLowerCase().includes(word.toLowerCase())) {
			return true;
		}
	}
	return false;
}

/* 채팅 메세지 입력 처리 */
function sendChatMessage() {
	var input = document.getElementById('chatInputArea'); // 채팅 출력 공간 인식
	var msg = input.value; // 메시지를 변수에 저장

	if (msg === "") return;

	// 전송 전에 비속어 체크
	if (containsBadWord(msg)) {
		alert("使えない単語が含まれています。");
		return; // 전송 중단
	}

	if (ws && ws.readyState === WebSocket.OPEN) { // 웹소캣과 연결이 되어있을 경우
		ws.send(msg); // 웹소캣 @OnMessage 메소드 호출
		input.value = ""; // 메세지 비우기
	} else { // 웹소캣과 연결이 되어있지 않은 경우
		alert("チャットサーバーとの接続が切断されました。");
	}
}

function setSocketEvents() { //웹소캣 설정
	if(!ws) return; //ws가 null이면 리턴
	ws.onmessage = function(event) { // 메시지를 받을 경우
		var data = JSON.parse(event.data); // json 변환
		var type = (data.senderUcode == ucode) ? 'me' : 'other'; //자신이 보낸 메세지인지 구별
		console.log(data);
		/* logRender 함수 호출해서 메시지 랜더링 */
		logRender(type, data.senderName, data.content, data.detectedDate, data.createdAt);

	};
	ws.onopen = function() { //웹소캣 연결시
		console.log("[연결성공] 채팅창 연결 성공.");
	};

	ws.onclose = function() { //웹소캣 연결해제시
		const area = document.getElementById('chatMessageArea');
		area.innerHTML = ''; // 패널 전환시 채팅창 비우기 (채팅창에서 영상 재생지 계속 재생되는것 방지)
		console.log("채팅 연결 종료");
		console.log("채팅창 정리");
	};

	ws.onerror = function(e) { //웹소캣 에러시
		console.error("웹소켓 에러:", e);
	};
}

/*/!* url 랜더링 함수 *!/ !!현재는 사용하지 않는 코드!!
function renderLink(url, messageDiv){
	var path = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
	var fetchUrl = `${CTX}/chat/getOgData?url=` + encodeURIComponent(url);

	fetch(fetchUrl)
		.then(res => res.json())
		.then(data => {
			if(!data || !data.title) return;
			const card = document.createElement('div');
			card.className = 'og-card';
			card.onclick = () => window.open(url, '_blank');
			card.innerHTML = `
				${data.image ? `<img class="og-thumb" src="${data.image}">` : ""}
				<div class="og-content">
					<div class="og-title">${data.title}</div>
					<div class="og-desc">${data.desc || ""}</div>
				</div>
			`;

			messageDiv.appendChild(card); // ⭐️ 메시지 안에 삽입
		})
		.catch(err => console.error("OG 로드 에러:", err));
}*/

/* 번역 */
document.addEventListener("click", function (e) {
	if (!e.target.classList.contains("translate-btn")) return;

	e.stopPropagation();//  유튜브, 링크 클릭 방해 안 함


	const bubble = e.target.closest(".message-bubble");
	const textEl = bubble.querySelector(".message-text");

	const originalText = bubble.dataset.original;
	const isTranslated = bubble.dataset.translated === "true";

	if (isTranslated) {
		textEl.textContent = originalText;
		bubble.dataset.translated = "false";
		e.target.textContent = "翻訳";
		return;
	}

	fetch(CTX + "/translate", {
		method: "POST",
		headers: {
			"Content-Type": "application/json; charset=UTF-8"
		},
		body: JSON.stringify({
			text: originalText,
			targetLang: systemLang
		})
	})
		.then(function (res) {
			return res.json();
		})
		.then(function (data) {
			textEl.textContent = data.translatedText;
			bubble.dataset.translated = "true";
			e.target.textContent = "原文";
		})
		.catch(function () {
			alert("翻訳失敗");
		});
});

function panelClose(){
	if(ws != null){
		ws.close();
	}
}

//링크검사
function renderLink(container) {
	const textEl = container.querySelector(".message-text");
	if (!textEl) return;

	const text = textEl.textContent;

	// 유튜브 링크 검사
	const ytMatch = text.match(
		/(https?:\/\/(?:www\.)?(?:youtube\.com\/watch\?v=|youtu\.be\/)([a-zA-Z0-9_-]+))/
	);

	if (ytMatch) {
		const videoId = ytMatch[2];

		const iframe = document.createElement("iframe");
		iframe.width = "320";
		iframe.height = "180";
		iframe.src = `https://www.youtube.com/embed/${videoId}`;
		iframe.frameBorder = "0";
		iframe.allow =
			"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture";
		iframe.allowFullscreen = true;

		textEl.appendChild(document.createElement("br"));
		textEl.appendChild(iframe);
		return;
	}

	// 일반 링크 → a 태그
	const linkified = text.replace(
		/(https?:\/\/[^\s]+)/g,
		'<a href="$1" target="_blank">$1</a>'
	);

	textEl.innerHTML = linkified;
}

document.addEventListener('click', function(e) {
	if (e.target.classList.contains('btn-cal')) {
		var title = e.target.getAttribute('data-title');
		var date = e.target.getAttribute('data-date');

		console.log('캘린더 버튼 클릭:', title, date);

		showPanel("calendarPanel");

		/* calendar.js의 openCalendarFromChat 함수 사용 */
		if (typeof OpenCalendarFromChat === 'function') {
			OpenCalendarFromChat(title, date);
		} else {
			alert('カレンダー画面を表示できません。先にカレンダーパネルを開いてください。');
		}

	}
});
