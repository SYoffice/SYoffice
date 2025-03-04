<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%
   String ctxPath = request.getContextPath();
   //     /syoffice 
%>      
<jsp:include page="../main/header.jsp" />


<div class="common_wrapper">
    <div class="side_menu_wrapper">
        <div class="side_menu_inner_wrapper">
            <span style="20pt" class="common_title">자료실</span>
			    <c:if test="${sessionScope.loginuser.fk_dept_id eq 2}">
                    <button type="button" id="toggleFolderForm">새 폴더</button>
                </c:if>
			<!-- 새 폴더 추가 -->
			<div id="folderFormContainer" style="display: none; margin-top: 10px;">
			    <form id="newFolderForm" action="${pageContext.request.contextPath}/dataroom/addCategory" method="POST">
			        <input type="text" name="categoryName" id="categoryName" placeholder="폴더 이름 입력" required>
			        <button class="submit" type="submit">확인</button>
			        <button class="cancel" type="button" id="cancelFolderForm">취소</button>
			    </form>
			</div>
			
			<ul class="side_menu_list">
			
		   <c:forEach var="category" items="${categoryList}">
	        <li style="font-size: 12pt; display: flex; justify-content: space-between; align-items: center;">
	            <a href="${pageContext.request.contextPath}/dataroom/index?data_cateno=${category.data_cateno}">
	                ${category.data_catename}
	            </a>
	            
                
	            <form id="delFloderForm" action="${pageContext.request.contextPath}/dataroom/deleteCategory" method="POST">
	                <input type="hidden" name="data_cateno" value="${category.data_cateno}">
	                <c:if test="${sessionScope.loginuser.fk_dept_id eq 2}">
	                <button type="submit" onclick="return confirm('정말 삭제하시겠습니까?');">X</button>
	                </c:if>
	            </form>
	            
	        </li>

   		   </c:forEach>
				
				 <c:if test="${sessionScope.loginuser.fk_dept_id eq 2}">
				    <form action="${pageContext.request.contextPath}/dataroom/uploadFile" method="post" enctype="multipart/form-data">
				        <input type="hidden" name="data_cateno" value="${param.data_cateno}" />
				        <button type="submit" >파일 업로드</button>
				        <input style=" margin-top: 10%;" type="file" name="uploadFile" required>
				    </form>
				 </c:if>

				
			</ul>
        </div>
    </div>
    <div class="contents_wrapper">
        <div class="contents_inner_wrapper">
        
        