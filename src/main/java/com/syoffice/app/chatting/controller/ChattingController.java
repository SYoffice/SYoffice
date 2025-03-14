package com.syoffice.app.chatting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.syoffice.app.chatting.domain.ChatroomVO;
import com.syoffice.app.chatting.service.ChatroomService;
import com.syoffice.app.employee.domain.EmployeeVO;
import org.springframework.web.socket.TextMessage;

import java.util.List;

@Controller
@RequestMapping("/chatting")
public class ChattingController {

    @Autowired
    private ChatroomService chatroomService;

   
    @GetMapping("/index")
    public String index(HttpServletRequest request) {
        return "/chatting/index";
    }

    @GetMapping("/multichat")
    public String requiredLogin_multichat(HttpServletRequest request, HttpServletResponse response) {
        return "/chatting/multichat";
    }

    // 채팅방 만들기 
    @PostMapping("/chatroom/create")
    @ResponseBody
    public ChatroomVO createChatroom(HttpSession session,
						             @RequestParam String roomName,
						             @RequestParam List<String> employees) {

        
        EmployeeVO loginUser = (EmployeeVO) session.getAttribute("loginuser");
        
        // 로그인한 사용자를 자동으로 참여자로 추가
        String creatorId = loginUser.getName()+ " " + loginUser.getGrade_name();
        return chatroomService.createChatroom(roomName, employees, creatorId);
    }

    //로그인한 사용자의 채팅방 목록 조회
    @GetMapping("/chatroom/list")
    @ResponseBody
    public List<ChatroomVO> getUserChatrooms(HttpSession session) {
        
        EmployeeVO loginUser = (EmployeeVO) session.getAttribute("loginuser");

        return chatroomService.getUserChatrooms(loginUser.getName()+ " " + loginUser.getGrade_name());
    }

    // 채팅방 나가기 (마지막 남은 사람이 나가면 방 삭제)
    @PutMapping("/chatroom/leave/{roomId}")
    @ResponseBody
    public ResponseEntity<String> leaveChatroom(@PathVariable String roomId, HttpSession session) {
        EmployeeVO loginUser = (EmployeeVO) session.getAttribute("loginuser");


        // 퇴장 메시지
        String sendMessage = "<div style='text-align: center;'>"
                            + loginUser.getName() + " " + loginUser.getGrade_name() + " 님이 채팅방을 나갔습니다."
                            + "</div>";

        // 퇴장 메시지를 같은 방 사용자들에게 전송
        sendExitMessage(roomId, sendMessage);

        // 채팅방 나가기 (마지막 사람이면 방 삭제)
        boolean isDeleted = chatroomService.removeUserFromRoom(roomId, loginUser.getName() + " " + loginUser.getGrade_name());

        if (isDeleted) {
            return ResponseEntity.ok("채팅방에서 나갔습니다. (방이 삭제되었습니다.)");
        } 
        else {
            return ResponseEntity.ok("채팅방에서 나갔습니다.");
        }
    }

    // 퇴장 메시지 전송
    private void sendExitMessage(String roomId, String message) {
        for (WebSocketSession session : WebsocketEchoHandler.connectedUsers) {
            String sessionRoomId = (String) session.getAttributes().get("roomId");

            if (roomId.equals(sessionRoomId)) { // 같은 채팅방 사용자에게만 메시지 전송
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    
    
    // 채팅방 상세 정보 가져오기
    @GetMapping("/chatroom/detail/{roomId}")
    @ResponseBody
    public ResponseEntity<ChatroomVO> getChatroomDetail(@PathVariable String roomId) {
        ChatroomVO chatroom = chatroomService.getChatroomById(roomId);
        
        
        if (chatroom != null) {
            return ResponseEntity.ok(chatroom);
        } 
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    


}
