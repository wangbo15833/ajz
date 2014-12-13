package com.v_info.ajzclientv3.service;

import java.util.HashMap;

import android.content.Context;
import android.util.Log;

public class Task {
   
	
	public static final int LOGIN=1;
	public static final int HOMEINFO=2;
	public static final int SIGNIN=3;
	public static final int MSGSEND=4;
	public static final int CHKSEND=6;
	public static final int QRYSEND=7;

	public static final int MSGRECIEVEFORE=5;//只有消息有接收 前台接收频率高
	public static final int MSGRECIEVEBACK=8;//只有消息有接收 后台接收频率低
	
	private int taskId;
	private HashMap<String,String> taskParams;
	private Context ctx;
	private static final String TAG="Task";
	
	public Task(int taskId, HashMap<String,String> taskParams,Context ctx) {
		super();
		this.taskId = taskId;
		this.taskParams = taskParams;
		this.ctx = ctx;
	}
	public Task(int taskId) {
		super();
		this.taskId = taskId;
		Log.d(TAG,"Contraction the task use lee method!");
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public HashMap<String,String> getTaskParams() {
		return taskParams;
	}
	public void setTaskParams(HashMap<String,String> taskParams) {
		this.taskParams = taskParams;
	}
	public Context getCtx() {
		return ctx;
	}
	public void setCtx(Context ctx) {
		this.ctx = ctx;
	}
	
	
}
