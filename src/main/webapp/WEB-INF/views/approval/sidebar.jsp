<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String ctxPath = request.getContextPath();
//     /syoffice
%>
<jsp:include page="../main/header.jsp" />
<jsp:include page="./select_form_modal.jsp" /> 

<link rel="stylesheet" href="<%=ctxPath%>/css/approval/approval_main.css">
<%-- <script src="${pageContext.request.contextPath}/js/approval/common.js"></script> --%>
<!-- Flatpickr CSS -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">

<!-- Flatpickr JavaScript -->
<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
<script src="https://npmcdn.com/flatpickr/dist/l10n/ko.js"></script>

<div class="common_wrapper">
   <div class="side_menu_wrapper">
      <div class="side_menu_inner_wrapper">
         <span class="common_title">전자결재</span>
         <button type="button" data-toggle="modal" data-target="#selectAprFormModal">기안문서 작성</button>
         <ul class="side_menu_list" id="side_menu">
            <li><a href="<%=ctxPath%>/approval/approval_main">홈</a></li>
            <li><a href="<%=ctxPath%>/approval/obtain_approval_box">결재대기문서함</a></li>
            <!-- <li><a href="#">참조문서함</a></li> -->
            <!-- <li><a href="#">임시저장함</a></li> -->
            <li><a href="<%=ctxPath%>/approval/my_approval_box">기안문서함</a></li>
            <li><a href="<%=ctxPath%>/approval/team_approval_box">팀문서함</a></li>
            <li><a href="<%=ctxPath%>/approval/approval_setting">환경설정</a></li>
         </ul>
      </div>
   </div>
   <div class="contents_wrapper">
      <div class="contents_inner_wrapper">