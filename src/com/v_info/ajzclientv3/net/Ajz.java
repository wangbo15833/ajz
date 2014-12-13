
/*
 * Copyright @21013 lizhennan.
 *
 */
package com.v_info.ajzclientv3.net;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.v_info.ajzclientv3.util.ProtocolConst;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieSyncManager;

//import com.Ajz.android.Paging;

/**
 * Encapsulation main ajz connect APIs, Include: 1. getRquestToken , 2.
 * getAccessToken, 3. url request. Used as a single instance class. Implements a
 * client api as a synchronized way.
 * 
 * @author  lizhennan (floatsnowflakes@hotmail.com)
 */
public class Ajz {

	/*
	 * 服务器地址
	 */
	//JSONObject a;
	public static String serverip = ProtocolConst.IP;
	//spublic static String serverip = "192.168.1.100";
	public static String SERVERtmp = "http://server:8080/ajz/MainServlet_sys";
	public static String SERVER = "http://server:8080/ajz/MainServlet_sys";
	
	//铃声
	public static String ring = "";
	
	//是否震动
	public static Boolean vibrate = true;
	
	public static Boolean psw = true;
	
	//是否记住密码
	public static Boolean rempsw = true;
	public static String password="";


	//用户姓名
	private static String realname;
	private static boolean Sta=true;
	private static boolean logined=false;
	
	private static Ajz mAjzInstance = null;
	private Token mAccessToken = null;
	
	private  static String uid="";
	//工作单位
	private static String work_unit;
	private static String userid;
	private static String account;
	private static String usertype;
	
	//纬度
	private static double lastla=0;
	//经度
	private static double lastln=0;
	private static List<Subinfo> subList;
	public static Map<String,Map<String,String>> params = new HashMap<String,Map<String,String>>();

	private String mRedirectUrl;
	
	private Ajz() {
	}

	public static double getLastla() {
		return lastla;
	}
	public static void setLastla(double lastla) {
		Ajz.lastla = lastla;
	}
	public static double getLastln() {
		return lastln;
	}
	public static void setLastln(double lastln) {
		Ajz.lastln = lastln;
	}

	public static String getQryUrl() {
		return "http://"+serverip+":8080/ajz/mobileqry/mainqry.jsp";
	}
	public static String getServerip() {
		return serverip;
	}

	public static void setServerip(String serverip) {
		Ajz.serverip = serverip;
	}
	

	public synchronized static Ajz getInstance() {
		if (mAjzInstance == null) {
			mAjzInstance = new Ajz();
		}
		return mAjzInstance;
	}
	
	

	public static String getUid() {
		return uid;
	}

	public static void setUid(String uuid) {
		uid = uuid;
	}
	
	public  static  String getRing() {
		return ring;
	}

	public  static  void setRing(String rring) {
		ring = rring;
	}
	
	public static  Boolean getVibrate() {
		return vibrate;
	}

	public  static void setVibrate(Boolean vvibrate) {
		vibrate = vvibrate;
	}

	public Token getAccessToken() {
		return this.mAccessToken;
	}

	public static String getSERVER() {
		return SERVER;
	}

	public static void setSERVER(String sERVER) {
		Ajz.setServerip(sERVER);
		SERVER = SERVERtmp.replace("server", sERVER);
	}

	public String getRedirectUrl() {
		return mRedirectUrl;
	}

	public void setRedirectUrl(String mRedirectUrl) {
		this.mRedirectUrl = mRedirectUrl;
	}

	public static void setWorkUnit(String optString) {
		work_unit = optString;
	}

	public static void setUserid(String optString) {
		userid = optString;
		
	}

	public static void setAccount(String optString) {
		account = optString;
		
	}

	public static void setUsertype(String optString) {
		usertype=optString;
		
	}

	public static void setRealname(String optString) {
	realname = optString;
	}
	public static String getWorkUnit() {
		return work_unit;
	}

	public static String getUserid() {
		return userid;
		
	}

	public static String getAccount() {
		return account;
		
	}

	public static String getUsertype() {
		return usertype;
		
	}
	/**
	 *  取得用户姓名
	 * @return String Realname
	 */
	public static String getRealname() {
		return realname ;
	}
	public static void setPsw(String string) {
		password=string;
	}
	public static String  getPsw() {
			return password;
	}
	public static List<Subinfo> getSubList() {
		return subList;
	}
	public static void setSubList(List<Subinfo> subList) {
		Ajz.subList = subList;
	}
	public static boolean getSta() {
		return Sta;
	}
	public static void setSta(boolean sta) {
		Sta = sta;
	}
	public static boolean isLogined() {
		return logined;
	}
	public static void setLogined(boolean logined) {
		Ajz.logined = logined;
	}
}

