function inputCheck(){
	// 이메일 검증
	const inputEmail = document.getElementById("email");
	const emailValue = inputEmail.value.trim();

	if(emailValue === ""){
		alert("E-mailアドレスを入力してください。");
		inputEmail.focus();
		return false;
	}

	// 이메일 길이 제한 (최대 100자)
	if(emailValue.length > 100){
		alert("メールアドレスは100文字以内で入力してください。");
		inputEmail.focus();
		return false;
	}

	// 개선된 이메일 정규식 + 특수문자 제한
	const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
	if(!emailRegex.test(emailValue)){
		alert('メールアドレスの形式が正しくありません。\n再度入力してください。');
		inputEmail.focus();
		return false;
	}

	// ID 검증
	const inputId = document.getElementById("id");
	const idValue = inputId.value.trim();

	if(idValue === ""){
		alert("IDを入力してください。");
		inputId.focus();
		return false;
	}

	// ID: 4-20자, 영문+숫자만 허용
	if(idValue.length < 4 || idValue.length > 20){
		alert("IDは4文字以上20文字以内で入力してください。");
		inputId.focus();
		return false;
	}

	const idRegex = /^[a-zA-Z0-9]+$/;
	if(!idRegex.test(idValue)){
		alert("IDは英数字のみ使用できます。");
		inputId.focus();
		return false;
	}

	// 닉네임 검증
	const inputName = document.getElementById("name");
	const nameValue = inputName.value.trim();

	if(nameValue === ""){
		alert("ニックネームを入力してください。");
		inputName.focus();
		return false;
	}

	// 닉네임: 2-30자, HTML/스크립트 태그 차단
	if(nameValue.length < 2 || nameValue.length > 30){
		alert("ニックネームは2文字以上30文字以内で入力してください。");
		inputName.focus();
		return false;
	}

	// XSS 방지: <, >, ", ', & 등 특수문자 차단
	const dangerousChars = /<|>|"|'|&|script|onclick|onerror/i;
	if(dangerousChars.test(nameValue)){
		alert("ニックネームに使用できない文字が含まれています。");
		inputName.focus();
		return false;
	}

	// 비밀번호 검증
	const inputPasswd = document.getElementById("passwd");
	const passwdValue = inputPasswd.value.trim();

	if(passwdValue === ""){
		alert("パスワードを入力してください。");
		inputPasswd.focus();
		return false;
	}

	// 비밀번호: 8-50자, 영문+숫자+특수문자 조합 필수
	if(passwdValue.length < 8 || passwdValue.length > 50){
		alert("パスワードは8文字以上50文字以内で入力してください。");
		inputPasswd.focus();
		return false;
	}

	// 영문, 숫자, 특수문자 각각 1개 이상 포함
	const hasLetter = /[a-zA-Z]/.test(passwdValue);
	const hasNumber = /[0-9]/.test(passwdValue);
	const hasSpecial = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(passwdValue);

	if(!hasLetter || !hasNumber || !hasSpecial){
		alert("パスワードは英字、数字、特殊文字を各1文字以上含む必要があります。");
		inputPasswd.focus();
		return false;
	}

	// 비밀번호 확인
	const inputPasswd_2 = document.getElementById("passwd_2");
	const passwd2Value = inputPasswd_2.value.trim();

	if(passwd2Value === ""){
		alert("パスワード(確認)を入力してください。");
		inputPasswd_2.focus();
		return false;
	}

	if(passwdValue !== passwd2Value){
		alert("パスワードが一致しません。");
		inputPasswd_2.focus();
		return false;
	}

	// 전화번호 검증
	const inputPhone1 = document.getElementById("phone1");
	const phone1Value = inputPhone1.value.trim();

	if(phone1Value === ""){
		alert("電話番号を入力してください。");
		inputPhone1.focus();
		return false;
	}

	// 전화번호: 숫자만, 2-4자
	if(!/^[0-9]{2,4}$/.test(phone1Value)){
		alert("電話番号は数字のみ入力してください。");
		inputPhone1.focus();
		return false;
	}

	const inputPhone2 = document.getElementById("phone2");
	const phone2Value = inputPhone2.value.trim();

	if(phone2Value === ""){
		alert("電話番号を入力してください。");
		inputPhone2.focus();
		return false;
	}

	if(!/^[0-9]{3,4}$/.test(phone2Value)){
		alert("電話番号は数字のみ入力してください。");
		inputPhone2.focus();
		return false;
	}

	const inputPhone3 = document.getElementById("phone3");
	const phone3Value = inputPhone3.value.trim();

	if(phone3Value === ""){
		alert("電話番号を入力してください。");
		inputPhone3.focus();
		return false;
	}

	if(!/^[0-9]{4}$/.test(phone3Value)){
		alert("電話番号は数字のみ入力してください。");
		inputPhone3.focus();
		return false;
	}

	const inputBirth1 = document.getElementById("birth1");
	const birth1Value = inputBirth1.value.trim();
	const inputBirth2 = document.getElementById("birth2");
	const birth2Value = inputBirth2.value.trim();
	const inputBirth3 = document.getElementById("birth3");
	const birth3Value = inputBirth3.value.trim();

	const params = new URLSearchParams();
	params.append("email", emailValue);
	params.append("id", idValue);
	params.append("name", nameValue);
	params.append("passwd", passwdValue);
	params.append("birth1", birth1Value);
	params.append("birth2", birth2Value);
	params.append("birth3", birth3Value);
	params.append("phone1", phone1Value);
	params.append("phone2", phone2Value);
	params.append("phone3", phone3Value);

	fetch("/user/reg/proc", {
		method: "POST",
		headers: { "Content-Type": "application/x-www-form-urlencoded" },
		body: params.toString()
	})
		.then(res => res.json())
		.then(res => {
			console.log(res);
			if(res.success){
				alert("登録が完了しました。");
				window.location.href = "/user/login";
			} else {
				if(res.code==null) {
					alert(res.message);
				}
				else {
					alert("["+res.code+"] "+res.message);
				}
			}
		})
		.catch(err => {
			console.error(err);
			alert("server error");
		});
}
