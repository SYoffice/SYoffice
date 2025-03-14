<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String ctxPath = request.getContextPath();
%>

<!-- Bootstrap CSS -->
<link rel="stylesheet" type="text/css" href="<%=ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css">

<!-- jsTree 라이브러리 -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.3.2/themes/default/style.min.css" />

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/approval/select_form_modal.css">
<script src="${pageContext.request.contextPath}/js/approval/select_form.js"></script>

<div class="modal fade" id="selectAprFormModal" tabindex="-1" role="dialog" aria-labelledby="selectAprFormModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="selectAprFormModalLabel">전자결재 양식 선택</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="modal-body-wrapper">
					<input id="formType" type="hidden" value="" />
					<!-- 왼쪽 양식 리스트 -->
			        <div class="form-list">
			          <div class="form-option" onclick="selectForm('leave')">근태 신청서</div>
			          <!-- <div class="form-option" onclick="selectForm('expend')">지출 결의서</div> -->
			          <div class="form-option" onclick="selectForm('draft')">업무 품의서</div>
			        </div>
			
			        <!-- 오른쪽 상세 정보 -->
			        <div class="form-detail">
			          <div id="formHeader" class="form-header">양식을 선택하세요</div>
			          <div id="formDescription" class="form-desc">
			            <p>양식에 대한 상세 정보가 이곳에 표시됩니다.</p>
			          </div>
			        </div>
				</div>
			</div>
			
			<div class="modal-footer">
		          <button class="select-button" onclick="goWritePage()">선택</button>
			</div>
		</div>
	</div>
</div>

<!-- jQuery, Bootstrap JS, jsTree -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.3.2/jstree.min.js"></script>


