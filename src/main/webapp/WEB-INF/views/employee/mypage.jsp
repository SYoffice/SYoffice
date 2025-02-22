<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>   
<%
	String ctxPath = request.getContextPath();
%>
<jsp:include page="../employee/sidebar.jsp" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/employee/mypage.css" />
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.1/font/bootstrap-icons.css">
<c:set var="loginUser" value="${sessionScope.loginuser}"/>

	<c:set var="orgTel" value="${loginUser.tel}"/>
	<c:set var="tel" value="${fn:substring(orgTel, 0, 3)}-${fn:substring(orgTel, 3, 7)}-${fn:substring(orgTel, 7, 11)}"/>
	
	<span class="h2"><i class="bi bi-person-fill"></i> ${loginUser.name}님의 정보</span>
	
	<div id="container">
		
	    <table border="1" class="table">
	        <tr>
	        	<c:if test="${not empty loginUser.profile_img}">
	            	<td rowspan="5" class="first_row" style="background-image: url('../resources/profile/${loginUser.profile_img}'); background-size: cover;"></td>
	            </c:if>
	            <c:if test="${empty loginUser.profile_img}">
	            	<td rowspan="5" class="first_row" style="background-image: url('../resources/profile/기본이미지.png'); background-size: cover;"></td>
	            </c:if>
	            <td style="font-weight: bold;" class="second_row">사원번호</td>
	            <td style="border: solid 1px black;">${loginUser.emp_id}</td>
	        </tr>
	        <tr>
	            <td class="second_row">성명</td>
	            <td>${loginUser.name}</td>
	        </tr>
	        <tr>
	            <td class="second_row">생년월일</td>
	            <td>${loginUser.birthday}</td>
	        </tr>
	        <tr>
	            <td class="second_row">성별</td>
	            <td>${loginUser.gender}자</td>
	        </tr>
	        <tr>
	            <td class="second_row">전화번호</td>
	            <td>${tel}</td>
	        </tr>
	        
	        <tr>
	            <td>개인 이메일</td>
	            <td colspan="2">${loginUser.personal_mail}</td>
	        </tr>
	        <tr>
	            <td>사내 이메일</td>
	            <td colspan="2">${loginUser.mail}</td>
	        </tr>
	        <tr>
	            <td>우편번호</td>
	            <td colspan="2">${loginUser.postcode}</td>
	        </tr>
	        <tr>
	            <td>주소</td>
	            <td colspan="2">${loginUser.address} ${loginUser.extraaddress} ${loginUser.detailaddress}</td>
	        </tr>
	        
	        <tr>
	            <td>소속 및 직급</td>
	            <td>${loginUser.branch_name}  ${loginUser.dept_name}</td>
	            <td>${loginUser.grade_name}</td>
	        </tr>
			
			<tr>
	            <td>상태 및 고용일자</td>
	            <td>
	            	<c:if test="${loginUser.status == 1}">
	            		재직
	            	</c:if>
	            	<c:if test="${loginUser.status == 2}">
	            		휴직
	            	</c:if>
	            	<c:if test="${loginUser.status == 3}">
	            		퇴직
	            	</c:if>
	            </td>
	            <td>${loginUser.hire_date}</td>
	        </tr>

	    </table>
	    
			
	</div>