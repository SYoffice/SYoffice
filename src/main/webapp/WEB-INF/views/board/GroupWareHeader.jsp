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


<title>그룹웨어 게시판 header 만들기</title>
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

   <!-- 게시판 좌측 jsp 만들기 -->
   <div id="container">
      
	      <div class="container p-3 my-3" style="height: 100vh; border: solid 2px #d8e5f3; position: fixed; width: 17%; background-color:#ecf2f9;">
	      
	         <div id="containerWrite">
	         	<button type="button" style="font-size: 20pt; font-weight: bold; margin: 10% 0 15% 0;" class="btn btn-outline-light text-dark" id="write" onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_Write'">글쓰기</button>
	         </div>
	         
	         
	         <div id="containerAllBoard">
	            <p style="font-size: 15pt;">전체 게시판</p>
	            <ul style="list-style-type: circle;">
	               <li>
	                  <a style="font-size: 13pt;" href="">공지사항</a>
	               </li>
	            </ul>
	         </div>
	         
	         
	         <div id="containerDepartmentBoard">
	         <div >
	            <p id="departmentBoard" style="font-size: 15pt; margin-bottom: 0;">부서 게시판<div style="font-size: 10pt;">[부서명]</div></p>
	         </div>   
	            <ul style="list-style-type: circle;">
	               <li>
	                  <a style="font-size: 13pt;">공지사항</a>
	               </li>
	            </ul>
	         </div>
	      </div>
   </div>











   <!-- 게시판 검색바 만들기 -->
   <div style="width: 100%;">
	   <form action="" style="display: flex; gap: 5px; margin-top: 2%; height: 30px;  width: 100%;">
		    <span class="dropdown" id="searchBar">
		        <select class="form-select" name="dropdown_selection" style="height: 30px;">
		            <option value="subject">제목</option>
		            <option value="name">작성자</option>
		            <option value="content">내용</option>
		            <option value="subject_content">제목+내용</option>
		        </select>
		     </span>
		     
		    <input class="form-control " type="text" placeholder="검색" style="height: 30px; width: 60%;">
		 	<button class="btn btn-success" type="submit" style="display: flex; gap: 4px; text-align: center; margin-right: 2%; font-size: 10pt;">Search<i style='font-size:15px; padding-top: 5%' class='fas'>&#xf002;</i></button>
	   </form> 
	   
	   
	   <%-- === #87. 검색어 입력시 자동글 완성하기 1 === --%>
	  <div id="displayList" style="border:solid 1px gray; border-top:0px; height:50px; margin-left:60.5%; margin-top:-1px; margin-bottom:30px; margin-right: 10.9%; overflow:auto;">
	  </div>
  
   <hr style="border-color: #e6ecff; margin: 5% 0 0 0;">
      
