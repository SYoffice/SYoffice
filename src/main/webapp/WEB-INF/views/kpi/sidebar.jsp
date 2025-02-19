<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
   String ctxPath = request.getContextPath();
   //     /syoffice 
%>
<jsp:include page="../main/header.jsp" />

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">


<div class="common_wrapper">
    <div class="side_menu_wrapper">
        <div class="side_menu_inner_wrapper">
            <span class="common_title">실적 관리</span>
            <button type="button" onclick="location.href='${pageContext.request.contextPath}/kpi/register'">목표 등록</button>
            <ul class="side_menu_list" id="side_menu">
                <li><a href="#">실적 등록</a></li>
                <li><a href="#">실적 통계</a></li>
                <li><a href="${pageContext.request.contextPath}/kpi/management/${sessionScope.loginuser.fk_dept_id}">목표 관리</a></li>
            </ul>
        </div>
    </div>