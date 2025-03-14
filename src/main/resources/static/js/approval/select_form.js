/**
 * 
 */

function selectForm(formName) {
	const formHeader = document.getElementById("formHeader");
	const formDescription = document.getElementById("formDescription");
	const formType = document.getElementById("formType");

	switch (formName) {
		case "leave":
			formHeader.innerText = "휴가 신청 양식";
			formDescription.innerHTML =
				"<p>휴가 날짜와 기간을 선택하고, 사유를 작성할 수 있습니다.</p>";
			formType.value = 'leaveForm';
			break;
		/*case "expend":
			formHeader.innerText = "비용 결재 양식";
			formDescription.innerHTML =
				"<p>발생한 비용을 입력하고, 결재를 요청할 수 있습니다.</p>";
			formType.value = 'expendForm';
			break;*/
		case "draft":
			formHeader.innerText = "업무 품의서 양식";
			formDescription.innerHTML =
				"<p>품의 제목, 내용을 입력할 수 있습니다.</p>";
			formType.value = 'draftForm';
			break;
		default:
			formHeader.innerText = "양식을 선택하세요";
			formDescription.innerHTML = "<p>양식에 대한 상세 정보가 이곳에 표시됩니다.</p>";
	}
}

function goWritePage() {
	const formType = document.getElementById("formType").value;

	if (formType == '') {
		alert('전자결재 양식을 선택해주세요.')
	} else {
		window.location.href = `/syoffice/approval/write?formType=${formType}`
	}

}
