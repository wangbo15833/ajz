package com.v_info.ajzclientv3.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
    public class MyReceiver extends BroadcastReceiver {  
        @Override  
        public void onReceive(Context context, Intent intent) {  
        	try{
            // TODO Auto-generated method stub  
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
            NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
            NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);  
            NetworkInfo activeInfo = manager.getActiveNetworkInfo();  
            if (null==activeInfo) {
            	NetWorker.setNetSta(false);
			}  else{
				NetWorker.setNetSta(true);
			}
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        } 
      
    }  