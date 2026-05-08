/* 페이지가 열리면 바로 실행 */
document.addEventListener('DOMContentLoaded', function() {
    loadFriendList();
});

/* 친구 목록 불러오기 */
function loadFriendList() {
fetch(`${CTX}/api/friends`)
    	.then(res => res.json())
    	.then(renderFriends);
}

/* 친구목록 렌더링 */
function renderFriends(list) {
	const panel = document.getElementById("friend-list-container");
	panel.innerHTML = list.map(f => 
	`<label class="friend-item">
	<input type="checkbox" name="friendUcodes" value="${f.ucode}">
	<span class="icon"><img src="${CTX}/resources/img/manatoku.png" alt="profile"></span>
	<div class="info">
	<span class="fname">${f.name}</span><span class="fid">@${f.id}</span><br>
	</div>
	</label>`)
	.join("");
}

document.getElementById("create-group-btn").addEventListener("click", function() {
    // 체크된 친구 ucode 수집
    const checkedBoxes = document.querySelectorAll("#friend-list-container input[type=checkbox]:checked");
    const friendUcodes = Array.from(checkedBoxes).map(cb => cb.value);

    if(friendUcodes.length === 0) {
        alert("フレンドを選択してください。");
        return;
    }

    // FormData 또는 URLSearchParams 생성
    const params = new URLSearchParams();
    friendUcodes.forEach(code => params.append("friendUcodes", code));

    // AJAX 요청
    fetch("/chat/createGroup", {
        method: "POST",
        body: params
    })
    .then(response => {
        if(response.ok) {
            alert("グループ作成完了！");
            window.opener.loadPanel("groups");
            window.close(); // 창 닫기
        } else {
            alert("グループ作成に失敗しました");
        }
    })
    .catch(err => {
        console.error(err);
        alert("エラーが発生しました");
    });
});