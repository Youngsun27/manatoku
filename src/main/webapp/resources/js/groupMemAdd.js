const param = new URLSearchParams(window.location.search);
const roomId = param.get("roomId");
/* 페이지가 열리면 바로 실행 */
document.addEventListener('DOMContentLoaded', function() {
    loadMemberAddList(roomId);
});

/* 친구 목록 불러오기 */
function loadMemberAddList() {
    fetch(`${CTX}/api/groupMemAddList?roomId=${roomId}`)
        .then(res => res.json())
        .then(renderMemberAddList);
}

/* 친구목록 렌더링 */
function renderMemberAddList(list) {
    const panel = document.getElementById("member-list-container");
    panel.innerHTML = list.map(g =>
        `<label class="member-item">
	<input type="checkbox" name="memberUcodes" value="${g.ucode}">
	<span class="icon"><img src="${CTX}/resources/img/manatoku.png" alt="profile"></span>
	<div class="info">
	<span class="gname">${g.name}</span><span class="gid">@${g.id}</span><br>
	</div>
	</label>`)
        .join("");
}

document.getElementById("add-groupMember-btn").addEventListener("click", function() {
    // 체크된 친구 ucode 수집
    const checkedMemberBoxes = document.querySelectorAll("#member-list-container input[type=checkbox]:checked");
    const memberUcodes = Array.from(checkedMemberBoxes).map(cb => cb.value);

    if(memberUcodes.length === 0) {
        alert("フレンドを選択してください。");
        return;
    }

    // FormData 또는 URLSearchParams 생성
    const params = new URLSearchParams();
    params.append("roomId",roomId);
    memberUcodes.forEach(code => params.append("memberUcodes", code));

    // AJAX 요청
    fetch("/chat/groupMemAdd", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: params.toString()  // int
        })
        .then(res => res.json())
        .then(res => {
            if(res.success) {
                alert("メンバー追加完了！");
                window.close(); // 창 닫기
            } else {
                alert("メンバー追加に失敗しました");
            }
        })
        .catch(err => {
            console.error(err);
            alert("エラーが発生しました");
        });
});