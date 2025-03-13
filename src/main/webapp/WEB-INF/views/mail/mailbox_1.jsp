<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="./sidebar.jsp" />

<%-- custom CSS --%>
<link href='<%= request.getContextPath() %>/css/mail/mailbox.css'rel='stylesheet' />
<%-- custom JS --%>
<script src='<%= request.getContextPath() %>/js/mail/mailbox.js'></script>

	<div class="contents_wrapper">
        <div class="contents_inner_wrapper">
        	
        	<div style="margin: 4% 0 4% 0">
				<span class="h3">보낸 메일함</span>
	   		</div>
        	
			<div class="toolbar_wrap">
				<input type="checkbox" id="allMailSelect" style="margin: 0 1% 0 0.8%;"/>
				<span class="toolbar delete"><i class="fa-regular fa-trash-can"></i> 삭제</span>
                <span class="toolbar reply"><i class="fa-solid fa-reply"></i> 답장</span>
                <span class="toolbar forward"><i class="fa-solid fa-share"></i> 전달</span>
                <span class="toolbar ban"><i class="fa-solid fa-ban"></i> 스팸등록</span>
			</div>

			<div class="mailbox_wrap">
				<table id="mailbox_table" class="table">
					<c:if test="${empty requestScope.mailList}">
						<tr>
							<th>보낸 메일이 없습니다.</th>
						</tr>	
					</c:if>
					<c:if test="${!empty requestScope.mailList}">
						<c:forEach var="mail" items="${requestScope.mailList}" varStatus="status">
							<c:if test="${mail.receive_status == 0}">

								<%-- 읽지 않은 메일 목록 --%>
								<tr class="unread">
									<td><input type="checkbox" name="fk_mail_no" class="mailSelect" value="${mail.fk_mail_no}" /></td>
									<td>
										<span><i class="fa-regular fa-envelope"></i></span>
										<c:if test="${mail.attachCnt > 0}">
											<span><i class="fa-solid fa-paperclip"></i></span>
										</c:if>
									</td>
									<td class="sender">
										<input type="hidden" class="fk_emp_id" value="${mail.fk_emp_id}" />
											${mail.name}
									</td>
									<td class="subject">
										<c:if test="${mail.mail_important == 1}">
											<span class="important"><i class="fa-solid fa-exclamation"></i></span>
										</c:if>
										 ${mail.mail_subject}
									</td>
									<td class="text-right">${mail.mail_senddate}</td>
								</tr>
							</c:if>

							<%-- 읽은 메일 목록 --%>
							<c:if test="${mail.receive_status == 1}">
								<tr class="read">
									<td><input type="checkbox" name="fk_mail_no" class="mailSelect" value="${mail.fk_mail_no}" /></td>
									<td>
										<span><i class="fa-regular fa-envelope"></i></span>
										<c:if test="${mail.attachCnt > 0}">
											<span><i class="fa-solid fa-paperclip"></i></span>
										</c:if>
									</td>
									<td class="sender">
										<input type="hidden" class="fk_emp_id" value="${mail.fk_emp_id}" />
											${mail.name}
									</td>
									<td class="subject">
										<c:if test="${mail.mail_important == 1}">
											<span class="important"><i class="fa-solid fa-exclamation"></i></span>
										</c:if>
										 ${mail.mail_subject}
									</td>
									<td class="text-right">${mail.mail_senddate}</td>
								</tr>
							</c:if>

						</c:forEach>
					</c:if>
				</table>
				
				<c:if test="${!empty requestScope.mailList}">
					<div class="pageBar">
						${requestScope.pageBar}
					</div>	
				</c:if>
				
			</div>

        </div>
    </div>
</div>

<%-- JS 활용 용도 --%>
<input type="hidden" id="path" 	value="${pageContext.request.contextPath}" />
<input type="hidden" id="fk_emp_id"	value="${sessionScope.loginuser.emp_id}" />
<input type="hidden" id="division"	value="${requestScope.division}" />