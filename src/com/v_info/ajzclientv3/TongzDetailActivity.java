package com.v_info.ajzclientv3;

import android.os.Bundle;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v_info.ajzclientv3.interFace.IFaceActivity;
import com.v_info.ajzclientv3.net.DataShop;
import com.v_info.ajzclientv3.net.Msg;
/**
 * 该Activity用于通知公告的详情显示
 * @author Administrator
 *
 */
public class TongzDetailActivity extends BaseActivity implements IFaceActivity,OnClickListener{
	public static String TAG ="TongzDetailActivity";
	public Msg msg;
	public TextView title;
	public TextView timer1;
	public TextView cont;
	public ImageView sta;
	public Button markToRead;
	private View tongz;
	private ImageButton btnBack;
	private RelativeLayout content;
	private TextView titlew;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.tagName=TAG;
		super.onCreate(savedInstanceState);
        setContentView(R.layout.tabbase);
        
        /*
         * 左上角的箭头
         */
      	btnBack=(ImageButton) findViewById(R.id.page_title_memu);
      	btnBack.setOnClickListener(this);
        
      	/*
      	 * 获取点击的消息内容，保存在DataShop的Msglist中呢
      	 */
        msg = DataShop.getMsglist().get(DataShop.getMsglist().size()-1-DataShop.getMsgCurPos());
        
        /*
         *填充该页的顶部标题
         */
		title=(TextView)findViewById(R.id.page_title_tv);
		title.setText(msg.getMsgTitle());
		
		tongz= View.inflate(this, R.layout.tongz_detail, null);
		
		/*
		 * 填充消息内容
		 */
		content=(RelativeLayout)findViewById(R.id.page_main_rl);
        content.addView(tongz);
		
		markToRead = (Button) tongz.findViewById(R.id.btn_stat);
		markToRead.setOnClickListener(this);
		
		/*
		 * 填充消息发送时间
		 */
		timer1 = (TextView) tongz.findViewById(R.id.timer1);
		timer1.setText(msg.getMsgTime());
		
		/*
		 * 填充消息内容
		 */
		cont = (TextView) tongz.findViewById(R.id.cont);
		System.out.println(msg.getCont());
		cont.setMovementMethod(ScrollingMovementMethod.getInstance());  
		cont.setText(msg.getCont().replaceAll("%40%40", "\r\n"));
		
		/*
		 * 根据消息是否已读设置底部的按钮状态
		 */
		sta = (ImageView) tongz.findViewById(R.id.sta);
		if (!msg.isIsRead()) {
			sta.setBackgroundResource(R.drawable.app_detail_ratingbar_on);
		}else{
			sta.setBackgroundResource(R.drawable.app_detail_ratingbar_off);
			markToRead.setEnabled(false);
			markToRead.setText("此通知已读");
			}
		
	}
//	public class MyClickListener implements OnClickListener{
//		public void onClick(View v){
//			if(v==markToRead){
//				con.getMarkMsg(msg.getMsgCode(),msg.getMsglb());
//			}
//		}
//	}
	
	
	public void onBackPressed(){
		finish();
	}

	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void refresh(Object... params) {
		String returnResult = (String) params[0];
		if(returnResult.equals("ok")){
			markToRead.setEnabled(false);
			markToRead.setText("此通知已读");
			msg.setIsRead("1");
			sta.setBackgroundResource(R.drawable.app_detail_ratingbar_off);
			Msg.setNewMsgNum(Msg.getNewMsgNum()-1);
			con.getMsgList(1,15);
		}else{
			markToRead.setEnabled(true);
			markToRead.setText("标记为已读");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_title_memu:
			TongzDetailActivity.this.finish();
			break;

		case R.id.btn_stat:
			con.getMarkMsg(msg.getMsgCode(),msg.getMsglb());
			break;
		}
	}


	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
