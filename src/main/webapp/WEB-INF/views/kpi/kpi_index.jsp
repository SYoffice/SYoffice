<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="./sidebar.jsp" />


<%-- custom CSS --%>
<link href='<%= request.getContextPath() %>/css/kpi/kpi_index.css'rel='stylesheet' />
<%-- custom JS --%>
<script src='<%= request.getContextPath() %>/js/kpi/kpi_index.js'></script>


	<div class="contents_wrapper">
        <div class="contents_inner_wrapper">
        
        	<div style="margin: 4% 0 4% 0">
		    	<span class="h3">실적 통계</span>
	   		</div>
	   		
	   		<div id="filterContainer">
	   			<form name="filterFrm">
	   				<button type="button" id="excelDown">Excel 다운로드</button>
			   		<select class="filter" id="searchQuarter" name="searchQuarter">
			   			<option value="">분기선택</option>
			   			<option value="1">1분기</option>
			   			<option value="2">2분기</option>
			   			<option value="3">3분기</option>
			   			<option value="4">4분기</option>
			   		</select>
			   		<select class="filter" style="margin-right: 1%;" id="searchYear" name="searchYear">
			   		</select>
			   		<input type="hidden" name="fk_dept_id" value="${sessionScope.loginuser.fk_dept_id}" />
		   		</form>
	   		</div>
	   		
	   		
			<table id="kpi" class="table table-bordered">
				<c:if test="${empty requestScope.resultvoList}">
					<tr class="text-center">
						<th>등록된 실적이 없습니다.</th>
					</tr>
				</c:if>
				<c:if test="${!empty requestScope.resultvoList}">
					<c:forEach var="resultvo" items="${requestScope.resultvoList}" varStatus="status">
						<c:if test="${status.index == 0}">
							<tr>
								<th>소속</th>
								<td colspan="6">${resultvo.branch_name} - ${resultvo.dept_name}</td>
							</tr>
							<tr class="text-center">
								<th></th>
								<th>사원번호</th>
								<th>사원명</th>
								<th>직급</th>
								<th>실적명</th>
								<th>실적발생일</th>
								<th>실적액</th>
							</tr>
						</c:if>
						<tr class="text-center">
							<td></td>
							<td>${resultvo.fk_emp_id}</td>
							<td>${resultvo.name}</td>
							<td>${resultvo.grade_name}</td>
							<td>
								<c:if test="${fn:length(resultvo.result_name) > 30}">
									${fn:substring(resultvo.result_name, 0, 27)}${'...'}
								</c:if>
								<c:if test="${fn:length(resultvo.result_name) < 30}">
									${resultvo.result_name}
								</c:if>
							</td>
							<td>${resultvo.result_date}</td>
							<td><fmt:formatNumber pattern="#,###" value="${resultvo.result_price}"></fmt:formatNumber></td>
						</tr>
					</c:forEach>
				</c:if>
			</table>
	   		
		</div>
	</div>
</div>



<%-- JS 활용 용도 --%>
<input type="hidden" id="path" 		 value="${pageContext.request.contextPath}" />
<input type="hidden" id="fk_dept_id" value="${sessionScope.loginuser.fk_dept_id}" />