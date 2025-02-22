<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="./sidebar.jsp" />

<%-- custom CSS --%>
<link href='<%= request.getContextPath() %>/css/kpi/kpi_edit.css'rel='stylesheet' />

<script type="text/javascript">
	$(document).ready(function() {
		
		// == 연도에 최근 5년 값 넣어주기 == //
	    const year = new Date().getFullYear();
	    let v_html = ``;
	    for (let i=year; i>=year-5; i--) {
	    	if (i == year) {
				v_html += `<option value="">선택하세요</option>`;
	 	        v_html += `<option value='\${i}'>\${i}</option>`;
	    	}
	    	else{	    		
	 	        v_html += `<option value='\${i}'>\${i}</option>`;
	    	}
	    }// end of for() ----------------
	    
	    
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
	    
	    // === 불러온 값 넣어주기 === //
	    $("select[name='kpi_year']").html(`<option value='${requestScope.kpivo.kpi_year}'>${requestScope.kpivo.kpi_year}</option>`);			// 목표실적 연도
	    $("select[name='kpi_quarter']").html(`<option value='${requestScope.kpivo.kpi_quarter}'>${requestScope.kpivo.kpi_quarter}분기</option>`);	// 목표실적 분기
	    $("input#inputKpi").val(Number("${requestScope.kpivo.kpi_index}").toLocaleString('en'));		// 목표 실적액
	    $("input[name='kpi_index']").val("${requestScope.kpivo.kpi_index}");		// 작성 시 실제 전송 될 태그
	    
	    
	 	// == 수정 버튼 클릭 시 == //
	    $("button#edit").on("click", function() {
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
	                	const frm = document.kpiFrm;
	                    frm.action = $("input#path").val()+"/kpi/edit";
	                    frm.method = "POST";
	                    frm.submit();
	                }

	            },
	            error: function(request, status, error){
	                alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	            }
	        });
	    });// end of $("button#register").on("click", function() {}) ---------------
	    
	});// end of $(document).ready(function() {}) --------------------- 
</script>

	
    <div class="contents_wrapper">
        <div class="contents_inner_wrapper">
        	<div id="formBox">
        	<div style="margin: 4% 0 4% 0">
		    	<span class="h3">목표실적 수정</span>
	   		</div>
	   		
				<form name="kpiFrm">
					<table id="kpi" class="table table-bordered">
						<tr>
							<th>소속</th>
							<td colspan="3">
								본사-인사부
							</td>
							
						</tr>
						<tr>
							<th>연도</th>
							<td>
								<select style="width: 90px;" name="kpi_year"></select>
							</td>
							<th>분기</th>
							<td>
								<select style="width: 89px;" name="kpi_quarter"></select>
							</td>
						</tr>
						<tr>
							<th>목표실적액</th>
							<td colspan="3">
								<input type="text" size="39" id="inputKpi" /><span style="margin-left: 18px;">원</span>
								<input type="hidden" name="kpi_index" />
							</td>
						</tr>
					</table>
					<input type="hidden" value="${sessionScope.loginuser.fk_dept_id}" name="fk_dept_id"/>
					<input type="hidden" value="${requestScope.kpivo.kpi_no}" name="kpi_no"/>
				</form>
				<div style="float: right;">
					<%-- <button type="button" id="register" class="border-1 rounded-md" style="margin-right: 10px;">등록</button>--%>
					<button type="button" class="buttonBorder" id="edit" style="margin-right: 10px; background-color: #99ccff;">수정</button>
					<button type="button" class="buttonBorder" style="background-color: ecf0f8;" onclick="javascript:history.back()">취소</button> 
				</div>
			</div>
		</div>
	</div>
</div>



<%-- JS 활용 용도 --%>
<input type="hidden" id="path" 	value="${pageContext.request.contextPath}" />