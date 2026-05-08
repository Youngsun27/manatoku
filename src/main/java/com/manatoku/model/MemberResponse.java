package com.manatoku.model;

/* MemberResponse DTO 객체는 Member객체를 브라우저로 전송하기 위해 담는 DTO입니다.*/
public class MemberResponse {
	
	private int ucode;
	private String email;
	private String id;
	private String name;
	private String icon;
	private String birth1;
	private String birth2;
	private String birth3;
	private String phone1;
	private String phone2;
	private String phone3;
	
	public int getUcode() {
		return ucode;
	}
	public void setUcode(int ucode) {
		this.ucode = ucode;
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
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
		return birth1+"/"+birth2+"/"+birth3;
	}
	
	
}
