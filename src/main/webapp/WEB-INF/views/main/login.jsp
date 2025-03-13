<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>  

<%
	String ctxPath = request.getContextPath();
%>
<%-- 직접 만든 CSS --%>
<link rel="stylesheet" type="text/css" href="<%=ctxPath%>/css/login/login.css" />

<%-- Optional JavaScript --%>
<script type="text/javascript" src="<%=ctxPath%>/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="<%=ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js" ></script>

<%-- 직접 만든 JS --%>
<script type="text/javascript" src="<%=ctxPath%>/js/login/login.js"></script>




<div id="container">

	<div id="loginBox">
		
		<div id="main">
		
			<div id="logoBox">
				<img  src="<%=ctxPath%>/images/login_logo.png"/>
			</div>
			
			<form name="loginFrm">
				<div id="empIdBox">
					<input type="text" name="emp_id" id="emp_id" placeholder="아이디(사번)"/>
				</div>
				
				<div id="passwordBox">
					<input type="password" name="password" id="password" placeholder="비밀번호"/>
				</div>
				
				<div id="loginBtnBox">
					<button type="button" id="btnLogin">로그인</button>
				</div>
			</form>
			
		</div>
	</div>

</div>    