<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

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
        	
        </div>
    </div>
</div>