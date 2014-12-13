package com.v_info.ajzclientv3;


import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.v_info.ajzclientv3.adapter.AppAdapter;
import com.v_info.ajzclientv3.widget.MenuButton;
import com.v_info.ajzclientv3.widget.PageControlView;
import com.v_info.ajzclientv3.widget.ScrollLayout;
import com.v_info.ajzclientv3.widget.ScrollLayout.OnScreenChangeListenerDataLoad;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 该Activity是登陆后的主界面
 * @author Administrator
 * 使用GridView把的布局用上，然后动态的添加按钮布局
 */

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup implements OnClickListener//,OnTouchListener,
//GestureDetector.OnGestureListener
{
	
	private LinearLayout ll_right;
	private LinearLayout ll_left;
	private ImageButton menu;
	private MenuButton custinfo,setting,aboutBtn,exitBtn,helpBtn,callmeBtn;
	private int window_width;	// 屏幕的宽度
	private int SPEED  = 30;	//滑动的速度
	private int MAX_WIDTH = 0;	//滑动的最大距离
	private boolean isFinish = true;
	private boolean isMenuOpen = false;
	private boolean hasMeasured = false;	// 是否Measured.

	private ScrollLayout mScrollLayout;
	private static final float APP_PAGE_SIZE = 6.0f;
	private PageControlView pageControl;
	public MyHandler myHandler;
	public int n=0;
	private DataLoading dataLoad;
	
	private boolean isAdmin	= true;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.main);
		//分页
		dataLoad = new DataLoading();
		mScrollLayout = (ScrollLayout)findViewById(R.id.ScrollLayoutTest);
		myHandler = new MyHandler(this,1);
		MyThread m = new MyThread();		//起一个线程更新数据
		new Thread(m).start();
		getView();
		getMaxX();
	}

	private void getView(){//得到控件的对象实例
		ll_right = (LinearLayout)findViewById(R.id.layout_right);
		ll_left = (LinearLayout)findViewById(R.id.layout_left);
		menu=(ImageButton) findViewById(R.id.page_title_memu);
		menu.setOnClickListener(this);
		
		custinfo=(MenuButton)findViewById(R.id.custinfo);
		custinfo.setOnClickListener(this);
		setting=(MenuButton)findViewById(R.id.setting);
		setting.setOnClickListener(this);
		
		aboutBtn=(MenuButton)findViewById(R.id.about);
		aboutBtn.setOnClickListener(this);
		
		exitBtn=(MenuButton)findViewById(R.id.exit);
		exitBtn.setOnClickListener(this);
		
		helpBtn=(MenuButton)findViewById(R.id.help);
		helpBtn.setOnClickListener(this);
		
		callmeBtn=(MenuButton)findViewById(R.id.callback);
		callmeBtn.setOnClickListener(this);

	}
	
	private void getMaxX(){//得到滑动的最大宽度,即此layout的宽度
		
		ViewTreeObserver viewTreeObserver = ll_right.getViewTreeObserver();
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener(){
			@Override
			public boolean onPreDraw() {
				if (!hasMeasured) {
					window_width = getWindowManager().getDefaultDisplay().getWidth();	//屏幕宽度
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)ll_right
							.getLayoutParams();	//layout参数
					layoutParams.width = window_width;
					ll_right.setLayoutParams(layoutParams);
					hasMeasured = true;
					MAX_WIDTH = ll_left.getWidth();//左边layout宽度
				}
				return true;
			}
		});
	}
	
	class AsynMove extends AsyncTask<Integer, Integer, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			isFinish = false;
			int times = 0;
			if (MAX_WIDTH % Math.abs(params[0]) == 0)// 整除
				times = MAX_WIDTH / Math.abs(params[0]);
			else
				times = MAX_WIDTH / Math.abs(params[0]) + 1;// 有余数

			for (int i = 0; i < times; i++) {
				publishProgress(params[0]);
				try {
					Thread.sleep(Math.abs(params[0]));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			isFinish = true;
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_right.getLayoutParams();
			if(layoutParams.leftMargin >= MAX_WIDTH){
					isMenuOpen = true;
			}else{
				    isMenuOpen = false;
			}
			super.onPostExecute(result);
		}

		/**
		 * update UI
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_right
					.getLayoutParams();
			// 右移动
			if (values[0] > 0) {
				layoutParams.leftMargin = Math.min(layoutParams.leftMargin
						+ values[0], MAX_WIDTH);
				layoutParams.rightMargin = Math.max(layoutParams.rightMargin
						- values[0], -MAX_WIDTH);
				
			} else {
				// 左移动
				layoutParams.leftMargin = Math.max(layoutParams.leftMargin
						+ values[0], 0);
			
			}
			ll_right.setLayoutParams(layoutParams);
			ll_left.invalidate();

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_title_memu:
			if(isMenuOpen){
				if(isFinish){
					
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_right
							.getLayoutParams();

					int tempSpeed = SPEED;
					

					if(isMenuOpen ){
						tempSpeed = -tempSpeed;
					}

					if ( (!isMenuOpen && (layoutParams.leftMargin > window_width / 2))
							|| (isMenuOpen && (layoutParams.leftMargin < window_width / 2))) {
						
						new AsynMove().execute(tempSpeed);
						
					} else {
						
						new AsynMove().execute(-tempSpeed);
						
					}

				}
			}else{
				new AsynMove().execute(30);
			}
			break;

		//打开用户信息
		case R.id.custinfo:
			Intent intent=new Intent();
			intent.setClass(this, CustinfoActivity.class);
			startActivity(intent);
			break;
		//打开软件设置
		case R.id.setting:
			Intent intent1=new Intent();
			intent1.setClass(this, SettingsActivity.class);
			startActivity(intent1);
			break;
		//打开软件关于
		case R.id.about:
			Intent aboutIntent=new Intent();
			aboutIntent.setClass(this, AboutActivity.class);
			startActivity(aboutIntent);
			break;
			
		//退出软件
		case R.id.exit:
			showDialog();
			break;
//			带滚动条的TextView 进行文字性的描述帮助。
		case R.id.help:

			Intent helpIntent=new Intent();
			helpIntent.setClass(this, AboutActivity.class);
			startActivity(helpIntent);
			break;
		case R.id.callback:
//			接收意见反馈。
			Intent opintent=new Intent();
			opintent.setClass(this, OpinionsActivity.class);
			startActivity(opintent);
			break;
		}
		
	}
//---------------------用于分页的内部类-----------------------------

	/**
	 * gridView 的onItemLick响应事件
	 */
	public OnItemClickListener listener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent;
			if(isAdmin){
				switch (position) {
				case 0:
//					通知公告
					intent=new Intent();
					intent.setClass(MainActivity.this, MsgActivity.class);
					MainActivity.this.startActivity(intent);
					break;
				case 1:
//					地图   调用百度web地图
					intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri content_url = Uri.parse("http://map.baidu.com/mobile/webapp/index/index/" +
					"qt=cur&wd=%E7%A7%A6%E7%9A%87%E5%B2%9B%E5%B8%82&from=maponline&tn=m01&ie=utf-8/vt=map");
					intent.setData(content_url);
					intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
					startActivity(intent);
					break;
				case 2:
//					监督
					intent = new Intent().setClass(MainActivity.this, WorkPlanActivity.class);
					startActivity(intent);
					break;
				case 3:
//					签到
					intent = new Intent().setClass(MainActivity.this, SigActivity.class);
					startActivity(intent);
					break;
				case 4:
//					综合查询
					intent =new Intent().setClass(MainActivity.this, QryActivity.class);
					startActivity(intent);
					break;
				case 5:
//					法律法规
					intent =new Intent().setClass(MainActivity.this, LawQryActivity.class);
					startActivity(intent);
					break;
				}
			}else{
				switch (position) {
				case 0:
//					通知公告
					Intent messageIntent=new Intent();
					messageIntent.setClass(MainActivity.this, MsgActivity.class);
					MainActivity.this.startActivity(messageIntent);
					break;
				case 1:
//					地图   调用百度web地图
					Intent mapIntent = new Intent();
					mapIntent.setAction("android.intent.action.VIEW");
					Uri content_url = Uri.parse("http://map.baidu.com/mobile/webapp/index/index/" +
					"qt=cur&wd=%E7%A7%A6%E7%9A%87%E5%B2%9B%E5%B8%82&from=maponline&tn=m01&ie=utf-8/vt=map");
					mapIntent.setData(content_url);
					mapIntent.setClassName("com.android.browser",
							"com.android.browser.BrowserActivity");
					startActivity(mapIntent);
					break;
				case 2:
//					签到
					intent = new Intent().setClass(MainActivity.this, SigActivity.class);
					startActivity(intent);
					break;
				case 3:
//					综合查询
					intent =new Intent().setClass(MainActivity.this, QryActivity.class);
					startActivity(intent);
					break;
				case 4:
//					法律法规
					break;
				}	
				}
			}
		
		
	};
	
	
	//分页数据
	class DataLoading {
		private int count;
		public void bindScrollViewGroup(ScrollLayout scrollViewGroup) {
			this.count=scrollViewGroup.getChildCount();
			scrollViewGroup.setOnScreenChangeListenerDataLoad(new OnScreenChangeListenerDataLoad() {
				public void onScreenChange(int currentIndex) {
					// TODO Auto-generated method stub
//					generatePageControl(currentIndex);
				}
			});
		}
		
//		private void generatePageControl(int currentIndex){
//			//如果到最后一页，就加载16条记录
//			if(count==currentIndex+1){
//				MyThread m = new MyThread();
//				new Thread(m).start();
//			}
//		}
	}

	 List<Map> list = new ArrayList<Map>();
	
	// 更新后台数据
	class MyThread implements Runnable {
		public void run() {
			 Map map0=new HashMap();
			 map0.put("title", "通知公告");
			 map0.put("explain", "信息消息");
			 map0.put("icon", R.drawable.lt_app_page_map_icon);
			 list.add(map0);
			 Map map1=new HashMap();
			 map1.put("title", "地图");
			 map1.put("explain", "地图导航");
			 map1.put("icon", R.drawable.lt_app_page_map_icon);
			 list.add(map1);
			 
		 if(isAdmin){
			 Map map2=new HashMap();
			 map2.put("title", "监督");
			 map2.put("explain", "监督检查计划");
			 map2.put("icon", R.drawable.lt_app_page_map_icon);
			 list.add(map2);
		 }
			 Map map3=new HashMap();
			 map3.put("title", "签到");
			 map3.put("explain", "项目签到");
			 map3.put("icon", R.drawable.lt_app_page_map_icon);
			 list.add(map3);
			 Map map4=new HashMap();
			 map4.put("title", "综合查询");
			 map4.put("explain", "进行项目、任务查询");
			 map4.put("icon", R.drawable.lt_app_page_map_icon);
			 list.add(map4);
			 Map map5=new HashMap();
			 map5.put("title", "法律法规");
			 map5.put("explain", "安全监管相关条例");
			 map5.put("icon", R.drawable.lt_app_page_map_icon);
			 list.add(map5);
			 
			String msglist = "1";
			Message msg = new Message();
			Bundle b = new Bundle();// 存放数据
			b.putString("rmsg", msglist);
			msg.setData(b);
			MainActivity.this.myHandler.sendMessage(msg); // 向Handler发送消息,更新UI

		}
	}

	class MyHandler extends Handler {
		private MainActivity mContext;
		public MyHandler(Context conn,int a) {
			mContext = (MainActivity)conn;
		}
		public MyHandler(Looper L) {
			super(L);
		}
		// 子类必须重写此方法,接受数据
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle b = msg.getData();
			String rmsg = b.getString("rmsg");
			if ("1".equals(rmsg)) {
				int pageNo = (int)Math.ceil( list.size()/APP_PAGE_SIZE);
				for (int i = 0; i < pageNo; i++) {
					GridView appPage = new GridView(mContext);
					// get the "i" page data
					appPage.setAdapter(new AppAdapter(mContext, list, i));
					appPage.setNumColumns(2);
					appPage.setOnItemClickListener(listener);
					mScrollLayout.addView(appPage);
				}
				//加载分页
				pageControl = (PageControlView) findViewById(R.id.pageControl);
				pageControl.bindScrollViewGroup(mScrollLayout);
				//加载分页数据
				dataLoad.bindScrollViewGroup(mScrollLayout);
					
				}
			}

		}

	private void showDialog(){
		AlertDialog.Builder builder=new Builder(MainActivity.this);
		builder.setMessage("确定退出吗?");
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				MainActivity.this.finish();
			}
		});
		
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			showDialog();
		}
		return false;
	}

}