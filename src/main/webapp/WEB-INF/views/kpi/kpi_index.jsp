<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="./sidebar.jsp" />

	<div class="contents_wrapper">
        <div class="contents_inner_wrapper">
        
        	<div style="margin: 4% 0 4% 0">
		    	<span class="h3">목표 관리</span>
	   		</div>
	   		
	   		<select id="searchYear" name="searchYear">
	   			<option value="">연도선택</option>
	   		</select>
	   		<select id="searchYear" name="searchYear">
	   			<option value="">분기선택</option>
	   		</select>
	   		
			<table id="kpi" class="table table-bordered">
				<c:if test="${empty requestScope.resultvoList}">
					<tr>
						<th>등록된 목표실적이 없습니다.</th>
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
								<th>직급</th>
								<%-- 사원번호 --%>
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