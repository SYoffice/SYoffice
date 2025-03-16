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
  top: 0;
  right: -20px;
  transition: 0.4s;
}


   
span.move  {cursor: pointer; color: navy;}
.moveColor {color: #660029; font-weight: bold; background-color: #ffffe6;}

td.comment {text-align: center;}

a {text-decoration: none !important;}
 
    
</style>


<script>

// === 이전글, 다음글 보기 === //
function goView(notice_no){

	const goBackURL = "${requestScope.goBackURL}";
	console.log(goBackURL);
	
	const frm = document.goViewFrm;
	frm.notice_no.value = notice_no;
	frm.goBackURL.value = goBackURL;
	
	/* searchType 과 searchWord 도 view단 페이지로 보내줘야 검색어가 있을 경우 이전글 다음글을 볼 때 해당 검색어가 있는 1개의 글을 알아올 수 있다. */
	
	if(${not empty requestScope.paraMap}) { // 검색조건이 있을 경우
		frm.searchType.value = "${requestScope.paraMap.searchType}";
		frm.searchWord.value = "${requestScope.paraMap.searchWord}";
	}
	
	frm.method = "post";
	
	<%-- === 이전글보기, 다음글보기를 클릭할때 글조회수 증가를 하기 위한 용도이다. === --%>
	frm.action = "<%= ctxPath%>/board/noticeViewList";  
	frm.submit();
}
	
	
function goDel() {
	  Swal.fire({
	    title: "정말 삭제하시겠습니까?",
	    icon: "warning",
	    showCancelButton: true,
	    confirmButtonColor: "#80aaff",
	    cancelButtonColor: "#ff471a",
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
	      frm.action = "<%= ctxPath%>/board/GroupWare_Del";
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
                <li style="margin-left: 10%; font-size: 11pt;"><span>신간도서</span></li>
                <li style="margin-left: 10%; font-size: 11pt;"><span>오늘의 뉴스</span></li>
                <li style="margin-left: 10%; font-size: 11pt;"><span>주간식단표</span></li>
                <li style="margin-left: 10%; font-size: 11pt;"><span>무엇이든 물어보세요!</span></li>
            </ul>
        </div>
    </div>
        
<div class="contents_wrapper" style="margin-top: 3%;">
	<!-- 페이지 공통 부분  -->
	<div style="width: 100%;">
		<div style="border: solid 0px gray; display: flex; width: 100%;">
			<p class="bg-light text-dark" style="font-size: 20pt; font-weight: bold; padding: 1% 0 1% 4.5%; width: 30%;">전체게시판&nbsp;<i style='font-size:24px' class='fas'>&#xf105;</i>&nbsp;<span style="font-size: 15pt;">공지사항</span></p>
		</div>	
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
						<span id="subject" style="border: solid 0px gray; width: 100%; padding-top: 0.5%; font-size: 25px; ">${noticeboardvo.notice_subject}</span>
					</div>
				</td>
			</tr>
		
			<tr>
				<th style="width: 5%;">
					<div style="width: 60%;">
						<img src="<%= ctxPath%>/resources/profile/${noticeboardvo.profile_img}" style='font-size:25px; width: 80%; border-radius: 50%;'/>
					</div>
				</th>
				<td style="vertical-align: middle;  display: flex; width: 100%;">
					<span style="font-size: 18px; font-weight: bold; width: 8%;">${noticeboardvo.name}</span>
					<span style="font-size: 15px; width: 17%; padding-top: 0.3%;">${noticeboardvo.notice_regdate}</span>
					<span style="font-size: 15px; font-weight: normal; width: 10%; padding-top: 1.8%; float: right; margin-left: 77%;" id="viewCount">조회수&nbsp;&nbsp;<span style="font-weight: normal; ">${noticeboardvo.notice_viewcount}</span></span>
					<input type="hidden" name="fk_emp_id" value=""/>
				</td>
			</tr>
			
			<tr>
				<th></th>
				<td style="display: flex; width: 100%; margin-top: 1%;">
					<div style="display: flex; width: 100%; margin-left: 75%;">
						<c:if test="${not empty sessionScope.loginuser && sessionScope.loginuser.emp_id == requestScope.noticeboardvo.fk_emp_id}"><!-- 로그인 되어졌고 해당글의 작성자가 사용자와 같아야한다.  -->
							<button style="width: 45%; margin-right: 8%;" type="button" class="btn btn-light"  onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_Edit/${requestScope.noticeboardvo.notice_no}'" style="margin: 0 3%;"><i style='font-size:24px' class='fas'>&#xf044;</i>수정</button>
							<button style="width: 45%;" type="button" class="btn btn-light"  onclick="goDel()"><i style='font-size:18px' class='far'>&#xf2ed;</i>삭제</button>
						</c:if>
					</div>
					<button style="width: 45%;" type="button" class="btn btn-light"  onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_Write'"><i style='font-size:18px' class='fas'>&#xf304;</i>새글쓰기</button>
				</td>
			</tr>
		</table>
	
		<div style="border: solid 0px red;">
			<div style="display:inline-block; width: 100%; margin: 2% 0 1% 0%;">
				<div style="height: 616px; width: 100%; border: solid 0px gray; word-break: break-all; overflow: auto;" class="content-div">${noticeboardvo.notice_content}</div>
				<c:if test="${not empty requestScope.noticeboardvo.atnotice_orgfilename}">
					<div style="font-size: 15px; margin-top: 1%;">
						<button id="downloadButton" type="button" onclick="javascript:location.href='<%= ctxPath%>/board/download?notice_no=${requestScope.noticeboardvo.notice_no}'" class="css-button-arrow--sand" style="margin-right: 1%;">다운로드</button>
						<label style= "cursor: pointer;" for="downloadButton" onclick="javascript:location.href='<%= ctxPath%>/board/download?notice_no=${requestScope.noticeboardvo.notice_no}'">${requestScope.noticeboardvo.atnotice_orgfilename}</label><!-- 컨트롤러에서는 로그인을 해야지만 download 경로로 들어갈 수 있도록 Aop 사용 -->
					</div>
				</c:if>
			</div>
		</div>
		
		<div id="container_view" style="width: 100%;">
			<div id="container_viewList" class="container" style="margin: 0%; width: 90%; height: 20px;">            
<%-- ==== 이전글제목, 다음글제목 보기 시작 ==== --%>
		<c:if test="${not empty requestScope.noticeboardvo}">	
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
			        <td style="text-align: center;"><span class="move" onclick="goView('${requestScope.noticeboardvo.previousseq}')">${requestScope.noticeboardvo.previoussubject}</span></td>
			      </tr>
			      <tr>
			        <td style="font-weight: bold;">>></td>
			        <td style="text-align: center;">${requestScope.noticeboardvo.notice_subject}</td>
			      </tr>
			      <tr>
			        <td style="font-weight: bold;">다음글</td>
			        <td style="text-align: center;"><span class="move" onclick="goView('${requestScope.noticeboardvo.nextseq}')">${requestScope.noticeboardvo.nextsubject}</span></td>
			      </tr>
			    </tbody>
			  </table>
		</c:if>
<%-- ==== 이전글제목, 다음글제목 보기 끝 ==== --%>		
					
			<span>
				<button type="button" onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_noticeBoard'" class="btn btn-outline-secondary" style="margin-right: 2%;">전체목록조회</button>
				<button type="button" class="btn btn-outline-secondary" onclick="javascript:location.href='<%= ctxPath%>${requestScope.goBackURL}'">검색된목록조회</button>
			</span>
			</div>
		</div>
		
		
	</div>
	</div>
</div>


<form name="goViewFrm">
	<input type="hidden" name="notice_no" value="${requestScope.noticeboardvo.notice_no}"/>
	<input type="hidden" name="goBackURL"/>
	<input type="hidden" name="searchType"/>
	<input type="hidden" name="searchWord"/>
</form>