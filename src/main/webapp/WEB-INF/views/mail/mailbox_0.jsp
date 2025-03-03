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
				<span class="h3">받은 메일함</span>
				<span>${requestScope.mailCntMap.cnt}</span> / <span>${requestScope.mailCntMap.total}</span>
	   		</div>
        	
			<div class="toolbar_wrap">
				<input type="checkbox" id="allMailSelect" />
				<span>삭제</span>
				<span>읽음</span>
				<span>스팸등록</span>
			</div>

			<div class="mailbox_wrap">
				<table id="mailbox_table" class="table">
					<c:if test="${empty requestScope.mailList}">
						<tr>
							<th>받은 메일이 없습니다.</th>
						</tr>
					</c:if>
					<c:if test="${!empty requestScope.mailList}">
						<c:forEach var="mail" items="${requestScope.mailList}" varStatus="status">
							<c:if test="${mail.receive_status == 0}">

								<%-- 읽지 않은 메일 목록 --%>
								<tr class="text-center unread">
									<td><input type="checkbox" name="fk_mail_no" class="mailSelect" value="${mail.fk_mail_no}" /></td>
									<td>
										<span><i class="fa-regular fa-envelope"></i></span>
										<c:if test="${mail.attachCnt > 0}">
											<span><i class="fa-solid fa-paperclip"></i></span>
										</c:if>
									</td>
									<td class="sender">${mail.name}</td>
									<td class="subject">
										<c:if test="${mail.mail_important == 1}">
											<span class="important"><i class="fa-solid fa-exclamation"></i> (중요)</span> ${mail.mail_subject}
										</c:if>
										<c:if test="${mail.mail_important == 0}">
											${mail.mail_subject}
										</c:if>
									</td>
									<td>${mail.mail_senddate}</td>
								</tr>
							</c:if>

							<%-- 읽은 메일 목록 --%>
							<c:if test="${mail.receive_status == 1}">
								<tr class="text-center read">
									<td><input type="checkbox" name="fk_mail_no" class="mailSelect" value="${mail.fk_mail_no}" /></td>
									<td>
										<span><i class="fa-regular fa-envelope"></i></span>
										<c:if test="${mail.attachCnt > 0}">
											<span><i class="fa-solid fa-paperclip"></i></span>
										</c:if>
									</td>
									<td class="sender">${mail.name}</td>
									<td class="subject">
										<c:if test="${mail.mail_important == 1}">
											<span class="important"><i class="fa-solid fa-exclamation"></i> (중요)</span> ${mail.mail_subject}
										</c:if>
										<c:if test="${mail.mail_important == 0}">
											${mail.mail_subject}
										</c:if>

									</td>
									<td>${mail.mail_senddate}</td>
								</tr>
							</c:if>

						</c:forEach>
					</c:if>

				</table>
			</div>

        </div>
    </div>
</div>

<%-- JS 활용 용도 --%>
<input type="hidden" id="path" 	value="${pageContext.request.contextPath}" />