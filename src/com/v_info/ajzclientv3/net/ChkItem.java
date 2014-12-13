package com.v_info.ajzclientv3.net;

import android.graphics.Bitmap;

public class ChkItem {
	private  Bitmap bitmap;
	private String comment;
	private String path;
	
	public ChkItem(Bitmap bitmap, String comment, String path) {
		super();
		this.bitmap = bitmap;
		this.comment = comment;
		this.path = path;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
