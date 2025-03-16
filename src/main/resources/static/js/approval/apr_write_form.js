/**
 * 
 */

// 취소 버튼 클릭시
function onClickCancel() {
	window.location.href = "/syoffice/approval/approval_main";
}

// 자주쓰는 결재라인 선택 모달에서 리스트 받음
function drawSignatureSection(employee_list) {

	// 결재자 apr_approver, apr_approver2, apr_approver3
	let apr_approver_html = document.getElementById("apr_approver");
	let apr_approver2_html = document.getElementById("apr_approver2");
	let apr_approver3_html = document.getElementById("apr_approver3");

	// 서명 이름란
	const signatureTable = document.getElementById("signature-table-names");
	// 서명란
	const signature_sec = document.getElementById("signature-table-sec2");
	let html = ``;
	let s_html = ``;

	for (let i = 0; i < employee_list.length; i++) {
		const employee = employee_list[i];
		if(employee && employee.NAME) {
			html += `<th rowspan="3" style="width: 10px">결재</th>
					<td>${employee.NAME}</td>`;
	
			s_html += `<td></td>`
		}
	}

	apr_approver_html.value = employee_list[0].EMP_ID;
	apr_approver2_html.value = employee_list[1] ? employee_list[1].EMP_ID : "";
	apr_approver3_html.value = employee_list[2] ? employee_list[2].EMP_ID : "";

	signatureTable.innerHTML = html;
	signature_sec.innerHTML = s_html;

	// 모달 닫기
	$('#selectApproverModal').modal('hide');
}



document.addEventListener("DOMContentLoaded", function() {

	const formNm = document.getElementById("form_name").value;

	flatpickr.localize(flatpickr.l10ns.ko);

	if (formNm == "leave") {

		flatpickr("#date", {
			mode: "range",
			minDate: "today",
			dateFormat: "Y-m-d",
	        onChange: function(selectedDates, dateStr, instance) {
				// 두 날짜가 선택되면 selectedDates 배열에 저장됨
				const startDate = selectedDates[0];
	            const endDate = selectedDates[1];

				const formatDate = (date) => {
					const year = date.getFullYear();
				    const month = String(date.getMonth() + 1).padStart(2, '0');
				    const day = String(date.getDate()).padStart(2, '0');
				    const hours = String(date.getHours()).padStart(2, '0');
				    const minutes = String(date.getMinutes()).padStart(2, '0');
				    return `${year}-${month}-${day}`;
				};

	           document.getElementById('leave_startdate').value = formatDate(startDate); // 시작일
	           document.getElementById('leave_enddate').value = formatDate(endDate);   // 종료일

	        }
		}); // 첫번째 파라미터는 dom id, 두번째 파라미터는 flatpickr 옵션

		flatpickr("mySelector");
	}
	
	// 체크박스 선택시 랄ㄹ라
	document.getElementById("isImportant").addEventListener("change", function () {
		const title = formNm == "leave" ? `근태 신청서` : `업무 품의서`;
		let html = ``;
		if (this.checked) {
			html += `<img src="https://upload.wikimedia.org/wikipedia/commons/8/83/Emergency_Light.gif" style="
						width: 63px;">`
		}
		html += `${title}`;
		
		document.getElementById("title_sec").innerHTML = html;
	})

	// formNm 으로 공통 사용
	const submit_btn = document.getElementById(`${formNm}-submit`);

	// 결재요청 버튼 클릭 이벤트 - 결재요청(기안상신) 함
	submit_btn.addEventListener("click", function() {
		// const frm = document.draft_form;
		const frm = document[`${formNm}_form`];
		frm.method = "post";
		frm.action = `/syoffice/approval/write_${formNm}_form`;
		
		console.log(frm.action)

		const formData = new FormData(frm);

		// apr_important 필드 값을 'on'과 비교하여 체크 상태 확인
		const checked = document.querySelector('input[name="isImportant"]:checked');
		
		// submit 전 "긴급" 값 재세팅
		const aprImportantInput = document.getElementById("apr_important");
		aprImportantInput.value = checked ? "1" : "0";

		// submit 전 "휴가종류" 값 재세팅
		const selectedRadio = document.querySelector('input[name="leave_type"]:checked');
		if (selectedRadio) {
		    frm.querySelector("[name='leave_type']").value = selectedRadio.value;
		}

		// == 필수값 입력 체크 [S] ==
		let valid = true;
		const inputs = frm.querySelectorAll('input[required], select[required], textarea[required]');

		inputs.forEach((input) => {
			if (!input.value.trim()) {
				valid = false;
				//input.style.borderColor = 'red'; // 빈 input은 빨간색 테두리로 표시
			} else {
				//input.style.borderColor = ''; 
			}
		});

		if (!valid) {
			alert("입력되지 않은 항목이 있습니다.");
			return;
		}
		// == 필수값 입력 체크 [E] ==
		for (let [key, value] of formData.entries()) {
		    console.log(`${key}: ${value}`);
		}
		frm.submit();
	})

});

