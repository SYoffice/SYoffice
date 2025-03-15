<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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

<!-- 조직도 -->
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
    let today = new Date().toISOString().split('T')[0];  // 'YYYY-MM-DD' 형식으로 오늘 날짜 가져오기

    $.ajax({
        url: "<%=request.getContextPath()%>/attendance/list",
        type: "GET",
        data: { date: today },  // ✅ 기존 요청에 날짜 추가
        dataType: "json",
        success: function(json) {
            console.log("✅ 받은 데이터:", json);
            if (!json || json.length === 0) {
                console.error("❌ 조직도 데이터 없음!");
                alert("조직도 데이터를 불러오지 못했습니다.");
                return;
            }

            let jsonData = convertToJsTreeFormat(json);

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
        },
        error: function(request, status, error) {
            console.error("❌ AJAX 요청 실패:", status, error);
            alert("조직도 데이터를 불러올 수 없습니다.");
        }
    });
});

      

function convertToJsTreeFormat(data) {
    let result = [];  
    let dept = {}; // 부서 저장
    let branch = {}; // 지점 저장

    data.forEach(item => {
        let branchName = item.BRANCH_NAME ? item.BRANCH_NAME.trim() : null;
        let deptName = item.DEPT_NAME ? item.DEPT_NAME.trim() : null;
        let attendDate = item.ATTEND_DATE || "-";
        let attendStart = item.ATTEND_START || "-";
        let attendEnd = item.ATTEND_END || "-";
        let attendStatus = item.ATTEND_STATUS || "0";

        if (!branchName || !deptName) {
            console.error("❌ BRANCH_NAME 또는 DEPT_NAME이 없음", item);
            return;
        }

        let branchId = `branch_${branchName}`;
        let deptId = `dept_${branchName}_${deptName}`;

        // 지점 추가
        if (!branch[branchId]) {     
            branch[branchId] = {
                "id": branchId,
                "parent": "#",
                "text": branchName,
                "type": "department",
                "state": { "opened": true }
            };
            result.push(branch[branchId]);
        }

        // ✅ 부서 추가
        if (!dept[deptId]) { 
            dept[deptId] = {
                "id": deptId,
                "parent": branchId,
                "text": deptName,
                "type": "department"
            };
            result.push(dept[deptId]);

            // ✅ 부서 근태 현황 노드 추가
            result.push({
                "id": `att_${deptId}`,
                "parent": deptId,
                "text": "📊 부서 근태 현황",
                "original": { 
                    "deptName": deptName, 
                    "branchName": branchName,
                    "attendDate": attendDate,
                    "attendStart": attendStart,
                    "attendEnd": attendEnd,
                    "attendStatus": attendStatus
                }
            });
        }
    });

    console.log("✅ 변환된 조직도 데이터:", result);
    return result;
}



</script>
