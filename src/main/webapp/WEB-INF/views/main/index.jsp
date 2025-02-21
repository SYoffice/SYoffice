<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp" />

<%
    String ctxPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <!-- index CSS -->
    <link rel="stylesheet" type="text/css" href="<%=ctxPath%>/css/index/index.css" >
    <style>
        /* 버튼들이 옆으로 나란히 */
        .button-area {
            display: flex;
            flex-direction: row;
            gap: 10px;
        }
        /* 버튼 내부 텍스트를 중앙 정렬 */
        .button-area button {
            display: inline-flex;       
            align-items: center;        
            justify-content: center;    
            min-width: 60px;           
            white-space: nowrap;        
        }
    </style>
</head>
<body>
    
    <div class="container-fluid content-wrapper">
        <div class="row-flex">
            <!-- 왼쪽 컬럼 -->
            <div class="col-3 left-col">           
                    <!-- 로그인한 사용자 이름 출력 -->
                    <h4>${loginUser.name} </h4>                 
                    <div class="button-area">
                        <button>메일함</button>
                        <button>결재함</button>
                    </div>
                    
                    <div class="calendar-area">
                        <h4>2025.02</h4>
                        <div class="calendar-box">
                            달력 표시
                        </div>
                    </div>
                </div>
            

            <!-- 오른쪽 컬럼 -->
            <div class="right-col">
                <div class="info-box">
                    <!-- 오늘 일정 -->
                    <div class="schedule-box">
                        <h4>오늘 일정 3건</h4>
                        <p>등록된 일정이 없습니다.</p>
                    </div>
                    <!-- 공지사항 -->
                    <div class="notice-box">
                        <h4>공지사항</h4>
                        <p>새로운 공지사항이 없습니다.</p>
                    </div>
                    <!-- 차트 -->
                    <div class="chart-box">                  
                        <div class="chart-area">
                            <h4>차트</h4>
                            차트 표시
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
