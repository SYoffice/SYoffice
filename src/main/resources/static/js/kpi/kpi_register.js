
$(document).ready(function(){
    
    // == 연도에 최근 5년 값 넣어주기 == //
    const year = new Date().getFullYear();
    let v_html = ``;
    for (let i=year; i>=year-5; i--) {
        if (i == year) {
            v_html += `<option value="">선택하세요</option>`;
            v_html += `<option value='${i}'>${i}</option>`;
        }
        else{
            v_html += `<option value='${i}'>${i}</option>`;
        }
    }// end of for() ----------------
    $("select[name='kpi_year']").html(v_html);
    
    // == 목표 실적액 입력 시 콤마 넣어주기 == //
    $("input#inputKpi").on("blur", function() {
        let input = $(this).val().split(",").join("");
        
        if (isNaN(input)) {
            Swal.fire({
                title: '목표실적액은 숫자만 입력하세요!!',        // Alert 제목
                icon: 'warning',
            })
            .then(()=>{
                $("input[name='kpi_index']").val("");
                $("input#inputKpi").val("");
            });
        }

        $("input[name='kpi_index']").val(input);
        $("input#inputKpi").val(Number(input).toLocaleString('en'));
    });// end of $("input#inputKpi").on("change", function() {}) ---------------------- 
    

    // == 등록 버튼 클릭 시 == //
    $("button#register").on("click", function() {
        const kpi_index = $("input[name='kpi_index']").val().trim();
        if (kpi_index == "") {
            Swal.fire({
                title: '목표실적액을 입력하세요!!',        // Alert 제목
                icon: 'warning',
            });
            return;
        }

        const kpi_year = $("select[name='kpi_year']").val();
        if (kpi_year == "") {
            Swal.fire({
                title: '목표연도를 선택하세요!!',        // Alert 제목
                icon: 'warning',
            });
            return;
        }

        const kpi_quarter = $("select[name='kpi_quarter']").val();
        if (kpi_quarter == "") {
            Swal.fire({
                title: '분기를 선택하세요!!',        // Alert 제목
                icon: 'warning',
            });
            return;
        }

        const formData = $("form[name='kpiFrm']").serialize();

        $.ajax({
            url: $("input#path").val()+"/api/kpi/duplicateCheck",
            data: formData,
            type: "POST",
            dataType: "JSON",
            success: function(json) {
                /*
                    {"result": 1} or {"result": 0}
                */
                if (json.result == 1) {
                    // 조회 된 것이 있는 경우
                    Swal.fire({
                        title: '이미 등록한 기간의 목표실적입니다!!',        // Alert 제목
                        text: '목표 관리에서 확인하세요!',  
                        icon: 'error',
                    });
                    return;
                }
                else {
                    const frm = document.kpiFrm;
                    frm.action = $("input#path").val()+"/kpi/register";
                    frm.method = "POST";
                    frm.submit();
                }
            },
            error: function(request, status, error){
                alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
            }
        });
    });// end of $("button#register").on("click", function() {}) ---------------

});// end of $(document).ready(function(){}) -------------------- 


