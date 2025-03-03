
$(document).ready(function() {

    // === 파일 다운로드 클릭 시 다운로드 해주기 === //
    $("i.fa-download").on("click", e => {
        const atmail_no = $(e.target).parent().find("input").val();
        //alert(atmail_no);

        location.href= $("input#path").val()+"/mail/file/"+atmail_no;
    });// end of $("i.fa-download").on("click", e => {}) ---------------------


    // === 삭제 버튼을 클릭 시 휴지통으로 이동하기 === //
    $("span.delete").on("click", () => {
        const mail_no = $("input#mail_no").val();
        //console.log(mail_no);
        $.ajax({
            url : $("input#path").val()+"/api/mail/"+mail_no,
            type: "PUT",
            data: {"mail_no": mail_no},
            dataType: "JSON",
            success: function(json) {
                console.log(JSON.stringify(json));
                /*
                    {"result": 1} or {"result": 0}
                */
            },
            error: function(request, status, error){
                alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
            }
        });

        Swal.fire({
            title: '캘린더를 삭제했습니다.',        // Alert 제목
            text: '삭제된 캘린더명 : '+ smcatego_name,
            icon: 'success',
            confirmButtonText: "확인"
        })
        .then((result) => {
            location.href="javascript:history.go(0)";	// 페이지 새로고침
        })
    });// end of $("span.delete").on("click",() => {}) -------------------

});// end of $(document).ready(function() {}) -------------------------