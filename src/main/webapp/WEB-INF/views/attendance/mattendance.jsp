<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String ctxPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <title>관리자 근태 & 연차</title>

    <!-- jQuery -->
    <script type="text/javascript" src="<%=ctxPath%>/js/jquery-3.7.1.min.js"></script>

    <!-- Bootstrap -->
    <script type="text/javascript" src="<%=ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

    <style>
        body { background-color: #f8f9fa; }
        .container-fluid { padding: 20px; }
        .table-container { background: white; padding: 20px; border-radius: 10px; }
        .summary-box { display: flex; justify-content: space-around; padding: 20px; background: white; border-radius: 10px; margin-top: 20px; }
        .summary-item { text-align: center; }
        .table th, .table td { text-align: center; }
        .summary-item {font-weight: bold;  /* 글씨를 두껍게 */}
    </style>
</head>

<body>
    <jsp:include page="../main/header.jsp"/>

    <div class="container-fluid">
        <div class="row">
            <div class="col-md-3">
                <label>지점 선택</label>
                <select id="branchSelect" class="form-control"></select>
            </div>
            <div class="col-md-3">
                <label>부서 선택</label>
                <select id="deptSelect" class="form-control" disabled></select>
            </div>
            <div class="col-md-3">
                <label>조회 유형</label>
                <select id="typeSelect" class="form-control" disabled>
                    <option value="attendance">근태 내역</option>
                    <option value="leave">연차 내역</option>
                </select>
            </div>
            <div class="col-md-3">
                <label>조회 날짜</label>
                <input type="date" id="selectedDate" class="form-control">
            </div>
        </div>
        <button id="searchBtn" class="btn btn-primary mt-3">조회</button>
    </div>

    <!--  근태 및 연차 요약 박스 -->
    <div class="summary-box">
        <div class="summary-item">
            <h5>출근 미체크</h5>
            <p id="missedCheckIn">0</p>
        </div>
        <div class="summary-item">
            <h5>퇴근 미체크</h5>
            <p id="missedCheckOut">0</p>
        </div>
        <div class="summary-item">
            <h5 id="statusTitle">결근</h5>
            <p id="absentOrLeave">0</p>
        </div>
    </div>

    <div class="table-container">
        <h5 id="tableTitle"></h5>
        <table class="table">
            <thead id="tableHeader">
            </thead>
            <tbody id="dataTableBody"></tbody>
        </table>
    </div>

    <script>
        $(document).ready(function() {
            let today = new Date().toISOString().slice(0, 10);  
            $("#selectedDate").val(today);

            // 지점 목록 가져오기
            $.ajax({
                url: "<%=ctxPath%>/attendance/branches",
                type: "GET",
                dataType: "json",
                success: function(branches) {
                    let branchSelect = $("#branchSelect").empty().append(`<option value="">지점 선택</option>`);
                    branches.forEach(branch => branchSelect.append(`<option value="\${branch.BRANCH_NO}">\${branch.BRANCH_NAME}</option>`));
                }
            });

            // 부서 목록 가져오기
            $("#branchSelect").change(function() {
                let branchNo = $(this).val();
                let deptSelect = $("#deptSelect").html('<option value="">부서 선택</option>').prop("disabled", true);
                $("#typeSelect").prop("disabled", true);

                if (!branchNo) return;

                $.ajax({
                    url: "<%=ctxPath%>/attendance/departments",
                    type: "GET",
                    dataType: "json",
                    data: { branch_no: branchNo },
                    success: function(departments) {
                        if (departments.length > 0) {
                            departments.forEach(dept => deptSelect.append(`<option value="\${dept.DEPT_ID}">\${dept.DEPT_NAME}</option>`));
                            deptSelect.prop("disabled", false);
                        }
                    }
                });
            });

            $("#deptSelect").change(function() {
                $("#typeSelect").prop("disabled", !$(this).val());
            });

            $("#searchBtn").click(function() {
                let deptId = $("#deptSelect").val();
                let type = $("#typeSelect").val();
                let selectedDate = $("#selectedDate").val();

                if (!deptId || !type) {
                    alert("부서와 조회 유형을 선택해주세요.");
                    return;
                }

                let url = "<%=ctxPath%>/attendance/dataByDept";
                $("#statusTitle").text(type === "attendance" ? "결근" : "연차 사용");

                $.ajax({
                    url: url,
                    type: "GET",
                    dataType: "json",
                    data: { deptId: deptId, date: selectedDate, type: type },
                    success: function(data) {
                        renderTable(data, type);
                        updateSummary(data, type);
                    }
                });
            });

            function renderTable(list, type) {
                let thead = $("#tableHeader").empty();
                let tbody = $("#dataTableBody").empty();

                if (type === "attendance") {
                    thead.append(`
                        <tr style="background-color:#e6eeff; color:black;">
                            <th>사원명</th>
                            <th>부서명</th>
                            <th>날짜</th>
                            <th>출근</th>
                            <th>퇴근</th>
                            <th>상태</th>
                        </tr>
                    `);
                } else {
                    thead.append(`
                        <tr style="background-color:#e6eeff; color:black;">
                            <th>사원명</th>
                            <th>부서명</th>
                            <th>연차 시작</th>
                            <th>연차 종료</th>
                        </tr>
                    `);
                }

                if (!list || list.length === 0) {
                    tbody.append("<tr><td colspan='6'>데이터 없음</td></tr>");
                    return;
                }

                list.forEach(item => {
                    if (type === "attendance") {
                        tbody.append(`<tr>
                        		<td>\${item.EMPNAME || "-"}</td> 
                        		<td>\${item.DEPTNAME || "-"}</td> 
                        		<td>\${item.ATTENDDATE || "-"}</td> 
                        		<td>\${item.STARTTIME || "-"}</td> 
                        		<td>\${item.ENDTIME || "-"}</td> 
                        		<td>\${item.STATUS || "-"}</td>
                        </tr>`);
                    } else {
                        tbody.append(`<tr>
                       		<td>\${item.EMPNAME || "-"}</td> 
                       		<td>\${item.DEPTNAME || "-"}</td> 
                       		<td>\${item.STARTTIME || "-"}</td> 
                    		<td>\${item.ENDTIME || "-"}</td> 
                        </tr>`);
                    }
                });
            }

            function updateSummary(data, type) {
                let missedCheckInCount = 0;
                let missedCheckOutCount = 0;
                let leaveCount = 0;

                data.forEach(item => {
                    // 출근 미체크 (출근 기록이 없는 경우)
                    if (type === "attendance" && !item.STARTTIME) {
                        missedCheckInCount++;
                    }
                    
                    // 퇴근 미체크 (퇴근 기록이 없는 경우)
                    if (type === "attendance" && !item.ENDTIME) {
                        missedCheckOutCount++;
                    }

                    // 결근 또는 연차 사용
                    if (type === "attendance" && item.STATUS === "결근") {
                        leaveCount++;
                    }
                    if (type === "leave") {
                        leaveCount++;
                    }
                });

                // 업데이트된 값들 설정
                $("#missedCheckIn").text(missedCheckInCount);
                $("#missedCheckOut").text(missedCheckOutCount);
                $("#absentOrLeave").text(leaveCount);
            }
        });
    </script>
</body>
</html>
