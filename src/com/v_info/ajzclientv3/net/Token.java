package com.v_info.ajzclientv3.net;
/**
 * A token base class contais token and secret.Whether accessToken or
 * requestToken should be as child of it.
 * 
 * @author  @author  lizhennan (floatsnowflakes@hotmail.com)
 */
public class Token {
	private static String userId="";
	private static String userName="";
	private static String userRole="";
	private static String userAccount="";

	public static void setUserId(String uid ){
		userId=uid;
	}
	public static String getUserId(){
		return userId;
	}
	public static void setUserName(String uid ){
		userName=uid;
	}
	public static String getUserName(){
		return userName;
	}
	public static void setUserRole(String uid ){
		userRole=uid;
	}
	public static String getUserRole(){
		return userRole;
	}
	public static void setUserAccount(String uid ){
		userAccount=uid;
	}
	public static String getUserAccount(){
		return userAccount;
	}
}
