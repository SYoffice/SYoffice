<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%
   String ctxPath = request.getContextPath();
   //     /syoffice 
%>      
<jsp:include page="../approval/sidebar.jsp" />
<script src="${pageContext.request.contextPath}/js/approval/obtain_approval_box.js"></script>
<div class="common_title mb-80">결재대기문서함</div>
            <div class="approval_wrapper">
                <div class="contents">
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
                        <tbody id="obtain_approval_box_list">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>