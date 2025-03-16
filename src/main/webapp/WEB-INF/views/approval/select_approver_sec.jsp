<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String ctxPath = request.getContextPath();
%>

<!-- Bootstrap CSS -->
<link rel="stylesheet" type="text/css" href="<%=ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css">

<!-- jsTree 라이브러리 -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.3.2/themes/default/style.min.css" />

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/approval/select_approval.css">
<script src="${pageContext.request.contextPath}/js/approval/select_approver_sec.js"></script>
<style>
	#apr_line_section {
    	overflow-y: auto; 
	}

	.apr-line-item {
		padding: 10px;
    	cursor: pointer;
    	margin-bottom: 2px;
	}
	
	.apr-line-item:hover {
		background-color: #e0e0e0;
	}
	
	.approver-item {
		display: flex;
	    justify-content: space-between;
		padding: 10px;
    	cursor: pointer;
    	margin-bottom: 2px;
	}
	
	.approver-item:hover {
		background-color: #e0e0e0;
	}
</style>

<div class="modal fade" id="selectApproverModal" tabindex="-1" role="dialog" aria-labelledby="selectApproverModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="selectApproverModalLabel">결재자 선택</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<input type="hidden" id="loginuser_id" value="${sessionScope.loginuser.emp_id}">
				<div style="display: flex; justify-content: space-between; align-content: center;">
					<div id="jojikdo" style="width: 48%">
						<div class="card">
							<!-- <div class="card-header"><i class="fa-solid fa-sitemap"></i></div> -->
							<div class="card-body tree-container">
								<div id="jstree"></div>
							</div>
						</div>
					</div>
					<div style="display: flex; width: 48%; flex-direction: column; justify-content: space-between;">
						<div style="height: 48%" class="card" id="apr_line_section">
						</div>
						<span>선택</span>
						<div style="height: 40%" class="card" id="select_section">
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" onclick="completeSelect()">선택완료</button>
			</div>
		</div>
	</div>
</div>

<!-- jQuery, Bootstrap JS, jsTree -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.3.2/jstree.min.js"></script>


