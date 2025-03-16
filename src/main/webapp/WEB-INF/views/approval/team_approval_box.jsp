<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%
   String ctxPath = request.getContextPath();
   //     /syoffice 
%>      
<jsp:include page="../approval/sidebar.jsp" />

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/hr/hrIndex.css" />
<c:set var="type" value="${requestScope.paraMap.type}" />
<c:set var="search_type" value="${requestScope.paraMap.search_type}" />
<c:set var="search_text" value="${requestScope.paraMap.search_text}" />
<c:set var="href" value="team_approval_box?type=${type_condition}&search_type=${search_type}&search_text=${search_text}" />
<c:set var="href" value="${href}" scope="request" />

<script src="${pageContext.request.contextPath}/js/approval/team_approval_box.js"></script>
<div class="common_title mb-45">팀문서함</div>
    <div class="approval_wrapper">
        <div class="contents">
            <div class="searchsortbox">
                <div class="select_container">
                	<select id="type_condition">
                        <option value="0" <c:if test="${type == '0'}">selected</c:if>>전체</option>
                        <option value="1" <c:if test="${type == '1'}">selected</c:if>>업무품의서</option>
                        <option value="3" <c:if test="${type == '3'}">selected</c:if>>근태신청서</option>
                    </select>
                    <select id="search_type">
                        <option value="emp_name" <c:if test="${search_type == 'fk_emp_id'}">selected</c:if>>기안자</option>
                        <option value="subject" <c:if test="${search_type == 'subject'}">selected</c:if>>제목</option>
                    </select>
                </div> 
                
                <div class="search_container">
	                <input type="text" placeholder="Search.." name="search" id="search_text" value="${search_text}">
	                <button type="submit" id="search_btn"><i class="fa fa-search"></i></button>
                </div>
            </div>
            <table class="common_table">
                <thead>
                    <tr>
                        <!-- <th style="width: 10%;">APR_NO</th> -->
                        <th style="width: 10%;">기안일</th>
                        <th style="width: 10%;">결재완료일</th>
                        <th style="width: 10%;">종류</th>
                        <th style="width: 50%;">제목</th>
                        <th style="width: 10%;">기안자</th>
                    </tr>
                </thead>
                <tbody id="team_approval_box_list">
                	<c:if test="${aprList.size() == 0}">
						<tr>
							<td colspan="5">팀 내 결재 완료 문서가 없습니다.</td>
						</tr>
					</c:if>
					<c:forEach var="approval" items="${aprList}">
						<tr onclick="window.location.href = '/syoffice/approval/form_view/${approval.apr_no}'">
				             <%-- <td>${approval.apr_no}</td> --%>
				             <td>${approval.apr_startdate}</td>
				             <td>${approval.apr_enddate}</td>
				             <td>${approval.typename}</td>
				             <td class="${approval.apr_important==1 ? 'flex' : ''}" style="text-align: left;">
								<c:if test="${approval.apr_important==1}">
									<span class="emergency">긴급</span>
								</c:if>
								<c:if test="${approval.type == 1}">
				                    ${approval.draft_subject}
				                </c:if>
								<c:if test="${approval.type == 3}">
				                    ${approval.leave_subject}
				                </c:if>
				             </td>
				             <td>${approval.name}</td>
				         </tr>
			        </c:forEach>
                </tbody>
            </table>
        </div>
    <%-- 페이지네이션  --%>
	<jsp:include page="../approval/apr_pagination.jsp" />
	<%-- 페이지네이션 --%>
</div>
</div>
</div>
</div>