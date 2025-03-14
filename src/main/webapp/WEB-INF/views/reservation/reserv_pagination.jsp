<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<%-- 페이지네이션  --%>
<%-- li 태그 안의 a 태그의 주소값만 수정해주시면 됩니다. --%>

<nav class="text-center">
	<ul class="pagination">
		<!-- 첫 페이지  -->
		<div class="pageBtn_box">
			<li><a href="${href}&curPage=1" data-page="1"><span aria-hidden="true"><img class="pageBtn" src="${pageContext.request.contextPath}/images/icon/go_first.svg" /></span></a></li>
			<!-- 이전 페이지 -->
			<c:if test="${pagingDTO.firstPage ne 1}">
				<li><a href="${href}&curPage=${i}?curPage=${pagingDTO.firstPage-1}" data-page="${pagingDTO.firstPage-1}"><span aria-hidden="true"><img class="pageBtn" src="${pageContext.request.contextPath}/images/icon/prev.svg" /></span></a></li>
				
			</c:if>
		</div>
		
		<div id="pageNo_box">
			<!-- 페이지 넘버링  -->
			<c:forEach begin="${pagingDTO.firstPage}" end="${pagingDTO.lastPage}" var="i" >
				
				<c:if test="${pagingDTO.curPage ne i}">
					<li><a class="pageNo"  href="${href}&curPage=${i}" data-page="${i}">${i}</a></li>
				</c:if>
				
				<c:if test="${pagingDTO.curPage eq i}">
					<li class="active"><a class="pageNo active" href="#">${i}</a></li>
				</c:if>
				
			</c:forEach>
		</div>
		
		<!-- 다음  페이지  -->
		<div class="pageBtn_box">
			<c:if test="${pagingDTO.lastPage ne pagingDTO.totalPageCount}">
				<li><a href="${href}&curPage=${pagingDTO.lastPage+1}" data-page="${pagingDTO.lastPage+1}"><span aria-hidden="true"><img class="pageBtn" src="${pageContext.request.contextPath}/images/icon/next.svg" /></span></a></li>
			</c:if>
			
			<!-- 마지막 페이지 -->
			<li><a href="${href}&curPage=${pagingDTO.totalPageCount}" data-page="${pagingDTO.totalPageCount}"><span aria-hidden="true"><img class="pageBtn" src="${pageContext.request.contextPath}/images/icon/go_last.svg" /></span></a></li>
		</div>
	</ul>
</nav>
<%-- 페이지네이션 --%>