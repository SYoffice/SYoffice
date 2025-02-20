
$(document).ready(function() {
    // == 연도에 최근 5년 값 넣어주기 == //
    const year = new Date().getFullYear();
    let v_html = ``;
    for (let i=year; i>=year-5; i--) {
        v_html += `<option value='${i}'>${i}년</option>`;
    }// end of for() ----------------
    $("select[name='searchYear']").html(v_html);

    // == 연도가 변경 될 때마다 해당연도의 목표실적을 보여주기 == //
    $("select[name='searchYear']").on("change", function() {
        const searchYear = $("select[name='searchYear']").val();
        const fk_dept_id = $("input#fk_dept_id").val();

        $.ajax({
            url : $("input#path").val()+"/api/kpi/deptKpiByYear",
            type: "GET",
            data: {"fk_dept_id": fk_dept_id, "searchYear": searchYear},
            dataType: "JSON",
            success: function (json) {
                //console.log(JSON.stringify(json));
                /*
                    [{"kpi_no":"13","fk_dept_id":"2","kpi_year":"2025","kpi_quarter":"1","kpi_index":"30000000","dept_name":"인사부","manager_id":"9999","branch_name":"본사"}]
                */
                let v_html = ``;
                if (json.length > 0){
                    $.each(json, function(index, item) {
                        if (index == 0) {
                        v_html  +=` <tr>
                                        <th>소속</th>
                                        <td colspan="6">${item.branch_name} - ${item.dept_name}</td>
                                    </tr>
                                    <tr class="text-center">
                                        <th></th>
                                        <th>연도 - 분기</th>
                                        <th>목표실적액(원)</th>
                                        <th>달성실적액(원)</th>
                                        <th>완료비율(%)</th>
                                        <th colspan="2">수정 / 삭제</th>
                                    </tr>`;
                        }
                        v_html += `<tr class="text-center">
                                    <td></td>
                                    <td>${item.kpi_year} - ${item.kpi_quarter}분기</td>
                                    <td>${Number(item.kpi_index).toLocaleString('en')}</td>
                                    <td>${Number(item.sum_result_price).toLocaleString('en')}</td>
                                    <td>${item.result_pct}</td>
                                    <td class="text-center"><button type="button" class="buttonBorder" onclick="kpiEdit('${item.kpi_no}')" style="margin-right: 10px; background-color: #99ccff;">수정</button></td>
                                    <td class="text-center"><button type="button" class="buttonBorder" onclick="kpiDelete('${item.kpi_no}', '${item.kpi_year}', '${item.kpi_quarter}')" style="margin-right: 10px; background-color: #ffcc99;">삭제</button></td>
                                    <input type="hidden" id="fk_dept_id" value="${item.fk_dept_id}">
                                </tr>`;
                    });//end of $.each(json, function(index, item) }) -----------------------
                    $("table#kpi").html(v_html);
                }
                else {
                    v_html += `
                            <tr class="text-center">
                                <th>등록한 목표실적이 존재하지 않습니다.</th>
                            </tr>
                            <input type="hidden" id="fk_dept_id" value="${fk_dept_id}">
                            `;
                    $("table#kpi").html(v_html);
                }
            },
            error: function(request, status, error){
                alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
            }
        });

    });// end of $("select[name='searchYear']").on("change", function() {}) --------------------

});// end of $(document).ready(function() {}) ---------------------


// Function Declaration
// === 목표실적 삭제하기 === ///
function kpiDelete(kpi_no, kpi_year, kpi_quarter) {
    Swal.fire({
        title: "목표실적 삭제",
        icon : "question",
        text : kpi_year +"년 "+ kpi_quarter +"분기 목표 실적을 삭제하시겠습니까?",
        showCancelButton: true,
        confirmButtonText: "삭제",
        cancelButtonText: "취소",
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
    })
    .then((result) => {
        if (result.isConfirmed){	// 등록확인 버튼 클릭 시
            $.ajax({
                url : $("input#path").val()+"/api/kpi/delete",
                data: {"kpi_no": kpi_no},
                type: "DELETE",
                dataType: "JSON",
                success: function(json) {
                    /* 
                        {"result": 1} or {"result": 0}
                    */
                    if (json.result == 1) {
                        Swal.fire({
							title: '목표실적을 삭제했습니다.',        // Alert 제목
							text: '삭제된 목표실적 : '+  kpi_year +"년 "+ kpi_quarter +"분기",
							icon: 'success',
                            confirmButtonText: "확인"
						})
                        .then((result) => {
                            location.href="javascript:history.go(0)";
                        });
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

}// end of function kpiDelete(kpi_no) -------------------


// === 목표실적 수정하기 === //
function kpiEdit (kpi_no) {
    
}// end of function kpiEdit (kpi_no) --------------------