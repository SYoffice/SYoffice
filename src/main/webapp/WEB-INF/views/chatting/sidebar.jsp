<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%
   String ctxPath = request.getContextPath();
   //     /syoffice 
%>      
<jsp:include page="../main/header.jsp" />


<div class="common_wrapper">
    <div class="side_menu_wrapper">
        <div class="side_menu_inner_wrapper">
            <span style="20pt; margin-top: -10%;" class="common_title">메신저</span>
			
			<button type="button" Id="openChatRoomBtn" >채팅방 만들기</button>
			
			<ul class="side_menu_list">
			
            
                <li Id="sidebarChatList"></li>
				
			</ul>
        </div>
    </div>
    
<script>
document.getElementById("openChatRoomBtn").addEventListener("click", function() {
	
    window.parent.postMessage("openChatRoomModal", "*");
});// end of document.getElementById("openChatRoomBtn").addEventListener("click", function() {}------------------------------------------

//채팅방 목록 가져오기
function getChatrooms() {
    let loginuserId = $("#loginuserId").val(); // 로그인한 사용자 Id 가져오기

    $.ajax({
        type: "GET",
        url: "<%=request.getContextPath()%>/chatting/chatroom/list",
        success: function(chatrooms) {
            let chatroomList = \$("#chatroomList"); // index.jsp의 채팅방 테이블
            let sidebarChatList = \$("#sidebarChatList"); // sidebar.jsp의 사이드바 목록
            
            chatroomList.empty(); // 테이블 초기화
            sidebarChatList.empty(); // 사이드바 초기화

            console.log("받은 채팅방 데이터:", chatrooms);

            if (chatrooms.length == 0) {
                chatroomList.append(`
                    <tr>
                        <td colspan="3" class="text-center">참여 중인 채팅방이 없습니다.</td>
                    </tr>
                `);
                sidebarChatList.append("<li class='text-center'></li>");
            }
            else {
                chatrooms.forEach(room => {
                    let employeeCount = room.employees.length;

                    // index.jsp의 테이블에 추가
                    let chatRow = `
                        <tr>
                            <td>
                             <a href="javascript:void(0);" onclick="openChatroomDetailModal('\${room.roomId}')" style="color: black;">
                            	\${room.roomName}
                       		 </a>
                            </td>
                            <td class="text-center">\${employeeCount}명</td>
                            <td class="text-right">
                            	<button onclick="enterChatroom('\${room.roomId}')" class="btn btn-light; border border-dark;">입장하기</button>
                                <button onclick="leaveChatroom('\${room.roomId}')" class="btn btn-danger border border-dark">나가기</button>
                            </td>
                        </tr>`;
                    chatroomList.append(chatRow);
	
                    // sidebar.jsp의 목록에 추가
                    let chatItem = ` 
                        <li>
                            <a href="javascript:voId(0);" onclick="enterChatroom('\${room.roomId}')" style="font-size: 15pt;">
                                \${room.roomName}
                            </a>
                        </li>`;
                        
                    sidebarChatList.append(chatItem);
                });
            }
        },
        error: function(request, status, error){
	        alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	    }
    });
}		
		
		
</script>

    <div class="contents_wrapper">
        <div class="contents_inner_wrapper">
        
        
        
        