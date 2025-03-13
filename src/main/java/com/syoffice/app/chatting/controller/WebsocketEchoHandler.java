package com.syoffice.app.chatting.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;

import com.syoffice.app.chatting.domain.MessageVO;
import com.syoffice.app.chatting.domain.Mongo_messageVO;
import com.syoffice.app.chatting.service.ChattingMongoOperations;
import com.syoffice.app.employee.domain.EmployeeVO;

// === (#웹채팅관련6) === //
@Component // (bean으로 올라감)
public class WebsocketEchoHandler extends TextWebSocketHandler {
	
   // === 웹소켓서버에 연결한 클라이언트 사용자들을 저장하는 리스트 === 웹소켓에 접속된 클라이언트를 아라옵ㅁ
   private List<WebSocketSession> connectedUsers = new ArrayList<>();
   
   // === 웹소켓 서버에 접속시 채팅에 접속한 사용자ID, 성명, 이메일 정보를 보여주기 위해 채팅에 접속한 EmployeeVO 를 저장하는 List   
   private List<EmployeeVO> employeevo_list = new ArrayList<>();
   
   // ========== 몽고DB 시작 ========== //
   // === (#웹채팅관련8) === //
   @Autowired
   private ChattingMongoOperations chattingMongo;
   
   @Autowired
   private Mongo_messageVO dto;
   
   // ========== 몽고DB 끝 ========== //
    
   // init-method(초기화 메소드)
   public void init() throws Exception{}
   
   
   // === 클라이언트가 웹소켓서버에 연결했을때의 작업 처리하기 ===
    /*
       afterConnectionEstablished(WebSocketSession wsession) 메소드는 
               클라이언트가 웹소켓서버에 연결이 되어지면 자동으로 실행되는 메소드로서
       WebSocket 연결이 열리고 사용이 준비될 때 호출되어지는(실행되어지는) 메소드이다.
    */
   @Override
   public void afterConnectionEstablished(WebSocketSession wsession) throws Exception {
	   
	   // 웹소켓서버에 접속한 클라이언트의 IP Address 얻어오기
       /*
         STS 메뉴의 
         Run --> Run Configuration 
             --> Arguments 탭
             --> VM arguments 속에 맨 뒤에
             --> 한칸 띄우고 -Djava.net.preferIPv4Stack=true 
                        을 추가한다.  
       */
  //   System.out.println("===> 웹채팅확인용 : " + wsession.getId() + " 님이 접속했습니다." );
     // ===> 웹채팅확인용 : cfda9ae6-b779-84f7-5843-a9cdbc02fd86 님이 접속했습니다. 
     // ===> 웹채팅확인용 : 2b29f053-bb34-3a56-f3cf-6de1b8ad1e96 님이 접속했습니다.
     // ===> 웹채팅확인용 : a4a51287-2d13-3405-3ff1-c15ef11a1e71 님이 접속했습니다.
     
     
     System.out.println("====> 웹채팅확인용 : " + "연결 IP : " + wsession.getRemoteAddress().getAddress().getHostAddress()); 
     // ====> 웹채팅확인용 : 연결 IP : 192.168.0.210
  
     connectedUsers.add(wsession);
     
     // === 웹소켓 서버에 접속시 접속자 명단을 알려주기 위한 것 시작(세션정보를 알아와야 함) === //
     // Spring에서 WebSocket 사용시 먼저 HttpSession에 저장된 값들을 읽어와서 사용하기
       /*
          com.spring.app.config.WebSocketConfiguration 클래스 파일에서
         
          @Override
          public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) 메소드내에
          addInterceptors(new HttpSessionHandshakeInterceptor()); 를 추가해주면 WebsocketEchoHandler 클래스를 사용하기 전에 
          먼저 HttpSession에 저장되어진 값들을 읽어 들여, WebsocketEchoHandler 클래스에서 사용할 수 있도록 처리해준다.  
       */

     // >>>>> 웹소켓 서버에 접속시 채팅에 접속한 사용자ID, 성명, 이메일 정보를 보여주기 위한 것 시작 <<<<< //
     String v_html = "⊆";  // 'ㄷ'에 있는 것임
       if(employeevo_list.size() > 0) {
          for(EmployeeVO EmployeeVO : employeevo_list) {
             v_html += "<tr>"
                   + "<td>"+EmployeeVO.getEmp_id()+"</td>"
                   + "<td>"+EmployeeVO.getProfile_img()+"</td>"
                   + "<td>"+EmployeeVO.getName()+"</td>"
                   + "<td>"+EmployeeVO.getGrade_name()+"</td>"
                   + "<td>"+EmployeeVO.getDept_name()+"</td>"
                   + "<td>"+EmployeeVO.getMail()+"</td>"
                   + "</tr>";
          }
          for(WebSocketSession webSocketSession : connectedUsers) {
              webSocketSession.sendMessage(new TextMessage(v_html));
           }// end of for------------------------
       }
     // >>>>> 웹소켓 서버에 접속시 채팅에 접속한 사용자ID, 성명, 이메일 정보를 보여주기 위한 것  끝 <<<<< //
       
	   Map<String, Object> map = wsession.getAttributes();
	   String queryString = wsession.getUri().getQuery(); 
	    EmployeeVO loginuser = (EmployeeVO)map.get("loginuser");

	    String roomId = null;
	    if (queryString != null) {
	        for (String param : queryString.split("&")) {
	            String[] pair = param.split("=");
	            if (pair.length == 2 && pair[0].equals("roomId")) {
	                roomId = pair[1];
	                break;
	            }
	        }
	    }

	    if (roomId == null) {
	        wsession.close(); // roomId가 없으면 WebSocket 연결 종료
	        return;
	    }
	    
	    wsession.getAttributes().put("roomId", roomId);

	    // System.out.println("채팅방 ID: " + roomId);
	
   
      
	    // ============ 몽고DB 시작 ============ //
        List<Mongo_messageVO> list = chattingMongo.listChatting(roomId);  // 몽고DB에 저장된 채팅내용(지난대화)을 읽어온다.
     
        SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREAN);
        
        if(list != null && list.size() > 0) { // 이전에 나누었던 대화내용이 있다라면 
     	   
     	   for(int i=0; i<list.size(); i++) {
     		   
     		  String Profile = list.get(i).getProfile_img();  
         	  String profileImage = (Profile != null) ? Profile : "기본이미지.png";
     		   
     		   String str_created = sdfrmt.format(list.get(i).getCreated()); // 대화내용을 나누었던 날짜를 읽어온다. 
     		   
     		  /*
     		   System.out.println(list.get(i).getUserid() + "\n"
     				            + list.get(i).getName() + "\n"       
     				            + list.get(i).getCurrentTime() + "\n"
     				            + list.get(i).getMessage() + "\n"
     				            + list.get(i).getCreated() + "\n"
     				            + list.get(i).getDept_name() + "\n"
     				            + str_created + "\n" 
     				            + list.get(i).getCurrentTime() + "\n");
     		   */
     		   // =================================================== //
     		   
     		   boolean is_newDay = true; // 대화내용의 날짜가 같은 날짜인지 새로운 날짜인지 알기위한 용도임.
     		   
     		   if(i>0 && str_created.equals(sdfrmt.format(list.get(i-1).getCreated())) ) { // 다음번 내용물에 있는 대화를 했던 날짜가 이전 내용물에 있는 대화를 했던 날짜와 같다라면 
     			   is_newDay = false; // 이 대화내용은 새로운 날짜의 대화가 아님을 표시한다.
     		   }
     		   
     		   if(is_newDay) {
     			   wsession.sendMessage(
		    			  new TextMessage("<div style='text-align: center; width:26%; opacity : 0.8; border-radius: 30px; margin: 0 auto; background-color: #ededed;'> <i class=\"fa-solid fa-calendar-days\"></i>&nbsp;&nbsp;" + str_created + "</div> </br>")  
		    		   ); // 대화를 나누었던 날짜를 배경색을 회색으로 하여 보여주도록 한다.  
     		   }
     		   
     		   
     		   map = wsession.getAttributes();
  	       	   /*
  	       	      wsession.getAttributes(); 은 
  	       	      HttpSession에 setAttribute("키",오브젝트); 되어 저장되어진 값들을 읽어오는 것으로써,
  	       	      리턴값은  "키",오브젝트로 이루어진 Map<String, Object> 으로 받아온다.
  	       	   */ 
     		   
     		   loginuser = (EmployeeVO)map.get("loginuser");  
     	       // "loginuser" 은 HttpSession에 저장된 키 값으로 로그인 되어진 사용자이다.
     		   
     		   if(loginuser.getEmp_id().equals(list.get(i).getUserid())) { 
     			   // 본인이 작성한 채팅메시지일 경우라면.. 작성자명 없이 노랑배경색에 오른쪽 정렬로 보이게 한다.
		        	  
          		  wsession.sendMessage(
  					  new TextMessage("<div style='background-color: #ffff80; display: inline-block; max-width: 60%;  float: right; padding: 7px; border-radius: 10px; word-break: break-all;'>" + list.get(i).getMessage() + "</div> <div style='display: inline-block; float: right; padding: 20px 5px 0 0; font-size: 7pt;'>"+list.get(i).getCurrentTime()+"</div> <div style='clear: both;'>&nbsp;</div>")  
  				  ); 
  		        	  
  	      	   }
     		   else { // 다른 사람이 작성한 채팅메시지일 경우라면.. 작성자명이 나오고 흰배경색으로 보이게 한다.
  	      		  
          		  wsession.sendMessage(
          				new TextMessage("<div style='display: flex; flex-direction: column; align-items: flex-start; gap: 3px;'>" +
          		                "<span style='font-weight: bold; margin-bottom: 3px; cursor: pointer; text-align: left;' class='loginuserName'>" +
          		                  list.get(i).getName() + " " + list.get(i).getGrade_name() + "(" + list.get(i).getDept_name() + ")" + "</span>" +
          		                "<div style='display: flex; align-items: flex-start; gap: 10px;'>" +
          		                "<img src='/syoffice/resources/profile/" + profileImage + "' style='width: 40px; height: 40px; border-radius: 50%;'>" +
          		                "<div style='display: flex; align-items: center;'>" +
          		                "<div style='position: relative; background-color: white; display: inline-block; margin: 8px 7px; padding: 7px 10px; " +
	                            "border-radius: 10px; word-break: break-word;'>" + list.get(i).getMessage() +
	                            "<div style='content: \"\"; position: absolute; left: -8px; top: 50%; transform: translateY(-50%); width: 0; height: 0; " +
	                            "border-top: 8px solid transparent; border-bottom: 8px solid transparent; border-right: 8px solid white;'>" +"</div>" + "</div>" + 
	                            "<div style='font-size: 7pt; margin-top: 2px; margin-left: 5px;'>" + list.get(i).getCurrentTime() + "</div>" + "</div>" + "</div>" + "</div>"));
  	      	  }
     	   
     	   }// end of for------------------
        
        }// end of if(list != null && list.size() > 0)---------
        
     
        	
        
     // ============ 몽고DB 끝 ============ //
     
   
   } // end of public void afterConnectionEstablished(WebSocketSession wsession) throws Exception {} --------------------------- 연결해오기만 하면 자동적으로 실행이 됨
   
   
   // === 클라이언트가 웹소켓 서버로 메시지를 보냈을때의 Send 이벤트를 처리하기 === // 
   // 여기에서 전달받은 메시지를 처리해준다(클라이언트가 메시지를 보낸 것을 => jsp에서 send(92번째 줄)
    /*
       handleTextMessage(WebSocketSession wsession, TextMessage message) 메소드는 
       클라이언트가 웹소켓서버로 메시지를 전송했을 때 자동으로 호출되는(실행되는) 메소드이다.
       첫번째 파라미터  WebSocketSession 은  메시지를 보낸 클라이언트임.
       두번째 파라미터  TextMessage 은  메시지의 내용임.
     */
   @Override
   public void handleTextMessage(WebSocketSession wsession, TextMessage message) throws Exception { // 메소드가 자동적으로 호출됨
	   
        Map<String, Object> map = wsession.getAttributes(); // 세션정보를 다 읽어온다
     
        EmployeeVO loginuser = (EmployeeVO)map.get("loginuser");
   
        MessageVO messageVO = MessageVO.convertMessage(message.getPayload());
        String roomId = (String) wsession.getAttributes().get("roomId");  // 🌟 추가: 현재 세션의 roomId 가져오기
      
        Date now = new Date(); // 현재시각 (java.util)
        String currentTime = String.format("%tp %tl:%tM",now,now,now); 
        // %tp              오전, 오후를 출력
        // %tl              시간을 1~12 으로 출력
        // %tM              분을 00~59 으로 출력
        
        
        for(WebSocketSession webSocketSession : connectedUsers) { // webSocketSession 에 정보가 다 담겨있음, connectedUsers 에는 현재접속자 뿐만 아니라 다른 사람들도 들어있음
    	 String sessionRoomId = (String) webSocketSession.getAttributes().get("roomId");
    	 String Profile = loginuser.getProfile_img();  
    	 String profileImage = (Profile != null) ? Profile : "기본이미지.png";
    	 // System.out.println(Profile);
    	 // System.out.println(profileImage);
    	 
         if("all".equals(messageVO.getType()) ) {
            // 채팅할 대상이 "전체" 인 공개대화인 경우
            // 메시지를 자기자신을 뺀 나머지 모든 사용자들에게 메시지를 보냄.
            
            if( !wsession.getId().equals(webSocketSession.getId()) && roomId.equals(sessionRoomId) ) { // wsession.getId() : 나를 뺀 다른 사람들한테 보냄, webSocketSession : 나를 뺀 사람들
               // wsession 은 메시지를 보낸 클라이언트임.
                   // webSocketSession 은 웹소켓서버에 연결된 모든 클라이언트중 하나임.
                   // wsession.getId() 와  webSocketSession.getId() 는 자동증가되는 고유한 값으로 나옴 
            	
               // 누가 입장했음을 보여준다.(loginuser.getName() 는 로그인해서 입장한 사람)
            	webSocketSession.sendMessage(
            	new TextMessage("<div style='display: flex; flex-direction: column; align-items: flex-start; gap: 3px;'>" +
            		    "<span style='font-weight: bold; margin-bottom: 3px; cursor: pointer; text-align: left;' class='loginuserName'>" + 
            		        loginuser.getName() + " " + loginuser.getGrade_name() + "("+ loginuser.getDept_name() +")" +"</span>" +
            		    "<div style='display: flex; align-items: flex-start; gap: 10px;'>" +
        		        "<img src='/syoffice/resources/profile/" + profileImage + "' style='width: 40px; height: 40px; border-radius: 50%;'>" + 
        		        "<div style='display: flex; align-items: center;'>" +
    		            "<div style='position: relative; background-color: white; display: inline-block; margin:8px 7px; padding: 7px 10px; " +
		                "border-radius: 10px; word-break: break-word;'>" + messageVO.getMessage() +
		                "<div style='content: \"\"; position: absolute; left: -8px; top: 50%; transform: translateY(-50%); width: 0; height: 0; " +
	                    "border-top: 8px solid transparent; border-bottom: 8px solid transparent; border-right: 8px solid white;'>" + "</div>" + "</div>" +
    		            "<div style='font-size: 7pt; marginn-top: 2px; margin-left: 5px;'>" + currentTime + "</div>" + "</div>" + "</div>" + "</div>"));

            }
            
         }
         else { // 채팅할 대상이 "전체"가 아닌 특정대상(귀속말대상웹소켓.getId()임)인 귓속말 채팅인 경우 => one
            
            String ws_id = webSocketSession.getId();
                     // webSocketSession 은 웹소켓서버에 연결한 모든 클라이언트중 하나이며, 그 클라이언트의 웹소켓의 고유한 id 값을 알아오는 것임.
            
            //전달받은 특정 id값이 messageVO에 들어오고 있다
            if(messageVO.getTo().equals(ws_id)) { // 채팅방에 들어온 모든 웹소켓 id
            // messageVO.getTo() 는 클라이언트가 보내온 귓속말대상웹소켓.getId() 임.
               webSocketSession.sendMessage( // wsession.getId() 는 귓속말 때문에 해줌
                     new TextMessage("<span style='display:none'>"+wsession.getId()+"</span>&nbsp;<span style='font-weight:bold; cursor:pointer;' class='loginuserName'>" +loginuser.getName()+ "</span>]<br><div style='background-color: white; display: inline-block; max-width: 60%; padding: 7px; border-radius: 15%; word-break: break-all; color: red;'>"+ messageVO.getMessage() +"</div> <div style='display: inline-block; padding: 20px 0 0 5px; font-size: 7pt;'>"+currentTime+"</div> <div>&nbsp;</div>"));
                                                                                                                                                                                                                                                  /* word-break: break-all; 은 공백없이 영어로만 되어질 경우 해당구역을 빠져나가므로 이것을 막기위해서 사용한다. */
               break;  // 지금의 특정대상(지금은 귓속말대상 웹소켓id)은 1개이므로 
                       // 특정대상(지금은 귓속말대상 웹소켓id 임)에게만 메시지를 보내고  break;를 한다.
            }
         }
         
      } // end of for ---------------------------------
      
      // ============================ 몽고DB 시작 ============================ //
  	  // === 상대방에게 대화한 내용을 위에서 보여준 후, 채팅할 대상이 "전체" 인 공개대화에 대해서만 몽고DB에 저장하도록 한다. 귓속말은 몽고DB에 저장하지 않도록 한다. === // 
      if("all".equals(messageVO.getType())) {
        String _id = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", Calendar.getInstance()); 
        _id += System.nanoTime();
        dto.set_id(_id);
        dto.setRoomId(roomId); 
        dto.setMessage(messageVO.getMessage());
        dto.setTo(messageVO.getTo());
        dto.setType(messageVO.getType());
        dto.setProfile_img(loginuser.getProfile_img());
        dto.setGrade_name(loginuser.getGrade_name());
        dto.setDept_name(loginuser.getDept_name());
        dto.setUserid(loginuser.getEmp_id());
        dto.setName(loginuser.getName());
        dto.setCurrentTime(currentTime);
        dto.setCreated(new Date());
        
        chattingMongo.insertMessage(dto);
      }
      // ============================ 몽고DB 끝 ============================ //
      
      
      
   } // end of public void handleTextMessage(WebSocketSession wsession, TextMessage message) throws Exception -------------------------
   
   
   
   // === 클라이언트가 웹소켓서버와의 연결을 끊을때 작업 처리하기 ===
    /*
       afterConnectionClosed(WebSocketSession session, CloseStatus status) 메소드는 
          클라이언트가 연결을 끊었을 때 
          즉, WebSocket 연결이 닫혔을 때(채팅페이지가 닫히거나 채팅페이지에서 다른 페이지로 이동되는 경우) 자동으로 호출되어지는(실행되어지는) 메소드이다.
             (누가누가 나갔다~ 표시)
    */
   @Override
    public void afterConnectionClosed(WebSocketSession wsession, CloseStatus status) throws Exception {
       // 파라미터 WebSocketSession wsession 은 연결을 끊은 웹소켓 클라이언트임.
       // 파라미터 CloseStatus 은 웹소켓 클라이언트의 연결 상태.
   
      Map<String, Object> map = wsession.getAttributes();  // 세션 얻어옴
      EmployeeVO loginuser = (EmployeeVO) map.get("loginuser");
      
      
   //  System.out.println("====> 웹채팅확인용 : 웹세션ID " + wsession.getId() + "이 퇴장했습니다.");
      
       connectedUsers.remove(wsession); 
       // 웹소켓 서버에 연결되어진 클라이언트 목록에서 연결은 끊은 클라이언트는 삭제시킨다.  
      
       
       
       // ===== 접속을 끊을시 현재 남아있는 접속자명단을 알려주기 위한 것 시작 ===== //
      String connectingUserName = "";  // 「 은 자음 ㄴ을 하면 나온다.
     
      
      
      
      // >>>>> 접속을 끊을시 현재 남아있는 채팅에 접속한 사용자ID, 성명, 이메일 정보를 보여주기 위한 것 시작 <<<<< //
      if(employeevo_list.size() > 0) {
              for(EmployeeVO EmployeeVO : employeevo_list) {
                 if(EmployeeVO.getEmp_id().equals(loginuser.getEmp_id())) {
                	 employeevo_list.remove(EmployeeVO);
                    break;
                 }
              }
              
              String v_html = "⊆";  // 'ㄷ'에 있는 것임
               if(employeevo_list.size() > 0) {
                  for(EmployeeVO EmployeeVO : employeevo_list) {
                     v_html += "<tr>"
                           + "<td>"+EmployeeVO.getEmp_id()+"</td>"
                           + "<td>"+EmployeeVO.getName()+"</td>"
                           + "<td>"+EmployeeVO.getMail()+"</td>"
                           + "</tr>";
                  }
                  for(WebSocketSession webSocketSession : connectedUsers) {
                      webSocketSession.sendMessage(new TextMessage(v_html));
                   }// end of for------------------------
               }
           }
      
      // >>>>> 접속을 끊을시 현재 남아있는 채팅에 접속한 사용자ID, 성명, 이메일 정보를 보여주기 위한 것 끝 <<<<< //
      
      
      
      
      
   } // end of public void afterConnectionClosed(WebSocketSession wsession, CloseStatus status) throws Exception {} ---------------------------
   
   
   
}








