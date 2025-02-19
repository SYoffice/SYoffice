

$(document).ready(function() {

    // === 일정을 삭제하기 === //
    $("button#delete").on("click", function() {
        const schedule_name = $("input#schedule_name").val();   // 일정명
        const schedule_no = $("input#schedule_no").val();       // 일정번호(PK)

        Swal.fire({
            title: "일정 삭제",
            icon : "question",
            text : '일정 '+ schedule_name + '을(를) 삭제하시겠습니까?',
            showCancelButton: true,
            confirmButtonText: "삭제",
            cancelButtonText: "취소",
        })
        .then((result) => {
            if (result.isConfirmed){	// 삭제 버튼 클릭 시
                $.ajax({
                    url: $("input#path").val()+"/schedule/deleteSchedule",
                    type: "DELETE",
                    data: {"schedule_no": schedule_no},
                    dataType: "JSON",
                    success: function(json){
                        if(json.n==1){
                            Swal.fire({
                                title: '일정을 삭제했습니다.',        // Alert 제목
                                text: '삭제된 일정명 : '+ schedule_name,
                                icon: 'success',
                            });
                            setTimeout( () => {location.href = $("input#path").val()+"/schedule/scheduleIndex"}, 1000); // 캘린더 메인페이지로 이동
                        }
                    },
                    error: function(request, status, error){
                            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
                    }
                });
            }
            else {	// 취소 시
                Swal.fire({
                    title: '삭제를 취소했습니다.',        // Alert 제목
                    icon: 'info',
                });
            }
        }).catch((err) => {
            console.log(err);
        });
    });// end of $("button#delete").on("click", function() {}) -------------------



    // === 일정 수정하기 === //
    $("button#edit").on("click", function() {
        const frm = document.goEditFrm;
		
		frm.action = $("input#path").val()+"/schedule/editSchedule";
		frm.method = "POST";
		frm.submit();
    });// end of $("button#delete").on("click", function() {}) -----------------

});// end of $(document).ready(function() {}) --------------------------