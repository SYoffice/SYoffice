<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%
	String ctxPath = request.getContextPath();
%>
<jsp:include page="../hr/sidebar.jsp" />

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/hr/employeeRegister.css" />
<script src='<%= request.getContextPath() %>/js/hr/employeeRegister.js'></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>


			<span class="h2"><i class="fa-solid fa-seedling"></i> 신규사원 등록</span>
			
			<div id="container">
				<form name="registerFrm">
					<div id="input_container">
						<div class="input_box">
							<div class="input_text">성명</div> 
							<input type="text" name="name" />
							<div class="error name_error">올바른 성명을 입력하세요</div>
						</div>
						
						<div class="input_box">
							<div class="input_text">생년월일</div> 
							<input type="text" name="birthday" />
							<div class="error">올바른 생년월일을 입력하세요</div>
						</div>
						
						<div class="input_box">
							<div class="input_text">성별</div> 
							<input type='radio' name='gender' value='남' />남성
	  						<input type='radio' name='gender' value='여' />여성
						</div>
						
						<div class="input_box">
							<div class="input_text">이메일</div> 
							<input type="text" id="personal_mail" name="personal_mail" />
							<div class="error">올바른 이메일을 입력하세요</div>
						</div>
						
						<div class="input_box">
							<div class="input_text">사내메일</div> 
							<input type="text" id="mail" name="mail" readonly />
						</div>
						
						<div class="input_box">
							<div class="input_text">전화번호</div> 
							<input type="text" name="tel" />
							<div class="error">올바른 전화번호를 입력하세요</div>
						</div>
						
						<div class="input_box">
							<div class="input_text">우편번호</div>
							<input type="text" name="postcode" id="postcode" size="6" maxlength="5" />&nbsp;&nbsp;
		                       <%-- 우편번호 찾기 --%>
							<button id="find_Zip" type="button">
								<span>우편번호 찾기</span>
							</button>
							<div class="error">올바른 우편번호를 입력하세요</div>
	                	</div>
	                
						<div class="input_box">
							<div class="input_text">주소</div>
							<input type="text" name="address" id="address" size="40" maxlength="200" placeholder="주소" /><br>
							<input type="text" name="detailaddress" id="detailAddress" size="40" maxlength="200" placeholder="상세주소" />&nbsp;<input type="text" name="extraaddress" id="extraAddress" size="40" maxlength="200" placeholder="참고항목" />            
						</div>
						
						<div class="input_box">
							<div class="input_text">지점</div> 
							<select name="branch_no">
								<option value="">지점선택</option>
								<c:forEach items="${requestScope.branchList}" var="barnchVO">
									<option value="${barnchVO.branch_no}">${barnchVO.branch_name}</option>
								</c:forEach>
							</select>
						</div>
						
						<div class="input_box">
							<div class="input_text">부서</div> 
							<select name="dept_id">
								<option value="">부서선택</option>
								<c:forEach items="${requestScope.departmentList}" var="departmentVO">
									<option value="${departmentVO.dept_id}">${departmentVO.dept_name}</option>
								</c:forEach>
							</select>
						</div>
						
						<div class="input_box">
							<div class="input_text">직급</div> 
							<select name="grade_no">
								<option value="">직급선택</option>
								<c:forEach items="${requestScope.gradeList}" var="gradeVO">
									<option value="${gradeVO.grade_no}">${gradeVO.grade_name}</option>
								</c:forEach>
							</select>
						</div>
						
						<button type="button" id="reset">취소하기</button>
						<button type="button" id="employee_register_button" onclick="goRegister()">등록하기</button>
						
					</div>
					
					<div id="profile">
						<div class="input_text">사진첨부</div> 
						<input type="file" name="profile" />
						<div class="error">올바른 전화번호를 입력하세요</div>
					</div>
					
					
				</form>
			</div>
			
		</div>
	</div>
</div>