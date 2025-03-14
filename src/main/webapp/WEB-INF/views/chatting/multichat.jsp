<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
	String ctxPath = request.getContextPath();
%>

<%
    String roomId = request.getParameter("roomId");
%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/chatting/chatting.css" />

<jsp:include page="../chatting/sidebar2.jsp" />

<input type="hidden" id="chatRoomId" value="<%= roomId %>">
<%-- #웹채팅관련4 --%>

<script type="text/javascript">



// === !!! WebSocket 통신 프로그래밍은 HTML5 표준으로써 자바스크립트로 작성하는 것이다. !!! === //
// WebSocket(웹소켓)은 웹 서버로 소켓을 연결한 후 데이터를 주고 받을 수 있도록 만든 HTML5 표준이다. 
// 그런데 이러한 WebSocket(웹소켓)은 HTTP 프로토콜로 소켓 연결을 하기 때문에 웹 브라우저가 이 기능을 지원하지 않으면 사용할 수 없다. 
/*
>> 소켓(Socket)이란? 
   - 어떤 통신프로그램이 네트워크상에서 데이터를 송수신할 수 있도록 연결해주는 연결점으로써 
     IP Address와 port 번호의 조합으로 이루어진다. 
     또한 어떤 하나의 통신프로그램은 하나의 소켓(Socket)만을 가지는 것이 아니라 
     동일한 프로토콜, 동일한 IP Address, 동일한 port 번호를 가지는 수십개 혹은 수만 개의 소켓(Socket)을 가질 수 있다.

   =================================================================================================  
     클라이언트  소켓(Socket)                                         서버  소켓(Socket)
     211.238.142.70:7942 ◎------------------------------------------◎  211.238.142.72:9090

     클라이언트는 서버인 211.238.142.72:9099 소켓으로 클라이언트 자신의 정보인 211.238.142.70:7942 을 
     보내어 연결을 시도하여 연결이 이루어지면 서버는 클라이언트의 소켓인 211.238.142.70:7942 으로 데이터를 보내면서 통신이 이루어진다.
   ================================================================================================== 
     
     소켓(Socket)은 데이터를 통신할 수 있도록 해주는 연결점이기 때문에 통신할 두 프로그램(Client, Server) 모두에 소켓이 생성되야 한다.

   Server는 특정 포트와 연결된 소켓(Server 소켓)을 가지고 서버 컴퓨터 상에서 동작하게 되는데, 
   이 Server는 소켓을 통해 Cilent측 소켓의 연결 요청이 있을 때까지 기다리고 있다(Listening 한다 라고도 표현함).
   Client 소켓에서 연결요청을 하면(올바른 port로 들어왔을 때) Server 소켓이 허락을 하여 통신을 할 수 있도록 연결(connection)되는 것이다.
*/
$(document).ready(function(){
	// 채팅방 목록 불러오기
    getChatrooms();
   
    $("div#chatroom").css({"background-color":"#b7c6db"});
	
    var roomId = $("#chatRoomId").val(); 

    const wsUrl = "ws://" + window.location.host + "/syoffice/chatting/multichatstart?roomId=" + encodeURIComponent(roomId);
    console.log("웹소켓 연결 URL:", wsUrl);

    const websocket = new WebSocket(wsUrl); // WebSocket 연결
   // 즉, const websocket = new WebSocket("ws://"+root+"/multichatstart") 이다.
   // "/chatting/multichatstart" 에 대한 것은 
   // com.spring.app.config.WebsocketEchoHandler 에서 설정해두었다.
   
   
   // >> ====== !!중요!! Javascript WebSocket 이벤트 정리 ====== << //
    /*   -------------------------------------
             이벤트 종류          설명
         -------------------------------------
             onopen         WebSocket 연결
             onmessage      메시지 수신
             onerror        전송 에러 발생
             onclose        WebSocket 연결 해제
    */  
   
    let messageObj = {};   // 자바스크립트 객체 생성
    
    // === 웹소켓에 최초로 연결이 되었을 경우에 실행되어지는 콜백함수 정의 === //
    websocket.onopen = function(){
       // alert("웹소켓 연결됨!");
      
    };// end of websocket.onopen -----
    
    // === 메시지 수신 시 콜백함수 정의하기 === //
    websocket.onmessage = function(event) {
       
       // event.data 는 수신되어진 메시지이다.
       // 채팅서버에 연결된 사용자를 수신된 메시지는 「 정두환 엄정화 」 이다.
      if(event.data.substr(0, 1)=="「" && event.data.substr(event.data.length-1)=="」") {
          $("div#connectingUserList").html(event.data);
      }
      else if(event.data.substr(0,1)=="⊆") { 
          $("table#tbl > tbody").html(event.data);
      }
      else{
         // event.data 는 수신받은 채팅문자
          $("div#chatMessage").append(event.data);
          $("div#chatMessage").append("<br>");
          $("div#chatMessage").scrollTop(99999999);
      }
       
    };// end of websocket.onmessage -------------------------------------------
    
    // === 웹소켓 연결 해제시 콜백함수 정의하기 == //
    websocket.onclose = function(){
    }
    
    /////////////////////////////////////////////////////////////////////////////////
    
 	// === 메시지 입력후 엔터하기 === //
    $("input#message").keyup(function(key){
    	if(key.keyCode == 13) {
    		$("input#btnSendMessage").click(); 
        }
    });// end of $("input#message").keyup(function(key){}----------------------------
    		
	// === 메시지 보내기 === //
    let isOnlyOneDialog = false; // 귓속말 여부. true 이면 귓속말, false 이면 모두에게 공개되는 말 
    
    $("input#btnSendMessage").click(function(){
    
       if( $("input#message").val().trim() != "" ) {
    	   let roomId = $("#chatRoomId").val(); // input에서 채팅방 ID 가져오기
    	   
         // ==== 자바스크립트에서 replace를 replaceAll 처럼 사용하기 ====
         // 자바스크립트에서 replaceAll 은 없다.
         // 정규식을 이용하여 대상 문자열에서 모든 부분을 수정해 줄 수 있다.
         // 수정할 부분의 앞뒤에 슬래시를 하고 뒤에 gi 를 붙이면 replaceAll 과 같은 결과를 볼 수 있다. 
         	
             
         	let messageVal = $("input#message").val();
            //messageVal = messageVal.replace(/<script/gi, "&lt;script"); 
            messageVal = messageVal.replace(/</gi, "&lt;"); 
            messageVal = messageVal.replace(/>/gi, "&gt;"); 
            // 스크립트 공격을 막으려고 한 것임.
            
            <%-- 
             messageObj = {message : messageVal
                          ,type : "all"
                          ,to : "all"}; 
            --%>
            // 또는
            messageObj = {}; // 자바스크립트 객체 생성함. 
            messageObj.message = messageVal;
            messageObj.type = "all";   // 공개대화
            messageObj.to = "all";	   // 채팅방에 들어온 모든 웹소켓 id
            messageObj.roomId = roomId; // 채팅방 ID 추가
            
            
            const to = $("input#to").val();
            if( to != "" ){
                messageObj.type = "one"; // 귓속말(비밀대화)
                messageObj.to = to;	     // 귓속말(비밀대화)를 나눌 특정 웹소켓 id
            }
            
            websocket.send(JSON.stringify(messageObj));
            // JSON.stringify() 는 값을 그 값을 나타내는 JSON 표기법의 문자열로 변환한다
         	
         
            
            
            // 위에서 자신이 보낸 메시지를 웹소켓으로 보낸 다음에 자신이 보낸 메시지 내용을 웹페이지에 보여지도록 한다. 
            
            const now = new Date();
            let ampm = "오전 ";
            let hours = now.getHours();
            
            if(hours > 12) {
                hours = hours - 12;
                ampm = "오후 ";
            }
            
            if(hours == 0) {
                hours = 12;
            }
            
            if(hours == 12) {
                ampm = "오후 ";
            }
            
            let minutes = now.getMinutes();
            if(minutes < 10) {
                minutes = "0"+minutes;
            }
          
            const currentTime = ampm + hours + ":" + minutes; 
            
            if(isOnlyOneDialog == false) { // 귓속말이 아닌 경우
                 $("div#chatMessage").append("<div style='background-color: #ffff80; display: inline-block; max-width: 60%; float: right; padding: 7px; border-radius: 10px; word-break: break-all;'>" + messageVal + "</div> <div style='display: inline-block; float: right; padding: 20px 5px 0 0; font-size: 7pt;'>"+currentTime+"</div> <div style='clear: both;'>&nbsp;</div>"); 
            }
            
            else { // 귓속말인 경우. 글자색을 빨강색으로 함.
                 $("div#chatMessage").append("<div style='background-color: #ffff80; display: inline-block; max-width: 60%; float: right; padding: 7px; border-radius: 10px; word-break: break-all; color: red;'>" + messageVal + "</div> <div style='display: inline-block; float: right; padding: 20px 5px 0 0; font-size: 7pt;'>"+currentTime+"</div> <div style='clear: both;'>&nbsp;</div>");
            }
            
            $("div#chatMessage").scrollTop(99999999);
            
            $("input#message").val("");
            $("input#message").focus();
       }
       
    });// end of $("input#btnSendMessage").click(function(){}-------------------------------
    		
   
    
    ///////////////////////////////////////////////////////////////////////////////////////
    
    
});// end of $(document).ready(function() -------------------------------------------------------------

</script>

<div class="container-fluid">
   <div class="row">
      <div id="chatroom" class="col-md-8 mt-4 offset-md-1">
      <input style="font-size: 15pt; font:500; margin-left: 97%;" type="button" class="btn btn-light;" onclick="javascript:location.href='<%=request.getContextPath() %>/chatting/index'" value="X" />
         
         <input type="hidden" id="to" placeholder="귓속말대상웹소켓.getId()"/>
         
         <div id="chatMessage" style="height: 630px; overFlow: auto; margin: 20px 0;"></div>
      	
      	 <div style="display: flex; margin-bottom: 2%;">
         <input type="text"  id="message" class="form-control" placeholder="메시지 내용"/>
         <div style="margin-left: 1%; display: flex; " >
         <input type="button" id="btnSendMessage" class="btn btn-success btn-sm " value="전송하기" />
      	  </div>
      	 </div>
      </div>
   </div>
</div>
 
