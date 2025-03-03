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

<style type="text/css">
:root{
	font-family: "Noto Sans", serif;
	 font-optical-sizing: auto;
}
</style>


<div class="common_wrapper">
    <div class="side_menu_wrapper">
        <div class="side_menu_inner_wrapper">
            <span class="common_title">메일함</span>
            <button type="button" onclick="location.href='${pageContext.request.contextPath}/mail/write'">메일 쓰기</button>
            <ul class="side_menu_list" id="side_menu">
                <li><a href="${pageContext.request.contextPath}/mail/box/-1"><i class="fa-regular fa-envelope"></i><span style="margin-left: 3%;">전체메일함</span></a></li>
                <li><a href="${pageContext.request.contextPath}/mail/box/0"><i class="fa-solid fa-inbox"></i><span style="margin-left: 3%;">받은메일함</span></a></li>
                <li><a href="${pageContext.request.contextPath}/mail/box/1"><i class="fa-regular fa-paper-plane"></i><span style="margin-left: 3%;">보낸메일함</span></a></li>
                <li><a href="${pageContext.request.contextPath}/mail/box/2"><i class="fa-regular fa-file"></i><span style="margin-left: 4.7%;">임시보관함</span></a></li>
                <li><a href="${pageContext.request.contextPath}/mail/box/3"><i class="fa-regular fa-file-lines"></i><span style="margin-left: 4.7%;">내게쓴메일함</span></a></li>
            </ul>
            <ul class="side_menu_list">
            	<li><a href="${pageContext.request.contextPath}/mail/box/4"><i class="fa-regular fa-trash-can"></i><span style="margin-left: 5%;">휴지통</span></a></li>
            	<li><a href="${pageContext.request.contextPath}/mail/box/5"><i class="fa-solid fa-ban"></i><span style="margin-left: 4%;">스팸메일함</span></a></li>
            </ul>
            
            <ul class="side_menu_list">
            	<li><a href="${pageContext.request.contextPath}/mail/setting"><i class="fa-solid fa-gear"></i><span style="margin-left: 3%;">환경설정</span></a></li>
            </ul>
        </div>
    </div>