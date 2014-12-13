package com.v_info.ajzclientv3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
/**
 * 该Activity用于提前加载，GPS数据，本机号码等信息
 * @author Administrator
 *
 */
public class LogoActivity extends Activity {

	private static final String TAG = "LogoActivity";
	private ImageView logo_image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置窗口没有标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置窗体全屏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_logo);
		getTelePhoneNo();
		
		logo_image = (ImageView) this.findViewById(R.id.logo_image);
		//设置渐进渐出动画效果，其中0.1f是浮点数的意思，是透明度
		AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
		//设置动画持续时间，单位毫秒，不能为负数
		animation.setDuration(1500);
		logo_image.setAnimation(animation);
		
		Log.d(TAG,"display the logo for 1.5 sec.");
		
		//设置动画的监听器
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			//当动画结束跳入下一个画面
			@Override
			public void onAnimationEnd(Animation animation) {
				Intent intent = new Intent(LogoActivity.this, LoginActivity.class);
				startActivity(intent);
				LogoActivity.this.finish();
			}
		});
	}


//获取电话信息
private String getTelePhoneNo(){
	TelephonyManager tm=(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
	//唯一的设备ID
	String deviceid = tm.getDeviceId();
	//手机号
    String tel = tm.getLine1Number();
    //SIM卡的序列号
    String imei = tm.getSimSerialNumber();
    //返回用户唯一标识，比如GSM网络的IMSI编号
    String imsi = tm.getSubscriberId();
	System.out.println("deviceid="+deviceid+"|tel="+tel+"|imei="+imei+"|imsi="+imsi);
	
	return null;
	
}
}
