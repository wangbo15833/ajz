package com.v_info.ajzclientv3;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v_info.ajzclientv3.MsgActivity.MyAdapter;
import com.v_info.ajzclientv3.MsgActivity.ViewHolder;
import com.v_info.ajzclientv3.adapter.WorkPlanAdapter;
import com.v_info.ajzclientv3.net.Chkinfo;
import com.v_info.ajzclientv3.interFace.IFaceActivity;
import com.v_info.ajzclientv3.net.DataShop;
import com.v_info.ajzclientv3.service.ScreenManager;
import com.v_info.ajzclientv3.service.Tools;

/**
 * 该Activity用于监督检查计划
 */
public class WorkPlanActivity extends BaseActivity implements IFaceActivity,OnClickListener {
	
	private ImageButton btnBack;
	private TextView title;
	private RelativeLayout content;
	private ListView workplanTitle;
	private MyAdapter myAdapter;
	private List<Chkinfo> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tabbase);
		//向服务器获取检查计划列表
		con.getJdjlList();
		
		//把当前的activity推入栈顶
		new ScreenManager().pushActivity(this);
		
		btnBack=(ImageButton) findViewById(R.id.page_title_memu);
		btnBack.setOnClickListener(this);
		
		title=(TextView)findViewById(R.id.page_title_tv);
		title.setText("监督计划");
		//内容容器
		content=(RelativeLayout)findViewById(R.id.page_main_rl);
		View workplan=LayoutInflater.from(this).inflate(R.layout.workplan, null);
		content.addView(workplan);
		
		list = DataShop.getChklist();
		workplanTitle=(ListView) workplan.findViewById(R.id.lv_workplan);
		myAdapter = new MyAdapter(this);
		workplanTitle.setAdapter(myAdapter);
		
		
	}
	
	public final class ViewHolder {

		public RelativeLayout contaiter;
		public TextView tv_workplan_title;
		public TextView tv_workplan_time;
	}

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list.get(arg0);
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
				convertView = mInflater.inflate(R.layout.workplan_item, null);
				holder.contaiter = (RelativeLayout) convertView.findViewById(R.id.worklistLayout);
				holder.tv_workplan_title = (TextView) convertView.findViewById(R.id.tv_workplan_title);
				holder.tv_workplan_time = (TextView) convertView.findViewById(R.id.tv_workplan_time);

				//设置与视图相关的标签，这个标签用于在视图的层次中标识一个视图，并且在这个层次不必唯一。标签还可以在不用存储到其他的数据结构的情况下在视图中存储数据。
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_workplan_title.setText(Tools.omitnd((String) list.get(position).getTitle(), 10));
			holder.tv_workplan_time.setText((String) list.get(position).getTime());

			return convertView;
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refresh(Object... params) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		this.finish();
	}

}
