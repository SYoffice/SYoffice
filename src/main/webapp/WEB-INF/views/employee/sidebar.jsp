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
            <span class="common_title">내 정보</span>
			<button type="button" onclick="location.href='pwdChange'">비밀번호 변경</button>
			<ul class="side_menu_list">
				<li><a href="<%= ctxPath%>/employee/mypage">내정보 보기</a></li>
			</ul>
        </div>
    </div>
    <div class="contents_wrapper">
        <div class="contents_inner_wrapper">