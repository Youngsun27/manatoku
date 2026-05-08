package com.manatoku.model;

import java.sql.Date;

/* MemberForm DTO 객체는 회원가입 페이지(regForm)의 사용자의 가입 정보를 @Controller에 전달받기 위한 객체*/
public class MemberForm {
	
	/* 필드 선언 (주의 사항 : 파라미터의 name값과 동일하게 작성해야함) */
	private String email;
	private String id;
	private String name;
	private String passwd;
	private String newPwd;
	private String birth1;
	private String birth2;
	private String birth3;
	private String phone1;
	private String phone2;
	private String phone3;
	
	public String getNewPwd() {
		return newPwd;
	}
	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getBirth1() {
		return birth1;
	}
	public void setBirth1(String birth1) {
		this.birth1 = birth1;
	}
	public String getBirth2() {
		return birth2;
	}
	public void setBirth2(String birth2) {
		this.birth2 = birth2;
	}
	public String getBirth3() {
		return birth3;
	}
	public void setBirth3(String birth3) {
		this.birth3 = birth3;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public String getPhone3() {
		return phone3;
	}
	public void setPhone3(String phone3) {
		this.phone3 = phone3;
	}
	
	public String getPhone() {
		return phone1+"-"+phone2+"-"+phone3;
	}
	
	public String getBirth() {
		return birth1+"-"+birth2+"-"+birth3;
	}
	
	
	
}
