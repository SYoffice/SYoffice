<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%
	String ctxPath = request.getContextPath();
%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/hr/hrIndex.css" />

<jsp:include page="../hr/sidebar.jsp" />

<script type="text/javascript">
$(document).ready(function() {
	
	// 엔터 누를 시 검색
	$("input:text[name='searchWord']").on("keydown", e => {
		if (e.keyCode == 13) {
			goSearch();
		}
	});// end of $("input:text[name='searchWord']").on("keydown", e => {}) -----	
	
	//검색 시 검색조건 및 검색어 값 유지시키기
	if (${!empty requestScope.paraMap}) {
		$("select[name='searchType']").val("${requestScope.paraMap.searchType}");
		$("input:text[name='searchWord']").val("${requestScope.paraMap.searchWord}");
	}
	
});// end of $(document).ready(function() {}) ------

function goSearch() {
	const frm = document.searchFrm;
	
	frm.method = "GET";
	frm.submit();
}// end of function goSearch() ----------------------

</script>


            	
			<span class="h2">사원목록</span>
			
			<form name="searchFrm" style="margin: 20px auto;">
				<select name="searchType" style="height: 26px;">
					<option value="branch_name">지점명</option>
					<option value="dept_name">부서명</option>
					<option value="grade_name">직급</option>
					<option value="name">사원명</option>
				</select>
				
				<input type="text" name="searchWord" size="50" autocomplete="off" /> 
				<input type="text" style="display: none;"/>  
				<button type="button" class="btn btn-secondary btn-sm" onclick="goSearch()">검색</button>
			</form>
			
			<table class="table table-bordered">
				<thead>
					<tr>
						<th style="width: 10%;  text-align: center;">사원번호</th>
						<th style="width: 10%;  text-align: center;">지점</th>
						<th style="width: 10%;  text-align: center;">부서</th>
						<th style="width: 15%; text-align: center;">직급</th>
						<th style="width: 15%;  text-align: center;">성명</th>
						<th style="width: 45%; text-align: center;">이메일</th>
					</tr>
				</thead>
				
				<tbody>
					<%-- 조회된 사원목록이 있을 경우 --%>
				    <c:if test="${not empty requestScope.employeeList}">
				    	<c:forEach var="employeevo" items="${requestScope.employeeList}">
							<tr>
								<td style="width: 10%;  text-align: center;">${employeevo.emp_id}</td>
								<td style="width: 10%;  text-align: center;">${employeevo.branch_name}</td>
								<td style="width: 10%;  text-align: center;">${employeevo.dept_name}</td>
								<td style="width: 15%;  text-align: center;">${employeevo.grade_name}</td>
								<td style="width: 15%;  text-align: center;">${employeevo.name}</td>
								<td style="width: 45%;  text-align: center;">${employeevo.mail}</td>
							</tr>
						</c:forEach>
				    </c:if>
				    <%-- 조회된 사원목록이 없을 경우 --%>
				    <c:if test="${empty requestScope.employeeList}">
						<tr>
							<td style="text-align: center;" colspan="6">데이터가 없습니다.</td>
						</tr>
				    </c:if>
				</tbody>
		    </table>
           	
           	 
			<%-- 페이지네이션  --%>
			<nav class="text-center">
				<ul class="pagination">
					<!-- 첫 페이지  -->
					<div class="pageBtn_box">
						<li><a href="hrIndex?curPage=1" data-page="1"><span aria-hidden="true"><img class="pageBtn" src="${pageContext.request.contextPath}/images/icon/go_first.svg" /></span></a></li>
						<!-- 이전 페이지 -->
						<c:if test="${pagingDTO.firstPage ne 1}">
							<li><a href="hrIndex?curPage=${i}?curPage=${pagingDTO.firstPage-1}" data-page="${pagingDTO.firstPage-1}"><span aria-hidden="true"><img class="pageBtn" src="${pageContext.request.contextPath}/images/icon/prev.svg" /></span></a></li>
						</c:if>
					</div>
					
					<div id="pageNo_box">
						<!-- 페이지 넘버링  -->
						<c:forEach begin="${pagingDTO.firstPage}" end="${pagingDTO.lastPage}" var="i" >
							
							<c:if test="${pagingDTO.curPage ne i}">
								<li><a class="pageNo"  href="hrIndex?curPage=${i}" data-page="${i}">${i}</a></li>
							</c:if>
							
							<c:if test="${pagingDTO.curPage eq i}">
								<li class="active"><a class="pageNo active" href="#">${i}</a></li>
							</c:if>
							
						</c:forEach>
					</div>
					
					<!-- 다음  페이지  -->
					<div class="pageBtn_box">
						<c:if test="${pagingDTO.lastPage ne pagingDTO.totalPageCount}">
							<li><a href="hrIndex?curPage=${pagingDTO.lastPage+1}" data-page="${pagingDTO.lastPage+1}"><span aria-hidden="true"><img class="pageBtn" src="${pageContext.request.contextPath}/images/icon/next.svg" /></span></a></li>
						</c:if>
						
						<!-- 마지막 페이지 -->
						<li><a href="hrIndex?curPage=${pagingDTO.totalPageCount}" data-page="${pagingDTO.totalPageCount}"><span aria-hidden="true"><img class="pageBtn" src="${pageContext.request.contextPath}/images/icon/go_last.svg" /></span></a></li>
					</div>
				</ul>
			</nav>
			<%-- 페이지네이션 --%>
            	
			</div>
        </div>
    </div>