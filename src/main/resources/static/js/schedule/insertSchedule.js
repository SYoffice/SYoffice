/**
 * 
 */

$(document).ready(() => {
	
	// 캘린더 소분류 카테고리 숨기기
	$("select.small_category").hide();
	
	// === *** 달력(type="date") 관련 시작 *** === //
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
	// === *** 달력(type="date") 관련 끝 *** === //

	
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
	});// end of $("input#allDay").click(function() {}) ----------------------------
	
	
	
	// 일정 선택에 따른 서브캘린더 종류를 알아와서 select 태그에 넣어주기 
	$("select.calType").change(function(){
		var fk_lgcatego_no = $("select.calType").val();      		// 일정 대분류
		var fk_emp_id 	 = $("input[name=fk_emp_id]").val();  		// 로그인 한 유저의 사번
		
		if(fk_lgcatego_no != "") { // 선택하세요 가 아니라면
			$.ajax({
				url: $("input#path").val()+"/schedule/selectSmallCategory",
				data: {"fk_lgcatego_no":fk_lgcatego_no, "fk_emp_id": fk_emp_id},
				dataType: "JSON",
				success:function(json){
					console.log(JSON.stringify(json));
					
					var html = "";
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
	});// end of $("select.calType").change(function(){}) -------------------- 
	
	
	// 공유자 추가하기
	$("input#joinSearchWord").on("input",function(){
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
	
	
	// 등록 버튼 클릭
	$("button#register").click(function(){
	
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
		frm.action = $("input#path").val()+"/schedule/registerSchedule_end";
		frm.method = "post";
		frm.submit();

	});// end of $("button#register").click(function(){})--------------------
	
});// end of $(document).ready(() => {}) -------------------------




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
	$("input#joinSearchWord").val("");
}// end of function add_joinUser(value){}----------------------------