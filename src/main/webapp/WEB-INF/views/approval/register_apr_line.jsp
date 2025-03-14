<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String ctxPath = request.getContextPath();
%>
<jsp:include page="./sidebar.jsp" />

<link rel="stylesheet" href="<%=ctxPath%>/css/approval/approval_setting.css">
<script src="${pageContext.request.contextPath}/js/approval/register_apr_line.js"></script>

<div class="common_title mb-80">전자결재 환경설정</div>

<div class="approval_wrapper">
	<div class="contents">
		<div class="content-header">
			<span class="content_title mb-none">자동결재선 설정</span>
			<button onclick="register()">등록</button>
		</div>

		<div class="approval_line_name">
			<label>자동결재선명</label> <input id="apr_line_name" type="text" />
		</div>

		<div class="approval_line_form">
			<div class="label">결재선 설정</div>
			<div>
				<div class="form_box">
					<div>
						<div class="select-approver-btn-wrapper">
							<div>
								<span id="approval_line_text"></span>
							</div>
							<button onclick="openModal()">결재자 추가</button>
						</div>

						<!-- 모달 (select_employee_modal) -->
						<jsp:include page="./select_approver.jsp" />

						<table>
							<thead>
								<tr>
									<th style="width: 50px">순서</th>
									<th style="width: 100px">부서명</th>
									<th style="width: 100px">직책</th>
									<th style="width: 100px">이름</th>
									<th style="width: 100px">삭제</th>
								</tr>
							</thead>
							<tbody id="approval-tbody">
								<tr>
									<td colspan="5">결재자를 선택하세요.</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</div>
</div>
</div>