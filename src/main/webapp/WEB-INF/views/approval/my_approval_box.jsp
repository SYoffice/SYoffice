<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%
   String ctxPath = request.getContextPath();
   //     /syoffice 
%>      
<!-- 내 기안 문서함 -->
<jsp:include page="./sidebar.jsp" />

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/hr/hrIndex.css" />
<c:set var="apr_status" value="${requestScope.paraMap.apr_status}" />
<c:set var="href" value="my_approval_box?apr_status=${requestScope.paraMap.apr_status}" />
<c:set var="href" value="${href}" scope="request" />

<script src="${pageContext.request.contextPath}/js/approval/my_approval_box.js"></script>

<%-- paraMap : ${requestScope.paraMap} <br/> --%>


             <div class="common_title mb-45">기안 문서함</div>
             <div class="approval_wrapper">
                <div class="contents">
                    <div class="searchsortbox">
                        <div class="select_container">
							<select id="apr_select_option">
							    <option value="0" <c:if test="${apr_status == '0'}">selected</c:if>>전체</option>
							    <option value="1" <c:if test="${apr_status == '1'}">selected</c:if>>기안</option>
							    <option value="2" <c:if test="${apr_status == '2'}">selected</c:if>>결재진행중</option>
							    <option value="3" <c:if test="${apr_status == '3'}">selected</c:if>>전결대기</option>
							    <option value="4" <c:if test="${apr_status == '4'}">selected</c:if>>전결</option>
							    <option value="5" <c:if test="${apr_status == '5'}">selected</c:if>>반려</option>
							</select>
                        </div> 
                    </div>
                   <table class="common_table">
                        <thead>
                            <tr>
                            	<!-- <th style="width: 5%;">APR_NO</th> -->
                                <th style="width: 10%;">기안일</th>
                                <th style="width: 10%;">종류</th>
                                <th style="width: 50%;">제목</th>
                                <th style="width: 10%;">상태</th>
                                <th style="width: 20%;">결재 의견</th>
                            </tr>
                        </thead>
                        <tbody id="my_approval_box_list">
                        	<c:if test="${aprList.size() == 0}">
	                        	<tr>
			        				<td colspan="5">기안한 문서가 없습니다.</td>
			        			</tr>
                        	</c:if>
                        	<c:forEach var="approval" items="${aprList}">
	                        	<tr onclick="window.location.href = '/syoffice/approval/form_view/${approval.apr_no}'">
					                <%-- <td>${approval.apr_no}</td> --%>
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
									
									<c:if test="${approval.statusname eq '반려'}">
										<td style="color:red;">${approval.statusname}</td>
									</c:if>
									<c:if test="${approval.statusname eq '전결'}">
										<td style="color:blue;">${approval.statusname}</td>
									</c:if>
									<c:if test="${approval.statusname ne '전결' && approval.statusname ne '반려'}">
										<td>${approval.statusname}</td>
									</c:if>
									<td>${approval.apr_comment}</td>
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