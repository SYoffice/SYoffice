<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
   String ctxPath = request.getContextPath();
   //     /syoffice 
%>
<jsp:include page="../main/header.jsp" />

<%-- full calendar css --%>
<link href='<%= request.getContextPath() %>/fullcalendar_5.10.1/main.min.css' rel='stylesheet' />
<%-- custom css --%>
<link href='<%= request.getContextPath() %>/css/schedule/sidebar.css'rel='stylesheet' />

<!-- full calendar 에 관련된 script -->
<script src='<%= request.getContextPath() %>/fullcalendar_5.10.1/main.min.js'></script>
<script src='<%= request.getContextPath() %>/fullcalendar_5.10.1/ko.js'></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.24.0/moment.min.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
		$(document).on("click", "button#registerSchedule", function() {
			const today = new Date();

		 	const year = today.getFullYear(); // 2023
			const month = (today.getMonth() + 1).toString().padStart(2, '0'); // 06
			const day = today.getDate().toString().padStart(2, '0'); // 18

			const dateString = year + '-' + month + '-' + day; // 2023-06-18
			
			$("input[name='chooseDate']").val(dateString);
			
			var frm = document.dateFrm;
      	    frm.method = "POST";
      	    frm.action = "${pageContext.request.contextPath}/schedule/insertSchedule";
      	    frm.submit();
		});// end of $(document).on("click", "button#registerSchedule". function (){})---------------------
		
	});// end of $(document).ready(function(){}) ------------------
</script>

<div class="common_wrapper">
    <div class="side_menu_wrapper">
        <div class="side_menu_inner_wrapper">
            <span class="common_title">캘린더</span>
            <button type="button" id="registerSchedule">일정 등록</button>
            <ul class="side_menu_list" id="side_menu">
                <li>
                	<input style="cursor: pointer;" type="checkbox" id="allComCal" class="calendar_checkbox" checked/>&nbsp;&nbsp;<label style="cursor: pointer;" for="allComCal">전사 일정</label>
                 	<span class="btn btn_edit showComCal"><i class="fa-solid fa-caret-up"></i></span>
                 	<span class="btn btn_edit" onclick='inputSmallCategoInfo("1")' style="float: right;"><i class="fa-solid fa-plus"></i></span>
                	<%-- 전사 캘린더를 보여주는 곳 --%>
                 	<ul id="comCal"></ul>
                </li>
				<!--
                <li>
                	<%-- 부서 캘린더를 보여주는 곳 --%>
                	<input style="cursor: pointer;" type="checkbox" id="allDeptCal" class="calendar_checkbox" checked/>&nbsp;&nbsp;<label style="cursor: pointer;" for="allDeptCal">부서명</label>
                 	<span class="btn btn_edit showDeptCal"><i class="fa-solid fa-caret-up"></i></span>
                 	<span class="btn btn_edit" onclick='inputSmallCategoInfo("부서번호")' style="float: right;" ><i class="fa-solid fa-plus"></i></span>
                 	<%-- 부서 캘린더는 같은 부서원이 등록한 일정을 보이도록 한다. --%>
                 	<ul id="deptCal"></ul>
				</li>
				-->
                <li>
                	<input style="cursor: pointer;" type="checkbox" id="allMyCal" class="calendar_checkbox" checked/>&nbsp;&nbsp;<label style="cursor: pointer;" for="allMyCal">내 일정</label>
                 	<span class="btn btn_edit showMyCal"><i class="fa-solid fa-caret-up"></i></span>
                 	<span class="btn btn_edit" onclick='inputSmallCategoInfo("2")' style="float: right;"><i class="fa-solid fa-plus"></i></span>
                	<%-- 내 캘린더를 보여주는 곳 --%>
                 	<ul id="myCal"></ul>
                </li>
             </ul>
        </div>
    </div>
