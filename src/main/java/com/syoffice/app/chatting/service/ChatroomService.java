package com.syoffice.app.chatting.service;

import java.util.List;
import java.util.ArrayList;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.syoffice.app.chatting.domain.ChatroomVO;

@Service
public class ChatroomService {

    @Autowired
    private MongoOperations mongo;

    // 채팅방 생성 (로그인한 사용자 자동 포함)
    public ChatroomVO createChatroom(String roomName, List<String> employees, String creatorId) {

        // 로그인한 사용자를 자동 포함
        if (!employees.contains(creatorId)) {
            employees.add(creatorId);
        }

        // 채팅방 생성 및 저장
        ChatroomVO chatroom = new ChatroomVO(roomName, employees);
        return mongo.save(chatroom);
    }

    // 모든 채팅방 조회 (로그인한 사용자가 속한 방만 반환)
    public List<ChatroomVO> getUserChatrooms(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("employees").in(userId));
        
        List<ChatroomVO> rooms = mongo.find(query, ChatroomVO.class);
        return rooms != null ? rooms : new ArrayList<>();
    }


    public boolean removeUserFromRoom(String roomId, String userId) {
        ChatroomVO chatroom = mongo.findById(roomId, ChatroomVO.class);

        if (chatroom != null) {
            //  사용자를 참가자 목록에서 제거
            chatroom.getemployees().remove(userId);

            // 남은 인원이 0명이면 채팅방 삭제
            if (chatroom.getemployees().isEmpty()) {
                mongo.remove(chatroom);
                return true; // 방이 삭제됨
            } else {
                mongo.save(chatroom);
                return false; // 방이 유지됨
            }
        }
        return false;
    }

}
