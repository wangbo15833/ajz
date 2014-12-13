package com.v_info.ajzclientv3.communication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import com.v_info.ajzclientv3.BaseActivity;
import com.v_info.ajzclientv3.net.Ajz;
import com.v_info.ajzclientv3.net.Chkinfo;
import com.v_info.ajzclientv3.net.DataShop;
import com.v_info.ajzclientv3.net.Gginfo;
import com.v_info.ajzclientv3.net.Msg;
import com.v_info.ajzclientv3.net.Msg2;
import com.v_info.ajzclientv3.net.Subinfo;
import com.v_info.ajzclientv3.util.ProtocolConst;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class NetWorker extends Thread {
	private final String TAG = "NetWorker";

	private static String IP = ProtocolConst.IP;
	public static int connectSta = 0;

	private static boolean netSta = true;
	private static boolean firstLogin = true;

	final static int PORT = 8018;

	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	Context mContext;
	protected final byte connect = 1;
	protected final byte running = 2;
	protected byte state = connect; // 状态（默认为连接状态）

	private boolean onWork = true;

	public NetWorker(Context context) {
		mContext = context;
	}

	public static String getIP() {
		return IP;
	}

	public static void setIP(String iP) {
		IP = iP;
	}

	public static String mFilePath = "";

	public NetWorker() {
		NetWorker.IP = Ajz.getServerip();
	}

	@Override
	public void run() {
		while (onWork) {
			switch (state) {
			case connect:
				connect();
				break;
			case running:
				receiveMsg();
				if (!Ajz.getSta()) {
					break;
				}
			}
		}

		try {
			if (socket != null) {
				socket.close();
			}
			if (dis != null) {
				dis.close();
			}
			if (dos != null) {
				dos.close();
			}
			if (Ajz.getSta()) {
				onWork = true;
				state = connect;
			} else {
				onWork = false;
			}
			if (!Ajz.isLogined()) {
				onWork = false;
				state = connect;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private synchronized boolean connect() {
		try {
			if (netSta) {
				socket = new Socket(IP, PORT);
				//当上一步不发生异常，就可判断连接建立了
				state = running;
				dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				connectSta = 1;
				if (!firstLogin) {
					getReLogin(Ajz.getAccount(), Ajz.getPsw());
				}
				return true;
			} else {
				Thread.sleep(5000);
				return false;
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		//当socket建立失败后，要3、4分钟才能捕获到下面异常
		catch (IOException e) {
			e.printStackTrace();
			if (Ajz.isLogined()) {
				Message msg = new Message();
				msg.what = ProcMsgConst.REFRESH_SERVERREFUSE;
				msg.obj = "";
			} else {
				onWork = false;
				Message msg = new Message();
				msg.what = ProcMsgConst.REFRESH_LOGIN_ERR;
				msg.obj = "请检查您的网络";
				BaseActivity.sendMessage(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return false;
		}
	}

	public synchronized void receiveMsg() {
		try {
			int type = dis.readInt();
			switch (type) {
			/*
			 * 收到登录状态
			 */
			case Config.SEN_LOGIN:
				handLogin();
				break;
			case Config.SEN_KICK:
				handKick();
				break;
			case Config.SEN_JDJL_LIST:
				handGetJdjlList();
				break;
			case Config.SEN_GGINFO_LIST:
				handGetGginfoList();
				break;
			case Config.SEN_MSG_LIST:
				handGetMsgList();
				break;
			/*
			 * 收到当天签到状态
			 */
			case Config.SEN_SIG_STA:
				handGetSigSta();
				break;
			/*
			 * 收到签到后结果信息
			 */
			case Config.SEN_SIG:
				handGetSig();
				break;
			case Config.SEN_RELOGIN:
				handRelogin();
				break;
			case Config.SEN_SUBINFO:
				handSubinfo();
				break;
			case Config.SEN_NEWCHK:
				handNewChk();
				break;
			case Config.SEN_UPIMG:
				handUpImg();
				break;
			case Config.SEN_CHKPICS:
				// handChkPics();
				break;
			case Config.SEN_NOTIFY:
				handNotify();
				break;
			case Config.SEN_MARKMSG:
				handMarkMsg();
				break;
			default:
				Log.i(TAG, "没有找到对应的处理方法");
				break;
			}
		} catch (Exception e) {
			if (Ajz.getSta()) {
				socket = null;
				state = connect;
			} else {
				Log.i(TAG, "receiveMsg 网络连结出现问题 ，因为用户退出!");
				socket = null;
				onWork = false;
			}

			// getReLogin(Ajz.getAccount(),Ajz.getPsw());
			// try {
			// synchronized(this){
			// wait();
			// }
			// } catch (InterruptedException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
		}
	}

	// public void addReceiveInfoListener(ReceiveInfoListener listener) {
	// listeners.add(listener);
	// }

	// public boolean receive(ChatMessage message){
	// Log.i(TAG, "NetWorker中的listener数为：" + listeners.size());
	//
	// //现阶段只有ChatActivity注册了ReceiveInfoListener,所以listeners里只有一个ReceiveInfoListener
	// ReceiveInfoListener listener = listeners.get(0);
	// boolean result = listener.receive(message);
	//
	// return result;
	// }

	/******************* 以下方法是客户端向服务器发送请求的方法 ************************/
	/**
	 * 获取检查监督列表（无图片）
	 */
	public void getJdjlList() {
		try {
			if (state != running) {
				return;
			}
			dos.writeInt(Config.GET_JDJL_LIST);
			dos.writeUTF(Ajz.getAccount());
			dos.flush();
			Log.i(TAG, "向服务器端发送获取监督列表请求......");
		} catch (IOException e) {
			Log.i(TAG, "向服务器端发送获取监督列表请求失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 获取首页公告
	 */
	public void getGginfoList(int curpage, int numperpage) {
		try {
			if (state != running) {
				return;
			}
			dos.writeInt(Config.GET_GGINFO_LIST);
			dos.writeUTF(Ajz.getAccount());
			dos.writeInt(curpage);
			dos.writeInt(numperpage);
			dos.flush();
			Log.i(TAG, "向服务器端发送获取公告列表请求......");
		} catch (IOException e) {
			Log.i(TAG, "向服务器端发送获取公告列表请求失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 获取消息
	 */
	public void getMsgList(int curpage, int numperpage) {
		try {
			if (state != running) {
				return;
			}
			dos.writeInt(Config.GET_MSG_LIST);
			dos.writeUTF(Ajz.getAccount());
			dos.writeUTF(Ajz.getRealname());
			dos.writeInt(curpage);
			dos.writeInt(numperpage);
			dos.flush();
			Log.i(TAG, "向服务器端发送获取消息列表请求......");
		} catch (IOException e) {
			Log.i(TAG, "向服务器端发送获取消息列表请求失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 登录
	 */

	public void getLogin(String userId, String pwd) {
		try {
			// if(state!=running){
			// return;
			// }
			for (int i = 0; i < 9; i++) {
				if (i == 8) {
					Log.i(TAG, "handConnectErr,连接错误，请检查设置");
					// handConnectErr();
					return;
				}
				//如果state没有变成running，那么就做for循环，如果变成running了，就跳出for循环
				if (state != running) {
					Thread.sleep(500);
				} else {
					break;
				}
			}
			Log.i(TAG, "login() userId=" + userId + ", pwd=" + pwd);
			dos.writeInt(Config.GET_LOGIN);
			dos.writeUTF(userId);
			dos.writeUTF(pwd);
			dos.flush();
			Log.i(TAG, "向服务器端发送登录请求");
		} catch (IOException e) {
			Log.i(TAG, "向服务器端发送登录请求失败");
			e.printStackTrace();
		} catch (InterruptedException e) {
			Log.i(TAG, "InterruptedException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 断线重登录
	 */

	public void getReLogin(String userId, String pwd) {
		try {
			// if(state!=running){
			// return;
			// }
			for (int i = 0; i < 9; i++) {
				if (i == 8) {
					Log.i(TAG, "handConnectErr,连接错误，请检查设置");
					// handConnectErr();
					return;
				}
				if (connectSta == 0) {
					Thread.sleep(500);
				} else {
					break;
				}
			}
			Log.i(TAG, "login() userId=" + userId + ", pwd=" + pwd);
			dos.writeInt(Config.GET_RELOGIN);
			dos.writeUTF(userId);
			dos.writeUTF(pwd);
			dos.flush();
			Log.i(TAG, "向服务器端发送重登录请求");
		} catch (IOException e) {
			Log.i(TAG, "向服务器端发送重登录请求失败");
			e.printStackTrace();
		} catch (InterruptedException e) {
			Log.i(TAG, "InterruptedException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 退出登录
	 */

	public void getQuite() {
		try {
			if (null != dos) {
				dos.writeInt(Config.GET_QUITE);
				dos.writeUTF(Ajz.getAccount());
				dos.flush();
			}
		} catch (IOException e) {
			Log.e(TAG, "NetWorker getQuite() 退出程序异常：" + e.toString());
		}
	}

	/**
	 * 获取签到状态
	 */
	public void getSigSta() {
		try {
			if (state != running) {
				Log.d(TAG, "NetWork is not working ");
				return;
			}
			dos.writeInt(Config.GET_SIG_STA);
			dos.writeUTF(Ajz.getAccount());
			dos.flush();
		} catch (IOException e) {
			Log.e(TAG, "NetWorker getSigSta() 退出程序异常：" + e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 签到
	 */
	public void getSig(String userid, String account, String username,
			String dept, String la, String ln) {
		try {
			if (state != running) {
				Log.d(TAG, "NetWork is not working ");
				return;
			}
			dos.writeInt(Config.GET_SIG);
			dos.writeUTF(account);
			dos.writeUTF(username);
			dos.writeUTF(userid);
			dos.writeUTF(dept);
			dos.writeUTF(la);
			dos.writeUTF(ln);
			dos.flush();
		} catch (IOException e) {
			Log.e(TAG, "NetWorker getSig() 退出程序异常：" + e.toString());
		}
	}

	/**
	 * 获取相关项目信息
	 */
	public void getSubinfo() {
		try {
			if (state != running) {
				Log.d(TAG, "NetWork is not working ");
				return;
			}
			dos.writeInt(Config.GET_SUBINFO);
			dos.writeUTF(Ajz.getAccount());
			dos.writeUTF(Ajz.getWorkUnit());
			dos.flush();
		} catch (IOException e) {
			Log.e(TAG, "NetWorker getSubinfo() 退出程序异常：" + e.toString());
		}
	}

	/**
	 * 获取相关项目信息
	 */
	public void getNewChkCont(String title, String cont, String chklb,
			String chkgcname, String subcode) {
		try {
			if (state != running) {
				Log.d(TAG, "NetWork is not working ");
				return;
			}
			dos.writeInt(Config.GET_NEWCHK);
			dos.writeUTF(Ajz.getAccount());
			dos.writeUTF(Ajz.getWorkUnit());
			dos.writeUTF(Ajz.getRealname());
			dos.writeUTF(title);
			dos.writeUTF(cont);
			dos.writeUTF(chklb);
			dos.writeUTF(chkgcname);
			dos.writeUTF(subcode);
			dos.flush();
		} catch (IOException e) {
			Log.e(TAG, "NetWorker getSubinfo() 退出程序异常：" + e.toString());
		}
	}

	/**
	 * 发送图片
	 * 
	 * @param self
	 * @param receiver
	 * @param time
	 * @param filePath
	 * @return
	 */
	// jlcode,account,username,filename,comment
	public boolean getSenImg(String jlcode, String comment, String filePath) {
		boolean result = true;
		mFilePath = filePath;
		String account = Ajz.getAccount();
		String username = Ajz.getRealname();
		if (state != running) {
			Log.d(TAG, "NetWork is not working ");
			return false;
		}
		try {
			dos.writeInt(Config.GET_UPIMG);
			dos.writeUTF(account);
			dos.writeUTF(jlcode);
			dos.writeUTF(username);
			dos.writeUTF(comment);
			dos.flush();

			(new Thread() {
				public void run() {
					try {
						readFileSendData(mFilePath);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();

			Log.i(TAG, "用户" + account + "发送图片完毕：");

		} catch (IOException e) {
			Log.e(TAG, "发送图片失败getSenImg() exception:" + e.toString());
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/**
	 * 获取相关检查的所有图片信息
	 * 
	 * @param jlcode
	 * @return
	 */
	public boolean getChkPic(String jlcode) {
		boolean result = true;

		String account = Ajz.getAccount();
		String username = Ajz.getRealname();
		if (state != running) {
			Log.d(TAG, "NetWork is not working ");
			return false;
		}
		try {
			dos.writeInt(Config.GET_CHKPICS);
			dos.writeUTF(account);
			dos.writeUTF(username);
			dos.writeUTF(jlcode);
			dos.flush();

			Log.i(TAG, "用户" + account + "请求获取：" + jlcode + " 的所有图片信息");

		} catch (IOException e) {
			Log.e(TAG, "获取检查图片信息出错getChkPic() exception:" + e.toString());
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/**
	 * 标记某消息为已读
	 * 
	 * @param msgCode
	 * @return
	 */
	public void getMarkMsg(String msgCode, String msgtype) {
		String account = Ajz.getAccount();
		String username = Ajz.getRealname();
		if (state != running) {
			Log.d(TAG, "NetWork is not working ");
			return;
		}
		try {
			dos.writeInt(Config.GET_MARKMSG);
			dos.writeUTF(account);
			dos.writeUTF(username);
			dos.writeUTF(msgtype);
			dos.writeUTF(msgCode);
			dos.flush();

			Log.i(TAG, "用户" + account + "请求标记：" + msgCode + " 为已读");

		} catch (IOException e) {
			Log.e(TAG, "用户" + account + "请求标记：" + msgCode + " 为已读时出错getMarkMsg"
					+ e.toString());
			e.printStackTrace();
		}

	}

	private void readFileSendData(String filePath)
			throws FileNotFoundException, IOException {
		DataInputStream ddis = new DataInputStream(
				new FileInputStream(filePath));
		int length = 0;
		int totalNum = 0;
		byte[] buffer = new byte[1024];
		Log.i(TAG, "img.avaliable=" + ddis.available());
		Log.i(TAG, "img. path =" + filePath);

		while ((length = ddis.read(buffer)) != -1) {
			totalNum += length;
			// Log.i(TAG,"----------"+totalNum);
			dos.writeInt(length);
			dos.write(buffer, 0, length);
			dos.flush();
		}
		dos.writeInt(0);
		dos.flush();
	}
	
/*
 * 以下是处理服务器发过来的信息
 */

	public void handConnectErr() {
		connectSta = 0;
		onWork = false;
		Message msg = new Message();
		msg.what = ProcMsgConst.REFRESH_LOGIN_ERR;
		msg.obj = "连接服务器出错，请检查您的网络状态和服务器地址";
		BaseActivity.sendMessage(msg);
	}

	public void handLogin() {
		Log.i(TAG, "得到登录请求结果");
		String jsons = null;
		try {
			jsons = dis.readUTF();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "登录出错：" + e.toString());
		}
		Message msg = new Message();
		msg.what = ProcMsgConst.REFRESH_LOGIN;
		msg.obj = jsons;
		if (msg.obj == null) {
			msg.obj = " ";
		}
		BaseActivity.sendMessage(msg);
	}

	public void handRelogin() {
		String jsons = null;
		try {
			jsons = dis.readUTF();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "登录出错：" + e.toString());
		}
		if (null != jsons) {
			if (jsons.equals("true")) {
				Log.i(TAG, "重登录成功");
			}
		}
	}

	public void handKick() {
		String info = null;
		try {
			info = dis.readUTF();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "被踢出错：" + e.toString());
		}
		getQuite();
		Message msg = new Message();
		msg.what = ProcMsgConst.REFRESH_KICK;
		msg.obj = info;
		BaseActivity.sendMessage(msg);
	}

	public void handGetJdjlList() {
		Log.i(TAG, "得到监督列表");
		try {
			String jsons = dis.readUTF();
			// JSONObject json = new JSONObject(jsons);
			JSONArray achkinfo = new JSONArray(jsons);
			DataShop.clearChk();
			for (int i = 0; i < achkinfo.length(); i++) {
				Chkinfo chkinfo = new Chkinfo();
				chkinfo.setCode(achkinfo.optJSONObject(i).optString("jl_code"));
				chkinfo.setTime(achkinfo.optJSONObject(i).optString("jdtime"));
				chkinfo.setCont(achkinfo.optJSONObject(i).optString("cont"));
				chkinfo.setTitle(achkinfo.optJSONObject(i).optString("title"));
				chkinfo.setGcname(achkinfo.optJSONObject(i).optString("gcname"));
				chkinfo.setChklb(achkinfo.optJSONObject(i).optString("jdlb"));
				DataShop.addChk(chkinfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "handLogin() exception=" + e.toString());
		}
	}

	public void handGetGginfoList() {
		Log.i(TAG, "得到公告列表");
		try {
			String jsons = dis.readUTF();
			// JSONObject json = new JSONObject(jsons);
			JSONArray agginfo = new JSONArray(jsons);
			DataShop.clearGg();
			for (int i = 0; i < agginfo.length(); i++) {
				Gginfo gginfo = new Gginfo();
				gginfo.setCode(agginfo.optJSONObject(i).optString("code"));
				gginfo.setTime(agginfo.optJSONObject(i).optString("time"));
				gginfo.setCont(agginfo.optJSONObject(i).optString("cont"));
				gginfo.setTitle(agginfo.optJSONObject(i).optString("title"));
				gginfo.setSta(agginfo.optJSONObject(i).optString("sta"));
				DataShop.addGg(gginfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "解析公告列表出错：" + e.toString());
		}
		Message msg = new Message();
		msg.what = ProcMsgConst.REFRESH_GG;
		msg.obj = " ";
		BaseActivity.sendMessage(msg);
	}

	public void handSubinfo() {
		Log.i(TAG, "得到相关项目信息");
		try {
			String jsons = dis.readUTF();
			JSONArray asub = new JSONArray(jsons);
			List<Subinfo> subinfo = new ArrayList<Subinfo>();
			for (int i = 0; i < asub.length(); i++) {
				Subinfo sub = new Subinfo();
				sub.setSubcode(asub.optJSONObject(i).optString("subcode"));
				sub.setSubname(asub.optJSONObject(i).optString("subname"));
				sub.setLatitude(asub.optJSONObject(i).optDouble("la"));
				sub.setLongtitude(asub.optJSONObject(i).optDouble("ln"));
				subinfo.add(sub);
			}
			Ajz.setSubList(subinfo);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "解析相关项目信息出错：" + e.toString());
		}
	}

	public void handGetMsgList() {
		Log.i(TAG, "得到消息列表");
		try {
			String jsons = dis.readUTF();
			// JSONObject json = new JSONObject(jsons);
			JSONArray amsg = new JSONArray(jsons);
			DataShop.clearMsg();
			Msg.setNewMsgNum(0);
			DataShop.clearMsg2();
			Msg2.setNewMsgNum(0);
			for (int i = 0; i < amsg.length(); i++) {
				Msg msg = new Msg();
				Msg2 msg2 = new Msg2();
				if ((amsg.optJSONObject(i).optString("msglb")).equals("通知")) {
					//设置消息编码
					msg.setMsgCode(amsg.optJSONObject(i).optString("msgcode"));
					//设置消息时间
					msg.setMsgTime(amsg.optJSONObject(i).optString("msgtime"));
					//设置消息内容
					msg.setCont(amsg.optJSONObject(i).optString("cont"));
					//设置消息标题
					msg.setMsgTitle(amsg.optJSONObject(i).optString("msgtitle"));
					//设置消息类别
					msg.setMsglb(amsg.optJSONObject(i).optString("msglb"));
					//设置消息作者
					msg.setAuth(amsg.optJSONObject(i).optString("auth"));
					//设置消息账户
					msg.setAuthAccount(amsg.optJSONObject(i).optString(
							"auth_account"));
					msg.setAuthDept(amsg.optJSONObject(i)
							.optString("auth_dept"));
					msg.setIsRead(amsg.optJSONObject(i).optString("isread"));
					if (!msg.isIsRead()) {
						Msg.setNewMsgNum(Msg.getNewMsgNum() + 1);
					}
					DataShop.addMsg(msg);
				} else {
					msg2.setMsgCode(amsg.optJSONObject(i).optString("msgcode"));
					msg2.setMsgTime(amsg.optJSONObject(i).optString("msgtime"));
					msg2.setCont(amsg.optJSONObject(i).optString("cont"));
					msg2.setMsgTitle(amsg.optJSONObject(i)
							.optString("msgtitle"));
					msg2.setMsglb(amsg.optJSONObject(i).optString("msglb"));
					msg2.setAuth(amsg.optJSONObject(i).optString("auth"));
					msg2.setAuthAccount(amsg.optJSONObject(i).optString(
							"auth_account"));
					msg2.setAuthDept(amsg.optJSONObject(i).optString(
							"auth_dept"));
					msg2.setIsRead(amsg.optJSONObject(i).optString("isread"));
					if (!msg2.isIsRead()) {
						Msg2.setNewMsgNum(Msg2.getNewMsgNum() + 1);
					}
					DataShop.addMsg2(msg2);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		Message msg = new Message();
		msg.what = ProcMsgConst.REFRESH_MSG;
		msg.obj = "";
		BaseActivity.sendMessage(msg);
	}

	public void handGetSigSta() {
		String sta = "";
		try {
			/*
			 * 当没有签到时：sta = "nosigup"
			 */
			sta = dis.readUTF();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Message msg = new Message();
		msg.what = ProcMsgConst.REFRESH_SIGSTA;
		msg.obj = sta;
		BaseActivity.sendMessage(msg);
	}

	public void handGetSig() {
		String sta = "";
		try {
			sta = dis.readUTF();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.e(TAG, "接收签到结果时出错 handGetSigSta()");
		}
		Message msg = new Message();
		msg.what = ProcMsgConst.REFRESH_SIG;
		msg.obj = sta;
		BaseActivity.sendMessage(msg);
	}

	public void handNewChk() {
		JSONObject jsono = null;
		String json = null;
		String jlcode = null;
		try {
			json = dis.readUTF();
			jsono = new JSONObject(json);
			// sta=jsono.optBoolean("sta", false);
			jlcode = jsono.optString("jlcode", "增加失败");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Message msg = new Message();
		msg.what = ProcMsgConst.REFRESH_NEWCHK;
		msg.obj = jlcode;
		BaseActivity.sendMessage(msg);
		getJdjlList();// 刷新记录列表
	}

	/*
	 * public void handChkPics(){ String json=null; try { Log.i(TAG,
	 * "得到检查相关的图片信息"); json = dis.readUTF();
	 * 
	 * //String returnResult = (String) params[1];
	 * ChkImgListActivity.mListItem.clear();//先清空一下列表 System.out.println(json);
	 * JSONArray jsona = new JSONArray(json); Log.d(TAG,jsona.toString());
	 * String tempfilename=null; for(int i=0;i<jsona.length();i++){ PicItem pi =
	 * new PicItem(); tempfilename=jsona.getJSONObject(i).getString("filename");
	 * pi.picurl="http://"+Ajz.getServerip()+":8080/ajz/uploadfile/images/img/"
	 * +tempfilename.split("\\.")[0]+"_min."+tempfilename.split("\\.")[1];
	 * pi.comment=jsona.getJSONObject(i).getString("comment");
	 * pi.uptime=jsona.getJSONObject(i).getString("uptime"); try{ InputStream is
	 * = ChkImgListActivity.getNetInputStream(pi.picurl); Bitmap bitmap =
	 * BitmapFactory.decodeStream(is); is.close();
	 * //(pi.im).setImageBitmap(bitmap); pi.im=bitmap; }catch (Exception
	 * e){e.printStackTrace();} ChkImgListActivity.mListItem.add(pi); }
	 * if(jsona.length()==0){ PicItem p =new PicItem(); p.uptime="";
	 * p.picurl="http://"
	 * +Ajz.getServerip()+":8080/ajz/uploadfile/images/img/no.jpg";
	 * p.comment="此查检没有上传图"; } // Log .e(TAG,"here!"); //
	 * myAdapter.notifyDataSetChanged(); // submit.setEnabled(true); //
	 * submit.setText("上传\n图片"); // //Bitmap
	 * bp=BitmapFactory.decodeResource((ChkImgListActivity.this.getResources()),
	 * R.drawable.add_account_p); // //chkItemImage.setImageBitmap(bp); //
	 * picPath=""; // // ///////////////
	 * 
	 * 
	 * } catch (Exception e) { // TODO: handle exception e.printStackTrace();
	 * Log.e(TAG,"得到检查相关的图片出错 handChkPics()"); } Message msg=new Message();
	 * msg.what = ProcMsgConst.REFRESH_CHKPICS; msg.obj = json;
	 * BaseActivity.sendMessage(msg); }
	 */
	/**
	 * 收到上传图片返回的信息
	 */
	public void handUpImg() {
		String dbresult = null;
		String filename = null;
		try {
			Log.i(TAG, "得到上传返回的图片信息");
			dbresult = dis.readUTF();
			filename = dis.readUTF();
			Log.i(TAG, "反回的两个结果是 " + dbresult + " : " + filename);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.e(TAG, "得到检查相关的图片出错 handGetSigSta()");
		}
		Message msg = new Message();
		msg.what = ProcMsgConst.REFRESH_UPIMG;
		msg.obj = filename;
		BaseActivity.sendMessage(msg);
	}

	/**
	 * 收到更新消息
	 */
	public void handNotify() {
		String noti = null;
		try {
			Log.i(TAG, "得到更新消息的信息!!!");
			noti = dis.readUTF();
			if (noti.equals("newtongz") || noti.equals("newtxinfo")) {
				getMsgList(1, 15);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.e(TAG, "更新消息出错 handNotify()");
		}
	}

	/**
	 * 收到标记消息的返回
	 */
	public void handMarkMsg() {
		JSONObject jsono = null;
		String jsons = null;
		try {
			jsons = dis.readUTF();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.e(TAG, "得到检查相关的图片出错 handGetSigSta()");
		}
		Message msg = new Message();
		msg.what = ProcMsgConst.REFRESH_MARKMSG;
		msg.obj = jsons;
		BaseActivity.sendMessage(msg);
	}

	private void receiveDataWriteFile(String filePath)
			throws FileNotFoundException, IOException {
		DataOutputStream ddos = new DataOutputStream(new FileOutputStream(
				filePath)); // 将语音或图片写入本地SD卡
		int length = 0;
		int totalNum = 0;
		byte[] buffer = new byte[2048];
		while ((length = dis.readInt()) != 0) {
			length = dis.read(buffer, 0, length);
			totalNum += length;
			ddos.write(buffer, 0, length);
			ddos.flush();
		}

		if (ddos != null) {
			try {
				ddos.close();
				ddos = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Log.i(TAG, "receiveDataWriteFile(): receive bytes=" + totalNum);
	}

	public void setOnWork(boolean Work) {

		if (Work) {
			this.onWork = Work;
		} else {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.onWork = Work;
		}
	}

	public boolean writeBuf(byte[] data) {
		int length = data.length;
		try {
			dos.write(data);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void setNetSta(boolean b) {
		// TODO Auto-generated method stub
		netSta = b;

	}

	public static boolean isFirstLogin() {
		return firstLogin;
	}

	public static void setFirstLogin(boolean firstLogin) {
		NetWorker.firstLogin = firstLogin;
	}

}
