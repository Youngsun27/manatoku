package com.manatoku.model;

import org.apache.ibatis.type.Alias;

@Alias("Friends")
public class Friends {
	
	private int fcode;
	private int sender;
	private int reciver;
	private String status;
	private String regdate;
	private int friendUcode;
	private String friendId;
	private String friendName;
	private String friendIcon;

	public int getFriendUcode() {
		return friendUcode;
	}
	public void setFriendUcode(int friendUcode) {
		this.friendUcode = friendUcode;
	}
	public String getFriendId() {
		return friendId;
	}
	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public String getFriendIcon() {
		return friendIcon;
	}
	public void setFriendIcon(String friendIcon) {
		this.friendIcon = friendIcon;
	}
	public int getFcode() {
		return fcode;
	}
	public void setFcode(int fcode) {
		this.fcode = fcode;
	}
	public int getSender() {
		return sender;
	}
	public void setSender(int sender) {
		this.sender = sender;
	}
	public int getReciver() {
		return reciver;
	}
	public void setReciver(int reciver) {
		this.reciver = reciver;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	
	
}
