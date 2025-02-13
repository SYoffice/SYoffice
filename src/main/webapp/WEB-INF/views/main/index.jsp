<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="header.jsp" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
      
        .row-flex {
            display: flex;
            flex: 1;                
            min-height: 0;          
        }
        .left-col {
            flex: 0 0 15%;         
            max-width: 15%;
            border-right: 1px solid #ddd;
            box-sizing: border-box;
        }
        .profile-box {
            text-align: center;
            padding: 20px;
            box-sizing: border-box;
        }
        .profile-box img {
            width: 100px;
            height: 100px;
            border-radius: 50%;
        }
        .profile-box h3 {
            margin: 10px 0;
        }
        .button-area {
            margin-top: 15px;
        }
        .button-area button {
            margin: 0 5px;
            padding: 6px 12px;
            cursor: pointer;
        }
        .calendar-area {
            margin-top: 30px;
        }
        .calendar-area h4 {
            margin-bottom: 15px;
        }
        .calendar-box {
            border: 1px solid #ccc;
            padding: 15px;
            height: 250px; 
            box-sizing: border-box;
        }       
        .right-col {
            flex: 1; 
            display: flex;
            flex-direction: column;
            box-sizing: border-box;
        }      
        .info-box {
            flex: 1;  /* 세로 공간 가득 차게 */
            display: flex;
            flex-direction: column;
            padding: 20px;
            box-sizing: border-box;
        }
       
        .schedule-box{
            margin-bottom: 20px;
            border: 1px solid #ccc;  
            padding: 30px;           
            min-height: 300px;       
            box-sizing: border-box;
        }
        .notice-box {
            margin-bottom: 20px;
            border: 1px solid #ccc;  
            padding: 30px;           
            min-height: 700px;       
            box-sizing: border-box;
        }
        .schedule-box h4,
        .notice-box h4,
        .chart-box h4 {
            margin-bottom: 15px;
        } 
        .chart-box {
            margin-top: auto;     
            border-bottom: 1px solid #ddd;
            padding-bottom: 20px;
        }
        .chart-area {
            border: 1px solid #ccc;
            height: 1000px;
            box-sizing: border-box;
        }
    </style>
</head>
<body>
    
    <div class="container-fluid content-wrapper">
        <div class="row-flex">
            <!-- 왼쪽 컬럼 (15%) -->
            <div class="left-col">
                <div class="profile-box">
                     <!--<img src="<%= request.getContextPath() %>.png" alt="Profile">  -->
                    <h3>서영학 대리</h3>
                    <div class="button-area">
                        <button>메일함</button>
                        <button>결재함</button>
                    </div>
                    <!-- 달력 (설문조사시 수정) -->
                    <div class="calendar-area">
                        <h4>2025.02</h4>
                        <div class="calendar-box">
                            달력 표시
                        </div>
                    </div>
                </div>
            </div>

            <div class="right-col">
                <div class="info-box">
                    <!-- 오늘 일정(전체일정, 개인일정 나눠야함) -->
                    <div class="schedule-box">
                        <h4>오늘 일정 3건</h4>
                        <p>등록된 일정이 없습니다.</p>
                    </div>
                    <!-- 공지사항 -->
                    <div class="notice-box">
                        <h4>공지사항</h4>
                        <p>새로운 공지사항이 없습니다.</p>
                    </div>
                    <!-- 차트: margin-top: auto로 맨 아래 배치 -->
                    <div class="chart-box">
                        <h4>차트</h4>
                        <div class="chart-area">
                            차트 표시
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
