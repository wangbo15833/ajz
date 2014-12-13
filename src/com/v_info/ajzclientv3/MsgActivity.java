package com.v_info.ajzclientv3;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v_info.ajzclientv3.interFace.IFaceActivity;
import com.v_info.ajzclientv3.net.DataShop;
import com.v_info.ajzclientv3.net.Msg;
import com.v_info.ajzclientv3.service.MainService;
import com.v_info.ajzclientv3.service.ScreenManager;
import com.v_info.ajzclientv3.service.Tools;

/**
 * 消息界面包括，提醒和通知
 * 
 * @author Administrator
 * 
 */
public class MsgActivity extends BaseActivity implements IFaceActivity,OnMenuItemClickListener {
	private String TAG = "MsgActivity";
	private List<Msg> mListItem;
	private ListView mListView;
	MyAdapter myAdapter;
	String returnResult;//
	JSONObject jsonobject;
	JSONArray jsonarray;
	private View baseList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.tagName = TAG;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabbase);
		con.getMsgList(1, 15);
		//把当前的activity推入栈顶
		new ScreenManager().pushActivity(this);
		baseList = View.inflate(this, R.layout.msg, null);
		RelativeLayout base = (RelativeLayout) findViewById(R.id.page_main_rl);
		base.addView(baseList);
		((TextView) findViewById(R.id.page_title_tv)).setText("通知公告");
		ImageButton back = (ImageButton) findViewById(R.id.page_title_memu);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MsgActivity.this.finish();
			}
		});

		mListView = (ListView) baseList.findViewById(R.id.msglist);
		mListView.setDivider(null);
		mListView.setDividerHeight(10);
		mListItem = DataShop.getMsglist();
		myAdapter = new MyAdapter(this);
		mListView.setAdapter(myAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			/*
			 * (non-Javadoc)
			 * @see android.widget.AdapterView.OnItemClickListener#
			 * onItemClick(android.widget.AdapterView<?> parent, 
			 * android.view.View view, 
			 * int position, 
			 * long id)
			 * 
			 * parent-点击发生的adapterView
			 * view-在apapterview中的被点击的视图布局
			 * position-在adapter中视图的位置
			 * id-被点击的项的行id
			 */
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				DataShop.setMsgCurPos(mListItem.size() - arg2 - 1);
				Intent intent = new Intent(MsgActivity.this,TongzDetailActivity.class);
				startActivity(intent);
			}
		});
	}

	public void showInfo() {
		// Toast.makeText(this,"请阅读后自动取消标记", Toast.LENGTH_SHORT).show();
	}

	public Bitmap getImageByURL(String url) {
		try {
			URL imgURL = new URL(url);
			URLConnection conn = imgURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			Bitmap bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
			if (bm == null) {
			}
			return bm;
		} catch (Exception e) {
			return null;
		}
	}

	public final class ViewHolder {

		public RelativeLayout contaiter;
		public TextView msgtitle;
		public TextView msgtime;
		public TextView msglb;
		public TextView cont;
		public ImageView status;
	}

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mListItem.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mListItem.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				//Inflate()作用就是将xml定义的一个布局找出来，但仅仅是找出来而且隐藏的，没有找到的同时并显示功能。而setContentView()将布局设置成当前屏幕即Activity的内容，可以直接显示出来。
				convertView = mInflater.inflate(R.layout.msglist, null);
				holder.contaiter = (RelativeLayout) convertView.findViewById(R.id.msglistLayout);
				holder.status = (ImageView) convertView.findViewById(R.id.status);
				holder.msgtitle = (TextView) convertView.findViewById(R.id.msgtitle);
				holder.msgtime = (TextView) convertView.findViewById(R.id.msgtime);
				holder.msglb = (TextView) convertView.findViewById(R.id.msglb);
				holder.cont = (TextView) convertView.findViewById(R.id.cont);
				//设置与视图相关的标签，这个标签用于在视图的层次中标识一个视图，并且在这个层次不必唯一。标签还可以在不用存储到其他的数据结构的情况下在视图中存储数据。
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.msgtitle.setText(Tools.omitnd((String) mListItem.get(position).getMsgTitle(), 10));
			holder.msglb.setText((String) mListItem.get(position).getMsglb());
			holder.msgtime.setText(Tools.omitnd((String) mListItem.get(position).getMsgTime(), 10));
			holder.cont.setText(Tools.omit((String) mListItem.get(position).getCont(), 15));
			// holder.cont.setText(Tools.omit((String)
			// mListItem.get(position).getCont(),10).replaceAll("%40%40", ""));

			if (mListItem.get(position).isIsRead()) {
				holder.status.setBackgroundResource(R.drawable.app_detail_ratingbar_off);
				// holder.status.setBackgroundDrawable(R.id.detail_fav_highlight_selected);
			} else {
				holder.status.setBackgroundResource(R.drawable.app_detail_ratingbar_on);
			}
			// holder.chklistLayout.setBackgroundColor(0x665544);

			return convertView;
		}
	}

	@Override
	public void init() {

	}

	@Override
	public void refresh(Object... params) {
		/**
		 * 通知附属的视图基础数据已经改变，视图应该自动刷新。
		 */
		myAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*MenuItem add(int groupId,
        int itemId,
        int order,
        java.lang.CharSequence title)
                     返回值为一个新加的菜单项
        */
		
		MenuItem settingMenuItem = menu.add(1, 1, 1, "完全退出");
		settingMenuItem.setIcon(R.drawable.app_home_menu_iv_setting_selected);
		settingMenuItem.setOnMenuItemClickListener(this);
		
		MenuItem aboutMenuItem = menu.add(1, 2, 2, "后台运行");
		aboutMenuItem.setIcon(R.drawable.app_home_menu_iv_about_selected);
		aboutMenuItem.setOnMenuItemClickListener(this);
		return true;
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
	public void onResume() {
		super.onResume();
		myAdapter.notifyDataSetChanged();
	}

	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub

	}

}
