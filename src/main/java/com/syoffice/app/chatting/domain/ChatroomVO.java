package com.syoffice.app.chatting.domain;

import java.util.List;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Document(collection = "chatroom") // MongoDB에서 chatroom 컬렉션 사용
@Component
public class ChatroomVO {

    @Id
    private String roomId; // MongoDB의 _id 채팅방 아이디
    private String roomName; // 채팅방 이름
    private List<String> employees; // 참여하는 사원 ID 목록
    private Date create; // 방 생성 시간

    // 생성자
    public ChatroomVO() {}

    public ChatroomVO(String roomName, List<String> employees) {
        this.roomName = roomName;
        this.employees = employees;
        this.create = new Date();
    }
    
    
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<String> getemployees() {
        return employees;
    }

    public void setemployees(List<String> employees) {
        this.employees = employees;
    }

    public Date getCreatedAt() {
        return create;
    }

    public void setCreatedAt(Date createdAt) {
        this.create = createdAt;
    }
}
