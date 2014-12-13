package com.v_info.ajzclientv3;


import com.v_info.ajzclientv3.service.ScreenManager;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 该Activity用于显示软件版本、版权等相关信息
 * @author Administrator
 *
 */
public class AboutActivity extends Activity implements OnClickListener{

	private ImageButton btnBack;
	private TextView title;
	private RelativeLayout content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new ScreenManager().pushActivity(this);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tabbase);
		initView();
	}

	private void initView() {
		btnBack=(ImageButton) findViewById(R.id.page_title_memu);
		btnBack.setOnClickListener(this);
		title=(TextView)findViewById(R.id.page_title_tv);
		title.setText("相关信息");
		//内容容器
		content=(RelativeLayout)findViewById(R.id.page_main_rl);
		View custinfo=LayoutInflater.from(this).inflate(R.layout.about, null);
		content.addView(custinfo);
	}

	@Override
	public void onClick(View v) {
		AboutActivity.this.finish();
	}


}
