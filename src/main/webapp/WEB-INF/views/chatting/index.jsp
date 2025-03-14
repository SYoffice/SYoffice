<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
   String ctxPath = request.getContextPath();
%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/chatting/index.css" />

<jsp:include page="../chatting/sidebar.jsp" />


    <div style="font-size: 25pt; margin-top: 4%;"><i class="fa-brands fa-rocketchat"></i>&nbsp;채팅방 목록</div>
    <table style="margin-top: 1%; font-size: 14pt;" class="table">
        <thead >
            <tr>
                <th style="width: 40%;">채팅방 이름</th>
                <th style="width: 40%; border-right:none; text-align: center;">참여 인원</th>
                <th style="width: 20%; border-left:none;"></th>
            </tr>
        </thead>
        <tbody Id="chatroomList">
        </tbody>
    </table>
    
    

<!-- 모달 창 (채팅방 생성) -->
<div class="modal fade" Id="createChatRoomModal" tabindex="-1" role="dialog" aria-labelledby="createChatRoomLabel" aria-hIdden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-body" style="max-height: 800px; overflow: auto;">
        <div class="form-group">
            <label for="chatRoomName">채팅방 이름</label>
            <input type="text" class="form-control" Id="chatRoomName" name="chatRoomName" required>
        </div>
        <div class="form-group" >
            <label>참여할 사원 선택</label>
            <div Id="employeeList">
                <jsp:include page="../organization/organization2.jsp"></jsp:include>
            </div>
        </div>
        <div class="form-group" style="display: flex;">
            선택된 사원: &nbsp;
            <div Id="selectedEmployees" style="list-style: none; "></div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-primary" onclick="createChatroom()">생성</button>
            <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- 채팅방 상세 정보 모달 -->
<div class="modal fade" id="chatroomDetailModal" tabindex="-1" role="dialog" aria-labelledby="chatroomDetailLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="chatroomDetailLabel">채팅방 정보</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <p><strong>채팅방 이름:</strong> <span id="chatroomDetailName"></span></p>
        <div style="display: flex;" >
        <p><strong>참여 인원:&nbsp; </strong></p>
        <ul id="chatroomDetailMembers"></ul>
        </div>
        <p><strong>생성 날짜:</strong> <span id="chatroomDetailCreatedAt"></span></p>
		      
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
      </div>
    </div>
  </div>
</div>

<input type="hidden" id="loginuserId" value="${sessionScope.loginuser.name}&nbsp;${sessionScope.loginuser.grade_name}">
<script type="text/javascript">
$(document).ready(function() {
    // 채팅방 목록 불러오기
    getChatrooms();

    // 사이드바에서 채팅방 만들기 버튼 클릭 시 모달 열기
    window.addEventListener("message", function(event) {
        if (event.data == "openChatRoomModal") {
            $("#createChatRoomModal").modal("show");
        }
    });
    
    // 모달이 닫힐 때 선택된 사원 목록 초기화
    $('#createChatRoomModal').on('hidden.bs.modal', function () {
        $('#chatRoomName').val(''); // 채팅방 이름 초기화
        $('#selectedEmployees').empty(); // 선택된 사원 목록 초기화
        document.activeElement.blur();
        $('body').focus();
    });
    
});// end of  $(document).ready(function() {}------------------------------------------------------


// 선택한 방으로 입장
function enterChatroom(roomId, roomName) {
    // console.log("채팅방 Id:", roomId); 
    window.location.href = "<%=request.getContextPath()%>/chatting/multichat?roomId=" + roomId;
}


function getselectedEmployeeIds() {
    let employeeIds = []; // 사원번호 배열로 만들기

    // 선택된 사원 목록에서 사원번호 가져오기
    $("#selectedEmployees div").each(function() {
        let empId = \$(this).attr("data-Id"); // 사원번호 가져오기
        if (empId) {
            employeeIds.push(empId);
        }
    });
    // console.log("선택된 사원번호 목록:", employeeIds); 
    return employeeIds;
}

// 방만들기
function createChatroom() {
    let roomName = \$("#chatRoomName").val(); // 채팅방이름 가져옴
    let employees = getselectedEmployeeIds(); // 사원번호 가져옴

    if($("input#chatRoomName").val().trim() == "" ){
	   alert("채팅방 이름을 입력하세요");
	   return;
    }
	
    $.ajax({
        type: "POST",
        url: "<%=request.getContextPath()%>/chatting/chatroom/create",
        data: {roomName: roomName,
        	   employees: employees},
        success: function(response) {
            alert("채팅방이 생성되었습니다!");
            $("#createChatRoomModal").modal("hide"); // 방만들기 모달 숨기기
            getChatrooms(); 
        },
        error: function(request, status, error){
	        alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	    }
    });
}


// 방나가기
function leaveChatroom(roomId) {
    if (!confirm("정말 이 채팅방에서 나가시겠습니까?")) {
        return;
    }

    $.ajax({
        type: "PUT",
        url: `<%=request.getContextPath()%>/chatting/chatroom/leave/\${roomId}`,
        success: function(response) {
            alert(response);
            getChatrooms(); // 채팅방 목록 갱신
        },
        error: function(request, status, error){
	        alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	    }
    });
}


function openChatroomDetailModal(roomId) {
    $.ajax({
        type: "GET",
        url: `<%=request.getContextPath()%>/chatting/chatroom/detail/\${roomId}`,
        success: function(chatroom) {
        	console.log(chatroom);
        	
            $("#chatroomDetailName").text(chatroom.roomName);
            $("#chatroomDetailMembers").empty();
           
            chatroom.employees.forEach(employees => {
            	
                $("#chatroomDetailMembers").append(`<li>\${employees}</li>`);
            });

            let createdAt = new Date(chatroom.createdAt);
            $("#chatroomDetailCreatedAt").text(createdAt.toLocaleString());

            $("#chatroomDetailModal").modal("show"); // 모달 띄우기
        },
        error: function(request, status, error){
	        alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	    }
    });
}


</script>
