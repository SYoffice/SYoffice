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
<script src="${pageContext.request.contextPath}/js/approval/select_approver.js"></script>

<script>

	
	document.addEventListener("DOMContentLoaded", function() {
		$('#reject-button').on('click', function() {
			var aprNo = $(this).data('value'); // data-value 속성에 저장된 apr_no 값

			$('#aprNo').text(aprNo);
		});
	})

	//반려
	function rejectApr() {

		if (!confirm("기안을 반려합니다.")) {
			return;
		}

		const apr_no = document.getElementById("aprNo").innerText;
		const apr_comment = document.getElementById("apr_comment").value;

		$.ajax({
			url : "/syoffice/approval/rejectApr",
			type : "POST",
			data : {
				"apr_no" : apr_no,
				"apr_comment" : apr_comment
			},
			success : function(res) {
				console.log(res);
				if (res == 1) {
					alert("반려되었습니다.");
					window.location.href= '/syoffice/approval/obtain_approval_box';
				}

			}
		});
	}
</script>

<div class="modal fade" id="rejectModal" tabindex="-1" role="dialog" aria-labelledby="rejectModalLabel" aria-hidden="true">

	<div class="modal-dialog modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="selectApproverModalLabel">기안 문서</h5>
			</div>
			<div class="modal-body">
				<div>
					<div>
						<input type="hidden" id="aprNo"></span>
					</div>
					<div>
						<label>반려 의견</label>
						<input id="apr_comment" type="text" />
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button onclick="rejectApr()">확인</button>
			</div>
		</div>
	</div>
</div>