package com.v_info.ajzclientv3.communication;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.v_info.ajzclientv3.net.Ajz;

//import com.ajzclient.ui.ReceiveInfoListener;

public class Communication {

	private NetWorker netWorker;
	private static Communication instance;
	private static MessageDigest md;

	private Communication() {
		netWorker = new NetWorker();
		
		netWorker.start();
		try {
			if (md == null)
				md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String MD5(String strSrc) {
		byte[] bt = strSrc.getBytes();
		md.update(bt);
		String strDes = bytes2Hex(md.digest());  
		return strDes;
	}

	private static String bytes2Hex(byte[] bts) {
		StringBuffer des = new StringBuffer();
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des.append("0");
			}
			des.append(tmp);
		}
		return des.toString();
	}

	

	public static Communication newInstance() {
		if (instance == null)
			instance = new Communication();
		return instance;
	}
	
	public void setInstanceNull(){
		instance=null;
	}

	public NetWorker getTransportWorker() {
		return netWorker;
	}

	public void stopWork(){
		netWorker.connectSta=0;
		Ajz.setSta(false);
		netWorker.setOnWork(false);
		setInstanceNull();
	}

	
	/**
	 * 
	 * @param userId
	 * @param pwd
	 * @return
	 */
	//发送登录信息
	public boolean login(String userId, String pwd) {
		netWorker.getLogin(userId,pwd);
		return true;
	}
	
	//获取监督检查计划列表
	public boolean getJdjlList() {
		netWorker.getJdjlList();
		return true;
	}
	

	public boolean getGginfoList(int curpage,int numperpage) {
		netWorker.getGginfoList( curpage, numperpage);
		return true;
	}
	

	//获取通知列表
	public boolean getMsgList(int curpage,int numperpage) {
		netWorker.getMsgList( curpage, numperpage);
		return true;
	}

	public boolean getSigSta() {
		netWorker.getSigSta();
		return true;
	}

	public boolean getSig(String userid,String account,String username,String dept,String la,String ln) {
		netWorker.getSig(userid,account,username,dept,la,ln);
		return true;
	}

	public boolean getSubinfo() {
		netWorker.getSubinfo();
		return true;
	}

	public boolean getNewChkCont(String title,String cont,String chklb,String chkgcname,String subcode) {
		netWorker.getNewChkCont(title, cont, chklb, chkgcname, subcode);
		return true;
	}
	

	public boolean getSenImg(String jlcode,String comment,String filePath){
		netWorker.getSenImg(jlcode, comment, filePath);
		return true;
	}
	

	public void getChkPic(String jlcode) {
		netWorker.getChkPic (jlcode);
	}

	public void getMarkMsg(String msgCode,String msgtype) {
		netWorker.getMarkMsg (msgCode,msgtype);
	}


	
	public void getQuite(){
		netWorker.getQuite();
	}

//	public void addReceiveInfoListener(ReceiveInfoListener listener) {
//		netWorker.addReceiveInfoListener(listener);
//	}
	
	public String newSessionID() {
		return String.valueOf(System.currentTimeMillis());
	}
	
	public void reconnect() {
		netWorker.notify();
	}





}
