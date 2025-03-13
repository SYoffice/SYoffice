<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



<jsp:include page="header.jsp" />

<%
    String ctxPath = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>그룹웨어 메인</title>
    <link rel="stylesheet" type="text/css" href="<%=ctxPath%>/css/index/index.css" />
    <script src="<%= ctxPath%>/Highcharts-10.3.1/code/highcharts.js"></script>
    <script src="<%= ctxPath%>/Highcharts-10.3.1/code/modules/exporting.js"></script>
    <script src="<%= ctxPath%>/Highcharts-10.3.1/code/modules/accessibility.js"></script>
</head>
<body>
    <div class="common_wrapper">
        <!--  왼쪽 사이드바 -->
        <div class="side_menu_wrapper">
            <div class="profile-box">
                <div id="profile-image">
                    <%-- 등록된 프로필 사진이 있을 때 --%>
                    <c:if test="${not empty sessionScope.loginuser.profile_img}">
                        <img src="${pageContext.request.contextPath}/resources/profile/${sessionScope.loginuser.profile_img}" alt="프로필 사진" />
                    </c:if>
                    <%-- 등록된 프로필 사진이 없을 때 (기본 이미지) --%>
                    <c:if test="${empty sessionScope.loginuser.profile_img}">
                        <img src="${pageContext.request.contextPath}/resources/profile/기본이미지.png" alt="기본 프로필" />
                    </c:if>
                </div>
                <h3>${loginUser.name} ${sessionScope.loginuser.grade_name}</h3>
                <div class="button-area">
                    <div class="button-area">
					    <button onclick="location.href='<%=ctxPath%>/mail/box/0">메일함</button>
					    <button onclick="location.href='<%=ctxPath%>/approval/obtain_approval_box'">전자결재</button>
					</div>

                </div>
            </div>
            
            <div class="weather-box"> 
                <table class="table weather-table text-center">
                    <tr>
                        <td style="vertical-align: middle;" colspan="2">2025-03-12(수)</td>
                    </tr>
                    <tr>
                        <td style="vertical-align: middle; "><span style="color: blue;">2</span>℃ / <span style="color: red;">15</span>℃</td>
                        <td style="vertical-align: middle;">최저/최고</td>
                    </tr>
                    <tr>
                        <td style="vertical-align: middle;"><img style="width: 20px;" src="<%= ctxPath%>/images/vec/N.png">&nbsp;북동풍</td>
                        <td style="vertical-align: middle;">3.4m/s</td>
                    </tr>
                    <tr>
                        <td style="vertical-align: middle;"><i class="fa-solid fa-umbrella fa-sm"></i> 30%</td>
                        <td style="vertical-align: middle;"><i class="fa-solid fa-droplet"></i> 60%</td>
                    </tr>
                </table>

            </div>
        </div>

        <!--  오른쪽 컨텐츠 영역 -->
        <div class="contents_wrapper">     
        <!-- 공지사항 -->
            <div class="notice-box">
                <h4>공지사항</h4>
                <c:choose>
                    <c:when test="${empty noticeList}">
                        <p style="color: #ccc;">등록된 공지사항이 없습니다.</p>
                    </c:when>
                    <c:otherwise>
                        <table style="width:100%; border-collapse:collapse; text-align:center;">
                            <thead style="background-color:#e6eeff; color:black;">
                                <tr>
                                    <th style="padding:8px;">순번</th>
                                    <th style="padding:8px;">제목</th>
                                    <th style="padding:8px;">성명</th>
                                    <th style="padding:8px;">날짜</th>
                                    <th style="padding:8px;">조회수</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="notice" items="${noticeList}" varStatus="status">
                                    <tr style="border-bottom:1px solid #ccc;">
                                        <td style="padding:8px;">${status.index + 1}</td>
                                        <td style="padding:8px; text-align:left;">
                                            <a href="<%= ctxPath%>/board/viewOne">
                                            
                                                ${notice.notice_subject}
                                            </a>
                                        </td>
                                        <td style="padding:8px;">${notice.name}</td>
                                        <td style="padding:8px;">${notice.notice_regdate}</td>
                                        <td style="padding:8px;">${notice.notice_viewcount}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>  
        
        <br><br>
         
            <div class="schedule-container">
                <div class="schedule-box">
                    <h4>내 일정 <c:out value="${fn:length(mySchedule)}" />건</h4>
                    <c:choose>
                        <c:when test="${empty mySchedule}">
                            <p style="color: #ccc;">등록된 일정이 없습니다.</p>
                        </c:when>
                        <c:otherwise>
                            <ul>
                                <c:forEach var="schedule" items="${mySchedule}">
                                    <li><strong>${schedule.schedule_name}</strong></li>
                                </c:forEach>
                            </ul>
                        </c:otherwise>
                    </c:choose>
                </div>
                
                <div class="schedule-box">
                    <h4>전사 일정 <c:out value="${fn:length(deptSchedule)}" />건</h4>
                    <c:choose>
                        <c:when test="${empty deptSchedule}">
                            <p style="color: #ccc;">등록된 부서 일정이 없습니다.</p>
                        </c:when>
                        <c:otherwise>
                            <ul>
                                <c:forEach var="schedule" items="${deptSchedule}">
                                    <li><strong>${schedule.schedule_name}</strong></li>
                                </c:forEach>
                            </ul>
                        </c:otherwise>
                    </c:choose>
                </div>              
            </div>
            <br> <br>
             <!--  차트 영역 (가로 배치) -->
			<div class="chart-wrapper">
			    <div class="chart-container">
			        <h4>일주일 내 실적</h4>
			        <div id="branchSalesChart"></div>
			    </div>
			    <div class="chart-container">
			        <h4>부서별 실적 비교</h4>			       
			        <div id="salesTrendChart"></div>
			    </div>
			</div>
            
        </div>
    </div>
</body>
</html>

<style>
@charset "UTF-8";
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: "Arial", sans-serif;
  background-color: #f8f9fa;
  line-height: 1;
}

.common_wrapper {
  display: flex;
  height: calc(100vh - 65px);
}

/* 왼쪽 사이드바 */
.side_menu_wrapper {
  background-color: #ecf0f8;
  display: flex;
  flex-direction: column;
  width: 20%;
  height: 100vh;
  padding-top: 20px;
  align-items: center;
}

/* 프로필 박스 */
.profile-box {
  text-align: center;
  padding: 20px;
  border-radius: 12px;
  background: #e6eeff;
  width: 80%;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
}

/* 테두리 효과 제거 */
#profile-image img {
  width: 120px;
  height: 120px;
}

/*  프로필 텍스트 */
.profile-box h3 {
  font-size: 1.4em;
  font-weight: bold;
  margin-top: 15px;
}

/*  버튼 스타일 */
.button-area {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 10px;
}

.button-area button {
  padding: 8px 15px;
  font-size: 14px;
  border: none;
  background-color: #a3c0ff;
  cursor: pointer;
  border-radius: 8px;
  transition: all 0.2s ease-in-out;
  font-weight: bold;
  color: #333;
}

.button-area button:hover {
  background-color: #6d9cff;
  color: white;
}

/*  오른쪽 컨텐츠 */
.contents_wrapper {
  width: 80%;
  padding: 20px;
}

.schedule-container {
  display: flex;
  gap: 20px;
}

/*  일정 + 차트 */
.schedule-wrapper {
    display: flex;
    flex-direction: column;
    gap: 30px;
    width: 100%;
}

schedule-container {
    display: flex;
    gap: 20px;
    justify-content: space-between;
}

.schedule-box {
    flex: 1;
    background-color: white;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
}

/*  차트 */
.chart-container {
    width: 100%;
    background-color: white;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
}
/*  차트 컨테이너: 가로 정렬 */
.chart-wrapper {
    display: flex;
    justify-content: space-between;
    gap: 20px;  /* 차트 사이 여백 */
    width: 100%;
}

/*  각 차트 개별 스타일 */
.chart-container {
    flex: 1;
    background-color: white;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
    min-width: 45%;  /* 차트가 너무 작아지는 것 방지 */
}

/* 반응형: 작은 화면에서는 세로 정렬 */
@media (max-width: 768px) {
    .chart-wrapper {
        flex-direction: column;
    }
    .chart-container {
        width: 100%;
    }
}
h4 {
    font-size: 20px;  
    font-weight: bold;
}
.weather-box {
    width: auto; 
    height: auto; 
    border: 1px solid #000; 
    border-radius: 10px; 
    margin-top: 40px; 
}
</style>








<script>

////////////////////////////////////////////////////////////////////////////////////////////////////
document.addEventListener('DOMContentLoaded', function () {
    // 데이터가 비어 있을 경우를 대비한 예외 처리
    var weeklyPerformance = [];

    // 내 실적과 부서 실적을 위한 데이터
    var myPerformance = 0;  // 내 실적
    var departmentPerformance = 0;  // 부서 실적

    // 서버에서 전달받은 데이터로 내 실적과 부서 실적 값을 채우기
    <c:forEach var="data" items="${performanceData}" varStatus="status">
        // "내 실적"은 EMPLOYEE_PERFORMANCE
        myPerformance = ${data.EMPLOYEE_PERFORMANCE};
        // "내 부서 실적"은 DEPARTMENT_PERFORMANCE
        departmentPerformance = ${data.DEPARTMENT_PERFORMANCE};
    </c:forEach>

    // 최근 7일간 실적 (막대 그래프 데이터 추가)
    <c:forEach var="data" items="${weeklyPerformance}" varStatus="status">
        weeklyPerformance.push({
            result_day: '${data.RESULT_DAY}',
            total_performance: ${data.TOTAL_PERFORMANCE}
        });
    </c:forEach>

    //  최근 7일간 실적 (막대 그래프)
    if (weeklyPerformance.length > 0) {
        Highcharts.chart('branchSalesChart', {
            chart: { type: 'column' },
            title: { text: '최근 7일간 실적' },
            xAxis: {
                categories: weeklyPerformance.map(data => data.result_day)
            },
            yAxis: { title: { text: '실적 (단위: 금액)' } },
            series: [{
                name: '나의 실적',
                data: weeklyPerformance.map(data => data.total_performance)
            }]
        });
    } else {
        document.getElementById('branchSalesChart').innerHTML = "<p style='color:gray;'>데이터 없음</p>";
    }

    // 파이 차트 데이터 준비 (내 실적 vs 내 부서 실적)
    var chartData = [
        {
            name: '내 실적',  // 내 실적
            y: myPerformance  // 내 실적 값
        },
        {
            name: '부서 실적',  // 부서 실적
            y: departmentPerformance - myPerformance  // 부서 실적에서 내 실적을 제외한 값
        }
    ];

    console.log("차트 데이터: ", chartData);

    //  부서별 실적 비교 (파이 차트)
    if (chartData.length > 0) {
        Highcharts.chart('salesTrendChart', {
            chart: { type: 'pie' },
            title: { text: '부서실적' },
            series: [{
                name: '실적',
                data: chartData
            }]
        });
    } else {
        document.getElementById('salesTrendChart').innerHTML = "<p style='color:gray;'>데이터 없음</p>";
    }

});


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function weatherForecast() {
    $.ajax({
        url: "<%= ctxPath%>/weather/forecast",
        type: "GET",
        dataType: "JSON",
        success: function(json){
            // console.log(JSON.stringify(json));
            /*
                [{"forecast_no":null,"forecast_date":"2025-03-12 00:00","forecast_temp":"8","forecast_reh":"70","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"20","forecast_vec":"8","forecast_wsd":"1.1"},{"forecast_no":null,"forecast_date":"2025-03-12 01:00","forecast_temp":"8","forecast_reh":"75","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"20","forecast_vec":"8","forecast_wsd":"1.2"}
                ,{"forecast_no":null,"forecast_date":"2025-03-12 02:00","forecast_temp":"8","forecast_reh":"75","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"20","forecast_vec":"8","forecast_wsd":"1.1"},{"forecast_no":null,"forecast_date":"2025-03-12 03:00","forecast_temp":"8","forecast_reh":"75","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"20","forecast_vec":"8","forecast_wsd":"1.6"}
                ,{"forecast_no":null,"forecast_date":"2025-03-12 04:00","forecast_temp":"8","forecast_reh":"75","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"20","forecast_vec":"8","forecast_wsd":"1"},{"forecast_no":null,"forecast_date":"2025-03-12 05:00","forecast_temp":"7","forecast_reh":"75","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"20","forecast_vec":"8","forecast_wsd":"1.3"}
                ,{"forecast_no":null,"forecast_date":"2025-03-12 06:00","forecast_temp":"7","forecast_reh":"75","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"20","forecast_vec":"8","forecast_wsd":"1.3"},{"forecast_no":null,"forecast_date":"2025-03-12 07:00","forecast_temp":"7","forecast_reh":"75","forecast_mintemp":"5","forecast_maxtemp":null,"forecast_pop":"30","forecast_vec":"8","forecast_wsd":"1"}
                ,{"forecast_no":null,"forecast_date":"2025-03-12 08:00","forecast_temp":"7","forecast_reh":"75","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"30","forecast_vec":"8","forecast_wsd":"1.4"},{"forecast_no":null,"forecast_date":"2025-03-12 09:00","forecast_temp":"8","forecast_reh":"70","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"30","forecast_vec":"8","forecast_wsd":"1.6"}
                ,{"forecast_no":null,"forecast_date":"2025-03-12 10:00","forecast_temp":"9","forecast_reh":"65","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"30","forecast_vec":"10","forecast_wsd":"2"},{"forecast_no":null,"forecast_date":"2025-03-12 11:00","forecast_temp":"10","forecast_reh":"60","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"30","forecast_vec":"10","forecast_wsd":"2"}
                ,{"forecast_no":null,"forecast_date":"2025-03-12 12:00","forecast_temp":"10","forecast_reh":"65","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"30","forecast_vec":"10","forecast_wsd":"2.4"},{"forecast_no":null,"forecast_date":"2025-03-12 13:00","forecast_temp":"10","forecast_reh":"70","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"30","forecast_vec":"10","forecast_wsd":"2.9"}
                ,{"forecast_no":null,"forecast_date":"2025-03-12 14:00","forecast_temp":"10","forecast_reh":"80","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"30","forecast_vec":"10","forecast_wsd":"2.8"},{"forecast_no":null,"forecast_date":"2025-03-12 15:00","forecast_temp":"10","forecast_reh":"85","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"60","forecast_vec":"10","forecast_wsd":"2.7"}
                ,{"forecast_no":null,"forecast_date":"2025-03-12 16:00","forecast_temp":"10","forecast_reh":"85","forecast_mintemp":null,"forecast_maxtemp":"10","forecast_pop":"30","forecast_vec":"12","forecast_wsd":"2.7"},{"forecast_no":null,"forecast_date":"2025-03-12 17:00","forecast_temp":"9","forecast_reh":"85","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"20","forecast_vec":"10","forecast_wsd":"2.3"}
                ,{"forecast_no":null,"forecast_date":"2025-03-12 18:00","forecast_temp":"9","forecast_reh":"85","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"0","forecast_vec":"10","forecast_wsd":"1.6"},{"forecast_no":null,"forecast_date":"2025-03-12 19:00","forecast_temp":"8","forecast_reh":"80","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"0","forecast_vec":"10","forecast_wsd":"1.3"}
                ,{"forecast_no":null,"forecast_date":"2025-03-12 20:00","forecast_temp":"8","forecast_reh":"85","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"0","forecast_vec":"10","forecast_wsd":"1.1"},{"forecast_no":null,"forecast_date":"2025-03-12 21:00","forecast_temp":"7","forecast_reh":"85","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"0","forecast_vec":"12","forecast_wsd":"1.2"}
                ,{"forecast_no":null,"forecast_date":"2025-03-12 22:00","forecast_temp":"7","forecast_reh":"80","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"0","forecast_vec":"14","forecast_wsd":"1.4"},{"forecast_no":null,"forecast_date":"2025-03-12 23:00","forecast_temp":"6","forecast_reh":"70","forecast_mintemp":null,"forecast_maxtemp":null,"forecast_pop":"0","forecast_vec":"14","forecast_wsd":"1.4"}]
            */
            const now = new Date();             // 현재날짜시각

            let year  	= now.getFullYear();    // 현재연도(2025)
            let month   = now.getMonth() + 1;   // 월은 0부터 시작하므로 +1
            let date    = now.getDate();        // 현재일
            let hour    = now.getHours();
            let day     = now.getDay();

            switch (day) {
                case 0: day = "일"; break;
                case 1: day = "월"; break;
                case 2: day = "화"; break;
                case 3: day = "수"; break;
                case 4: day = "목"; break;
                case 5: day = "금"; break;
                case 6: day = "토"; break;
            }

            // 10 미만에서 앞에 0 붙이기
            month = String(month).padStart(2, '0')
            date = String(date).padStart(2, '0')
            hour = String(hour).padStart(2, '0')

            const times = year +"-"+ month +"-"+ date +" "+ hour+ ":00";

            // console.log(times);

            let today = "";
            let REH = "";       // 습도
            let TMN = "";       // 최저기온
            let TMX = "";       // 최고기온
            let POP = "";       // 강수확률
            let VEC = "";       // 풍향
            let WSD = "";       // 풍속
            let str_VEC = "";   // 풍향문자열


            let tempArr = [];
            let temptimeArr = [];
            $.each(json, function(index, item) {
                if (item.forecast_date == times) {
                    // console.log(item.forecast_date.substring(0, item.forecast_date.indexOf(' ')));
                    // 2025-03-12
                    today = item.forecast_date.substring(0, item.forecast_date.indexOf(' '));
                    REH = item.forecast_reh;
                    VEC = item.forecast_vec;
                    WSD = item.forecast_wsd;
                    POP = item.forecast_pop;
                }
                if (item.forecast_mintemp != null) {
                    TMN = item.forecast_mintemp;
                }
                if (item.forecast_maxtemp != null) {
                    TMX = item.forecast_maxtemp;
                }

                if (index % 2 == 0) {
                    // 짝수 시간대만 차트에 표시
                    tempArr.push(item.forecast_temp);
                    temptimeArr.push(item.forecast_date.substring(item.forecast_date.indexOf(' ')));
                }
            });// end of $.each(json, function(index, item) ----------------------

            switch (VEC) {
	            case '0': VEC = "S";    str_VEC = "북풍"; break;
	            case '2': VEC = "SW";   str_VEC = "북동풍"; break;
	            case '4': VEC = "W";    str_VEC = "동풍"; break;
	            case '6': VEC = "NW";   str_VEC = "남동풍"; break;
	            case '8': VEC = "N";    str_VEC = "남풍"; break;
	            case '10': VEC = "NE";  str_VEC = "남서풍"; break;
	            case '12': VEC = "S";   str_VEC = "서풍"; break;
	            case '14': VEC = "SE";  str_VEC = "북서풍"; break;
	            case '16': VEC = "S";   str_VEC = "북풍"; break;
	        }

            let v_html = `
                <table class="table weather-table text-center">
                    <tr>
                        <td style="vertical-align: middle;" colspan="2">\${today} (\${day})</td>
                    </tr>
                    <tr>
                        <td style="vertical-align: middle; "><span style="color: blue;">\${TMN}</span>℃ / <span style="color: red;">\${TMX}</span>℃</td>
                        <td style="vertical-align: middle;">최저/최고</td>
                    </tr>
                    <tr>
                        <td style="vertical-align: middle;"><img style="width: 20px;" src="<%= ctxPath%>/images/vec/\${VEC}.png">&nbsp;\${str_VEC}</td>
                        <td style="vertical-align: middle;">\${WSD} m/s</td>
                    </tr>
                    <tr>
                        <td style="vertical-align: middle;"><i class="fa-solid fa-umbrella fa-sm"></i> \${POP}%</td>
                        <td style="vertical-align: middle;"><i class="fa-solid fa-droplet"></i> \${REH}%</td>
                    </tr>
                </table>
                <div id="forecast">
                    <canvas id="forecast_chart"></canvas>
                </div>
            `;

            $("div.weather-box").html(v_html);

            // === 통계 차트 그리기 시작 === //
            const ctx = document.getElementById('forecast_chart');

            if (Chart.getChart(ctx)) {
                Chart.getChart(ctx)?.destroy();
            }

            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: temptimeArr,
                    datasets: [{
                        label: '기온(℃)',
                        data: tempArr,
                        borderWidth: 1,
                        borderColor:'rgb(0, 102, 255)'
                    },
                    ]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: false
                        }
                    }
                }
            });// end of new Chart(ctx, {}) ---------------------------

        },
        error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }

    });// end of $.ajax({}) ----
}// end of function weatherForecast()
</script>
