<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
   String ctxPath = request.getContextPath();
   //     /syoffice 
%>
<jsp:include page="../main/header.jsp" />

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">

<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>

<style type="text/css">
:root{
	font-family: "Noto Sans", serif;
	 font-optical-sizing: auto;
}

span.empty {
	float: right;
}

.text {
	margin: -1% 5% 0 0;
	border: 1px solid black;
	border-radius: 10px;
	font-size: 12px;
}

.digit {
	font-size: 14px;
	margin-left: 5%;
}
</style>


<script type="text/javascript">
	
	$(document).ready(function() {
		let trashCnt = 0;	// 휴지통메일개수
		let spamCnt  = 0;	// 스팸메일 개수
		
		$.ajax({
			url: "${pageContext.request.contextPath}/api/mail/trash",
			type: "GET",
			data: {"fk_emp_id": ${sessionScope.loginuser.emp_id}},
			success: function(json) {
				//console.log(JSON.stringify(json));
				
				/*
					[{"receive_division":4,"cnt":19}]
				*/
				
				$.each(json, function(index, item) {
					if (item.receive_division == 4) {
						trashCnt = item.cnt;
						
						let v_html = `
							<span class='trash empty text btn btn-sm'>비우기</span>
							<span class='digit'>\${item.cnt}</span>
						`;
						$("li#trash").append(v_html);
					}
					
					if (item.receive_division == 5) {
						spamCnt = item.cnt;
						
						let v_html = `
							<span class='spam empty text btn btn-sm'>비우기</span>
							<span class='digit'>\${item.cnt}</span>
						`;
						$("li#spam").append(v_html);
					} 
				});					
				
			},
			error: function(request, status, error){
                alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
            }
		});
		
		
		// === 비우기 버튼을 클릭 했을 때 이벤트 === //
		$(document).on("click", "span.empty", function() {
			const index = $("span.empty").index($(this));
			//alert(index);
			
			if (index == 0) {
				emptyMailBox(4, trashCnt);
			}
			else {
				emptyMailBox(5, spamCnt);
			}
		});// end of $(document).on("click", "span.empty", function() {}) --------------------- 
		
	});// end of $(document).ready(function() {}) -----------------------
	
	// Function Declaration
	// === 메일함의 메일을 모두 삭제하는 함수 === //
	function emptyMailBox(receive_division, emptyCnt) {
		let mailBoxName;
		if (receive_division == 4) {
			mailBoxName = "휴지통";
		}
		else {
			mailBoxName = "스팸메일함";
		}
		
		Swal.fire({
			title: mailBoxName+"을 비우시겠습니까?",
			text: "메일함을 비우면 복구할 수 없습니다.",
			icon : "question",
			showCancelButton: true,
			confirmButtonText: "확인",
			cancelButtonText: "취소",
		})
		.then((result) => {
			if (!result.isConfirmed) {  // 삭제 취소 시
	            return;
	        }
			
			$.ajax({
				url: "${pageContext.request.contextPath}/api/mail/trash",
				type: "DELETE",
				data: {"receive_division": receive_division, "fk_emp_id": ${sessionScope.loginuser.emp_id}},
				success: function(json) {
					console.log(JSON.stringify(json));
					/*
						{"result": 삭제된개수}
					*/
					if (emptyCnt == json.result) {
						Swal.fire({
                            title: mailBoxName+'을 비웠습니다.',        // Alert 제목
                            icon: 'success',
                            confirmButtonText: "확인"
                        })
                        .then((result) => {
                        	location.href = "${pageContext.request.contextPath}/mail/box/0";	// 받은 메일함으로 이동
                        });
					}
				},
				error: function(request, status, error){
	                alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	            }
			});
		});
		
	}// end of function emptyMailBox(receive_division) ------------------- 
</script>

<div class="common_wrapper">
    <div class="side_menu_wrapper">
        <div class="side_menu_inner_wrapper">
            <span class="common_title">메일함</span>
            <button type="button" onclick="location.href='${pageContext.request.contextPath}/mail/new'">메일 쓰기</button>
            <ul class="side_menu_list" id="side_menu">
                <li><a href="${pageContext.request.contextPath}/mail/box/-1"><i class="fa-regular fa-envelope"></i><span style="margin-left: 3%;">전체메일함</span></a></li>
                <li><a href="${pageContext.request.contextPath}/mail/box/0"><i class="fa-solid fa-inbox"></i><span style="margin-left: 3%;">받은메일함</span></a></li>
                <li><a href="${pageContext.request.contextPath}/mail/box/1"><i class="fa-regular fa-paper-plane"></i><span style="margin-left: 3%;">보낸메일함</span></a></li>
                <li><a href="${pageContext.request.contextPath}/mail/box/2"><i class="fa-regular fa-file"></i><span style="margin-left: 4.7%;">임시보관함</span></a></li>
                <li><a href="${pageContext.request.contextPath}/mail/box/3"><i class="fa-regular fa-file-lines"></i><span style="margin-left: 4.7%;">내게쓴메일함</span></a></li>
            </ul>
            <ul class="side_menu_list">
            	<li id="trash"><a href="${pageContext.request.contextPath}/mail/box/4"><i class="fa-regular fa-trash-can"></i><span style="margin-left: 5%;">휴지통</span></a></li>
            	<li id="spam"><a href="${pageContext.request.contextPath}/mail/box/5"><i class="fa-solid fa-ban"></i><span style="margin-left: 4%;">스팸메일함</span></a></li>
            </ul>
            
            <ul class="side_menu_list">
            	<li><a href="${pageContext.request.contextPath}/mail/setting"><i class="fa-solid fa-gear"></i><span style="margin-left: 3%;">환경설정</span></a></li>
            </ul>
        </div>
    </div>