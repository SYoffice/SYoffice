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

</style>

<script>

	$(document).ready(function(){
<%-- 		
		$("button#like").click(function(){
			$ajax({
				url:"<%= ctxPath%>/board/like",
				type:"post",
				data:{"fk_board_no":${requestScope.boardvo.fk_board_no},   // 게시글번호
					  "fk_emp_id":${sessionScope.loginuser.fk_emp_id}},	   // 사원번호
				dataType:"json",
				success:function(json) {
					console.log(JSON.stringify(json));
					
					if(json.n == 1){ // 제대로 insert 가 되어져서 해당 게시글에 좋아요 개수가 1증가했다면
						$("i#likeColor").css("color","red");
					}
					
				},
			    error: function(request, status, error){
	               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	            }
			});
			
		});
		 --%>
	});

function goDel() {
	
	confirm("정말 삭제하시겠습니까?");
	
	if()
	
	const frm = document.goViewFrm;
	frm.method = "POST";
	frm.action = "<%= ctxPath%>/board/GroupWare_Del";
	frm.submit();
	
}// end of goDel()-----------------------	
	
	
</script>

<div class="common_wrapper" style="margin:0%;">
    <div class="side_menu_wrapper">
        <div class="side_menu_inner_wrapper">
      	<button type="button"  id="write" onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_Write'">글쓰기</button>
            <ul class="side_menu_list">
                <li style="font-weight: bold;"><a href="<%= ctxPath%>/board/GroupWare_noticeBoard?boardLocation=notice">전체 게시판</a></li>
                <li style="margin-left: 10%; font-size: 11pt;"><a href="<%= ctxPath%>/board/GroupWare_noticeBoard?boardLocation=notice">공지사항</a></li>
                <li style="font-weight: bold;"><a href="<%= ctxPath%>/board/GroupWare_Board?boardLocation=boardDept">부서 게시판[${requestScope.fk_dept_id}]</a></li>
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
		<p class="bg-light text-dark" style="font-size: 20pt; font-weight: bold; padding: 1% 0 1% 4.5%;">전체게시판&nbsp;<i style='font-size:24px' class='fas'>&#xf105;</i>&nbsp;<span style="font-size: 15pt;">공지사항</span></p>
	</div>	
	<!-- 페이지 공통 부분  -->
	
	<div id="container" style="border: solid 0px gray; width: 90%; margin: 0 20% 0 5%;">
		<table style="width: 100%;">
			<tr style="margin-bottom: 3%; height: 62px;">
				<th style="width: 7%;">
					<div style="font-size: 25px; font-weight: bold;">[제목]</div>
				</th>
				<td>
					<div style="display: flex; margin-top:2%; border: solid 0px red; width: 100%; height: 60px;">
						<span id="subject" style="border: solid 0px gray; width: 100%; padding-top: 0.5%; font-size: 25px; ">${noticeboardvo.notice_subject}</span>
					
						<span style="border: solid 0px gray;  width: 100%; text-align: right;">
							<span style="margin-right: 2%; font-size: 15px; font-weight: normal; width: 50%;" id="viewCount">조회수&nbsp;&nbsp;<span style="font-weight: normal; ">${noticeboardvo.notice_viewcount}</span></span>
							<button type="button" id="like" style="font-size: 15px; border: none; width: 13%;" >좋아요</button>
							<label for="like"><i id="likeColor" class="fa fa-heart" style="font-size:40px; color:gray; border: solid 2px gray; background-color: white; padding: 0.5%; margin-left: 0.5%;"></i></label>
						</span>
					</div>
				</td>
			</tr>
		
			<tr>
				<th style="width: 5%; padding-left: 0.5%;">
					<i style='font-size:40px;' class='far'>&#xf2bd;</i>
				</th>
				<td>
					<span style="margin-right: 0.5%;">${noticeboardvo.name}</span>
					<span style="font-size: 10pt;">${noticeboardvo.notice_regdate}</span>
					<input type="hidden" name="fk_emp_id" value=""/>
				</td>
			</tr>
			
			<tr>
				<th></th>
				<td>
					<div style="display: flex; width: 100%; margin-left: 75%;">
						<button type="button" class="btn btn-light"  onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_Write'"><i style='font-size:18px' class='fas'>&#xf304;</i>새글쓰기</button>
						<button type="button" class="btn btn-light"  onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_Edit/${requestScope.noticeboardvo.notice_no}'" style="margin: 0 3%;"><i style='font-size:24px' class='fas'>&#xf044;</i>수정</button>
						<button type="button" class="btn btn-light"  onclick="goDel()"><i style='font-size:18px' class='far'>&#xf2ed;</i>삭제</button>
					</div>
				</td>
			</tr>
		</table>
	
		<div style="border: solid 0px red;">
			<div style="display:inline-block; width: 100%; margin: 2% 0 1% 0%;">
				<div style="height: 616px; width: 100%; border: solid 0px gray; word-break: break-all; overflow: scroll;" class="content-div">${noticeboardvo.notice_content}</div>
				<c:if test="${not empty requestScope.noticeboardvo.atnotice_orgfilename}">
					<div style="font-size: 15px; margin-top: 1%;">
						<button id="downloadButton" type="button" onclick="javascript:location.href='<%= ctxPath%>/board/download?notice_no=${requestScope.noticeboardvo.notice_no}'" class="css-button-arrow--sand" style="margin-right: 1%;">다운로드</button>
						<label style= "cursor: pointer;" for="downloadButton" onclick="javascript:location.href='<%= ctxPath%>/board/download?notice_no=${requestScope.noticeboardvo.notice_no}'">${requestScope.noticeboardvo.atnotice_orgfilename}</label><!-- 컨트롤러에서는 로그인을 해야지만 download 경로로 들어갈 수 있도록 Aop 사용 -->
					</div>
				</c:if>
			</div>
		</div>

		<div id="newReview">
			js에서 반복문 설정 댓글작성 완료시 나오는 댓글 div
		</div>	
		
		<div id="review" class="msg_wrap" style="width: 100%; border-radius: 4px; border: 1px solid #ddd; height: 82px; padding-top: 1%; padding-right: 1%; padding-left: 0.5%; margin-bottom: 2%;">
			<i style=' font-size:24px; margin-right: 1%; vertical-align: top; background-color: white;' class='far'>&#xf2bd;</i>
			<span>
				<input style="width: 92%; height: 50px; border: 1px solid #ddd;" placeholder="댓글을 남겨보세요"/>
				<button class="btn btn-secondary" style="font-size:12px; float:right; margin-top: 1%; ">등록</button>
			</span>
		</div>
		<div id="container_view" style="width: 100%;">
			<div id="container_viewList" class="container" style="margin: 0%; width: 90%; height: 20px;">            
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
			        <td style="text-align: center;">${noticeboardvo.previoussubject}</td>
			      </tr>
			      <tr>
			        <td style="font-weight: bold;">>></td>
			        <td style="text-align: center;">${noticeboardvo.notice_subject}</td>
			      </tr>
			      <tr>
			        <td style="font-weight: bold;">다음글</td>
			        <td style="text-align: center;">${noticeboardvo.nextsubject}</td>
			      </tr>
			    </tbody>
			  </table>
					
			<span>
				<button type="button" onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_noticeBoard?boardLocation=notice'" class="btn btn-outline-secondary" style="margin-right: 2%;">전체목록조회</button>
				<button type="button" class="btn btn-outline-secondary">검색된목록조회</button>
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

<jsp:include page="../board/GroupWarefooter.jsp" /> 