<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<jsp:include page="./sidebar.jsp" />

<%-- custom JS --%>
<script src='<%= request.getContextPath() %>/js/mail/mailedit.js'></script>

<%-- custom CSS --%>
<link href='<%= request.getContextPath() %>/css/mail/mailedit.css'rel='stylesheet' />
    
    <div class="contents_wrapper">
        <div class="contents_inner_wrapper">
        	
        	<div style="margin: 4% 0 4% 0">
		    	<span class="h3">메일 쓰기</span>
	   		</div>
        	
        	<div class="toolbar_area">
        		<button type="button" id="sendMail">보내기</button>
        		<button type="button">예약발송</button>
        		<button type="button" id="tempStore">임시저장</button>
        	</div>
        	
        	
        	<%-- 메일 정보 입력하는 공간 --%>
        	<div style="margin-top: 3%;">
        		<form name="mailSend">
	        		<table id="schedule" class="table">
						<tr>
							<th class="title_area">
								<label for="recipient">받는사람</label>
								<input class="title_checkbox" type="checkbox" id="toMe" name="receive_status" value="3">
								<label for="toMe">나에게</label>
							</th>
							<td>
								<div style="display: flex;">
									<input type="text" id="recipient" class="form-control"/>
									<button type="button" class="buttonBorder" id="organization">조직도</button>
								</div>
								<div class="displayRecipientList"></div>
								<input type="hidden" name="recipient" />
							</td>
							
						</tr>
						<tr>
							<th class="title_area">
								<span style="margin-right: 15.5%;">제목</span>
								<input class="title_checkbox" type="checkbox" id="important" >
								<label for="important"><i style="color: red;" class="fa-solid fa-exclamation"></i>&nbsp;중요</label>
							</th>
							<td><input type="text" id="mail_subject" name="mail_subject" class="form-control"/></td>
						</tr>
						<tr>
							<th class="title_area">참조</th>
							<td>
								<div style="display: flex;"> 						
									<input type="text" id="mail_cc" name="mail_cc" class="form-control"/>
									<button type="button" class="buttonBorder" id="mailcc">조직도</button>
								</div>							
								<div class="displayCCUserList"></div>
								<input type="hidden" name="ccuser" />
							</td>
						</tr>
						<tr>
							<th class="title_area">파일첨부</th>
							<td>
								<span style="font-size: 14px;">파일을 마우스로 끌어 오세요</span>
								<div id="fileDrop" class="fileDrop"></div>
							</td>
						</tr>
						<tr>
							<th class="title_area">내용</th>
							<td colspan="2"><textarea rows="10" cols="100" style="height: 200px;" name="mail_content" id="mail_content"  class="form-control"></textarea></td>
						</tr>
					</table>
				</form>
        	</div>
        	
        	
        </div>
    </div>
</div>
	<%-- 조직도 모달 시작 --%>
	<div class="modal fade" id="organization"> <%-- 만약에 모달이 안보이거나 뒤로 가버릴 경우에는 모달의 class 에서 fade 를 뺀 class="modal" 로 하고서 해당 모달의 css 에서 zindex 값을 1050; 으로 주면 된다. --%>  
		<div class="modal-dialog modal-dialog-centered modal-lg">
	  		<div class="modal-content">
	  
	    		<!-- Modal header -->
				<div class="modal-header">
	  				<h4 class="modal-title text-center">주소록</h4>
	  				<button type="button" class="close organization" data-dismiss="modal">&times;</button>
				</div>
	
				<!-- Modal body -->
				<div class="modal-body">
	 				<div id="organization_Info">
						<jsp:include page="../organization/organization.jsp"></jsp:include>	     			
	  				</div>
				</div>
	
				<!-- Modal footer -->
				<%-- 
	      		<div class="modal-footer text-center">
	      			<div style="width:100%; margin: 0 auto;">
		        		<button style="margin-right: 10%;" type="button" class="btn btn-success modalFooter" id="confirm" data-toggle="modal" data-target="#updateConfirm">변경</button>
		        		<button type="button" class="btn btn-danger modalFooter" data-dismiss="modal">취소</button>
	        		</div>
	      		</div>
	      		--%>
	    	</div>
		</div>
	</div>
	<%-- 조직도 모달 끝 --%>



<%-- JS 활용 용도 --%>
<input type="hidden" id="path" 		 value="${pageContext.request.contextPath}" />
<input type="hidden" id="fk_dept_id" value="${sessionScope.loginuser.fk_dept_id}" />
<input type="hidden" id="mail" 		 value="${sessionScope.loginuser.mail}" />
<input type="hidden" id="emp_id"	 value="${sessionScope.loginuser.emp_id}" />
<input type="hidden" id="mail_no"	 value="${requestScope.fk_mail_no}" />