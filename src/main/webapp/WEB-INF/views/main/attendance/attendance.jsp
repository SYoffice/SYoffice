<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../header.jsp" /> 

<!DOCTYPE html>

<html>
<head>
    <meta charset="UTF-8">
    <title>근태관리</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/bootstrap-4.6.2-dist/css/bootstrap.min.css">
    <style>
        body {
            margin-top: 80px; 
            background-color: #f8f9fa;
        }
        .row-flex {
            display: flex;
        }       
        .left-col {
            flex: 0 0 15%;
            max-width: 15%;
            background-color: #ffffff;
            border: 1px solid #ddd;
            padding: 20px;
            text-align: center;
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
        .btn-area button {
            width: 80px;
            margin: 5px;
        }
        .right-col {
            flex: 0 0 85%;
            max-width: 85%;
            padding: 20px;
            display: flex;
            flex-direction: column;
            min-height: calc(100vh - 80px);
        }
        .summary-boxes {
            display: flex;
            gap: 20px;
            margin-bottom: 20px;
        }
        .summary-box {
            flex: 1;
            background-color: #ffffff;
            border: 1px solid #ddd;
            padding: 15px;
            text-align: center;
            box-sizing: border-box;
        }
        .summary-box h5 {
            margin-bottom: 10px;
        }
        /* 날짜/탭 영역 */
        .date-and-tabs {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 15px;
           
        }
        .tabs-area {
            display: flex;
            gap: 10px;
        }
        .tabs-area a {
            text-decoration: none;
            color: #333;
            padding: 6px 12px;
            border: 1px solid #ddd;
            background-color: #fff;
            border-radius: 4px;
        }
        .tabs-area a:hover {
            background-color: #f0f0f0;
        }
        .tab-content-wrapper {
            display: flex;
            flex-direction: column;
            flex: 1;
        }
        /* 스케줄(달력)*/
        .schedule-area {
            background-color: #ffffff;
            border: 1px solid #ddd;
            padding: 20px;
            min-height: 800px; 
            margin-bottom: 20px;
        }
        /* 그래프 */
        .chart-area {
            background-color: #ffffff;
            border: 1px solid #ddd;
            padding: 20px;
            height: 250px; 
        }
         /* 내연차내역*/
        .vacation-block {
		    min-height: 300px;
		    background: #ffffff;
		    border: 1px solid #ddd;
		    padding: 20px;
		    box-sizing: border-box;
		    width: 100%;
		    margin-bottom: 20px;
		    height: 900px;
		}

        /* 마지막 콘텐츠에 margin-top: auto; 적용하여 아래쪽에 붙이기 */
        .last-block {
            margin-top: auto;
        }
    </style>
</head>
<body>
<!-- Bootstrap JS -->
<script src="<%=request.getContextPath()%>/js/jquery-3.7.1.min.js"></script>
<script src="<%=request.getContextPath()%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js"></script>

<%
    String tab = request.getParameter("tab");
    if(tab == null) {
        tab = "attendance";
    }
%>

<div class="container-fluid">
    <div class="row-flex">
        <!-- 왼쪽 컬럼 (출퇴근 정보) -->
        <div class="col-3 left-col">
            <div class="time-display"></div>
            <div class="user-name">서영학님</div>
            <div class="time-info">
                출근시간: 09:13:00<br>
                퇴근시간: 18:00:00
            </div>
            <div class="btn-area">
                <button class="btn btn-primary">출근</button>
                <button class="btn btn-secondary">퇴근</button>
            </div>
        </div>
        <!-- 오른쪽 컬럼 -->
        <div class="col-9 right-col">
            <!-- 상단 근태 요약 3박스 -->
            <div class="summary-boxes">
                <div class="summary-box">
                    <h5>이번주 누적</h5>
                    <p>9h 5m</p>
                </div>
                <div class="summary-box">
                    <h5>월간 누적</h5>
                    <p>9h 5m</p>
                </div>
                <div class="summary-box">
                    <h5>이번주 잔여</h5>
                    <p>9h 5m</p>
                </div>
            </div>
            
            <div class="date-and-tabs">
                <div class="tabs-area">
                    <a href="?tab=attendance">내근태현황</a>
                    <a href="?tab=vacation">내연차확인</a>
                </div>
            </div>
            
           
            <div class="tab-content-wrapper">
            <%
                if("attendance".equals(tab)) {
            %>
                <!--  내 근태 현황  -->
                 <h4>2025-02-05</h4>
                <div class="schedule-area">              
                    <p>스케줄/달력 표시 영역</p>
                </div>
                <div class="chart-area last-block">
                    <p>근태 차트 표시 영역</p>
                </div>
            <%
                } else if("vacation".equals(tab)) {
            %>
                <!-- 내 연차내역  -->
                <div class="vacation-block last-block">
                    <p>내연차내역</p>
                </div>
            <%
                }
            %>
            </div>
        </div>
    </div>
</div>


<script>
    function updateClock() {
        var now = new Date();
        var hours = (now.getHours() < 10 ? "0" : "") + now.getHours();
        var minutes = (now.getMinutes() < 10 ? "0" : "") + now.getMinutes();
        var seconds = (now.getSeconds() < 10 ? "0" : "") + now.getSeconds();
        document.querySelector('.time-display').textContent = hours + ":" + minutes + ":" + seconds;
    }
    updateClock();
    setInterval(updateClock, 1000);
</script>

</body>
</html>
