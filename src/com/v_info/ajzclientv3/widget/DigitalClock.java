package com.v_info.ajzclientv3.widget;


import java.text.SimpleDateFormat;  
import java.util.Calendar;  
import java.util.Date;  
  
import android.content.Context;  
import android.content.res.Resources;  
import android.database.ContentObserver;  
import android.os.Handler;  
import android.os.SystemClock;  
import android.provider.Settings;  
import android.text.format.DateFormat;  
import android.util.AttributeSet;  
  
@SuppressWarnings("deprecation")
public class DigitalClock extends android.widget.DigitalClock{  
    Calendar mCalendar;  
    private final static String mFormat = "EEEE,MMMM-dd-yyyy hh:mm aa";//h:mm:ss aa  
    private FormatChangeObserver mFormatChangeObserver;  
      
    private Runnable mTicker;  
    private Handler mHandler;  
      
    private boolean mTickerStopped = false;  
      
    public DigitalClock(Context context) {  
        super(context);  
        initClock(context);  
    }  
      
    public DigitalClock(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        initClock(context);  
    }  
      
    private void initClock(Context context){  
        Resources r = context.getResources();  
        if(mCalendar == null){  
            mCalendar = Calendar.getInstance();  
        }  
        mFormatChangeObserver = new FormatChangeObserver();  
        getContext().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, mFormatChangeObserver);  
          
    }  
      
    @Override  
    protected void onAttachedToWindow() {  
        mTickerStopped = false;  
        super.onAttachedToWindow();  
          
        mHandler = new Handler();  
          
        mTicker = new Runnable(){  
            @Override  
            public void run() {  
                if(mTickerStopped){  
                    return ;  
                }  
                mCalendar.setTimeInMillis(System.currentTimeMillis()); 
                SimpleDateFormat dateformat2=new SimpleDateFormat("yyyy 年 MM 月 dd 日 E\nHH 点 mm 分");   
                //setText(DateFormat.format(mFormat, mCalendar));  
                setText(dateformat2.format(new Date()));  
                invalidate();  
                long now = SystemClock.uptimeMillis();  
                long next = now + (1000 - now % 1000);  
                mHandler.postAtTime(mTicker, next);  
            }  
        };  
        mTicker.run();  
    }  
      
      
              
    private class FormatChangeObserver extends ContentObserver{  
  
        public FormatChangeObserver() {  
            super(new Handler());  
        }  
          
        @Override  
        public void onChange(boolean selfChange) {  
              
        }  
    }  
}  