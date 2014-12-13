package com.v_info.ajzclientv3.communication;

public interface Config {
	///////////////////////////
	public static final int GET_LOGIN=8001;			//登录
	public static final int SEN_LOGIN=9001;			//返回登录信息
	public static final int SEN_KICK=9002;			//踢下线
	public static final int GET_RELOGIN = 8003;		//receiveMassage出错，重登录
	public static final int SEN_RELOGIN = 9003;		//重登录返回信息
	
	public static final int GET_QUITE=8002;			//请求下线
	
	public static final int GET_JDJL_LIST=8005;		//获取检查监督列表请求
	public static final int SEN_JDJL_LIST=9005;		//返回检查监督列表
	
	public static final int GET_GGINFO_LIST=8007;	//获取公告列表请求
	public static final int SEN_GGINFO_LIST=9007;	//返回公告列表
	
	public static final int GET_MSG_LIST=8009;		//获取消息
	public static final int SEN_MSG_LIST=9009;		//返回消息
	
	public static final int GET_SIG_STA=8011;		//获取签到状态
	public static final int SEN_SIG_STA=9011;		//返回签到状态
	
	public static final int GET_SIG=8013;			//签到
	public static final int SEN_SIG=9013;			//签到

	public static final int GET_SUBINFO = 8015;		//获取项目信息
	public static final int SEN_SUBINFO = 9015;		//返回项目信息
	

	public static final int GET_NEWCHK = 8017;		//新建一个检查
	public static final int SEN_NEWCHK = 9017;		//返回一个检查


	public static final int GET_UPIMG = 8019;		//上传图片
	public static final int SEN_UPIMG = 9019;		//返回上传图片

	public static final int GET_CHKPICS = 8021;		//取得图片信息列表
	public static final int SEN_CHKPICS = 9021;		//返回回信息列表
	

	public static final int GET_MARKMSG = 8023;		//请求标记某消息已读
	public static final int SEN_MARKMSG = 8023;		//返回标记某消息已读
	
	public static final int HEART_BEAT = 10000;		//心跳请求
	public static final int SEN_NOTIFY = 10001;		//发送一个消息提醒
	
}
