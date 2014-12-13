package com.v_info.ajzclientv3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ReLoginActivity extends Activity {

	public static Button relogin;
	public static Button finish;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.relogin);
		relogin = (Button) findViewById(R.id.relogin);
		finish = (Button) findViewById(R.id.finish);
		
		myClickListener l = new myClickListener();
		
		relogin.setOnClickListener(l);
		finish.setOnClickListener(l);
	}	
		
		public class myClickListener implements OnClickListener   {
			@Override
			public void onClick(View v) {
				if (v == relogin) {
					Intent intent = new Intent();
					intent.setClass(ReLoginActivity.this, LogoActivity.class);
					startActivity(intent);
					ReLoginActivity.this.finish();
				}else if (v== finish){
					ReLoginActivity.this.finish();
				}
			}
		}

}
