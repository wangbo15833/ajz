package com.v_info.ajzclientv3.adapter;

import java.util.List;

import com.v_info.ajzclientv3.R;
import com.v_info.ajzclientv3.bean.WorkPlan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WorkPlanAdapter extends BaseAdapter {
	private Context context;
	private List<WorkPlan> list;
	
	public WorkPlanAdapter(Context context,List<WorkPlan> list){
		this.context=context;
		changeDate(list);
	}
	
	public void changeDate(List<WorkPlan> list){
		this.list=list;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		WorkPlan workplan=list.get(position);
		ViewHolder vh;
		if(convertView==null){
			View v=LayoutInflater.from(context).inflate(R.layout.workplan_item, null);
			vh=new ViewHolder();
			vh.title=(TextView)v.findViewById(R.id.tv_workplan_title);
			vh.time=(TextView)v.findViewById(R.id.tv_workplan_time);
			vh.status=(ImageView)v.findViewById(R.id.iv_workplan_stat);
			v.setTag(vh);
			convertView=v;
			
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		vh.title.setText(workplan.getTitle());
		vh.time.setText(workplan.getTime());
		if(workplan.isStatus()){
			vh.status.setImageResource(R.drawable.star_ratingbar_fource);
		}else{
			vh.status.setImageResource(R.drawable.star_ratingbar_unfource);
		}
		
		
		return convertView;
	}
private class ViewHolder{
	private TextView title,time;
	private ImageView status;
}
}
