package com.v_info.ajzclientv3.service;

import java.util.Stack;

import android.app.Activity;

public class ScreenManager {
	@SuppressWarnings("rawtypes")
	private static Stack activityStack;
	private static ScreenManager instance;

	public ScreenManager() {
	}

	public static ScreenManager getScreenManager() {
		instance = new ScreenManager();
		return instance;
	}

	// 退出栈顶Activity
	public void popActivity(Activity activity) {
		activity.finish();
		activityStack.remove(activity);
		activity = null;
	}

	// 获得当前栈顶Activity
	public Activity currentActivity() {
		Activity activity = (Activity)activityStack.lastElement();
		return activity;
	}

	// 将当前Activity推入栈中
	public void pushActivity(Activity activity) {
	//	activityStack = new Stack();
		if(activityStack ==null ){
			activityStack = new Stack();
		}

		activityStack.add(activity);
	}

	// 退出栈中所有Activity
	public void popAllActivity() {
		while (activityStack.size()!=0) {
			Activity activity = currentActivity();
			if (activity!= null){
				popActivity(activity);
			}
		}
	}
}