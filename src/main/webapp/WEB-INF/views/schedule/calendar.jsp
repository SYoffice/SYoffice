<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%-- temp --%>
<script src='<%= request.getContextPath() %>/js/jquery-3.7.1.min.js'></script>
<script src='<%= request.getContextPath() %>/bootstrap-4.6.2-dist/js/bootstrap.min.js'></script>
<link href='<%= request.getContextPath() %>/bootstrap-4.6.2-dist/css/bootstrap.min.css'   rel='stylesheet' />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.1/css/all.min.css">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11.4.10/dist/sweetalert2.min.css">



<%-- full calendar css --%>
<link href='<%= request.getContextPath() %>/fullcalendar_5.10.1/main.min.css' rel='stylesheet' />
<%-- custom css --%>
<link href='<%= request.getContextPath() %>/css/schedule/sidebar.css'rel='stylesheet' />

<%-- sweat alert --%>
<link href="https://cdn.jsdelivr.net/npm/sweetalert2@11.16.1/dist/sweetalert2.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.16.1/dist/sweetalert2.all.min.js"></script>


<!-- full calendar 에 관련된 script -->
<script src='<%= request.getContextPath() %>/fullcalendar_5.10.1/main.min.js'></script>
<script src='<%= request.getContextPath() %>/fullcalendar_5.10.1/ko.js'></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.24.0/moment.min.js"></script>

<%-- custom js --%>
<script src='<%= request.getContextPath() %>/js/schedule/calendar.js'></script>



<div id="calendar"></div>





<%-- === 마우스로 클릭한 날짜의 일정 등록을 위한 폼 === --%>     
<form name="dateFrm">
	<input type="hidden" name="chooseDate" />	
</form>	

<%-- JS 에서 사용할 path --%>
<input type="hidden" id="path" 	value="${pageContext.request.contextPath}" />

<%-- 사원정보 (세션에 담기는 값) --%>
<input type="hidden" id="fk_emp_id" value="${sessionScope.loginuser.emp_id}" />
<input type="hidden" id="name" value="${sessionScope.loginuser.name}" />
<input type="hidden" id="dept_id" value="${sessionScope.loginuser.fk_dept_id}" />