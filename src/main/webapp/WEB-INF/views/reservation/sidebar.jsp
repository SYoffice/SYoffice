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
            
            <button type="button" class="reviewBtn" data-toggle="modal" data-target="#addReviewModal" data-value="${option.optionno}">자원 예약하기</button>

            <ul class="side_menu_list" id="side_menu">
                <li><a href="<%= ctxPath%>/reservation/meetingRoomReservation">회의실예약</a></li>
                <li><a href="#">차량예약</a></li>
                <li><a href="#">예약내역</a></li>
                <li><a href="#">결재대기문서함</a></li>
                <li>
                    <a href="#">관리자메뉴</a>
                    <ul>
                        <li><a href="#">예약내역</a></li>
                        <li><a href="#">자원관리</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
    <div class="contents_wrapper">
        <div class="contents_inner_wrapper">
            