<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String ctxPath = request.getContextPath();
%>
<jsp:include page="../sidebar.jsp" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/approval/apr_write_form.css">
<script src="${pageContext.request.contextPath}/js/approval/apr_write_form.js"></script>

<div class="common_title" style="text-align: center">근태 신청서</div>

<!-- 폼 이름 -->
<input type="hidden" id="form_name" value="leave">
<div class="container">
   <form  name="leave_form">
      <input type="hidden" name="apr_approver" id="apr_approver" value="">
      <input type="hidden" name="apr_approver2" id="apr_approver2" value="">
      <input type="hidden" name="apr_approver3" id="apr_approver3" value="">
      <input type="hidden" name="apr_important" id="apr_important" value="">
      <input type="hidden" name="leave_startdate" id="leave_startdate" value="">
      <input type="hidden" name="leave_enddate" id="leave_enddate" value="">
      <div class="btn-container">
         <button type="button" class="btn-submit" id="leave-submit">결재요청</button>
         <button type="button" class="btn-cancel" onclick="onClickCancel()">취소</button>
         <button type="button" onclick="openModal()">결재정보</button>
      </div>
      <!-- 모달 (select_employee_modal) -->
      <jsp:include page="../select_approver_sec.jsp" />

      <div class="contents-section">
         <input id="isImportant" name="isImportant" type="checkbox"/>긴급
         <!-- 상단 기안자 정보 및 서명 테이블 -->
         <div class="top-info">
            <div class="info-table">
               <table>
                  <tr>
                     <th>기안자</th>
                     <td>${sessionScope.loginuser.name}</td>
                  </tr>
                  <tr>
                     <th>기안부서</th>
                     <td>${sessionScope.loginuser.dept_name}</td>
                  </tr>
                  <tr>
                     <th>기안일</th>
                     <td></td>
                  </tr>
               </table>
            </div>

            <div class="signature-table">
               <table>
                  <tr id="signature-table-names">
                  </tr>
                  <tr id="signature-table-sec2">
                  </tr>
               </table>
            </div>
         </div>

         <!-- 종류, 상신자, 결재 내용 테이블 -->
         <table>
            <tr>
               <th>문서 번호</th>
               <td></td>
            </tr>
            <tr>
               <th>결재 종류</th>
               <td>근태 신청서</td>
            </tr>
            <tr>
               <th>결재 제목</th>
               <td><input type="text" name="leave_subject" required /></td>
            </tr>
            <tr>
               <th>종류</th>
               <td>
                  <label>
                      <input type="radio" name="leave_type" value="1" required>
                      연차
                  </label>
                  <label>
                      <input type="radio" name="leave_type" value="2" required>
                      반차
                  </label>
                  <label>
                      <input type="radio" name="leave_type" value="3" required>
                      보건
                  </label>
                  <label>
                      <input type="radio" name="leave_type" value="4" required>
                      경조
                  </label>
               </td>
            </tr>
               <tr>
                   <th>신청 일시</th>
                   <td>
                  <input
                      id="date"
                      type="date"
                      required
                  />
               </td>
               </tr>
            <tr>
               <th>내용</th>
               <td class="approval-contents"><textarea placeholder="" name="leave_content" id="approval-comments" style="height: 100%; resize: none"></textarea></td>
            </tr>

            <tr>
               <th>첨부 파일</th>
               <td><input type="file" id="attachment" name="attachment" /></td>
            </tr>
         </table>
      </div>
   </form>
</div>
</div>
</div>
</div>