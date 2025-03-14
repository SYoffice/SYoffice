<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/hr/hrIndex.css" />


<script src="${pageContext.request.contextPath}/js/reservation/common.js"></script>
<c:set var="href" value="reservationList?" />
<c:set var="href" value="${href}" scope="request" />

<div class="common_title mb-80">내 예약/대여 현황</div>

<input type="hidden" id="login_user_id" value="${sessionScope.loginuser.emp_id}" />
<div id="reservation-table">
   <table>
      <tr>
         <th>예약 번호</th>
         <th>예약자 이름</th>
         <th>시작 시간</th>
         <th>종료 시간</th>
         <th>정보</th>
         <th>취소/반납</th>
      </tr>
      <tbody id="reservation-tbody">
         <c:if test="${reservationList.size() > 0}">
            <c:forEach var="reservation" items="${reservationList}">
               <tr>
                  <td>${reservation.reserv_no}</td>
                  <td>${reservation.empl_name}</td>
                  <td>${reservation.reserv_start}</td>
                  <td>${reservation.reserv_end}</td>
                  <td>${reservation.resource_name}</td>
                  <c:if test="${sessionScope.loginuser.emp_id == reservation.fk_emp_id && !reservation.isPassed}">
                     <td>
                        <c:if test="${reservation.posibleCancel}">
                           <button onclick="handleReturnItem(${reservation.reserv_no})">반납</button>
                        </c:if> 
                        <c:if test="${!reservation.posibleCancel}">
                           <button onclick="handleRemoveItem(${reservation.reserv_no})">취소</button>
                        </c:if>
                     </td>
                  </c:if>
                  <c:if test="${sessionScope.loginuser.emp_id != reservation.fk_emp_id || reservation.isPassed}">
                     <td> 사용 완료 </td>
                  </c:if>
               </tr>
            </c:forEach>
         </c:if>
         <c:if test="${reservationList.size() == 0}">
            <tr class="empty-table">
               <td colspan="6">예약 데이터가 없습니다.</td>
            </tr>
         </c:if>
      </tbody>
   </table>
</div>
<jsp:include page="./reserv_pagination.jsp" />
</div>
</div>
</div>