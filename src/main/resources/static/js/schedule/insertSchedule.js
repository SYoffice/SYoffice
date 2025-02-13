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
	
	
	
});// end of $(document).ready(() => {}) -------------------------