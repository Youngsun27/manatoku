/* 패널 버튼 클릭 동작 */
/* 리스트 버튼 동작 */
/* 문서의 id값 friendsListBtn의 요소가 클릭되었을 경우 호출 */
/* 친구 목록 버튼 */
document.getElementById("friendsListBtn").addEventListener("click", () => {
    loadPanel("friends"); // loadPanel 함수 호출
    /* 모든 버튼 상태에 'active'를 제거 */
    document.querySelectorAll(".tabs .nav button").forEach(b => b.classList.remove("active"));
    /* id값 friendsListBtn의 상태에 'active'를 추가 */
    document.getElementById("friendsListBtn").classList.add("active");
});
/* 그룹채팅 목록 버튼 */
document.getElementById("roomsListBtn").addEventListener("click", () => {
    loadPanel("groups");
    document.querySelectorAll(".tabs .nav button").forEach(b => b.classList.remove("active"));
    document.getElementById("roomsListBtn").classList.add("active");
});
/* 기능 버튼 동작 */
/* 문서의 id값 siritoriBtn의 요소가 클릭되었을 경우 호출 */
/* 끝말잇기 버튼 */
document.getElementById("siritoriBtn").addEventListener("click", () => {
    /* 모든 버튼 상태에 'active'를 제거 */
    document.querySelectorAll(".tabs .nav button").forEach(b => b.classList.remove("active"));
    /* id값 roomsListBtn의 상태에 'active'를 추가 */
    document.getElementById("siritoriBtn").classList.add("active");
    /* showPanel 함수 호출 */
    showPanel("siritoriPanel");

});
/* 한자쓰기 버튼 */
document.getElementById("nazorikakiBtn").addEventListener("click", () => {
    /* 모든 버튼 상태에 'active'를 제거 */
    document.querySelectorAll(".tabs .nav button").forEach(b => b.classList.remove("active"));
    /* id값 roomsListBtn의 상태에 'active'를 추가 */
    document.getElementById("nazorikakiBtn").classList.add("active");
    /* showPanel 함수 호출 */
    showPanel("nazorikakiPanel");
});

/* 캘린더 버튼 */
document.getElementById("calendarBtn").addEventListener("click", () => {
    document.querySelectorAll(".tabs .nav button").forEach(b => b.classList.remove("active"));
    document.getElementById("calendarBtn").classList.add("active");
    showPanel("calendarPanel");
});

/* 패널 작동 (showPanel) */
function showPanel(targetId) {
    const panels = document.querySelectorAll(".content-panel");
    panels.forEach(panel => {
        /* 모든 패널 숨기기 */
        panelClose();
        panel.style.display = "none";
    });

    const target = document.getElementById(targetId);
    /* 선택 패널 보이게 */
    if (target) {
        target.style.display = "block";
        /* 캘린더 패널의 경우 호출 타이밍 버그로 인해 지연 출력이 필요함 */
        if (!calendar) {
            initCalendar();
        } else { // 이미 존재한다면 레이아웃만 재계산 (0.01초 지연)
            setTimeout(function() {
                calendar.updateSize();
            }, 10);
        }
    }
}

/* 리스트 패널 출력 */
/* 리스트 패널 출력(1단계) */
function loadPanel(type) {
    /* 친구 리스트 가져오기 */
    if(type === "friends") {
        fetch(`${CTX}/api/friends`)
            .then(res => res.json())
            .then(renderFriends); //json으로 친구 객체 리스트를 받아서 renderFriends 함수로 넘기기
    }
    /* 그룹채팅 리스트 가져오기 */
    if(type === "groups") {
        fetch(`${CTX}/api/groups`)
            .then(res => res.json())
            .then(renderRooms); //json으로 친구 객체 리스트를 받아서 renderRooms 함수로 넘기기
    }
}

/* 리스트 패널 출력(2단계) */
/* 친구 리스트 출력 */
function renderFriends(list) {
    /* 리스트 패널 숨김 해제 */
    document.getElementById('panel').style.display = "block";
    /* 리스트 패널 헤더 가져오기 */
    const panelName = document.querySelector('.panel-title');
    /* 리스트 패널 해더 구성 (타이틀) */
    panelName.innerHTML = `フレンドリスト`;
    /* 리스트 패널 가져오기 */
    const panel = document.getElementById("panelContainer");
    /* 리스트 패널 구성 */
    panel.innerHTML = list.map(f =>
        `<button class="chat-btn active" data-chat-title="${f.name}" data-friend-ucode="${f.ucode}" data-room-type="friend">
	<span class="icon"><img src="${CTX}/resources/img/manatoku.png"></span>
	<span class="fname">${f.name}</span><span class="fid">@${f.id}</span></button>`)
        .join("");
}
/* 그룹채팅 리스트 출력 */
function renderRooms(list) {
    /* 리스트 패널 숨김 해제 */
    document.getElementById('panel').style.display = "block";
    /* 리스트 패널 헤더 가져오기 */
    const panelName = document.querySelector('.panel-title');
    /* 리스트 패널 해더 구성 (타이틀 + 버튼) */
    panelName.innerHTML = `グループリスト　<button onclick="groupAdd()">グループ＋</button>`;
    /* 리스트 패널 가져오기 */
    const panel = document.getElementById("panelContainer");
    /* 리스트 패널 구성 */
    panel.innerHTML = list.map(r =>
        `<button data-chat-title="${r.title}" class="chat-btn active" data-room-id="${r.roomId}" data-room-type="group">
	<span class="rtitle">${r.title}</span></button>`)
        .join("");
}

/* 리스트 패널 버튼 동작 */
document.getElementById("panelContainer").addEventListener("click", (e) => {
    const btn = e.target.closest(".chat-btn"); //버튼 찾기
    if (!btn) return; //버튼을 찾는데 실패하면 리턴
    const roomType = btn.dataset.roomType; //버튼에서 roomType 데이터 가져오기
    loadChat(roomType,btn); //버튼 정보와 roomType 데이터 loadChat 함수로 보내기
});
