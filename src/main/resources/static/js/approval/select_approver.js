/**
 * 환경설정 > 자동 결재선 설정에서 결재자 선택 js
 */

// 모달 오픈
function openModal() {
	$('#selectApproverModal').modal('show');
}

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
			setElementClick(json)
		},
		error: function(request, status, error) {
			alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
		}

	});
}

function setElementClick(empl_list) {
	// 사원 더블 클릭 시 정보 세팅
	$('#jstree').on("dblclick", ".jstree-node", function(e) {
		let nodeId = $(this).attr("id"); // 노드의 id 가져오기
		let empInfo = empl_list.find(emp => +emp.EMP_ID == nodeId);
		
		// 사원 정보가 있으면 selectEmployee 함수 실행 - register_apr_line.js
		if (empInfo) {
			selectEmployee(empInfo);
		}
	});
}


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

document.addEventListener("DOMContentLoaded", function() {
	getEmployees();

})