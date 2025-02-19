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
			<ul class="side_menu_list">
				<li><a href="<%= ctxPath%>/hr/hrIndex">홈</a></li>
				<li><a href="#">부서관리</a></li>
				<li><a href="#">자원관리</a></li>
			</ul>
        </div>
    </div>
    <div class="contents_wrapper">
        <div class="contents_inner_wrapper">