/**
 * 
 */

// 상신 취소
function cancelApr(apr_no) {

	if (!confirm("정말 취소하시겠습니까? 취소 하신 문서는 삭제 됩니다.")) {
		return;
	}

	const form_type = document.getElementById("form_type").value;
	let typename = 'draft';
	
	if(form_type == '3') {
		typename = 'leave';
	}
	if(form_type == '2') {
		typename = 'expend';
	}
		
	const form_no = document.getElementById(`${typename}_no`).value;

	$.ajax({
		url: "/syoffice/approval/deleteMyForm",
		type: "POST",
		data: {
			"apr_no": apr_no,
			"form_type": form_type,
			"form_no": form_no
		},
		success: function(res) {
			console.log(res);
			if (res == 1) {
				window.location.href = '/syoffice/approval/my_approval_box';
			}

		}
	})
}

// 결재 승인
function acceptApr(apr_no) {

	if (!confirm("결재 승인합니다.")) {
		return;
	}
	
	$.ajax({
		url: "/syoffice/approval/acceptApr",
		type: "POST",
		data: {
			"apr_no": apr_no
		},
		success: function(res) {
			console.log(res);
			if (res == 1) {
				alert("승인되었습니다.");
				window.location.reload();
			}

		}
	});
}