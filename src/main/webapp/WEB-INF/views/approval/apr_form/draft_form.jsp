<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String ctxPath = request.getContextPath();
%>
<jsp:include page="../sidebar.jsp" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/approval/apr_write_form.css">
<script src="${pageContext.request.contextPath}/js/approval/apr_write_form.js"></script>
<div class="common_title" style="text-align: center">업무 품의서</div>

<!-- 폼 이름 -->
<input type="hidden" id="form_name" value="draft">
<div class="container">
	<form name="draft_form">
		<input type="hidden" name="apr_approver" id="apr_approver" value="">
		<input type="hidden" name="apr_approver2" id="apr_approver2" value="">
		<input type="hidden" name="apr_approver3" id="apr_approver3" value="">
		<input type="hidden" name="apr_important" id="apr_important" value="">
		<div class="btn-container">
			<button type="button" class="btn-submit" id="draft-submit">결재요청</button>
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
					<td>업무 품의서</td>
				</tr>
				<tr>
					<th>결재 제목</th>
					<td><input type="text" name="draft_subject" required /></td>
				</tr>
				<tr>
					<th>결재 내용</th>
					<td class="approval-contents">예시) <br /> OOO년 OO월 OO일 OOO에 근거하여 OOO원 사용금액을 OOO회사 측에 결제하고자 하오니 검토 후 재가하여 주시기 바랍니다. <br /> <br /> - 아 래 - <br /> 1. 목적 : 계약에 근거한 OO월 OO차분 대금결제 <br /> 2. 금액
						: 일금 OO만원 (부가세 별도) <br /> 3. 지급날짜 : 품의완료일로부터 즉시 <br /> <br />
						<div>
							<label for="approval-comments">추가 내용</label>
							<textarea required name="draft_content" placeholder="위의 양식을 따라주시기 바랍니다." id="approval-comments" style="width: 100%; height: 220px; resize: vertical"></textarea>
						</div>
					</td>
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