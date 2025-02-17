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
<link href='<%= request.getContextPath() %>/css/schedule/scheduleIndex.css'rel='stylesheet' />

<%-- sweat alert --%>
<link href="https://cdn.jsdelivr.net/npm/sweetalert2@11.16.1/dist/sweetalert2.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.16.1/dist/sweetalert2.all.min.js"></script>


<!-- full calendar 에 관련된 script -->
<script src='<%= request.getContextPath() %>/fullcalendar_5.10.1/main.min.js'></script>
<script src='<%= request.getContextPath() %>/fullcalendar_5.10.1/ko.js'></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.24.0/moment.min.js"></script>

<%-- custom js --%>
<script src='<%= request.getContextPath() %>/js/schedule/scheduleIndex.js'></script>  



<div id="container">
	<div id="title">
		<h3>일정 관리</h3>
	</div>
	
	<div style="display: flex;">
		<div id="sideMenu">
			<div id="menuList">
				<%-- 사내 캘린더를 보여주는 곳 --%>
				<input type="checkbox" id="allComCal" class="calendar_checkbox" checked/>&nbsp;&nbsp;<label for="allComCal">전사 일정</label>
				<button class="btn_edit showCal" value="1"><i class="fa-solid fa-caret-up"></i></button>
				<button class="btn_edit addCal" style="float: right;" value="1"><i class="fa-solid fa-plus"></i></button>
				<%-- 사내캘린더는 경영지원부에서 행사일정 등록할 때만 쓰인다. --%>
				<div id="companyCal"></div>
				<br>
				
				<%-- 부서 캘린더를 보여주는 곳 --%>
				<input type="checkbox" id="allDeptCal" class="calendar_checkbox" checked/>&nbsp;&nbsp;<label for="allDeptCal">부서명(캘린더)</label>
				<%-- 버튼 값에 부서번호가 들어올 것 --%>
				<button class="btn_edit showCal" value="3"><i class="fa-solid fa-caret-up"></i></button>
				<button class="btn_edit addCal" style="float: right;" value="3"><i class="fa-solid fa-plus"></i></button>
				<%-- 부서 캘린더는 같은 부서원이 등록한 일정을 보이도록 한다. --%>
				<div id="departmentCal"></div>
				<br>

				<%-- 내 캘린더를 보여주는 곳 --%>
				<input type="checkbox" id="allMyCal" class="calendar_checkbox" checked/>&nbsp;&nbsp;<label for="allMyCal">내 일정</label>
				<button class="btn_edit showCal" value="2"><i class="fa-solid fa-caret-up"></i></button>
				<button class="btn_edit addCal" style="float: right;" value="2"><i class="fa-solid fa-plus"></i></button>
				<div id="myCal"></div>
				<br>
				
				<input type="checkbox" id="sharedCal" class="calendar_checkbox" value="0" checked/>&nbsp;&nbsp;<label for="sharedCal">공유받은 캘린더</label> 
			</div>
		</div>
		
		<%-- 풀캘린더가 보여지는 엘리먼트  --%>
		<div id="calendar"></div>
	</div>
</div>

<%-- === 마우스로 클릭한 날짜의 일정 등록을 위한 폼 === --%>     
<form name="dateFrm">
	<input type="hidden" name="chooseDate" />	
</form>	

<%-- JS 에서 사용할 path --%>
<input type="hidden" id="path" 		value="${pageContext.request.contextPath}" />
<%-- 사원정보 --%>
<input type="hidden" id="fk_emp_id" value="1000" />
<input type="hidden" id="name" value="이순신" />
<input type="hidden" id="dept_id" value="2" />