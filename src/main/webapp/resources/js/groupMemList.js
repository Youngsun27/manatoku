document.addEventListener('DOMContentLoaded', function() {
    const params = new URLSearchParams(window.location.search);
    const roomId = params.get("roomId");
    loadFriendList(roomId);
});

/* 친구 목록 불러오기 */
function loadFriendList(roomId) {
    fetch(`${CTX}/chat/groupMemList`, { //그룹 멤버 리스트 가져오기
        method: "POST",
        headers: {"Content-Type": "application/x-www-form-urlencoded"},
        body: `roomId=${roomId}`
    }).then(res => res.json())
        .then(renderGroupMem);
}

/* 친구목록 렌더링 */
function renderGroupMem(list) {
    const panel = document.getElementById("friend-list-container");
    panel.innerHTML = list.map(g =>
        `<label class="friend-item">
	<span class="icon"><img src="${CTX}/resources/img/manatoku.png" alt="profile"></span>
	<div class="info">
	<span class="gname">${g.name}</span><span class="gid">@${g.id}</span><span class="grole">＃メンバー</span><br>
	</div>
	</label>`)
        .join("");
}