function inputCheck(){
	if(document.regForm.email.value==""){
		alert("E-mailアドレスを入力してください。");
		document.regForm.email.focus();
		return;
		}
	var str = document.regForm.email.value;
	var atPos = str.indexOf('@');
	var atLastPos = str.lastIndexOf('@');
	var dotPos = str.indexOf('.');
	var spacePos = str.indexOf(' ');
	var commaPos = str.indexOf(',');
	var eMailSize = str.length;
	if(atPos > 1 && atPos == atLastPos && dotPos > 3 && spacePos == -1 && commaPos == -1 && atPos +1 < dotPos && dotPos +1 < eMailSize){
		
	} else {
	alert('メールアドレスの形式が正しくありません。\n\r再度入力してください。');
	document.regForm.email.focus();
	return;	
	}
	
	if(document.regForm.id.value==""){
		alert("IDを入力してください。");
		document.regForm.id.focus();
		return;
		}
	if(document.regForm.name.value==""){
		alert("ニックネームを入力してください。");
		document.regForm.name.focus();
		return;
		}
	if(document.regForm.passwd.value==""){
		alert("パスワードを入力してください。");
		document.regForm.passwd.focus();
		return;
		}
	if(document.regForm.passwd_2.value==""){
		alert("パスワードを入力してください。");
		document.regForm.passwd_2.focus();
		return;
		}
	if(document.regForm.passwd.value != document.regForm.passwd_2.value){
		alert("パスワードが一致しません。");
		document.regForm.passwd_2.focus();
		return;
	}
	if(document.regForm.phone1.value==""){
		alert("電話番後を入力してください。");
		document.regForm.phone1.focus();
		return;
	}
	if(document.regForm.phone2.value==""){
		alert("電話番後を入力してください。");
		document.regForm.phone2.focus();
		return;
	}
	if(document.regForm.phone3.value==""){
		alert("電話番後を入力してください。");
		document.regForm.phone3.focus();
		return;
	}
	
		
	document.regForm.submit();
}