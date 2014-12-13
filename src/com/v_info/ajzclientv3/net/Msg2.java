package com.v_info.ajzclientv3.net;

public class Msg2 {
	private String msgCode;
	private String msgTitle;
	private String msgTime;
	private String msglb;
	private String cont;
	
	private boolean isread;
	private String auth;
	private String auth_account;
	private String auth_dept;
	private static int newMsgNum=0;
	
	public static int getNewMsgNum() {
		return newMsgNum;
	}
	public static void setNewMsgNum(int newMsgNum) {
		Msg2.newMsgNum = newMsgNum;
	}
	
	public boolean isIsRead() {
		return isread;
	}
	public void setIsRead(String isread) {
		if(isread.equals("1")){
			this.isread = true;
		}else{
			this.isread=false;
		}
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public String getAuthAccount() {
		return auth_account;
	}
	public void setAuthAccount(String auth_account) {
		this.auth_account = auth_account;
	}
	public String getAuthDept() {
		return auth_dept;
	}
	public void setAuthDept(String auth_dept) {
		this.auth_dept = auth_dept;
	}
	public String getMsgCode() {
		return msgCode;
	}
	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}
	public String getMsgTitle() {
		return msgTitle;
	}
	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}
	public String getMsgTime() {
		return msgTime;
	}
	public void setMsgTime(String msgTime) {
		this.msgTime = msgTime;
	}
	public String getMsglb() {
		return msglb;
	}
	public void setMsglb(String msglb) {
		this.msglb = msglb;
	}
	public String getCont() {
		return cont;
	}
	public void setCont(String cont) {
		this.cont = cont;
	}
}
