<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
   String ctxPath = request.getContextPath();
    //     /myspring
%>

<jsp:include page="../main/header.jsp" />
<link rel="stylesheet" href="<%= ctxPath%>/css/board/common.css">

<script type="text/javascript">

	$(document).ready(function(){
		
		$("select[name='fk_bcate_no']").hide();
		$("tr#board_show").hide();
		
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
			const notice = $("option[value='notice']");

			if(boardLocation == "notice"){
				$("tr#board_show").hide();
			}
			else {
				$("tr#board_show").show();
			}

		    if(boardLocation == "boardDept"){
		    	$("select[name='notice']").hide();
		    	$("select[name='fk_bcate_no']").show();
		    	$("select[name='fk_bcate_no']").val("카테고리");
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
			
			if(boardLocation == "boardDept"){
				if(security == ""){
					alert("공개설정을 선택해주세요!");
					$("input[type='radio']").focus();
					return; // 함수 종료
				}
			
			// 공개설정 유효성 검사 끝 //
			}
			
			
			// 위치 설정에 따라 각 URL로 폼 데이터 보내주기 //
			if(boardLocation == "boardDept") {
				// 폼(form)을 전송(submit)
				const frm = document.BoardFrm;
				frm.method = "post";
				frm.action = "<%= ctxPath%>/board/GroupWare_deptWrite"; 
		        frm.submit();
			}
			else if(boardLocation == "notice"){
				const frm = document.BoardFrm;
				frm.method = "post";
				frm.action = "<%= ctxPath%>/board/GroupWare_noticeWrite"; 
		        frm.submit();
			}
			
			

			
		});// end of $("button#add").click(function(){})-----------------
		
		$("button#temporaryBoard").click(function(){// 임시저장 버튼을 클릭
			
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
			
			if(boardLocation == "boardDept"){
				if(security == ""){
					alert("공개설정을 선택해주세요!");
					$("input[type='radio']").focus();
					return; // 함수 종료
				}
			// 공개설정 유효성 검사 끝 //
			}
			
			//	alert("임시저장 클릭");
			// 폼(form)을 전송(submit)
			const frm = document.BoardFrm;
			frm.method = "post";
			frm.action = "<%= ctxPath%>/board/temporaryBoard";
	        frm.submit();
		});
		
		
		
	});// end of $(document).ready(function(){})--------------------------------------
	
</script>

<c:if test="${not empty message}">
    <script>
        alert("${message}");
    </script>
</c:if>

<div class="common_wrapper" style="margin-top: 0;">
    <div class="side_menu_wrapper">
        <div class="side_menu_inner_wrapper">
      	<button type="button"  id="write" onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_Write'">글쓰기</button>
           <ul class="side_menu_list">
                <li style="font-weight: bold;"><a href="<%= ctxPath%>/board/GroupWare_noticeBoard?boardLocation=notice">전체 게시판</a></li>
                <li style="margin-left: 10%; font-size: 11pt;"><a href="<%= ctxPath%>/board/GroupWare_noticeBoard?boardLocation=notice">공지사항</a></li>
                <li style="font-weight: bold;"><a href="<%= ctxPath%>/board/GroupWare_Board?boardLocation=boardDept">부서 게시판[]</a></li>
                <li style="margin-left: 10%; font-size: 11pt;"><a href="#">신간도서</a></li>
                <li style="margin-left: 10%; font-size: 11pt;"><a href="#">오늘의 뉴스</a></li>
                <li style="margin-left: 10%; font-size: 11pt;"><a href="#">주간식단표</a></li>
                <li style="margin-left: 10%; font-size: 11pt;"><a href="#">무엇이든 물어보세요!</a></li>
            </ul>
        </div>
    </div>



        
<div class="contents_wrapper" style="margin-top: 3%;">
        	
<!-- 페이지 공통 부분  -->
<div style="border: solid 0px gray; display: flex;">
	<p class="bg-light text-dark" style="font-size: 20pt; font-weight: bold; padding: 1% 0 1% 4.5%;">글쓰기 홈</p><i style='font-size:30px; margin-left: 2%; padding-top: 1%;' class='far'>&#xf328;</i>
</div>	  
<!-- 페이지 공통 부분  -->


<div style="width: 90%;">
	<form name="BoardFrm" enctype="multipart/form-data">
		<table style="width: 100%; margin-left: 5%; margin-top: 3%; ">
			 <tr style="margin-bottom: 5%; border: solid 0px red; height: 60px;">
			    <th style="width: 10%;"><i style="font-size:20px;" class='fas'>&#xf08d; 위치 </i></th>
			    <td >
					<span class="dropdown">
					   <select class="form-select" name="boardLocation">
					       <option value="notice">전체 게시판</option>
					       <option value="boardDept">부서 게시판[]</option>
					   </select>
					</span>
					
					<span class="dropdown">
					   <select class="form-select" name="fk_bcate_no">
					       <option selected disabled>카테고리</option>
					       <option value="1">신간도서</option>
					       <option value="2">오늘의 뉴스</option>
					       <option value="3">주간식단표</option>
					       <option value="4">무엇이든 물어보세요!</option>
					   </select>
				   </span>
				   <span class="dropdown">
				   <select class="form-select" name="notice">
				       <option value="notice">공지사항</option>
				   </select>
				   </span>
				   <button type="button" class="btn btn-outline-secondary" style="float: right; width: 20%;" data-toggle="modal" data-target="#myModal">임시저장글보기</button>
				</td>
				
			 </tr> 
			          	
			<tr style="margin-bottom: 5%; border: solid 0px red; height: 60px;">
				<td style="width: 10%;">
					<span style="font-size:20px;" class='fas'>제목</span>
				</td>
				<td>
					<input type="text" name="subject" class="form-control" id="subject"/>
				</td>
			</tr>
			<tr style="margin-bottom: 5%; border: solid 0px red; height: 60px;">
				<td style="width: 10%;">
					<span style="font-size:20px;" class='fas'>파일첨부</span>
				</td>
				<td>
					<input type="file" name="attach" class="form-control-file border" style=" height: 35px; padding-top: 0.8%;">
				</td>
			</tr>
			
			<tr style="margin-bottom: 5%; border: solid 0px red; height: 500px;">
				<td style="width: 10%; vertical-align: top;">
					<div style="font-size:20px;" class='fas'>내용</div>
				</td>
				<td>
					<textarea name="content" class="form-control-file border" style="width: 100%; height: 500px;" id="content"></textarea>
				</td>
			</tr>
			
			<tr id = "board_show" style=" border: solid 0px red; height: 80px;">
				<td style="width: 8%;">
					<span style="font-size:20px; padding-top: 18%; vertical-align: middle;" class='fas'>공개설정</span>
				</td>
				<td style="width: 100%; padding-left: 2%;">
			        <div style="position: relative;">
			            <div style="position: absolute; display: flex; width: 100%; vertical-align: bottom;">
			                <div style="display: flex; align-items: center; width: 25%; margin-left:5%; ">
			                    <input type="radio" class="form-check-input" name="board_show" value="1" />
			                    <span>공개</span>
			                </div>
			                <div style="display: flex; align-items: center; width: 50%;">
			                    <input type="radio" class="form-check-input" name="board_show" value="0"/>
			                    <span>비공개</span>
			                </div>
			            </div>
			        </div>
				</td>
			</tr>

		</table>
	</form>
</div>

<hr style="border-color: #e6ecff; margin: 1% 0 3% 0%;">
			   
<div style="text-align: center; margin-bottom: 3%;">
	<button style="margin-right: 4%; width: 8%;" type="button" class="btn btn-info" id="add">등록</button>
	<button style="margin-right: 4%; width: 10%;" type="button" class="btn btn-success" id="temporaryBoard">임시저장</button>
	<button style="width: 8%;" type="button" class="btn btn-secondary" onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_Board'">취소</button>
</div>			
			

<!-- ========================== 임시저장글 클릭시 모달창 만들기 시작 ==========================================-->
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
        
        
<c:if test="${not empty requestScope.tmapList}">
    <c:forEach var="tmapList" items="${requestScope.tmapList}" varStatus="status">
        <div class="modal-body">
            <span style="border: solid 1px gray;">제목</span>
            <span style="border: solid 1px red;">작성일자</span>
        </div>
    </c:forEach>
</c:if>
        
        <!-- Modal footer -->
        <div class="modal-footer">
          <button type="button" class="btn btn-success" data-dismiss="modal">확인</button>
        </div>
        
      </div>
    </div>
  </div>
</div>
<!-- ========================== 임시저장글 클릭시 모달창 만들기 끝 ==========================================-->

  </div>
</div>