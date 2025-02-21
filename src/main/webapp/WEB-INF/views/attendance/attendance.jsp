<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="../main/header.jsp" />

<%
    // contextPath 설정
    String ctxPath = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>근태관리</title>
    
    <!-- jQuery, Bootstrap -->
    <script src="<%=ctxPath%>/js/jquery-3.7.1.min.js"></script>
    <script src="<%=ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js"></script>
    <link href="<%=ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css" rel="stylesheet" />

    <!-- FullCalendar CSS & JS -->
    <link href="<%=ctxPath%>/fullcalendar_5.10.1/main.min.css" rel="stylesheet" />
    <script src="<%=ctxPath%>/fullcalendar_5.10.1/main.min.js"></script>
    <script src="<%=ctxPath%>/fullcalendar_5.10.1/ko.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.24.0/moment.min.js"></script>

    <!-- SweetAlert2 (필요 시) -->
    <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11.16.1/dist/sweetalert2.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.16.1/dist/sweetalert2.all.min.js"></script>
    
    <!-- Custom CSS (scheduleIndex.css 등) -->
    <link href="<%=ctxPath%>/css/schedule/scheduleIndex.css" rel="stylesheet" />

     <style>
        body {
            background-color: #f8f9fa;
        }
        .row-flex {
            display: flex;
        }
        .left-col {
            margin-top: 20px;
            flex: 0 0 15%;
            max-width: 15%;
            border: 1px solid #ddd;
            padding: 20px;
            text-align: center;
            background-color: #EAF0FF;
        }
        .time-display {
            font-size: 2em;
            font-weight: bold;
            margin-bottom: 10px;
        }
        .user-name {
            font-size: 1.2em;
            margin-bottom: 10px;
        }
        .time-info {
            margin-bottom: 15px;
            font-size: 0.95em;
            color: #666;
        }
        .btn-area {
            display: flex;
            gap: 10px;
            justify-content: center;
            margin-top: 20px;
        }
        .btn-area button {
            width: 80px;
        }
        .right-col {
            flex: 0 0 85%;
            max-width: 85%;
            padding: 20px;
            display: flex;
            flex-direction: column;
            min-height: calc(100vh - 80px);
        }
        .nav-tabs {
            margin-bottom: 20px;
        }
        /* Summary 영역 스타일 */
        .summary-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 15px;
        }
        .summary-box {
            flex: 1;
            margin: 0 5px;
            background-color: #fff;
            border: 1px solid #ddd;
            text-align: center;
            padding: 10px;
        }
        .date-navigation {
            text-align: center;
            margin-bottom: 15px;
        }
        /* 캘린더 영역 스타일 */
        .chart-area {
            background-color: #ffffff;
            border: 1px solid #ddd;
            padding: 20px;
            margin-bottom: 20px;
            text-align: center;
            color: #999;
        }
        #calendar {
            max-width: 900px;
            margin: 0 auto;
        }
        /* 버튼 색상 */
        .btn-active {
            background-color: #BFD2FA;
            border-color: #BFD2FA;
        }
        .btn-disabled {
            background-color: gray;
            border-color: gray;
        }
    </style>
</head>
<body>

<div class="container-fluid">
    <div class="row-flex">
        <!-- 왼쪽 컬럼 (출퇴근 정보) -->
        <div class="col-3 left-col">
            <!-- 실시간 시계 표시 -->
            <div class="time-display" id="clockArea"></div>
            <!-- 로그인된 사용자 이름 표시 -->
            <div class="user-name">
                <c:choose>
                    <c:when test="${not empty sessionScope.loginuser}">
                        ${sessionScope.loginuser.name} ${employee.grade_name}
                    </c:when>
                    <c:otherwise>
                        로그인 필요
                    </c:otherwise>
                </c:choose>
            </div>
            <!-- 오늘의 출근/퇴근 시간 표시 -->
            <div class="time-info">
                출근시간:
                <c:choose>
                    <c:when test="${not empty attendanceVO.attendStart}">
                        <fmt:formatDate value="${attendanceVO.attendStart}" pattern="HH:mm:ss"/>
                        <c:if test="${attendanceVO.attendStatus == 2}">
                            (지각)
                        </c:if>
                    </c:when>
                    <c:otherwise>N/A</c:otherwise>
                </c:choose>
               
                퇴근시간:
                <c:choose>
                    <c:when test="${not empty attendanceVO.attendEnd}">
                        <fmt:formatDate value="${attendanceVO.attendEnd}" pattern="HH:mm:ss"/>
                    </c:when>
                    <c:otherwise>N/A</c:otherwise>
                </c:choose>
            </div>
  			<hr>
            <!-- 출근/퇴근 버튼 -->
            <div class="btn-area">
                <!-- 출근 버튼 -->
                <form method="post" action="<%=ctxPath%>/attendance/check">
                    <c:choose>
                        <c:when test="${canCheckIn}">
                            <button class="btn btn-active" type="submit" name="action" value="checkIn">출근</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-disabled" type="submit" name="action" value="checkIn" disabled>출근</button>
                        </c:otherwise>
                    </c:choose>
                </form>
                <!-- 퇴근 버튼 -->
                <form method="post" action="<%=ctxPath%>/attendance/check">
                    <c:choose>
                        <c:when test="${canCheckOut}">
                            <button class="btn btn-active" type="submit" name="action" value="checkOut">퇴근</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-disabled" type="submit" name="action" value="checkOut" disabled>퇴근</button>
                        </c:otherwise>
                    </c:choose>
                </form>
            </div>
        </div>

      
        <div class="col-9 right-col">
            <ul class="nav nav-tabs">
                <li class="nav-item">
                    <a class="nav-link active" data-toggle="tab" href="#attHistory">근태 내역</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="#leaveHistory">연차 내역</a>
                </li>
            </ul>

 
            <div class="tab-content">
                <div class="tab-pane fade show active" id="attHistory">
                    <div class="summary-row">
                        <div class="summary-box">
                             <div>이번주 누적</div>
								<div>
								    ${weeklyAccumulated}
								</div>
                        </div>
                        <div class="summary-box">
                            <div>월간 누적</div>
                            	<div>							   
									${monthlyAccumulated}
								</div>
                        </div>
                        <div class="summary-box">
						    <div>추가 근무</div>
						    	<%
						    	// monthlyAccumulated를 가져오기
						        Object monthlyObj = pageContext.getAttribute("monthlyAccumulated");

						        // Null 체크 및 기본값 설정
						        double monthly = (monthlyObj != null) ? Double.parseDouble(monthlyObj.toString()) : 0.0;

						        // 209시간을 뺀 값 계산
						        double calculatedHours = monthly - 209;

						        // 결과 값이 0 미만일 경우 0으로 처리
						        if (calculatedHours < 0) {
						            calculatedHours = 0;
						        }

						        // 시간 및 분 변환
						        int hours = (int) calculatedHours;
						        int minutes = (int) Math.round((calculatedHours - hours) * 60);
								%>
								<div> <%= hours %>h <%= minutes %>m</div> 
						</div>
                    </div>
                    
                    <!-- 차트 영역 (캘린더) -->
                    <div class="chart-area">
                        <div id="calendar"></div>
                    </div>

                    <h4>근태 차트</h4>
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>근무일자</th>
                                <th>출근시간</th>
                                <th>퇴근시간</th>
                                <th>근태상태</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="att" items="${attendanceList}">
                                <tr>
                                    <td><fmt:formatDate value="${att.attendDate}" pattern="yyyy-MM-dd"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty att.attendStart}">
                                                <fmt:formatDate value="${att.attendStart}" pattern="HH:mm:ss"/>
                                                <c:if test="${att.attendStatus == 2}">
                                                     (지각)
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>N/A</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty att.attendEnd}">
                                                <fmt:formatDate value="${att.attendEnd}" pattern="HH:mm:ss"/>
                                            </c:when>
                                            <c:otherwise>N/A</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- 연차 -->
                <div class="tab-pane fade" id="leaveHistory">
                    <div class="summary-row">
                        <div class="summary-box">
                            <div>총 연차</div>
                           <div>${totalLeave}</div>
                        </div>
                        <div class="summary-box">
                            <div>사용 연차</div>
                            <div>${usedLeave}</div>
                        </div>
                        <div class="summary-box">
                            <div>잔여 연차</div>
                          <div>${remainingLeave}</div>
                        </div>
                    </div>
                    <h4>연차 내역</h4>
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>연차사용기간</th>
                                <th>이름</th>
                                <th>연차 종류</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="leave" items="${leaveList}">
                                <tr>
                                    <td><fmt:formatDate value="${leave.leaveDate}" pattern="yyyy-MM-dd"/></td>
                                    <td>${leave.leaveType}</td>
                                    <td>${leave.leaveReason}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div> <!-- tab-content -->
        </div> <!-- right-col -->
    </div> <!-- row-flex -->
</div> <!-- container-fluid -->


<script>
    function updateClock() {
        var now = new Date();
        var hours = (now.getHours() < 10 ? "0" : "") + now.getHours();
        var minutes = (now.getMinutes() < 10 ? "0" : "") + now.getMinutes();
        var seconds = (now.getSeconds() < 10 ? "0" : "") + now.getSeconds();
        document.getElementById('clockArea').textContent = hours + ":" + minutes + ":" + seconds;
    }
    updateClock();
    setInterval(updateClock, 1000);
</script>


<script>
document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        height: 'auto',
        locale: 'ko',
        events: function(fetchInfo, successCallback, failureCallback) {
            $.ajax({
                url: '<%=ctxPath%>/attendance/calendarEvents',
                dataType: 'json',
                data: {
                    empId: '<c:out value="${sessionScope.loginuser.emp_id}" />'
                },
                success: function(response) {
                    var events = [];

                    response.forEach(function(item) {
                        var backgroundColor = "#007bff"; // 파란색  
                        var titleText = "";
                        
                       // 출근 
                        if (item.attendStart) {
                            if (item.attendStatus == 2) { // 지각
                            	titleText = `지각 ${item.attendStart}`;
                                backgroundColor = "#ffcc00"; // 노란색
                            } else { // 정상 출근
                                titleText = `출근 ${item.attendStart}`;
                                backgroundColor = "green"; // 초록색 
                            }
                            events.push({
                                title: titleText,
                                start: item.attendStart,
                                backgroundColor: backgroundColor
                            });
                        }

                        // 퇴근 
                        if (item.attendEnd) {
                            events.push({
                                title: `퇴근 ${item.attendEnd}`,
                                start: item.attendEnd,
                                backgroundColor: "#007bff" // 파란색  
                            });
                        }

                       // 결근 
                        if (!item.attendStart && !item.attendEnd) {
                            events.push({                          
                                title: "결근",
                                start: item.attendDate,
                                backgroundColor: "#ff4d4d" // 빨간색
                            });
                        }
                    });
                    successCallback(events);
                },
                error: function(request, status, error){
                    alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
                 }
            });
        }
    });

    calendar.render();
});



$(document).ready(function(){
    $.ajax({
        url: '<%=ctxPath%>/attendance/getLeaveList', // 연차 데이터를 반환하는 엔드포인트
        type: 'GET',
        dataType: 'json',
        data: {
            empId: '<c:out value="${sessionScope.loginuser.emp_id}" />'
        },
        success: function(response){
            // 연차 요약 영역 업데이트
            $('#totalLeave').text(response.totalLeave);
            $('#usedLeave').text(response.usedLeave);
            $('#remainingLeave').text(response.remainingLeave);
            
            // 연차 내역 테이블 업데이트
            var tbody = $('#leaveTable tbody');
            tbody.empty(); 
            $.each(response.leaveList, function(index, leave){
               
                var row = $('<tr/>');
                row.append($('<td/>').text(leave.leaveDate));
                row.append($('<td/>').text(response.userName)); 
                row.append($('<td/>').text(leave.leaveType));
                tbody.append(row);
            });
        },
        error: function(xhr, status, error){
            console.error("연차 내역을 가져오는 중 오류 발생: " + error);
        }
    });
});


</script>


</body>
</html>
