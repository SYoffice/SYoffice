/**
 * 
 */
let approverList = [];
let seq = 1;

let el_approverList = document.getElementById('approval-tbody');
let el_approvalLineText = document.getElementById('approval_line_text');

document.addEventListener("DOMContentLoaded", function() {
	el_approverList = document.getElementById('approval-tbody');
	el_approvalLineText = document.getElementById('approval_line_text');
});

// 모달 오픈
function openModal() {
	$('#selectApproverModal').modal('show');
}

// 모달에서 사원 선택시 수행
function selectEmployee(employee) {
	// console.log(employee);
	const validEmployee = validationCheck(employee);
	
	if (approverList.length == 3) {
			alert("결재자는 최대 3명입니다.");
			return;
		}

	if (validEmployee) {
		employee.seq = seq++;
		approverList.push(employee);

		showInTable();
		$('#selectApproverModal').modal('hide');
	}

}

function validationCheck(employee) {
	let result = true;
	for (let emp of approverList) {
		// console.log(emp, employee)
		if (employee.FK_GRADE_NO > emp.FK_GRADE_NO) {
			alert('선택한 결재자의 직책이 낮습니다.')
			result = false;
		}
	}
	return result;
}

// 테이블 다시 그리기
function showInTable() {
	seq = 1;
	let html = ``;
	let text_html = ``;

	if (approverList.length == 0) {

		html = `<tr><td colspan="5">결재자를 선택하세요.</td></tr>`;
		text_html = '';

	} else {

		$.each(approverList, function(_index, item) {
			item.seq = seq++;
			text_html += _index == 0 ? `결재선: ${item.NAME}` : ` - ${item.NAME}`;
			html += `<tr>
						<td>${item.seq}</td>
						<td>${item.DEPT_NAME}</td>
						<td>${item.GRADE_NAME}</td>
						<td>${item.NAME}</td>
						<td>
							<button onclick="removeApprover(${_index})">삭제</button>
						</td>
					</tr>`;
		});

	}

	el_approverList.innerHTML = html;
	el_approvalLineText.innerHTML = text_html;
}

// 삭제 버튼 클릭
function removeApprover(index) {
	approverList = approverList.filter((_, _idx) => _idx != index);
	showInTable();
}

function register() {
	const apline_name = document.getElementById("apr_line_name").value;
	
	if (apline_name == '') {
		alert("결재선 명은 필수값입니다.");
		return;
	} else if (approverList.length == 0) {
		alert("결재자는 1명 이상 선택해 주세요.");
		return;
	}

	const paramAprLine = {};

	paramAprLine.apline_name = apline_name;

	paramAprLine.apline_approver = approverList[0].EMP_ID;
	paramAprLine.apline_approver2 = approverList[1] ? approverList[1].EMP_ID : null;
	paramAprLine.apline_approver3 = approverList[2] ? approverList[2].EMP_ID : null;

	console.log("등록::", paramAprLine);

	$.ajax({
		url: "/syoffice/approval/registerAprLine",
		contentType: "application/json",
		type: "POST",
		data: JSON.stringify(paramAprLine),
		success: function(res) {
			if (res == 1) {
				alert("결재선이 등록되었습니다");
				window.location.href = "/syoffice/approval/approval_setting";
			}
		},
		error: function(request, status, error) {
			alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
		}
	});
}

