<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
	String ctxPath = request.getContextPath();
%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dataroom/index.css" />

<jsp:include page="../dataroom/sidebar.jsp" />


<script type="text/javascript">



$(document).ready(function() {
	
	// 엔터 누를 시 검색
	$("input:text[name='searchWord']").on("keydown", e => {
		if (e.keyCode == 13) {
			goSearch();
		}
	});// end of $("input:text[name='searchWord']").on("keydown", e => {}) -----	
	
	//검색 시 검색조건 및 검색어 값 유지시키기
	if (${!empty requestScope.paraMap.searchType && !empty requestScope.paraMap.searchWord}) {
		$("select[name='searchType']").val("${requestScope.paraMap.searchType}");
		$("input:text[name='searchWord']").val("${requestScope.paraMap.searchWord}");
	}
	
	if(${sessionScope.loginuser.fk_dept_id eq 2}){
	 document.getElementById("toggleFolderForm").addEventListener("click", function() {
	        let formContainer = document.getElementById("folderFormContainer");
	        formContainer.style.display = (formContainer.style.display === "none") ? "block" : "none";
	 });
	
	 
	 document.getElementById("cancelFolderForm").addEventListener("click", function() {
	        document.getElementById("folderFormContainer").style.display = "none";
	 });

	}
	
});// end of $(document).ready(function() {}) ------




function goSearch() {
	const frm = document.searchFrm;
	
	frm.method = "GET";
	frm.submit();
}// end of function goSearch() ----------------------

</script>

			<span class="h2">${selectedCategoryName}</span>
			
			<form name="searchFrm" method="get" action="${pageContext.request.contextPath}/dataroom/index">
			<input type="hidden" name="data_cateno" value="${param.data_cateno != null ? param.data_cateno : '1'}" />  
				<div id="formContainer">
					<select name="searchType" class="form-select">
 				<option value="data_orgfilename" ${requestScope.paraMap.searchType == 'data_orgfilename' ? 'selected' : ''}>파일명</option>
					</select>
					<div class="search-container">
						<input type="text" name="searchWord" size="50" autocomplete="off" value="${requestScope.paraMap.searchWord}"  /> 
						<button type="submit" class="btn btn-secondary btn-sm search-btn"><i class="fas fa-search"></i></button>
					</div>
				</div>			
			</form>
			
			
			
			
			
		<%-- 	
			<form name="searchFrm" method="get" action="${pageContext.request.contextPath}/dataroom/index" style="margin: 20px auto;">
			<div id="formContainer">
			
			    <input type="hidden" name="data_cateno" value="${param.data_cateno != null ? param.data_cateno : '1'}" />  
			    <select name="searchType" style="height: 26px;">
			       
			    </select>
			    
			    <input type="text" name="searchWord" size="50" autocomplete="off" />
			    <button type="button" class="btn btn-secondary btn-sm search-btn" onclick="goSearch()"><i class="fas fa-search"></i></button>
			    </div>		
			</form> --%>


			
			<table class="table">
    <thead>
        <tr>
            <th style="text-align: left;">파일명</th>
            <th style="text-align: center;" >파일크기</th>
            <th></th>
        </tr>
    </thead>
    <tbody>
    	<%-- 파일이 있을떄 --%>
        <c:if test="${not empty fileList}">
            <c:forEach var="file" items="${fileList}">
                <tr style="font-size: 14pt;">
                    <td>${file.data_orgfilename}</td>
                    
	                <td class="file-size">
                      <fmt:formatNumber value="${file.data_filesize / 1024.0}" pattern="#.#"/>KB
	                </td>
	                
                    <td>
                       <a href="${pageContext.request.contextPath}/dataroom/downloadFile?data_no=${file.data_no}" class="btn btn-light; border border-dark;">
		                다운로드</a>
		                
                        <c:if test="${sessionScope.loginuser.fk_dept_id eq 2}">
	                    <form action="${pageContext.request.contextPath}/dataroom/deleteFile" method="POST" style="display: inline;">
						    <input type="hidden" name="data_no" value="${file.data_no}">
						    <button type="submit" class="btn btn-danger border border-dark" onclick="return confirm('정말 삭제하시겠습니까?');">삭제</button>
						</form>
	                	</c:if>
		                
                    </td>
                    
                </tr>
            </c:forEach>
        </c:if>
        <%-- 파일이 없을떄 --%>
        <c:if test="${empty fileList}">
            <tr>
                <td colspan="3" style="text-align: center;">등록된 파일이 없습니다.</td>
            </tr>
        </c:if>
    </tbody>
</table>

           	
           	 
			<%-- 페이지네이션  --%>
			<%-- li 태그 안의 a 태그의 주소값만 수정해주시면 됩니다. --%>
			<nav class="text-center">
			    <ul class="pagination">
			        <!-- 첫 페이지 -->
			        <div class="pageBtn_box">
			            <li>
			                <a href="index?data_cateno=${param.data_cateno}&searchType=${requestScope.paraMap.searchType}&searchWord=${requestScope.paraMap.searchWord}&curPage=1" data-page="1">
			                    <span aria-hidden="true"><img class="pageBtn" src="${pageContext.request.contextPath}/images/icon/go_first.svg" /></span>
			                </a>
			            </li>
			            <!-- 이전 페이지 -->
			            <c:if test="${pagingDTO.firstPage ne 1}">
			                <li>
			                    <a href="index?data_cateno=${param.data_cateno}&searchType=${requestScope.paraMap.searchType}&searchWord=${requestScope.paraMap.searchWord}&curPage=${pagingDTO.firstPage-1}" data-page="${pagingDTO.firstPage-1}">
			                        <span aria-hidden="true"><img class="pageBtn" src="${pageContext.request.contextPath}/images/icon/prev.svg" /></span>
			                    </a>
			                </li>
			            </c:if>
			        </div>
			
			        <div id="pageNo_box">
			            <!-- 페이지 넘버링 -->
			            <c:forEach begin="${pagingDTO.firstPage}" end="${pagingDTO.lastPage}" var="i">
			                <c:if test="${pagingDTO.curPage ne i}">
			                    <li>
			                        <a class="pageNo" href="index?data_cateno=${param.data_cateno}&searchType=${requestScope.paraMap.searchType}&searchWord=${requestScope.paraMap.searchWord}&curPage=${i}" data-page="${i}">${i}</a>
			                    </li>
			                </c:if>
			                <c:if test="${pagingDTO.curPage eq i}">
			                    <li class="active">
			                        <a class="pageNo active" href="#">${i}</a>
			                    </li>
			                </c:if>
			            </c:forEach>
			        </div>
			
			        <!-- 다음 페이지 -->
			        <div class="pageBtn_box">
			            <c:if test="${pagingDTO.lastPage ne pagingDTO.totalPageCount}">
			                <li>
			                    <a href="index?data_cateno=${param.data_cateno}&searchType=${requestScope.paraMap.searchType}&searchWord=${requestScope.paraMap.searchWord}&curPage=${pagingDTO.lastPage+1}" data-page="${pagingDTO.lastPage+1}">
			                        <span aria-hidden="true"><img class="pageBtn" src="${pageContext.request.contextPath}/images/icon/next.svg" /></span>
			                    </a>
			                </li>
			            </c:if>
			
			            <!-- 마지막 페이지 -->
			            <li>
			                <a href="index?data_cateno=${param.data_cateno}&searchType=${requestScope.paraMap.searchType}&searchWord=${requestScope.paraMap.searchWord}&curPage=${pagingDTO.totalPageCount}" data-page="${pagingDTO.totalPageCount}">
			                    <span aria-hidden="true"><img class="pageBtn" src="${pageContext.request.contextPath}/images/icon/go_last.svg" /></span>
			                </a>
			            </li>
			        </div>
			    </ul>
			</nav>

			<%-- 페이지네이션 --%>
            	
			</div>
        </div>
    </div>
    
    