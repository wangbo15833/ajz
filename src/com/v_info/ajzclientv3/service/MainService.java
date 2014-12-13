package com.v_info.ajzclientv3.service;

import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.json.JSONException;

import com.v_info.ajzclientv3.interFace.IFaceActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;


public class MainService extends Service implements Runnable,IService{
  
	private static final String TAG = "MainService";
	private static ArrayList<IFaceActivity> allActivity=new ArrayList<IFaceActivity>();
	private static ArrayList<Task> allTasks=new ArrayList<Task>();
	public static boolean isRun=false;
	
	public static HttpResponse httpResponse;
	public static int sleeptime = 2000;//
	public static int loopgaptime = 2;//
	

	public static void newTask(Task t){

		android.util.Log.d(TAG," newTask(Task t) to add a new task!");
		allTasks.add(t);
	}
	public static void addActivity(IFaceActivity iw){
		allActivity.add(iw);
	}
	public static void removeActivity(IFaceActivity iw){
		allActivity.remove(iw);
	}
	public static IFaceActivity getActivityByName(String name){
		for(IFaceActivity iw:allActivity){
			if(iw.getClass().getName().indexOf(name)>=0){
				return iw;
			}
		}
		return null;
	}
	@Override
	public void onCreate() {
//		Log.e("=============================", "MainService    onCreate()");
		super.onCreate();
		
		isRun=true;
		new Thread(this).start();
		
	}
	

	public void onStart() {
		
//		isRun=true;
//		url=Ajz.getSERVER();
//		httpPost = new HttpPost(url);
//		new Thread(this).start();
//		Log.e("=============================", "MainService    onStart()");
		isRun=true;
	}
	
	@Override
	public void onDestroy() {
		isRun=false;
		super.onDestroy();
	}
	
	public static void exit(){
		isRun=false;
	}
	@Override
	public void run() {
		
//		Log.d(TAG,"the run is execute and the isRun :"+isRun);
		while(isRun){
			try{
				if(allTasks.size()>0)
				{
			     //执行任务
	            doTask(allTasks.get(0));
				}else{
					try{
						Thread.sleep(loopgaptime*1000);
					}catch(Exception e){}
//					Log.e("------===------", "MainService   doing   nothing    ");
				}
			}catch(Exception e){
				if(allTasks.size()>0)
					allTasks.remove(allTasks.get(0));
//				Log.d("error","------------------"+e);
			}
		}
	}
   private static Handler handler=new Handler(){

	@Override
	public void handleMessage(Message msg) {
		// 
		super.handleMessage(msg);
		
		switch (msg.what) {
		case Task.HOMEINFO://首页公告
			MainService.getActivityByName("HomeActivity").refresh(msg.obj);
//			Log.d(TAG,"the handler get the HOMEINFO info (gginfo)");
			break;
		case Task.LOGIN://登录
			MainService.getActivityByName("LoginActivity").refresh(msg.obj);
			break;
		case Task.SIGNIN://签到
			MainService.getActivityByName("SigActivity").refresh(msg.obj);
		    break;
		case Task.CHKSEND://发送检查
			/*MainService.getActivityByName("MsgActivity")
			.refresh(msg.obj);*/
			break;
		case Task.MSGSEND://发送消息
			MainService.getActivityByName("NewAjzActivity111").refresh(msg.obj);
			break;
		case Task.MSGRECIEVEFORE://前台接收消息
			MainService.getActivityByName("ViewWeiBoActivity").refresh(msg.obj);
			break;
		case Task.MSGRECIEVEBACK://后台接收消息
			MainService.getActivityByName("ViewWeiBoActivity").refresh(msg.obj);
				break;
		case Task.QRYSEND://发关查询
			MainService.getActivityByName("ViewWeiBoActivity").refresh(msg.obj);
				break;
		default:
			break;
		}
	}
	   
   };
	private void doTask(Task task) throws JSONException {
		// 
		Message msg=handler.obtainMessage();
		msg.what=task.getTaskId();
		
		switch (task.getTaskId()) {
		case Task.LOGIN://登录
//			Log.d(TAG," doTask(Task task) to process login info!");
			
			break;
		case Task.HOMEINFO://首页公告
//			Log.d(TAG," doTask(Task task) to process Home(gginfo) info!");
			
			break;
		case Task.SIGNIN://签到
//			Log.d(TAG," doTask(Task task) to process sig info!");
			
		    break;
		case Task.CHKSEND://发送检查
				  try {
					String content=task.getTaskParams().get("msg").toString();
					//Ajz.getInstance().updateStatus(task.getCtx(),content);
					msg.obj=content;
				} catch (Exception e) {
					// 
					e.printStackTrace();
				}
			break;
		case Task.MSGSEND://发送消息

			try {
				//String weiboid=task.getTaskParams().get("weiboid").toString();
				String returnStatus  = "789";//jz.getInstance().showStatus(task.getCtx(),weiboid);
				msg.obj=returnStatus;
			} catch (Exception e) {
				// 
				e.printStackTrace();
			}

		break;
		case Task.MSGRECIEVEFORE://前台接收消息
			 try {
					String content=task.getTaskParams().get("msg").toString();
					//Ajz.getInstance().updateStatus(task.getCtx(),content);
					msg.obj=content;
				} catch (Exception e) {
					// 
					e.printStackTrace();
				}
			break;
		case Task.MSGRECIEVEBACK://后台接收消息
			 try {
					String content=task.getTaskParams().get("msg").toString();
					//Ajz.getInstance().updateStatus(task.getCtx(),content);
					msg.obj=content;
				} catch (Exception e) {
					e.printStackTrace();
				}
			break;
		case Task.QRYSEND://发关查询
			 try {
					String content=task.getTaskParams().get("msg").toString();
					//Ajz.getInstance().updateStatus(task.getCtx(),content);
					msg.obj=content;
				} catch (Exception e) {
					// 
					e.printStackTrace();
				}
			break;
		default:
			break;
		}
		allTasks.remove(task);
		//通知主线程更新UI
		handler.sendMessage(msg);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void destory() {
		stopSelf();
	}
	
}
