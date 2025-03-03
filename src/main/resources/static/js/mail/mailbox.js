

$(document).ready(function() {

    $("input:checkbox[name='fk_mail_no']").on("change", function(){
        //alert($(this).val());

    });

    /* 메일 제목 클릭 시 상세보기 */
    $("td.subject").on("click", function(){
        const fk_mail_no = $(this).parent().children().find("input").val();
        //alert(fk_mail_no);    3
        location.href = $("input#path").val()+"/mail/"+fk_mail_no;
    });// end of $("td.subject").on("click", function(){}) ------------------------------


});// end of $(document).ready(function() {}) ---------------------