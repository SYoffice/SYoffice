<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String ctxPath = request.getContextPath();
//     /syoffice
%>
<jsp:include page="./sidebar.jsp" />
<style>
	.approval_wrapper a {
		margin-top: 5px;
	    display: flex;
		justify-content: end;
	}
	
	.content_title {
	    overflow: hidden;
	    text-overflow: ellipsis;
	    display: -webkit-box;
	    -webkit-line-clamp: 2;
	    -webkit-box-orient: vertical;
    }
</style>

<div class="common_title mb-45">전자결재</div>
<div class="approval_wrapper">
	<c:if test="${myaprList.size() > 0}">
		<div class="contents">
			<div class="content_title">결재대기문서</div>
			<div class="content_sub_title">
				결재해야할 문서가 <span style="color: #95b6ff; font-weight: bold;">${myaprList.size()}</span>건
				있습니다.
			</div>
			<div class="gridbox_wrapper">
				<c:forEach var="myapproval" items="${myaprList}">
					<div class="gridbox">
						<div class="grid_text">
							<%-- <div class="content_title">${myapproval.apr_no}</div> --%>
							<div class="content_title">
								<c:if test="${myapproval.type == '1'}">
                                    ${myapproval.draft_subject}
                                </c:if>
								<c:if test="${myapproval.type == '3'}">
                                    ${myapproval.leave_subject}
                                </c:if>
							</div>
							<div class="ft_14">기안자 : ${myapproval.name}</div>
							<div class="ft_14">기안일 : ${myapproval.apr_startdate}</div>
							<div class="ft_14">종류 : ${myapproval.typename}</div>
						</div>
						<button type="button"
							onclick="window.location.href = '<%=ctxPath%>/approval/form_view/${myapproval.apr_no}'">결재하기</button>
					</div>
				</c:forEach>
			</div>
		</div>
	</c:if>
	<div class="contents">
		<div class="content_title">기안 진행 문서</div>
		<table class="common_table">
			<thead>
				<tr>
					<!-- <th style="width: 10%;">APR_NO</th> -->
					<th style="width: 10%;">기안일</th>
					<th style="width: 10%;">종류</th>
					<th style="width: 80%;">제목</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${aprList.size() == 0}">
					<tr>
						<td colspan="4">기안 진행 문서가 없습니다.</td>
					</tr>
				</c:if>
				<c:forEach var="approval" items="${aprList}">
					<tr>
						<%-- <td>${approval.apr_no}</td> --%>
						<td>${approval.apr_startdate}</td>
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
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<a href="<%=ctxPath%>/approval/my_approval_box">더보기</a>
	</div>
	<div class="contents">
		<div class="content_title">결재 완료 문서</div>
		<div></div>
		<table class="common_table">
			<thead>
				<tr>
					<!-- <th style="width: 10%;">APR_NO</th> -->
					<th style="width: 10%;">기안일</th>
					<th style="width: 10%;">결재완료일</th>
					<th style="width: 10%;">종류</th>
					<th style="width: 70%;">제목</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${aprList4.size() == 0}">
					<tr>
						<td colspan="4">결재 완료 문서가 없습니다.</td>
					</tr>
				</c:if>
				<c:forEach var="approval" items="${aprList4}">
					<tr>
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
					</tr>
				</c:forEach>

			</tbody>
		</table>
		<a href="<%=ctxPath%>/approval/team_approval_box">더보기</a>
	</div>
</div>
</div>
</div>
</div>