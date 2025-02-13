<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
   String ctxPath = request.getContextPath();
    //     /myspring
%>

<jsp:include page="../board/GroupWareHeader.jsp" /> 



<!-- 페이지 공통 부분  -->
	<p class="bg-light text-dark" style="font-size: 20pt; font-weight: bold; padding: 1% 0 1% 3%; ">글삭제<i style='font-size:30px; margin-left: 1.5%;' class='far'>&#xf2ed;</i></p>
<!-- 페이지 공통 부분  -->
<div id="container" style="border: solid 0px red; width: 90%; margin: 5% auto;">
	<div style="display: flex;  margin-top: 2%;">
		<span style="font-size:25px; text-align: center; width: 100px; margin-right: 1%" class='fas'>글암호</span>
		<input type="password" class="form-control" id="pwd" style="width: 250px; height: 30px; margin-left: 2%;" name="pwd"/>
	</div>
</div>

 
	<div style="margin-bottom: 3%; border: solid 0px red; margin-left: 6%;">
		<button style="margin-right: 4%;" type="button" class="btn btn-danger" onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_Board'">삭제</button>
		<button type="button" class="btn btn-secondary" onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_Board'">취소</button>
	</div>


<jsp:include page="../board/GroupWarefooter.jsp" /> 