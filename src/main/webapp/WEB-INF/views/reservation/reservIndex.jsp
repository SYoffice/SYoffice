<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String ctxPath = request.getContextPath();
%>
<!-- 예약 페이지 사이드바 -->
<jsp:include page="./sidebar.jsp" />

<script type="text/javascript" src="https://unpkg.com/vis-timeline@latest/standalone/umd/vis-timeline-graph2d.min.js"></script>
<link href="https://unpkg.com/vis-timeline@latest/styles/vis-timeline-graph2d.min.css" rel="stylesheet" type="text/css" />
<%-- <link href="${pageContext.request.contextPath}/css/reservation/reservation_modal.css" rel="stylesheet" type="text/css" /> --%>
<link href="${pageContext.request.contextPath}/css/reservation/reservation_main.css" rel="stylesheet" type="text/css" />


<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/hr/hrIndex.css" />
<c:set var="href" value="reservIndex?" />
<c:set var="href" value="${href}" scope="request" />

<!-- 현재 메뉴의 카테고리 값 (회의실 혹은 차량) -->
<input type="hidden" id="category_no" value="${category_no}" />
<!-- 로그인 아이디 -->
<input type="hidden" id="login_user_id" value="${sessionScope.loginuser.emp_id}" />

<div class="common_title mb-80">
   <c:if test="${category_no == 1}">
       회의실
   </c:if>
   <c:if test="${category_no != 1}">
       차량
   </c:if>
   예약
</div>
<div class="resourceReservation_wrapper">
   <div class="timeline-header">
      <button class="prev-button" onclick="prevDate()"><</button>
      <div id="today-text" class="common_title"></div>
      <button class="next-button" onclick="nextDate()">></button>
      <button onclick="setToday()">오늘</button>
   </div>
   <span class="timeline-noti">* 예약을 추가/수정하려면 더블클릭 하세요.</span>

   <!-- vis 라이브러리 타임라인 ui 영역 -->
   <div id="timeline"></div>
   
   <jsp:include page="./reservationModal.jsp" />
   <script src="${pageContext.request.contextPath}/js/reservation/reservIndex.js"></script>
</div>

<div id="reservation-table">
   <div style="display: flex; justify-content: space-between; align-items: center">
      <div class="common_title">예약/대여 현황</div>
      <div style="display: flex; justify-content: end"></div>
   </div>
   <table>
      <tr>
         <th>예약 번호</th>
         <th>예약자 이름</th>
         <th>시작 시간</th>
         <th>종료 시간</th>
         <th>자원 명</th>
         <th>취소/반납</th>
      </tr>
      <tbody id="reservation-tbody">
      </tbody>
   </table>
</div>
<%-- 페이지네이션  --%>
<style>
   a {
      cursor: pointer;
   }
</style>
<nav class="text-center">
   <ul class="pagination">
      <!-- 첫 페이지  -->
      <div class="pageBtn_box">
         <li><a onclick="getReservationPageList(1)" data-page="1"><span aria-hidden="true"><img class="pageBtn" src="${pageContext.request.contextPath}/images/icon/go_first.svg" /></span></a></li>
         <!-- 이전 페이지 -->
         <c:if test="${pagingDTO.firstPage ne 1}">
            <li><a onclick="getReservationPageList(${pagingDTO.firstPage-1})" data-page="${pagingDTO.firstPage-1}"><span aria-hidden="true"><img class="pageBtn" src="${pageContext.request.contextPath}/images/icon/prev.svg" /></span></a></li>
            
         </c:if>
      </div>
      
      <div id="pageNo_box">
         <!-- 페이지 넘버링  -->
         <c:forEach begin="${pagingDTO.firstPage}" end="${pagingDTO.lastPage}" var="i" >
            <li><a class="pageNo" id="pageNoIdx${i}" onclick="getReservationPageList(${i})" data-page="${i}">${i}</a></li>
         </c:forEach>
      </div>
      
      <!-- 다음  페이지  -->
      <div class="pageBtn_box">
         <c:if test="${pagingDTO.lastPage ne pagingDTO.totalPageCount}">
            <li><a onclick="getReservationPageList(${pagingDTO.lastPage+1})" data-page="${pagingDTO.lastPage+1}"><span aria-hidden="true"><img class="pageBtn" src="${pageContext.request.contextPath}/images/icon/next.svg" /></span></a></li>
         </c:if>
         
         <!-- 마지막 페이지 -->
         <li><a onclick="getReservationPageList(${pagingDTO.totalPageCount})" data-page="${pagingDTO.totalPageCount}"><span aria-hidden="true"><img class="pageBtn" src="${pageContext.request.contextPath}/images/icon/go_last.svg" /></span></a></li>
      </div>
   </ul>
</nav>
<%-- 페이지네이션 --%>
</div>
</div>
</div>