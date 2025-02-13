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
	<p class="bg-light text-dark" style="font-size: 20pt; font-weight: bold; padding: 1% 0 1% 3%;">게시판 홈 <i style='font-size:30px; margin-left: 1.5%;' class='far'>&#xf328;</i></p>
<!-- 페이지 공통 부분  -->


<form name="BoardFrm">
	<span class="dropdown">
	   <select class="form-select" name="searchType" style="height: 30px; margin: 0 0 2% 1.5%;">
	       <option value="subject">전체 게시판</option>
	       <option value="name">부서 게시판[부서명]</option>
	   </select>
	</span>
</form>



<div class="container">
  <table class="table">
    <thead class="thead-dark">
       <tr>
          <th style="width: 70px;  text-align: center;">순번</th>
          <th style="width: 70px;  text-align: center;">글번호</th>
          <th style="width: 300px; text-align: center;">제목</th>
          <th style="width: 70px;  text-align: center;">성명</th>
          <th style="width: 150px; text-align: center;">날짜</th>
          <th style="width: 60px;  text-align: center;">조회수</th>
        </tr>
    </thead>
    <tbody>
    <c:if test="${not empty requestScope.boardList}">
      		<c:forEach var="boardvo" items="${requestScope.boardList}" varStatus="status">
      			<tr>
      				<td align="center">
      				${(requestScope.totalCount) - (requestScope.currentShowPageNo - 1) * (requestScope.sizePerPage) - (status.index)}
      				<%-- >>> 페이징 처리시 보여주는 순번 공식 <<<
                     데이터개수 - (페이지번호 - 1) * 1페이지당보여줄개수 - 인덱스번호 => 순번 
                  
                     <예제>
                     데이터개수 : 12
                     1페이지당보여줄개수 : 5
                  
                     ==> 1 페이지       
                     12 - (1-1) * 5 - 0  => 12
                     12 - (1-1) * 5 - 1  => 11
                     12 - (1-1) * 5 - 2  => 10
                     12 - (1-1) * 5 - 3  =>  9
                     12 - (1-1) * 5 - 4  =>  8
                  
                     ==> 2 페이지
                     12 - (2-1) * 5 - 0  =>  7
                     12 - (2-1) * 5 - 1  =>  6
                     12 - (2-1) * 5 - 2  =>  5
                     12 - (2-1) * 5 - 3  =>  4
                     12 - (2-1) * 5 - 4  =>  3
                  
                     ==> 3 페이지
                     12 - (3-1) * 5 - 0  =>  2
                     12 - (3-1) * 5 - 1  =>  1 
                	 --%>
      				
      				</td>
      				<td align="center">${boardvo.seq}</td>
      				<td>
      					<%-- === 댓글쓰기 및 답변형 및 파일첨부가 있는 게시판 시작 === --%>
      						<%-- 첨부파일이 없는 경우 시작 --%>
	      					
	      					<%-- ======= #157. 첨부파일이 없는 경우 시작 ======= --%>
	      					<c:if test="${empty boardvo.fileName}">
	      					<%-- >>>>>>>>> #142. 원글인 경우 시작 <<<<<<<<< --%>
		      					<%-- 댓글이 있는 경우 시작 --%>
			      					<c:if test="${boardvo.depthno == 0 && boardvo.commentCount > 0}">
				      					<c:if test="${fn:length(boardvo.subject) < 30}">
				                           <span class="subject" onclick="goView('${boardvo.seq}')">${boardvo.subject}<span style="vertical-align: super;">[<span style="color: red; font-style: italic; font-size: 9pt; font-weight: bold;">${boardvo.commentCount}</span>]</span></span>
				                        </c:if>
				                        <c:if test="${fn:length(boardvo.subject) >= 30}">
				                           <span class="subject" onclick="goView('${boardvo.seq}')">${fn:substring(boardvo.subject, 0, 28)}..<span style="vertical-align: super;">[<span style="color: red; font-style: italic; font-size: 9pt; font-weight: bold;">${boardvo.commentCount}</span>]</span></span>
				                        </c:if>
				                    </c:if>
		                        <%-- 댓글이 있는 경우 끝 --%>
		                        
		                        <%-- 댓글이 없는 경우 시작 --%><!-- 댓글이 없는 경우라면 0을 나타내는 것이 아니라 숫자를 아예 안 보이도록 하겠다. -->
		                        <c:if test="${boardvo.depthno == 0 && boardvo.commentCount == 0}">
			                        <c:if test="${fn:length(boardvo.subject) < 30}">
			                           <span class="subject" onclick="goView('${boardvo.seq}')">${boardvo.subject}</span>
			                        </c:if>
			                        <c:if test="${fn:length(boardvo.subject) >= 30}">
			                           <span class="subject" onclick="goView('${boardvo.seq}')">${fn:substring(boardvo.subject, 0, 28)}..</span>
			                        </c:if>
			                    </c:if>
		                        <%-- 댓글이 없는 경우 끝 --%>
	                       <%-- >>>>>>>>> 원글인 경우 끝 <<<<<<<<< --%>    
	                    
	                    
	                    
	                    <%-- >>>>>>>>> #143. 답변글인 경우 들여쓰기 시작 <<<<<<<<< --%>
		                    <%-- 댓글이 있는 경우 시작 --%>
	      					<c:if test="${boardvo.depthno > 0 && boardvo.commentCount > 0}">
		      					<c:if test="${fn:length(boardvo.subject) < 30}">
		                           <span class="subject" onclick="goView('${boardvo.seq}')"><span style="color: red; font-style: italic; padding-left: ${boardvo.depthno * 20}px;">└Re&nbsp;</span>${boardvo.subject}<span style="vertical-align: super;">[<span style="color: red; font-style: italic; font-size: 9pt; font-weight: bold;">${boardvo.commentCount}</span>]</span></span>
		                        </c:if>
		                        <c:if test="${fn:length(boardvo.subject) >= 30}">
		                           <span class="subject" onclick="goView('${boardvo.seq}')"><span style="color: red; font-style: italic; padding-left: ${boardvo.depthno * 20}px;">└Re&nbsp;</span>${fn:substring(boardvo.subject, 0, 28)}..<span style="vertical-align: super;">[<span style="color: red; font-style: italic; font-size: 9pt; font-weight: bold;">${boardvo.commentCount}</span>]</span></span>
		                        </c:if>
		                    </c:if>
	                       <%-- 댓글이 있는 경우 끝 --%>
	                       
	                       <%-- 댓글이 없는 경우 시작 --%><!-- 댓글이 없는 경우라면 0을 나타내는 것이 아니라 숫자를 아예 안 보이도록 하겠다. -->
	                       <c:if test="${boardvo.depthno > 0 && boardvo.commentCount == 0}">
	                        <c:if test="${fn:length(boardvo.subject) < 30}">
	                           <span class="subject" onclick="goView('${boardvo.seq}')"><span style="color: red; font-style: italic; padding-left: ${boardvo.depthno * 20}px;">└Re&nbsp;</span>${boardvo.subject}</span>
	                        </c:if>
	                        <c:if test="${fn:length(boardvo.subject) >= 30}">
	                           <span class="subject" onclick="goView('${boardvo.seq}')"><span style="color: red; font-style: italic; padding-left: ${boardvo.depthno * 20}px;">└Re&nbsp;</span>${fn:substring(boardvo.subject, 0, 28)}..</span>
	                        </c:if>
	                       </c:if>
	                       <%-- 댓글이 없는 경우 끝 --%>
	                   <%-- >>>>>>>>> 답변글인 경우 들여쓰기 끝 <<<<<<<<< --%>   
	                   </c:if>
	                   <%-- ======= 첨부파일이 없는 경우 끝 ======= --%> 
	                   
	                   <%-- ======= #158. 첨부파일이 있는 경우 시작 ======= --%>
	      					<c:if test="${not empty boardvo.fileName}">
	      					<%-- >>>>>>>>> #142. 원글인 경우 시작 <<<<<<<<< --%>
		      					<%-- 댓글이 있는 경우 시작 --%>
			      					<c:if test="${boardvo.depthno == 0 && boardvo.commentCount > 0}">
				      					<c:if test="${fn:length(boardvo.subject) < 30}">
				                           <span class="subject" onclick="goView('${boardvo.seq}')">${boardvo.subject}&nbsp;<img src="<%= ctxPath%>/images/disk.gif" /><span style="vertical-align: super;">[<span style="color: red; font-style: italic; font-size: 9pt; font-weight: bold;">${boardvo.commentCount}</span>]</span></span>
				                        </c:if>
				                        <c:if test="${fn:length(boardvo.subject) >= 30}">
				                           <span class="subject" onclick="goView('${boardvo.seq}')">${fn:substring(boardvo.subject, 0, 28)}..&nbsp;<img src="<%= ctxPath%>/images/disk.gif" /><span style="vertical-align: super;">[<span style="color: red; font-style: italic; font-size: 9pt; font-weight: bold;">${boardvo.commentCount}</span>]</span></span>
				                        </c:if>
				                    </c:if>
		                        <%-- 댓글이 있는 경우 끝 --%>
		                        
		                        <%-- 댓글이 없는 경우 시작 --%><!-- 댓글이 없는 경우라면 0을 나타내는 것이 아니라 숫자를 아예 안 보이도록 하겠다. -->
		                        <c:if test="${boardvo.depthno == 0 && boardvo.commentCount == 0}">
			                        <c:if test="${fn:length(boardvo.subject) < 30}">
			                           <span class="subject" onclick="goView('${boardvo.seq}')">${boardvo.subject}&nbsp;<img src="<%= ctxPath%>/images/disk.gif" /></span>
			                        </c:if>
			                        <c:if test="${fn:length(boardvo.subject) >= 30}">
			                           <span class="subject" onclick="goView('${boardvo.seq}')">${fn:substring(boardvo.subject, 0, 28)}..&nbsp;<img src="<%= ctxPath%>/images/disk.gif" /></span>
			                        </c:if>
			                    </c:if>
		                        <%-- 댓글이 없는 경우 끝 --%>
	                       <%-- >>>>>>>>> 원글인 경우 끝 <<<<<<<<< --%>    
	                    
	                    
	                    
	                    <%-- >>>>>>>>> #143. 답변글인 경우 들여쓰기 시작 <<<<<<<<< --%>
		                    <%-- 댓글이 있는 경우 시작 --%>
	      					<c:if test="${boardvo.depthno > 0 && boardvo.commentCount > 0}">
		      					<c:if test="${fn:length(boardvo.subject) < 30}">
		                           <span class="subject" onclick="goView('${boardvo.seq}')"><span style="color: red; font-style: italic; padding-left: ${boardvo.depthno * 20}px;">└Re&nbsp;</span>${boardvo.subject}&nbsp;<img src="<%= ctxPath%>/images/disk.gif" /><span style="vertical-align: super;">[<span style="color: red; font-style: italic; font-size: 9pt; font-weight: bold;">${boardvo.commentCount}</span>]</span></span>
		                        </c:if>
		                        <c:if test="${fn:length(boardvo.subject) >= 30}">
		                           <span class="subject" onclick="goView('${boardvo.seq}')"><span style="color: red; font-style: italic; padding-left: ${boardvo.depthno * 20}px;">└Re&nbsp;</span>${fn:substring(boardvo.subject, 0, 28)}..&nbsp;<img src="<%= ctxPath%>/images/disk.gif" /><span style="vertical-align: super;">[<span style="color: red; font-style: italic; font-size: 9pt; font-weight: bold;">${boardvo.commentCount}</span>]</span></span>
		                        </c:if>
		                    </c:if>
	                       <%-- 댓글이 있는 경우 끝 --%>
	                       
	                       <%-- 댓글이 없는 경우 시작 --%><!-- 댓글이 없는 경우라면 0을 나타내는 것이 아니라 숫자를 아예 안 보이도록 하겠다. -->
	                       <c:if test="${boardvo.depthno > 0 && boardvo.commentCount == 0}">
	                        <c:if test="${fn:length(boardvo.subject) < 30}">
	                           <span class="subject" onclick="goView('${boardvo.seq}')"><span style="color: red; font-style: italic; padding-left: ${boardvo.depthno * 20}px;">└Re&nbsp;</span>${boardvo.subject}&nbsp;<img src="<%= ctxPath%>/images/disk.gif" /></span>
	                        </c:if>
	                        <c:if test="${fn:length(boardvo.subject) >= 30}">
	                           <span class="subject" onclick="goView('${boardvo.seq}')"><span style="color: red; font-style: italic; padding-left: ${boardvo.depthno * 20}px;">└Re&nbsp;</span>${fn:substring(boardvo.subject, 0, 28)}..&nbsp;<img src="<%= ctxPath%>/images/disk.gif" /></span>
	                        </c:if>
	                       </c:if>
	                       <%-- 댓글이 없는 경우 끝 --%>
	                   <%-- >>>>>>>>> 답변글인 경우 들여쓰기 끝 <<<<<<<<< --%>   
	                   </c:if>
	                   <%-- ======= 첨부파일이 있는 경우 끝 ======= --%> 
	                   
                       <%-- === 댓글쓰기 및 답변형 및 파일첨부가 있는 게시판 끝 === --%>	 
      				</td>
      				<td align="center">${boardvo.name}</td>
      				<td align="center">${boardvo.regDate}</td>
      				<td align="center">${boardvo.readCount}</td>
      			</tr>
      		</c:forEach>
      	</c:if>
      	
        <c:if test="${empty requestScope.boardList}">
      		<tr>
      			<td colspan="6">데이터가 없습니다.</td>
      		</tr>
      	</c:if>
      </tbody>
  </table>
</div>



<jsp:include page="../board/GroupWarefooter.jsp" /> 