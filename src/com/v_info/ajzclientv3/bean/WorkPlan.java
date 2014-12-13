package com.v_info.ajzclientv3.bean;

import java.io.Serializable;

public class WorkPlan implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String time;
	private boolean status;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
}
