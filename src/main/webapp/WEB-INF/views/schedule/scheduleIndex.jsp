<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
   String ctxPath = request.getContextPath();
   //     /syoffice 
%>


<jsp:include page="./sidebar.jsp"></jsp:include>
    
    <div class="contents_wrapper">
        <div class="contents_inner_wrapper">
        	<%-- 캘린더를 보여주는 곳 --%>
        	<jsp:include page="./calendar.jsp"></jsp:include>
        </div>
    </div>
</div>
