/**
 * 
 */

$(document).ready(function(){
	
	/* 로그인 처리해주는 함수 */
	const func_Login = function(){
		  
		const emp_id = $("input#emp_id").val();	// 입력한 아이디
		const password = $("input#password").val();	// 입력한 비밀번호
		  
		if(emp_id.trim() == "") {
			// 아이디에 공백 입력 시
			alert("아이디를 입력하세요!!");
			$("input#emp_id").val("");
			$("input#emp_id").focus();
			return; // 종료
		}
		  
		if(password.trim() == "") {
			// 비밀번호에 공백 입력 시
			alert("비밀번호를 입력하세요!!");
			$("input#password").val("");
			$("input#password").focus();
			return; // 종료
		}
		  
		const frm = document.loginFrm;
		  
		frm.action = "/syoffice/employee/login";
		frm.method = "post";
		frm.submit();
	  };// end of const func_Login = function(){ } ------
	
	/* 로그인 버튼 클릭 시 */
	$("button#btnLogin").click(function(){
		func_Login();	// 로그인 처리해주는 함수 실행
	});
	
	/* 비밀번호에서 엔터 입력 시 */
	$("input:password[id='password']").keydown(function(e){
		if(e.keyCode == 13) { 
			func_Login();
		}  
	});
	
});// end of $(document).ready(function(){}) -----