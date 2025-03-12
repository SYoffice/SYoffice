<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="java.net.InetAddress" %>


<%
    String ctxPath = request.getContextPath();

%>

<%   
    // === (#웹채팅관련2) === 
    // === 서버 IP 주소 알아오기(사용중인 IP주소가 유동IP 이라면 IP주소를 알아와야 한다.) === 
    
 //   InetAddress inet = InetAddress.getLocalHost();
 //   String serverIP = inet.getHostAddress();
     
 //   System.out.println("serverIP : " + serverIP);
 // serverIP : 192.168.0.203

 // String serverIP = "192.168.0.219";
    String serverIP = "146.56.40.110"; 
    // 자신의 EC2 퍼블릭 IPv4 주소임. // 아마존(AWS)에 배포를 하기 위한 것임. 
    // 만약에 사용중인 IP주소가 고정IP 이라면 IP주소를 직접입력해주면 된다. 
 
    // === 서버 포트번호 알아오기 === //
    int portnumber = request.getServerPort();
 // System.out.println("portnumber : " + portnumber);
 // portnumber : 9090
 
    String serverName = "http://"+serverIP+":"+portnumber;
 // System.out.println("serverName : " + serverName);
 // serverName : http://192.168.0.203:9090

%>


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>SYOFFICE</title>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" type="text/css" href="<%=ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css">
   
    <!-- header CSS -->
    <link rel="stylesheet" type="text/css" href="<%=ctxPath%>/css/header/header.css">
    <link rel='stylesheet' href='https://cdn-uicons.flaticon.com/2.6.0/uicons-solid-rounded/css/uicons-solid-rounded.css'>    
    <link rel='stylesheet' href='https://cdn-uicons.flaticon.com/2.6.0/uicons-thin-straight/css/uicons-thin-straight.css'>
    <link rel='stylesheet' href='https://cdn-uicons.flaticon.com/2.6.0/uicons-bold-straight/css/uicons-bold-straight.css'>

    <!-- sidebar common CSS -->
    <link rel="stylesheet" href="<%= ctxPath%>/css/common/common.css">
    
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
    
    <!-- sweet alert -->
    <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11.16.1/dist/sweetalert2.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.16.1/dist/sweetalert2.all.min.js"></script>

    <script type="text/javascript">
        window.onload = function() {
            const sideMenu = document.querySelectorAll("ul#side_menu li");

            sideMenu.forEach(function(item) {
                const link = item.querySelector("a");

                // 현재 페이지 URL과 href가 일치하는지 확인
                 if (window.location.pathname + window.location.search === link.getAttribute("href")) {


                    item.classList.add('active');
                }

                link.addEventListener('click', function() {
                    sideMenu.forEach(function(e) {
                        e.classList.remove('active');
                    });

                    item.classList.add('active');
                });
            });
        }
    </script>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-light w-100 py-3">
        <a class="navbar-brand" href="<%=ctxPath%>/index">로고</a>
        <div class="collapse navbar-collapse" id="collapsibleNavbar">
            <ul class="navbar-nav">
                <li class="nav-item active">
                    <a class="nav-link" href="<%=ctxPath%>/index">홈</a>    
                </li>
                <c:choose>
                    <c:when test="${sessionScope.loginuser.fk_dept_id eq 2}">
                        <li class="nav-item active">
                            <a class="nav-link" href="<%=ctxPath%>/attendance/manager">근태관리</a> 
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link" href="<%=ctxPath%>/attendance">근태관리</a> 
                        </li>
                    </c:otherwise>
                </c:choose>
                
                 <li class="nav-item active">
                    <a class="nav-link" href="<%=ctxPath%>/dataroom/index">자료실</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="<%=ctxPath%>/mail/box/0">메일함</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="<%=ctxPath%>/schedule/scheduleIndex">캘린더</a>
                </li>
                <li class="nav-item active">  
                    <a class="nav-link" href="<%= ctxPath%>/board/GroupWare_noticeBoard?boardLocation=notice">게시판</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="<%=ctxPath%>/organization/chart">조직도</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="<%=ctxPath%>/reservation/reservIndex">예약</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="<%=ctxPath%>/approval/approval_main">전자결재</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="<%=ctxPath%>/chatting/index">메신저</a>
                </li>            
                <c:if test="${sessionScope.loginuser.emp_id eq sessionScope.loginuser.manager_id}">
                    <li class="nav-item active">
                        <a class="nav-link" href="<%=ctxPath%>/kpi/index">실적관리</a>
                    </li>
                </c:if>
                <c:if test="${sessionScope.loginuser.fk_dept_id eq 2}">
                    <li class="nav-item active">
                        <a class="nav-link" href="<%=ctxPath%>/hr/hrIndex">인사관리</a>
                    </li>
                </c:if>
            </ul>
            <ul class="navbar-nav ml-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="<%=ctxPath%>/employee/mypage">${loginuser.name}</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link"  href="<%=ctxPath%>/logout"><i class="fi fi-bs-sign-out-alt"></i></a>
                </li>
            </ul>
        </div>
    </nav>
</body>
</html>
