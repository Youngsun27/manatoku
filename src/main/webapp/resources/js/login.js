$(document).ready(function() {
    // 페이지 접속하자마자 현재 한자 로드
    fetchKanji('current');
});

function fetchKanji(endpoint) {
    $.ajax({
        url: '/today/' + endpoint, // 컨트롤러의 @RequestMapping("/today")와 매칭
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            if(!data) {
                alert("데이터가 없습니다.");
                return;
            }
            renderKanji(data);
        },
        error: function(xhr, status, error) {
            console.error("에러 발생:", error);
        }
    });
}

// 데이터를 화면에 그려주는 함수 (중복 코드 방지)
function renderKanji(data) {
    // 1. 텍스트 변경
    $('#todayKanji').text(data.kanji);

    // 2. 리스트(뜻) 비우고 다시 채우기
    var $list = $('#todayKanjiMeaning');
    $list.empty();

    if (data.meanings && data.meanings.length > 0) {
        data.meanings.forEach(function(m) {
            $list.append('<li>' + m + '</li>');
        });
    } else {
        $list.append('<li>뜻 정보가 없습니다.</li>');
    }
}

const loginIdInput = document.getElementById('id');//입력창 인식
const loginPwInput = document.getElementById('pw');
if(loginIdInput) {
    loginIdInput.onkeypress = function(e) {
        if (e.keyCode === 13 && !e.shiftKey) { //입력창 안에서 엔터를 누를시 동작
            e.preventDefault(); //엔터시 줄바꿈 방지
            inputLoginCheck(); //sendChatMessage함수 호출
        }
    };
}
if(loginPwInput) {
    loginPwInput.onkeypress = function(e) {
        if (e.keyCode === 13 && !e.shiftKey) { //입력창 안에서 엔터를 누를시 동작
            e.preventDefault(); //엔터시 줄바꿈 방지
            inputLoginCheck(); //sendChatMessage함수 호출
        }
    };
}

function inputLoginCheck() {
    const idInput = document.getElementById("id");
    const idValue = idInput.value.trim();
    const pwInput = document.getElementById("pw");
    const pwValue = pwInput.value;

    if(idValue===""){
        alert("IDを入力してください。")
        idInput.focus();
        return;
    }
    if(pwValue===""){
        alert("パスワードを入力してください。")
        pwInput.focus();
        return;
    }

    const params = new URLSearchParams();
    params.append("id",idValue);
    params.append("pw",pwValue);

    const idRegex = /^[a-zA-Z0-9]+$/;
    if(!idRegex.test(idValue)){
        alert("IDは英数字のみ使用できます。");
        idInput.focus();
        return false;
    }



    fetch("/user/login/proc", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: params.toString()  // int
    }).then(res => res.json())
        .then(res => {
        if(res.success){
            window.location.href = "/main/home";
        }else{
            if(res.code==null) {
                alert(res.message);
            }
            else{
                alert("["+res.code+"]"+res.message);
            }
        }
    })
}