<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%
   String ctxPath = request.getContextPath();
   //     /syoffice 
%>
      

<jsp:include page="./sidebar.jsp" />

<%-- custom css --%>
<link href='<%= request.getContextPath() %>/css/schedule/detailSchedule.css'rel='stylesheet' />

<%-- custom js --%>
<script src='<%= request.getContextPath() %>/js/schedule/detailSchedule.js'></script>



    <div class="contents_wrapper">
    	
        <div style="display: flex;" class="contents_inner_wrapper">
        	<div id="detailScheduleInfo">
	        	<div style="margin: 4% 0 4% 0">
			    	<span class="h3">일정 상세</span>
			    	<span style="float: right; cursor: pointer;" onclick="location.href='${pageContext.request.contextPath}/schedule/scheduleIndex'">캘린더로 돌아가기</span>
		   		</div>
        		<table class="table table-borderd">
        			<tr>
        				<th>일자</th>
        				<td>
	        				<span id="startdate">${requestScope.map.startdate}</span>&nbsp;~&nbsp;<span id="enddate">${requestScope.map.enddate}</span>&nbsp;&nbsp;  
							<input type="checkbox" id="allDay" disabled/>&nbsp;종일
						</td>
        			</tr>
        			<tr>
        				<th>일정명</th>
        				<td>${requestScope.map.schedule_name}</td>
        			</tr>
        			<tr>
        				<th>일정분류</th>
        				<td>
        					<c:choose>
        						<%-- 전사 일정일 경우 --%>
        						<c:when test="${requestScope.map.fk_lgcatego_no == 1}">
        							전사 일정 - ${requestScope.map.smcatego_name} 
        						</c:when>
        						<%-- 내 일정일 경우 --%>
        						<c:when test="${requestScope.map.fk_lgcatego_no == 2}">
        							내 일정 - ${requestScope.map.smcatego_name}
        						</c:when>
								<%-- 부서 일정일 경우 --%>
								<c:otherwise>
									${requestScope.map.dept_name} - ${requestScope.map.smcatego_name}
								</c:otherwise>
        					</c:choose>
        				</td>
        			</tr>
        			<tr>
        				<th>장소</th>
        				<td>${requestScope.map.schedule_place}</td>
        			</tr>
        			<tr>
        				<th>참석자</th>
        				<td>${requestScope.map.schedule_joinemp}</td>
        			</tr>
        			<tr>
        				<th>내용</th>
        				<td><textarea id="content" rows="10" cols="70" style="height: 100px; border: none;" readonly>${requestScope.map.schedule_content}</textarea></td>
        			</tr>
        			<tr>
        				<th>일정등록자</th>
        				<td>${requestScope.map.name}</td>
        			</tr>
        		</table>
        		<div style="float: right;">
					<%-- <button type="button" id="register" class="border-1 rounded-md" style="margin-right: 10px;">등록</button>--%>
					<button type="button" class="buttonBorder" id="edit" style="margin-right: 10px; background-color: #99ccff;">수정</button>
					<button type="button" class="buttonBorder" id="delete" style="background-color: ecf0f8;">삭제</button> 
				</div>
        	</div>
        	<div id="showCal">
        		<jsp:include page="./calendar.jsp" />
        	</div>
        </div>
    </div>
</div>

<form name="goEditFrm">
	<input type="hidden" name="schedule_no" id="schedule_no" value="${requestScope.map.schedule_no}" />
	<input type="hidden" name="gobackURL_detailSchedule" value="${requestScope.gobackURL_detailSchedule}"/>
</form>

<%-- calendar 용 데이터 --%>
<input type="hidden" id="fk_emp_id" value="1000" />
<input type="hidden" id="name" value="이순신" />
<input type="hidden" id="dept_id" value="2" />

<%-- JS 활용 용도 --%>
<input type="hidden" id="path" 	value="${pageContext.request.contextPath}" />
<input type="hidden" id="schedule_name" value="${requestScope.map.schedule_name}" />

