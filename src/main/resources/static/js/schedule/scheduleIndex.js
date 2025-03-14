/**
 * 
 */

window.onload = () => {
	
	// === 내 캘린더 소분류 추가를 위해 +아이콘 클릭시 === //
	$(document).on("click", "button#myCal", () => {
		$("div#modal_addMyCal").modal();
	});// end of $(document).on("click", "button#myCal", () => {}) ---------------- 
	
	
	
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
		
		// 풀캘린더에서 날짜 클릭할 때 발생하는 이벤트(일정 등록창으로 넘어간다)
        dateClick: function(info) {
      	 // alert('클릭한 Date: ' + info.dateStr); // 클릭한 Date: 2021-11-20
      	    $(".fc-day").css('background','none'); // 현재 날짜 배경색 없애기
      	    info.dayEl.style.backgroundColor = '#b1b8cd'; // 클릭한 날짜의 배경색 지정하기
      	    $("input[name=chooseDate]").val(info.dateStr);

						
      	    var frm = document.dateFrm;
      	    frm.method="POST";
      	    frm.action=$("input#path").val()+"/schedule/insertSchedule";
      	    frm.submit();
      	  },
		
		
	});// end of var calendar = new FullCalendar.Calendar(calendarEl, {}) ----------------- 
	
	calendar.render();  // 풀캘린더 보여주기
	
}// end of window.onload = () => {} ----------------------



