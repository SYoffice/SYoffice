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


<style>
.swal2-icon.my-custom-icon {
  top: 10px !important;
  left: 210px !important;
  font-size: 10pt;
}

/* 페이징 관련 스타일 */
.pagination {
    margin-bottom: 0;
    display: flex;
    justify-content: center;
}

.page-item {
    margin: 0 2px;
}

.page-link {
    padding: 0.5rem 0.75rem;
    color: #333;
    background-color: #fff;
    border: 1px solid #dee2e6;
}

.page-item.active .page-link {
    background-color: #007bff;
    border-color: #007bff;
    color: white;
}

/* 모달 내용 스타일링 */
#temporaryList {
    max-height: 400px;
}

.modal-body {
    padding: 1rem;
}
</style>

<script type="text/javascript">

	//전역변수
	var obj = [];

	$(document).ready(function(){ // 새로고침 또는 화면이 처음에 보여질때 설정해줄 것들은 여기 안에 적어주면 된다.

		
		$("select[name='fk_bcate_no']").hide(); // 처음에 카테고리 select 태그를 숨겨준다.
		
		/* 스마트 에디터를 사용할 경우 */
		<%-- === 스마트 에디터 구현 시작 === --%>
		// 전역변수
		// var obj = [];
       
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
       
	    // 글쓰기 게시판 위치 및 공개설정 설정하기 시작 //
	    if(${loginuser.fk_dept_id eq '2'}){ // 인사부라면
	    	
	    	 $("select[name='boardLocation']").on('change',function(e) {
	 	        const boardLocation = $("select[name='boardLocation']").val();
	 			const boardDept = $("option[value='boardDept']");
	 			const notice = $("option[value='notice']");
	 			
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
	    }
	    else { // 인사부가 아니라면 
	    	const boardLocation = $("select[name='boardLocation']").val();
	    	if(boardLocation == "boardDept"){
	    		$("select[name='fk_bcate_no']").show();
	    	}
	    }
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
	    
		
			
		    // === 글내용 유효성 검사(스마트 에디터를 사용할 경우) 시작 === //
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
	     	// === 글내용 유효성 검사(스마트 에디터를 사용할 경우) 끝 === //
	     	
	     	
	     	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////			
	const formData = new FormData(document.BoardFrm);
			
    if ($("input[name='temp_notice_no']").val()) { // 임시저장글을 불러왔을때 해당 글번호가 input에 temp_notice_no의 value 값이 들어와서 값이 생긴 경우라면
        // 기존 임시저장 글이 있다면 UPDATE 요청
        formData.append("notice_no", $("input[name='temp_notice_no']").val());
        // console.log("formData:", formData);
    
        $.ajax({
            url: "<%= ctxPath%>/board/updateNoticeTemporary",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                if (response.success) {
                    alert("게시글이 정상적으로 등록되었습니다.");
                    location.href = "<%= ctxPath%>/board/GroupWare_noticeBoard";
                } else {
                    alert("🚨 게시글 등록에 실패했습니다.");
                }
            },
            error: function(xhr, status, error) {
                alert("🚨 게시글 등록 중 오류가 발생했습니다.");
                // console.error(error);
            }
        });
    }
    else if ($("input[name='temp_board_no']").val()) {
        
    	// ✅ 기존 임시저장 글이 있다면 UPDATE 요청
        formData.append("board_no", $("input[name='temp_board_no']").val());
        
        $.ajax({
            url: "<%= ctxPath%>/board/updateBoardTemporary",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                if (response.success) {
                    alert("게시글이 정상적으로 등록되었습니다.");
                    location.href = "<%= ctxPath%>/board/GroupWare_Board";
                } else {
                    alert("🚨 게시글 등록에 실패했습니다.");
                }
            },
            error: function(xhr, status, error) {
                alert("🚨 게시글 등록 중 오류가 발생했습니다.");
                // console.error(error);
            }
        });
    }			
    else {
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
    }
			
	});// end of $("button#add").click(function(){})-----------------
		
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		
		$("button#temporaryBoard").click(function() { // 임시저장 버튼을 클릭할 경우
// === 유효성 검사 시작 === //
			/* 스마트 에디터를 사용할 경우 */
		    <%-- === 스마트 에디터 구현 시작 === --%>
		    obj.getById["content"].exec("UPDATE_CONTENTS_FIELD", []);
		    <%-- === 스마트 에디터 구현 끝 === --%>
		
		    // 게시글 위치 유효성 검사
		    const boardLocation = $("select[name='boardLocation']").val();
		    const boardDept = $("option[value='boardDept']");
		
		    if(boardLocation == "boardDept") {
		        if($("select[name='fk_bcate_no']").val() == null) {
		            alert("작성하실 글의 카테고리를 선택하세요!");
		            return;
		        }
		    }
		
		    // 제목 유효성 검사
		    const subject = $("input[name='subject']").val().trim();
		    if(subject == "") {
		        alert("제목을 입력하세요!");
		        $("input[name='subject']").val("");
		        $("input[name='subject']").focus();
		        return;
		    }
		
		    // 글내용 유효성 검사
		    let content_val = $("textarea[name='content']").val().trim();
		    content_val = content_val.replace(/&nbsp;/gi, "");
		    content_val = content_val.substring(content_val.indexOf("<p>")+3);
		    content_val = content_val.substring(0, content_val.indexOf("</p>"));
		
		    if(content_val.trim().length == 0) {
		        alert("글내용을 입력하세요!!");
		        return;
		    }
		    
		    
			// 임시저장시 파일첨부 했을 경우 경고알림 띄우기		    
		    const file = $("input[type='file']")[0];
		    if(file.files.length > 0){
		    	alert("⚠ 파일이 첨부된 상태에서는 임시저장이 불가능합니다. 첨부된 파일을 삭제하고 다시 시도해주세요.");
		    	// console.log("조건충족!");
		    	return;
		    }
		    
// === 유효성 검사 끝 === //		

		const formData = new FormData(document.BoardFrm);

	    // AJAX로 전송
	    $.ajax({
	        url: "<%= ctxPath%>/board/" + (boardLocation == "notice" ? "noticeTemporaryBoard" : "TemporaryBoard"),
	        type: "POST",
	        data: formData,
	        processData: false,
	        contentType: false,
	        success: function(response) {
	            if(response.success) {
	                alert("임시저장 되었습니다.");
	                location.href="<%= ctxPath%>/board/GroupWare_Write";
	                // 임시저장 목록 업데이트
	                
		            if(boardLocation == 'notice'){
		            	goNoticeTempList();
		            }
		             else {
		            	goBoardTempList(); 
		            } 
	            
	            } else {
	                alert("임시저장 실패: " + (response.message || "알 수 없는 오류가 발생했습니다."));
	            }
	        },
	        error: function(xhr, status, error) {
	            alert("임시저장 중 오류가 발생했습니다.");
	            // console.log("Error:", error);
	        }
	    });// end of ajax()-------------------------------------
    
	});// end of $("button#temporaryBoard").click(function() {----------------------


		
});	// end of $(document).ready(function()----------------------------------------------------------------------------	
		
		
		

	  // [1] 임시저장 글 목록을 가져와 화면에 뿌려주는 함수
	  //     page 파라미터를 받아서, 해당 page에 맞는 목록을 가져오도록 함
	  function goNoticeTempList(page) {
	      if(!page) {
	          page = 1;  // page 파라미터가 없으면 기본 1페이지
	      }

	      $.ajax({
	          url: "<%= ctxPath %>/board/noticeTemporaryBoardList",
	          type: "GET",
	          dataType: "json",
	          data: {
	              "fk_emp_id": ${loginuser.emp_id},
	              "page": page,
	              "itemsPerPage": 5
	          },
	          success: function(json) {
	          //  console.log(JSON.stringify(json));
	              
	              // 1) 목록 영역 먼저 비우기
	              $("div#temporaryList").empty();

	              // 2) 데이터가 있는 경우
	              if(json.data && json.data.length > 0) {
	                  $.each(json.data, function(index, item){
	                      let rowHtml = `
	                          <tr style="width: 100%; height: 48px;">
	                              <td style="width: 37%; font-size: 13pt;">\${item.notice_subject}</td>
	                              <td style="width: 22%;">\${item.notice_regdate}</td>
	                              <td style="width: 17%;">
	                                  <button id="loadNotice" style="background-color: #80aaff; color:white;" class="btn" onclick="loadTemporaryContent('\${item.notice_no}')"  data-dismiss="modal">불러오기</button>
	                                  <button style="background-color: #ff3333; color:white;" class="btn" onclick="deleteNoticeTemporary('\${item.notice_no}')">삭제</button>
	                              </td>
	                          </tr>
	                      `;
	                      $("div#temporaryList").append(rowHtml);
	                  });

	                  // 3) 페이지바 생성
	                  let paginationHtml = createNoticePagination(json.totalCount, json.itemsPerPage, json.currentPage);
	                  $("div#temporaryList_paging").html(paginationHtml);
	              }
	              else {
	                  // 데이터가 없는 경우
	                  $("div#temporaryList").html("<p class='text-center mt-3'>임시저장된 글이 없습니다.</p>");
	                  // $("div#temporaryList_paging").empty(); 
	              }
	          },
	          error: function(xhr, status, error) {
	              alert("임시저장 목록을 가져오는 중 오류가 발생했습니다.");
	              // console.error(error);
	          }
	      });
	  }


	  // [2] 페이지 이동 함수
	  //     pagination에서 [1][2][3]... 버튼 클릭 시 호출
	  function goNoticeToPage(page) {
	      goNoticeTempList(page);
	  }


	  // [3] 페이징바(블록 단위) 생성 함수
	  //     blockSize = 5 이면 페이지번호를 최대 5개씩 보여주고 이전/다음 블록 이동 버튼을 만듦
	  function createNoticePagination(totalCount, itemsPerPage, currentPage) {
	      
	      // 파라미터 유효성 체크
	      if(!totalCount || !itemsPerPage || !currentPage) {
	     //   console.error('createNoticePagination()에 필요한 값이 부족합니다.');
	          return '';
	      }

	      // 숫자로 변환
	      totalCount    = parseInt(totalCount, 10);
	      itemsPerPage  = parseInt(itemsPerPage, 10);
	      currentPage   = parseInt(currentPage, 10);

	      // 전체 페이지 수
	      const totalPages = Math.ceil(totalCount / itemsPerPage);

	      // 페이지 범위 보정
	      if(currentPage < 1) currentPage = 1;
	      if(currentPage > totalPages) currentPage = totalPages;

	      // 총 페이지가 1페이지 이하라면 굳이 페이지바 필요 없음
	      if(totalPages <= 1) {
	          return '';
	      }

	      // 블록 크기 지정 (한번에 보여줄 페이지번호 개수)
	      const blockSize  = 5;
	      const blockIndex = Math.floor((currentPage - 1) / blockSize);  // 0부터 시작
	      const blockStart = blockIndex * blockSize + 1;                 // 블록 첫 페이지
	      let   blockEnd   = blockStart + blockSize - 1;                 // 블록 마지막 페이지

	      if(blockEnd > totalPages) {
	          blockEnd = totalPages;
	      }

	      // 이전/다음 블록 이동용 페이지
	      const prevBlockPage = blockStart - 1;
	      const nextBlockPage = blockEnd + 1;

	      let v_html = '<nav><ul class="pagination justify-content-center mt-3">';

	      // [처음] 버튼
	      if(currentPage > 1) {
	          v_html += `
	              <li class="page-item">
	                  <a class="page-link" href="javascript:void(0);" onclick="goNoticeToPage(1)">처음</a>
	              </li>
	          `;
	      }

	      // [이전 블록] 버튼
	      if(prevBlockPage > 0) {
	          v_html += `
	              <li class="page-item">
	                  <a class="page-link" href="javascript:void(0);" onclick="goNoticeToPage(\${prevBlockPage})">이전</a>
	              </li>
	          `;
	      }

	      // 페이지 번호들
	      for(let i = blockStart; i <= blockEnd; i++) {
	          v_html += `
	              <li class="page-item ${ (currentPage == i) ? 'active' : '' }">
	                  <a class="page-link" href="javascript:void(0);" onclick="goNoticeToPage(\${i})">\${i}</a>
	              </li>
	          `;
	      }

	      // [다음 블록] 버튼
	      if(nextBlockPage <= totalPages) {
	          v_html += `
	              <li class="page-item">
	                  <a class="page-link" href="javascript:void(0);" onclick="goNoticeToPage(\${nextBlockPage})">다음</a>
	              </li>
	          `;
	      }

	      // [마지막] 버튼
	      if(currentPage < totalPages) {
	          v_html += `
	              <li class="page-item">
	                  <a class="page-link" href="javascript:void(0);" onclick="goNoticeToPage(\${totalPages})">마지막</a>
	              </li>
	          `;
	      }

	      v_html += '</ul></nav>';
	      return v_html;
	  }

	  // [4] 모달창 열 때(또는 '임시저장글보기' 버튼 클릭 시) 1페이지 목록 불러오기
	  //     기존 코드: button 클릭 이벤트에서 goNoticeTempList()를 호출
	  //     아래와 같이 page=1 을 명시해주면 됨.
	  function openTemporaryModal() {
		  
		// 부서게시판 임시저장글 불러오기를 클릭한 후 임시저장글보기를 재클릭했을 때 알림띄우기
	   	if($("input[name='temp_board_no']").val()){
	   			console.log($("input[name='temp_board_no']").val());
	       		alert("해당 게시글 작성하기를 끝내고 싶으시다면 취소를 눌러주세요.");
	       		return false;
	   	}	
	 	// 공지사항 임시저장글 불러오기를 클릭한 후 임시저장글보기를 재클릭했을 때 알림띄우기
	   	else if($("input[name='temp_notice_no']").val()){
	   			console.log($("#temp_notice_no").val());
	   			alert("해당 게시글 작성하기를 끝내고 싶으시다면 취소를 눌러주세요.");
	       		return false;
	   	}
	      goNoticeTempList(1);
	      goBoardTempList(1);
	      $("#myModal").modal("show");
	  }

	  
	  function deleteNoticeTemporary(notice_no){
		  
		  // console.log("확인용 notice_no" , notice_no);
		  
		  Swal.fire({
			    title: "정말 삭제하시겠습니까?",
			    icon: "warning",
			    showCancelButton: true,
			    confirmButtonColor: "#3085d6",
			    cancelButtonColor: "#d33",
			    confirmButtonText: "네, 삭제합니다",
			    cancelButtonText: "취소",
			    customClass: {
			        icon: 'my-custom-icon'
			    }
			  }).then((result) => {
			    if (result.isConfirmed) {
			      // 사용자가 확인을 눌렀을 때만 실행
				  location.href = `<%= ctxPath%>/board/deleteNoticeTemporary?notice_no=\${notice_no}`;
		    }
		  });
	 }// end of function deleteNoticeTemporary(notice_no)-------------------------------
	  
	 
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////





// [1] 임시저장 글 목록을 가져와 화면에 뿌려주는 함수
	  //     page 파라미터를 받아서, 해당 page에 맞는 목록을 가져오도록 함
	  function goBoardTempList(page) {
	      if(!page) {
	          page = 1;  // page 파라미터가 없으면 기본 1페이지
	      }

	      $.ajax({
	          url: "<%= ctxPath %>/board/TemporaryBoardList",
	          type: "GET",
	          dataType: "json",
	          data: {
	              "fk_emp_id": ${loginuser.emp_id},
	              "page": page,
	              "itemsPerPage": 5
	          },
	          success: function(json) {
	          //    console.log(JSON.stringify(json));
	              
	              // 1) 목록 영역 먼저 비우기
	              $("div#BoardtemporaryList").empty();

	              // 2) 데이터가 있는 경우
	              if(json.data && json.data.length > 0) {
	                  $.each(json.data, function(index, item){
	                      let rowHtml = `
	                          <tr style="width: 100%; height: 48px;">
	                              <td style="width: 37%; font-size: 14pt; font-weight: bold;">\${item.subject}</td>
	                              <td style="width: 22%;">\${item.board_regdate}</td>
	                              <td style="width: 17%;">
	                                  <button id="loadBoard" style="background-color: #80aaff; color:white;" class="btn" onclick="loadBoardTemporaryContent('\${item.board_no}')"  data-dismiss="modal">불러오기</button>
	                                  <button style="background-color: #ff3333; color:white;" class="btn" onclick="deleteBoardTemporary('\${item.board_no}')">삭제</button>
	                              </td>
	                          </tr>
	                      `;
	                      $("div#BoardtemporaryList").append(rowHtml);
	                  });

	                  // 3) 페이지바 생성
	                  let paginationHtml = createBoardPagination(json.totalCount, json.itemsPerPage, json.currentPage);
	                  $("div#BoardtemporaryList_paging").html(paginationHtml);
	              }
	              else {
	                  // 데이터가 없는 경우
	                  $("div#BoardtemporaryList").html("<p class='text-center mt-3'>임시저장된 글이 없습니다.</p>");
	                  // $("div#BoardtemporaryList_paging").empty(); 
	              }
	          },
	          error: function(xhr, status, error) {
	              alert("임시저장 목록을 가져오는 중 오류가 발생했습니다.");
	              // console.error(error);
	          }
	      });
	  }


	  // [2] 페이지 이동 함수
	  //     pagination에서 [1][2][3]... 버튼 클릭 시 호출
	  function goBoardToPage(page) {
	      goBoardTempList(page);
	  }


	  // [3] 페이징바(블록 단위) 생성 함수
	  //     blockSize = 5 이면 페이지번호를 최대 5개씩 보여주고 이전/다음 블록 이동 버튼을 만듦
	  function createBoardPagination(totalCount, itemsPerPage, currentPage) {
	      
	      // 파라미터 유효성 체크
	      if(!totalCount || !itemsPerPage || !currentPage) {
	      //  console.error('createBoardPagination()에 필요한 값이 부족합니다.');
	          return '';
	      }

	      // 숫자로 변환
	      totalCount    = parseInt(totalCount, 10);
	      itemsPerPage  = parseInt(itemsPerPage, 10);
	      currentPage   = parseInt(currentPage, 10);

	      // 전체 페이지 수
	      const totalPages = Math.ceil(totalCount / itemsPerPage);

	      // 페이지 범위 보정
	      if(currentPage < 1) currentPage = 1;
	      if(currentPage > totalPages) currentPage = totalPages;

	      // 총 페이지가 1페이지 이하라면 굳이 페이지바 필요 없음
	      if(totalPages <= 1) {
	          return '';
	      }

	      // 블록 크기 지정 (한번에 보여줄 페이지번호 개수)
	      const blockSize  = 5;
	      const blockIndex = Math.floor((currentPage - 1) / blockSize);  // 0부터 시작
	      const blockStart = blockIndex * blockSize + 1;                 // 블록 첫 페이지
	      let   blockEnd   = blockStart + blockSize - 1;                 // 블록 마지막 페이지

	      if(blockEnd > totalPages) {
	          blockEnd = totalPages;
	      }

	      // 이전/다음 블록 이동용 페이지
	      const prevBlockPage = blockStart - 1;
	      const nextBlockPage = blockEnd + 1;

	      let v_html = '<nav><ul class="pagination justify-content-center mt-3">';

	      // [처음] 버튼
	      if(currentPage > 1) {
	          v_html += `
	              <li class="page-item">
	                  <a class="page-link" href="javascript:void(0);" onclick="goBoardToPage(1)">처음</a>
	              </li>
	          `;
	      }

	      // [이전 블록] 버튼
	      if(prevBlockPage > 0) {
	          v_html += `
	              <li class="page-item">
	                  <a class="page-link" href="javascript:void(0);" onclick="goBoardToPage(\${prevBlockPage})">이전</a>
	              </li>
	          `;
	      }

	      // 페이지 번호들
	      for(let i = blockStart; i <= blockEnd; i++) {
	          v_html += `
	              <li class="page-item ${ (currentPage == i) ? 'active' : '' }">
	                  <a class="page-link" href="javascript:void(0);" onclick="goBoardToPage(\${i})">\${i}</a>
	              </li>
	          `;
	      }

	      // [다음 블록] 버튼
	      if(nextBlockPage <= totalPages) {
	          v_html += `
	              <li class="page-item">
	                  <a class="page-link" href="javascript:void(0);" onclick="goBoardToPage(\${nextBlockPage})">다음</a>
	              </li>
	          `;
	      }

	      // [마지막] 버튼
	      if(currentPage < totalPages) {
	          v_html += `
	              <li class="page-item">
	                  <a class="page-link" href="javascript:void(0);" onclick="goBoardToPage(\${totalPages})">마지막</a>
	              </li>
	          `;
	      }

	      v_html += '</ul></nav>';
	      return v_html;
	  }

	 
	  function deleteBoardTemporary(board_no){
	//	  console.log("확인용 board_no" , board_no);
		  Swal.fire({
			    title: "정말 삭제하시겠습니까?",
			    icon: "warning",
			    showCancelButton: true,
			    confirmButtonColor: "#3085d6",
			    cancelButtonColor: "#d33",
			    confirmButtonText: "네, 삭제합니다",
			    cancelButtonText: "취소",
			    customClass: {
			        icon: 'my-custom-icon'
			    }
			  }).then((result) => {
			    if (result.isConfirmed) {
			      // 사용자가 확인을 눌렀을 때만 실행
				  location.href = `<%= ctxPath%>/board/deleteBoardTemporary?board_no=\${board_no}`;
		    }
		  });
	 }// end of function deleteNoticeTemporary(notice_no)-------------------------------	 
	 
	 
	 // 공지사항 임시저장글 불러오기
	 function loadTemporaryContent(notice_no){
			 
	    // 기존 스마트에디터 삭제 후 다시 생성
	    removeSmartEditor();
			 
		//	  console.log("확인용 notice_no" , notice_no);
		//	  console.log("확인용 fk_emp_id", ${sessionScope.loginuser.emp_id});
		  
		// alert("불러오기 클릭!");
       
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
		 
		 
		 $.ajax({
	        url: "<%= ctxPath %>/board/getNoticeTemporary",
	        type: "GET",
	        data: {"notice_no": notice_no,
	        	   "fk_emp_id":${sessionScope.loginuser.emp_id}}, // 선택한 임시저장 글의 ID 전달
	        dataType:"json",
	        success: function(response) {
	            if (response.success) {
	            //  console.log("✅ 불러온 데이터:", response);
	                
	                // 제목 입력
	                $("input[name='subject']").val(response.data.notice_subject);
	                $("textarea[name='content']").val(response.data.notice_content);
	                
	             	// 기존 임시저장 글의 notice_no 저장
	             	// 기존의 임시저장글의 notice_no의 번호가 있다면 등록 버튼을 눌렀을 경우에 insert가 아닌 notice_status=2의 상태를 1로 update 해주려는 용도
	                $("#temp_notice_no").val(response.data.notice_no);
	                
	                // 공지사항 게시판에서 불러온 글이라면 boardLocation을 notice로 고정시켜주기
            		$("select[name='boardLocation'] option[value='boardDept']").remove();
	                $("select[name='fk_bcate_no']").hide();
	             	
	             	// 다시 임시저장할 수 없도로 방지
	                $("button#temporaryBoard").hide(); 
	                
	                // 불러오기 이후에 취소 버튼을 누르면 글쓰기 페이지에 머물게 하기
	                $("button#cancle").click(function(){
	                	window.location.reload(); // 단순 새로고침
	                });
	                
	                
	            } else {
		        	alert("🚨 글을 불러오는 데 실패했습니다.");
		        }
	        },
	        error: function(xhr, status, error) {
	            alert("🚨 글을 불러오는 중 오류가 발생했습니다.");
	            console.error(error);
	        }
	    });
		 
		 
	 }// end of function loadTemporaryContent(notice_no)------------------------------------
	 
	 
	 // 부서 게시판 임시저장글 불러오기
	 function loadBoardTemporaryContent(board_no){
		 
	    // 기존 스마트에디터 삭제 후 다시 생성
	    removeSmartEditor();
			 
	 	// console.log("확인용 board_no" , board_no);
		// alert("불러오기 클릭!");
	       
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
			 
			 $.ajax({
		        url: "<%= ctxPath %>/board/getDeptBoardTemporary",
		        type: "GET",
		        data: {"board_no": board_no,
		        	   "fk_emp_id":${sessionScope.loginuser.emp_id}}, // 선택한 임시저장 글의 ID 전달
		        dataType:"json",
		        success: function(response) {
		            if (response.success) {
		            //  console.log("✅ 불러온 데이터:", response);
		                
		                // 제목 입력
		                $("input[name='subject']").val(response.data.subject);
		                $("textarea[name='content']").val(response.data.content);
		                
		             	// 기존 임시저장글의 board_no 저장
		             	// 기존의 임시저장글의 board_no의 번호가 있다면 등록 버튼을 눌렀을 경우에 insert가 아닌 board_status=2의 상태를 1로 update 해주려는 용도
		                $("#temp_board_no").val(response.data.board_no);
		                
		                $("button#temporaryBoard").hide(); // 다시 임시저장할 수 없도로 방지

		                // 부서 게시판에서 불러온 글이라면 boardLocation을 boardDept로 고정시켜주기
                		$("select[name='boardLocation'] option[value='notice']").remove();
                		$("select[name='fk_bcate_no']").show();
		                $("select[name='notice']").hide();
		                
		                // 불러오기 이후에 취소 버튼을 누르면 글쓰기 페이지에 머물게 하기
		                $("button#cancle").click(function(){
		                	window.location.reload(); // 단순 새로고침
		                });
		                
		                
		                
		            } else {
			        	alert("🚨 글을 불러오는 데 실패했습니다.");
			        }
		        },
		        error: function(xhr, status, error) {
		            alert("🚨 글을 불러오는 중 오류가 발생했습니다.");
		            console.error(error);
		        }
		    });
		 
		 
	 }// end of function loadBoardTemporaryContent(board_no)---------------------------------

	 
	 // 기존 스마트에디터 삭제 함수
	 function removeSmartEditor() {
	     if (obj.length > 0) {
	    	 obj[0].exec("FOCUS");   // 에디터에 포커스를 줌
	    	 obj[0].exec("RESET_EDITOR"); // 에디터 내용 초기화
	    	 // 스마트에디터를 감싸고 있는 부모 요소를 찾아 삭제
	         $("#content").parent().find("iframe").remove();
	         obj = [];  // 객체 배열 초기화
	     }
	 }	 
	 
	 
</script>

	


<div class="common_wrapper" style="margin-top: 0;">
    <div class="side_menu_wrapper">
        <div class="side_menu_inner_wrapper">
      	<button type="button"  id="write" onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_Write'">글쓰기</button>
           <ul class="side_menu_list">
                <li style="font-weight: bold;"><a href="<%= ctxPath%>/board/GroupWare_noticeBoard">전체 게시판</a></li>
                <li style="margin-left: 10%; font-size: 11pt;"><a href="<%= ctxPath%>/board/GroupWare_noticeBoard">공지사항</a></li>
                <li style="font-weight: bold;"><a href="<%= ctxPath%>/board/GroupWare_Board">부서 게시판[${sessionScope.loginuser.dept_name}]</a></li>
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
				<!-- === 부서 번호가 2인 인사부만 전체 게시판에 글을 작성 할 수 있도록 한다. ===  -->   
				   <c:if test="${loginuser.fk_dept_id eq '2'}">  
				       <option value="notice">전체 게시판</option>
				   </c:if> 
				<!-- === 부서 번호가 2인 인사부만 전체 게시판에 글을 작성 할 수 있도록 한다. ===  -->    
				     
					       <option value="boardDept">부서 게시판[${sessionScope.loginuser.dept_name}]</option>
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
				   
			<!-- === 부서 번호가 2인 인사부만 전체 게시판에 글을 작성할 수 있도록 한다. 시작 ===  -->   	   
			   <c:if test="${loginuser.fk_dept_id eq '2'}"> 
				   <span class="dropdown">
				   <select class="form-select" name="notice">
				       <option value="notice">공지사항</option>
				   </select>
				   </span>
			   </c:if>
			<!-- === 부서 번호가 2인 인사부만 전체 게시판에 글을 작성할 수 있도록 한다. 끝 ===  -->      
			
				   <button type="button" class="btn btn-outline-secondary" style="float: right; width: 20%;" onclick="openTemporaryModal()">임시저장글보기</button>				  
        		   <input type="hidden" name="fk_emp_id" value="${loginuser.emp_id}"/>
				   <input type="hidden" name="fk_dept_id" value="${loginuser.fk_dept_id}"/>
				   <!-- 공지사항 게시판에 임시저장글의 번호를 빈값으로 저장해둔다. -->
				   <input type="text" id="temp_notice_no" name="temp_notice_no" value="" style="display: none;">
				   <!-- 부서 게시판에 임시저장글의 번호를 빈값으로 저장해둔다. -->
				   <input type="text" id="temp_board_no" name="temp_board_no" value="" style="display: none;">
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
		</table>
	</form>
</div>

<hr style="border-color: #e6ecff; margin: 1% 0 3% 0%;">
			   
<div style="text-align: center; margin-bottom: 3%;">
	<button style="margin-right: 4%; width: 8%; background-color: #809fff; color: white; font-weight: bold;" type="button" class="btn" id="add">등록</button>
	<button style="margin-right: 4%; width: 10%; background-color: #b3d7ff; color: white; font-weight: bold;" type="button" class="btn" id="temporaryBoard">임시저장</button>
	<button style="width: 8%; background-color: #b3b3b3; color: white; font-weight: bold;" type="button" class="btn" id="cancle" onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_noticeBoard'">취소</button>
</div>			
			

<!-- ========================== 임시저장글 클릭시 모달창 만들기 시작 ==========================================-->
<!-- 모달창 -->
<div class="modal fade" id="myModal">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
    
      <!-- Modal Header -->
      <div class="modal-header">
        <div class="modal-title" style="font-size: 24px;">임시저장글 보기</div>
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>
      
      <!-- Modal Body -->
<!-- 공지사항 게시판 임시저장글 목록 -->      
<c:if test="${loginuser.fk_dept_id eq '2'}"><!-- 공지사항 게시판 임시저장글은 부서가 2인 인사부만 보여질 수 있게 설정한다. -->   
      <div style="text-align: center; font-size: 20px; font-weight: bold; height: 47px; padding-top: 1.5%; background-color: #e6f2ff;">
          공지사항 게시판 임시저장글
      </div>
      <div class="modal-body">
          <div id="temporaryList"></div>
          <div id="temporaryList_paging" class="mt-3"></div>
      </div>
</c:if> 

<!-- 부서 게시판 임시저장글 목록 --> 
	  <div style="text-align: center; font-size: 20px; font-weight: bold; height: 47px; padding-top: 1.5%; background-color: #e6f2ff">
          부서 게시판 임시저장글
      </div>
      <div class="modal-body">
          <div id="BoardtemporaryList"></div>
          <div id="BoardtemporaryList_paging" class="mt-3"></div>
      </div>


      <!-- Modal Footer -->
      <div class="modal-footer">
        <button type="button" style="background-color: #6b96c7; color: white;" class="btn" data-dismiss="modal">확인</button>
      </div>

    </div>
  </div>
</div>
<!-- ========================== 임시저장글 클릭시 모달창 만들기 끝 ==========================================-->

  </div>
</div>