package com.v_info.ajzclientv3;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v_info.ajzclientv3.interFace.IFaceActivity;

/**
 * 该Activity用于提交意见反馈
 * @author Administrator
 *
 */
public class OpinionsActivity extends BaseActivity implements IFaceActivity,OnClickListener {

	
	private ImageButton btnBack;
	private TextView title;
	private RelativeLayout content;
	private EditText op_content;
	private Button submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tabbase);

		btnBack=(ImageButton) findViewById(R.id.page_title_memu);
		btnBack.setOnClickListener(this);
		title=(TextView)findViewById(R.id.page_title_tv);
		title.setText("意见建议");
		//内容容器
		content=(RelativeLayout)findViewById(R.id.page_main_rl);
		View opinions=LayoutInflater.from(this).inflate(R.layout.opinions, null);
		content.addView(opinions);
		op_content=(EditText)opinions.findViewById(R.id.et_op_content);
		submit=(Button)opinions.findViewById(R.id.btn_op_submit);
		submit.setOnClickListener(this);
		
		
	}
	
	@Override
	public void init() {

	}

	@Override
	public void refresh(Object... params) {

	}

	@Override
	public void processMessage(Message msg) {

	}

	@Override
	public void onClick(View v) {
		String texts=op_content.getText().toString();
		switch (v.getId()) {
		case R.id.btn_op_submit:
//			提交到平台接口 给一个dialog
			if(texts!=null&&!texts.equals("")){
				showDialog("提示","感谢您的宝贵意见。");
			}else{
				showDialog("提示","请输入您的意见或建议后提交。");
			}
			break;
		case R.id.page_title_memu:
			this.finish();
			break;
		}
	}
	private void showDialog(String title,String message){
		AlertDialog.Builder builder=new Builder(OpinionsActivity.this);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
}
