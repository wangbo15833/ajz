package com.v_info.ajzclientv3.net;

import java.util.ArrayList;
import java.util.List;


public class DataShop {
	
	//首页公告信息
	public static List<Gginfo> gglist = new ArrayList<Gginfo>();
	public static int ggcurpos = 0;
	//检查信息
	public static List<Chkinfo> chklist = new ArrayList<Chkinfo>();
	public static int chkcurpos = 0;
	
	//消息
	public static List<Msg> msglist = new ArrayList<Msg>();
	public static int msgcurpos = 0;
	
	//消息
	public static List<Msg2> msglist2 = new ArrayList<Msg2>();
	public static int msgcurpos2 = 0;
	
	//查检项目
	public static List<ChkItem> chkItem = new ArrayList<ChkItem>();
	public static int chkitemcurpos2 = 0;
	
	//首页公告方法
	public static List<Gginfo> getGglist() {
		return gglist;
	}

	public static void addGg(Gginfo gginfo) {
		gglist.add(0,gginfo);
	}
	
	public static void clearGg() {
		gglist.clear();
	}
	public static void setGgCurPos(int i){
		ggcurpos = i;
	}

	public static int getGgCurPos() {
		// TODO Auto-generated method stub
		return ggcurpos;
	}
	
	//检查方法
	public static List<Chkinfo> getChklist() {
		return chklist;
	}
	
	public static void addChk(Chkinfo chkinfo) {
		chklist.add(0,chkinfo);
	}
	
	public static void clearChk() {
		chklist.clear();
	}
	public static void setChkCurPos(int i){
		chkcurpos = i;
	}
	
	public static int getChkCurPos() {
		// TODO Auto-generated method stub
		return chkcurpos;
	}
	
	//消息
	public static List<Msg> getMsglist() {
		return msglist;
	}
	
	public static void addMsg(Msg msg) {
		msglist.add(0,msg);
	}
	
	public static void clearMsg() {
		msglist.clear();
	}
	public static void setMsgCurPos(int i){
		msgcurpos = i;
	}
	
	public static int getMsgCurPos() {
		// TODO Auto-generated method stub
		return msgcurpos;
	}
	//消息
	public static List<Msg2> getMsglist2() {
		return msglist2;
	}
	
	public static void addMsg2(Msg2 msg) {
		msglist2.add(0,msg);
	}
	
	public static void clearMsg2() {
		msglist2.clear();
	}
	public static void setMsgCurPos2(int i){
		msgcurpos2 = i;
	}
	
	public static int getMsgCurPos2() {
		// TODO Auto-generated method stub
		return msgcurpos2;
	}
	//检查条目
	public static List<ChkItem> getChkItem() {
		return chkItem;
	}
	
	public static void addChkItem(ChkItem msg) {
		chkItem.add(msg);
	}
	
	public static void clearChkItem() {
		chkItem.clear();
	}
	public static void setChkItemCurPos(int i){
		chkitemcurpos2 = i;
	}
	
	public static int getChkItemCurPos2() {
		// TODO Auto-generated method stub
		return chkitemcurpos2;
	}
}
