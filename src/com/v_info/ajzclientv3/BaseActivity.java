package com.v_info.ajzclientv3;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.v_info.ajzclientv3.bean.User;
import com.v_info.ajzclientv3.communication.Communication;
import com.v_info.ajzclientv3.communication.ProcMsgConst;
import com.v_info.ajzclientv3.interFace.IFaceActivity;
import com.v_info.ajzclientv3.interFace.LocSer;
import com.v_info.ajzclientv3.net.Ajz;
import com.v_info.ajzclientv3.service.MainService;
import com.v_info.ajzclientv3.service.ScreenManager;
import com.v_info.ajzclientv3.util.DatabaseUtil;
import com.v_info.ajzclientv3.util.ProtocolConst;
import com.v_info.ajzclientv3.widget.MyToast;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {

	protected static LinkedList<BaseActivity> queue = new LinkedList<BaseActivity>();
	public static Map<String, IFaceActivity> mp = new HashMap<String, IFaceActivity>();
	public static final String communication = "请稍后，正在通信……";
	public static final String communication_faild = "对不起，通信失败！";
	protected static Communication con;
	private static MediaPlayer player;
	private static Vibrator vibrator;
	protected static DatabaseUtil dbUtil;
	private static final String TAG = "BaseActivity";
	public String tagName = "BaseActivity";// 每一个对象都单独有企这个数据

	private static final String PREFERENCE_NAME = "ajz.pre";
	private static final String USERID = "userId";
	private static final String PWD = "pwd";
	private int backCount = 0;

	public static DisplayMetrics dm;
	final int EXIT_DIALOG = 0x12;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 如果当前Activity不在链表中，那么就加入进去
		if (!queue.contains(this))
			queue.add(this);
		if (!mp.containsKey(this.tagName)) {
			Log.d("AjzBaseActivity.onCreate", "添加到mp,当前的tag是： " + this.tagName);
			mp.put(this.tagName, (IFaceActivity) this);
		} else {
			Log.d("AjzBaseActivity.onCreate", "更新mp,当前的tag是： " + this.tagName);
			mp.put(this.tagName, (IFaceActivity) this);
		}
		if (dbUtil == null) {
			dbUtil = new DatabaseUtil(this);
		}
		if (null == vibrator) {
			vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		}
		// 设置为无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Log.i(TAG, "目前Activity number=" + queue.size());
	}

	public static void setPlayerOn(Context c) {
		if (player != null) {
			player.release();
			player = null;
		}
		if (Ajz.getRing().equals("tone2")) {
			player = MediaPlayer.create(c, R.raw.tone2);
		} 
		else {
		//public static MediaPlayer create(Context context,int resid)
		//context 所用容器
		//resid 资源ID
		
			player = MediaPlayer.create(c, R.raw.tone1);
		}
		
		Log.i(TAG, TAG + " player=" + player + "\nandplay : " + Ajz.getRing());

		try {
			player.stop();
			player.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static DatabaseUtil getDbUtil() {
		return dbUtil;
	}

	public static void setDbUtil(DatabaseUtil dbUtil) {
		BaseActivity.dbUtil = dbUtil;
	}

	public static BaseActivity getActivity(int index) {
		if (index < 0 || index >= queue.size())
			throw new IllegalArgumentException("out of queue");
		return queue.get(index);
	}

	public static BaseActivity getCurrentActivity() {
		return queue.getLast();
	}

	public void makeTextShort(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	public void makeTextLong(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	public abstract void processMessage(Message msg);

	public static void sendMessage(int cmd, String text) {
		Message msg = new Message();
		msg.what = cmd;
		msg.obj = text;
		sendMessage(msg);
	}

	public static void sendMessage(Message msg) {
		handler.sendMessage(msg);
	}

	public static void sendEmptyMessage(int what) {
		handler.sendEmptyMessage(what);
	}

	// ////
	public void backCountTick() {
		backCount++;
	}

	public void backCountReset() {
		backCount = 0;
	}

	public int getBackCount() {
		return backCount;
	}

	private static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ProtocolConst.CMD_SYSTEM_INFO: {
				queue.getLast().makeTextShort(msg.obj.toString());
			}
				break;
			case ProtocolConst.CMD_SYSTEM_ERROR: {
				queue.getLast().makeTextShort(msg.obj.toString());
			}
				break;
			case ProtocolConst.CMD_PLAY_MSG: {
				playMsg();
			}
				break;
			case ProcMsgConst.REFRESH_LOGIN: {
				try {
					IFaceActivity IFace = BaseActivity.mp.get("LoginActivity");
					Log.d(TAG, "rerfresh-----" + msg.obj);
					IFace.refresh(msg.obj.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
				break;
			case ProcMsgConst.REFRESH_GG: {
				IFaceActivity IFace = BaseActivity.mp.get("HomeActivity");
				IFace.refresh(msg.obj.toString());
			}
				break;
			case ProcMsgConst.REFRESH_CHK: {
				IFaceActivity IFace = BaseActivity.mp.get("ChkActivity");
				IFace.refresh(msg.obj.toString());
			}
				break;
			case ProcMsgConst.REFRESH_MSG: {
				if (mp.containsKey("MsgActivity")) {
					IFaceActivity IFace = BaseActivity.mp.get("MsgActivity");
					IFace.refresh(msg.obj.toString());
					playMsg();
				} else {
					Intent intent = new Intent();
					intent.setClass(BaseActivity.getCurrentActivity(),
							MsgActivity.class);
					BaseActivity.getCurrentActivity().startActivity(intent);
				}
			}
				break;
			case ProcMsgConst.REFRESH_KICK: {
				MyToast.showMyToast(getCurrentActivity(), MyToast.KICK);
				con.stopWork();
				MainService.exit();
				Intent serviceintent = new Intent(
						BaseActivity.getCurrentActivity(), MainService.class);
				BaseActivity.getCurrentActivity().stopService(serviceintent);
				Intent intent = new Intent();
				intent.setClass(BaseActivity.getCurrentActivity(),
						ReLoginActivity.class);
				BaseActivity.getCurrentActivity().startActivity(intent);
				// /
				BaseActivity.getCurrentActivity().exit();
			}
				break;
			case ProcMsgConst.REFRESH_LOGIN_ERR: {
				IFaceActivity IFace = BaseActivity.mp.get("LoginActivity");
				if (null != IFace) {
					IFace.refresh(msg.obj.toString());
				}
			}
				break;
			case ProcMsgConst.REFRESH_SIGSTA: {
				IFaceActivity IFace = BaseActivity.mp.get("SigActivity");
				IFace.refresh(msg.obj.toString());
			}
				break;
			case ProcMsgConst.REFRESH_SIG: {
				IFaceActivity IFace = BaseActivity.mp.get("SigActivity");
				IFace.refresh(msg.obj.toString());
			}
				break;
			case ProcMsgConst.REFRESH_RELOGIN: {
				con.getGginfoList(1, 15);
				con.getSigSta();
				con.getMsgList(1, 15);
			}
				break;
			case ProcMsgConst.REFRESH_SERVERREFUSE: {
				MainService.exit();
				Toast.makeText(BaseActivity.getCurrentActivity(),
						"服务器故障不能连接，请与管理员联系", Toast.LENGTH_LONG).show();

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				con.getQuite();
				con.stopWork();
				Intent serviceintent = new Intent(
						BaseActivity.getCurrentActivity(), MainService.class);
				BaseActivity.getCurrentActivity().stopService(serviceintent);
				BaseActivity.getCurrentActivity().exit();
				new ScreenManager().popAllActivity();

			}
				break;
			case ProcMsgConst.REFRESH_NEWCHK: {
				IFaceActivity IFace = BaseActivity.mp.get("ChkNewActivity");
				if (null != IFace) {
					IFace.refresh(msg.obj.toString());
				}
			}
				break;
			case ProcMsgConst.REFRESH_UPIMG: {
				IFaceActivity IFace = BaseActivity.mp.get("ChkImgListActivity");
				if (null != IFace) {
					IFace.refresh(1, msg.obj.toString());
				}
			}
				break;
			case ProcMsgConst.REFRESH_CHKPICS: {
				IFaceActivity IFace = BaseActivity.mp.get("ChkImgListActivity");
				if (null != IFace) {
					IFace.refresh(2, msg.obj.toString());
				}
			}
				break;
			case ProcMsgConst.REFRESH_BIGIMG: {
				IFaceActivity IFace = BaseActivity.mp.get("ChkBigImgActivity");
				if (null != IFace) {
					IFace.refresh(1, msg.obj.toString());
				}
			}
				break;
			case ProcMsgConst.REFRESH_MARKMSG: {
				try {
					JSONObject jsono = new JSONObject(msg.obj.toString());
					String msgtype = jsono.optString("msgtype");
					if (msgtype.equals("通知")) {
						IFaceActivity IFace = BaseActivity.mp
								.get("TongzDetailActivity");
						if (null != IFace) {
							IFace.refresh(jsono.optString("sta"));
						}
					} else if (msgtype.equals("提醒")) {
						IFaceActivity IFace = BaseActivity.mp
								.get("TxinfoDetailActivity");
						if (null != IFace) {
							IFace.refresh(jsono.optString("sta"));
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
				break;
			default:
				if (!queue.isEmpty()) {
					queue.getLast().processMessage(msg);
				}
				break;
			}
		}
	};

	private static void playMsg() {
		try {
			if (!Ajz.getRing().equals("0")) {
				player.start();
			}
			if (Ajz.getVibrate()) {
				long[] pattern = { 80, 200, 80, 200, 80, 200 };
				vibrator.vibrate(pattern, -1);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	public static String getTime() {
		return DateFormat.format("hh:mm:ss", Calendar.getInstance()).toString();
	}

	@SuppressWarnings("deprecation")
	public void sendNotifycation() {
		// 发送Notification
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(2, "新消息",
				System.currentTimeMillis());
		Intent intent = new Intent(this, MsgActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, 0);
		notification.setLatestEventInfo(this, "新消息", "查看新消息", pendingIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		manager.notify(0, notification);
	}

	@Override
	public void onBackPressed() {
		// Log.i(TAG, "Activity number="+queue.size());
		// if(queue.size()==1){ //当前Activity是最后一个Activity了
		// showDialog(EXIT_DIALOG);
		// }else{
		// queue.getLast().finish();
		// }
		if (getBackCount() >= 1) {
			Log.d(TAG, "连按了返回，应该回到桌面");
			Intent home = new Intent(Intent.ACTION_MAIN);
			home.addCategory(Intent.CATEGORY_HOME);
			startActivity(home);
		} else {

			Toast.makeText(this, "再按一次反回桌面", Toast.LENGTH_SHORT).show();
			backCountTick();
			(new Thread() {
				public void run() {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					backCountReset();
				}
			}).start();
		}

	}

	@Override
	public void finish() {
		if (!queue.isEmpty()) {
			queue.removeLast();
		}
		if (null != mp.get(tagName)) {
		}
		super.finish();
	}

	public void exit() {
		try {
			if (null != mp.get("SigActivity")) {
				Log.i(TAG, "得到了SigActivity");
				((LocSer) mp.get("SigActivity")).locSerShtDn();
			}
			con.stopWork();
			if (dbUtil != null) {
				dbUtil = null;
			}
			if (player != null)
				player.release();
			player = null;
			for (String key : mp.keySet()) {
				if (null != mp.get(key)) {
					if (!key.equals(this.tagName)) {
						((Activity) mp.get(key)).finish();
					}
				}
			}
			this.finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		Log.i(TAG, "dialog id=" + id);
		switch (id) {
		case EXIT_DIALOG: {
			Log.i(TAG, "要弹出的是退出提醒对话框");
			builder.setMessage("退出？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									con.getQuite();
									exit();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							});
		}
			break;
		}
		AlertDialog dialog = builder.create();
		Log.i(TAG, "dialog=" + dialog);
		return dialog;
	}

	/*
	 * private void showExitDialog(){ AlertDialog.Builder builder=new
	 * AlertDialog.Builder(getApplicationContext()); builder.setMessage("退出？")
	 * .setPositiveButton("确定", new DialogInterface.OnClickListener(){
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * con.getQuite(); exit(); } }) .setNegativeButton("取消", new
	 * DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * 
	 * } }); builder.create().show(); }
	 */
	protected void setPreference(String userId, String pwd) {
		SharedPreferences sp = getSharedPreferences(PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(USERID, userId);
		editor.putString(PWD, pwd);
		editor.commit();
	}

	protected User getPreference() {
		User user = new User();
		SharedPreferences sp = getSharedPreferences(PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		String userId = sp.getString(USERID, "");
		String pwd = sp.getString(PWD, "");
		user.setUserId(userId);
		user.setPwd(pwd);
		return user;
	}
}
