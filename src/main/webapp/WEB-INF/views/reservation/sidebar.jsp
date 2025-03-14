<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%
   String ctxPath = request.getContextPath();
   //     /syoffice 
%>      
<jsp:include page="../main/header.jsp" />

<link rel="stylesheet" href="<%= ctxPath%>/css/reservation/reservation.css">

<div class="common_wrapper">
   <div class="side_menu_wrapper">
        <div class="side_menu_inner_wrapper">
            <span class="common_title">자원예약</span>

            <ul class="side_menu_list" id="side_menu">
                <li><a href="<%= ctxPath%>/reservation/reservIndex">회의실예약</a></li>
                <li><a href="<%= ctxPath%>/reservation/reservIndex?category_no=2">차량예약</a></li>
                <li><a href="<%= ctxPath%>/reservation/reservationList">예약내역</a></li>
            </ul>
        </div>
    </div>
    <div class="contents_wrapper">
        <div class="contents_inner_wrapper">
            