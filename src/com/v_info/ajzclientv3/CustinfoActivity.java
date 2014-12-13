package com.v_info.ajzclientv3;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.v_info.ajzclientv3.net.Ajz;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 该Activity用于显示用户信息和修改用户密码
 * @author Administrator
 *
 */
public class CustinfoActivity extends Activity implements OnClickListener{
	
	private ImageButton btnBack;
	private TextView title,custName,companyName,address,tv_msg;
	private EditText oldPwd,newPwd,newPwd2;
	private Button bt_edltPwd,bt_submit;
	private RelativeLayout content;
	private LinearLayout ll_editpwd;
	
	public static LocationClient mLocClient = null;
	private BDLocationListener myListener = new MyLocationListener();
	
	private double posLatitude =0;
	private double posLongtitude =0;
	private String posAddr ="未解析出当前地址";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//全屏现实，去掉标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		
		setContentView(R.layout.tabbase);
		
		mLocClient = new LocationClient(getApplicationContext()); 
		mLocClient.setAK("8TGI1ApEfMR1LGwAsGTSYnOq");
		mLocClient.registerLocationListener( myListener ); 
		setLocationOption();
		mLocClient.start();
		mLocClient.requestLocation();
		
		initView();
	}

	private void initView() {
		//左上角的弯箭头
		btnBack=(ImageButton) findViewById(R.id.page_title_memu);
		btnBack.setOnClickListener(this);
		
		//顶部大标题
		title=(TextView)findViewById(R.id.page_title_tv);
		title.setText("个人信息");
		
		
		View custinfo=LayoutInflater.from(this).inflate(R.layout.custinfo, null);
		
		//用户姓名
		custName=(TextView) custinfo.findViewById(R.id.custName);
		custName.setText(Ajz.getRealname());
		
		//用户单位
		companyName=(TextView) custinfo.findViewById(R.id.companyName);
		companyName.setText(Ajz.getWorkUnit());
		
		//用户所处位置
		address=(TextView) custinfo.findViewById(R.id.address);
		
		//修改密码提示信息
		tv_msg=(TextView)custinfo.findViewById(R.id.tv_msg);
		tv_msg.setText("修改密码后点击确认按钮");
		
		//原密码
		oldPwd=(EditText)custinfo.findViewById(R.id.et_oldPwd);
		newPwd=(EditText)custinfo.findViewById(R.id.et_newPwd);
		newPwd2=(EditText)custinfo.findViewById(R.id.et_newPwd2);
		
		bt_edltPwd=(Button)custinfo.findViewById(R.id.bt_editPwd);
		bt_edltPwd.setOnClickListener(this);
		
		bt_submit=(Button)custinfo.findViewById(R.id.bt_submitpwd);
		bt_submit.setOnClickListener(this);
		
		ll_editpwd=(LinearLayout)custinfo.findViewById(R.id.ll_editpwd);
		
		//内容容器
		content=(RelativeLayout)findViewById(R.id.page_main_rl);
		content.addView(custinfo);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_editPwd:
//			控制修改密码的显示和隐藏
			if (ll_editpwd.getVisibility()==View.VISIBLE) {
				ll_editpwd.setVisibility(View.GONE);

				tv_msg.setText("修改密码后点击确认按钮");
				oldPwd.setText("");
				newPwd.setText("");
				newPwd2.setText("");
			}else{
				ll_editpwd.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.bt_submitpwd:
//			提交保存的密码
			
			break;
		case R.id.page_title_memu:
			CustinfoActivity.this.finish();		
			break;
		}
	}


private class MyLocationListener implements BDLocationListener {
	    @Override
	    public void onReceiveLocation(BDLocation location) {
		    if (location == null)
		    	{return ;}
		    posLatitude    =   location.getLatitude();
		    posLongtitude  =   location.getLongitude();
		    posAddr        =   (null==location.getAddrStr())?"没有取得地址信息":location.getAddrStr();
		    
			address.setText(posAddr);
		    
		    Ajz.setLastla(posLatitude);
		    Ajz.setLastln(posLongtitude);
		    
	    }
	    public void onReceivePoi(BDLocation poiLocation) {}
	 }
	
private void setLocationOption(){
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

}
