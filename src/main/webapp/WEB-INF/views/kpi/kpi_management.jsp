<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="./sidebar.jsp" />


<%-- custom CSS --%>
<link href='<%= request.getContextPath() %>/css/kpi/kpi_management.css'rel='stylesheet' />

<%-- custom JS --%>
<script src='<%= request.getContextPath() %>/js/kpi/kpi_management.js'></script>


	<div class="contents_wrapper">
        <div class="contents_inner_wrapper">
        	<div id="formBox">
        	
	        	<div style="margin: 4% 0 4% 0">
			    	<span class="h3">목표 관리</span>
		   		</div>
		   		<select id="searchYear" name="searchYear">
		   			<option value="">연도선택</option>
		   		</select>
				<table id="kpi" class="table table-bordered">
					<c:if test="${empty requestScope.deptKpiList}">
						<tr>
							<th>등록된 목표실적이 없습니다.</th>
						</tr>
					</c:if>
					<c:if test="${!empty requestScope.deptKpiList}">
						<c:forEach var="kpivo" items="${requestScope.deptKpiList}" varStatus="status">
							<c:if test="${status.index == 0}">
								<tr>
									<th>소속</th>
									<td colspan="6">${kpivo.branch_name} - ${kpivo.dept_name}</td>
								</tr>
								<tr class="text-center">
									<th></th>
									<th>연도 - 분기</th>
									<th>목표실적액(원)</th>
									<th>달성실적액(원)</th>
									<th>달성비율(%)</th>
									<th colspan="2">수정 / 삭제</th>
								</tr>
							</c:if>
							<tr class="text-center">
								<td><input type="hidden" id="fk_dept_id" value="${kpivo.fk_dept_id}"></td>
								<td>${kpivo.kpi_year} - ${kpivo.kpi_quarter}분기</td>
								<td><fmt:formatNumber pattern="#,###" value="${kpivo.kpi_index}"></fmt:formatNumber></td>
								<td><fmt:formatNumber pattern="#,###" value="${kpivo.sum_result_price}"></fmt:formatNumber></td>
								<td>${kpivo.result_pct}</td>
								<td class="text-center"><button type="button" class="buttonBorder" onclick="location.href='${pageContext.request.contextPath}/kpi/edit/${kpivo.kpi_no}'" style="margin-right: 10px; background-color: #99ccff;">수정</button></td>
								<td class="text-center"><button type="button" class="buttonBorder" onclick="kpiDelete('${kpivo.kpi_no}', '${kpivo.kpi_year}', '${kpivo.kpi_quarter}')" style="margin-right: 10px; background-color: #ffcc99;">삭제</button></td>
							</tr>
						</c:forEach>
					</c:if>
					
				</table>
			</div>
			
		</div>
	</div>
</div>



<%-- JS 활용 용도 --%>
<input type="hidden" id="path" 	value="${pageContext.request.contextPath}" />