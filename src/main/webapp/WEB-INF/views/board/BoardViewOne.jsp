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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/hr/hrIndex.css" />

<style>

.swal2-icon.my-custom-icon {
  top: 10px !important;
  left: 210px !important;
  font-size: 10pt;
}

.css-button-arrow--sand {
  min-width: 130px;
  height: 40px;
  color: #fff;
  padding: 5px 10px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  display: inline-block;
  outline: none;
  overflow: hidden;
  border-radius: 5px;
  border: none;
  background-color: #ced4da
}
.css-button-arrow--sand:hover {
  border-radius: 5px;
  padding-right: 24px;
  padding-left:8px;
}
.css-button-arrow--sand:hover:after {
  opacity: 1;
  right: 10px;
}
.css-button-arrow--sand:after {
  content: "\00BB";
  position: absolute;
  opacity: 0;
  font-size: 20px;
  line-height: 40px;
  top: -3px;
  right: -20px;
  transition: 0.4s;
}


   
span.move  {cursor: pointer; color: navy;}
.moveColor {color: #660029; font-weight: bold; background-color: #ffffe6;}

td.comment {text-align: center;}

a {text-decoration: none !important;}
 
 
 
 
 
 
 
table#cmt {
  width: 100%;
  border: 1px solid #ccc;
  border-collapse: collapse;
  margin: 10px 0;
}
td.cmt {
  border: 1px solid #ccc;
  border-right: none;
  padding: 8px;
  vertical-align: top;
}
.profile {
  width: 60px;
  text-align: center;
}
.profile img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
.comment-info {
  color: #999;
  font-size: 0.9em;
  margin-top: 5px;
}
.actions {
  width: 150px;
  text-align: center;
  padding-bottom : 1.3%;
}
.actions a {
  margin-left: 5px;
  text-decoration: none;
  color: #333;
} 
 

/* 하트버튼 만들기 */
.btn_like {width: 62px; height: 50px;background: url(https://umings.github.io/images/i_like_off.png) no-repeat center / 50px; cursor: pointer; border:0; font-size:0; display:block;}
.btn_like.on {background: url(https://umings.github.io/images/i_like_on.png) no-repeat center / 50px; animation: beating .5s 1 alternate;}
@keyframes beating {
    0% {transform: scale(1);}
    40% {transform: scale(1.25);}
    70% {transform: scale(0.9);}
    100% {transform: scale(1);}
}
    
</style>


<script>

	$(document).ready(function(){
		
		goCmtPaging(1); // 페이징처리한 댓글목록 불러오기
 		
		// 하트 버튼 toggle 설정
		$(".btn_like").click(function () { // 하트 버튼을 클릭했을 경우 tbl_like 테이블에 좋아요 수 1 증가시키기 
			
			const queryString = $("form[name='cmtFrm']").serialize();
			
			let $btn = $(this);
			let isLiked = $btn.hasClass("on"); // true면 이미 좋아요 상태
		
			if(!isLiked){ // 하트 버튼이 클릭되어있지 않을 경우
			
				 $.ajax({
						url:"<%= ctxPath%>/board/addLike",
						type:"POST",
						data:queryString,
						dataType:"json",
						success:function(json) {
						//	console.log(JSON.stringify(json));
							
							if(json.success){ // 제대로 insert 가 되어졌다면
								$("span#likeCnt").text(json.n);
							    $btn.addClass("on");
								// alert("좋아요 클릭!");
							}
						},
					    error: function(request, status, error){
			               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			            }
				});
			}
			else {
				$.ajax({
				      url:"<%= ctxPath%>/board/removeLike",
				      type: "POST",
				      data:queryString,
					  dataType:"json",
				      success: function(json) {
				        if(json.success) {
				          // 좋아요 delete 성공
				          $("span#likeCnt").text(json.n);
				          $btn.removeClass("on");
				          // alert("좋아요 취소!");
				        }
				      }
				 });
			 }
		});
		

//////////////////////////////////////////////////////////////////////////		
		// 로그인되어 있는 사용자가 해당 게시글에 좋아요를 누른 상태라면 좋아요 상태 유지하기 // 
		const likeExists = ${requestScope.like_paraMap.isLike};
		
		if(likeExists) {
			$(".btn_like").addClass("on");
		}
		else {
			$(".btn_like").removeClass("on");
		}
		// 로그인되어 있는 사용자가 해당 게시글에 좋아요를 누른 상태라면 좋아요 상태 유지하기 // 
/////////////////////////////////////////////////////////////////////////



		
///////////////////////////////////////////////////////////////////////////////////////////////////		 
	// 등록을 클릭하거나 댓글내용을 작성한 후 엔터를 친 경우라면
    $("input:text[name='cmt_content']").bind("keydown", function(e){
  	  if(e.keyCode === 13) { // 엔터
  		e.preventDefault(); // 엔터시 자동 폼 submit 방지
  		/* 앞으로 Enter 키를 눌렀을 때 form이 의도치 않게 제출되는 경우가 있다면 e.preventDefault();를 꼭 기억하세요!!  */
  		addCmt(); 	// 댓글쓰기
  	  }
  	  
    });
///////////////////////////////////////////////////////////////////////////////////////////////////		 

//////////////////////////
// 카테고리명 설정해주기 //

 const fk_bcate_no = ${requestScope.boardvo.fk_bcate_no}; 
  if (fk_bcate_no == "1") {
	 $("span.fk_bcate_no").text("신간도서");
  }
  else if (fk_bcate_no == "2") {
	 $("span.fk_bcate_no").text("오늘의 뉴스");
  }
  else if (fk_bcate_no == "3") {
	 $("span.fk_bcate_no").text("주간식단표");
  }
  else if (fk_bcate_no == "4") {
	 $("span.fk_bcate_no").text("무엇이든 물어보세요!");
  }




}); // $(document).ready(function(){})------------------------------------

	
	
///////////////////////////////////////////////////////////////////////////////////////////////////	

// 댓글 등록버튼을 누르면
function addCmt(){ 
	
	const cmt_content = $("input:text[name='cmt_content']").val().trim();
	   if(cmt_content == ""){
       	  alert("댓글 내용을 입력하세요!!");
          return; // 종료
	   }

	goAddCmt(); // 댓글 insert 해주는 메소드

}// end of function addCmt()-----------------



function goAddCmt(){
	
	<%--
       // 보내야할 데이터를 선정하는 또 다른 방법
       // jQuery에서 사용하는 것으로써,
       // form태그의 선택자.serialize(); 을 해주면 form 태그내의 모든 값들을 name값을 키값으로 만들어서 보내준다. 
       const queryString = $("form[name='cmtFrm']").serialize();
   --%>
   
   const queryString = $("form[name='cmtFrm']").serialize();
	//  alert(queryString);
   /*
   		cmt_content=qwer&fk_emp_id=9999&name=%EA%B4%80%EB%A6%AC%EC%9E%90&fk_board_no=125
   */
   
   $.ajax({
	   url:"<%= ctxPath%>/board/BoardComment",
	/*  
	   data:{"fk_emp_id":$("input:hidden[name='fk_emp_id']").val()
		   , "name":$("input:text[name='name']").val()
		   , "cmt_content":$("input:text[name='cmt_content']").val()
		   , "fk_board_no":$("input:hidden[name='fk_board_no']").val()},
	*/
	
	// 또는
	  data:queryString,
	  type:"post",
	  dataType:"json",
	  success:function(json){
		console.log(JSON.stringify(json));
		// {"name":"이연진", "n":1}
		// 또는 
		// {"name":"이연진", "n":0}
		
		if(json.n == 1){
			// 페이징 처리 한 댓글 읽어오기
			goCmtPaging(1); 
		}
		
		$("input:text[name='cmt_content']").val("");
	  },
	  error: function(request, status, error){
          alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
      }
   });
	
} // end of function goAddCmt()--------------


// Ajax로 불러온 댓글 내용들을 페이징 처리하기 
function goCmtPaging(currentShowPageNo) {  // $(document).ready(function(){}) 에서 currentShowPageNo 디폴트 1값을 줌 
	   
	   $.ajax({
			url:"<%= ctxPath%>/board/CommentList",
			data:{"fk_board_no":"${requestScope.boardvo.board_no}"
				 ,"currentShowPageNo":currentShowPageNo}, // 숫자가 아닐경우엔 꼭 "" 써줘야한다. 숫자에 "" 써도 호환되기 때문에 그냥 꼭 "" 쓰자!
			dataType:"json",
			success:function(json){
			//	console.log(JSON.stringify(json));
				/*
				[{"commentList":
				{"name":"관리자","fk_emp_id":"9999","cmt_content":"안녕하신가요?","fk_board_no":"125","comment_no":"90","cmt_regdate":"2025-03-06 17:20:21"},"totalCount":15,"sizePerPage":5}
				,{"commentList":
				{"name":"관리자","fk_emp_id":"9999","cmt_content":"qwer","fk_board_no":"125","comment_no":"89","cmt_regdate":"2025-03-06 17:07:11"},"totalCount":15,"sizePerPage":5}
				,{"commentList":
				{"name":"관리자","fk_emp_id":"9999","cmt_content":"부서 게시판 댓글 연습입니다2","fk_board_no":"125","comment_no":"88","cmt_regdate":"2025-03-06 16:02:14"},"totalCount":15,"sizePerPage":5}
				,{"commentList":
				{"name":"관리자","fk_emp_id":"9999","cmt_content":"하이","fk_board_no":"125","comment_no":"87","cmt_regdate":"2025-03-06 16:02:06"},"totalCount":15,"sizePerPage":5}
				,{"commentList":
				{"name":"관리자","fk_emp_id":"9999","cmt_content":"f","fk_board_no":"125","comment_no":"86","cmt_regdate":"2025-03-06 16:01:57"},"totalCount":15,"sizePerPage":5}]  
				
				// 또는
				
					[]
				*/
				
				let v_html = ``;
				
				const emp_id = ${sessionScope.loginuser.emp_id};
				
				if(json.length > 0) {
					$.each(json, function(index,item){
					
					const sunbun = item.totalCount - (currentShowPageNo -1) * item.sizePerPage - index;
	
	    				v_html += `<table id="cmt">
	    				    <tr style="height: 70px;">
	    				      <!-- 프로필 영역 -->
	    				     <td class="profile">
	    				       <img src='<%= ctxPath %>/resources/profile/\${item.commentList.profile_img}' style=' font-size:30px; margin-top: 27%; background-color: white;' />
	    				     </td>
	    				     
	    				     <!-- 댓글 내용 영역 -->
	    				     <td class="cmt">
	    				       <!-- 작성자명 + 날짜 -->
	    				       <div>
	    				         <strong>\${item.commentList.name}</strong>
	    				         <span class="comment-info">\${item.commentList.cmt_regdate}</span>
	    				       </div>
	    				       
	    				       <!-- 실제 댓글 내용 -->
	    				       <div style=" width:100%; display: flex; ">
	    				       <span id="cmtEdit\${item.commentList.comment_no}" style="padding-top: 1%;">
	    				       	\${item.commentList.cmt_content}
	    				       </span>
	    				       <!-- 수정 클릭시 기존 댓글 display:none;으로 설정 후 input태그 보이기 -->
	    				       <input name="cmtEdit\${item.commentList.comment_no}" style="margin-top: 10px; width:100%; margin-right:5%; display:none;" value="\${item.commentList.cmt_content}"/>`;
	    				       
	    				       
	    				     
	    				     <!-- 수정/삭제 버튼 영역 -->
	    				    
	    				     if(emp_id == item.commentList.fk_emp_id){
		    				        v_html += `<div id="cmtEdit\${item.commentList.comment_no}" style="margin-left:auto;">
			    				        		   <button type="button" class="btn btn-sm btn-secondary" style="font-size: 15px;" onclick="cmtEdit(\${item.commentList.comment_no})">수정</button>
						    				       <button type="button" id="cmtDel" class="btn btn-sm btn-danger" style="font-size: 15px;" onclick="cmtDel(\${item.commentList.comment_no})">삭제</button>
					    				       </div>
					    				       
		    				        		   <div id="cmtSave\${item.commentList.comment_no}" style="display:none; width:13%;  margin-left:auto;">
			    				        		   <button type="button" class="btn btn-sm btn-secondary" style="font-size: 15px; margin-right: 5%;" onclick="cmtSave(\${item.commentList.comment_no})">저장</button>
					    				       	   <button type="button" class="btn btn-sm btn-danger"  style="font-size: 15px;" onclick="cmtCancle(\${item.commentList.comment_no})">취소</button>
				    				       	   </div>`;
	    				     }
	    				     
		    	  v_html += `</div>
		    	  			</td>
	    				   </tr>
    				 </table>`;
						
		    	  // console.log(v_html);
    				 
					});
					
					
				}
				
				else {
					v_html = `<div style="text-align: center;">댓글이 없습니다.</div>`;
				}
				
				$("div#cmtList").html(v_html);
				
				// 페이지바 함수 호출
				const totalPage = Math.ceil(json[0].totalCount/json[0].sizePerPage);
				console.log("totalPage :",totalPage);
				// totalPage : 8
				
				makeCommentPageBar(currentShowPageNo, totalPage);
			},
			error: function(request, status, error){  
	              alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
			
		}); 
	   
}// end of function goCmtPaging(currentShowPageNo) {}------------------------------------


// 페이지바 함수 만들기
function makeCommentPageBar(currentShowPageNo, totalPage) {
	   
	   const blockSize = 5;
	   
	   let loop = 1;
    
	   let pageNo = Math.floor((currentShowPageNo - 1)/blockSize) * blockSize + 1;

	   let pageBar_HTML = "<ul style='list-style:none;'>";
    
	// === [맨처음][이전] 만들기 === //
	  pageBar_HTML += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='javascript:goCmtPaging(1)'><img src='${pageContext.request.contextPath}/images/icon/go_first.svg' /></a></li>";
   
   while( !(loop > blockSize || pageNo > totalPage)) {
      
      if(pageNo == currentShowPageNo) {
         pageBar_HTML += "<li style='display:inline-block; width:30px; font-size:15pt; border:solid 1px gray; color:red; padding:2px 4px;'><a>"+pageNo+"</a></li>";
      }
      else {
         pageBar_HTML += "<li style='display:inline-block; width:30px; font-size:14pt; padding-bottom:1%;'><a href='javascript:goCmtPaging("+pageNo+")'>"+pageNo+"</a></li>"; 
      }
      
      loop++;
      pageNo++;
   }// end of while------------------------

   pageBar_HTML += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='javascript:goCmtPaging("+totalPage+")'><img src='${pageContext.request.contextPath}/images/icon/go_last.svg' /></a></li>"; 
   
   pageBar_HTML += "</ul>";
	      
   $("div#pageBar").html(pageBar_HTML);
   
   
}// end of  function makeCommentPageBar(currentShowPageNo, totalPage)--------------------



// 수정하기
function cmtEdit(comment_no){
	// alert("수정 클릭!")
	$("input[name='cmtEdit"+comment_no+"']").show();
	$("span#cmtEdit"+ comment_no).hide();
	$("div#cmtEdit"+ comment_no).hide();
	$("div#cmtSave"+ comment_no).css("display","flex");
	
}

// 수정 취소하기
function cmtCancle(comment_no) {
	$("input[name='cmtEdit"+ comment_no+"']").hide();
	$("div#cmtEdit"+ comment_no).show();
	$("span#cmtEdit"+ comment_no).show();
	$("div#cmtSave"+ comment_no).css("display","none");
}

// 댓글 삭제하기
function cmtDel(comment_no){
	
	$.ajax({
		url:"<%= ctxPath%>/board/cmtDel",
		data:{"fk_emp_id":${sessionScope.loginuser.emp_id},
			  "comment_no":comment_no,
			  "board_no":${requestScope.boardvo.board_no}},
		dataType:"json",
		type:"post",
		success:function(json) {
		//	console.log(JSON.stringify(json));
			
			if(json.success){
				alert(json.message);
				// 단순 새로고침
		        location.reload(); 
			}
			else {
				alert(json.message);
				return false;
			}
		},
		error: function(request, status, error){
           alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }
	});
	
}// end of function cmtDel(comment_no)--------------------

// 수정한 댓글 저장하기
function cmtSave(comment_no) {
	const cmt_content = $("input[name='cmtEdit"+ comment_no+"']").val();
	
	$.ajax({
		url:"<%= ctxPath%>/board/cmtSave",
		data:{"fk_emp_id":${sessionScope.loginuser.emp_id},
			  "comment_no":comment_no,
			  "cmt_content":cmt_content,
			  "board_no": ${requestScope.boardvo.board_no}},
		dataType:"json",
		type:"post",
		success:function(json) {
		//	console.log(JSON.stringify(json));
		
			if(json.success){
				alert(json.message);
				
				// 수정후 페이지 이동
				window.location.href = "<%= ctxPath %>/board/BoardviewOne?board_no=" + "${requestScope.boardvo.board_no}";
				
			}
			else{
				alert(json.message);
				return false;
			}
			
		},
		error: function(request, status, error){
           alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }
		
	});
	
}
///////////////////////////////////////////////////////////////////////////////////////////////////		 



// 이전글, 다음글 보기 
function goView(board_no){

	const goBackURL = "${requestScope.goBackURL}";
	// alert(goBackURL);
	// alert(board_no);
	
	const frm = document.goViewFrm;
	frm.board_no.value = board_no;
	frm.goBackURL.value = goBackURL;
	
	/* searchType 과 searchWord 도 view단 페이지로 보내줘야 검색어가 있을 경우 이전글 다음글을 볼 때 해당 검색어가 있는 1개의 글을 알아올 수 있다. */
	
	if(${not empty requestScope.paraMap}) { // 검색조건이 있을 경우
		frm.searchType.value = "${requestScope.paraMap.searchType}";
		frm.searchWord.value = "${requestScope.paraMap.searchWord}";
	}
	
	frm.method = "post";
	
	<%-- === 이전글보기, 다음글보기를 클릭할때 글조회수 증가를 하기 위한 용도이다. === --%>
	frm.action = "<%= ctxPath%>/board/BoardViewList";  
	frm.submit();
}
	
// 부서 게시판의 게시글을 삭제하기	
function goDel() {
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
	      const frm = document.goViewFrm;
	      frm.method = "POST";
	      frm.action = "<%= ctxPath%>/board/GroupWare_BoardDel";
      	  frm.submit();
    }
  });
}// end of goDel()-----------------------	
	
	
</script>


<div class="common_wrapper" style="margin:0%;">
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
		<p class="bg-light text-dark" style="font-size: 20pt; font-weight: bold; padding: 1% 0 1% 4.5%;">부서게시판&nbsp;<i style='font-size:24px' class='fas'>&#xf105;</i>&nbsp;<span class="fk_bcate_no" style="font-size: 16pt;"></span></p>
	</div>	
	<!-- 페이지 공통 부분  -->

	<div id="container" style="border: solid 0px gray; width: 90%; margin: 0 20% 0 5%;">
		<table style="width: 100%;">
			<tr style="margin-bottom: 3%; height: 62px;">
				<th style="width: 7%;">
					<div style="font-size: 25px; font-weight: bold;">제목</div>
				</th>
				<td>
					<div style="display: flex; margin-top:2%; border: solid 0px red; width: 100%; height: 60px;">
						<span id="subject" style="border: solid 0px gray; width: 100%; padding-top: 0.5%; font-size: 25px;">${boardvo.subject}</span>
						<span style="font-size: 15px; font-weight: normal; width: 7%; padding-top: 1.7%;" id="viewCount">조회수&nbsp;&nbsp;<span style="font-weight: normal;">${boardvo.view_count}</span></span>
						<button id="like" style="font-size: 15px; border: none; width: 8%; background-color: transparent;">좋아요&nbsp;&nbsp;<span id="likeCnt" style="font-weight: normal;">${requestScope.n}</span></button>
						<span class="btn_like" style="font-size:40px; background-color: transparent; color:gray; border: solid 0px gray; padding: 0.5%; margin-left: 0.5%; cursor: pointer;"></span>
					</div>
				</td>
			</tr>
		
			<tr>
				<th style="width: 5%;">
				<div style="width: 60%;">
					<img src="<%= ctxPath%>/resources/profile/${boardvo.profile_img}" style='font-size:25px; width: 80%; border-radius: 50%;'/>
				</div>
				</th>
				<td style="vertical-align: middle;">
					<span style="margin-right: 0.5%;">${boardvo.name}</span>
					<span style="font-size: 10pt;">${boardvo.board_regdate}</span>
					<input type="hidden" name="fk_emp_id" value=""/>
				</td>
			</tr>
			
			<tr>
				<th></th>
				<td style="display: flex; width: 100%;">
					<div style="display: flex; width: 100%; margin-left: 75%;">
						<c:if test="${not empty sessionScope.loginuser && sessionScope.loginuser.emp_id == requestScope.boardvo.fk_emp_id}"><!-- 로그인 되어졌고 해당글의 작성자가 사용자와 같아야한다.  -->
							<button style="width: 45%; margin-right: 8%;" type="button" class="btn btn-light"  onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_BoardEdit/${requestScope.boardvo.board_no}'" style="margin: 0 3%;"><i style='font-size:24px' class='fas'>&#xf044;</i>수정</button>
							<button style="width: 45%;" type="button" class="btn btn-light"  onclick="goDel()"><i style='font-size:18px' class='far'>&#xf2ed;</i>삭제</button>
						</c:if>
					</div>
					<button style="width: 45%;" type="button" class="btn btn-light"  onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_Write'"><i style='font-size:18px' class='fas'>&#xf304;</i>새글쓰기</button>
				</td>
			</tr>
		</table>
	
	
		<div style="border: solid 0px red;">
			<div style="display:inline-block; width: 100%; margin: 2% 0 1% 0%;">
				<div style="height: 616px; width: 100%; border: solid 0px gray; word-break: break-all; overflow: auto;" class="content-div">${boardvo.content}</div>
				<c:if test="${not empty requestScope.boardvo.atboard_orgfilename}">
					<div style="font-size: 15px; margin-top: 1%;">
						<button id="downloadButton" type="button" onclick="javascript:location.href='<%= ctxPath%>/board/Bdownload?board_no=${requestScope.boardvo.board_no}'" class="css-button-arrow--sand" style="margin-right: 1%;">다운로드</button>
						<label style= "cursor: pointer;" for="downloadButton" onclick="javascript:location.href='<%= ctxPath%>/board/Bdownload?board_no=${requestScope.boardvo.board_no}'">${requestScope.boardvo.atboard_orgfilename}</label><!-- 컨트롤러에서는 로그인을 해야지만 download 경로로 들어갈 수 있도록 Aop 사용 -->
					</div>
				</c:if>
			</div>
		</div>

<c:if test="${empty requestScope.paraMap.redirect_like}">	
<form name ="cmtFrm">
	<div id="review" class="msg_wrap" style="width: 100%; border-radius: 4px; border: 1px solid #ddd; height: 82px; padding-top: 1%; padding-right: 1%; padding-left: 0.5%; margin-bottom: 2%;">
		<i style=' font-size:24px; margin-right: 1%; margin-left: 1%; background-color: white;' class='far'>&#xf2bd;</i>
		<span>
			<input type="text" name="cmt_content" style="width: 92%; height: 50px; border: 1px solid #ddd;" placeholder="댓글을 남겨보세요" />
			<input name="fk_emp_id" type="hidden" value="${sessionScope.loginuser.emp_id}"/>
			<input name="fk_board_no" type="hidden" value="${requestScope.boardvo.board_no}"/>
		</span>
		<button type="button" onclick="addCmt()" class="btn btn-secondary" style="font-size:12px; float:right; margin-top: 1%; ">등록</button>
	</div>
</form>
<div >
<div id="cmtList"></div>
<div id="pageBar" style="width: 100%; text-align: center; font-size: 50px; margin-bottom: 2%;"></div>
</div>
	
		<div id="container_view" style="width: 100%;">
			<div id="container_viewList" class="container" style="margin: 0%; width: 90%; height: 20px;">            
			  
<%-- ==== 이전글제목, 다음글제목 보기 시작 ==== --%>
		<c:if test="${not empty requestScope.boardvo}">	
			  <table class="table table-striped" style="width: 125%;">
			    <thead>
			      <tr>
			        <th></th>
			        <th style="font-weight: bold; text-align: center;">제목</th>
			      </tr>
			    </thead>
			    <tbody>
			      <tr>
			        <td style="font-weight: bold;">이전글</td>
			        <td style="text-align: center;"><span class="move" onclick="goView('${requestScope.boardvo.previousseq}')">${requestScope.boardvo.previoussubject}</span></td>
			      </tr>
			      <tr>
			        <td style="font-weight: bold;">>></td>
			        <td style="text-align: center;">${requestScope.boardvo.subject}</td>
			      </tr>
			      <tr>
			        <td style="font-weight: bold;">다음글</td>
			        <td style="text-align: center;"><span class="move" onclick="goView('${requestScope.boardvo.nextseq}')">${requestScope.boardvo.nextsubject}</span></td>
			      </tr>
			    </tbody>
			  </table>
		</c:if> 
<%-- ==== 이전글제목, 다음글제목 보기 끝 ==== --%>		
					
			<span>
				<button type="button" onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_Board'" class="btn btn-outline-secondary" style="margin-right: 2%;">전체목록조회</button>
				<button type="button" class="btn btn-outline-secondary" onclick="javascript:location.href='<%= ctxPath%>${requestScope.goBackURL}'">검색된목록조회</button>
			</span>
		 
	  
			</div>
		</div>
</c:if> 
	</div>
	</div>
</div>


<form name="goViewFrm">
	<input type="hidden"   name="board_no" value="${requestScope.boardvo.board_no}"/>
	<input type="hidden"   name="fk_emp_id" value="${sessionScope.loginuser.emp_id}"/>
	<input type="hidden" name="goBackURL"/>
	<input type="hidden" name="searchType"/>
	<input type="hidden" name="searchWord"/>
</form>