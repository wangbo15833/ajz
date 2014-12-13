package com.v_info.ajzclientv3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.v_info.ajzclientv3.interFace.IFaceActivity;
import com.v_info.ajzclientv3.net.Ajz;
import com.v_info.ajzclientv3.service.MainService;
import com.v_info.ajzclientv3.service.ScreenManager;

/**
 * 该Activity用于综合查询界面
 */
public class QryActivity extends BaseActivity implements IFaceActivity,OnGestureListener,OnMenuItemClickListener,OnClickListener{
	private GestureDetector gestureDetector = null;
	WebView webView ;
	private ImageButton btnBack;
	private TextView title;
	private RelativeLayout content;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabbase);
		btnBack=(ImageButton) findViewById(R.id.page_title_memu);
		btnBack.setOnClickListener(this);
		title=(TextView)findViewById(R.id.page_title_tv);
		title.setText("个人信息");
		//内容容器
		content=(RelativeLayout)findViewById(R.id.page_main_rl);
		View qry = LayoutInflater.from(this).inflate(R.layout.qry,null);
		content.addView(qry);
		gestureDetector = new GestureDetector(this);
		new ScreenManager().pushActivity(this);
		webView = (WebView) qry.findViewById(R.id.webqry);
		webView.setWebViewClient(new WebViewClient(){
			public boolean shouldOverrideUrlLoading(WebView view, String url) {    
	            view.loadUrl(url);       
	            return true;       
			}
			 public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)    
			 { 
			     view.stopLoading();
			     view.clearView();
			     Message msg=handler.obtainMessage();
			     msg.what=1;
			     handler.sendMessage(msg);
			 }
		});
        webView.getSettings().setJavaScriptEnabled(true); 
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setSupportMultipleWindows(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		if(URLUtil.isNetworkUrl(Ajz.getQryUrl())){
			webView.loadUrl(Ajz.getQryUrl());
		}else{
			Toast.makeText(this, "网址不正确", Toast.LENGTH_SHORT).show();
		}
	}
       protected Handler handler = new Handler(){
               public void handleMessage(Message message){
                   switch (message.what) {
                   case 1:{
                	   String html="<html><body><div style='margin-top:100px; margin-left:100px; font-color:red;'>连接服务器出错<br>请与管理员联系</div></body></html>";
 //               	   String html="<html><body>12341234143正2</body></html>";
                       webView.loadData(html,"text/html","utf-8");}
                       break;
                   default:
                       break;
                   }
               }
           };
	
	public boolean dispatchTouchEvent(MotionEvent ev) {
					gestureDetector.onTouchEvent(ev);
	                return super.dispatchTouchEvent(ev);
	        }
	
	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,float arg3) {
			//Toast.makeText(this, "进入了OnFling", Toast.LENGTH_SHORT).show();
		if (arg0.getX() - arg1.getX() > 120) {
			webView.goForward();
			return true;
		}else if (arg0.getX() - arg1.getX() < -120) {
			webView.goBack();
			return true;
		}
		return true;}

	@Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if(keyCode==KeyEvent.KEYCODE_BACK){
	    	 webView.goBack();
	         return true;
	     }
	     return super.onKeyDown(keyCode, event);
	 }
	
	
	
	@Override
	public boolean onMenuItemClick(MenuItem Item){
		int itemId = Item.getItemId();
		switch (itemId){
		case 1 :
			MainService.exit();
			con.getQuite();
			con.stopWork();
		   Intent serviceintent = new Intent(this,MainService.class);
		        stopService(serviceintent);
			new ScreenManager().popAllActivity();
			break;
		case 2 :
			new ScreenManager().popAllActivity();
			break;
		case 3 :
			webView.reload();
			break;
		default:
			break;
		}
		return true;
	}
@Override
public void onLongPress(MotionEvent arg0) {}
@Override
public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,float arg3) {return true;}

@Override
public void onShowPress(MotionEvent arg0) {}

@Override
public boolean onSingleTapUp(MotionEvent arg0) {return true;}

@Override
public void init() {}

@Override
public void refresh(Object... params) {}

@Override
public void processMessage(Message msg) {}

public boolean onDown(MotionEvent arg0) {return true;}
@Override
public boolean onTouchEvent(MotionEvent event) {return true;}

@Override
public void onClick(View v) {
	this.finish();
}

}
