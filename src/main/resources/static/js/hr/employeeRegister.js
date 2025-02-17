
let nameCheckVaild = false; // 이름이 유효한 경우 true

let birthdayCheckVaild = false; // 생년월일이 유효한 경우 true

let mobileCheckVaild = false; // 전화번호가 유효한 경우 true

$(document).ready(function() {
	
	$("div.error").hide();
	
	// 성명 유효성 검사
	$(document).on("blur", "input[name='name']", function(e) {
        const name = $(e.target).val();
        // 이름 유효성 검사
        if (!checkName(name)) {
            $("div.name_error").show();  
            nameCheckVaild = false; 
        } else {
            $("div.name_error").hide();  
            nameCheckVaild = true;
        }
    });
	
	// 생년월일 유효성 검사
	$("input#birthday").blur((e) => {
		const birthday = $(e.target).val(); // 생년월일
		
		
		if(!checkBirthday(birthday)) {
			$("div.error").show();
		}
		else{
			// 공백이 아닌 글자를 입력했을 경우
			$("div.error").hide();
			birthdayCheckVaild = true;
		}
		
	});	
	
	
	// ==== 이메일 값 사내메일 칸으로 보내주기 ==== //
	$("input#personal_mail").change(function() {
	    const personal_mail = $(this).val();
	    const mail = personal_mail.split('@')[0];
	    $("input#mail").val(mail + "@syoffice.syo");
	});
	// ==== 이메일 값 사내메일 칸으로 보내주기 ==== // 
	
	
	
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


function goRegister() {
	
	const frm = document.registerFrm;
	frm.action = "/syoffice/hr/employeeRegister";
	frm.method = "post";
	frm.submit();
	
};// end of function goRegister() -----		