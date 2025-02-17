<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%
   String ctxPath = request.getContextPath();
   //     /syoffice 
%>      
<jsp:include page="../approval/sidebar.jsp" />
<div class="common_title mb-80">결재대기문서함</div>
            <div class="approval_wrapper">
                <div class="contents">
                    <div class="content_title">결재예정문서</div>
                    <div class="content_sub_title">결재예정 문서가 <span style="color: #95b6ff; font-weight: bold;">3</span>건 있습니다.</div>
                    <div class="searchsortbox">
                        <div class="select_container">
                            <select>
                                <option value="0">기안자</option>
                                <option value="1">기안부서</option>
                                <option value="2">종류</option>
                            </select>
                        </div> 
                        
                        <div class="search_container">
                            <form action="/action_page.php">
                                <input type="text" placeholder="Search.." name="search">
                                <button type="submit"><i class="fa fa-search"></i></button>
                            </form>
                        </div>
                    </div>
                    <table class="common_table">
                        <thead>
                            <tr>
                                <th style="width: 10%;">기안일</th>
                                <th style="width: 10%;">종류</th>
                                <th style="width: 60%;">제목</th>
                                <th style="width: 10%;">기안부서</th>
                                <th style="width: 10%;">기안자</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td colspan="5">결재예정문서가 없습니다.</td>
                            </tr>
                            <tr>
                                <td>2025-02-11</td>
                                <td>지출결의서</td>
                                <td style="text-align: left;" class="flex"><span class="emergency">긴급</span> 안녕하세요.</td>
                                <td>개발팀</td>
                                <td>한민정</td>
                            </tr>
                            <tr>
                                <td>2025-02-11</td>
                                <td>업무품의</td>
                                <td style="text-align: left;">안녕하세요.</td>
                                <td>운영팀</td>
                                <td>이야호</td>
                            </tr>
                            <tr>
                                <td>2025-02-11</td>
                                <td>업무품의</td>
                                <td style="text-align: left;">안녕하세요.</td>
                                <td>운영팀</td>
                                <td>이야호</td>
                            </tr>
                            <tr>
                                <td>2025-02-11</td>
                                <td>업무품의</td>
                                <td style="text-align: left;">안녕하세요.</td>
                                <td>운영팀</td>
                                <td>이야호</td>
                            </tr>
                            <tr>
                                <td>2025-02-11</td>
                                <td>업무품의</td>
                                <td style="text-align: left;">안녕하세요.</td>
                                <td>운영팀</td>
                                <td>이야호</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>