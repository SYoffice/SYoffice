<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
   String ctxPath = request.getContextPath();
    //     /myspring
%>

<jsp:include page="../board/GroupWareHeader.jsp" /> 


<script type="text/javascript">

	$(document).ready(function(){
		
		$("select[name='fk_bcate_no']").hide();
		
		/* 스마트 에디터를 사용할 경우 */
		<%-- === 스마트 에디터 구현 시작 === --%>
      //전역변수
       var obj = [];
       
       //스마트에디터 프레임생성
       nhn.husky.EZCreator.createInIFrame({
           oAppRef: obj,
           elPlaceHolder: "content",
           sSkinURI: "<%= ctxPath%>/smarteditor/SmartEditor2Skin.html",
           htParams : {
               // 툴바 사용 여부 (true:사용/ false:사용하지 않음)
               bUseToolbar : true,            
               // 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
               bUseVerticalResizer : true,    
               // 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
               bUseModeChanger : true,
           }
       });
       <%-- === 스마트 에디터 구현 끝 === --%>
		
		// 글쓰기 게시판 위치 설정하기 시작 //
		$("select[name='boardLocation']").on('change',function(e) {
			const boardLocation = $(e.target).val();
			const boardDept = $("option[value='boardDept']");
			
		    if(boardLocation == "boardDept"){
		    	$("select[name='notice']").hide();
		    	$("select[name='fk_bcate_no']").show();
		    	$("select[name='fk_bcate_no']").val("ctg");
		    }
		    else {
		    	$("select[name='notice']").show();
		    	$("select[name='fk_bcate_no']").hide();
		    }
		});
		
		
		
		// 글쓰기 게시판 위치 설정하기 끝 //
		
		

		$("button#add").click(function(){ // 등록 버튼	
		
			/* 스마트 에디터를 사용할 경우 */
			<%-- === 스마트 에디터 구현 시작 === --%>
		     // id가 content인 textarea에 에디터에서 대입
		     obj.getById["content"].exec("UPDATE_CONTENTS_FIELD", []);
		    <%-- === 스마트 에디터 구현 끝 === --%>
	    
		    // 게시글 위치 유효성 검사 시작 //
		    const boardLocation = $("select[name='boardLocation']").val();
			const boardDept = $("option[value='boardDept']");
		    
		    if(boardLocation == "boardDept"){
				if($("select[name='fk_bcate_no']").val() == null ){
					alert("작성하실 글의 카테고리를 선택하세요!");
					return; // 종료
				}
		    }
			// 게시글 위치 유효성 검사 끝 //
	    
			// 제목 유효성 검사 시작 //
			const subject = $("input[name='subject']").val().trim();
			
			if(subject == ""){
				alert("제목을 입력하세요!");
				$("input[name='subject']").val("");
				$("input[name='subject']").focus();
				return; // 함수 종료
			}
			// 제목 유효성 검사 끝 //
	    
		
		    // === 글내용 유효성 검사(스마트 에디터를 사용할 경우) === //
		    let content_val = $("textarea[name='content']").val().trim();
	       
		    // alert(content_val);  // content 에 공백만 여러개를 입력하여 쓰기할 경우 알아보는 것
		    // <p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</p> 이라고 나온다.
	       
	        content_val = content_val.replace(/&nbsp;/gi, ""); // 공백(&nbsp;)을 "" 으로 변환
		    /*    
	             대상문자열.replace(/찾을 문자열/gi, "변경할 문자열");
	           ==> 여기서 꼭 알아야 될 점은 나누기(/)표시안에 넣는 찾을 문자열의 따옴표는 없어야 한다는 점입니다. 
	                       그리고 뒤의 gi는 다음을 의미합니다.
	        
	           g : 전체 모든 문자열을 변경 global
	           i : 영문 대소문자를 무시, 모두 일치하는 패턴 검색 ignore
		    */
	        // alert(content_val);
	   		// <p>                 </p>     
	      
	      	content_val = content_val.substring(content_val.indexOf("<p>")+3);
	   		// alert(content_val);
	      	//                              </p>
	      
	     	content_val = content_val.substring(0, content_val.indexOf("</p>")  );
	   		// alert(content_val);
	      
	        if(content_val.trim().length == 0){
	           alert("글내용을 입력하세요!!");
	           return;   // 종료
	        }
		
			// 공개설정 유효성 검사 시작 //
			const security = $("input[name='board_show']:checked").length;
			
			if(security == ""){
				alert("공개설정을 선택해주세요!");
				$("input[type='radio']").focus();
				return; // 함수 종료
			}
			// 공개설정 유효성 검사 끝 //
		
		
			// 폼(form)을 전송(submit)
			const frm = document.BoardFrm;
			frm.method = "post";
			frm.action = "<%= ctxPath%>/board/GroupWare_Write"; 
	        frm.submit();
	        
		});// end of $("button#add").click(function(){})-----------------
		
	});// end of $(document).ready(function(){})--------------------------------------
	
</script>


<!-- 페이지 공통 부분  -->
	<p class="bg-light text-dark" style="font-size: 20pt; font-weight: bold; padding: 1% 0 1% 3%;">글쓰기 홈<i style="font-size:30px; margin-left: 2%;" class='far'>&#xf044;</i></p>
<!-- 페이지 공통 부분  -->

<div style="width: 90%;">

	<form name="BoardFrm" enctype="multipart/form-data">
		<i style="font-size:20px; margin-left: 5.5%; margin-top: 4%;" class='fas'>&#xf08d; 위치 :</i>
		
			<span class="dropdown">
			   <select class="form-select" name="boardLocation" style="height: 30px; margin: 0 0 2% 1%;">
			       <option value="notice">전체 게시판</option>
			       <option value="boardDept">부서 게시판[부서명]</option>
			   </select>
			</span>
		
			
			<span class="dropdown">
			   <select class="form-select" name="fk_bcate_no" style="height: 30px; margin: 0 0 2% 0;">
			       <option value="ctg" selected disabled>카테고리</option>
			       <option value="1">신간도서</option>
			       <option value="2">오늘의 뉴스</option>
			       <option value="3">주간식단표</option>
			       <option value="4">무엇이든 물어보세요!</option>
			   </select>
			</span>
			
			
			
			<span class="dropdown">
			   <select class="form-select" name="notice" style="height: 30px; margin: 0 0 2% 0;">
			       <option value="notice">공지사항</option>
			   </select>
			</span>
			
			<br>
			
			
			
			<div style="display: flex; margin-left: 4.5%; margin-top: 2%;">
				<span style="font-size:20px; text-align: center; width: 60px;" class='fas'>제목</span>
				<input type="text" name="subject" class="form-control" id="subject" style="width: 550px; margin-left: 2%;"/>
			</div>
			
			
			
			<div style="margin-top: 4%; margin-left: 5%;">
				<span style="font-size:20px; text-align: center; width: 90px; margin-bottom: 3%;" class='fas'>파일첨부</span>
				
				<div class="container p-3 border" style="width: 100%; margin-left: 0;">	
					<input type="file" class="form-control-file border" style=" font-size:15px; margin-left: 1%;" name="attach">
				</div>
			</div>
			
			
			
			<div style="margin-top: 4%; margin-left:5%; ">
				<span style="font-size:20px; text-align: left; width: 90px; margin-bottom: 3%;" class='fas'>내용</span>
				
				<button type="button" class="btn btn-outline-secondary" style="margin-right:15%; float: right;" data-toggle="modal" data-target="#myModal">임시저장글보기</button>
				
				<div class="container p-3 border" style="width: 100%; margin-left: 0;">	
					<textarea name="content" class="form-control-file border" style="width: 100%; height: 612px;" name="content" id="content"></textarea>
				</div>
			</div>
			
			
			
			<div style="margin-top: 4%; margin-left: 5%;">
				<span style="font-size:20px; text-align: left; width: 90px; margin-bottom: 3%;" class='fas'>공개설정</span>
				
				<div class="form-check-inline" style="margin-left: 3%;">
				  <label class="form-check-label">
				    <input type="radio" class="form-check-input" name="board_show" value="1">공개
				  </label>
				</div>
				
				<div class="form-check-inline" style="margin-left: 4%;">
				  <label class="form-check-label">
				    <input type="radio" class="form-check-input" name="board_show" value="0">비공개
				  </label>
				</div>
				
				<!-- <div style="display: flex;  margin-top: 2%;">
					<span style="font-size:20px; text-align: center; width: 60px; margin-right: 5%" class='fas'>글암호</span>
					<input type="text" class="form-control" id="pwd" style="width: 200px; height: 30px; margin-left: 2%;" name="pwd"/>
				</div> -->
				
		    </div>
			   
			<hr style="border-color: #e6ecff; margin: 1% 0 3% 0;">
			
			<div style="text-align: center; margin-bottom: 3%;">
				<button style="margin-right: 4%;" type="button" class="btn btn-info" id="add">등록</button>
				<button type="button" class="btn btn-secondary" onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_Board'">취소</button>
			</div>
			
			<input type="text" name="fk_emp_id" value="${sessionScope.loginuser.fk_emp_id}"/>
	</form>
</div>





<!-- 임시저장글 클릭시 모달창 만들기 -->
<!-- 모달창으로 임시저장글을 클릭하면 등록,취소 버튼이 => 등록(insert),삭제(status 변경) 버튼으로 바뀌어야한다. -->
<div class="container">
  <!-- The Modal -->
  <div class="modal fade" id="myModal">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
      
        <!-- Modal Header -->
        <div class="modal-header">
          <h4 class="modal-title">임시저장글 보기</h4>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>
        
        <!-- Modal body -->
        <div class="modal-body">
          임시저장글입니다.
        </div>
        
        
        <!-- Modal footer -->
        <div class="modal-footer">
          <button type="button" class="btn btn-success" data-dismiss="modal">확인</button>
        </div>
        
      </div>
    </div>
  </div>
</div>




<jsp:include page="../board/GroupWarefooter.jsp" /> 