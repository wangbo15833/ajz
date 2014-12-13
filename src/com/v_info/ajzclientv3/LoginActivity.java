package com.v_info.ajzclientv3;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.v_info.ajzclientv3.communication.Communication;
import com.v_info.ajzclientv3.communication.NetWorker;
import com.v_info.ajzclientv3.interFace.IFaceActivity;
import com.v_info.ajzclientv3.net.Ajz;
import com.v_info.ajzclientv3.service.MainService;
import com.v_info.ajzclientv3.service.ScreenManager;
import com.v_info.ajzclientv3.service.Tools;
import com.v_info.ajzclientv3.util.ProtocolConst;
import com.v_info.ajzclientv3.widget.MyToast;

/**
 * 该Activity用于输入并提交登录信息
 */
public class LoginActivity extends BaseActivity implements IFaceActivity {

	private static final String TAG = "LoginActivity";

	// private static final String IP = "121.22.91.89";//192.168.1.201/
	private static final String IP = ProtocolConst.IP;

	private static final String SpName = "com.ajzclient_preferences";
	public static SharedPreferences mSharedPreference;
	public static SharedPreferences.Editor mEditor;

	// 用户名输入框
	public static EditText edtusr;
	// 密码输入框
	public static EditText edtpsd;

	// 是否记住密码
	public static CheckBox rempsw;
	// 是否震动
	public static CheckBox vibrate;
	// 是否静音
	public static CheckBox ring;

	// 登录按钮
	public static Button login;

	// 清除账户按钮
	public static ImageButton ImageButton02;
	// public static Button testbutton;

	public static HashMap<String, String> hp = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.tagName = TAG;
		super.onCreate(savedInstanceState);
		new ScreenManager().pushActivity(this);
		// 因BaseActivity中有这句，所以去掉
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		setContentView(R.layout.login);

		mSharedPreference = getSharedPreferences(SpName, 0);
		mEditor = mSharedPreference.edit();
		// 登录按钮
		login = (Button) findViewById(R.id.login);
		// 记住密码
		rempsw = (CheckBox) findViewById(R.id.login_cb_savepwd);
		// 消息静音
		ring = (CheckBox) findViewById(R.id.slient);
		// 开启震动
		vibrate = (CheckBox) findViewById(R.id.openvibra);
		// 测试模式
		// testbutton = (Button) findViewById(R.id.testbutton);
		// 密码
		edtpsd = (EditText) findViewById(R.id.edtpsd);
		// 用户名
		edtusr = (EditText) findViewById(R.id.edtusr);
		// 清除账号按钮
		ImageButton02 = (ImageButton) findViewById(R.id.ImageButton02);
		// 初始化控件的数据
		rempsw.setChecked(mSharedPreference.getBoolean("rempsw", true));
		ring.setChecked(mSharedPreference.getString("ring", "0").equals("0"));
		vibrate.setChecked(mSharedPreference.getBoolean("vibrate", true));
		edtusr.setText(mSharedPreference.getString("account", ""));
		if (mSharedPreference.getBoolean("rempsw", true)) {
			edtpsd.setText(mSharedPreference.getString("psw", ""));
		}
		// 点击事件
		myClickListener l = new myClickListener();
		rempsw.setOnClickListener(l);
		ring.setOnClickListener(l);
		vibrate.setOnClickListener(l);
		ImageButton02.setOnClickListener(l);
		login.setOnClickListener(l);

		// 输入限制
		myWatcher mWatcher = new myWatcher();
		edtusr.addTextChangedListener(mWatcher);
		// 获取屏幕尺寸用于适配
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		//
		if (MainService.isRun == true
				&& !Tools.nvl(Ajz.getAccount()).equals("")) {
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, MainActivity.class);
			startActivity(intent);
		}
		mEditor = null;
		mSharedPreference = null;
	}

	public class myWatcher implements TextWatcher {
		public void beforeTextChanged(CharSequence cs, int a, int b, int c) {

		}

		public void onTextChanged(CharSequence cs, int a, int b, int c) {
			edtpsd.setText("");
		}

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
		}
	}

	public class myClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			mSharedPreference = getSharedPreferences(SpName, 0);
			mEditor = mSharedPreference.edit();
			if (v == login) {

				MyToast.showMyToast(LoginActivity.this, MyToast.LOADING);

				Ajz.setServerip(mSharedPreference.getString("serveraddr", IP));
				Ajz.setSERVER(Ajz.getServerip());
				Ajz.setRing(mSharedPreference.getString("ring", "tone1"));
				Ajz.setVibrate(mSharedPreference.getBoolean("vibrate", true));
				Ajz.setPsw(edtpsd.getText().toString());

				((Button) v).setEnabled(false);

				if (mSharedPreference.getBoolean("rempsw", true)) {
					mEditor.putString("psw", edtpsd.getText().toString());
				}
				mEditor.putString("account", edtusr.getText().toString());
				// start the MainService
				EditText edtuser = (EditText) findViewById(R.id.edtusr);
				EditText edtpsd = (EditText) findViewById(R.id.edtpsd);
				String useraccount = edtuser.getText().toString();
				String userpsd = edtpsd.getText().toString();
				hp.clear();
				hp.put("useraccount", useraccount);
				hp.put("userpsd", userpsd);

				if (MainService.isRun == true) {

				} else {
					Intent service = new Intent(LoginActivity.this,
							MainService.class);
					LoginActivity.this.startService(service);
					MainService.isRun = true;
				}
				NetWorker.setIP(Ajz.getServerip());
				setPlayerOn(LoginActivity.this);

				NetWorker.setFirstLogin(true);
				Ajz.setSta(true);
				con = Communication.newInstance();
				if (null == con)
					return;
				con.login(hp.get("useraccount"), hp.get("userpsd"));

			} 
			else if (v == ring) {
				CheckBox cb = (CheckBox) v;
				if (cb.isChecked()) {
					mEditor.putString("ring", "0");
				} else {
					mEditor.putString("ring", "tone1");
				}
			}
			else if (v == vibrate) {
				CheckBox cb = (CheckBox) v;
				if (cb.isChecked()) {
					mEditor.putBoolean("vibrate", true);
				} else {
					mEditor.putBoolean("vibrate", false);
				}
			} 
			else if (v == rempsw) {
				CheckBox cb = (CheckBox) v;
				if (cb.isChecked()) {
					mEditor.putBoolean("rempsw", true);
				} else {
					mEditor.putBoolean("rempsw", false);
					mEditor.putString("psw", "");
				}
			} 
			else if (v == ImageButton02) {
				edtusr.setText("");
			}
	
			// 一个新安装的app，在editor没有提交之前，不会创建sharepreference.xml文件的
			mEditor.commit();
			mEditor = null;
			mSharedPreference = null;
		}
	}

	@Override
	public void init() {

	}

	@Override
	public void refresh(Object... params) {
		boolean errflag = true;
		try {
			String returnResult = (String) params[0];
			if (returnResult.indexOf("该用户不存在") > -1) {
				MyToast.showMyToast(LoginActivity.this, MyToast.USERNAMEWRONG);
			}
			else if (returnResult.indexOf("用户口令不正确") > -1) {
				MyToast.showMyToast(LoginActivity.this, MyToast.PSWWRONG);
			}
			else if (returnResult.indexOf("请检查您的网络") > -1) {
				MyToast.showMyToast(LoginActivity.this, MyToast.CONERR);
			}
			else if (returnResult.indexOf("禁用") > -1) {
				MyToast.showMyToast(LoginActivity.this, MyToast.USERSTAWRONG);
			}
			else {
				JSONObject json = new JSONObject(returnResult);
				Ajz.setWorkUnit(json.optString("work_unit", ""));
				Ajz.setUserid(json.optString("userid", ""));
				Ajz.setAccount(json.optString("account", ""));
				Ajz.setUsertype(json.optString("usertype", ""));
				Ajz.setRealname(json.optString("realname", ""));
				Ajz.setLogined(true);

				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				errflag = false;
				this.finish();
				NetWorker.setFirstLogin(false);
			}
			if (errflag) {
				MainService.exit();
				if (null != con) {
					con.stopWork();
					con = null;
				}
				Intent serviceintent = new Intent(this, MainService.class);
				stopService(serviceintent);
				((Button) findViewById(R.id.login)).setEnabled(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void onResume() {
		super.onResume();
		mSharedPreference = getSharedPreferences(SpName, 0);
		mEditor = mSharedPreference.edit();
		rempsw.setChecked(mSharedPreference.getBoolean("rempsw", true));
		ring.setChecked(mSharedPreference.getString("ring", "0").equals("0"));
		vibrate.setChecked(mSharedPreference.getBoolean("vibrate", true));

		mEditor = null;
		mSharedPreference = null;
	}

	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onBackPressed() {

		if (getBackCount() >= 1) {
			MainService.exit();
			new ScreenManager().popAllActivity();
		} else {

			Toast.makeText(this, "再按一次反回完全退出", Toast.LENGTH_SHORT).show();
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

}
