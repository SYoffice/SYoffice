/**
 * 결재 상신 페이지에서 결재자 선택 js
 */

// 결재자 리스트
let selected_emp_list = [];

// 모달 오픈
function openModal() {
	document.getElementById("select_section").html = "";
	$('#selectApproverModal').modal('show');
}

document.addEventListener("DOMContentLoaded", function() {
	getEmployees();
	getApprovalLine();
})

function convertToJsTreeFormat(data) {
	let result = [];
	let dept = {};
	let branch = {};
	let personType = "";

	data.forEach((item) => {
		let branchId = item.FK_BRANCH_NO;
		let deptId = `${item.FK_BRANCH_NO}_${item.FK_DEPT_ID}`;
		let empId = item.EMP_ID;
		let gradeId = item.FK_GRADE_NO;
		let managerId = item.MANAGER_ID;

		if (gradeId < 4 || managerId == empId) {
			personType = "executive";
		} else {
			personType = "person";
		}

		if (!branch[branchId]) {
			branch[branchId] = {
				id: branchId,
				parent: "#",
				text: item.BRANCH_NAME,
				type: "department",
				state: { opened: true },
			};
			result.push(branch[branchId]);
		}

		if (!dept[deptId]) {
			dept[deptId] = {
				id: deptId,
				parent: branchId,
				text: item.DEPT_NAME,
				type: "department",
			};
			result.push(dept[deptId]);
		}

		result.push({
			id: empId,
			parent: deptId,
			text: `${item.NAME} ${item.GRADE_NAME}`,
			type: personType,
		});
	});

	return result;
}

// 사원 조회 및 return
function getEmployees() {
	$.ajax({
		url: "/syoffice/organization/list",
		type: "GET",
		dataType: "json",
		success: function(json) {
			showInJsTree(json);
			setElementClick(json);
		},
		error: function(request, status, error) {
			alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
		}

	});
}

// jstree 영역에 데이터 노출
function showInJsTree(empl_list) {
	$("#jstree").jstree({
		core: {
			animation: 0,
			check_callback: true,
			data: convertToJsTreeFormat(empl_list),
			themes: { dots: false },
		},
		plugins: ["types"],
		types: {
			executive: { icon: "fa-solid fa-user-tie" },
			department: { icon: "fa-solid fa-building" },
			person: { icon: "fa-solid fa-user" },
		},
	});
}

// 각 리스트 더블 클릭 이벤트를 넣어준다
function setElementClick(empl_list) {
	// 사원 더블 클릭 시 정보 세팅
	$('#jstree').on("changed.jstree", function(e, data) {
		let empInfo = empl_list.find(emp => +emp.EMP_ID == data.node.id);

		// 사원 정보가 있으면 전역변수 push
		if (empInfo) {
			addEmployeeList(empInfo);
		}
	});
}

// 내가 등록한 결재선 조회 
function getApprovalLine() {
	$.ajax({
		url: "/syoffice/approval/get_approval_line_json",
		type: "GET",
		dataType: "json",
		success: function(json) {
			const line_section = document.getElementById("apr_line_section");

			let html = ``;

			$.each(json, function(index, item) {
				html += `<buutton class="apr-line-item" onclick="addEmployeeListAprLine('${JSON.stringify(item).replace(/"/g, '&quot;')}')">
			                ${item.apline_name} <br/> ㄴ ${item.approval_chain_names}
			             </buutton>`;
			});
			line_section.innerHTML = html;
		},
		error: function(request, status, error) {
			alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
		}
	})
}

// 결재선 정보 한꺼번에 selected_emp_list 에 넣어버리기
function addEmployeeListAprLine(item) {
	const parsedItem = JSON.parse(item);

	selected_emp_list = [
		{ EMP_ID: parsedItem.apline_approver, NAME: parsedItem.apline_approver_name },
		{ EMP_ID: parsedItem.apline_approver2, NAME: parsedItem.apline_approver2_name },
		{ EMP_ID: parsedItem.apline_approver3, NAME: parsedItem.apline_approver3_name }
	];
	
	drawTable();
}


// 전역 selected_emp_list 변수에 push
function addEmployeeList(empInfo) {

	if (selected_emp_list.find((emp) => emp.EMP_ID == empInfo.EMP_ID)) {
		alert("이미 선택된 결재자입니다.");
		return;
	}

	const validEmployee = validationCheck(empInfo);

	if (!validEmployee) {
		return;
	}

	if (selected_emp_list.length == 3) {
		alert("결재자는 최대 3명입니다.");
		return;
	}

	selected_emp_list.push(empInfo);
	drawTable();
}

// 오른쪽 섹션에 그리는 함수
function drawTable() {
	const select_section = document.getElementById("select_section");
	let html = ``;

	for (let i = 0; i < selected_emp_list.length; i++) {

		const empInfo = selected_emp_list[i];

		if(empInfo && empInfo.NAME) {
			html += `<div>
						<div class="approver-item">${i + 1} ${empInfo.NAME}
							<span onclick="removeEmp(${empInfo.EMP_ID})">삭제</span>
						</div>
					</div>`;
		}
	}
	select_section.innerHTML = html;
}

function validationCheck(employee) {
	let result = true;
	for (let emp of selected_emp_list) {
		// console.log(emp, employee)
		if (employee.FK_GRADE_NO > emp.FK_GRADE_NO) {
			alert('선택한 결재자의 직책이 낮습니다.')
			result = false;
		}
	}
	return result;
}

function removeEmp(emp_id) {
	selected_emp_list = selected_emp_list.filter((v) => v.EMP_ID != emp_id);
	drawTable();
}

function completeSelect() {
	// apr_write_form.js 의 서명 섹션에 그리기 함수에
	// selected_emp_list를 넘겨준다 apr_write_form.js
	drawSignatureSection(selected_emp_list);
}

