package com.syoffice.app.chatting.domain;

import com.google.gson.Gson;

// === (#웹채팅관련7) === //  
public class MessageVO {

	private String roomId;
	private String message;
	private String type; // all 이면 전체에게 채팅메시지를 보냄. one 이면 특정 사용자에게 채팅메시지를 보냄. 
	private String to;   // 특정 웹소켓 id 
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getTo() {
		return to;
	}
	
	public void setTo(String to) {
		this.to = to;
	}
	
	
	/////////////////////////////////////////////////////////////
		
	public static MessageVO convertMessage(String source) {
		
		Gson gson = new Gson();
		
		MessageVO messagevo = gson.fromJson(source, MessageVO.class);
		// source 는 JSON 형태로 되어진 문자열
		// gson.fromJson(source, MessageVO.class); 은 
		// JSON 형태로 되어진 문자열 source를 실제 MessageVO 객체로 변환해준다.
		
		return messagevo;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	
	
	
}
