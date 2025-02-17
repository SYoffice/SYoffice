<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String ctxPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" type="text/css" href="<%=ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css" >
   
    <!-- header CSS -->
    <link rel="stylesheet" type="text/css" href="<%=ctxPath%>/css/header/header.css" >
    <link rel='stylesheet' href='https://cdn-uicons.flaticon.com/2.6.0/uicons-solid-rounded/css/uicons-solid-rounded.css'>    
    <link rel='stylesheet' href='https://cdn-uicons.flaticon.com/2.6.0/uicons-thin-straight/css/uicons-thin-straight.css'>
  
    
    <!-- Font Awesome 6 Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

    <!-- Optional JavaScript -->
    <script type="text/javascript" src="<%=ctxPath%>/js/jquery-3.7.1.min.js"></script>
    <script type="text/javascript" src="<%=ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" src="<%=ctxPath%>/smarteditor/js/HuskyEZCreator.js" charset="utf-8"></script> 

    <!-- jQueryUI (스피너, datepicker 등) -->
    <link rel="stylesheet" type="text/css" href="<%=ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.min.css" />
    <script type="text/javascript" src="<%=ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.min.js"></script>

    <!-- ajaxForm 라이브러리 -->
    <script type="text/javascript" src="<%=ctxPath%>/js/jquery.form.min.js"></script> 
    
    <%-- sweat alert --%>
	<link href="https://cdn.jsdelivr.net/npm/sweetalert2@11.16.1/dist/sweetalert2.min.css" rel="stylesheet">
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.16.1/dist/sweetalert2.all.min.js"></script>
 
</head>
<body>
    
    <nav class="navbar navbar-expand-lg navbar-light fixed-top w-100 py-3">
        <a class="navbar-brand" href="<%=ctxPath%>/index">로고</a>
        <div class="collapse navbar-collapse" id="collapsibleNavbar">
            <ul class="navbar-nav">
                <li class="nav-item active">
                    <a class="nav-link" href="<%=ctxPath%>/index">홈</a>    
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="<%=ctxPath%>/attendance">근태관리</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="#">문서함</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="#">메일함</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="#">캘린더</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="#">게시판</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="#">조직도</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="#">예약</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="#">전자결재</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="#">메신저</a>
                </li>
            </ul>
            <ul class="navbar-nav ml-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="#"><img src="<%= ctxPath %>/css/header/heder.img/schedule.png" style="width:30px;"/>로그아웃</a>
                </li>
            </ul>
        </div>
    </nav>
</body>
</html>
    