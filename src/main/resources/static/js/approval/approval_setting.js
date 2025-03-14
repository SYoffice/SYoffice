/**
 * 
 */

function goRegisterPage() {
	window.location = '/syoffice/approval/register_apr_line';
}

function onClickRemove() {
	let arr_checked = [];
	const checkboxes = document.querySelectorAll('input[name="apline_no"]');
	checkboxes.forEach(function(checkbox) {
		if (checkbox.checked) {
			arr_checked.push(checkbox.value);
		}
	});
	
	if(arr_checked.length === 0) {
		alert("삭제할 항목을 선택해주세요.");
		return;
	}
	
	const str_checked = arr_checked.join();	
	
	$.ajax({
	    url: "/syoffice/approval/deleteAprLine",
	    type: "POST",  // DELETE 요청
	    data: { "str_checked": str_checked },  // URL 파라미터로 데이터 전달
	    dataType: "json",  // 응답 데이터 타입
	    success: function(res) {
			
			console.log(arr_checked, str_checked, res, arr_checked.length)
			
			if (res == arr_checked.length) {
				alert("삭제되었습니다.");
				window.location.href = "/syoffice/approval/approval_setting";				 
			}
			
	    },
	    error: function(request, status, error) {
	        alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
	    }
	});
}