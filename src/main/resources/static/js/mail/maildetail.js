
$(document).ready(function() {

    // === 파일 다운로드 클릭 시 다운로드 해주기 === //
    $("i.fa-download").on("click", e => {
        const atmail_no = $(e.target).parent().find("input").val();
        //alert(atmail_no);

        location.href= $("input#path").val()+"/mail/file/"+atmail_no;
    });// end of $("i.fa-download").on("click", e => {}) ---------------------


    // === 삭제 버튼을 클릭 시 휴지통으로 이동하기 === //
    $("span.delete").on("click", () => {
        const mail_no   = $("input#mail_no").val();
        const fk_emp_id = $("input#fk_emp_id").val();
        //console.log(mail_no);
        //console.log(fk_emp_id);
        
        $.ajax({
            url : $("input#path").val()+"/api/mail/"+mail_no,
            type: "DELETE",
            data: {"mail_no": mail_no, "fk_emp_id": fk_emp_id},
            dataType: "JSON",
            success: function(json) {
                console.log(JSON.stringify(json));
                /*
                    {"result": 1} or {"result": 0}
                */

                if (json.result == 1) {
                    Swal.fire({
                        title: '메일을 삭제했습니다.',        // Alert 제목
                        icon: 'success',
                        confirmButtonText: "확인"
                    })
                    .then((result) => {
                        location.href=$("input#path").val()+"/mail/box/0";	// 받은 메일함으로 이동
                    })
                }
                
            },
            error: function(request, status, error){
                alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
            }
        });

        
    });// end of $("span.delete").on("click",() => {}) -------------------

});// end of $(document).ready(function() {}) -------------------------