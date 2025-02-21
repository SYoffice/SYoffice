<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
   
<%
	String ctxPath = request.getContextPath();
%>

<link rel="stylesheet" type="text/css" href="<%=ctxPath%>/css/employee/pwdChange.css" />
<%-- Optional JavaScript --%>
<script type="text/javascript" src="<%=ctxPath%>/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="<%=ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js" ></script>



<script>

	let pwdCheckVaild = false;
	
	$(document).ready(function() {
		
		$("div#error").hide();
		
		// 비밀번호가 6글자 이하 일 때
		$(document).on("blur", "input[name='newPwd']", function(e) {
			
			const newPwd = $("input[name='newPwd']").val().trim();
			
			console.log("입력한 비밀번호 : " + newPwd);
			
			if(!checkPwd(newPwd)) {
				$("div#error").show();
				pwdCheckVaild = false;
			}
			else {
				$("div#error").hide();
				pwdCheckVaild = true;
			}
		});	

	});// end of $(document).ready(function() -----
	
	// 새 비밀번호 유효성 검사
	function checkPwd(newPwd){
		// 공백없이 6글자 이상
		const regExp_newPwd = /^\S{6,}$/;
		return regExp_newPwd.test(newPwd);
	}
	
	// 변경하기 버튼 클릭 	
	function go_update() {
		
	    const newPwd = $("input[name='newPwd']").val().trim();
	    const confirmPwd = $("input[name='confirmPwd']").val().trim();

	    // 비밀번호 유효성 검사
	    if (!pwdCheckVaild) {
	        return;
	    }

		// 비밀번호 중복 검사
	    if (newPwd !== confirmPwd) {
	        alert("비밀번호가 일치하지 않습니다.");
	        $("input[name='confirmPwd']").val("").focus();
	        return;
	    }

	    const frm = document.updateFrm;
	    frm.action = "/syoffice/employee/pwdChange";
	    frm.method = "post";
	    frm.submit();
	}// end of function go_updatd() -----
</script>

<div id="container">
	<form name="updateFrm">
		<div id="main">		
			<div id="box">	
			    <div class="inputBox">
			        <div class="inputText">변경할 비밀번호</div>
			        <input type="password" id="newPwd" name="newPwd" placeholder="비밀번호 (6자 이상)" required>
			    </div>
			    
			    <div id="error">비밀번호는 6자 이상이어야 합니다. 다시 입력해주세요</div>
		    </div>
		    <div class="inputBox">
		        <div class="inputText">변경할 비밀번호 확인</div>
		        <input type="password" id="confirmPwd" name="confirmPwd" placeholder="비밀번호 확인" required>
		    </div>
	    	
	    	<%-- 비밀번호 변경이 필요한 상태일 때 --%>
	    	<c:if test="${sessionScope.loginuser.pwdchangestatus == 1}">
		    	<div id="buttonBox">
				    <button type="button" id="cancle_button" onclick="location.href='${pageContext.request.contextPath}/index'">메인으로</button>
					<button type="button" id="update_button" onclick="go_update()">변경하기</button>
				</div>
			</c:if>
			<%-- 비밀번호 변경이 필요하지 않은 상태일 때 --%>
			<c:if test="${sessionScope.loginuser.pwdchangestatus == 0}">
				<div id="buttonBox">
					<button type="button" id="update_button2" onclick="go_update()">변경하기</button>
				</div>
			</c:if>
			
				
		</div>
	</form>
</div>