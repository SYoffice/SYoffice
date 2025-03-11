<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../main/header.jsp" />

<%

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
		/* FullCalendar 기본 날짜(일자) 텍스트 색상 검정색으로 변경 */
	    .fc-daygrid-day-number {
	        color: black !important;
	    }
	
	    /* FullCalendar 요일(일, 월, 화, 수, 목, 금, 토) 색상 검정색으로 변경 */
	    .fc-col-header-cell-cushion  {
	        color: black !important;
	        font-weight: bold; 
	    }
	    /*  토요일 & 일요일 날짜 빨간색 */
		.fc-day-sat a, .fc-day-sun a {
		    color: blue !important;
		    font-weight: bold;
		}
		.fc-day-sun a {
		    color: red !important;
		    font-weight: bold;
		}
		.fc-event-time /*.fc-event-title*/{
		    color: black !important;
	      
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
        <c:otherwise></c:otherwise>
    </c:choose>
    &nbsp;퇴근시간:
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


                </div>

				<!-- 연차 내역 -->
<div class="tab-pane fade" id="leaveHistory">
    <h4>연차 내역</h4>

    <!-- ✅ 사용한 연차 & 잔여 연차 정보 추가 -->
    <div class="summary-row">
        <div class="summary-box">
            <div>사용한 연차</div>
            <div>${usedLeave} 일</div>
        </div>
        <div class="summary-box">
            <div>잔여 연차</div>
            <div>${remainingLeave} 일</div>
        </div>
    </div>

    <!-- 연차 내역 테이블 -->
    <table class="table table-bordered">
        <thead>
            <tr>
                <th>연차 시작일</th>
                <th>연차 종료일</th>
                <th>연차 제목</th>
                <th>연차 사유</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="leave" items="${leaveList}">
                <tr>
                    <td><fmt:formatDate value="${leave.leaveStart}" pattern="yyyy-MM-dd"/></td>
                    <td><fmt:formatDate value="${leave.leaveEnd}" pattern="yyyy-MM-dd"/></td>
                    <td>${leave.leaveSubject}</td>
                    <td>${leave.leaveContent}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
				
				
            </div> 
        </div> 
    </div> 
</div>




<script>
document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    if (!calendarEl) {
        console.error("❌ 캘린더 컨테이너를 찾을 수 없습니다.");
        return;
    }
    console.log("✅ 캘린더 컨테이너 확인:", calendarEl);

    var currentYear, currentMonth;
    
    var calendar = new FullCalendar.Calendar(calendarEl, {
        googleCalendarApiKey: "AIzaSyASM5hq3PTF2dNRmliR_rXpjqNqC-6aPbQ", // ✅ Google API Key
        eventSources: [
            {
                googleCalendarId: 'ko.south_korea#holiday@group.v.calendar.google.com', // ✅ 한국 공휴일 연동
                color: 'white',
                textColor: 'red'
            }
        ],
        initialView: 'dayGridMonth',
        locale: 'ko',
        datesSet: function(info) {
            var midTime = (info.start.getTime() + info.end.getTime()) / 2;
            var midDate = new Date(midTime);
            currentYear = midDate.getFullYear();
            currentMonth = midDate.getMonth() + 1;
            console.log("📅 현재 연/월:", currentYear, currentMonth);

            fetchCalendarEvents(currentYear, currentMonth);
        }
    });

    function fetchCalendarEvents(year, month) {
        if (!year || !month) {
            console.error(" [ERROR] 잘못된 날짜 데이터: year=" + year + ", month=" + month);
            return;
        }
        let empId = '<c:out value="${sessionScope.loginuser.emp_id}" />';
        if (!empId || isNaN(empId)) {
            console.error("[ERROR] 유효하지 않은 empId:", empId);
            return;
        }

        console.log(" [INFO] AJAX 요청 시작: empId=" + empId + ", year=" + year + ", month=" + month);

        $.ajax({
            url: '<%=ctxPath%>/attendance/calendarEvents',
            dataType: 'json',
            data: { 
                empId: empId,
                year: year,
                month: month
            },
            success: function(response) {
                console.log(" [SUCCESS] AJAX 응답:", response);
                
                let events = [];
                let today = new Date();

                response.forEach(function(item) {
                    let eventDate = new Date(item.attendDate);
                    
                    if ((!item.attendStart || item.attendStart.trim() === "") &&
                        (!item.attendEnd || item.attendEnd.trim() === "") &&
                        item.attendStatus !== 5 && item.attendStatus !== 4) {
                        return; // 스킵
                    }

                    let event = { };

                    // 연차 표시
                    if (item.attendStatus == 5) {
                        event.title = "연차";
                        event.backgroundColor = "#800080";
                    }
                    // 과거 데이터만 결근 표시
                    else if (item.attendStatus == 6 && eventDate < today) {
                        event.title = "결근";
                        event.backgroundColor = "#ff4d4d";
                    }
                    // 출근 및 지각 처리
                    else if (item.attendStart) {
                        let titleText = (item.attendStatus == 2) ? "지각" : "출근";
                        let backgroundColor = (item.attendStatus == 2) ? "#ffcc00" : "green";
                        events.push({
                            title: titleText,
                            start: item.attendStart,
                            backgroundColor: backgroundColor
                        });
                    }
                    // 퇴근 이벤트 추가
                    if (item.attendEnd) {
                        events.push({
                            title: "퇴근",
                            start: item.attendEnd,
                            backgroundColor: "green"
                        });
                    }

                    // 최종 이벤트 추가
                    events.push(event);
                });

                // 기존 이벤트 삭제 후 새로운 데이터 추가
                calendar.removeAllEvents();
                calendar.addEventSource(events);
            },
            error: function(request, status, error) {
                console.error(" [ERROR] AJAX 요청 실패:", request.responseText);
            }
        });
    }

    calendar.render();
});
</script>








</body>

</html>