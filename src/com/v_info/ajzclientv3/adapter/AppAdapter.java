package com.v_info.ajzclientv3.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.v_info.ajzclientv3.R;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class AppAdapter extends BaseAdapter {
	private List<Map> mList;
	private Context mContext;
	public static final int APP_PAGE_SIZE = 6;
	private PackageManager pm;
	
	public AppAdapter(Context context, List<Map> list, int page) {
		mContext = context;
		pm = context.getPackageManager();
		
		mList = new ArrayList<Map>();
		int i = page * APP_PAGE_SIZE;
		int iEnd = i+APP_PAGE_SIZE;
		while ((i<list.size()) && (i<iEnd)) {
			mList.add(list.get(i));
			i++;
		}
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Map appInfo = mList.get(position);
		AppItem appItem;
		if (convertView == null) {
			View v = LayoutInflater.from(mContext).inflate(R.layout.main_item, null);
			
			appItem = new AppItem();
			appItem.item_icon = (ImageView)v.findViewById(R.id.item_icon);
			appItem.title = (TextView)v.findViewById(R.id.title);
			appItem.explain=(TextView)v.findViewById(R.id.explain);
			
			v.setTag(appItem);
			convertView = v;
		} else {
			appItem = (AppItem)convertView.getTag();
		}
		// set the icon
		appItem.item_icon.setImageResource((Integer) appInfo.get("icon"));
		// set the app name
		appItem.title.setText(appInfo.get("title").toString());
		appItem.explain.setText(appInfo.get("explain").toString());
		
		
		
		return convertView;
	}

	/**
	 * 每个应用显示的内容，包括图标和名称
	 * @author Yao.GUET
	 *
	 */
	class AppItem {
		ImageView item_icon;
		TextView explain,title;
	}
}
