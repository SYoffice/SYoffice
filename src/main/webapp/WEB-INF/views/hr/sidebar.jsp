<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%
   String ctxPath = request.getContextPath();
   //     /syoffice 
%>      
<jsp:include page="../main/header.jsp" />
<div class="common_wrapper">
    <div class="side_menu_wrapper">
        <div class="side_menu_inner_wrapper">
            <span class="common_title">인사관리</span>
			<button type="button" onclick="location.href='employeeRegister'">신규 사원등록</button>
			<button type="button">신규 부서등록</button>
			<button type="button">신규 자원등록</button>
			<ul class="side_menu_list">
				<li><a href="<%= ctxPath%>/hr/hrIndex">홈</a></li>
				<li><a href="#">결재대기문서함</a></li>
				<li><a href="#">참조문서함</a></li>
				<li><a href="#">임시저장함</a></li>
				<li><a href="#">기안문서함</a></li>
				<li><a href="#">팀문서함</a></li>
				<li><a href="#">환경설정</a></li>
			</ul>
        </div>
    </div>
    <div class="contents_wrapper">
        <div class="contents_inner_wrapper">