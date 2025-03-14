<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String ctxPath = request.getContextPath();
%>
<!-- 사이드바 -->
<jsp:include page="./sidebar.jsp" />

<link rel="stylesheet" href="<%=ctxPath%>/css/approval/approval_setting.css">
<script src="${pageContext.request.contextPath}/js/approval/approval_setting.js"></script>

<div class="common_title mb-80">전자결재 환경설정</div>

<div>
</div>
				
<div class="approval_wrapper">
	<div class="contents">
		<div class="content-header">
			<span class="content_title mb-none">자동결재선 목록</span>
			<div>
				<button type="button" onclick="goRegisterPage()">결재선 등록</button>
				<button type="button" onclick="onClickRemove()">삭제</button>
			</div>
		</div>

		<div>
			<table>
				<thead>
					<tr>
						<th style="width: 10%;">선택</th>
						<th style="width: 10%;">no</th>
						<th style="width: 30%;">결재선 명</th>
						<th style="width: 50%;">결재선</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${aprLineList.size() == 0}">
						<tr>
							<td colspan="4">저장된 결재선이 없습니다.</td>
						</tr>
					</c:if>
					<c:forEach var="aprLine" items="${aprLineList}">
						<tr>
							<td style="width: 50px"><input name="apline_no" type="checkbox" value="${aprLine.apline_no}"/></td>
							<td style="width: 50px">${aprLine.apline_no}</td>
							<td>${aprLine.apline_name}</td>
							<td style="text-align: left">${aprLine.approval_chain_names}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<form name="delFrm">
				<input type="hidden" name="apline_no" />
			</form>
		</div>
	</div>
</div>
</div>
</div>
</div>