<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%-- temp --%>
<script src='<%= request.getContextPath() %>/js/jquery-3.7.1.min.js'></script>
<script src='<%= request.getContextPath() %>/bootstrap-4.6.2-dist/js/bootstrap.min.js'></script>
<link href='<%= request.getContextPath() %>/bootstrap-4.6.2-dist/css/bootstrap.min.css' rel='stylesheet' />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.1/css/all.min.css">



<%-- custom css --%>
<link href='<%= request.getContextPath() %>/css/schedule/insertSchedule.css'rel='stylesheet' />
<%-- custom js --%>
<script src='<%= request.getContextPath() %>/js/schedule/insertSchedule.js'></script>


<div id="container">
	<div id="title">
		<h3>일정 등록</h3>
	</div>
	
	<div style="display: flex;">
		<div id="sideMenu">
			<div id="menuList">
				<%-- 사내 캘린더를 보여주는 곳 --%>
				<input type="checkbox" id="allComCal" class="calendar_checkbox" checked/>&nbsp;&nbsp;<label for="allComCal">사내 캘린더</label>
				<%-- 사내캘린더는 경영지원부에서 행사일정 등록할 때만 쓰인다. --%>
				<div id="companyCal"></div>
				
				<%-- 부서 캘린더를 보여주는 곳 --%>
				<input type="checkbox" id="allDeptCal" class="calendar_checkbox" checked/>&nbsp;&nbsp;<label for="allDeptCal">부서명(캘린더)</label>
				<%-- 부서 캘린더는 같은 부서원이 등록한 일정을 보이도록 한다. --%>
				<div id="departmentCal"></div>
				
				<%-- 내 캘린더를 보여주는 곳 --%>
				<input type="checkbox" id="allMyCal" class="calendar_checkbox" checked/>&nbsp;&nbsp;<label for="allMyCal">내 캘린더</label>
				<button id="myCal" class="btn_edit" style="float: right;"><i class="fa-solid fa-plus"></i></button>
				<div id="myCal"></div>
			</div>
		</div>
		
		<%-- 일정을 입력하는 폼 태그  --%>
		<div id="scheduleInput">
			<form name="scheduleFrm">
				<table id="schedule" class="table table-bordered">
					<tr>
						<th>일자</th>
						<td>
							<input type="date" id="startDate" value="${requestScope.chooseDate}" style="height: 30px;"/>&nbsp; 
							<select id="startHour" class="schedule"></select> 시
							<select id="startMinute" class="schedule"></select> 분
							- <input type="date" id="endDate" value="${requestScope.chooseDate}" style="height: 30px;"/>&nbsp;
							<select id="endHour" class="schedule"></select> 시
							<select id="endMinute" class="schedule"></select> 분&nbsp;
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
							<select class="calType schedule" name="fk_lgcatego_no">
								<c:choose>
								<%-- 사내 캘린더 추가를 할 수 있는 직원은 직위코드가 3 이면서 부서코드가 4 에 근무하는 사원이 로그인 한 경우에만 가능하도록 조건을 걸어둔다.
									<c:when test="${loginuser.fk_pcode =='3' && loginuser.fk_dcode == '4' }">
										<option value="">선택하세요</option>
										<option value="1">내 캘린더</option>
										<option value="2">사내 캘린더</option>
									</c:when>
								--%> 
								<%-- 일정등록시 사내캘린더 등록은 부서가 인사부원만 등록이 가능하도록 한다. --%> 
									<c:when test="${loginuser.gradelevel =='10'}"> 
										<option value="">선택하세요</option>
										<option value="2">내 일정</option>
										<option value="1">전사 일정</option>
									</c:when>
								<%-- 일정등록시 내캘린더 등록은 로그인 된 사용자이라면 누구나 등록이 가능하다. --%> 	
									<c:otherwise>
										<option value="">선택하세요</option>
										<option value="1">내 일정</option>
									</c:otherwise >
								</c:choose>
							</select> &nbsp;
							<select class="small_category schedule" name="fk_smcatego_no"></select>
						</td>
					</tr>
					<tr>
						<th>색상</th>
						<td><input type="color" id="schedule_color" name="schedule_color" value="#009900"/></td>
					</tr>
					<tr>
						<th>장소</th>
						<td><input type="text" name="place" class="form-control"/></td>
					</tr>
					
					<tr>
						<th>공유자</th>
						<td>
							<input type="text" id="joinUserName" class="form-control" placeholder="일정을 공유할 회원명을 입력하세요"/>
							<div class="displayUserList"></div>
							<input type="hidden" name="schedule_joinemp"/>
						</td>
					</tr>
					<tr>
						<th>내용</th>
						<td><textarea rows="10" cols="100" style="height: 200px;" name="content" id="content"  class="form-control"></textarea></td>
					</tr>
				</table>
			</form>
			<div style="float: right;">
				<button type="button" id="register" class="btn_normal" style="margin-right: 10px; background-color: #0071bd;">등록</button>
				<button type="button" class="btn_normal" style="background-color: #990000;" onclick="javascript:location.href='<%= request.getContextPath()%>/schedule/scheduleIndex'">취소</button> 
			</div>
		</div>
		
	</div>
	
</div>