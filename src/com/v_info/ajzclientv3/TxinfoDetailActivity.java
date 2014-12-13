package com.v_info.ajzclientv3;

import android.os.Bundle;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.v_info.ajzclientv3.interFace.IFaceActivity;
import com.v_info.ajzclientv3.net.DataShop;
import com.v_info.ajzclientv3.net.Msg;
import com.v_info.ajzclientv3.net.Msg2;
/**
 * 该Activity用于提醒的详情显示
 * @author Administrator
 *
 */
public class TxinfoDetailActivity extends BaseActivity implements IFaceActivity{
	public static String TAG ="TxinfoDetailActivity";
	public Msg2 msg2;
	public TextView title;
	public TextView timer1;
	public TextView cont;
	public ImageView sta;
	public Button markToRead;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.tagName=TAG;
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tongz_detail);
		msg2 = DataShop.getMsglist2().get(DataShop.getMsglist2().size()-1-DataShop.getMsgCurPos2());
		markToRead = (Button) findViewById(R.id.markToRead);
		markToRead.setOnClickListener(new MyClickListener());
		
		title = (TextView) findViewById(R.id.title);
		title.setText(msg2.getMsgTitle());
		timer1 = (TextView) findViewById(R.id.timer1);
		
		timer1.setText(msg2.getMsgTime());
		cont = (TextView) findViewById(R.id.cont);
		cont.setMovementMethod(ScrollingMovementMethod.getInstance());  
		cont.setText(msg2.getCont().replaceAll("%40%40", "\r\n"));
		sta = (ImageView) findViewById(R.id.sta);
		if (!msg2.isIsRead()) {
			sta.setBackgroundResource(R.drawable.app_detail_ratingbar_on);
		}else{
			sta.setBackgroundResource(R.drawable.app_detail_ratingbar_off);
			markToRead.setEnabled(false);
			markToRead.setText("此提醒已读");
			}
		
	}
	public class MyClickListener implements OnClickListener{
		public void onClick(View v){
			if(v==markToRead){
				con.getMarkMsg(msg2.getMsgCode(),msg2.getMsglb());
			}
		}
	}
	
	
	public void onBackPressed(){
		finish();
	}

	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(Object... params) {
		String returnResult = (String) params[0];
		if(returnResult.equals("ok")){
			markToRead.setEnabled(false);
			markToRead.setText("此提醒已读");
			msg2.setIsRead("1");
			sta.setBackgroundResource(R.drawable.app_detail_ratingbar_off);
			Msg.setNewMsgNum(Msg.getNewMsgNum()-1);
			con.getMsgList(1,15);
		}else{
			markToRead.setEnabled(true);
			markToRead.setText("标记为已读");
		}
	}

}
