<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<jsp:include page="../main/header.jsp" />
    
    
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">

<%-- custom css --%>
<link href='<%= request.getContextPath() %>/css/schedule/editSchedule.css'rel='stylesheet' />

<script type="text/javascript">
	$(document).ready(function() {
		// ==== 종일체크박스에 체크를 할 것인지 안할 것인지를 결정하는 것 시작 ==== //
		// 시작 시 분
		var str_startdate = "${requestScope.map.startdate}";
	 // console.log(str_startdate); 
		// 2021-12-01 09:00
		var target = str_startdate.indexOf(":");
		var start_min = str_startdate.substring(target+1);
	 // console.log(start_min);
		// 00
		var start_hour = str_startdate.substring(target-2,target);
	 // console.log(start_hour);
		// 09
				
		
		// 종료 시 분
		var str_enddate = "${requestScope.map.enddate}";
	 //	console.log(str_enddate);
		// 2021-12-01 18:00
		target = str_enddate.indexOf(":");
		var end_min = str_enddate.substring(target+1);
	 // console.log(end_min);
	    // 00 
		var end_hour = str_enddate.substring(target-2,target);
	 //	console.log(end_hour);
		// 18
		
		
		if(start_hour=='00' && start_min=='00' && end_hour=='23' && end_min=='59' ){
			$("input#allDay").prop("checked",true);
		}
		else{
			$("input#allDay").prop("checked",false);
		}
		// ==== 종일체크박스에 체크를 할 것인지 안할 것인지를 결정하는 것 끝 ==== // 
		
		// 시작날짜 넣어주기
		target = str_startdate.indexOf(" ");
		var start_yyyymmdd = str_startdate.substring(0,target);
		$("input#startDate").val(start_yyyymmdd);
		
		// 종료날짜 넣어주기
		target = str_enddate.indexOf(" ");
		var end_yyyymmdd = str_enddate.substring(0,target);
		$("input#endDate").val(end_yyyymmdd);
		
		// 시작시간, 종료시간		
		var html="";
		for(var i=0; i<24; i++){
			if(i<10){
				html+="<option value='0"+i+"'>0"+i+"</option>";
			}
			else{
				html+="<option value="+i+">"+i+"</option>";
			}
		}// end of for----------------------
		
		$("select#startHour").html(html);
		$("select#endHour").html(html);
		
		// === *** 시작시간 시 분 넣어주기 *** === //
		$("select#startHour").val(start_hour);
		$("select#endHour").val(end_hour);
		
		// 시작분, 종료분 
		html="";
		for(var i=0; i<60; i=i+5){
			if(i<10){
				html+="<option value='0"+i+"'>0"+i+"</option>";
			}
			else {
				html+="<option value="+i+">"+i+"</option>";
			}
		}// end of for--------------------
		html+="<option value="+59+">"+59+"</option>"
		
		$("select#startMinute").html(html);
		$("select#endMinute").html(html);
		
		// === *** 종료시간 시 분 넣어주기 *** === //
		$("select#startMinute").val(start_min);
		$("select#endMinute").val(end_min);
		
		
		// '종일' 체크박스 클릭시
		$("input#allDay").click(function() {
			var bool = $('input#allDay').prop("checked");
			
			if(bool == true) {
				$("select#startHour").val("00");
				$("select#startMinute").val("00");
				$("select#endHour").val("23");
				$("select#endMinute").val("59");
				$("select#startHour").prop("disabled",true);
				$("select#startMinute").prop("disabled",true);
				$("select#endHour").prop("disabled",true);
				$("select#endMinute").prop("disabled",true);
			} 
			else {
				$("select#startHour").prop("disabled",false);
				$("select#startMinute").prop("disabled",false);
				$("select#endHour").prop("disabled",false);
				$("select#endMinute").prop("disabled",false);
			}
		});
		
		// ========== *** 캘린더선택에서 이미 저장된 캘린더 넣어주기 시작 *** ========== //
		$("select.calType").val("${requestScope.map.fk_lgcatego_no}");
		$.ajax({
			url: "${pageContext.request.contextPath}/schedule/selectSmallCategory",
			data: {"fk_lgcatego_no":"${requestScope.map.fk_lgcatego_no}", 
				   "fk_emp_id":"${requestScope.map.fk_emp_id}"},
			dataType: "json",
			async: false,  //동기방식
			success:function(json){
				var html ="";
				if(json.length>0){
					$.each(json, function(index, item){
						html+="<option value='"+item.smcatego_no+"'>"+item.smcatego_name+"</option>";
					});
					$("select.small_category").html(html);
				}
			},
			error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});
		
		$("select.small_category").val("${requestScope.map.fk_smcatego_no}");
		// ========== *** 캘린더선택에서 이미 저장된 캘린더 넣어주기 끝 *** ========== //
		
		// 내캘린더,사내캘린더 선택에 따른 서브캘린더 종류를 알아와서 select 태그에 넣어주기 
		$("select.calType").change(function(){
			var fk_lgcatego_no = $("select.calType").val();      // 내캘린더이라면 1, 사내캘린더이라면 2 이다.
			var fk_emp_id = $("input[name=fk_emp_id]").val();  // 로그인 된 사용자아이디
			
			if(fk_lgcatego_no != "") { // 선택하세요 가 아니라면
				$.ajax({
						url: "${pageContext.request.contextPath}/schedule/selectSmallCategory",
						data: {"fk_lgcatego_no":fk_lgcatego_no, 
							   "fk_emp_id":fk_emp_id},
						dataType: "json",
						success:function(json){
							var html ="";
							if(json.length>0){
								
								$.each(json, function(index, item){
									html+="<option value='"+item.smcatego_no+"'>"+item.smcatego_name+"</option>"
								});
								$("select.small_category").html(html);
								$("select.small_category").show();
							}
						},
						error: function(request, status, error){
				            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
						}
				});
			}
			else {
				// 선택하세요 이라면
				$("select.small_category").hide();
			}
		});
		
		// **** 수정하기전 이미 저장되어있는 공유자 **** 
		var stored_joinuser = "${requestScope.map.schedule_joinemp}";
		if(stored_joinuser != "공유자가 없습니다."){
			var arr_stored_joinuser = stored_joinuser.split(",");
			var str_joinuser = "";
			for(var i=0; i<arr_stored_joinuser.length; i++){
				var user = arr_stored_joinuser[i];
			//	console.log(user);
				add_joinUser(user);
			}// end of for--------------------------
		}// end of if--------------------------------
		
		// 공유자 추가하기
		$("input#joinSearchWord").bind("keyup",function(){
			var joinSearchWord = $(this).val();
			//	console.log("확인용 joinUserName : " + joinUserName);
			$.ajax({
				url: $("input#path").val()+"/schedule/insertSchedule/searchJoinUserList",
				data: {"joinSearchWord": joinSearchWord},
				dataType:"json",
				success : function(json){
					var joinUserArr = [];
				    
					//  input태그 공유자입력란에 "이" 를 입력해본 결과를 json.length 값이 얼마 나오는지 알아본다. 
					//	console.log(json.length);
					
					if(json.length > 0){
							
						$.each(json, function(index, item){
							var name = item.name;
							if(name.includes(joinSearchWord) || item.dept_name.includes(joinSearchWord) || item.branch_name.includes(joinSearchWord)){
								// name 이라는 문자열에 joinSearchWord 라는 문자열이 포함된 경우라면 true , 
	                            // name 이라는 문자열에 joinUserName 라는 문자열이 포함되지 않은 경우라면 false 
							   	joinUserArr.push(name+"("+item.branch_name+"-"+item.dept_name+")");
							}
						});
							
						$("input#joinSearchWord").autocomplete({  // 참조 https://jqueryui.com/autocomplete/#default
							source:joinUserArr,
							select: function(event, ui) {       // 자동완성 되어 나온 공유자이름을 마우스로 클릭할 경우 
								add_joinUser(ui.item.value);    // 아래에서 만들어 두었던 add_joinUser(value) 함수 호출하기 
								                                // ui.item.value 이  선택한이름 이다.
								return false;
					        },
					        focus: function(event, ui) {
					            return false;
					        }
						});
							
					}// end of if------------------------------------
				}// end of success-----------------------------------
			});
		});// end of $("input#joinUserName").bind("keyup",function(){}) ---------------
		
		// x아이콘 클릭시 공유자 제거하기
		$(document).on('click','div.displayUserList > span.plusUser > i',function(){
			//var text = $(this).parent().text(); // 이순신(leess/leesunsin@naver.com)
			
			//var bool = confirm("공유자 목록에서 "+ text +" 회원을 삭제하시겠습니까?");
			// 공유자 목록에서 이순신(leess/leesunsin@naver.com) 회원을 삭제하시겠습니까?
			//if(bool) {
			$(this).parent().remove();
			//}
		});// end of $(document).on('click','div.displayUserList > span#plusUser > i',function(){}) --------------------- 
		
		// 수정 버튼 클릭
		$("button#edit").click(function(){
		
			// 일자 유효성 검사 (시작일자가 종료일자 보다 크면 안된다!!)
			var startDate = $("input#startDate").val();	
	    	var sArr = startDate.split("-");
	    	startDate= "";	
	    	for(var i=0; i<sArr.length; i++){
	    		startDate += sArr[i];
	    	}
	    	
	    	var endDate = $("input#endDate").val();	
	    	var eArr = endDate.split("-");   
	     	var endDate= "";
	     	for(var i=0; i<eArr.length; i++){
	     		endDate += eArr[i];
	     	}
			
	     	var startHour= $("select#startHour").val();
	     	var endHour = $("select#endHour").val();
	     	var startMinute= $("select#startMinute").val();
	     	var endMinute= $("select#endMinute").val();
	        
	     	// 조회기간 시작일자가 종료일자 보다 크면 경고
	        if (Number(endDate) - Number(startDate) < 0) {
	         	alert("종료일이 시작일 보다 작습니다."); 
	         	return;
	        }
	        
	     	// 시작일과 종료일 같을 때 시간과 분에 대한 유효성 검사
	        else if(Number(endDate) == Number(startDate)) {
	        	
	        	if(Number(startHour) > Number(endHour)){
	        		alert("종료일이 시작일 보다 작습니다."); 
	        		return;
	        	}
	        	else if(Number(startHour) == Number(endHour)){
	        		if(Number(startMinute) > Number(endMinute)){
	        			alert("종료일이 시작일 보다 작습니다."); 
	        			return;
	        		}
	        		else if(Number(startMinute) == Number(endMinute)){
	        			alert("시작일과 종료일이 동일합니다."); 
	        			return;
	        		}
	        	}
	        }// end of else if---------------------------------
	    	
			// 제목 유효성 검사
			var subject = $("input#schedule_name").val().trim();
	        if(subject==""){
				alert("제목을 입력하세요."); 
				return;
			}
	        
	        // 캘린더 선택 유무 검사
			var calType = $("select.calType").val().trim();
			if(calType==""){
				alert("캘린더 종류를 선택하세요."); 
				return;
			}
			
			// 달력 형태로 만들어야 한다.(시작일과 종료일)
			// 오라클에 들어갈 date 형식(년월일시분초)으로 만들기
			var sdate = startDate+$("select#startHour").val()+$("select#startMinute").val()+"00";
			var edate = endDate+$("select#endHour").val()+$("select#endMinute").val()+"00";
			
			$("input[name=schedule_startdate]").val(sdate);
			$("input[name=schedule_enddate]").val(edate);
		
		//	console.log("캘린더 소분류 번호 => " + $("select[name=fk_smcatgono]").val());
			/*
			      캘린더 소분류 번호 => 1 OR 캘린더 소분류 번호 => 2 OR 캘린더 소분류 번호 => 3 OR 캘린더 소분류 번호 => 4 
			*/
			
		//  console.log("색상 => " + $("input#color").val());
			
			// 공유자 넣어주기
			var plusUser_elm = document.querySelectorAll("div.displayUserList > span.plusUser");
			var joinUserArr = new Array();
			
			plusUser_elm.forEach(function(item,index,array){
			//	console.log(item.innerText.trim());
				/*
					이순신(leess) 
					아이유1(iyou1) 
					설현(seolh) 
				*/
				joinUserArr.push(item.innerText.trim());
			});
			
			var joinuser = joinUserArr.join(",");
		//	console.log("공유자 => " + joinuser);
			// 이순신(leess),아이유1(iyou1),설현(seolh) 
			
			$("input[name=schedule_joinemp]").val(joinuser);
			
			var frm = document.scheduleFrm;
			frm.action = $("input#path").val()+"/schedule/editSchedule_end";
			frm.method = "post";
			frm.submit();

		});// end of $("button#register").click(function(){})--------------------
	});// end of $(document).ready(function() {}) -----------------
	
	
	
	// ~~~~ Function Declaration ~~~~
	
	// div.displayUserList 에 공유자를 넣어주는 함수
	function add_joinUser(value){  // value 가 공유자로 선택한이름 이다.
		
		var plusUser_es = $("div.displayUserList > span.plusUser").text();
	
	 // console.log("확인용 plusUser_es => " + plusUser_es);
	    /*
	    	확인용 plusUser_es => 
 			확인용 plusUser_es => 이순신(leess/hanmailrg@naver.com)
 			확인용 plusUser_es => 이순신(leess/hanmailrg@naver.com)아이유1(iyou1/younghak0959@naver.com)
 			확인용 plusUser_es => 이순신(leess/hanmailrg@naver.com)아이유1(iyou1/younghak0959@naver.com)아이유2(iyou2/younghak0959@naver.com)
	    */
	
		if(plusUser_es.includes(value)) {  // plusUser_es 문자열 속에 value 문자열이 들어있다라면 
			alert("이미 추가한 회원입니다.");
		}
		
		else {
			$("div.displayUserList").append("<span class='plusUser joinBorder'>"+value+"&nbsp;<i style='cursor: pointer;' class='fas fa-times-circle'></i></span>");
		}
		
		$("input#joinUserName").val("");
		
	}// end of function add_joinUser(value){}----------------------------
	
</script>

<div class="common_wrapper">
    <div class="side_menu_wrapper">
        <div class="side_menu_inner_wrapper">
            <span class="common_title">캘린더</span>
            <button type="button" id="registerSchedule">일정 등록</button>
            <ul class="side_menu_list" id="side_menu">
                 <li><a href="${pageContext.request.contextPath}/schedule/scheduleIndex">달력 보기</a></li>
             </ul>
        </div>
    </div>
    <div class="contents_wrapper">
		<div class="contents_inner_wrapper">
			<div style="margin: 4% 0 4% 0">
		    	<span class="h3">일정 수정</span>
		    	<span style="float: right; cursor: pointer;" onclick="location.href='${pageContext.request.contextPath}/schedule/scheduleIndex'">캘린더로 돌아가기</span>
	   		</div>
			<form name="scheduleFrm">
				<table id="schedule" class="table table-bordered">
					<tr>
						<th>일자</th>
						<td>
							<input type="date" class="border" id="startDate" value="${requestScope.chooseDate}" style="height: 30px;"/>&nbsp; 
							<select id="startHour" class="schedule border"></select> 시
							<select id="startMinute" class="schedule border"></select> 분
							- <input type="date" id="endDate" class="border" value="${requestScope.chooseDate}" style="height: 30px;"/>&nbsp;
							<select id="endHour" class="schedule border"></select> 시
							<select id="endMinute" class="schedule border"></select> 분&nbsp;
							<input type="checkbox" id="allDay"/>&nbsp;<label for="allDay">종일</label>
							
							<input type="hidden" name="schedule_startdate"/>
							<input type="hidden" name="schedule_enddate"/>
						</td>
					</tr>
					<tr>
						<th>제목</th>
						<td><input type="text" id="schedule_name" name="schedule_name" class="form-control" value="${requestScope.map.schedule_name}" /></td>
					</tr>
					<tr>
						<th>캘린더선택</th>
						<td>
							<select class="calType schedule border" name="fk_lgcatego_no">
								<c:choose>
								<%-- 일정등록시 사내캘린더 등록은 부서가 인사부원만 등록이 가능하도록 한다. --%> 
									<c:when test="${sessionScope.loginuser.fk_dept_id == 2}"> 
										<option value="">선택하세요</option>
										<option value="2">내 일정</option>
										<option value="1">전사 일정</option>
									</c:when>
								<%-- 일정등록시 내캘린더 등록은 로그인 된 사용자이라면 누구나 등록이 가능하다. --%> 	
									<c:otherwise>
										<option value="">선택하세요</option>
										<option value="2">내 일정</option>
									</c:otherwise>
								</c:choose>
							</select> &nbsp;
							<select class="small_category schedule border" name="fk_smcatego_no"></select>
						</td>
					</tr>
					<tr>
						<th>색상</th>
						<td><input class="border" type="color" id="schedule_color" name="schedule_color" value="#009900"/></td>
					</tr>
					<tr>
						<th>장소</th>
						<td><input type="text" name="schedule_place" class="form-control" value="${requestScope.map.schedule_place}"/></td>
					</tr>
					
					<tr>
						<th>공유자</th>
						<td>
							<input type="text" id="joinSearchWord" class="form-control" placeholder="회원명 혹은 부서 혹은 지점으로 검색"/>
							<div class="displayUserList"></div>
							<input type="hidden" name="schedule_joinemp"/>
						</td>
					</tr>
					<tr>
						<th>내용</th>
						<td><textarea rows="10" cols="100" style="height: 200px;" name="schedule_content" id="schedule_content"  class="form-control" >${requestScope.map.schedule_content}</textarea></td>
					</tr>
				</table>
				<input type="hidden" value="${sessionScope.loginuser.emp_id}" name="fk_emp_id"/>
				<input type="hidden" value="${requestScope.map.schedule_no}" name="schedule_no"/>
			</form>
			<div style="float: right;">
				<%-- <button type="button" id="register" class="border-1 rounded-md" style="margin-right: 10px;">등록</button>--%>
				<button type="button" class="buttonBorder" id="edit" style="margin-right: 10px; background-color: #99ccff;">수정</button>
				<button type="button" class="buttonBorder" style="background-color: ecf0f8;" onclick="javascript:location.href='${pageContext.request.contextPath}${gobackURL_detailSchedule}'">취소</button> 
			</div>
		
		</div>
		
	</div>
	
</div>

<%-- path --%>
<input type="hidden" id="path" value="${pageContext.request.contextPath}" /> 

<%-- === 일정 등록을 위한 폼 === --%>     
<form name="dateFrm">
	<input type="hidden" name="chooseDate" />	
</form>	
