package com.manatoku.model;

import org.apache.ibatis.type.Alias;

@Alias("FriendInfo")
public class FriendInfo {
	private int ucode;
	private String id;
	private String name;
	private String icon;
	
	public int getUcode() {
		return ucode;
	}
	public void setUcode(int ucode) {
		this.ucode = ucode;
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
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	
}
