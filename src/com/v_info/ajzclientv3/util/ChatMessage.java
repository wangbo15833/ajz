package com.v_info.ajzclientv3.util;

import java.io.Serializable;

/**
 * 聊天信息的格式
 * @author hu
 *
 */
public class ChatMessage implements Serializable{
	private String self;
	private String friend;
	private int direction;
	private int type;		//消息类型：纯文本、表情+文本、图片、语音
	private String content;
	private String time;
	
	public ChatMessage(String self, String friend,int direction, int type, String time, String content) {
		this.self=self;
		this.friend=friend;
		this.direction = direction;
		this.type=type;
		this.time=time;
		this.content = content;
	}

	public ChatMessage(){
		
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}

	public String getFriend() {
		return friend;
	}

	public void setFriend(String friend) {
		this.friend = friend;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

}
