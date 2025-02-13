<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
   String ctxPath = request.getContextPath();
    //     /myspring
%>

<jsp:include page="../board/GroupWareHeader.jsp" /> 



<!-- 페이지 공통 부분  -->
	<p class="bg-light text-dark" style="font-size: 20pt; font-weight: bold; padding: 1% 0 1% 3%;">전체게시판&nbsp;<i style='font-size:24px' class='fas'>&#xf105;</i>&nbsp;<span style="font-size: 15pt;">공지사항</span></p>
<!-- 페이지 공통 부분  -->

<script>

	$(document).ready(function(){
		
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
		
	});

	
</script>


<div id="container" style="border: solid 0px gray; width: 95%; margin: 2% 2%;">
	<div id="container_viewOne" style="width: 100%;">
		<div id="container_view" style="width: 100%;">
			<div style="margin-bottom: 2%;">
				<i style='font-size:30px; margin-right: 1%;' class='far'>&#xf2bd;</i>
				<span>성명</span>
				<span>작성일자</span>
				<input type="hidden" name="fk_emp_id" value="${sessionScope.loginuser.fk_emp_id}"/>
			</div>	
			<div>
				<button type="button" class="btn btn-light"  onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_Write'"><i style='font-size:24px' class='fas'>&#xf304;</i>새글쓰기</button>
				<button type="button" class="btn btn-light"  onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_Edit'" style="margin: 0 3%;"><i style='font-size:24px' class='fas'>&#xf044;</i>수정</button>
				<button type="button" class="btn btn-light"  onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_Del'"><i style='font-size:24px' class='far'>&#xf2ed;</i>삭제</button>
			</div>
			
			<div style="display: flex; margin-right: 78%; margin-top:2%; border: solid 0px red; width: 100%; height: 60px;">
				<span style="font-size: 20px; margin-left: 1.5%; margin-right: 1.5%;">제목</span><input type="text" class="form-control" id="subject" style="border: solid 1px gray; width: 70%;" name="subject"/>
			</div>
			<div class="container" style="background-color: #f2f2f2; padding-bottom: 2%; font-size: 20px; width: 100%;">내용
				<div style="display:inline-block; text-align: right; width: 40%; margin: 2% 0 1% 53.3%;">
					<span style="margin-right: 2%; font-size: 15px;" id="viewCount">조회수</span>
					<button type="button" id="like" style="font-size: 15px; border: none;" >좋아요</button>
					<label for="like"><i id="likeColor" class="fa fa-heart" style="font-size:40px; color:gray; border: solid 2px gray; background-color: white; padding: 0.5%; margin-left: 0.5%;"></i></label>
				</div>
				<textarea style="display: block; height: 400px; width: 98%; margin: "></textarea>
				<div style="font-size: 15px; margin-top: 1%;">첨부파일 다운로드 div</div>
			</div>
		</div>
		
		
		<div id="container_review" style="width: 100%;">
			<div id="newReview">
				js에서 반복문 설정 댓글작성 완료시 나오는 댓글 div
			</div>
			
			<div id="review" class="container" style="background-color: #f2f2f2; border: solid 1px gray; height: 70px; padding-top: 1%; width: 100%;">
				<i style='border-radius:50%; font-size:24px; margin-right: 1%; vertical-align: top; background-color: white;' class='far'>&#xf2bd;</i><span><textarea style="width:80%; h" placeholder="댓글을 남겨보세요"></textarea><button class="btn btn-secondary" style="font-size:10px; float:right; margin-top: 2.5%;">등록</button></span>
			</div>
		</div>
		
		
		<div id="container_viewList" class="container" style="margin-top: 3%; ">            
		  <table class="table table-striped">
		    <thead>
		      <tr>
		        <th></th>
		        <th>제목</th>
		      </tr>
		    </thead>
		    <tbody>
		      <tr>
		        <td>이전글</td>
		        <td>john@example.com</td>
		      </tr>
		      <tr>
		        <td>>></td>
		        <td>mary@example.com</td>
		      </tr>
		      <tr>
		        <td>다음글</td>
		        <td>july@example.com</td>
		      </tr>
		    </tbody>
		  </table>
		</div>
		
		<span style="margin-left: 1.5%;">
			<button type="button" onclick="javascript:location.href='<%= ctxPath%>/board/GroupWare_Board'" class="btn btn-outline-secondary" style="margin-right: 2%;">전체목록조회</button>
			<button type="button" class="btn btn-outline-secondary">검색된목록조회</button>
		</span>
	</div>
</div>


<jsp:include page="../board/GroupWarefooter.jsp" /> 