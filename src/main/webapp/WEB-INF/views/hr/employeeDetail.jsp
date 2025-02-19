<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
   
<%
	String ctxPath = request.getContextPath();
%>
<jsp:include page="../hr/sidebar.jsp" />
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.1/font/bootstrap-icons.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/hr/employeeDetail.css" />
<script src='<%= request.getContextPath() %>/js/hr/employeeDetail.js'></script>
			 
			<c:set var="orgTel" value="${requestScope.employeevo.tel}"/>
			<c:set var="tel" value="${fn:substring(orgTel, 0, 3)}-${fn:substring(orgTel, 3, 7)}-${fn:substring(orgTel, 7, 11)}"/>
			
			<span class="h2"><i class="bi bi-person-fill"></i> ${requestScope.employeevo.name}님의 정보</span>
			
			<div id="container">
				
			    <table border="1" class="table">
			        <tr>
			        	<c:if test="${not empty requestScope.employeevo.profile_img}">
			            	<td rowspan="5" class="first_row" style="background-image: url('../resources/profile/${requestScope.employeevo.profile_img}'); background-size: cover;"></td>
			            </c:if>
			            <c:if test="${empty requestScope.employeevo.profile_img}">
			            	<td rowspan="5" class="first_row" style="background-image: url('../resources/profile/기본이미지.png'); background-size: cover;"></td>
			            </c:if>
			            <td class="second_row">사원번호</td>
			            <td style="border: solid 1px black;">${requestScope.employeevo.emp_id}</td>
			        </tr>
			        <tr>
			            <td class="second_row">성명</td>
			            <td>${requestScope.employeevo.name}</td>
			        </tr>
			        <tr>
			            <td class="second_row">생년월일</td>
			            <td>${requestScope.employeevo.birthday}</td>
			        </tr>
			        <tr>
			            <td class="second_row">성별</td>
			            <td>${requestScope.employeevo.gender}자</td>
			        </tr>
			        <tr>
			            <td class="second_row">전화번호</td>
			            <td>${tel}</td>
			        </tr>
			        
			        <tr>
			            <td>개인 이메일</td>
			            <td colspan="2">${requestScope.employeevo.personal_mail}</td>
			        </tr>
			        <tr>
			            <td>사내 이메일</td>
			            <td colspan="2">${requestScope.employeevo.mail}</td>
			        </tr>
			        <tr>
			            <td>우편번호</td>
			            <td colspan="2">${requestScope.employeevo.postcode}</td>
			        </tr>
			        <tr>
			            <td>주소</td>
			            <td colspan="2">${requestScope.employeevo.address} ${requestScope.employeevo.extraaddress} ${requestScope.employeevo.detailaddress}</td>
			        </tr>
			        
			        <tr>
			            <td>소속 및 직급</td>
			            <td>${requestScope.employeevo.branch_name}  ${requestScope.employeevo.dept_name}</td>
			            <td>${requestScope.employeevo.grade_name}</td>
			        </tr>
					
					<tr>
			            <td>상태 및 고용일자</td>
			            <td>
			            	<c:if test="${requestScope.employeevo.status == 1}">
			            		재직
			            	</c:if>
			            	<c:if test="${requestScope.employeevo.status == 2}">
			            		휴직
			            	</c:if>
			            	<c:if test="${requestScope.employeevo.status == 3}">
			            		퇴직
			            	</c:if>
			            </td>
			            <td>${requestScope.employeevo.hire_date}</td>
			        </tr>

			    </table>
			    
					
			</div>	
					
			<div class="btnBox">
				<button class="goBackBtn" type="button" id="goBack" onclick="history.back()">뒤로가기</button>
				<button class="EditBtn" type="button" id="employee_edit_button" onclick="goEdit()">수정하기</button>
			</div>
					
					
				</div>
		
			
		</div>
	</div>
</div>