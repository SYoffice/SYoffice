<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%
   String ctxPath = request.getContextPath();
   //     /syoffice 
%>      
<jsp:include page="../approval/sidebar.jsp" />
             <div class="common_title mb-80">전자결재</div>
             <div class="approval_wrapper">
                 <div class="contents">
                     <div class="content_title">결재대기문서</div>
                     <div class="content_sub_title">결재해야할 문서가 <span style="color: #95b6ff; font-weight: bold;">5</span>건 있습니다.</div>
                    <div class="gridbox_wrapper">
                        <div class="gridbox">
                            <div class="grid_text">
                                <div class="content_title">안녕하세요.</div>
                                <div class="ft_14">기안자 : 나선일</div>
                                <div class="ft_14">기안일 : 2025-02-11</div>
                                <div class="ft_14">종류 : 지출결의서</div>
                            </div>
                            <button type="button">결재하기</button>
                        </div>
                        <div class="gridbox">
                            <div class="grid_text">
                                <div class="content_title">안녕하세요.</div>
                                <div class="ft_14">기안자 : 나선일</div>
                                <div class="ft_14">기안일 : 2025-02-11</div>
                                <div class="ft_14">종류 : 업무품의</div>
                            </div>
                            <button type="button">결재하기</button>
                        </div>
                        <div class="gridbox">
                            <div class="grid_text">
                                <div class="content_title">안녕하세요.</div>
                                <div class="ft_14">기안자 : 나선일</div>
                                <div class="ft_14">기안일 : 2025-02-11</div>
                                <div class="ft_14">종류 : 업무품의</div>
                            </div>
                            <button type="button">결재하기</button>
                        </div>
                        <div class="gridbox">
                            <div class="grid_text">
                                <div class="content_title">안녕하세요.</div>
                                <div class="ft_14">기안자 : 나선일</div>
                                <div class="ft_14">기안일 : 2025-02-11</div>
                                <div class="ft_14">종류 : 업무품의</div>
                            </div>
                            <button type="button">결재하기</button>
                        </div>
                    </div>
                </div>
                <div class="contents">
                    <div class="content_title">기안 진행 문서</div>
                    <div class="content_sub_title">진행중인 문서가 <span style="color: #95b6ff; font-weight: bold;">5</span>건 있습니다.</div>
                    <table class="common_table">
                        <thead>
                            <tr>
                                <th style="width: 10%;">기안일</th>
                                <th style="width: 10%;">종류</th>
                                <th style="width: 80%;">제목</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>2025-02-11</td>
                                <td>지출결의서</td>
                                <td style="text-align: left;" class="flex"><span class="emergency">긴급</span> 안녕하세요.</td>
                            </tr>
                            <tr>
                                <td>2025-02-11</td>
                                <td>업무품의</td>
                                <td style="text-align: left;">안녕하세요.</td>

                            </tr>
                            <tr>
                                <td>2025-02-11</td>
                                <td>업무품의</td>
                                <td style="text-align: left;">안녕하세요.</td>
                            </tr>
                            <tr>
                                <td>2025-02-11</td>
                                <td>업무품의</td>
                                <td style="text-align: left;">안녕하세요.</td>
                            </tr>
                            <tr>
                                <td>2025-02-11</td>
                                <td>업무품의</td>
                                <td style="text-align: left;">안녕하세요.</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="contents">
                    <div class="content_title">결재 완료 문서</div>
                    <table class="common_table">
                        <thead>
                            <tr>
                                <th style="width: 10%;">기안일</th>
                                <th style="width: 10%;">종류</th>
                                <th style="width: 80%;">제목</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>2025-02-11</td>
                                <td>지출결의서</td>
                                <td style="text-align: left;">안녕하세요.</td>
                            </tr>
                            <tr>
                                <td>2025-02-11</td>
                                <td>업무품의</td>
                                <td style="text-align: left;">안녕하세요.</td>

                            </tr>
                            <tr>
                                <td>2025-02-11</td>
                                <td>업무품의</td>
                                <td style="text-align: left;">안녕하세요.</td>
                            </tr>
                            <tr>
                                <td>2025-02-11</td>
                                <td>업무품의</td>
                                <td style="text-align: left;">안녕하세요.</td>
                            </tr>
                            <tr>
                                <td>2025-02-11</td>
                                <td>업무품의</td>
                                <td style="text-align: left;">안녕하세요.</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>