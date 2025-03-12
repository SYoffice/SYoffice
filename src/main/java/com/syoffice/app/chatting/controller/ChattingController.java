package com.syoffice.app.chatting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.syoffice.app.chatting.domain.ChatroomVO;
import com.syoffice.app.chatting.service.ChatroomService;
import com.syoffice.app.employee.domain.EmployeeVO;

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
    public ChatroomVO createChatroom(
            HttpSession session,
            @RequestParam String roomName,
            @RequestParam List<String> employees) {

        
        EmployeeVO loginUser = (EmployeeVO) session.getAttribute("loginuser");

        // 로그인한 사용자를 자동으로 참여자로 추가
        String creatorId = loginUser.getEmp_id();
        return chatroomService.createChatroom(roomName, employees, creatorId);
    }

    //로그인한 사용자의 채팅방 목록 조회
    @GetMapping("/chatroom/list")
    @ResponseBody
    public List<ChatroomVO> getUserChatrooms(HttpSession session) {
        
        EmployeeVO loginUser = (EmployeeVO) session.getAttribute("loginuser");

        return chatroomService.getUserChatrooms(loginUser.getEmp_id());
    }



 // 채팅방 나가기(마지막 남은사람이 나가면 방삭제)
    @PutMapping("/chatroom/leave/{roomId}")
    @ResponseBody
    public ResponseEntity<String> leaveChatroom(@PathVariable String roomId, HttpSession session) {
        EmployeeVO loginUser = (EmployeeVO) session.getAttribute("loginuser");

        boolean isDeleted = chatroomService.removeUserFromRoom(roomId, loginUser.getEmp_id());
        
        if (isDeleted) {
            return ResponseEntity.ok("채팅방에서 나갔습니다. (방이 삭제되었습니다.)");
        } 
        else {
            return ResponseEntity.ok("채팅방에서 나갔습니다.");
        }
    }
    
    


}
