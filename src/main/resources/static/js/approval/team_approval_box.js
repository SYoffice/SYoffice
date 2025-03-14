/**
 * 
 */
document.addEventListener("DOMContentLoaded", function() {
	// selectList();
	
	// 엔터 칠 때
	document.getElementById("search_text").addEventListener("keyup", function (event) {
		if (event.key === 'Enter') {
			onSearch(this.value);
	    }
	});

	// 돋보기 버튼 클릭
	document.getElementById("search_btn").addEventListener("click", function () {
		onSearch();
	})
});

// 검색
function onSearch(text) {
	const type_condition = document.getElementById('type_condition').value;
	const search_type = document.getElementById('search_type').value;
	const search_text = document.getElementById('search_text').value;

	window.location.href = `team_approval_box?type=${type_condition}&search_type=${search_type}&search_text=${text ? text :search_text}`;
}

// 페이징 처리 이전 ajax 요청 함수 - 현재 사용안함 
function selectList() {
	$.ajax({
		url: "/syoffice/approval/team_approval_box_json",
		type: "GET",
//		data: {  },
		dataType: "json",
		success: function(json) {

			const team_approval_box_list = document.getElementById("team_approval_box_list");

			let html = ``;
			
			if (json.length == 0) {
				html += `
						<tr>
		        				<td colspan="4">문서가 없습니다.</td>
		        			</tr>`;
			} else {

				$.each(json, function(index, approval) {
					html += `
							<tr onclick="window.location.href = '/syoffice/approval/form_view/${approval.apr_no}'">
				                <td>${approval.apr_no}</td>
				                <td>${approval.apr_startdate}</td>
				                <td>${approval.apr_enddate}</td>
				                <td>${approval.typename}</td>
				                <td class="${approval.apr_important == 1 ? 'flex' : ''}" style="text-align: left;">
				                		${approval.apr_important == 1 ? `<span class="emergency">긴급</span>` : ``}
				                		${approval.draft_subject}
				                	</td>
				                <td>${approval.name}</td>
				            </tr>`;
				});
			}

			team_approval_box_list.innerHTML = html;
		}
	});
}