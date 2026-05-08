/* 친구 추가 */
function friendAdd(){
    url=`${CTX}/friends/search?check=y`;
    window.open(url, "post","toolbar=no, width=500, height=500, directories=no, status=yes, scrollbars=yes, menubar=no");
}

function friendSelect(){
    url=`${CTX}/friends/receive?check=y`;
    window.open(url, "post","toolbar=no, width=500, height=500, directories=no, status=yes, scrollbars=yes, menubar=no");
}

function groupAdd() {
    window.open('/chat/groupAdd','groupCreate','width=500px,height=650px,resizable=no, scrollbars=no, status=no, toolbar=no');
}

function groupMemList() {
    const button = document.getElementById("groupButton");
    const roomId = button.dataset.roomId;
    window.open(`/chat/groupMember?roomId=${roomId}`,'groupMember','width=500px,height=650px,resizable=no, scrollbars=no, status=no, toolbar=no');
}

function groupMemAdd() {
    const button = document.getElementById("groupButton");
    const roomId = button.dataset.roomId;
    window.open(`/chat/memberAdd?roomId=${roomId}`,'memberAdd','width=500px,height=650px,resizable=no, scrollbars=no, status=no, toolbar=no');
}

function groupRename() {
    const button = document.getElementById("groupButton");
    const roomId = button.dataset.roomId;
    const title = prompt("グループチャットの名前を入力してください。");


    if(title=== "" || title===null) {
        return;
    }else {
        const params = new URLSearchParams();
        params.append("roomId", roomId);
        params.append("title", title);
        fetch("/chat/groupRename", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded"},
            body: params.toString()
        }).then(res => res.json())
            .then(res => {
                if (res.success) {
                    alert("グループチャットの名前を変更しました。");
                    const titleElement = document.querySelector('.header-center h3');
                    titleElement.innerText = title;
                    loadPanel("groups");
                } else {
                    alert("グループチャットの名前を変更に失敗しました。");
                }
            })
    }


}

function friendDel() {
    if (confirm("フレンドを削除しますか？")) {
        const button = document.getElementById("friendButton");
        const friendUcode = button.dataset.friendUcode;
        deleteFriend(friendUcode);
    }
}
/* 친구 삭제 */
function deleteFriend(friendUcode) {
    fetch(`${CTX}/friends/delete`,{ //친구 삭제 요청
        method: "POST",
        headers: {"Content-Type": "application/x-www-form-urlencoded"},
        body: `friendUcode=${friendUcode}`
    }).then(res => res.json())
        .then(alertDelfriend);

}
function alertDelfriend(res) {
    if(res.success){
        alert("フレンドの削除が完了しました。");
        loadPanel("friends");
        ws.close();
    } else {
        alert(res.message);
    }
}

function groupExit() {
    if(confirm("グループから脱出しますか？")){
        const button = document.getElementById("groupButton");
        const roomId = button.dataset.roomId;
        exitGroup(roomId);
    }
}

function exitGroup(roomId) {
    fetch(`${CTX}/chat/exitGroup`,{ //친구 삭제 요청
        method: "POST",
        headers: {"Content-Type": "application/x-www-form-urlencoded"},
        body: `roomId=${roomId}`
    }).then(res => res.json())
        .then(alertDelGroup);
}

function alertDelGroup(res){
    if(res.success){
        alert("グループから脱出しました。");
        loadPanel("groups");
        ws.close();
    } else {
        alert(res.message);
    }
}