/**
 * 
 */

document.addEventListener("DOMContentLoaded", function() {

	//selectList("0");

	document.getElementById("apr_select_option").addEventListener("change", function() {

		const apr_status = this.value;
		window.location.href = `my_approval_box?apr_status=${apr_status}&curPage=1`;
		//selectList(apr_status);
	});

});

// 페이징 처리 이전 ajax 요청 함수 - 현재 사용안함 
function selectList(apr_status) {
	$.ajax({
		url: "/syoffice/approval/my_approval_box_json",
		type: "GET",
		data: { "apr_status": apr_status },
		dataType: "json",
		success: function(json) {

			const my_approval_box_list = document.getElementById("my_approval_box_list");

			let html = ``;
			
			if (json.length == 0) {
				html += `
						<tr>
	        				<td colspan="4">기안한 문서가 없습니다.</td>
	        			</tr>`;
			} else {

				$.each(json, function(index, approval) {
					html += `
							<tr onclick="window.location.href = '/syoffice/approval/form_view/${approval.apr_no}'">
				                <td>${approval.apr_no}</td>
				                <td>${approval.apr_startdate}</td>
				                <td>${approval.typename}</td>
								<td class="${approval.apr_important == 1 ? 'flex' : ''}" style="text-align: left;">
										${approval.apr_important == 1 ? `<span class="emergency">긴급</span>` : ``}
										${approval.type == '1' ? approval.draft_subject : approval.leave_subject}
								</td>
								<td style="${approval.statusname == '반려' ? 'color:red;' : '' }">${approval.statusname}</td>
								${approval.statusname == '반려' ? `<td style='color:red;'>` : `` }${approval.statusname}</td>
								<td>${approval.apr_comment ?? '-'}</td>
				            </tr>`;
				});
			}

			my_approval_box_list.innerHTML = html;
		}
	});
}