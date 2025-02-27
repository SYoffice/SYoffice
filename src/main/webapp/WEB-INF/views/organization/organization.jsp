<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String ctxPath = request.getContextPath();
%>

<!-- jQuery & Bootstrap -->
<script type="text/javascript" src="<%=ctxPath%>/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="<%=ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css">

<!-- Font Awesome -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

<!-- jsTree 라이브러리 -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.3.2/themes/default/style.min.css" />
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.3.2/jstree.min.js"></script>

<!-- 조직도 CSS -->
<link rel="stylesheet" type="text/css" href="<%=ctxPath%>/css/organization/organization.css" />

<div id="jojikdo">
    <div class="card">
        <div class="card-header">
            <i class="fa-solid fa-sitemap"></i>&nbsp;조직도
        </div>
        <div class="card-body tree-container">
            <div id="jstree"></div>
        </div>
    </div>
</div>


<script>
$(document).ready(function() {
	
    $.ajax({
        url: "<%=request.getContextPath()%>/organization/list",
        type: "GET",
        dataType: "json",
        success: function(json) {
        //	console.log(JSON.stringify(json));
        
        	/*
        	[{"DEPT_NAME":"임원진","EMP_ID":2025001,"MAIL":"seoyh@syoffice.syo","HIRE_DATE":"2025-02-14","GRADE_NAME":"대표이사","TEL":"01012341234","FK_DEPT_ID":1,"FK_GRADE_NO":1,"BRANCH_NAME":"본사","FK_BRANCH_NO":1,"NAME":"서영학"}
        	,{"DEPT_NAME":"인사부","EMP_ID":9999,"MAIL":"admin@syoffice.syo","HIRE_DATE":"2025-02-11","GRADE_NAME":"대표이사","TEL":"01012341234","FK_DEPT_ID":2,"FK_GRADE_NO":1,"BRANCH_NAME":"본사","FK_BRANCH_NO":1,"NAME":"관리자"}
        	,{"DEPT_NAME":"인사부","EMP_ID":2025002,"MAIL":"eomjh@syoffice.syo","HIRE_DATE":"2025-02-14","GRADE_NAME":"이사","TEL":"01098765432","FK_DEPT_ID":2,"FK_GRADE_NO":2,"BRANCH_NAME":"본사","FK_BRANCH_NO":1,"NAME":"엄정화"}
        	,{"DEPT_NAME":"회계부","EMP_ID":2025004,"MAIL":"kangkc@syoffice.syo","HIRE_DATE":"2025-02-17","GRADE_NAME":"사원","TEL":"01088998899","FK_DEPT_ID":4,"FK_GRADE_NO":8,"BRANCH_NAME":"본사","FK_BRANCH_NO":1,"NAME":"강감찬"}
        	,{"DEPT_NAME":"영업부","EMP_ID":1000,"MAIL":"leess@syoffice.syo","HIRE_DATE":"2025-02-13","GRADE_NAME":"대표이사","TEL":"01011112222","FK_DEPT_ID":5,"FK_GRADE_NO":1,"BRANCH_NAME":"본사","FK_BRANCH_NO":1,"NAME":"이순신"}
        	,{"DEPT_NAME":"기획부","EMP_ID":2025005,"MAIL":"iyou@syoffice.syo","HIRE_DATE":"2025-02-17","GRADE_NAME":"대리","TEL":"01082838485","FK_DEPT_ID":6,"FK_GRADE_NO":6,"BRANCH_NAME":"본사","FK_BRANCH_NO":1,"NAME":"아이유"}
        	,{"DEPT_NAME":"마케팅부","EMP_ID":2025003,"MAIL":"test@syoffice.syo","HIRE_DATE":"2025-02-17","GRADE_NAME":"사원","TEL":"01043214321","FK_DEPT_ID":7,"FK_GRADE_NO":8,"BRANCH_NAME":"본사","FK_BRANCH_NO":1,"NAME":"실험용"}
        	,{"DEPT_NAME":"판매부","EMP_ID":20250411,"MAIL":"kimth@syoffice.syo","HIRE_DATE":"2025-02-18","GRADE_NAME":"대리","TEL":"01012341234","FK_DEPT_ID":8,"FK_GRADE_NO":6,"BRANCH_NAME":"강남지점","FK_BRANCH_NO":2,"NAME":"김태희"}]
        	*/
        	
            let jsonData = convertToJsTreeFormat(json); // 데이터를 jsTree 형식으로 변환

            $('#jstree').jstree({
                'core': {
                    "animation": 0,
                    "check_callback": true,
                    'data': jsonData,
                    "themes": { "dots": false }
                },
                "plugins": ["types"],
                "types": {
                    "executive": { "icon": "fa-solid fa-user-tie" },
                    "department": { "icon": "fa-solid fa-building" },
                    "person": { "icon": "fa-solid fa-user" }
                }
            });
            
            
            
            // 사원 클릭 시 모달 띄우기
            $('#jstree').on("select_node.jstree", function(e, data) {
                let nodeId = data.node.id;
				// console.log(nodeId);
                
                if (nodeId) {  // 사원을 선택한다면
                    let empInfo = json.find(emp => + emp.EMP_ID == nodeId);
	
                   
                    
                    if (empInfo) {
                    	
                    	$("#userImage").attr("src", `<%= ctxPath %>/resources/profile/\${empInfo.PROFILE_IMG || "기본이미지.png"}`);
                        $("#name").val(empInfo.NAME);
                        $("#department").val(empInfo.DEPT_NAME);
                        $("#grade").val(empInfo.GRADE_NAME);
                        $("#mail").val(empInfo.MAIL);
                        $("#tel").val(formatNumber(empInfo.TEL));
                        $("#hire_date").val(empInfo.HIRE_DATE); 
					

                        // 모달 띄우기
                        $("#userModal").modal("show");
                    }
                }
            });
            
        },
        error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
         }
        
    });// end of $.ajax({})-----------------------------------------------------------------
    
    
    
});// end of $(document).ready(function() {}---------------------------------------------------

		
// 전화번호 - 긋기		
function formatNumber(Number) {
    return Number.replace(/(\d{3})(\d{4})(\d{4})/, "$1-$2-$3");
}	
		

// jsTree 형식으로 변환
function convertToJsTreeFormat(data) {
    let result = [];  
    let dept = {}; // 부서 
    let branch = {}; // 지점
    let personType = "";

    data.forEach(item => {
        let branchId = item.FK_BRANCH_NO; // 지점 ID
        let deptId = `\${item.FK_BRANCH_NO}_\${item.FK_DEPT_ID}`; // 부서 ID (판매부서 하나로 강남, 강북지점 나눠야되서 지점번호_부서번호 로 구분)
        let empId = item.EMP_ID; // 직원 ID 
        let gradeId = item.FK_GRADE_NO; // 직급 ID
        let managerId = item.MANAGER_ID; // 매니저 ID
        
        // console.log(branchId);
        // console.log(deptId);
        // console.log(empId);
        // console.log(gradeId);
        // console.log(managerId);
        
        if(gradeId < 4 || managerId == empId){  // 대표,전무,부장이랑 매니저는 넥타이맨 아이콘
        	personType = "executive";
        }
        else{ // 나머지는 그냥 유저 아이콘
        	personType = "person";
        }

        
        
     	// 지점 추가
        if (!branch[branchId]) {	  
        	branch[branchId] = {
                "id": branchId,
                "parent": "#",
                "text": item.BRANCH_NAME,
                "type": "department",
                "state": { "opened": true }
            };
            result.push(branch[branchId]);
        }

        
        
     	// 부서 추가
        if (!dept[deptId]) { 
        	dept[deptId] = {
                "id": deptId,
                "parent": branchId,
                "text": item.DEPT_NAME,
                "type": "department"
            };
            result.push(dept[deptId]);
        }

     	
     	
        // 사원 추가
        result.push({	 
            "id": empId,
            "parent": deptId,
            "text": item.NAME + " " + item.GRADE_NAME + "",
            "type": personType
        });
    });

    return result;
    
}// end of function convertToJsTreeFormat(data) {}-----------------------------------------------------------



</script>
