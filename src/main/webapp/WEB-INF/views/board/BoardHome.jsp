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
	
	$(document).ready(function() {

		if(${requestScope.boardLocation == 'boardDept'}){
			$("select[name='boardLocation']").val("deptBoard");
		}
		
		$("span.subject").hover(function(e){// 각각 mouseenter와 mouseleave의 기능
			$(e.target).addClass("subjectStyle");
		}, function(e){
			$(e.target).removeClass("subjectStyle");
		});
		
		$("input:text[name='searchWord']").bind("keyup", function(e){
			if(e.keyCode == 13){// 엔터를 했을 경우
				goSearch();
			}
		});
		
		// 검색시 검색조건 및 검색어 값 유지시키기
		if(${not empty requestScope.paraMap}) {
			$("select[name='searchType']").val("${requestScope.paraMap.searchType}");
			$("input[name='searchWord']").val("${requestScope.paraMap.searchWord}");
		}
		
		<%-- === #88. 검색어 입력시 자동글 완성하기 2 === --%>
		$("div#displayList").hide();
		
		$("input[name='searchWord']").keyup(function(){
			
			const wordLength = $(this).val().trim().length; // $(this) ==> $("input[name='searchWord']") 를 의미한다.
			// 검색어에서 공백을 제거한 길이를 알아온다.
			
			if(wordLength == 0) {// input 창에 글이 하나도 안 들어왔을 경우
				$("div#displayList").hide();
				// 검색어가 공백이거나 검색어 입력후 백스페이스키를 눌러서 검색어를 모두 지우면 검색된 내용이 안 나오도록 해야 한다.
			}
			else { // 검색어가 존재하는데
				
				if($("select[name='searchType']").val() == "subject" || 
				   $("select[name='searchType']").val() == "name") {// option이 글제목 또는 글쓴이 일 경우에만 적용
				
					$.ajax({ 
						url:"<%= ctxPath%>/board/wordSearchShow",
						type:"get",
						data:{"searchType":$("select[name='searchType']").val() // option의 value값이 select의 value값이 된다.
							, "searchWord":$("input[name='searchWord']").val()},// 컨트롤러에서 파라미터에서 map으로 받아줌
						dataType:"json",
						success:function(json){// 배열의 "길이"는 배열에 포함된 원소의 개수를 의미한다. 아래 배열의 길이는 = 6
						//	console.log(JSON.stringify(json));
							/*
								[{"word":"java가 쉽나요?"},
								 {"word":"JAVA공부를 하려고 해요. 도와주세요~~"},
								 {"word":"프로그래밍은 Java 를 해야 하나요?"},
								 {"word":"javascript 는 쉬운가요?"},
								 {"word":"프론트 엔드를 하려면 JavaScript 를 해야 하나요?"},
								 {"word":"질문있어요 jQuery 와 javaScript 는 관련이 있나요?"}]
							
								또는 []
							
							
							*/
							
							<%-- === #93. 검색어 입력시 자동글 완성하기 7 === --%>
							if(json.length > 0){// 검색된 데이터가 있는 경우임.
								
								let v_html = ``;
								$.each(json, function(index, item){
									
						/*
							배열을 사용하는 이유는 순서가 중요한 데이터를 다룰 때 더 직관적이고, 
							배열로 반환된 데이터는 $.each()로 순차적으로 순회하기에 더 적합하기 때문입니다. 
							순서가 중요하지 않다면 객체를 사용할 수도 있지만, 
							일반적으로 데이터를 순차적으로 처리해야 하는 경우에는 배열이 더 적합합니다.
						*/
									
									const word = item.word;
									/* 	   
										   java가 쉽나요?
										   JAVA공부를 하려고 해요. 도와주세요~~
										   프로그래밍은 Java 를 해야 하나요?
										   javascript 는 쉬운가요?	   
										   프론트 엔드를 하려면 JavaScript 를 해야 하나요?
										   질문있어요 jQuery 와 javaScript 는 관련이 있나요?			   
									*/
									
									// word.toLowerCase() 은 word 를 모두 소문자로 변경한다. // toLowerCase(소문자로), toUpperCase(대문자로) 는 javaScript이다.
									
									/*	   
										   java가 쉽나요?
										   java공부를 하려고 해요. 도와주세요~~
										   프로그래밍은 java 를 해야 하나요?
										   javascript 는 쉬운가요?	   
										   프론트 엔드를 하려면 javascript 를 해야 하나요?
										   질문있어요 jquery 와 javascript 는 관련이 있나요?			   
									*/
									
									const idx = word.toLowerCase().indexOf($("input[name='searchWord']").val().toLowerCase());
									// 만약에 검색어가 JavA이라면 
									
									/*  indexOf() 메서드는 배열이나 문자열에서 특정 요소(값)의 인덱스를 찾는 데 사용됩니다.
										이 메서드는 찾고자 하는 값이 배열이나 문자열에 있는지 확인하고, 
										그 값이 처음 나타나는 위치의 인덱스를 반환합니다. 
										값이 존재하지 않으면 -1을 반환합니다.
									*/
									
									/*	   java가 쉽나요?									은 idx 가 0 이다.		
										   java공부를 하려고 해요. 도와주세요~~ 				은 idx 가 0 이다.	
										   프로그래밍은 java 를 해야 하나요?   				은 idx 가 7 이다.	
										   javascript 는 쉬운가요?	       					은 idx 가 0 이다.	
										   프론트 엔드를 하려면 javascript 를 해야 하나요? 		은 idx 가 12 이다.	
										   질문있어요 jquery 와 javascript 는 관련이 있나요?	은 idx 가 15 이다.		   
									*/
									
									const len = $("input[name='searchWord']").val().length;
									// 검색어(JavA)의 길이 len 은 4 가 된다.
									/*
										console.log("~~~ 시작 ~~~");
										console.log(word.substring(0, idx)); 		// 검색어(JavA) 앞까지의 글자 	==> 프로그래밍은 			// 프로그래밍은 Java 를 해야 하나요?//
										console.log(word.substring(idx, idx+len));  // 검색어(JavA) 글자 			==> Java
										console.log(word.substring(idx+len));		// 검색어(JavA) 뒤부터 끝까지 글자 ==> 를 해야 하나요?
										console.log("~~~ 끝 ~~~");
									*/
									
									const result = word.substring(0, idx) + "<span style='color:purple;'>"+word.substring(idx, idx+len)+"</span>" + word.substring(idx+len);
									
									v_html += `<span style='cursor:pointer;' class='result'>\${result}</span><br>`;
									
								});// end of $.each(json, function(index, item){})---------------------------
								
								const input_width = $("input[name='searchWord']").css("width"); // 검색어 input 태그 width 값 알아오기 // .css({"width": })는 값을 넣어주는 방법이니 헷갈리지 않기.
								
								$("div#displayList").css({"width":input_width}); // 검색결과 div 의 width 크기를 검색어 입력 input 태그의 width 와 일치시키기
								
								$("div#displayList").html(v_html).show(); 
							}
						},
						error: function(request, status, error){
		                     alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		                }
					});
					
				}
			}
			
		});// end of $("input[name='searchWord']").keyup(function(){})-----------------------------
		
		
		<%-- === #94. 검색어 입력시 자동글 완성하기 8 === --%>
		$(document).on('click',"span.result", function(e){
			const word = $(e.target).text();
			$("input[name='searchWord']").val(word); // 클릭한 것의 text를 넣어준다. // 텍스트박스에 검색된 결과의 문자열을 입력해준다.
			$("div#displayList").hide();
			goSearch();
		});// end of $(document).on('click',"span.result", function(e){})------------------
		
		
	});// end of $(document).ready(function(){})--------------------------

	
	// Function Declaration
	function goView(board_no){
		
	<%--
		location.href=`<%= ctxPath%>/board/view?seq=\${seq}`; 
	--%>
		<%--역슬레시를 사용하는 이유 : jsp 내에서 ${}로 javaScript 를 사용할 때 \를 붙여주지 않으면 jsp에서 먼저 처리하므로 \를 붙이므로써 js가 ${}안의 데이터를 가져갈 수 있도록 하기 위함이다.--%>
		
		const goBackURL = "${requestScope.goBackURL}";
	//	console.log(goBackURL);
		//  /board/list?searchType=subject&searchWord=%EC%9E%85%EB%8B%88%EB%8B%A4(한글로 입니다라는 의미)&currentShowPageNo=7
		
	<%--		
		아래처럼 get 방식으로 보내면 안된다. 왜냐하면 get 방식에서 &는 데이터의 구분자로 사용되기 때문이다.!!! 중요 !!!		
		그래서 BoardController 의 @GetMapping("view") 에 가서 @RequestParam String goBackURL 에 저장된 값을 꺼내보면 
		/board/list?searchType=subject&searchWord=%EC%9E%85%EB%8B%88%EB%8B%A4(한글로 입니다라는 의미)&currentShowPageNo=7 와 같이 나오지 않고
		/board/list?searchType=subject 까지만 나온다.
		그래서 보내줄 데이터에 &가 들어가 있는 경우라면 get 방식이 아닌 post 방식으로 전달해줘야한다.
	--%>	
	<%--
		꽝!!! =>	location.href=`<%= ctxPath%>/board/view?seq=\${seq}&goBackURL=\${goBackURL}`;
	--%>	
	
	<%-- 그러므로 &를 데이터의 구분자로 사용하지 않고 글자 그대로 인식하도록 하기 위해 post 방식으로 보내야한다.  
		 아래의 본문에 #105 에 표기된 form 태그를 먼저 만든다. --%>
	const frm = document.goViewFrm;
	frm.board_no.value = board_no;
	frm.goBackURL.value = goBackURL;
	
	
	/* searchType 과 searchWord 도 view단 페이지로 보내줘야 검색어가 있을 경우 이전글 다음글을 볼 때 해당 검색어가 있는 1개의 글을 알아올 수 있다. */
	
	if(${not empty requestScope.paraMap}) { // 검색조건이 있을 경우
		frm.searchType.value = "${requestScope.paraMap.searchType}";
		frm.searchWord.value = "${requestScope.paraMap.searchWord}";
	}
	
	frm.method = "post";
	frm.action = "<%= ctxPath%>/board/view";
	frm.submit();
	
	}// end of function goView(seq){})-----------------------
		
	});// end of $(document).ready(function()})----------------------------------------------------------
	
	
	
function goSearch() { // 검색을 클릭하면
		
		const frm = document.searchFrm; // 폼 안에 있는 모든 밸류값들을 list controller로 넘겨준다.
<%--	
		frm.method = "get";
		frm.action = "<%= ctxPath%>/board/BoardHome"; // 자기자신의 페이지로 이동
--%>
		frm.submit();
	}// end of function goSearch()--------------------------------
	
</script>

<div class="common_wrapper" style="margin: 0%;">
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
        
<div class="contents_wrapper">

<!-- 페이지 공통 부분  -->
<p class="bg-light text-dark" style="font-size: 20pt; font-weight: bold; padding: 1% 0 1% 3%;">부서 게시판 홈 <i style='font-size:30px; margin-left: 1.5%;' class='far'>&#xf328;</i></p>
<!-- 페이지 공통 부분  -->


	<span class="dropdown">
	   <select class="form-select" name="boardLocation" style="height: 30px; margin: 0 0 2% 1.5%;">
	       <option value="notice">전체 게시판</option>
	       <option value="boardDept">부서 게시판[]</option>
	   </select>
	</span>




<div class="container" style="width: 80%; margin: 0 77% 0 0; padding-left: 0%;" >
  <table class="table" style="width: 1010px; margin-left: 2%;">
    <thead class="thead-dark">
       <tr>
       	  <th style="width: 40px;  text-align: center;">순번</th>
          <th style="width: 230px; text-align: left;">제목</th>
          <th style="width: 70px;  text-align: center;">성명</th>
          <th style="width: 150px; text-align: center;">날짜</th>
          <th style="width: 60px;  text-align: center;">조회수</th>
        </tr>
    </thead>
    <tbody>
    <c:if test="${not empty requestScope.boardList}">
    	<!-- 부서게시판에 글작성을 했을 경우 -->
    	<c:if test="${requestScope.boardLocation == 'boardDept'}">
      		<c:forEach var="boardList" items="${requestScope.boardList}" varStatus="status">
      			<tr>
	      			<td align="center">
	      				${(requestScope.totalCount) - (requestScope.currentShowPageNo - 1) * (requestScope.sizePerPage) - (status.index)}
	      			</td>
      				<td>
      					<%-- === 댓글쓰기 및 파일첨부가 있는 게시판 시작 === --%>
	      					
	      					<%-- ======= 첨부파일이 없는 경우 시작 ======= --%>
	      					<c:if test="${empty boardList.atboard_fileName}">
		      					<%-- 댓글이 있는 경우 시작 --%>
			      					<%-- <c:if test="${boardList.commentCount > 0}"> --%>
				      					<c:if test="${fn:length(boardList.subject) < 30}">
				                           <span class="subject" onclick="goView('${boardList.board_no}')">${boardList.subject}<span style="vertical-align: super;"></span></span>
				                        </c:if>
				                        <c:if test="${fn:length(boardList.subject) >= 30}">
				                           <span class="subject" onclick="goView('${boardList.board_no}')">${fn:substring(boardList.subject, 0, 28)}..<span style="vertical-align: super;"></span></span>
				                        </c:if>
				                    <%-- </c:if> --%>
		                        <%-- 댓글이 있는 경우 끝 --%>
		      					
		      					
		                        <%-- 댓글이 없는 경우 시작 --%><!-- 댓글이 없는 경우라면 0을 나타내는 것이 아니라 숫자를 아예 안 보이도록 하겠다. -->
			                        <c:if test="${fn:length(boardList.subject) < 30}">
			                           <span class="subject" onclick="goView('${boardList.board_no}')">${boardList.subject}</span>
			                        </c:if>
			                        <c:if test="${fn:length(boardList.subject) >= 30}">
			                           <span class="subject" onclick="goView('${boardList.board_no}')">${fn:substring(boardList.subject, 0, 28)}..</span>
			                        </c:if>
		                        <%-- 댓글이 없는 경우 끝 --%>   
	                        </c:if>
	                        <%-- ======= 첨부파일이 없는 경우 끝 ======= --%> 
	                   
	                   
	                   
	                   <%-- ======= 첨부파일이 있는 경우 시작 ======= --%>
	      					<c:if test="${not empty boardList.atboard_fileName}">
		      					<%-- 댓글이 있는 경우 시작 --%>
			      					<%-- <c:if test="${boardList.commentCount > 0}"> --%>
				      					<c:if test="${fn:length(boardList.subject) < 30}">
				                           <span class="subject" onclick="goView('${boardList.board_no}')">${boardList.subject}&nbsp;<span style="vertical-align: super;"></span></span>
				                        </c:if>
				                        <c:if test="${fn:length(boardList.subject) >= 30}">
				                           <span class="subject" onclick="goView('${boardList.board_no}')">${fn:substring(boardList.subject, 0, 28)}..&nbsp;<span style="vertical-align: super;"></span></span>
				                        </c:if>
				                   <%--  </c:if> --%>
		                        <%-- 댓글이 있는 경우 끝 --%>
		                        
		                        <%-- 댓글이 없는 경우 시작 --%><!-- 댓글이 없는 경우라면 0을 나타내는 것이 아니라 숫자를 아예 안 보이도록 하겠다. -->
		                        <%-- <c:if test="${boardList.commentCount == 0}"> --%>
			                        <c:if test="${fn:length(boardList.subject) < 30}">
			                           <span class="subject" onclick="goView('${boardList.board_no}')">${boardList.subject}&nbsp;</span>
			                        </c:if>
			                        <c:if test="${fn:length(boardList.subject) >= 30}">
			                           <span class="subject" onclick="goView('${boardList.board_no}')">${fn:substring(boardList.subject, 0, 28)}..&nbsp;</span>
			                        </c:if>
			                    <%-- </c:if> --%>
			                 </c:if>
		                        <%-- 댓글이 없는 경우 끝 --%>   
	                  <%-- ======= 첨부파일이 있는 경우 끝 ======= --%>
	                   
                       <%-- === 댓글쓰기 및 답변형 및 파일첨부가 있는 게시판 끝 === --%>	 
      				</td>
      				<td align="center">${boardList.name}</td>
      				<td align="center">${boardList.board_regDate}</td>
      				<td align="center">${boardList.view_count}</td>
      			</tr>
      		</c:forEach>
      	</c:if>
      </c:if>
      </tbody>
  </table>
</div>  
    <%-- === 페이지바 보여주기 === --%>
  <div align="center" style="border: solid 0px gray; width: 80%; margin: 30px auto;">
  	  ${requestScope.pageBar}
  </div>
  
	</div>
</div>