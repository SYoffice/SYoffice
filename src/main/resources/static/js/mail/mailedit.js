
let isRecipient = false;    // 조직도 사용 시 수신인, 참조인 구분용
let file_arr = []; // 첨부된어진 파일 정보를 담아 둘 배열

window.onload = () => {

    getMailInfo();      // 메일 수신인 정보를 가져오는 함수
    getMailRecipientInfo();  // 메일 정보를 가져오는 함수

    // === 수신인 조직도 버튼 클릭 시 === //
    $("button#organization").on("click", function() {
        isRecipient = true;
       $("div#organization").modal();
    });// end of $("button#organization").on("click", function() {}) ------------------------

    // === 참조인 조직도 버튼 클릭 시 === //
    $("button#mailcc").on("click", function() {
        isRecipient = false;
       $("div#organization").modal();
    });// end of $("button#organization").on("click", function() {}) ------------------------


    // === 조직도 팝업에서 사원 클릭 시 이벤트 === //
    $('#jstree').on("select_node.jstree", function(e, data) {
        let nodeId = data.node.id;
        getEmployeeInfo(nodeId);
    });// end of $('#jstree').on("select_node.jstree", function(e, data) {}) ---------------------


    // === 수신자 직접 입력 시 검색 기능 === //
    $("input#recipient").on("input", function(e) {
        let searchWord = $(this).val();
        searchUserList(searchWord);
    });// end of $("input#recipient").on("input", function() {}) ---------------------

    
    // === 내게 쓰기 체크 이벤트 === //
    $("input:checkbox[name='receive_status']").on("change", function() {
        const isChecked = $("input:checkbox[name='receive_status']").is(":checked");

        if (isChecked) {
            const mail = $("input#mail").val();
            $("input#recipient").val(mail);         // 수신자에 자신
            $("input#recipient").attr("readonly", true);    // 수정못하도록
            $("button#organization").hide();        // 조직도 버튼 숨기기
            $("div.displayRecipientList").empty();  // 수신자 모음 비우기

            $("input#mail_cc").val("").attr("readonly", true);      // 참조 입력란 비우고 읽기 전용으로
            $("button#mailcc").hide();              // 조직도 버튼 숨기기
            $("div.displayCCUserList").empty();     // 참조자 모음 비우기
        }
        else {
            $("input#recipient").val("");         // 수신자 비우기
            $("input#recipient").removeAttr("readonly");
            $("button#organization").show();      // 조직도 버튼 보이기

            $("input#mail_cc").removeAttr("readonly");
            $("button#mailcc").show();
        }
        
    });// end of $("input:checkbox[name='receive_status']").on("change", function() {}) ------------------
    

    // === 메일 전송 버튼 클릭 시 === //
    $("button#sendMail").on("click", function() {
        const isChecked = $("input:checkbox[name='receive_status']").is(":checked");
        const sender = $("input#emp_id").val();

        if (isChecked) {
            // 나에게 보내기
            sendMailToMe(sender);
        }
        else {
            // 타인에게 보내기
            sendMail(sender);
        }
    });// end of $("button#sendMail").on("click", function()}) ---------------------------

    // === 메일 임시저장 버튼 클릭 시 === //
    $("button#tempStore").on("click", function() {
        const sender = $("input#emp_id").val();
        mailStoreTemp(sender);
    });// end of $("button#tempStore").on("click", function() {}) -----------------------------


    // === x아이콘 클릭시 수신인, 참조인 제거하기 === //
	$(document).on('click','div.displayRecipientList > span.plusUser > i',function(){
		$(this).parent().remove();
	});// end of $(document).on('click','div.displayRecipientList > span#plusUser > i',function(){}) --------------------- 

	$(document).on('click','div.displayCCUserList > span.plusUser > i',function(){
		$(this).parent().remove();
	});// end of $(document).on('click','div.displayCCUserList > span#plusUser > i',function(){}) --------------------- 
    // === x아이콘 클릭시 수신인, 참조인 제거하기 === //


    // === 파일첨부 Drag & Drop 이벤트 생성 시작 === //
    
    $("div#fileDrop").on("dragenter", function(e){
        e.preventDefault();     // 브라우저에서 pdf 보이기 방지
        e.stopPropagation();    // 이벤트 버블링 방지
    }).on("dragover", function(e){ /* "dragover" 이벤트는 드롭대상인 박스 안에 Drag 한 파일이 머물러 있는 중일 때. 필수이벤트이다. dragover 이벤트를 적용하지 않으면 drop 이벤트가 작동하지 않음 */ 
        e.preventDefault();
        e.stopPropagation();
        $(this).css("background-color", "#ffd8d8");
    }).on("dragleave", function(e){ /* "dragleave" 이벤트는 Drag 한 파일이 드롭대상인 박스 밖으로 벗어났을 때  */
        e.preventDefault();
        e.stopPropagation();
        $(this).css("background-color", "#fff");
    }).on("drop", function(e){
        e.preventDefault();

        var files = e.originalEvent.dataTransfer.files;

        if(files != null && files != undefined) {
            // 들어온 것이 파일이 맞으면

            let html = "";
            const file = files[0];
            let fileSize = file.size/1024/1024;

            if(fileSize >= 10) {
                alert("10MB 이상인 파일은 업로드할 수 없습니다.!!");
                $(this).css("background-color", "#fff");
                return;
            }

            file_arr.push(file);
            const fileName = file.name;
            fileSize = fileSize < 1 ? fileSize.toFixed(3) : fileSize.toFixed(1);
            // fileSize 가 1MB 보다 작으면 소수부는 반올림하여 소수점 3자리까지 나타내며, 
            // fileSize 가 1MB 이상이면 소수부는 반올림하여 소수점 1자리까지 나타낸다. 만약에 소수부가 없으면 소수점은 0 으로 표시한다.
            html += 
                "<div class='fileList'>" +
                    "<span class='delete'><i class='fa-solid fa-circle-minus'></i></span>" +  
                    "<span class='fileName'>"+fileName+"</span>" +
                    "<span class='fileSize'>"+fileSize+" MB</span>" +
                    "<span class='clear'></span>" +  // <span class='clear'></span> 의 용도는 CSS 에서 float:right; 를 clear: both; 하기 위한 용도이다. 
                "</div>";
            $(this).append(html);
        }// end of if(files != null && files != undefined) -------------------
        $(this).css("background-color", "#fff");
    });
    // === 파일 추가 끝 === //

    // == Drop 되어진 파일목록 제거하기 == //
    $(document).on("click", "span.delete > i", function(e){
        let index = $("span.delete").index($(e.target));

        file_arr.splice(index,1); // 드롭대상인 박스 안에 첨부파일을 드롭하면 파일들을 담아둘 배열인 file_arr 에서 파일을 제거시키도록 한다.
        $(e.target).parent().parent().remove();  // 화면에서 보여주는 것 삭제
    });// end of $(document).on("click", "span.delete", function(e){}) ---------------------

    // === 파일첨부 Drag & Drop 이벤트 생성 끝 === //

}// end of window.onload = () => {} ----------------------------



// Function Declaration
// === 조직도에서 사원 선택 시 수신인 혹은 참조인에 정보 담아주는 함수 === //
function getEmployeeInfo(emp_id) {

    // 사원 이외 선택 시 종료
    if (emp_id.length < 4) {
        return;
    }

    //alert("emp_id : "+ emp_id + " isRecipient : "+ isRecipient);

    $.ajax({
        url: $("input#path").val()+"/api/mail/getEmployeeInfo",
        type: "GET",
        data: {"emp_id": emp_id},
        success: function(json) {
            //console.log(JSON.stringify(json));
            /*
                {"mail":"seoyh@syoffice.syo","branch_name":"본사","name":"서영학","dept_name":"임원진","emp_id":"2025001"}
            */
            const mail = json.mail;
            const branch_name = json.branch_name
            const name = json.name
            const dept_name = json.dept_name
            const emp_id = json.emp_id

            const recipient = `${name} &lt;${mail}&gt;`;
            if (isRecipient){
                addRecipient(recipient, emp_id);
            }
            else {
                addCCUser(recipient, emp_id);
            }
        },
        error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }
    });

}// end of function getEmployeeInfo() ------------------------




// div.displayRecipientList 에 수신자를 넣어주는 함수
function addRecipient(value, fk_emp_id){  // value 가 수신자로 선택한이름 이다.
	var plusUser_re = $("div.displayRecipientList > span.plusUser > input").val();
    var plusUser_cc = $("div.displayCCUserList > span.plusUser > input").val();

    if (fk_emp_id == $("input#emp_id").val()){
        Swal.fire({
            title: '본인은 추가할 수 없습니다.',        // Alert 제목
            icon: 'warning',
            confirmButtonText: "확인"
        });
        return;
    }

	if(plusUser_re == fk_emp_id || plusUser_cc == fk_emp_id) {
		//alert("이미 추가한 사원입니다.");
        Swal.fire({
            title: '이미 추가한 사원입니다.',        // Alert 제목
            icon: 'warning',
            confirmButtonText: "확인"
        })
        .then((result) => {
            $("button#organization").click();
        })
	}
	else {

        const length = $("div.displayRecipientList").children("span").length;
        if (length >= 4) {
            $("div.displayRecipientList").append("<br><br>")
        }

		$("div.displayRecipientList").append("<span class='plusUser joinBorder'>"+value+"&nbsp;<i style='cursor: pointer;' class='fas fa-times-circle'></i><input class='recipient' type='hidden' value='"+fk_emp_id+"' /></span>");
	}
	$("div#organization").modal('hide');
}// end of function addRecipient(value){}----------------------------


// div.displayCCUserList 에 참조자를 넣어주는 함수
function addCCUser(value, fk_emp_id){  // value 가 수신자로 선택한이름 이다.
	//var plusUser_es = $("div.displayCCUserList > span.plusUser").text();
    var plusUser_cc = $("div.displayCCUserList > span.plusUser > input").val();
    var plusUser_re = $("div.displayRecipientList > span.plusUser > input").val();
 // console.log("확인용 plusUser_es => " + plusUser_es);

    if (fk_emp_id == $("input#emp_id").val()){
        Swal.fire({
            title: '본인은 추가할 수 없습니다.',        // Alert 제목
            icon: 'warning',
            confirmButtonText: "확인"
        });
        return;
    }

	if(plusUser_cc == fk_emp_id || plusUser_re == fk_emp_id) {
		//alert("이미 추가한 사원입니다.");
        Swal.fire({
            title: '이미 추가한 사원입니다.',        // Alert 제목
            icon: 'warning',
            confirmButtonText: "확인"
        })
        .then((result) => {
            $("button#mailcc").click();
        })
	}
	else {

        const length = $("div.displayCCUserList").children("span").length;
        if (length >= 4) {
            $("div.displayCCUserList").append("<br><br>")
        }

		$("div.displayCCUserList").append("<span class='plusUser joinBorder'>"+value+"&nbsp;<i style='cursor: pointer;' class='fas fa-times-circle'></i><input class='cc' type='hidden' value='"+fk_emp_id+"' /></span>");
	}
	$("div#organization").modal('hide');
}// end of function addCCUser(value){}----------------------------


function searchUserList(joinSearchWord) {
    $.ajax({
        url: $("input#path").val()+"/schedule/insertSchedule/searchJoinUserList",
        data: {"joinSearchWord": joinSearchWord},
        dataType:"json",
        success : function(json){
            var joinUserArr = [];
            console.log(JSON.stringify(json));
            
            if(json.length > 0){
                $.each(json, function(index, item){
                    var name = item.name;
                    if(name.includes(joinSearchWord) || item.dept_name.includes(joinSearchWord) || item.branch_name.includes(joinSearchWord)){
                        // name 이라는 문자열에 joinSearchWord 라는 문자열이 포함된 경우라면 true , 
                        // name 이라는 문자열에 joinUserName 라는 문자열이 포함되지 않은 경우라면 false 
                           joinUserArr.push(name+"("+item.branch_name+"-"+item.dept_name+")");
                    }
                });
                
                
                $("input#recipient").autocomplete({  // 참조 https://jqueryui.com/autocomplete/#default
                    source:joinUserArr,
                    select: function(event, ui) {       // 자동완성 되어 나온 공유자이름을 마우스로 클릭할 경우 
                        addRecipient(ui.item.value);    // 아래에서 만들어 두었던 add_joinUser(value) 함수 호출하기 
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
}// end of function searchUserList() ----------------------


// === 메일 전송 함수(타인) === //
function sendMail(sender) {
    //alert(sender);    // 발신인 확인

    // 수신인 데이터 전송
    // 3가지 데이터 배열 필요(수신인, 참조여부, 메일함

    // 수신인 입력 확인
    let $recipient = document.querySelectorAll("div.displayRecipientList > span.plusUser > input.recipient");
    let recipientArr = [];
    if($recipient.length == 0) {
        Swal.fire({
            title: '수신인을 입력하세요!',        // Alert 제목
            icon: 'warning',
            confirmButtonText: "확인"
        });
        return;
    }

    // 메일 제목 입력 확인
    const mail_subject = $("input#mail_subject").val().trim();
    if(mail_subject === "") {
        $("input#mail_subject").val("");
        Swal.fire({
            title: '메일 제목을 입력하세요!',        // Alert 제목
            icon: 'warning',
            confirmButtonText: "확인"
        });
        return;
    }

    // 메일 본문 입력 확인
    const mail_content = $("textarea#mail_content").val().trim();
    if(mail_content === "") {
        $("textarea#mail_content").val("");
        Swal.fire({
            title: '메일 내용을 입력하세요!',        // Alert 제목
            icon: 'warning',
            confirmButtonText: "확인"
        });
        return;
    }

    // 수신자 넣어주기
    $recipient.forEach(item => {
        recipientArr.push(item.value);
    });// end of $recipient.forEach(item => {}) -------------------------

    //console.log(recipientArr);
    // ['2025032', '2025011']
    let recipient = recipientArr.join(",");
    $("input[name='recipient']").val(recipient);

    // 참조인 넣어주기
    let $ccuser = document.querySelectorAll("div.displayCCUserList > span.plusUser > input.cc");
    let ccuserArr = [];
    if ($ccuser.length > 0) {
        // 참조한 사람이 있을 경우

        $ccuser.forEach(item => {
            ccuserArr.push(item.value);
        });// end of $ccuser.forEach(item => {}) -------------------------

        let ccuser = ccuserArr.join(",");
        $("input[name='ccuser']").val(ccuser);
    }
    
    var formData = new FormData($("form[name='mailSend']").get(0));

    if(file_arr.length > 0) { // 파일첨부가 있을 경우 
              
        // 첨부한 파일의 총합의 크기가 10MB 이상 이라면 메일 전송을 하지 못하게 막는다.
        let sum_file_size = 0;
        for(let i=0; i<file_arr.length; i++) {
            sum_file_size += file_arr[i].size;
        }// end of for---------------
          
        if( sum_file_size >= 10*1024*1024 ) { // 첨부한 파일의 총합의 크기가 10MB 이상 이라면 
            Swal.fire({
                title: '첨부한 파일의 총합의 크기가 10MB 이상이라서 파일을 업로드할 수 없습니다.!!',        // Alert 제목
                icon: 'error',
                confirmButtonText: "확인"
            });
            return; // 종료
        }
        else { // formData 속에 첨부파일 넣어주기
            file_arr.forEach(function(item){
                formData.append("file_arr", item);  // 첨부파일 추가하기.  "file_arr" 이 키값이고  item 이 밸류값인데 file_arr 배열속에 저장되어진 배열요소인 파일첨부되어진 파일이 되어진다.    
                                                    // 같은 key를 가진 값을 여러 개 넣을 수 있다.(덮어씌워지지 않고 추가가 된다.)
            });
        }
    }

    // 중요메일 여부 확인
    const isImportant = $("input:checkbox[name='mail_important']").is(":checked");
    if (isImportant) {
        formData.append("mail_important", 1);
    }
    else {
        formData.append("mail_important", 0);
    }

    formData.append("sender", sender);  // 발신인 추가

    $.ajax({
        url: $("input#path").val()+"/api/mail/sendMail",
        type: "POST",
        data : formData,
        processData: false,  // 파일 전송시 설정
        contentType: false,  // 파일 전송시 설정
        dataType: "JSON",
        success:function(json){
            console.log(JSON.stringify(json));
            
            if (json.result == 1) {
                Swal.fire({
                    title: '메일을 발송했습니다.',        // Alert 제목
                    icon: 'success',
                    confirmButtonText: "확인"
                })
                .then((result) => {
                    location.href = $("input#path").val()+"/mail/box/0";	// 받은 메일함으로 이동
                })
            }
            else {
                Swal.fire({
                    title: '메일 발송에 실패했습니다.',        // Alert 제목
                    icon: 'error',
                    confirmButtonText: "확인"
                })
                .then((result) => {
                    location.href = $("input#path").val()+"/mail/box/0";	// 받은 메일함으로 이동
                })
            }
            
        },
        error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }
    });

}// end of function sendMail() --------------------------


// === 메일 전송 함수(자신) === //
function sendMailToMe(sender) {
    //alert(sender);    // 발신인 확인

    // 수신인 데이터 전송
    // 3가지 데이터 배열 필요(수신인, 참조여부, 메일함

    // 수신인 입력 확인
    let recipient = sender;
    $("input[name='recipient']").val(recipient);

    // 메일 제목 입력 확인
    const mail_subject = $("input#mail_subject").val().trim();
    if(mail_subject === "") {
        $("input#mail_subject").val("");
        Swal.fire({
            title: '메일 제목을 입력하세요!',        // Alert 제목
            icon: 'warning',
            confirmButtonText: "확인"
        });
        return;
    }

    // 메일 본문 입력 확인
    const mail_content = $("textarea#mail_content").val().trim();
    if(mail_content === "") {
        $("textarea#mail_content").val("");
        Swal.fire({
            title: '메일 내용을 입력하세요!',        // Alert 제목
            icon: 'warning',
            confirmButtonText: "확인"
        });
        return;
    }

    var formData = new FormData($("form[name='mailSend']").get(0));

    if(file_arr.length > 0) { // 파일첨부가 있을 경우 
              
        // 첨부한 파일의 총합의 크기가 10MB 이상 이라면 메일 전송을 하지 못하게 막는다.
        let sum_file_size = 0;
        for(let i=0; i<file_arr.length; i++) {
            sum_file_size += file_arr[i].size;
        }// end of for---------------
          
        if( sum_file_size >= 10*1024*1024 ) { // 첨부한 파일의 총합의 크기가 10MB 이상 이라면 
            Swal.fire({
                title: '첨부한 파일의 총합의 크기가 10MB 이상이라서 파일을 업로드할 수 없습니다.!!',        // Alert 제목
                icon: 'error',
                confirmButtonText: "확인"
            });
            return; // 종료
        }
        else { // formData 속에 첨부파일 넣어주기
            file_arr.forEach(function(item){
                formData.append("file_arr", item);  // 첨부파일 추가하기.  "file_arr" 이 키값이고  item 이 밸류값인데 file_arr 배열속에 저장되어진 배열요소인 파일첨부되어진 파일이 되어진다.    
                                                    // 같은 key를 가진 값을 여러 개 넣을 수 있다.(덮어씌워지지 않고 추가가 된다.)
            });
        }
    }

    // 중요메일 여부 확인
    const isImportant = $("input#important").is(":checked");
    if (isImportant) {
        formData.append("mail_important", 1);
    }
    else {
        formData.append("mail_important", 0);
    }

    formData.append("sender", sender);  // 발신인 추가

    $.ajax({
        url: $("input#path").val()+"/api/mail/sendMailToMe",
        type: "POST",
        data : formData,
        processData: false,  // 파일 전송시 설정
        contentType: false,  // 파일 전송시 설정
        dataType: "JSON",
        success:function(json){
            console.log(JSON.stringify(json));
            
            if (json.result == 1) {
                Swal.fire({
                    title: '메일을 발송했습니다.',        // Alert 제목
                    icon: 'success',
                    confirmButtonText: "확인"
                })
                .then((result) => {
                    location.href = $("input#path").val()+"/mail/box/0";	// 받은 메일함으로 이동
                })
            }
            else {
                Swal.fire({
                    title: '메일 발송에 실패했습니다.',        // Alert 제목
                    icon: 'error',
                    confirmButtonText: "확인"
                })
                .then((result) => {
                    location.href = $("input#path").val()+"/mail/box/0";	// 받은 메일함으로 이동
                })
            }
            
        },
        error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }
    });

}// end of function sendMailToMe(sender) --------------------------


// === 메일 임시저장 === //
function mailStoreTemp(sender) {

    Swal.fire({
		title: "메일 임시저장",
        text: "첨부파일은 임시저장 되지 않습니다.\n진행하시겠습니까?",
		icon : "question",
		showCancelButton: true,
		confirmButtonText: "확인",
		cancelButtonText: "취소",
	})
    .then((result) => {
        // 수신인 입력 확인
        if (!result.isConfirmed) {
            return;
        }

        let $recipient = document.querySelectorAll("div.displayRecipientList > span.plusUser > input.recipient");
        let recipientArr = [];
        const isChecked = $("input:checkbox[name='receive_status']").is(":checked");
        if (isChecked) {
            // 나에게 전송일 경우
            $("input[name='recipient']").val(sender);
        }
        else {
            if($recipient.length == 0) {
                Swal.fire({
                    title: '수신인을 입력하세요!',        // Alert 제목
                    icon: 'warning',
                    confirmButtonText: "확인"
                });
                return;
            }
            // 수신자 넣어주기
            $recipient.forEach(item => {
                recipientArr.push(item.value);
            });// end of $recipient.forEach(item => {}) -------------------------

            //console.log(recipientArr);
            // ['2025032', '2025011']
            let recipient = recipientArr.join(",");
            $("input[name='recipient']").val(recipient);
        }

        // 메일 제목 입력 확인
        const mail_subject = $("input#mail_subject").val().trim();
        if(mail_subject === "") {
            $("input#mail_subject").val("");
            Swal.fire({
                title: '메일 제목을 입력하세요!',        // Alert 제목
                icon: 'warning',
                confirmButtonText: "확인"
            });
            return;
        }

        // 메일 본문 입력 확인
        const mail_content = $("textarea#mail_content").val().trim();
        if(mail_content === "") {
            $("textarea#mail_content").val("");
            Swal.fire({
                title: '메일 내용을 입력하세요!',        // Alert 제목
                icon: 'warning',
                confirmButtonText: "확인"
            });
            return;
        }

        // 참조인 넣어주기
        let $ccuser = document.querySelectorAll("div.displayCCUserList > span.plusUser > input.cc");
        let ccuserArr = [];
        if ($ccuser.length > 0) {
            // 참조한 사람이 있을 경우

            $ccuser.forEach(item => {
                ccuserArr.push(item.value);
            });// end of $ccuser.forEach(item => {}) -------------------------

            let ccuser = ccuserArr.join(",");
            $("input[name='ccuser']").val(ccuser);
        }
        
        var formData = new FormData($("form[name='mailSend']").get(0));

        // 중요메일 여부 확인
        const isImportant = $("input#important").is(":checked");
        if (isImportant) {
            formData.append("mail_important", 1);
        }
        else {
            formData.append("mail_important", 0);
        }

        formData.append("sender", sender);  // 발신인 추가
        formData.append("mail_no", $("input#mail_no").val());  // 메일번호 추가

        $.ajax({
            url: $("input#path").val()+"/api/mail/mailStoreTemp",
            type: "PUT",
            data : formData,
            processData: false,  // 파일 전송시 설정
            contentType: false,  // 파일 전송시 설정
            dataType: "JSON",
            success:function(json){
                console.log(JSON.stringify(json));
                
                if (json.result == 1) {
                    Swal.fire({
                        title: '임시저장 되었습니다.',        // Alert 제목
                        icon: 'success',
                        confirmButtonText: "확인"
                    })
                    .then((result) => {
                        location.href = $("input#path").val()+"/mail/box/0";	// 받은 메일함으로 이동
                    })
                }
                else {
                    Swal.fire({
                        title: '에러가 발생했습니다.',        // Alert 제목
                        icon: 'error',
                        confirmButtonText: "확인"
                    })
                    .then((result) => {
                        location.href = $("input#path").val()+"/mail/box/0";	// 받은 메일함으로 이동
                    })
                }
                
            },
            error: function(request, status, error){
                alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
            }
        });
    });
}// end of function mailStoreTemp(sender) ----------------------


// === 메일 수신인 정보를 가져오는 함수 === //
function getMailRecipientInfo() {
    const mail_no  = $("input#mail_no").val();
    const fk_emp_id = $("input#emp_id").val();
    //alert(mail_no);

    $.ajax({
        url: $("input#path").val()+"/api/mail/"+mail_no,
        type: "GET",
        data: {"fk_emp_id": fk_emp_id},
        dataType: "JSON",
        success: function(json){
            console.log(JSON.stringify(json));
            /*
                [{"receive_no":"20","receiver":"2025021","receivercc":"0","receiver_name":"강이훈","receiver_mail":"kang110@syoffice.syo"}
                ,{"receive_no":"21","receiver":"2025047","receivercc":"0","receiver_name":"이보니","receiver_mail":"boni123@syoffice.syo"}
                ,{"receive_no":"22","receiver":"2025032","receivercc":"0","receiver_name":"김회계","receiver_mail":"qwer1111@syoffice.syo"}
                ,{"receive_no":"23","receiver":"2025017","receivercc":"1","receiver_name":"이영학","receiver_mail":"qwer12@syoffice.syo"}
                ,{"receive_no":"24","receiver":"2025016","receivercc":"1","receiver_name":"박영학","receiver_mail":"qwer1@syoffice.syo"}]
            */
            $.each(json, function(index, item) {
                const recipient = `${item.receiver_name} &lt;${item.receiver_mail}&gt;`;
                const receiver  = item.receiver;

                if (json.length == 1 && fk_emp_id == receiver) {
                    // 내게 쓰기인 경우
                    const mail = $("input#mail").val();
                    $("input#recipient").val(mail);         // 수신자에 자신
                    $("input#recipient").attr("readonly", true);    // 수정못하도록
                    $("button#organization").hide();        // 조직도 버튼 숨기기
                    $("div.displayRecipientList").empty();  // 수신자 모음 비우기

                    $("input:checkbox[name='receive_status']").prop("checked", true);   // 체크박스에 체크

                    $("input#mail_cc").val("").attr("readonly", true);      // 참조 입력란 비우고 읽기 전용으로
                    $("button#mailcc").hide();              // 조직도 버튼 숨기기
                    $("div.displayCCUserList").empty();     // 참조자 모음 비우기
                    return;
                }
                
                // 본인은 제외 되어야 한다.
                if(fk_emp_id != receiver){
                    if (item.receivercc == 0) {
                        // 수신자일 경우
                        addRecipient(recipient, receiver);
                    }
                    
                    if (item.receivercc == 1) {
                        addCCUser(recipient, receiver);
                    }
                }
            });// end of $.each(json, function(index, item) {}}) --------------
            
        },
        error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }
    });
}// end of function getMailInfo() ---------------------


// === 메일 정보를 가져오는 함수 === //
function getMailInfo() {
    const mail_no  = $("input#mail_no").val();
    const fk_emp_id = $("input#emp_id").val();

    $.ajax({
        url: $("input#path").val()+"/api/mail/file/"+mail_no,
        type: "GET",
        data: {"fk_emp_id": fk_emp_id},
        dataType: "JSON",
        success: function(json){
            console.log(JSON.stringify(json));

            /*
                {"mail_no":"20","fk_emp_id":"2025032","mail_subject":"ㅇ미시저장테스트","mail_content":"ㄴㅇㅁㄹㄴㅇㄹ","mail_senddate":"2025-03-06 14:06:35","mail_important":"0","receiver":null,"receivercc":null,"receiver_name":null,"receiver_mail":null,"sender":null,"sender_mail":null,"sender_name":null,"atmail_no":null,"atmail_filename":null,"atmail_orgfilename":null,"atmail_filesize":null,"receive_no":null}
            */
            $("input#mail_subject").val(json.mail_subject)       // 제목입력
            $("textarea#mail_content").val(json.mail_content)    // 내용입력
            if (json.mail_important == 1) {
                $("input#important").prop("checked", true);      // 중요 체크
            }
        },
        error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }
    });
}// end of function getMailFileInfo() ---------------------