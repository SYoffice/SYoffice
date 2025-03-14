/**
 * 
 */

document.addEventListener("DOMContentLoaded", function() {

	// selectList();
	
	// 엔터 칠 때
	document.getElementById("search_text").addEventListener("keyup", function (event) {
		if (event.key === 'Enter') {
			const type = document.getElementById('type_condition').value;
			const search_codition = document.getElementById('search_codition').value;
            console.log('조회 조건::', type, search_codition, this.value);
			
			onSearch(type, search_codition, this.value);
			// selectList(type_condition, search_codition, this.value);
        }
	});
	
	// 돋보기 버튼 클릭
	document.getElementById("search_btn").addEventListener("click", function () {
		const type = document.getElementById('type_condition').value;
		const search_codition = document.getElementById('search_codition').value;
		const search_text = document.getElementById('search_text').value;
        console.log('조회 조건::', type, search_codition, search_text);

		onSearch(type, search_codition, search_text);
		// selectList(type_condition, search_codition, search_text);
	})

});

// 검색
function onSearch(type, search_codition, search_text) {
	window.location.href = `obtain_approval_box?type=${type}&${search_codition}=${search_text}`;
}

// 페이징 처리 이전 ajax 요청 함수 - 현재 사용안함 
function selectList(type, condition, text) {
	$.ajax({
		url: "/syoffice/approval/obtain_approval_box_json",
		type: "GET",
		data: { "type": type, [condition]: text },
		dataType: "json",
		success: function(json) {

			const obtain_approval_box_list = document.getElementById("obtain_approval_box_list");

			let html = ``;
			
			if (json.length == 0) {
				html += `
						<tr>
	        				<td colspan="5">문서가 없습니다.</td>
	        			</tr>`;
			} else {

				$.each(json, function(index, approval) {
					html += `
							<tr onclick="window.location.href = '/syoffice/approval/form_view/${approval.apr_no}'">
	                            <td>${approval.apr_startdate}</td>
	                            <td>${approval.typename}</td>
								<td class="${approval.apr_important == 1 ? 'flex' : ''}" style="text-align: left;">
										${approval.apr_important == 1 ? `<span class="emergency">긴급</span>` : ``}
										${approval.type == '1' ? approval.draft_subject : approval.leave_subject}
								</td>
								<td>${approval.dept_name}</td>
	                            <td>${approval.name}</td>
	                        </tr>`;
				});
			}

			obtain_approval_box_list.innerHTML = html;
		}
	});
}