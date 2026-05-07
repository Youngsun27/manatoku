package com.model;

import java.util.Date;

public class FriendsTWO {

    private int fcode;
    private int sender;
    private int receiver;
    private String status;
    private Date regdate;

    // JOIN 결과용
    private int friendUcode;
    private String friendId;
    private String friendName;
    private String friendIcon;

    public int getFcode() { return fcode; }
    public void setFcode(int fcode) { this.fcode = fcode; }

    public int getSender() { return sender; }
    public void setSender(int sender) { this.sender = sender; }

    public int getReceiver() { return receiver; }
    public void setReceiver(int receiver) { this.receiver = receiver; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getRegdate() { return regdate; }
    public void setRegdate(Date regdate) { this.regdate = regdate; }

    public int getFriendUcode() { return friendUcode; }
    public void setFriendUcode(int friendUcode) { this.friendUcode = friendUcode; }

    public String getFriendId() { return friendId; }
    public void setFriendId(String friendId) { this.friendId = friendId; }

    public String getFriendName() { return friendName; }
    public void setFriendName(String friendName) { this.friendName = friendName; }

    public String getFriendIcon() { return friendIcon; }
    public void setFriendIcon(String friendIcon) { this.friendIcon = friendIcon; }
}