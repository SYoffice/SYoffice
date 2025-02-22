<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%
	String ctxPath = request.getContextPath();
%>
<jsp:include page="../hr/sidebar.jsp" />

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/hr/employeeEdit.css" />
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
<script src='<%= request.getContextPath() %>/js/hr/employeeEdit.js'></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
    // 전역 변수로 contextPath 설정
    window.contextPath = "${pageContext.request.contextPath}";
</script>
<c:set var="employee" value="${requestScope.employeevo}" />

	<span class="h2"><i class="bi bi-pencil-square"></i> 사원정보 수정</span>
			
			<div id="container">
				<form name="editFrm" enctype="multipart/form-data">
					
					<div id="img_container">
						<div class="input_box">
							<input type="hidden" id="originalProfileImg" name="originalProfileImg" value="${employee.profile_img}" />
							<div class="input_text">사진첨부</div> 
							<div id="preview">
								<%-- 등록된 프로필 사진이 있을 때 --%>
								<c:if test="${not empty employee.profile_img}">
									<img src="${pageContext.request.contextPath}/resources/profile/${employee.profile_img}"/>
								</c:if>
								<%-- 등록된 프로필 사진이 없을 때 --%>
								<c:if test="${empty employee.profile_img}">
									<img src="${pageContext.request.contextPath}/resources/profile/기본이미지.png"/>
								</c:if>
							</div>
							<input type="file" name="profile_img"/>
						</div>
					</div>
					
					<div id="input_container">
					
						<div class="input_box">
							<div class="input_text">성명</div>
							<input type="hidden" id="emp_id" name="emp_id" value="${employee.emp_id}" /> 
							<input type="text" name="name" value="${employee.name}"/>
							<div class="error name_error">올바른 성명을 입력하세요</div>
						</div>
						
						<div class="input_box">
							<div class="input_text">생년월일</div> 
							<input readonly type="text" name="birthday" value="${employee.birthday}"/>
							<div class="error birthday_error">올바른 생년월일을 입력하세요</div>
						</div>
						
						<div class="input_box radioBox">
							<div class="input_text">성별</div> 
							<input type='radio' name='gender' value='남' <c:if test="${employee.gender == '남'}">checked</c:if>/>
							<span class="gender">남성</span>
	  						<input type='radio' name='gender' value='여' <c:if test="${employee.gender == '여'}">checked</c:if> />
	  						<span class="gender">여성</span>
						</div>
						
						<div class="input_box">
							<div class="input_text">이메일</div> 
							<input type="text" id="personal_mail" name="personal_mail" value="${employee.personal_mail}"/>
							<div class="error personal_mail_error">올바른 이메일을 입력하세요</div>
						</div>
						
						<div class="input_box">
							<div class="input_text">사내메일</div> 
							<div id="mailBox">
								<input type="hidden" id="originalEmail" value="${employee.mail}" />
								<input type="text" id="mailId" name="mailId" value="${fn:substringBefore(employee.mail, '@')}"/>
								<input type="text" id="mailAddr" name="mailAddr" value="@syoffice.syo"/>
							</div>
							<div class="error mail_error">중복된 이메일입니다.</div>
						</div>
						
						<div class="input_box">
							<div class="input_text">전화번호</div> 
							<input type="text" name="tel" value="${employee.tel}" />
							<div class="error tel_error">올바른 전화번호를 입력하세요</div>
						</div>
						
					</div>
						
					<div id="input_container">
					
						<div class="input_box">
							<div class="input_text">우편번호</div>
							<div id="inputBox">
								<input type="text" name="postcode" id="postcode" size="6" maxlength="5" readonly value="${employee.postcode}"/>
								<button id="find_Zip" type="button">
									<span>우편번호 찾기</span>
								</button>
							</div>
	                	</div>
	                
						<div class="input_box">
							<div class="input_text">주소</div>
							<input type="text" name="address" id="address" size="40" maxlength="200" value="${employee.address}" readonly />&nbsp;
							<input type="text" name="detailaddress" id="detailAddress" size="40" maxlength="200" value="${employee.detailaddress}" />&nbsp;
							<input type="text" name="extraaddress" id="extraAddress" size="40" maxlength="200" value="${employee.extraaddress}" readonly />            
						</div>
						
						<div id="select1">
						
							<div class="input_box select">
								<div class="input_text">지점</div> 
								<select name="branch_no">
									<c:forEach items="${requestScope.branchList}" var="barnchVO">
											<option value="${barnchVO.branch_no}" <c:if test="${barnchVO.branch_name == employeevo.branch_name}">selected</c:if>>
												${barnchVO.branch_name}
											</option>
									</c:forEach>
								</select>
							</div>
							
							<div class="input_box select">
								<div class="input_text">부서</div> 
								<select name="dept_id">
									<c:forEach items="${requestScope.departmentList}" var="departmentVO">
										<option value="${departmentVO.dept_id}" <c:if test="${departmentVO.dept_name == employeevo.dept_name}">selected</c:if> >
											${departmentVO.dept_name}
										</option>
									</c:forEach>
								</select>
							</div>
							
						</div>
						
						<div id="select2">
						
							<div class="input_box select">
								<div class="input_text">직급</div> 
								<select name="grade_no">
									<c:forEach items="${requestScope.gradeList}" var="gradeVO">
										<option value="${gradeVO.grade_no}" <c:if test="${gradeVO.grade_name == employeevo.grade_name}">selected</c:if> >
											${gradeVO.grade_name}
										</option>
									</c:forEach>
								</select>
							</div>
							
							<div class="input_box select">
								<div class="input_text">상태</div> 
								<select name="empstatus">
									    <option value="1" ${employeevo.status == 1 ? 'selected' : ''}>재직</option>
									    <option value="2" ${employeevo.status == 2 ? 'selected' : ''}>휴직</option>
									    <option value="3" ${employeevo.status == 3 ? 'selected' : ''}>퇴직</option>
								</select>
							</div>
							
						</div>
						
						<div class="btnBox">
							<button class="resetButton" type="button" id="reset" onclick="history.back()">취소하기</button>
							<button class="EditButton" type="button" id="employee_edit_button" onclick="goEdit()">수정하기</button>
						</div>
						
					</div>
						
						
					
					
				</form>
			</div>
			
			