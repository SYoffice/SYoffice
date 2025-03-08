

$(document).ready(function() {
    getMailReadCount();     // 읽은 메일과 전체 메일 개수를 가져오는 함수
    getMailList();          // 나에게 보낸 메일리스트를 가져옴
});// end of $(document).ready(function() {}) -----------------------------


// Function Declaration
// === 읽은 메일과 전체 메일 개수를 가져오는 함수 === //
function getMailReadCount() {
    const fk_emp_id = $("input#fk_emp_id").val();

    $.ajax({
        url: $("input#path").val()+"/api/mail/count",
        type: "GET",
        data: {"fk_emp_id": fk_emp_id, "receive_division": "0"},
        dataType: "JSON",
        success: function(json) {
            // console.log(JSON.stringify(json));
            /*
                {"cnt":0, "total":2}
            */

            let v_html = `<span>${json.cnt}</span>&nbsp;/&nbsp;<span>${json.total}</span>`;
            $("div#title_area").append(v_html);
        },
        error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }
    });
}// end of function getMailReadCount() -----------


// === 나에게 보낸 메일 리스트를 가져오는 함수 === //
function getMailList() {
    const fk_emp_id = $("input#fk_emp_id").val();

    $.ajax({
        url: $("input#path").val()+"/api/mail/count",
        type: "GET",
        data: {"fk_emp_id": fk_emp_id, "receive_division": "0"},
        dataType: "JSON",
        success: function(json) {
            // console.log(JSON.stringify(json));
            /*
                {"cnt":0, "total":2}
            */

            let v_html = `
                <span class="h3">받은 메일함</span>
                <span>${json.cnt}</span>&nbsp;/&nbsp;<span>${json.total}</span>
            `;
            $("div#title_area").html(v_html);
        },
        error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }
    });
}// end of function getMailList() ----------------------