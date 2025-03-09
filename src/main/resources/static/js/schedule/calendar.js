/**
 * 
 */

window.onload = () => {
	
	showMyCal();		// 등록된 소분류가 없으면 일정이 출력 되지 않음
	showCompanyCal();
	
	// === 전사 일정 체크박스 전체 선택/전체 해제 === //
	$("input:checkbox[id=allComCal]").click(function(){
		var bool = $(this).prop("checked");
		$("input:checkbox[name=com_smcatgono]").prop("checked", bool);
	});// end of $("input:checkbox[id=allComCal]").click(function(){})-------
	
	// === 내 일정 체크박스 전체 선택/전체 해제 === //
	$("input:checkbox[id=allMyCal]").click(function(){		
		var bool = $(this).prop("checked");
		$("input:checkbox[name=my_smcatgono]").prop("checked", bool);
	});// end of $("input:checkbox[id=allMyCal]").click(function(){})-------
	
	// === 사내캘린더 에 속한 특정 체크박스를 클릭할 경우 === 
	$(document).on("click","input:checkbox[name=com_smcatgono]",function(){	
		var bool = $(this).prop("checked");
		
		if(bool){ // 체크박스에 클릭한 것이 체크된 것이라면 
			
			var flag=false;
			
			$("input:checkbox[name=com_smcatgono]").each(function(index, item){
				var bChecked = $(item).prop("checked");
				
				if(!bChecked){     // 체크되지 않았다면 
					flag=true;     // flag 를 true 로 변경
					return false;  // 반복을 빠져 나옴.
				}
			}); // end of $("input:checkbox[name=com_smcatgono]").each(function(index, item){})---------

			if(!flag){ // 사내캘린더 에 속한 서브캘린더의 체크박스가 모두 체크가 되어진 경우라면 			
                $("input#allComCal").prop("checked",true); // 사내캘린더 체크박스에 체크를 한다.
			}
			
			var com_smcatgonoArr = document.querySelectorAll("input.com_smcatgono");
		    
			com_smcatgonoArr.forEach(function(item) {
		         item.addEventListener("change", function() {  // "change" 대신에 "click" 을 해도 무방함.
		         //	 console.log(item);
		        	 calendar.refetchEvents();  // 모든 소스의 이벤트를 다시 가져와 화면에 다시 표시합니다.
		         });
		    });// end of com_smcatgonoArr.forEach(function(item) {})---------------------

		}
		
		else {
			   $("input#allComCal").prop("checked",false);
		}
		
	});// end of $(document).on("click","input:checkbox[name=com_smcatgono]",function(){})--------
	
	// === 내캘린더 에 속한 특정 체크박스를 클릭할 경우 === 
	$(document).on("click","input:checkbox[name=my_smcatgono]",function(){	
		var bool = $(this).prop("checked");
		
		if(bool){ // 체크박스에 클릭한 것이 체크된 것이라면 
			
			var flag=false;
			
			$("input:checkbox[name=my_smcatgono]").each(function(index, item){
				var bChecked = $(item).prop("checked");
				
				if(!bChecked){    // 체크되지 않았다면 
					flag=true;    // flag 를 true 로 변경
					return false; // 반복을 빠져 나옴.
				}
			}); // end of $("input:checkbox[name=my_smcatgono]").each(function(index, item){})---------

			if(!flag){	// 내캘린더 에 속한 서브캘린더의 체크박스가 모두 체크가 되어진 경우라면 	
                $("input#allMyCal").prop("checked",true); // 내캘린더 체크박스에 체크를 한다.
			}
			
			var my_smcatgonoArr = document.querySelectorAll("input.my_smcatgono");
		      
			my_smcatgonoArr.forEach(function(item) {
				item.addEventListener("change", function() {   // "change" 대신에 "click" 을 해도 무방함.
				 // console.log(item); 
					calendar.refetchEvents();  // 모든 소스의 이벤트를 다시 가져와 화면에 다시 표시합니다.
		        });
		    });// end of my_smcatgonoArr.forEach(function(item) {})---------------------

		}
		
		else {
			   $("input#allMyCal").prop("checked",false);
		}
		
	});// end of $(document).on("click","input:checkbox[name=my_smcatgono]",function(){})--------
	
	// === 일정 소분류 관련 시작 === //
	// 일정 소분류 추가를 위해 + 아이콘 클릭시 == //
	$(document).on("click", "button.addCal", function() {
		//$("input#fk_lgcatego_no").val($(this).val());
		//$("div#modal_addCal").modal();
		//alert($(this).find("input[name='lgcatego_no']").val());
		
		Swal.fire({
			title: "소분류명 등록",
			icon : "question",
			input: "text",
			showCancelButton: true,
			confirmButtonText: "등록",
			cancelButtonText: "취소",
		})
		.then((result) => {
			if (result.isConfirmed){	// 등록확인 버튼 클릭 시
				if (result.value == "") {
					// 아무 내용도 입력하지 않았을 시
					Swal.fire({
						title: '소분류명을 입력하세요!!',        // Alert 제목
						icon: 'warning',
					});
					setTimeout( () => {$(this).click()}, 1000) ;	// 다시 버튼 클릭
				}
				else {
					goAddCal(result.value, $(this).val());	// 소분류 추가 함수 호출(입력값, 대분류 카테고리)					
				}
			}
			else {	// 취소 시
				Swal.fire({
					title: '등록을 취소했습니다.',        // Alert 제목
					icon: 'info',
				});
			}
		}).catch((err) => {
			
		});

	});// end of $(document).on("click", "button#myCal", () => {}) ---------------- 
	
	
	
	// 전사 일정 화살표 버튼을 눌렀을 때 등록한 소분류를 표시, 감추기
	$(document).on("click", "span.showComCal", function() {
		//alert($(this).html());

		if ($(this).children().hasClass("fa-caret-down")) {	
			$(this).children().removeClass("fa-caret-down").addClass("fa-caret-up");
			showCompanyCal();
		}
		else {			
			$(this).children().removeClass("fa-caret-up").addClass("fa-caret-down");
			$("ul#comCal").hide().css({"transition": "0.8s"});
		}
	});// end of $(document).on("click", "button.showSmallCal", function() {})------------------
	
	
	// 부서 일정 화살표 버튼을 눌렀을 때 등록한 소분류를 표시, 감추기
	$(document).on("click", "span.showDeptCal", function() {
		//alert($(this).html());

		if ($(this).children().hasClass("fa-caret-down")) {	
			$(this).children().removeClass("fa-caret-down").addClass("fa-caret-up");
			//showCompanyCal();
		}
		else {			
			$(this).children().removeClass("fa-caret-up").addClass("fa-caret-down");
			$("ul#deptCal").hide().css({"transition": "0.8s"});
		}
	});// end of $(document).on("click", "button.showSmallCal", function() {})------------------
	
	
	// 내 일정 화살표 버튼을 눌렀을 때 등록한 소분류를 표시, 감추기
	$(document).on("click", "span.showMyCal", function() {
		//alert($(this).html());

		if ($(this).children().hasClass("fa-caret-down")) {	
			$(this).children().removeClass("fa-caret-down").addClass("fa-caret-up");
			showMyCal();
		}
		else {			
			$(this).children().removeClass("fa-caret-up").addClass("fa-caret-down");
			$("ul#myCal").hide().css({"transition": "0.8s"});
		}
	});// end of $(document).on("click", "button.showSmallCal", function() {})------------------
	
	
	
	// 소분류명 입력 후 엔터 키 입력 시 등록하는 이벤트 	
	$(document).on("keyDown", "input#smcatego_name", e => {
		if (e.keyCode == 13) {	// 엔터 입력 시
			goAddCal();
		}
	});// end of $(document).on("keyDown", "input#smcatego_name", e => {}) ----------------- 
	// === 일정 소분류 관련 끝 === //
	
	
	
	
	// ==== 풀캘린더와 관련된 소스코드 시작(화면이 로드되면 캘린더 전체 화면 보이게 해줌) ==== //
	var calendarEl = document.getElementById('calendar');
	
	var calendar = new FullCalendar.Calendar(calendarEl, {
		// === 구글캘린더를 이용하여 대한민국 공휴일 표시하기 시작 === //
		googleCalendarApiKey : "AIzaSyASM5hq3PTF2dNRmliR_rXpjqNqC-6aPbQ",	// 구글 클라우드에서 발급받은 API 키 
		eventSources :[ 
            {
            //  googleCalendarId : '대한민국의 휴일 캘린더 통합 캘린더 ID'
                googleCalendarId : 'ko.south_korea#holiday@group.v.calendar.google.com'
              , color: 'white'   // 옵션임! 옵션참고 사이트 https://fullcalendar.io/docs/event-source-object
              , textColor: 'red' // 옵션임! 옵션참고 사이트 https://fullcalendar.io/docs/event-source-object 
        	} 
        ],
		// === 구글캘린더를 이용하여 대한민국 공휴일 표시하기 끝 === //
		initialView: 'dayGridMonth',	// 캘린더 생성 렌더 시 첫 보여줄 타입 (월간뷰)
        locale: 'ko',					// 표시 언어
        selectable: true,				
	    editable: false,				
	    headerToolbar: {
	    	  left: 'prev,next today',		// 이전달, 다음달, 오늘
	          center: 'title',
	          right: 'dayGridMonth dayGridWeek dayGridDay'
	    },
	    dayMaxEventRows: true, // for all non-TimeGrid views
	    views: {
	      timeGrid: {
	        dayMaxEventRows: 3 // adjust to 6 only for timeGridWeek/timeGridDay
	      }
	    },
		// ===================== DB 와 연동하는 법 시작 ===================== //
		events:function(info, successCallback, failureCallback) {
			$.ajax({
				url: $("input#path").val()+'/schedule/selectSchedule',
				data:{"fk_emp_id": $("input#fk_emp_id").val(), "name": $("input#name").val()},
				dataType: "JSON",
				async: false,
				success: function(json) {
					//console.log(JSON.stringify(json));
					/*
						[{"fk_smcatego_no":"1","schedule_startdate":"2025-02-18 13:30:00","fk_emp_id":"1000","schedule_no":"2","schedule_place":"회사","schedule_content":"채팅으로","schedule_color":"#009900","schedule_enddate":"2025-02-18 18:00:00","schedule_name":"퇴근하고싶다","schedule_joinemp":"이순신(본사-영업부)","fk_lgcatego_no":"1"}
						,{"fk_smcatego_no":"2","schedule_startdate":"2025-02-17 14:00:00","fk_emp_id":"1000","schedule_no":"3","schedule_place":"쌍용강북 g클래스","schedule_content":"일정관리 표시","schedule_color":"#009900","schedule_enddate":"2025-02-17 18:00:00","schedule_name":"파이널프로젝트","schedule_joinemp":"관리자(본사-인사부),엄정화(본사-인사부),이순신(본사-영업부)","fk_lgcatego_no":"2"}
						,{"fk_smcatego_no":"5","schedule_startdate":"2025-02-19 08:00:00","fk_emp_id":"1000","schedule_no":"4","schedule_place":"집","schedule_content":"실험체 구하기","schedule_color":"#7c7bbc","schedule_enddate":"2025-02-19 09:00:00","schedule_name":"출근","schedule_joinemp":"아이유(본사-기획부),실험용(본사-마케팅부)","fk_lgcatego_no":"2"}]
					*/
					var events = [];	// 이벤트를 담을 배열

					if(json.length > 0){
						// 조회 된 것이 있다면
						$.each(json, function(index, item) {
							var startdate = moment(item.schedule_startdate).format('YYYY-MM-DD HH:mm:ss');
							var enddate = moment(item.schedule_enddate).format('YYYY-MM-DD HH:mm:ss');

							// 사내 캘린더로 등록된 일정을 풀캘린더 달력에 보여주기 
							// 일정등록시 사내 캘린더에서 선택한 소분류에 등록된 일정을 풀캘린더 달력 날짜에 나타내어지게 한다.
							if( $("input:checkbox[name=com_smcatgono]:checked").length <= $("input:checkbox[name=com_smcatgono]").length ){
								for(var i=0; i<$("input:checkbox[name=com_smcatgono]:checked").length; i++){   
									if($("input:checkbox[name=com_smcatgono]:checked").eq(i).val() == item.fk_smcatego_no){
										// alert("캘린더 소분류 번호 : " + $("input:checkbox[name=com_smcatgono]:checked").eq(i).val());
										events.push({
											id: item.schedule_no,
											title: item.schedule_name,
											start: startdate,
											end: enddate,
											url: $("input#path").val()+"/schedule/detailSchedule?scheduleno="+item.schedule_no,
											color: item.schedule_color,
											cid: item.fk_smcatego_no  // 사내캘린더 내의 서브캘린더 체크박스의 value값과 일치하도록 만들어야 한다. 그래야만 서브캘린더의 체크박스와 cid 값이 연결되어 체크시 풀캘린더에서 일정이 보여지고 체크해제시 풀캘린더에서 일정이 숨겨져 안보이게 된다. 
										}); // end of events.push({})---------
									}
								}// end of for-------------------------------------
							}// end of if-------------------------------------------

							// 내 캘린더로 등록된 일정을 풀캘린더 달력에 보여주기
							// 일정등록시 내 캘린더에서 선택한 소분류에 등록된 일정을 풀캘린더 달력 날짜에 나타내어지게 한다.
							if( $("input:checkbox[name=my_smcatgono]:checked").length <= $("input:checkbox[name=my_smcatgono]").length ){
								
								for(var i=0; i<$("input:checkbox[name=my_smcatgono]:checked").length; i++){
									if($("input:checkbox[name=my_smcatgono]:checked").eq(i).val() == item.fk_smcatego_no && item.fk_emp_id == $("input#fk_emp_id").val()){
										//  alert("캘린더 소분류 번호 : " + $("input:checkbox[name=my_smcatgono]:checked").eq(i).val());
										events.push({
											id: item.schedule_no,
											title: item.schedule_name,
											start: startdate,
											end: enddate,
											url: $("input#path").val()+"/schedule/detailSchedule?scheduleno="+item.schedule_no,
											color: item.schedule_color,
											cid: item.fk_smcatego_no  // 내캘린더 내의 서브캘린더 체크박스의 value값과 일치하도록 만들어야 한다. 그래야만 서브캘린더의 체크박스와 cid 값이 연결되어 체크시 풀캘린더에서 일정이 보여지고 체크해제시 풀캘린더에서 일정이 숨겨져 안보이게 된다. 
										}); // end of events.push({})---------
									}
								}// end of for-------------------------------------
							}// end of if-------------------------------------------

							// 공유받은 캘린더(다른 사용자가 내캘린더로 만든 것을 공유받은 경우임)
							if (item.fk_lgcatego_no == 2 && item.fk_emp_id != $("input#fk_emp_id").val() && (item.schedule_joinemp).indexOf($("input#fk_emp_id").val()) != -1 ){  
								events.push({
									id: "0",  // "0" 인 이유는  배열 events 에 push 할때 id는 고유해야 하는데 위의 사내캘린더 및 내캘린더에서 push 할때 id값으로 item.scheduleno 을 사용하였다. item.scheduleno 값은 DB에서 1 부터 시작하는 시퀀스로 사용된 값이므로 0 값은 위의 사내캘린더나 내캘린더에서 사용되지 않으므로 여기서 고유한 값을 사용하기 위해 0 값을 준 것이다. 
									title: item.schedule_name,
									start: startdate,
									end: enddate,
									url: $("input#path").val()+"/schedule/detailSchedule?scheduleno="+item.schedule_no,
									color: item.schedule_color,
									cid: "0"  // "0" 인 이유는  공유받은캘린더 에서의 체크박스의 value 를 "0" 으로 주었기 때문이다.
								}); // end of events.push({})--------- 
								
							}// end of if------------------------- 

						});// end of $.each -----------------------
					}
					successCallback(events);  
				},
				error: function(request, status, error){
					alert("여기서나는거임!!"+"code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
					
				}
			});
		},// end of events:function(info, successCallback, failureCallback) {}---------
		// ===================== DB 와 연동하는 법 끝 ===================== //
		
		// 풀캘린더에서 날짜 클릭할 때 발생하는 이벤트(일정 등록창으로 넘어간다)
        dateClick: function(info) {
      	  	//alert('클릭한 Date: ' + info.dateStr); // 클릭한 Date: 2021-11-20
      	    $(".fc-day").css('background','none'); // 현재 날짜 배경색 없애기
      	    info.dayEl.style.backgroundColor = '#b1b8cd'; // 클릭한 날짜의 배경색 지정하기
      	    $("input[name=chooseDate]").val(info.dateStr);

						
      	    var frm = document.dateFrm;
      	    frm.method="POST";
      	    frm.action=$("input#path").val()+"/schedule/insertSchedule";
      	    frm.submit();
      	},
		eventDidMount: function (arg) {
			var arr_calendar_checkbox = document.querySelectorAll("input.calendar_checkbox"); 
			// 사내캘린더, 내캘린더, 공유받은캘린더 에서의 모든 체크박스임

			arr_calendar_checkbox.forEach(function(item) { // item 이 사내캘린더, 내캘린더, 공유받은캘린더 에서의 모든 체크박스 중 하나인 체크박스임
				if (item.checked) { 
				// 사내캘린더, 내캘린더, 공유받은캘린더 에서의 체크박스중 체크박스에 체크를 한 경우 라면
					if (arg.event.extendedProps.cid === item.value) { // item.value 가 체크박스의 value 값이다.
						// console.log("일정을 보여주는 cid : "  + arg.event.extendedProps.cid);
						// console.log("일정을 보여주는 체크박스의 value값(item.value) : " + item.value);
						
						arg.el.style.display = "block"; // 풀캘린더에서 일정을 보여준다.
					}
				} 
				else { 
				// 사내캘린더, 내캘린더, 공유받은캘린더 에서의 체크박스중 체크박스에 체크를 해제한 경우 라면
					if (arg.event.extendedProps.cid === item.value) {
						// console.log("일정을 숨기는 cid : "  + arg.event.extendedProps.cid);
						// console.log("일정을 숨기는 체크박스의 value값(item.value) : " + item.value);
						
						arg.el.style.display = "none"; // 풀캘린더에서 일정을  숨긴다.
					}
				}
			});// end of arr_calendar_checkbox.forEach(function(item) {})------------
	 	}
	});// end of var calendar = new FullCalendar.Calendar(calendarEl, {}) ----------------- 
	
	setTimeout(()=>{ calendar.render();}, 400)  // 풀캘린더 보여주기 보이다 말다 하므로 timeout 설정
	
	/*
	let render = new Promise(function (resolve, reject) {
		resolve();		// resolve 실행 시 
	});
	
	// 일정 소분류를 불러오고 나서 캘린더 랜더링
	render.then(()=>{
		showMyCal()
		showCompanyCal();
	}).then(()=>{
		calendar.render();
	})
	*/
	
	var arr_calendar_checkbox = document.querySelectorAll("input.calendar_checkbox"); 
	// 사내캘린더, 내캘린더, 공유받은캘린더 에서의 체크박스임
	  
	arr_calendar_checkbox.forEach(function(item) {
		item.addEventListener("change", function () {
		    // console.log(item);
			calendar.refetchEvents(); // 모든 소스의 이벤트를 다시 가져와 화면에 다시 표시합니다.
	    });
	});
}// end of window.onload = () => {} ----------------------


// === 내 캘린더 추가 모달창에서 추가 버튼 클릭시 === 
function goAddCal(smcatego_name, fk_lgcatego_no){
	$.ajax({
		url: $("input#path").val()+"/schedule/addCalendar",
		type: "post",
		data: {"smcatego_name": smcatego_name, 
			   "fk_emp_id": $("input#fk_emp_id").val(), "fk_lgcatego_no": fk_lgcatego_no},
		dataType: "json",
		success:function(json){
			
			if(json.n!=1){
				//alert("이미 등록한 캘린더입니다.");
				Swal.fire({
				  title: '캘린더 등록에 실패했습니다.',        // Alert 제목
				  text: smcatego_name+ '은(는) 이미 사용중인 캘린더 명입니다',  		 	// Alert 내용
				  icon: 'error',
				  //type: 'Error',                        // Alert 타입
				});
				return;
			}
			else if(json.n==1){				
				Swal.fire({
				  title: '캘린더 등록에 성공했습니다.',        // Alert 제목
				  text: smcatego_name +' 캘린더가 등록되었습니다.',  		 	// Alert 내용
				  icon: 'success',
				  //type: 'Error',                        // Alert 타입
				});
			 	//showCal(); // 내 캘린더 소분류 보여주기
				switch (fk_lgcatego_no) {
					case "1":	// 전사 일정
						showCompanyCal();
						break;
					case "2":	// 내 일정
						showMyCal();
						break;
				}
				
			}
		},
		error: function(request, status, error){
         	alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	    }	 
	});
}// end of function goAddMyCal(){}-----------------------


// === 내 캘린더에서 내캘린더 소분류 보여주기  === //
function showMyCal(){
	$.ajax({
		 url: $("input#path").val()+"/schedule/showMyCalendar",
		 type:"get",
		 data:{"fk_emp_id": $("input#fk_emp_id").val()},
		 dataType:"json",
		 success:function(json){
			 var html = ``;
			 if(json.length > 0){				 	 
				 $.each(json, function(index, item){
					html += `
					<li>
						<input type='checkbox' name='my_smcatgono' class='calendar_checkbox my_smcatgono' style='margin-right: 12px; cursor: pointer;' value='${item.smcatego_no}' checked id='my_smcatgono_${index}'/><label style='cursor: pointer;' for='my_smcatgono_${index}'>${item.smcatego_name}</label>
	         			<span style="display: inline-block; margin-right: 12%;" class='btn_edit delCal float-right' onclick='delCalendar("${item.smcatego_no}", "${item.smcatego_name}")'><i class='fas fa-trash'></i></span>
	         			<span style="display: inline-block; margin-right: 8%;" class='btn_edit float-right' onclick='editMyCalendar("${item.smcatego_no}", "${item.smcatego_name}")'><i class='fas fa-edit'></i></span>
					</li>`; 
				 });
			 }
			 
			$("ul#myCal").html(html);
			$("ul#myCal").show();
		 },
		 error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	     }	 	
	});
}// end of function showmyCal()---------------------


// === 전사일정 소분류 보여주기  === //
function showCompanyCal(){
	$.ajax({
		url:$("input#path").val()+"/schedule/showCompanyCalendar",
		type:"get",
		dataType:"json",
		success:function(json){
				console.log(JSON.stringify(json));
				
				var html = "";
				if(json.length > 0){
					$.each(json, function(index, item){
						html += `
							<li>
								<input type='checkbox' name='com_smcatgono' class='calendar_checkbox com_smcatgono' style='margin-right: 12px; cursor: pointer;' value='${item.smcatego_no}' checked id='com_smcatgono_${index}'/><label style='cursor: pointer;' for='com_smcatgono_${index}'>${item.smcatego_name}</label>
							`;
						if(Number($("input#dept_id").val()) == 2) {	// 인사부서일 경우만 수정, 삭제 가능
							 html += `
							 	<span style="display: inline-block; margin-right: 12%;" class='btn_edit delCal float-right' onclick='delCalendar("${item.smcatego_no}", "${item.smcatego_name}")'><i class='fas fa-trash'></i></span>
		         				<span style="display: inline-block; margin-right: 8%;" class='btn_edit float-right' onclick='editComCalendar("${item.smcatego_no}", "${item.smcatego_name}")'><i class='fas fa-edit'></i></span>
							 `;
						}
						html += "</li>";
					});
				}
			 
				$("ul#comCal").html(html);
				$("ul#comCal").show();
		},
		error: function(request, status, error){
	           alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }	 	
	});

}// end of function showCompanyCal()------------------



function inputSmallCategoInfo(fk_lgcatego_no) {
	
	//alert("확인용 대분류 : "+ fk_lgcatego_no)
	
	Swal.fire({
		title: "캘린더명 등록",
		icon : "question",
		input: "text",
		showCancelButton: true,
		confirmButtonText: "등록",
		cancelButtonText: "취소",
	})
	.then((result) => {
		if (result.isConfirmed){	// 등록확인 버튼 클릭 시
			if (result.value == "") {
				// 아무 내용도 입력하지 않았을 시
				Swal.fire({
					title: '캘린더명을 입력하세요!!',        // Alert 제목
					icon: 'warning',
				});
				setTimeout( () => {$(this).click()}, 1000) ;	// 다시 버튼 클릭
			}
			else {
				goAddCal(result.value, fk_lgcatego_no);	// 소분류 추가 함수 호출(입력값, 대분류 카테고리)					
			}
		}
		else {	// 취소 시
			Swal.fire({
				title: '등록을 취소했습니다.',        // Alert 제목
				icon: 'info',
			});
		}
	}).catch((err) => {
		console.log(err);
	});
	
}// end of function inputSmallCategoInfo(fk_lgcatego_no) -----------------------


// == 전사 일정 소분류 수정하기 == //
function editComCalendar(smcatego_no, smcatego_name){
	Swal.fire({
		title: "캘린더명 수정",
		icon : "question",
		input: "text",
		showCancelButton: true,
		confirmButtonText: "수정",
		cancelButtonText: "취소",
	})
	.then((result) => {
		if (result.isConfirmed){	// 수정 버튼 클릭 시
			if (result.value == "") {
				// 아무 내용도 입력하지 않았을 시
				Swal.fire({
					title: '캘린더명을 입력하세요!!',        // Alert 제목
					icon: 'warning',
				});
				setTimeout( () => {editComCalendar()}, 800) ;	// 다시 함수 호출
			}
			else {
				$.ajax({
					url:$("input#path").val()+"/schedule/editComCalendar",
					type: "PUT",
					data: {"smcatego_no": smcatego_no, 
						   "smcatego_name": result.value, 
						   "fk_emp_id": $("input#fk_emp_id").val(),
						   "fk_lgcatego_no": "1"
					},
					dataType: "JSON",
					success:function(json){
						if(json.n == 0){
		   					//alert($("input.edit_com_smcatgoname").val()+"은(는) 이미 존재하는 캘린더 명입니다.");
							Swal.fire({
								title: smcatego_name +'은(는) 이미 사용중인 캘린더 명입니다.',        // Alert 제목
								icon: 'warning',
							});
		   					return;
		   				 }
						if(json.n == 1){
							Swal.fire({
								title: '캘린더명을 변경했습니다.',        // Alert 제목
								text: '변경된 캘린더명 : '+ result.value,
								icon: 'success',
							});
							showCompanyCal();
						}
					},
					 error: function(request, status, error){
				            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				    }
				});
			}
		}
		else {	// 취소 시
			Swal.fire({
				title: '수정을 취소했습니다.',        // Alert 제목
				icon: 'info',
			});
		}
	}).catch((err) => {
		console.log(err);
	});
}// end of function editComCalendar(smcatego_no, smcatego_name) ------------


// == 내 일정 소분류 수정하기 == //
function editMyCalendar(smcatego_no, smcatego_name){
	Swal.fire({
		title: "캘린더명 수정",
		icon : "question",
		input: "text",
		showCancelButton: true,
		confirmButtonText: "수정",
		cancelButtonText: "취소",
	})
	.then((result) => {
		if (result.isConfirmed){	// 수정 버튼 클릭 시
			if (result.value == "") {
				// 아무 내용도 입력하지 않았을 시
				Swal.fire({
					title: '캘린더명을 입력하세요!!',        // Alert 제목
					icon: 'warning',
				});
				setTimeout( () => {editComCalendar()}, 800) ;	// 다시 함수 호출
			}
			else {
				$.ajax({
					url:$("input#path").val()+"/schedule/editMyCalendar",
					type: "PUT",
					data: {"smcatego_no": smcatego_no, 
						   "smcatego_name": result.value, 
						   "fk_emp_id": $("input#fk_emp_id").val(),
						   "fk_lgcatego_no": "2"
					},
					dataType: "JSON",
					success:function(json){
						if(json.n == 0){
		   					//alert($("input.edit_com_smcatgoname").val()+"은(는) 이미 존재하는 캘린더 명입니다.");
							Swal.fire({
								title: smcatego_name +'은(는) 이미 사용중인 캘린더 명입니다.',        // Alert 제목
								icon: 'warning',
							});
		   					return;
		   				 }
						if(json.n == 1){
							Swal.fire({
								title: '캘린더명을 변경했습니다.',        // Alert 제목
								text: '변경된 캘린더명 : '+ result.value,
								icon: 'success',
							});
							showMyCal();
						}
					},
					 error: function(request, status, error){
				            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				    }
				});
			}
		}
		else {	// 취소 시
			Swal.fire({
				title: '수정을 취소했습니다.',        // Alert 제목
				icon: 'info',
			});
		}
	}).catch((err) => {
		console.log(err);
	});
}// end of function editMyCalendar(smcatego_no, smcatego_name) ------------------


// == 등록한 일정 소분류 삭제하기 == //
function delCalendar(smcatego_no, smcatego_name){ // smcatgono => 캘린더 소분류 번호, smcatgoname => 캘린더 소분류 명
	
	Swal.fire({
		title: "캘린더 삭제",
		icon : "question",
		text : '캘린더 '+ smcatego_name + '을(를) 삭제하시겠습니까?',
		showCancelButton: true,
		confirmButtonText: "삭제",
		cancelButtonText: "취소",
		confirmButtonColor: "#3085d6",
  		cancelButtonColor: "#d33",
	})
	.then((result) => {
		if (result.isConfirmed){	// 삭제 버튼 클릭 시
			$.ajax({
				url: $("input#path").val()+"/schedule/deleteSubCalendar",
				type: "DELETE",
				data: {"smcatego_no": smcatego_no},
				dataType: "JSON",
				success: function(json){
					if(json.n==1){
						Swal.fire({
							title: '캘린더를 삭제했습니다.',        // Alert 제목
							text: '삭제된 캘린더명 : '+ smcatego_name,
							icon: 'success',
							confirmButtonText: "확인"
						})
						.then((result) => {
							location.href="javascript:history.go(0)";	// 페이지 새로고침
                        })
					}
				},
				error: function(request, status, error){
						alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
			});
		}
		else {	// 취소 시
			Swal.fire({
				title: '삭제를 취소했습니다.',        // Alert 제목
				icon: 'info',
			});
		}
	}).catch((err) => {
		console.log(err);
	});
	
}// end of function delCalendar(smcatgono, smcatgoname){}------------------------

 