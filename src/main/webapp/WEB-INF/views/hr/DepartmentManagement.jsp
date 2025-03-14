<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
   
<%
	String ctxPath = request.getContextPath();
%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/hr/DepartmentManagement.css" />
<jsp:include page="../hr/sidebar.jsp" />
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.1/font/bootstrap-icons.css">
<script type="text/javascript">
$(document).ready(function() {
	
	// 엔터 누를 시 검색
	$("input:text[name='searchWord']").on("keydown", e => {
		if (e.keyCode == 13) {
			goSearch();
		}
	});// end of $("input:text[name='searchWord']").on("keydown", e => {}) -----	
	
	//검색 시 검색조건 및 검색어 값 유지시키기
	if (${!empty requestScope.paraMap.searchType && !empty requestScope.paraMap.searchWord}) {
		$("select[name='searchType']").val("${requestScope.paraMap.searchType}");
		$("input:text[name='searchWord']").val("${requestScope.paraMap.searchWord}");
	}

	// 모달 겹치지 않게 설정
	$("#closeRegisterDeptModal, #cancelRegisterDeptModal").click(function() {
	       $("#registerDeptModal").hide();
	   });
	
	   // 모달 외부 클릭 시 닫기
	   $(window).click(function(event) {
	       if ($(event.target).is("#registerDeptModal")) {
	           $("#registerDeptModal").hide();
	       }
	});
	   
	
	// 신규부서 등록하기 
	$("#registerBtn").click(function(event) { 
	    let deptName = $("#deptName").val().trim();
	    let managerId = $("#managerId").val();
	    let executiveId = $("#executiveId").val();
	
	    if (deptName === "") {
	        alert("부서명을 입력해주세요.");
	        return;
	    }
	
	    if (!managerId || managerId.trim() === "") {
	        alert("부서장을 선택하세요.");
	        return;
	    }
	
	 	// 부서명 중복검사
	    $.ajax({
	        url: "${pageContext.request.contextPath}/hr/checkDeptName",
	        type: "GET",
	        data: { dept_name: deptName },
	        success: function (response) {
	            if (response.status === "1") {
	                alert("이미 존재하는 부서명입니다. 다른 이름을 입력해주세요.");
	                return;
	            } 

	            // 부서 등록
	            $.ajax({
	                url: "${pageContext.request.contextPath}/hr/RegisterDepartment", 
	                type: "POST",
	                data: {
	                    dept_name: deptName,
	                    manager_id: managerId, 
	                    executive_id: executiveId || ""
	                },
	                success: function(response) {
	                    if (response.status === "1") { 
	                        alert("부서가 성공적으로 추가되었습니다.");
	                        $("#modalBackground").hide();
	                        $("#registerDeptModal").hide();
	                        location.reload();
	                    } else {
	                        alert(response.message);
	                    }
	                },
	                error: function(request, status, error) {
	                    alert("code: " + request.status + "\nmessage: " + request.responseText + "\nerror: " + error);
	                }
	            }); // end of ajax() ----
	        },
	        error: function(request, status, error) {
	            alert("code: " + request.status + "\nmessage: " + request.responseText + "\nerror: " + error);
	        }
	    }); // end of 중복 검사 ajax() ----
	});// end of $("#registerBtn").click(function(event) ----

	
	// 부서 정보 수정하기
	$("#updateBtn").click(function(event) {
		let branch_no = $("#branch_no").val();
		let deptId = $("#editDeptId").val();
		let deptName = $("#editDeptName").val().trim();
	    let managerId = $("#editManagerId").val();
	    let executiveId = $("#editExecutiveId").val();
	    let orgDeptName = $("#orgDeptName").val();
		
	    if (deptName === "") {
	        alert("부서명을 입력해주세요.");
	        return;
	    }
	    
	    // 기존 부서명과 새로 입력받은 부서명이 다를 때만 검사
	    if(deptName != orgDeptName) {
	    	// 부서명 중복검사
		    $.ajax({
		        url: "${pageContext.request.contextPath}/hr/checkDeptName",
		        type: "GET",
		        data: { dept_name: deptName },
		        success: function (response) {
		            if (response.status === "1") {
						alert("이미 존재하는 부서명입니다. 다른 이름을 입력해주세요.");
		                return;
		            } 
		        },
		        error: function(request, status, error) {
		            alert("code: " + request.status + "\nmessage: " + request.responseText + "\nerror: " + error);
		        }
		    }); // end of 중복 검사 ajax() ----
	    }

	 	// 부서 수정
        $.ajax({
            url: "${pageContext.request.contextPath}/hr/editDepartment", 
            type: "POST",
            data: {
            	dept_id: deptId,
                dept_name: deptName,
                branch_no: branch_no,
                manager_id: managerId || "", 
                executive_id: executiveId || ""
            },
            success: function(response) {
                if (response.status === "1") { 
                    alert("부서가 성공적으로 수정되었습니다.");
                    $("#modalBackground").hide();
                    $("#editDeptModal").hide();
                    location.reload();
                } else {
                    alert(response.message);
                }
            },
            error: function(request, status, error) {
                alert("code: " + request.status + "\nmessage: " + request.responseText + "\nerror: " + error);
            }
        }); // end of ajax() ----
	    
	    
	});// end of $("#updateBtn").click(function(event) -----
			
    $("#cancelRegisterDeptModal").click(function() {
        $("#modalBackground").hide();
        $("#registerDeptModal").hide();
    });
	   
});// end of $(document).ready(function() {}) ------

function goSearch() {
	const frm = document.searchFrm;
	
	frm.method = "GET";
	frm.submit();
}// end of function goSearch() ----------------------

// 부서 별 직원목록 모달
function DepartmentEmployeeInfo(dept_id, dept_name, branch_no, branch_name) {
    if ($("#registerDeptModal").is(":visible")) {
        return; // 부서 추가 모달이 열려 있으면 직원 목록 모달을 열지 않음
    }

    $.ajax({
        url: "${pageContext.request.contextPath}/hr/DepartmentEmployeeInfo",
        type: "GET",
        data: { dept_id: dept_id, branch_no: branch_no, branch_name: branch_name },
        success: function (data) {
            let tableBody = $("#departmentEmployeesTable");
            tableBody.empty();
            $("#modalTitle").text(branch_name + " " + dept_name + " 직원 목록");

            if (!data || data.length === 0) {
                tableBody.append("<tr><td colspan='5' style='text-align:center;'>부서 직원이 없습니다.</td></tr>");
            } else {
                data.forEach(employee => {
                    let statusText = employee.status == "1" ? "재직" : "휴직";
                    let row = $("<tr>").append(
                        $("<td>").text(employee.emp_id),
                        $("<td>").text(employee.grade_name),
                        $("<td>").text(employee.name),
                        $("<td>").text(employee.mail),
                        $("<td>").text(statusText)
                    );
                    tableBody.append(row);
                });
            }

            $("#modalBackground").show(); // 공통 배경 표시
            $("#departmentEmployeesModal").show(); // 직원 목록 모달 표시
        },
        error: function (request, status, error) {
            alert("code: " + request.status + "\nmessage: " + request.responseText + "\nerror: " + error);
        },
    });
}// end of function DepartmentEmployeeInfo(dept_id, dept_name, branch_no, branch_name) -----

// 부서별 직원목록 모달 닫기
function closeModal() {
	$("#modalBackground").hide(); // 공통 배경 숨기기
    $("#registerDeptModal").hide(); // 부서 추가 모달 숨기기
    $("#departmentEmployeesModal").hide(); // 직원 목록 모달 숨기기
    $("#editDeptModal").hide();
}

///////////////////////////////////////////////////////////

// 부서 추가 모달 열기
function registerDept() {
    $("#modalBackground").show();
    $("#registerDeptModal").show();
}// end of function registerDept() -----

// 부서 수정 모달 띄우기 
function editDept(dept_id, dept_name, branch_no) {
    $.ajax({
        url: "${pageContext.request.contextPath}/hr/getDepartmentInfo",
        type: "GET",
        data: { dept_id: dept_id, dept_name: dept_name, branch_no: branch_no},
        success: function(response) {
            if (response.status === "1") {

                // response.data가 아닌 response 자체가 데이터라면 수정
                const data = response.data || response;

                // 기존 부서 정보 바인딩
                $("#orgDeptName").val(data.dept_name); 
                $("#branch_no").val(data.branch_no);	
                $("#editDeptId").val(data.dept_id);
                $("#editDeptName").val(data.dept_name);

                let selectedManagerId = data.manager_id || "";
                let selectedExecutiveId = data.executive_id || "";

                $("#editManagerId").val(selectedManagerId).change();
                $("#editExecutiveId").val(selectedExecutiveId).change();

                if(data.dept_name === "판매부") {
                    $("#editDeptName").prop("disabled", true);
                    $('label[for="editManagerId"]').text("지점장");
                    $("#editExecutiveId").prop("disabled", true);
                } else {
                    $('label[for="editManagerId"]').text("부서장");
                    $("#editExecutiveId").prop("disabled", false);
                    $("#editDeptName").prop("disabled", false);
                }

                // 모달 열기
                $("#modalBackground").show();
                $("#editDeptModal").show();

            } else {
                alert(response.message);
            }
        },
        error: function(request, status, error) {
            alert("code: " + request.status + "\nmessage: " + request.responseText + "\nerror: " + error);
        }
    });// end of ajax() ----- 
}// end of function editDept(dept_id, dept_name, branch_no) ------



//부서 삭제
function deleteDept(dept_id) {
    if (!confirm("정말 이 부서를 삭제하시겠습니까?\n※ 해당 부서에 소속된 직원이 있다면 삭제할 수 없습니다.")) {
        return;
    }

    $.ajax({
        url: "${pageContext.request.contextPath}/hr/deleteDepartment", 
        type: "POST",
        data: { dept_id: dept_id },
        success: function(response) {
            if (response.status === "1") {
                alert("부서가 성공적으로 삭제되었습니다.");
                location.reload(); // 목록 새로고침
            } else {
                alert(response.message); // 에러 메시지 출력
            }
        },
        error: function(request, status, error) {
            alert("code: " + request.status + "\nmessage: " + request.responseText + "\nerror: " + error);
        }
    });
}

</script>

	<span class="h2"><i class="bi bi-building"></i> 부서관리</span>
	
	<form name="searchFrm">
		<div id="formContainer">
			<select name="searchType" class="form-select">
				<option value="branch_name">소속지점</option>
				<option value="dept_name">부서명</option>
				<option value="manager_name">부서장</option>
			</select>
			
			<div class="search-container">
				<input type="text" name="searchWord" size="50" autocomplete="off" placeholder="Search..." /> 
				<button type="button" class="btn btn-secondary btn-sm search-btn" onclick="goSearch()"><i class="fas fa-search"></i></button>
			</div>
			
			<button type="button" id="registerDeptBtn" onclick="registerDept()">신규 부서등록</button>
		</div>			
	</form>
	
	
	
	<table class="table table-bordered table-hover">
		<thead>
			<tr style="background-color: #e6eeff;">
	 			<th style="width: 12%;">소속지점</th>
	            <th style="width: 15%;">부서명</th>
	            <th style="width: 10%;">직원 수</th>
	            <th style="width: 15%;">부서장</th>
	            <th style="width: 25%;">부서장 사내메일</th>
	            <th style="width: 12%;">관리</th>
			</tr>
		</thead>
		
		<tbody>
			<c:forEach items="${requestScope.departmentList}" var="dept">
				<tr id="main" >
					<td onclick="DepartmentEmployeeInfo('${dept.dept_id}', '${dept.dept_name}', '${dept.branch_no}', '${dept.branch_name}')">${dept.branch_name}</td>
					<td onclick="DepartmentEmployeeInfo('${dept.dept_id}', '${dept.dept_name}', '${dept.branch_no}', '${dept.branch_name}')">${dept.dept_name}</td>
					<td onclick="DepartmentEmployeeInfo('${dept.dept_id}', '${dept.dept_name}', '${dept.branch_no}', '${dept.branch_name}')">${dept.employee_count} 명</td>
					<td onclick="DepartmentEmployeeInfo('${dept.dept_id}', '${dept.dept_name}', '${dept.branch_no}', '${dept.branch_name}')">${empty dept.manager_name ? '공석' : dept.manager_name} ${dept.manager_grade}</td>
					<td onclick="DepartmentEmployeeInfo('${dept.dept_id}', '${dept.dept_name}', '${dept.branch_no}', '${dept.branch_name}')">${dept.manager_email}</td>
					<td>
						<div class="btn-container">
					        <button type="button" class="btn btn-sm btn-primary" id="editBtn" onclick="editDept('${dept.dept_id}', '${dept.dept_name}', '${dept.branch_no}')">수정</button>
			                <button type="button" class="btn btn-sm btn-danger" id="deleteBtn" onclick="deleteDept('${dept.dept_id}')">삭제</button>
					    </div>
		            </td>
				</tr>
			</c:forEach>
		</tbody>
    </table>
    
    <%-- 부서별 직원 목록 모달창 --%>
	<div id="departmentEmployeesModal">
	    <span id="modalTitle" class="h3"></span>
	    <table class="table table-bordered">
	        <thead class="thead-light">
	            <tr>
	                <th>사번</th>
	                <th>직급</th>
	                <th>이름</th>
	                <th>사내메일</th>
	                <th>근무 상태</th>
	            </tr>
	        </thead>
	        <tbody id="departmentEmployeesTable">
	        <%-- Ajax 로 내용 추가 --%>
	        </tbody>
	    </table>
	</div>
	 <%--모달 뒤의 배경 --%>
	<div id="modalBackground"  onclick="closeModal()"></div>

	
	<%-- 부서 추가 모달창 --%>
	<div id="registerDeptModal">
	    <div class="modal-content">
	        <h2>신규 부서 추가</h2>
	
	        <form id="registerDeptForm">
	            <!-- 부서명 입력 -->
				<div class="form-group">
				    <label for="deptName">부서명</label>
				    <input type="text" id="deptName" name="deptName" placeholder="부서명을 입력하세요" required>
				</div>
	
	            <%-- 부서장 선택 --%>
	            <div class="form-group">
	                <label for="managerId">부서장</label>
	                <select id="managerId" name="managerId">
	                    <option value="">선택 안 함</option>
	                    <c:forEach items="${managerList}" var="manager">
					        <option value="${manager.emp_id}">
					            ${manager.name} (${manager.grade_name})
					        </option>
					    </c:forEach>
	                </select>
	            </div>
	
	            <%-- 담당 임원 선택 --%>
	            <div class="form-group">
	                <label for="executiveId">담당 임원</label>
	                <select id="executiveId" name="executiveId">
	                    <option value="">선택 안 함</option>
	                    	<c:forEach items="${executiveList}" var="executive">
					        <option value="${executive.emp_id}" data-dept="${executive.fk_dept_id}" data-branch="${executive.fk_branch_no}">
					            ${executive.name} (${executive.grade_name})
					        </option>
					    </c:forEach>
	                </select>
	            </div>
	
	            <div class="btn-container">
	                <button type="button" id="cancelRegisterDeptModal" class="btn btn-secondary" onclick="closeModal()">취소</button>
	                <button type="button" class="btn btn-success" id="registerBtn">등록</button>
	            </div>
	        </form>
	    </div>
	</div>
    
    <%-- 부서 수정 모달창 --%>
	<div id="editDeptModal" style="display:none;">
	    <div class="modal-content">
	        <h2 style="font-weight: bold;">부서 수정</h2>
	
	        <form id="editDeptForm">
	            <input type="hidden" id="editDeptId" name="dept_id">
				<input type="hidden" id="branch_no" name="branch_no">
	            <div class="form-group">
	                <label for="editDeptName">부서명</label>
	                <input type="hidden" id="orgDeptName" name="orgDeptName" placeholder="부서명을 입력하세요" required>
	                <input type="text" id="editDeptName" name="dept_name" placeholder="부서명을 입력하세요" required>
	            </div>
	
	            <div class="form-group">
	                <label for="editManagerId">부서장</label>
	                <select id="editManagerId" name="manager_id">
	                    <option value="">선택 안 함</option>
	                    <c:forEach items="${allManagerList}" var="manager">
	                        <option value="${manager.emp_id}">${manager.name} (${manager.grade_name})</option>
	                    </c:forEach>
	                </select>
	            </div>
	
	            <div class="form-group">
	                <label for="editExecutiveId">담당 임원</label>
	                <select id="editExecutiveId" name="executive_id">
	                    <option value="">선택 안 함</option>
	                    <c:forEach items="${allExecutiveList}" var="executive">
	                        <option value="${executive.emp_id}">${executive.name} (${executive.grade_name})</option>
	                    </c:forEach>
	                </select>
	            </div>
	
	            <div class="btn-container">
	                <button type="button" id="cancelEditDeptModal" class="btn btn-secondary" onclick="closeModal()">취소</button>
	                <button type="button" class="btn btn-primary" id="updateBtn">수정</button>
	            </div>
	        </form>
	    </div>
	</div>


    
    