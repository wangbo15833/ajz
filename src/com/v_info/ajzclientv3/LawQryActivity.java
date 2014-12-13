package com.v_info.ajzclientv3;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v_info.ajzclientv3.interFace.IFaceActivity;

public class LawQryActivity extends BaseActivity implements IFaceActivity,OnClickListener {

	
	
	private ImageButton btnBack;
	private TextView title;
	private RelativeLayout content;
	private TextView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tabbase);
		//向服务器请求法律法规列表
		
		//左上角的弯箭头
		btnBack=(ImageButton) findViewById(R.id.page_title_memu);
		btnBack.setOnClickListener(this);
		
		title=(TextView)findViewById(R.id.page_title_tv);
		title.setText("安全生产手册");
		//内容容器
		content=(RelativeLayout)findViewById(R.id.page_main_rl);
		View child=LayoutInflater.from(this).inflate(R.layout.lawqry, null);
		content.addView(child);
		
		listview=(TextView) child.findViewById(R.id.lv_lawqry);
		
		
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
