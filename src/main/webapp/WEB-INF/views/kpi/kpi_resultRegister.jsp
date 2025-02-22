<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<jsp:include page="./sidebar.jsp" />

<style type="text/css">
	button.buttonBorder {
		border-radius: 5px;
		border: solid 1px black;
		width: 10em;
		height: 2.5em;
	}
</style>

<script type="text/javascript">
	$(document).ready(()=> {
		// === 엑셀파일 업로드 하기 시작 === //
	  	$("button#register").click(function(){
        	if($("input#upload_excel_file").val() == "") {
            alert("업로드할 엑셀파일을 선택하세요!!");
            return;
         }   
         else {
            let formData = new FormData($("form[name='excel_upload_frm']").get(0));   // 폼태그에 작성된 모든 데이터 보내기  
            // jQuery선택자.get(0) 은 jQuery 선택자인 jQuery Object 를 DOM(Document Object Model) element 로 바꿔주는 것이다. 
            // DOM element 로 바꿔주어야 순수한 javascript 문법과 명령어를 사용할 수 있게 된다.
            
            // 또는
            //   let formData = new FormData(document.getElementById("excel_upload_frm")); // 폼태그에 작성된 모든 데이터 보내기
            
            $.ajax({
                    url:"<%= request.getContextPath()%>/api/kpi/uploadExcelFile",
                    type:"post",
                    data:formData,
                    processData:false,  // 파일 전송시 설정 
                    contentType:false,  // 파일 전송시 설정 
                    dataType:"json",
                    success:function(json){
                       // console.log("~~~ 확인용 : " + JSON.stringify(json));
                        // ~~~ 확인용 : {"result":1}
                        if(json.result == 1) {
                           alert("성공");
                        }
                        else {
                        	alert("실패");
                        }
                    },
                    error: function(request, status, error){
                    alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
                    }
                });
         	}
    	});// end of $("button#btn_upload_excel").click(function(){}) ---------------
     	// ==== 엑셀관련파일 업로드 하기 끝 ====
	})// end of $(document).ready(()=> {}) -------------------------
</script>

	<div class="contents_wrapper">
        <div class="contents_inner_wrapper">

       	<div style="margin: 4% 0 4% 0">
	    	<span class="h3">발생실적 등록</span>
   		</div>
			<div style="width: 50; margin: 0 auto;">
				<form style="margin-bottom: 10px;" name="excel_upload_frm" method="post" enctype="multipart/form-data" >
		        	<input type="file" id="upload_excel_file" name="excel_file" accept=".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel" />
					<button type="button" class="buttonBorder" id="register" style="margin-right: 10px; background-color: #99ccff;">Excel 파일 업로드</button> 
		    	</form>
			</div>
		</div>
		
	</div>
</div>