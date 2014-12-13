package com.v_info.ajzclientv3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.v_info.ajzclientv3.interFace.IFaceActivity;
import com.v_info.ajzclientv3.interFace.LocSer;
import com.v_info.ajzclientv3.net.Ajz;
import com.v_info.ajzclientv3.service.MainService;
import com.v_info.ajzclientv3.service.ScreenManager;

public class SigActivity extends BaseActivity implements OnMenuItemClickListener, IFaceActivity, LocSer, OnClickListener {

	public static LocationClient mLocClient = null;
	public BDLocationListener myListener = new MyLocationListener();

	public static final String TAG = "SigActivity";

	TextView sigSta;
	TextView posInfo;
	Button getPosBtn;
	Button sigInBtn;
	boolean posSta = false;
	boolean getPosBtnSta = false;
	boolean isSigIn = true;
	boolean isGetAlready = false;

	String posType = "没有定位";
	String posTime = "";
	double posLatitude = 0;
	double posLongtitude = 0;
	float posRadius = 0;
	float posSpeed = 0;
	String posAddr = "";
	private ImageButton btnBack;
	private TextView title;
	private RelativeLayout content;
	private View sig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.tagName = TAG;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabbase);

		/*
		 * 左上角的弯箭头
		 */
		btnBack = (ImageButton) findViewById(R.id.page_title_memu);
		btnBack.setOnClickListener(this);

		/*
		 * 填充该页的顶部标题
		 */
		title = (TextView) findViewById(R.id.page_title_tv);
		title.setText("签到");

		/*
		 * 签到部分
		 */
		sig = View.inflate(this, R.layout.sig, null);

		sigSta = (TextView) sig.findViewById(R.id.sigSta);
		posInfo = (TextView) sig.findViewById(R.id.gpsInfo);
		sigInBtn = (Button) sig.findViewById(R.id.sigIn);

		/*
		 * 把签到部分嵌入主view
		 */
		content = (RelativeLayout) findViewById(R.id.page_main_rl);
		content.addView(sig);

		// setContentView(R.layout.sig);
		new ScreenManager().pushActivity(this);
		// ((TextView) findViewById(R.id.head_userName)).setText("签到");

		/*
		 * 百度地图定位部分
		 */
		mLocClient = new LocationClient(getApplicationContext());
		mLocClient.setAK("8TGI1ApEfMR1LGwAsGTSYnOq");
		mLocClient.registerLocationListener(myListener);
		setLocationOption();
		mLocClient.start();
		mLocClient.requestLocation();

		/*
		 * 设置签到按钮监听器
		 */
		myClickListener l = new myClickListener();
		sigInBtn.setOnClickListener(l);

		/*
		 * 异步获取签到状态
		 */
		con.getSigSta();
	}

	
	/*
	 * 百度地图监听器
	 */
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}
			posType = location.getLocType() == BDLocation.TypeGpsLocation ? "卫星定位": "基站定位";
			posTime = location.getTime();
			//纬度
			posLatitude = location.getLatitude();
			//经度
			posLongtitude = location.getLongitude();
			//半径
			posRadius = location.getRadius();
			posSpeed = location.getSpeed();
			posAddr = (null == location.getAddrStr()) ? "没有取得地址信息" : location.getAddrStr();
			setPosInfo();
			posSta = true;
			if (!isSigIn) {
				sigInBtn.setEnabled(true);
			};

			Ajz.setLastla(posLatitude);
			Ajz.setLastln(posLongtitude);

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	private void setPosInfo() {
		StringBuffer sb = new StringBuffer(256);
		sb.append("定位类型：").append(posType);
		sb.append("  定位时间：").append(posTime);
		sb.append("\n纬度：").append(posLatitude);
		sb.append("  经度：").append(posLongtitude);
		sb.append("\n精度：").append(posRadius);
		sb.append("  速度：").append(posSpeed);
		sb.append("\n地址：").append(posAddr);
		posInfo.setText(sb.toString());
	}

	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");
		option.setCoorType("bd09ll");
		option.setScanSpan(5000);
		option.disableCache(true);
		option.setPoiNumber(5);
		option.setPoiDistance(1000);
		option.setPoiExtraInfo(true);
		mLocClient.setLocOption(option);
	}

	public class myClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v == getPosBtn) {
				if (!getPosBtnSta) {
					setLocationOption();
					mLocClient.start();
					if (mLocClient != null && mLocClient.isStarted()) {
						mLocClient.requestLocation();
					} else {
					}
					((Button) v).setText("停止定位");
					getPosBtnSta = true;
				} else {
					mLocClient.stop();
					posType = "没有定位";
					posTime = "";
					posLatitude = 0;
					posLongtitude = 0;
					posRadius = 0;
					posSpeed = 0;
					posAddr = "";
					sigInBtn.setEnabled(false);
					setPosInfo();
					((Button) v).setText("开始定位");
					getPosBtnSta = false;
					posSta = false;
				}
			} else if (v == sigInBtn) {
				con.getSig(Ajz.getUserid(), Ajz.getAccount(),
						Ajz.getRealname(), Ajz.getWorkUnit(),
						String.valueOf(posLatitude),
						String.valueOf(posLongtitude));
				sigSta.setText("正在提交签到信息...");
				sigInBtn.setEnabled(false);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem settingMenuItem = menu.add(1, 1, 1, "完全退出");
		settingMenuItem.setIcon(R.drawable.app_home_menu_iv_setting_selected);
		settingMenuItem.setOnMenuItemClickListener(this);
		MenuItem aboutMenuItem = menu.add(1, 2, 2, "后台运行");
		aboutMenuItem.setIcon(R.drawable.app_home_menu_iv_about_selected);
		aboutMenuItem.setOnMenuItemClickListener(this);
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		con.getSigSta();
	}

	public void onPause() {
		super.onPause();
	}

	public void OnPause() {
	}

	@Override
	public boolean onMenuItemClick(MenuItem Item) {
		int itemId = Item.getItemId();
		switch (itemId) {
		case 1:
			MainService.exit();
			con.getQuite();
			con.stopWork();
			Intent serviceintent = new Intent(this, MainService.class);
			stopService(serviceintent);
			new ScreenManager().popAllActivity();
			break;
		case 2:
			new ScreenManager().popAllActivity();
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public void refresh(Object... params) {
		try {
			String returnResult = (String) params[0];
			if (returnResult.indexOf("signuped") > -1) {
				sigSta.setTextColor(Color.parseColor("#559955"));
				sigSta.setText("您今天已经签到");
				((ImageView) findViewById(R.id.sigstamp)).setVisibility(View.VISIBLE);
			}
			if (returnResult.indexOf("nosignup") > -1) {
				sigSta.setTextColor(Color.parseColor("#995555"));
				sigSta.setText("您今天还没有签到");
				((ImageView) findViewById(R.id.sigstamp)).setVisibility(View.INVISIBLE);
				isSigIn = false;
				if (posSta) {
					sigInBtn.setEnabled(true);
				}
			}
			if (returnResult.indexOf("true") > -1) {
				sigSta.setTextColor(Color.parseColor("#559955"));
				sigSta.setText("签到成功");
				isSigIn = true;
				sigInBtn.setEnabled(false);
				((ImageView) findViewById(R.id.sigstamp)).setVisibility(View.VISIBLE);
			}
			if (returnResult.indexOf("false") > -1) {
				sigSta.setTextColor(Color.parseColor("#995555"));
				sigSta.setText("签到失败，请重试");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void init() {
	}

	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void locSerShtDn() {
		if (null != mLocClient) {
			// TODO Auto-generated method stub
			if (mLocClient.isStarted()) {
				mLocClient.stop();
			}
		}
	}

	@Override
	public void onClick(View v) {
		this.finish();
	}

	@Override
	public void onBackPressed() {
		this.finish();
	}
}
