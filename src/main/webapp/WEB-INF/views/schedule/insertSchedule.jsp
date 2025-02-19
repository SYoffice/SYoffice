<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../main/header.jsp" />

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">


<%-- custom css --%>
<link href='<%= request.getContextPath() %>/css/schedule/insertSchedule.css'rel='stylesheet' />
<%-- custom js --%>
<script src='<%= request.getContextPath() %>/js/schedule/insertSchedule.js'></script>

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
      	    frm.action = $("input#path").val()+"/schedule/insertSchedule";
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
                 <li><a href="${pageContext.request.contextPath}/schedule/scheduleIndex">달력 보기</a></li>
             </ul>
        </div>
    </div>
    <div class="contents_wrapper">
		<div class="contents_inner_wrapper">
			<div style="margin: 4% 0 4% 0">
		    	<span class="h3">일정 등록</span>
		    	<span style="float: right; cursor: pointer;" onclick="location.href='${pageContext.request.contextPath}/schedule/scheduleIndex'">캘린더로 돌아가기</span>
	   		</div>
			<form name="scheduleFrm">
				<table id="schedule" class="table table-bordered">
					<tr>
						<th>일자</th>
						<td>
							<input type="date" class="border" id="startDate" value="${requestScope.chooseDate}" style="height: 30px;"/>&nbsp; 
							<select id="startHour" class="schedule border"></select> 시
							<select id="startMinute" class="schedule border"></select> 분
							- <input type="date" id="endDate" class="border" value="${requestScope.chooseDate}" style="height: 30px;"/>&nbsp;
							<select id="endHour" class="schedule border"></select> 시
							<select id="endMinute" class="schedule border"></select> 분&nbsp;
							<input type="checkbox" id="allDay"/>&nbsp;<label for="allDay">종일</label>
							
							<input type="hidden" name="schedule_startdate"/>
							<input type="hidden" name="schedule_enddate"/>
						</td>
					</tr>
					<tr>
						<th>제목</th>
						<td><input type="text" id="schedule_name" name="schedule_name" class="form-control"/></td>
					</tr>
					<tr>
						<th>캘린더선택</th>
						<td>
							<select class="calType schedule border" name="fk_lgcatego_no">
								<c:choose>
								<%-- 일정등록시 사내캘린더 등록은 부서가 인사부원만 등록이 가능하도록 한다. --%> 
									<c:when test="${sessionScope.loginuser.fk_dept_id == 2}"> 
										<option value="">선택하세요</option>
										<option value="2">내 일정</option>
										<option value="1">전사 일정</option>
									</c:when>
								<%-- 일정등록시 내캘린더 등록은 로그인 된 사용자이라면 누구나 등록이 가능하다. --%> 	
									<c:otherwise>
										<option value="">선택하세요</option>
										<option value="2">내 일정</option>
									</c:otherwise>
								</c:choose>
							</select> &nbsp;
							<select class="small_category schedule border" name="fk_smcatego_no"></select>
						</td>
					</tr>
					<tr>
						<th>색상</th>
						<td><input class="border" type="color" id="schedule_color" name="schedule_color" value="#009900"/></td>
					</tr>
					<tr>
						<th>장소</th>
						<td><input type="text" name="schedule_place" class="form-control"/></td>
					</tr>
					
					<tr>
						<th>공유자</th>
						<td>
							<input type="text" id="joinSearchWord" class="form-control" placeholder="회원명 혹은 부서 혹은 지점으로 검색"/>
							<div class="displayUserList"></div>
							<input type="hidden" name="schedule_joinemp"/>
						</td>
					</tr>
					<tr>
						<th>내용</th>
						<td><textarea rows="10" cols="100" style="height: 200px;" name="schedule_content" id="schedule_content"  class="form-control"></textarea></td>
					</tr>
				</table>
				<input type="hidden" value="${sessionScope.loginuser.emp_id}" name="fk_emp_id"/>
			</form>
			<div style="float: right;">
				<%-- <button type="button" id="register" class="border-1 rounded-md" style="margin-right: 10px;">등록</button>--%>
				<button type="button" class="buttonBorder" id="register" style="margin-right: 10px; background-color: #99ccff;">등록</button>
				<button type="button" class="buttonBorder" style="background-color: ecf0f8;" onclick="javascript:location.href='<%= request.getContextPath()%>/schedule/scheduleIndex'">취소</button> 
			</div>
		
		</div>
		
	</div>
	
</div>

<%-- path --%>
<input type="hidden" id="path" value="${pageContext.request.contextPath}" /> 

<%-- === 일정 등록을 위한 폼 === --%>     
<form name="dateFrm">
	<input type="hidden" name="chooseDate" />	
</form>	