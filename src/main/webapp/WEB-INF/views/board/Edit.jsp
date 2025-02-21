<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
   String ctxPath = request.getContextPath();
    //     /myspring
%>
<jsp:include page="../main/header.jsp" />
<jsp:include page="../board/GroupWareHeader.jsp" /> 

<link rel="stylesheet" href="<%= ctxPath%>/css/board/common.css">


<!-- 페이지 공통 부분  -->
	<p class="bg-light text-dark" style="font-size: 20pt; font-weight: bold; padding: 1% 0 1% 3%;">글수정<i style="font-size:30px; margin-left: 1.5%;" class='fas'>&#xf12d;</i></p>
<!-- 페이지 공통 부분  -->

<div style="width: 90%;">
	<form name="BoardFrm">
	
	<i style="font-size:20px; margin-left: 5.5%; margin-top: 4%;" class='fas'>&#xf08d; 위치 :</i>
	
		<span class="dropdown">
		   <select class="form-select" name="searchType" style="height: 30px; margin: 0 0 2% 1%;">
		       <option value="subject">전체 게시판</option>
		       <option value="name">부서 게시판[부서명]</option>
		   </select>
		</span>
		
		
		<span class="dropdown">
		   <select class="form-select" name="searchType" style="height: 30px; margin: 0 0 2% 0;">
		       <option value="subject" selected disabled>카테고리</option>
		       <option value="name">신간도서</option>
		       <option value="name">오늘의 뉴스</option>
		       <option value="name">주간식단표</option>
		       <option value="name">주간식단표</option>
		   </select>
		</span>
		
		<br>
		
		<div style="display: flex; margin-left: 4.5%; margin-top: 2%;">
			<span style="font-size:20px; text-align: center; width: 60px;" class='fas'>제목</span>
			<input type="text" class="form-control" id="subject" style="width: 550px; margin-left: 2%;" name="subject"/>
		</div>
		
		<div style="margin-top: 4%; margin-left: 5%;">
			<span style="font-size:20px; text-align: center; width: 90px; margin-bottom: 3%;" class='fas'>파일첨부</span>
			
			<div class="container p-3 border" style="width: 100%; margin-left: 0;">	
				<input type="file" class="form-control-file border" style=" font-size:15px; margin-left: 1%;" name="file">
			</div>
		</div>
		
		<div style="margin-top: 4%; margin-left:5%;">
			<span style="font-size:20px; text-align: left; width: 90px; margin-bottom: 3%;" class='fas'>내용</span>
			
			<div class="container p-3 border" style="width: 100%; margin-left: 0;">	
				<textarea class="form-control-file border" style=" font-size:15px; margin-left: 0 1%; height: 500px;"></textarea>
			</div>
		</div>
		

		
		<div style="margin-top: 4%; margin-left: 5%;">
			<span style="font-size:20px; text-align: left; width: 90px; margin-bottom: 3%;" class='fas'>공개설정</span>
			
			<div class="form-check-inline" style="margin-left: 3%;">
			  <label class="form-check-label">
			    <input type="radio" class="form-check-input" name="security" value="public">공개
			  </label>
			</div>
			
			<div class="form-check-inline" style="margin-left: 4%;">
			  <label class="form-check-label">
			    <input type="radio" class="form-check-input" name="security" value="private">비공개
			  </label>
			</div>
			
			<div style="display: flex;  margin-top: 2%;">
				<span style="font-size:20px; text-align: center; width: 60px; margin-right: 5%" class='fas'>글암호</span>
				<input type="text" class="form-control" id="pwd" style="width: 200px; height: 30px; margin-left: 2%;" name="pwd"/>
			</div>
			
	    </div>
		   
		<hr style="border-color: #e6ecff; margin: 1% 0 3% 0;">
		
		<div style="text-align: center; margin-bottom: 3%;">
			<button style="margin-right: 4%;" type="button" class="btn btn-info">등록</button>
			<button type="button" class="btn btn-secondary">취소</button>
		</div>
		
	</form>
</div>









<jsp:include page="../board/GroupWarefooter.jsp" /> 