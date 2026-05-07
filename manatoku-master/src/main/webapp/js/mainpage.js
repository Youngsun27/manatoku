/* 웹소캣 변수 선언 */
var ws = null;
var chatTitle = null;

/* 패널 버튼 동작 */

document.getElementById("friendsListBtn").addEventListener("click", () => {
	loadPanel("friends");
	document.querySelectorAll(".tabs .nav button").forEach(b => b.classList.remove("active"));
	document.getElementById("friendsListBtn").classList.add("active");
});

document.getElementById("roomsListBtn").addEventListener("click", () => {
	loadPanel("groups");
	document.querySelectorAll(".tabs .nav button").forEach(b => b.classList.remove("active"));
	document.getElementById("roomsListBtn").classList.add("active");
	
});

document.getElementById("siritoriBtn").addEventListener("click", () => {
	document.querySelectorAll(".tabs .nav button").forEach(b => b.classList.remove("active"));
	document.getElementById("siritoriBtn").classList.add("active");
});

document.getElementById("calendarBtn").addEventListener("click", () => {
	document.querySelectorAll(".tabs .nav button").forEach(b => b.classList.remove("active"));
	document.getElementById("calendarBtn").classList.add("active");
	
});

/* 친구 버튼 동작 */

/* 패널 동작 */

function showPanel(targetId) {
  const panels = document.querySelectorAll(".content-panel");
  panels.forEach(panel => {
    panel.style.display = "none";
  });

const target = document.getElementById(targetId);
  if (target) {
    target.style.display = "block";
	if (!calendar) {
        initCalendar(); 
    } else {
        // 2. 이미 존재한다면 레이아웃만 재계산 (0.01초 지연)
        setTimeout(function() {
            calendar.updateSize();
        }, 10);
    }
  }
	
}

/*function showPanel(panelId) {
	$('.content-panel').hide();
	$('#' + panelId).fadeIn(4000);
}*/

function loadPanel(type) {
if(type === "friends") {
	fetch(`${CTX}/api/friends`)
    	.then(res => res.json())
    	.then(renderFriends);
  }
if(type === "groups") {
	fetch(`${CTX}/api/groups`)
		.then(res => res.json())
		.then(renderRooms);
  }
}

/* 채팅패널버튼 동작 */

document.getElementById("panelContainer").addEventListener("click", (e) => {
	const btn = e.target.closest(".chat-btn");
	if (!btn) return;
	const roomType = btn.dataset.roomType;
	loadChat(roomType,btn);
});

function loadChat(type,btn) {
	if(type === "friend") {
		const friendUcode = btn.dataset.friendUcode;
		chatTitle = btn.dataset.chatTitle;
		fetch(`${CTX}/api/getRoom`,{
		method: "POST",
		headers: {"Content-Type": "application/x-www-form-urlencoded"},
		body: `friendUcode=${friendUcode}`
	})
		.then(res => res.json())
		.then(renderChat)
	}
	if(type === "group") {
		const roomId = btn.dataset.roomId;
		chatTitle = btn.dataset.chatTitle;
		renderChat(roomId);
	}
}

/* 채팅창 열기 */

function renderChat(roomId) {
	
	var btnSend = document.getElementById('chatSendBtn'); // 버튼 ID가 'btnSend'라고 가정
    if(btnSend) {
        btnSend.onclick = function() {
            sendChatMessage();
        };
    }
	
	var chatInput = document.getElementById('chatInputArea'); // 입력창 ID가 'chatInput'
    if(chatInput) {
        chatInput.onkeypress = function(e) {
            // 엔터키(KeyCode 13)이고, Shift 키를 누르지 않았을 때만 전송
            if (e.keyCode === 13 && !e.shiftKey) {
                e.preventDefault(); // 엔터 시 줄바꿈 방지
                sendChatMessage();
            }
        };
    }
	
	const titleElement = document.querySelector('.header-center h3');
	titleElement.innerText = chatTitle;

	document.querySelectorAll(".tabs .nav button").forEach(b => b.classList.remove("active"));
	showPanel('chatPanel');
	if(ws != null) {
		try {ws.close(); } catch(e) {}
	}
	ws = new WebSocket("ws://" + location.host + CTX + "/chat?roomId=" + roomId);
	setSocketEvents();
	
	fetch(`${CTX}/chat/logs?roomId=${roomId}`)
		.then(res => res.json())
		.then(log => {
			const area = document.getElementById('chatMessageArea');
   			area.innerHTML = '';
			log.forEach(log => {const type = (log.senderUcode == ucode) ? 'me' : 'other';
			logRender(type, log.senderName, log.content, log.createdAt)});
		});
}

/* 채팅 로그 렌더링 */
function logRender(type, name, content, createdAt) {
	const area = document.getElementById('chatMessageArea');
	const div = document.createElement('div');
	div.className = `message-bubble ${type}`;
	div.innerHTML = '<div class="sender-name">'+name+'</div><div class="message-text">'+content+'</div><div class="og-card"></div>';
	area.appendChild(div);
	
	const urlRegex = /(https?:\/\/[^\s]+)/g;
    const urlMatch = content.match(urlRegex);
	if (urlMatch) {
        renderLink(urlMatch[0],div);
    }
	area.scrollTop = area.scrollHeight;
}

/* 채팅 메세지 입력 처리 */

function sendChatMessage() {
    var input = document.getElementById('chatInputArea');
    var msg = input.value;

    if (ws && ws.readyState === WebSocket.OPEN) {
        ws.send(msg); // 전역 변수 socket을 통해 전송
        input.value = "";
    } else {
        alert("서버와 연결이 끊겨 있습니다.");
    }
}



function setSocketEvents() {
    if(!ws) return;
    ws.onmessage = function(event) {
        // 서버에서 온 데이터 파싱 (예: "ucode|name|content")
		var data = JSON.parse(event.data);
		var type = (data.senderUcode == ucode) ? 'me' : 'other';
		
		console.log(data);
        // 실시간 메시지는 화면에 바로 추가
		logRender(type, data.senderName, data.content, data.createdAt);
			
    };
	ws.onopen = function() {
        console.log("[연결성공] 서버와 웹소켓 연결이 완료되었습니다.");
    };

    ws.onclose = function() {
        console.log("채팅 연결 종료");
    };
    
    ws.onerror = function(e) {
        console.error("웹소켓 에러:", e);
    };

	console.log("--- 이벤트 리스너 등록 완료 ---");
}

function renderLink(url, messageDiv){
	var path = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
	var fetchUrl = path + "/getOgData?url=" + encodeURIComponent(url);

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
}


/* 친구 목록 띄우기 */

function renderFriends(list) {
	document.getElementById('panel').style.display = "block";
	const panelName = document.querySelector('.panel-title');
	panelName.innerHTML = `フレンドリスト`;
	const panel = document.getElementById("panelContainer");
	panel.innerHTML = list.map(f => 
	`<button class="chat-btn active" data-chat-title="${f.name}" data-friend-ucode="${f.ucode}" data-room-type="friend">
	<span class="icon"><img src="${CTX}/img/manatoku.png"></span>
	<span class="fname">${f.name}</span><span class="fid">@${f.id}</span></button>`)
	.join("");
}

/* 채팅방 목록 띄우기 */

function renderRooms(list) {
	document.getElementById('panel').style.display = "block";
	const panelName = document.querySelector('.panel-title');
	panelName.innerHTML = `グループリスト　<button onclick="window.open('layout/group/groupUserAdd.jsp','groupCreate','width=500px,height=560px,resizable=no, scrollbars=no, status=no, toolbar=no')">グループ＋</button>`;
	const panel = document.getElementById("panelContainer");
	panel.innerHTML = list.map(r => 
	`<button data-chat-title="${r.title}" class="chat-btn active" data-room-id="${r.roomId}" data-room-type="group">
	<span class="rtitle">${r.title}</span></button>`)
	.join("");
}

/*async function loadChat(type,friendUcode) {
if(type === "friends") {
	const result = await fetch(`${CTX}/chat/friends?friendUcode=${encodeURIComponent(friendUcode)}`);
    const data = await result.json();
	document.getElementById("chatname").textContent = data.friend.name;
	renderMessages(data.messages);
	JoinRoom(data.roomId);
  }
}*/

/* 친구 추가 */
function friendAdd(){
        url=`${CTX}/main/layout/friend/friendAdd.jsp?check=y`;
        window.open(url, "post","toolbar=no, width=500, height=500, directories=no, status=yes, scrollbars=yes, menubar=no");        
}

function friendSelect(){
        url=`${CTX}/main/layout/friend/friendSelect.jsp?check=y`;
        window.open(url, "post","toolbar=no, width=500, height=500, directories=no, status=yes, scrollbars=yes, menubar=no");        
}