package com.v_info.ajzclientv3.widget;

import com.v_info.ajzclientv3.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuButton extends LinearLayout {

	private ImageView mImage;
	  private TextView mText;
	  
	  public MenuButton(Context context, AttributeSet attrs)
	  {
	    super(context,attrs);
	  
	    mImage = new ImageView(context,attrs);
	    mImage.setPadding(0,35,0,0);
	    mText = new TextView(context,attrs);
	    mText.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
	    mText.setPadding(0,0,0,0);
	  
	    setClickable(true);
	    setFocusable(true);
	    setBackgroundResource(R.drawable.app_home_menu_item_new_bg);
	    setOrientation(LinearLayout.VERTICAL);
	    addView(mImage);
	    addView(mText);
	  } 

}
