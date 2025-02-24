


$(document).ready(function() {
    // == 연도에 최근 5년 값 넣어주기 == //
    const year = new Date().getFullYear();
    let v_html = ``;
    for (let i=year; i>=year-5; i--) {
        if (i == year) {
            v_html += `<option value="">연도선택</option>`;
            v_html += `<option value='${i}'>${i}</option>`;
        }
        else{
            v_html += `<option value='${i}'>${i}</option>`;
        }
    }// end of for() ----------------
    $("select[name='searchYear']").html(v_html);


    // === 엑셀 파일 다운로드 버튼을 클릭했을 경우 === //
    $("button#excelDown").on("click", function() {
        const frm = document.filterFrm;
        frm.method = "POST";
        frm.action = $("input#path").val()+"/kpi/downloadExcelFile";
        frm.submit();
    });// end of $("button#excelDown").on("click", function() {}) ---------------------


    // === 연도 및 분기 선택 시 === //
    $("select[name='searchQuarter']").on("change", () =>{
        getKpiByDept();
    });// end of $("select[name='searchQuarter']").on("change", () =>{}) -----------------
    $("select[name='searchYear']").on("change", () =>{
        getKpiByDept();
    });// end of $("select[name='searchQuarter']").on("change", () =>{}) -----------------

});// end of $(document).ready(function() {}) --------------------------


// Function Declaration
function getKpiByDept() {
    const kpi_year     = $("select[name='searchYear']").val();
    const kpi_quarter  = $("select[name='searchQuarter']").val();
    const fk_dept_id   = $("input#fk_dept_id").val();

    $.ajax({
        url: $("input#path").val()+"/api/kpi/deptResultByYearQuarter",
        type: "GET",
        data: {"fk_dept_id" : fk_dept_id, "kpi_year": kpi_year, "kpi_quarter": kpi_quarter},
        dataType: "JSON",
        success: function(json) {
            //console.log(JSON.stringify(json));
            /*
                [{"result_no":null,"fk_emp_id":"2025009","result_name":"금호타이어 4개","result_date":"2025-02-19","result_price":"7000000","fk_kpi_no":null,"branch_name":"본사","dept_name":"인사부","name":"김펭구","grade_name":"부장","kpi_year":"2025","kpi_quarter":"1","fk_branch_no":"1","fk_dept_id":"2"}
                ,{"result_no":null,"fk_emp_id":"2025009","result_name":"펑크난타이어 4개","result_date":"2025-02-27","result_price":"600000","fk_kpi_no":null,"branch_name":"본사","dept_name":"인사부","name":"김펭구","grade_name":"부장","kpi_year":"2025","kpi_quarter":"1","fk_branch_no":"1","fk_dept_id":"2"}]
            */
            let v_html = ``;

            if (json.length == 0) {
                v_html =`
                    <tr class="text-center">
						<th>등록된 실적이 없습니다.</th>
					</tr>`;
            }
            else {
                $.each(json, function(index, item) {
                    const result_name = item.result_name;
                    
                    if (index == 0) {
                        v_html += `
                            <tr>
								<th>소속</th>
								<td colspan="6">${item.branch_name} - ${item.dept_name}</td>
							</tr>
							<tr class="text-center">
								<th></th>
								<th>사원번호</th>
								<th>사원명</th>
								<th>직급</th>
								<th>실적명</th>
								<th>실적발생일</th>
								<th>실적액</th>
							</tr>
                        `;
                    }
                    v_html += `
                        <tr class="text-center">
							<td><input type="hidden" id="fk_dept_id" value="${item.fk_dept_id}"></td>
							<td>${item.fk_emp_id}</td>
							<td>${item.name}</td>
							<td>${item.grade_name}</td>
							<td>`;
								if (result_name.length > 30){
                                    v_html += result_name.substring(0, 27) + "...";
                                }
                                else {
                                    v_html += result_name;
                                }
                    v_html += `
                            </td>
                            <td>${item.result_date}</td>
                            <td>${Number(item.result_price).toLocaleString('en')}</td>
                        </tr>
                    `;
                });// end of $.each(json, function(index, item) {}) ----------------------
            }
            $("table#kpi").html(v_html);
        },
        error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }
    });

}// end of function getKpiByDept() -------------------
