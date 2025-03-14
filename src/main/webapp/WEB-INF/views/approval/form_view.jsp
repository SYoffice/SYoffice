<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String ctxPath = request.getContextPath();
%>
<jsp:include page="./sidebar.jsp" />
<c:set var="loginId" value="${sessionScope.loginuser.emp_id}" />
<c:set var="aprvo" value="${requestScope.aprvo}" />
<c:set var="isApprover" value="${aprvo.apr_approver == loginId || aprvo.apr_approver2 == loginId || aprvo.apr_approver3 == loginId}" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/approval/apr_write_form.css">
  <link rel="stylesheet" type="text/css" href="<%=ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css" >
<script type="text/javascript" src="<%=ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js" ></script>
<script src="${pageContext.request.contextPath}/js/approval/form_view.js"></script>
<div class="common_title" style="text-align: center">기안 문서
   <c:if test="${aprvo.apr_important == '1'}">
      <span style="background-color: red">긴급</span>
   </c:if>
</div>

<input type="hidden" id="form_type" value="${aprvo.type}">
<input type="hidden" id="draft_no" value="${aprvo.fk_draft_no}">
<input type="hidden" id="leave_no" value="${aprvo.fk_leave_no}">
<jsp:include page="./reject_approval.jsp" />
<div class="container">
   <form name="view_form">
      <div class="btn-container">

         <c:if test="${aprvo.fk_emp_id != loginId && isApprover && aprvo.apr_status != '5'}">
         
            <c:choose>  
               <c:when test="${(aprvo.apr_approver == loginId && not empty aprvo.apr_acceptday1) || 
                           (aprvo.apr_approver2 == loginId && not empty aprvo.apr_acceptday2) || 
                           (aprvo.apr_approver3 == loginId && not empty aprvo.apr_acceptday3)}">
               </c:when> 
               <c:otherwise>
                  <button type="button" class="btn-submit" id="draft-submit" onclick="acceptApr(${aprvo.apr_no})">결재 승인</button>
                  <button type="button" class="btn-cancel" id="reject-button" data-toggle="modal" data-target="#rejectModal" data-value="${aprvo.apr_no}">반려</button>
               </c:otherwise> 
            </c:choose>
         </c:if>
         <c:if test="${aprvo.apr_status == '1' && aprvo.fk_emp_id == loginId}">
            <button type="button" class="btn-submit" id="reject-button" onclick="cancelApr(${aprvo.apr_no})">기안 취소</button>
         </c:if>
      </div>

      <div class="contents-section">
         <!-- 상단 기안자 정보 및 서명 테이블 -->
         <div class="top-info">
            <div class="info-table">
               <table>
                  <tr>
                     <th>기안자</th>
                     <td>${aprvo.name}</td>
                  </tr>
                  <tr>
                     <th>기안부서</th>
                     <td>${aprvo.dept_name}</td>
                  </tr>
                  <tr>
                     <th>기안일</th>
                     <td>${aprvo.apr_startdate}</td>
                  </tr>
               </table>
            </div>

            <div class="signature-table">
               <table>
                  <tr id="signature-table-names">
                     <th rowspan="3" style="width: 10px">결재</th>
                     <td>${aprvo.apr_approver_name}</td>
                     <c:if test="${not empty aprvo.apr_approver2_name}">
                        <th rowspan="3" style="width: 10px">결재</th>
                        <td>${aprvo.apr_approver2_name}</td>
                     </c:if>
                     <c:if test="${not empty aprvo.apr_approver3_name}">
                        <th rowspan="3" style="width: 10px">결재</th>
                        <td>${aprvo.apr_approver3_name}</td>
                     </c:if>
                  </tr>
                  <tr id="signature-table-sec2">
                     <td>
                        <c:if test="${not empty aprvo.apr_acceptday1}">
                           승인
                        </c:if>
                     </td>
                     <c:if test="${not empty aprvo.apr_approver2_name}">
                        <td>
                           <c:if test="${not empty aprvo.apr_acceptday2}">
                              승인
                           </c:if>
                        </td>
                     </c:if>
                     <c:if test="${not empty aprvo.apr_approver3_name}">
                        <td>
                           <c:if test="${not empty aprvo.apr_acceptday3}">
                              승인
                           </c:if>
                        </td>
                     </c:if>
                  </tr>
               </table>
            </div>
         </div>

         <!-- 종류, 상신자, 결재 내용 테이블 -->
         <table>
            <tr>
               <th>문서 번호</th>
               <td>${aprvo.apr_no}</td>
            </tr>
            <tr>
               <th>결재 종류</th>
               <td>${aprvo.typename}</td>
            </tr>
            <tr>
               <th>결재 제목</th>
               <td>
                  <c:if test="${aprvo.type == 1}">
                     ${aprvo.draft_subject}
                  </c:if>
                  <c:if test="${aprvo.type == 3}">
                     ${aprvo.leave_subject}
                  </c:if>
               </td>
            </tr>
            <c:if test="${aprvo.type == 3}">
               <tr>
                  <th>종류</th>
                  <td>
                     <c:if test="${aprvo.leave_type == 1}">
                        연차
                     </c:if>
                     <c:if test="${aprvo.leave_type == 2}">
                        반차
                     </c:if>
                     <c:if test="${aprvo.leave_type == 3}">
                        보건
                     </c:if>
                     <c:if test="${aprvo.leave_type == 4}">
                        경조
                     </c:if>
                  </td>
               </tr>
               <tr>
                  <th>신청 일시</th>
                  <td>
                     ${aprvo.leave_startdate} ~ ${aprvo.leave_enddate}
                  </td>
               </tr>
            </c:if>
            <tr>
               <th>결재 내용</th>
               <td class="approval-contents">
                  <div>
                       <c:if test="${aprvo.type == 1}">
                           ${aprvo.draft_content}
                       </c:if>
                       <c:if test="${aprvo.type == 3}">
                           ${aprvo.leave_content}
                       </c:if>
                  </div>
               </td>
            </tr>

            <tr>
               <th>첨부 파일</th>
               <td></td>
            </tr>
         </table>
      </div>
   </form>
</div>

</div>
</div>
</div>