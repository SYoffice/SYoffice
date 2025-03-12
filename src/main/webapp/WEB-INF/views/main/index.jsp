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
             날씨 박스 
           
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
                            <thead style="background-color:#333; color:#fff;">
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
                                            <a href="${ctxPath}/notice/view?notice_no=${notice.notice_no}">
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
document.addEventListener('DOMContentLoaded', function () {
    //  데이터가 비어 있을 경우를 대비한 예외 처리
    var weeklyPerformance = [];

    var departmentPerformance = [];
    
    // departmentPerformance 배열 추가 (수정된 부분)
    departmentPerformance.push({ dept_name: '', total_performance: 0 });
    

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

    

    // 부서 내 직원별 실적 지분 데이터 생성
    var departmentSharePerformance = [
        <c:forEach var="data" items="${departmentPerformance}" varStatus="status">
            { name: '${data.EMP_NAME}', y: ${data.PERFORMANCE_SHARE} }<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    // 부서 내 직원별 실적 지분 (파이 차트)
    if (departmentSharePerformance.length > 0) {
        Highcharts.chart('salesTrendChart', {
            chart: { type: 'pie' },
            title: { text: '부서 내 직원별 실적 비율' },
            series: [{
                name: '실적 지분 (%)',
                data: departmentSharePerformance
            }]
        });
    } else {
        document.getElementById('salesTrendChart').innerHTML = "<p style='color:gray;'>데이터 없음</p>";
    }

});

</script>
