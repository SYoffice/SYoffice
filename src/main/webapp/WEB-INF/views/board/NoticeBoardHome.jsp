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

<style type="text/css">

.table td {
	vertical-align: inherit;
}

.subjectStyle {font-weight: lighter;
               color: #4d94ff;
               cursor: pointer; }
               
a {text-decoration: none !important;} /* 페이지바의 a 태그에 밑줄 없애기 */
/* //////////////////////////////////////////////////////////////////// */ 
 /* 컨테이너 스타일 */
.search-container {
  position: relative; 
  display: inline-block; 
}

/* 검색창 스타일 */
.search-container input[type="text"] {
  width: 300px;                /* 검색창 가로 길이 */
  padding: 10px 45px 10px 15px;/* 상하좌우 패딩 (오른쪽 패딩을 크게 잡아 아이콘 영역 확보) */
  font-size: 14px;
  border-radius: 20px;         /* 둥근 모서리 */
  border: 1px solid #ccc;      /* 테두리 색상 */
  outline: none;               /* 포커스시 파란색 라인 제거(브라우저 기본) */
  transition: box-shadow 0.2s; /* 호버/포커스 효과 부드럽게 */
}

/* 검색창 포커스시 살짝 강조 효과 */
.search-container input[type="text"]:focus {
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
}

/* 검색 버튼 스타일 */
.search-container .search-btn {
  position: absolute;          /* 부모 요소를 기준으로 위치 */
  right: 10px;                 /* 오른쪽 여백 */
  top: 76%;                    /* 세로 중앙 정렬 */
  transform: translateY(-50%); /* 버튼을 중앙에 맞추기 위해 세로 방향 -50% 이동 */
  background: none;            /* 배경 투명 */
  border: none;                /* 기본 테두리 제거 */
  cursor: pointer;             /* 마우스 커서 표시 */
  outline: none;               /* 포커스시 파란 라인 제거 */
  color: #666;                 /* 아이콘 색상 */
  font-size: 16px;             /* 아이콘 크기 */
  transition: color 0.2s;
}

/* 버튼 호버 시 아이콘 색상 변화 */
.search-container .search-btn:hover{
  color: #333;
}
   
    
</style>

<div class="common_wrapper" style="margin: 0%;">
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

<div style="display: inline-block; width: 97%; margin-bottom: 2%;">
<span class="bg-light text-dark" style="font-size: 20pt; font-weight: bold; padding: 1% 0 1% 3%;">공지사항<i style='font-size:30px; margin-left: 1%;' class='far'>&#xf328;</i></span>

<span style="float: right;">
<!-- 검색바 -->
<form name="searchFrm" style="display: flex; gap: 5px; margin-top: 2%; height: 30px;  width: 100%;">
    <select class="form-select" name="searchType" style="border-radius: 20px; border: 1px solid #ccc; height: 45px; transition: box-shadow 0.2s; color: #666; width: 120px; padding-left: 2.5%;">
         <option style="border-radius: 20px; border: 1px solid #ccc; height: 45px; transition: box-shadow 0.2s; color: #666;" value="subject">제목</option>
         <option style="border-radius: 20px; border: 1px solid #ccc; height: 45px; transition: box-shadow 0.2s; color: #666;" value="name">작성자</option>
         <option style="border-radius: 20px; border: 1px solid #ccc; height: 45px; transition: box-shadow 0.2s; color: #666;" value="content">내용</option>
         <option style="border-radius: 20px; border: 1px solid #ccc; height: 45px; transition: box-shadow 0.2s; color: #666;" value="subject_content">제목+내용</option>
    </select>
	<div class="search-container">
	  <input type="text" style="font-size:13pt;" name="searchWord" placeholder="Search..." />
	  <button type="submit" class="search-btn" onclick="goSearch()">
	    <i class="fas fa-search"></i>
	  </button>
	</div>
</form>
<!-- 페이지 공통 부분  -->
</span>
</div>


  <table class="table" style="width: 95%; margin: 0 auto;">
    <thead>
       <tr style="background-color: #e6eeff;">
       	  <th style="width: 40px;  text-align: center;">순번</th>
          <th style="width: 230px; text-align: center;">제목</th>
          <th style="width: 70px;  text-align: center;">성명</th>
          <th style="width: 150px; text-align: center;">날짜</th>
          <th style="width: 60px;  text-align: center;">조회수</th>
        </tr>
    </thead>
    <tbody>
    <c:if test="${not empty requestScope.noticeBoardList}">
        <!-- 공지사항 게시판에 글작성을 했을 경우 -->
      		<c:forEach var="noticeList" items="${requestScope.noticeBoardList}" varStatus="status">
      			<tr>
					<td align="center">
					${(requestScope.totalCount) - (requestScope.currentShowPageNo - 1) * (requestScope.sizePerPage) - (status.index)}
					</td>
      				<td align="center">
      					<%-- === 댓글쓰기 및 파일첨부가 있는 게시판 시작 === --%>
	      					
	      				<%-- ======= 첨부파일이 없는 경우 시작 ======= --%>
	      				<c:if test="${empty noticeList.atnotice_filename}">
		      					<c:if test="${fn:length(noticeList.notice_subject) < 20}">
		                           <span class="subject" onclick="goView('${noticeList.notice_no}')">${noticeList.notice_subject}<span style="vertical-align: super;"></span></span>
		                        </c:if>
		                        <c:if test="${fn:length(noticeList.notice_subject) >= 20}">
		                           <span class="subject" onclick="goView('${noticeList.notice_no}')">${fn:substring(noticeList.notice_subject, 0, 28)}..<span style="vertical-align: super;"></span></span>
		                    	</c:if>
	                    </c:if>
	                    <%-- ======= 첨부파일이 없는 경우 끝 ======= --%> 
	                   
                  		<%-- ======= 첨부파일이 있는 경우 시작 ======= --%>
      					<c:if test="${not empty noticeList.atnotice_filename}">
	      					<c:if test="${fn:length(noticeList.notice_subject) < 20}">
	                           <span class="subject" onclick="goView('${noticeList.notice_no}')">${noticeList.notice_subject}&nbsp;<span style="vertical-align: super;"></span></span>
	                        </c:if>
	                        <c:if test="${fn:length(noticeList.notice_subject) >= 20}">
	                           <span class="subject" onclick="goView('${noticeList.notice_no}')">${fn:substring(noticeList.notice_subject, 0, 28)}..&nbsp;<span style="vertical-align: super;"></span></span>
	                        </c:if>
		                </c:if>   
                  		<%-- ======= 첨부파일이 있는 경우 끝 ======= --%>
      				</td>
      				<td align="center">${noticeList.name}</td>
      				<td align="center">${noticeList.notice_regdate}</td>
      				<td align="center">${noticeList.notice_viewcount}</td>
      			</tr>
      		</c:forEach>
      	</c:if>
      	
        <c:if test="${empty requestScope.noticeBoardList}">
			<tr>
				<td colspan="6" style="text-align: center; height: 40px; padding-top: 3%;">등록된 게시글이 없습니다.</td>
			</tr>
      	</c:if>
      </tbody>
  </table>
 <%-- === 페이지바 보여주기 === --%>
 <div align="center" style="border: solid 0px gray; width: 80%; margin: 30px auto;">
 	  ${requestScope.pageBar}
 </div>
 
 
 
	</div>
</div>

<%--  페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후
      사용자가 "검색된결과목록보기" 버튼을 클릭했을때 돌아갈 페이지를 알려주기 위해
      현재 페이지 주소를 뷰단으로 넘겨준다.  --%>

<form name="goViewFrm">
	<input type="hidden" name="notice_no"/>
	<input type="hidden" name="goBackURL"/>
	<input type="text" name="searchType"/>
	<input type="text" name="searchWord"/>
</form>




<script type="text/javascript">
	
	$(document).ready(function() {
		
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
		
		//검색 시 검색조건 및 검색어 값 유지시키기
		if (${!empty requestScope.paraMap.searchType && !empty requestScope.paraMap.searchWord}) {
			$("select[name='searchType']").val("${requestScope.paraMap.searchType}");
			$("input:text[name='searchWord']").val("${requestScope.paraMap.searchWord}");
		}
		
	});// end of $(document).ready(function(){})--------------------------

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
frm.action = "<%= ctxPath%>/board/viewOne";
frm.submit();

}// end of function goView(notice_no){})-----------------------

	
function goSearch() { // 검색을 클릭하면
		// 폼 안에 있는 모든 밸류값들을 GroupWare_noticeBoard controller로 넘겨준다.
		const frm = document.searchFrm; 
<%--	
		frm.method = "get";
		frm.action = "<%= ctxPath%>/board/NoticeBoardHome"; // 자기자신의 페이지로 이동
--%>
		frm.submit();
	}// end of function goSearch()--------------------------------
	
</script>