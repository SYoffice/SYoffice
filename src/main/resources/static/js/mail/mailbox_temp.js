

$(document).ready(function() {

    // === 체크박스 상태 변화시 전체 체크/해제 하기 === //
    $("input:checkbox[name='fk_mail_no']").on("change", function() {
        let isNotChecked = false;
        $("input:checkbox[name='fk_mail_no']").each(function(index){
            if (!$(this).is(":checked")) {
                isNotChecked = true;
            }
        });

        if (isNotChecked) {
            // 체크된 것이 없다면
            $("input#allMailSelect").prop("checked", false);
        }
        else {
            // 모두 체크되어 있다면
            $("input#allMailSelect").prop("checked", true);
        }
    });// end of $("input:checkbox[name='fk_mail_no']").on("change", function() {}) ---------------------
	
    /* 메일 제목 클릭 시 상세보기 */
    $("td.subject").on("click", function(){
        const fk_mail_no = $(this).parent().children().find("input").val();
        //alert(fk_mail_no);    3
        location.href = $("input#path").val()+"/mail/new/"+fk_mail_no;  // 새로운 메일 작성 폼으로 이동
    });// end of $("td.subject").on("click", function(){}) ------------------------------

	
    // === 메일 전체 체크 === //
    $("input#allMailSelect").on("click", () => {
		const checked = $('input#allMailSelect').is(':checked');
		if (checked) {
			$("input:checkbox[name='fk_mail_no']").prop('checked', true);
		}
        else {
            $("input:checkbox[name='fk_mail_no']").prop('checked', false);
        }
    });// end of $("input:checkbox[name='fk_mail_no']").on("click", ()=> {}) -------------------

	
	// === 체크한 메일 삭제 버튼 클릭 시 === //
	$("span.delete").on("click", function() {
        const checkedLength = $("input:checkbox[name='fk_mail_no']:checked").length;
        if (checkedLength == 0) {
            Swal.fire({
                title: '체크된 항목이 없습니다.',        // Alert 제목
                icon: 'warning',
                confirmButtonText: "확인"
            });
            return;
        }
		deleteMail();
    })// end of $("span.delete").on("click", function() {}) ---------------------
	
});// end of $(document).ready(function() {}) ---------------------


// Function Declaration
// === 선택한 메일을 휴지통으로 이동
function deleteMail() {

    Swal.fire({
		title: "선택한 메일을 삭제하시겠습니까?",
		icon : "question",
		showCancelButton: true,
		confirmButtonText: "삭제",
		cancelButtonText: "취소",
	})
    .then((result) => {
        if (!result.isConfirmed) {  // 삭제 취소 시
            return;
        }
        else {
            let resultCnt = 0;
            const fk_emp_id = $("input#fk_emp_id").val();
            const checkedLength = $("input:checkbox[name='fk_mail_no']:checked").length;

            $("input:checkbox[name='fk_mail_no']:checked").each(function(index) {
                //console.log("fk_mail_no: "+ $(this).val());
                //mailNoArr.push($(this).val());
                const mail_no = $(this).val();
            
                $.ajax({
                    url : $("input#path").val()+"/api/mail/temp/"+mail_no,
                    type: "DELETE",
                    data: {"mail_no": mail_no, "fk_emp_id": fk_emp_id},
                    dataType: "JSON",
                    success: function(json) {
                        console.log(JSON.stringify(json));
                        
                        resultCnt += json.result;
                        
                        console.log("resultCnt: "+ resultCnt);
                        console.log("checkedLength: "+ checkedLength);
                        if (resultCnt == checkedLength) {
                            Swal.fire({
                                title: '메일을 삭제했습니다.',        // Alert 제목
                                icon: 'success',
                                confirmButtonText: "확인"
                            })
                            .then((result) => {
                                location.href="javascript:history.go(0)";	// 페이지 새로고침
                            });
                        }
                    },
                    error: function(request, status, error){
                        alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
                    }
                });
            });// end of $("input:checkbox[name='fk_mail_no']").each(function(index) {}) -------------  
        }
    });

}// end of function deleteMail() ----------------------------