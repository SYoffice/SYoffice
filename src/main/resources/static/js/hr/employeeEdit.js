
let nameCheckValid = false; // 이름이 유효한 경우 true

let birthdayCheckValid = false; // 생년월일이 유효한 경우 true

let emailCheckValid = false; // 이메일이 유효한 경우 true

let telCheckValid = false; // 전화번호가 유효한 경우 true

let mailCheckValid = false;	// 사내메일이 유효한 경우 true

$(document).ready(function() {
	
	$("div.error").hide();
	
	// 기존 값이 유효한지 검사하여 초기 상태를 설정
    nameCheckValid = checkName($("input[name='name']").val().trim());
    birthdayCheckValid = checkBirthday($("input[name='birthday']").val().trim());
    emailCheckValid = checkEmail($("input[name='personal_mail']").val().trim());
    telCheckValid = checkTel($("input[name='tel']").val().trim());

    let mailId = $("input[name='mailId']").val().trim();
    let mailAddr = $("#mailAddr").val().trim();
    let mail = mailId + mailAddr;
    let originalEmail = $("#originalEmail").val();

    if (mail === "" || mail === originalEmail) {
        mailCheckValid = true;
    }
	
	
	// 성명 유효성 검사
	$(document).on("blur", "input[name='name']", function(e) {
        const name = $(e.target).val();
		
        if (!checkName(name)) {
            $("div.name_error").show();  
            nameCheckValid = false; 
        } else {
			// 올바르게 입력한 경우
            $("div.name_error").hide();  
            nameCheckValid = true;
        }
    });
	
	// 생년월일 유효성 검사
	$(document).on("blur", "input[name='birthday']", function(e) {
		const birthday = $(e.target).val();
		
		if(!checkBirthday(birthday)) {
			$("div.birthday_error").show();
			birthdayCheckValid = false;
		}
		else{
			// 올바르게 입력한 경우
			$("div.birthday_error").hide();
			birthdayCheckValid = true;
		}
		
	});	
	
	// 이메일 유효성 검사
	$(document).on("blur", "input[name='personal_mail']", function(e) {
		const personal_mail = $(e.target).val();
		
		if(!checkEmail(personal_mail)) {
			$("div.personal_mail_error").show();
			emailCheckValid = false;
		}
		else{
			// 올바르게 입력한 경우
			$("div.personal_mail_error").hide();
			emailCheckValid = true;
		}
		
	});
	
	// 사내 메일 유효성 검사
	$(document).on("blur", "input[name='mailId']", function(e) {
        let mailId = $(e.target).val().trim();	// 입력한 사내 메일 (@ 앞까지)
        let mailAddr = $("#mailAddr").val().trim();	// 사내 메일 주소 (@부터 끝까지)
        let mail = mailId + mailAddr;
		
		let originalEmail = $("#originalEmail").val();	// 기존 사내메일 값 가져오기
		
        if (mail === "" || mail == originalEmail) {
			// 공백일때 에러메시지 감추기
            $(".mail_error").hide();
            return;
        }

        $.ajax({
            url: window.contextPath + "/hr/checkMail",
            type: "POST",
            data: { mail: mail },
			success: function (response) {
	            if (response === "1") { 
	                $(".mail_error").show(); // 중복이면 에러 메시지 표시
	            } 
				else {
	                $(".mail_error").hide(); // 중복이 아니면 숨김
					mailCheckValid = true;
	            }
	        },
	        error: function (request, status, error) {
	            alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
	        },
		});// end of $.ajax() ----
    });
	
	// 전화번호 유효성 검사
	$(document).on("blur", "input[name='tel']", function(e) {
		const tel = $(e.target).val();
		
		if(!checkTel(tel)) {
			$("div.tel_error").show();
			telCheckValid = false;
		}
		else{
			// 올바르게 입력한 경우
			$("div.tel_error").hide();
			telCheckValid = true;
		}
		
	});	
	
	
	// ==== "우편번호찾기"를 클릭했을 때 이벤트 처리하기 ==== //
    $("button#find_Zip").click(function(){
		new daum.Postcode({
        oncomplete: function(data) {
        // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
        
        // 각 주소의 노출 규칙에 따라 주소를 조합한다.
        // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
        let addr = ''; // 주소 변수
        let extraAddr = ''; // 참고항목 변수
        
        //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
        if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
            addr = data.roadAddress;
        } else { // 사용자가 지번 주소를 선택했을 경우(J)
            addr = data.jibunAddress;
        }
        
        // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
        if(data.userSelectedType === 'R'){
            // 법정동명이 있을 경우 추가한다. (법정리는 제외)
            // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
            if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                extraAddr += data.bname;
            }
            // 건물명이 있고, 공동주택일 경우 추가한다.
            if(data.buildingName !== '' && data.apartment === 'Y'){
                extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
            }
            // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
            if(extraAddr !== ''){
                extraAddr = ' (' + extraAddr + ')';
            }
            // 조합된 참고항목을 해당 필드에 넣는다.
            document.getElementById("extraAddress").value = extraAddr;
        
        } else {
            document.getElementById("extraAddress").value = '';
        }
        
        // 우편번호와 주소 정보를 해당 필드에 넣는다.
        document.getElementById('postcode').value = data.zonecode;
        document.getElementById("address").value = addr;
        // 커서를 상세주소 필드로 이동한다.
        document.getElementById("detailAddress").focus();
        }
    }).open();
		
    });// end of $("button#find_Zip").click(function(){})--------
	// ==== "우편번호찾기"를 클릭했을 때 이벤트 처리하기 ==== //    
	
});// end $(document).ready(function(){})----------------------


// 성명 유효성 검사
function checkName(name) {
    const regExp_name = new RegExp("^[가-힣]{3,10}$");
    return regExp_name.test(name);
}

// 생년월일 유효성 검사
function checkBirthday(birthday) {
    const regExp_birthday = new RegExp("^(\\d{4})(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$");
    // 정규식 통과 여부 확인
    if (!regExp_birthday.test(birthday)) {
        return false;
    }

    // 날짜의 연, 월, 일 추출
    const year = birthday.slice(0, 4);
    const month = parseInt(birthday.slice(4, 6), 10);
    const day = parseInt(birthday.slice(6, 8), 10);

    // 날짜 객체 생성 (주의: 월은 0부터 시작하므로 1을 빼줌)
    const date = new Date(year, month - 1, day);

    // 날짜가 유효한지 검사
    if (date.getFullYear() !== parseInt(year, 10) || date.getMonth() + 1 !== month || date.getDate() !== day) {
        return false;
    }

    return true;
}

// 이메일 유효성 검사
function checkEmail(personal_mail){	
	const regExp_personal_mail = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i; 
	return regExp_personal_mail.test(personal_mail);
}

// 전화번호 유효성 검사
function checkTel(tel){
	const regExp_tel = new RegExp("^010[0-9]{8}$");
	return regExp_tel.test(tel);
}

// 신규 사원 등록
function goEdit() {

	// 성명을 입력하지 않을 경우
	if (!nameCheckValid) {
		alert("성명을 입력하여 주세요.")
		return;
	}

	// 생년월일을 입력하지 않을 경우
	if (!birthdayCheckValid) {
		alert("생년월일 입력하여 주세요.")
		return;
	}
	
	// 성별을 선택하지 않은 경우
	const radio_checked_length = $("input:radio[name='gender']:checked").length;  
	if (radio_checked_length == 0) {
		alert("성별을 선택하셔야 합니다.");
		return;
	}
	
	// 이메일을 입력하지 않은 경우
	if (!emailCheckValid) {	
		alert("이메일을 입력하여 주세요.")
		return;
	}
	
	// 사내메일을 입력하지 않은 경우
	if (!mailCheckValid) {
		alert("사내메일을 다시 입력하여 주세요.");
		return;
	}
	
	// 전화번호를 입력하지 않은 경우
	if (!telCheckValid) {
		alert("전화번호를 입력하여 주세요.")
		return;
	}
	
	// 우편번호를 입력하지 않은 경우
	const postcode = $("input[name='postcode']").val();
	
	if (postcode.trim() === "") {
        alert("우편번호를 입력하세요.");
        return false;
    }
	
	// 지점을 선택하지 않은 경우
	const branch_no = $("select[name='branch_no']").val();
	if (branch_no == "") {
		alert("지점을 선택하셔야 합니다.");
		return;
	}
	
	// 부서를 선택하지 않은 경우
	const dept_id = $("select[name='dept_id']").val();				
	if (dept_id == "") {
		alert("부서를 선택하셔야 합니다.");
		return;
	}
	
	// 직급을 선택하지 않은 경우
	const grade_no = $("select[name='grade_no']").val();		
	if (grade_no == "") {
		alert("직급을 선택하셔야 합니다.");
		return;
	}
	
	const frm = document.editFrm;
	frm.action = "/syoffice/hr/employeeEdit";
	console.log("보내는 url : " + frm.action);
	frm.method = "POST";
	frm.submit();
	
};// end of function goEdit() -----		


//// 프로필 미리보기 ////
$(function(){
	$("input[name='profile_img']").change(function(event){
		const file = event.target.files;

		var image = new Image();
		var ImageTempUrl = window.URL.createObjectURL(file[0]);

		image.src = ImageTempUrl;

		$("#preview").html(image);
	});
});