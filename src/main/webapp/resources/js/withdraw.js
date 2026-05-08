

function deleteUser() {
    const pw = document.getElementById("pw").value;

    const params = new URLSearchParams();
    params.append("pw",pw);

    // AJAX 요청
    fetch("/user/my/withdraw/proc", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: params.toString()
    })
        .then(res => res.json())
        .then(res => {
            if(res.success) {
                alert("会員削除が完了しました。");
                window.location.href = "/main/home";
            } else {
                if(res.code == null){
                    alert(res.message);
                }
                else {
                    alert("["+res.code+"] "+res.message);
                }

            }
        })
        .catch(err => {
            console.error(err);
            alert("エラーが発生しました");
        });
}