<%@ page language="java" contentType="text/html; charset=UTF-8"%>
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

<!-- 공통 CSS -->
<link rel="stylesheet" type="text/css" href="<%=ctxPath%>/css/common/common.css" />

<style>
    body {
        background-color: #f8f9fa;
    }
    .container-fluid {
        padding: 20px;
    }

    /* 날짜 네비게이션 */
    .date-nav {
        display: flex;
        justify-content: center;
        align-items: center;
        font-size: 24px;
        font-weight: bold;
        margin-bottom: 20px;
    }
    .date-nav button {
        background: none;
        border: none;
        font-size: 28px;
        cursor: pointer;
        color: #333;
    }

    /* 근태 요약 박스 */
    .summary-row {
        display: flex;
        gap: 20px;
        justify-content: center;
        margin-bottom: 20px;
    }
    .summary-box {
        flex: 1;
        padding: 15px;
        background: white;
        border-radius: 8px;
        border: 1px solid #ddd;
        text-align: center;
        font-size: 20px;
        font-weight: bold;
    }

    /* 테이블 스타일 */
    .table-container {
        background: white;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0px 0px 10px rgba(0,0,0,0.1);
    }
    .table thead {
        background-color: #f8f9fa;
        font-weight: bold;
        border-bottom: 2px solid #ddd;
    }
    .table th, .table td {
        text-align: center;
        border: none;
    }
</style>

<!-- ✅ 헤더 유지 -->
<jsp:include page="../main/header.jsp"/>

<div class="container-fluid">
    <div class="row">
        <!-- ✅ 왼쪽 조직도 유지 -->
        <div class="col-3">
            <jsp:include page="../attendance/att_org.jsp"/>
            
        </div>

        <!-- ✅ 가운데 날짜 선택 -->
        <div class="col-9">
            <div class="date-nav">
                <button onclick="prevDate()">◀</button>
                <span id="currentDate">${date}</span>
                <button onclick="nextDate()">▶</button>
            </div>

            <!-- ✅ 근태 요약 -->
            <div class="summary-row">
                <div class="summary-box">출근 미체크 <br> <span id="checkInMiss">-</span></div>
                <div class="summary-box">퇴근 미체크 <br> <span id="checkOutMiss">-</span></div>
                <div class="summary-box">결근 <br> <span id="absentCount">-</span></div>
            </div>

            <!-- ✅ 근태 내역 -->
            <div class="table-container">
                <h5>근태 내역</h5>
                <table class="table">
                    <thead>
                        <tr>
                            <th>부서원</th>
                            <th>부서명</th>
                            <th>날짜</th>
                            <th>출근</th>
                            <th>퇴근</th>
                            <th>결근</th>
                        </tr>
                    </thead>
                    <tbody id="attendanceTableBody">
                        <!-- AJAX로 데이터 로드 -->
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script>
$('#jstree').on("select_node.jstree", function(e, data) {
    let nodeId = data.node.id;
    let nodeOriginal = data.node.original;

    if (nodeId.startsWith("att_")) {
        let deptName = nodeOriginal.deptName;
        let branchName = nodeOriginal.branchName;
        console.log("부서 근태 내역 조회 요청:", { dept_name: deptName, branch_name: branchName });
        loadDeptAttendance(deptName, branchName);
    }
});




    function loadDeptAttendance(deptName, branchName) {
        console.log("📢 부서 근태 내역 조회 요청:", { dept_name: deptName, branch_name: branchName });

        $.ajax({
            url: "<%=ctxPath%>/attendance/dataByDept",
            data: {
                dept_name: deptName,
                branch_name: branchName,
                date: $("#currentDate").text()
            },
            dataType: "json",
            success: function (json) {
                console.log("받은 근태 데이터:", json);
                /*
                0:{DEPT_ID: 1, EMP_NAME: '서영학', DEPT_NAME: '임원진', EMP_ID: 2025001, GRADE_NO: 1, …}
					1: {DEPT_ID: 1, EMP_NAME: '김영학', DEPT_NAME: '임원진', EMP_ID: 2025015, GRADE_NO: 2, …}
					2:{DEPT_ID: 1, EMP_NAME: '박영학', DEPT_NAME: '임원진', EMP_ID: 2025016, GRADE_NO: 2, …}
					3:{DEPT_ID: 1, EMP_NAME: '이영학', DEPT_NAME: '임원진', EMP_ID: 2025017, GRADE_NO: 2, …}
					4:{DEPT_ID: 1, EMP_NAME: '강이훈', DEPT_NAME: '임원진', EMP_ID: 2025021, GRADE_NO: 2, …}
                
                */

                let tbody = $("#attendanceTableBody");
                tbody.empty(); // 기존 데이터 삭제

                if (json.length === 0) {
                    tbody.append("<tr><td colspan='6'>📢 근태 데이터 없음</td></tr>");
                    return;
                }

                json.forEach(att => {
                    tbody.append(`
                        <tr>
                            <td>${att.empName || "-"}</td>
                            <td>${att.deptName || "-"}</td>
                            <td>${att.attendDate || "-"}</td>
                            <td>${att.attendStart ? att.attendStart : "-"}</td>
                            <td>${att.attendEnd ? att.attendEnd : "-"}</td>
                            <td>${att.attendStatus == 4 ? "⭕" : "❌"}</td>
                        </tr>
                    `);
                });
            },
            error: function (xhr, status, error) {
                console.error("AJAX 요청 실패:", status, error);
                alert("근태 데이터 불러오기 실패");
            }
        });
    }


   
</script>

 
