<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
   String ctxPath = request.getContextPath();
   //     /myspring 
%>        
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">


  <%-- Bootstrap CSS --%>
  <link rel="stylesheet" type="text/css" href="<%=ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css" >
 
  <%-- Font Awesome 6 Icons --%>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

  <%-- Optional JavaScript --%>
  <script type="text/javascript" src="<%=ctxPath%>/js/jquery-3.7.1.min.js"></script>
  <script type="text/javascript" src="<%=ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js" ></script>

  <script type="text/javascript" src="<%=ctxPath%>/smarteditor/js/HuskyEZCreator.js" charset="utf-8"></script> 

  <%-- 스피너 및 datepicker 를 사용하기 위해 jQueryUI CSS 및 JS --%>
  <link rel="stylesheet" type="text/css" href="<%=ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.min.css" />
  <script type="text/javascript" src="<%=ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.min.js"></script>

  <%-- === jQuery 에서 ajax로 파일을 업로드 할때 가장 널리 사용하는 방법 : ajaxForm === --%> 
  <script type="text/javascript" src="<%=ctxPath%>/js/jquery.form.min.js"></script> 


<title>그룹웨어 게시판 header(검색바) 만들기</title>
</head>

<script type="text/javascript">

$(document).ready(function(){
	
	
	
});

	

</script>

<style>
   div#container {
      /* border: solid 1px gray; */
      width: 20%;
      margin: 0 0 0 1%;
      
   }
   
   div#containerWrite {
      /* border: solid 1px red; */
      margin: auto;
      text-align: center;
   }
   
   div#containerAllBoard,
   div#containerDepartmentBoard {
      /* border: solid 1px orange; */
      margin: 30% 10% 80% 10%;
   }
   
   span#searchBar {
      /* border: solid 1px blue; */
      margin-left: 50%;
      cursor: pointer;
   }
   
</style>


<body>



<div style="display:flex;">
   <!-- 게시판 검색바 만들기 시작-->
   <div style="width: 100%;">
	   <form name="searchFrm" style="display: flex; gap: 5px; margin-top: 2%; height: 30px;  width: 100%;">
		    <span class="dropdown" id="searchBar">
		        <select class="form-select" name="searchType" style="height: 30px;">
		            <option value="subject">제목</option>
		            <option value="name">작성자</option>
		            <option value="content">내용</option>
		            <option value="subject_content">제목+내용</option>
		        </select>
		     </span>
	   </form> 
	   
	   
	   <%-- === 검색어 입력시 자동글 완성하기 1 === --%>
	  <div id="displayList" style="border:solid 1px gray; border-top:0px; height:50px; margin-left:60.5%; margin-top:-1px; margin-bottom:30px; margin-right: 10.9%; overflow:auto;">
	  </div>
  
   <hr style="border-color: #e6ecff; margin: 5% 0 0 0;">
   <!-- 게시판 검색바 만들기 끝-->   
