<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String ctxPath = request.getContextPath();
%>
<!-- 예약 페이지 사이드바 -->
<jsp:include page="./sidebar.jsp" />

<script type="text/javascript" src="https://unpkg.com/vis-timeline@latest/standalone/umd/vis-timeline-graph2d.min.js"></script>
<link href="https://unpkg.com/vis-timeline@latest/styles/vis-timeline-graph2d.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/reservation/reservation_modal.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/reservation/reservation_main.css" rel="stylesheet" type="text/css" />


<div class="common_title mb-80">회의실예약</div>
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

   <!-- 모달 - jsp에서는 import 예정 -->
   <div id="myModal" class="modal">
      <div class="modal-content">
         <h3>회의실 예약</h3>
         <form id="reservationForm">
            <input type="hidden" id="id" name="id" /> <input type="hidden"
               id="group" name="group" required />
            <div class="form-group">
               <label for="resource_name">회의실 명</label> <input disabled
                  type="text" id="resource_name" name="resource_name" required />
            </div>
            <div class="form-group">
               <label for="content">예약 명</label> <input type="text" id="content"
                  name="content" required />
            </div>
            <div class="form-group">
               <!-- 예약 시작 -->
               <label for="start-time">예약 시작 <span
                  class="input-group-addon" id="start-date">2025-02-20</span></label>
               <!-- 날짜 표시 -->
               <input type="time" class="form-control" id="start-time"
                  placeholder="시작 시간" required />
            </div>
            <div class="form-group">
               <!-- 예약 종료 -->
               <label for="end-time">예약 종료 <span class="input-group-addon"
                  id="end-date">2025-02-20</span></label>

               <!-- 날짜 표시 -->
               <input type="time" class="form-control" id="end-time"
                  placeholder="종료 시간" required />
            </div>
            <div class="form-group">
               <button type="submit">예약하기</button>
            </div>
         </form>
      </div>
   </div>

   <script src="${pageContext.request.contextPath}/js/reservation/reservation.js"></script>
</div>

<div id="reservation-table">
   <div
      style="display: flex; justify-content: space-between; align-items: center">
      <div class="common_title">내 예약/대여 현황</div>
      <div style="display: flex; justify-content: end"></div>
   </div>
   <table>
      <tr>
         <th>예약 번호</th>
         <th>예약자 이름</th>
         <th>예약 설명</th>
         <th>시작 시간</th>
         <th>종료 시간</th>
         <th>회의실</th>
         <th>취소/반납</th>
      </tr>
      <tbody id="reservation-tbody"></tbody>
   </table>
</div>
</div>
</div>
</div>