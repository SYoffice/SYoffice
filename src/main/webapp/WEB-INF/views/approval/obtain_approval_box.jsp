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
<c:set var="emp_name" value="${requestScope.paraMap.emp_name}" />
<c:set var="dept_name" value="${requestScope.paraMap.dept_name}" />
<c:set var="href" value="obtain_approval_box?type=${type}&emp_name=${emp_name}&dept_name=${dept_name}" />
<c:set var="href" value="${href}" scope="request" />

<script src="${pageContext.request.contextPath}/js/approval/obtain_approval_box.js"></script>
<div class="common_title mb-45">결재대기문서함</div>
            <div class="approval_wrapper">
                <div class="contents">
                   <div class="searchsortbox">
                        <div class="select_container">
                        	<select id="type_condition">
                                <option value="0" <c:if test="${type == '0'}">selected</c:if>>전체</option>
                                <option value="1" <c:if test="${type == '1'}">selected</c:if>>업무품의서</option>
                                <option value="3" <c:if test="${type == '3'}">selected</c:if>>근태신청서</option>
                            </select>
                            
                            <select id="search_codition">
                                <option value="emp_name" <c:if test="${not empty emp_name}">selected</c:if>>기안자</option>
                                <option value="dept_name" <c:if test="${not empty dept_name}">selected</c:if>>기안부서</option>
                            </select>
                        </div> 
                        
                        <div class="search_container">
	                        <input type="text" id="search_text" placeholder="Search.." name="search" 
								value="<c:if test="${not empty emp_name}">${emp_name}</c:if><c:if test="${not empty dept_name}">${dept_name}</c:if>"
							/>
	                        <button id="search_btn" type="submit"><i class="fa fa-search"></i></button>
                        </div>
                    </div>
                    <table class="common_table">
                        <thead>
                            <tr>
                                <th style="width: 10%;">기안일</th>
                                <th style="width: 10%;">종류</th>
                                <th style="width: 60%;">제목</th>
                                <th style="width: 10%;">기안부서</th>
                                <th style="width: 10%;">기안자</th>
                            </tr>
                        </thead>
                        <tbody id="obtain_approval_box_list">
                        	<c:if test="${aprList.size() == 0}">
	                        	<tr>
			        				<td colspan="5">문서가 없습니다.</td>
			        			</tr>
                        	</c:if>
                        	<c:forEach var="approval" items="${aprList}">
	                        	<tr onclick="window.location.href = '/syoffice/approval/form_view/${approval.apr_no}'">
		                            <td>${approval.apr_startdate}</td>
		                            <td>${approval.typename}</td>
									<td class="${approval.apr_important == 1 ? 'flex' : ''}" style="text-align: left;">
										<c:if test="${approval.apr_important == 1}">
											<span class="emergency">긴급</span>
										</c:if>
										<c:if test="${approval.type == '1'}">
											${approval.draft_subject}
										</c:if>
										<c:if test="${approval.type == '3'}">
											${approval.leave_subject}
										</c:if>
									</td>
									<td>${approval.dept_name}</td>
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