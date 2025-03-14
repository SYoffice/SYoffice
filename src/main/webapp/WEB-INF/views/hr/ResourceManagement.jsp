<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
   
<%
	String ctxPath = request.getContextPath();
%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/hr/ResourceManagement.css" />
<jsp:include page="../hr/sidebar.jsp" />
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.1/font/bootstrap-icons.css">
<script type="text/javascript">
$(document).ready(function() {
	
	// 엔터 누를 시 검색
	$("input:text[name='searchWord']").on("keydown", e => {
		if (e.keyCode == 13) {
			goSearch();
		}
	});// end of $("input:text[name='searchWord']").on("keydown", e => {}) -----	
	   
	//검색 시 검색조건 및 검색어 값 유지시키기
	if (${!empty requestScope.paraMap.searchType && !empty requestScope.paraMap.searchWord}) {
		$("select[name='searchType']").val("${requestScope.paraMap.searchType}");
		$("input:text[name='searchWord']").val("${requestScope.paraMap.searchWord}");
	}
	
	// 모달 겹치지 않게 설정
	$("#closeRegisterResourceModal, #cancelRegisterResourceModal").click(function() {
	       $("#registerResourceModal").hide();
	       $("#editResourceModal").hide();
	   });
	
	   // 모달 외부 클릭 시 닫기
	   $(window).click(function(event) {
	       if ($(event.target).is("#registerResourceModal")) {
	           $("#registerResourceModal").hide();
	           $("#editResourceModal").hide();
	       }
	});

	// 자원 추가 버튼 클릭 이벤트
	$("#registerBtn").click(function(event) { 
		const resource_name = $("#resource_name").val().trim();
		const fk_category_no = $("#fk_category_no").val();

		if (resource_name === "") {
			alert("자원명을 입력해주세요.");
			return;
		}

		if (!fk_category_no || fk_category_no.trim() === "") {
			alert("자원 분류를 선택하세요.");
			return;
		}

		// 자원명 중복 검사
		$.ajax({
			url: "${pageContext.request.contextPath}/hr/checkResourceName",
			type: "GET",
			data: { resource_name: resource_name, fk_category_no: fk_category_no },
			success: function(response) {
				if (response.status === "1") {
					alert("이미 존재하는 자원명입니다. 다른 이름을 입력해주세요.");
					return;
				}

				// 중복이 없을 경우 신규 자원등록
				$.ajax({
					url: "${pageContext.request.contextPath}/hr/registerResource", 
					type: "POST",
					data: {
						resource_name: resource_name,
						fk_category_no: fk_category_no
					},
					success: function(response) {
						if (response.status === "1") { 
							alert("자원이 성공적으로 추가되었습니다.");
							$("#modal_background").hide();
                          	$("#register_resource_modal").hide();
                           	location.reload();
                       	} 
						else {
							alert(response.message);
						}
					},
					error: function(request, status, error) {
						alert("code: " + request.status + "\nmessage: " + request.responseText + "\nerror: " + error);
					}
				}); // end of 자원 추가 ajax() -----
			},
			error: function(request, status, error) {
				alert("중복 검사에 실패했습니다. 다시 시도해주세요.");
			}
		}); // end of 중복 검사 ajax()-----
	});// end of $("#registerBtn").click(function(event) ----

	
	
	// 자원 수정 버튼 클릭 이벤트
	$("#updateBtn").click(function () {
		const resource_no = $("#editResourceNo").val().trim();
		const resource_name = $("#editResourceName").val().trim();
		const resource_status = $("#editResourceStatus").val();
		const category_name = $("#editResourceCategoryName").val().trim();
		const fk_category_no = $("#fk_category_no").val().trim();
		
		if (resource_name === "") {
			alert("자원명을 입력해주세요.");
			return;
		}

		// 중복 검사
		$.ajax({
			url: "${pageContext.request.contextPath}/hr/checkResourceName",
			type: "GET",
			data: {
				resource_name: resource_name,
				fk_category_no: fk_category_no
			},
			success: function(response) {
				if (response.status === "1" && resource_name !== $("#editResourceName").attr("data-original-name")) {
					alert("이미 존재하는 자원명입니다. 다른 이름을 입력해주세요.");
					return;
				}

				// 중복이 없을 경우 수정 진행
				$.ajax({
					url: "${pageContext.request.contextPath}/hr/updateResource",
					type: "POST",
					data: {
						resource_no: resource_no,
						resource_name: resource_name,
						resource_status: resource_status
					},
					success: function (response) {
						if (response.status === "1") {
							alert("자원이 성공적으로 수정되었습니다.");
                          	$("#modalBackground").hide();
                           	$("#editResourceModal").hide();
                           	location.reload();
						} 
						else {
							alert(response.message);
						}
					},
					error: function () {
						alert("자원 수정에 실패했습니다. 다시 시도해주세요.");
					}
				}); // end of 자원 수정 ajax() -----
			},
			error: function() {
				alert("중복 검사에 실패했습니다. 다시 시도해주세요.");
			}
		}); // end of 중복 검사 ajax() -----
	});// end of $("#updateBtn").click(function () -----
	
});// end of $(document).ready(function() {}) ------

function goSearch() {
	const frm = document.searchFrm;
	
	frm.method = "GET";
	frm.submit();
}// end of function goSearch() ----------------------

// 자원 추가 모달 열기
function registerResource() {
    $("#modalBackground").show();
    $("#registerResourceModal").show();
}// end of function registerDept() -----

// 모달창 닫는 함수
function closeModal() {
    $("#modalBackground").hide(); 
    $("#registerResourceModal").hide(); // 자원 추가 모달 닫기
    $("#editResourceModal").hide();    // 자원 수정 모달 닫기
}// end of function closeModal() -----

// 자원 수정 모달 열기
function editResource(resource_no, resource_name, resource_status, category_name, fk_category_no) {
	$("#fk_category_no").val(fk_category_no);
    $("#editResourceNo").val(resource_no);
    $("#editResourceName").val(resource_name).attr("data-original-name", resource_name);
    $("#editResourceStatus").val(resource_status);
    $("#editResourceCategoryName").val(category_name);
    
	$("#modalBackground").show();
    $("#editResourceModal").show();
}// end of function editResource(resource_no, resource_name, resource_status, category_name, fk_category_no) -----

//자원 삭제 버튼 클릭 이벤트
function deleteResource(resource_no) {
    if (!confirm("정말 해당 자원을 삭제하시겠습니까?")) {
        return;
    }

    $.ajax({
        url: "${pageContext.request.contextPath}/hr/deleteResource",
        type: "POST",
        data: { resource_no: resource_no },
        success: function(response) {
            if (response.status === "1") {
                alert("자원이 성공적으로 삭제되었습니다.");
                location.reload();
            } else {
                alert(response.message);
            }
        },
        error: function() {
            alert("자원 삭제에 실패했습니다. 다시 시도해주세요.");
        }
    });// end of ajax() -----
}// end of function deleteResource(resource_no) -----
</script>

	<span class="h2"><i class="bi-archive"></i> 자원관리</span>
	
	<form name="searchFrm">
		<div id="formContainer">
			<select name="searchType" class="form-select">
				<option value="category_name">자원분류</option>
				<option value="resource_name">자원명</option>
			</select>
			
			<div class="search-container">
				<input type="text" name="searchWord" size="50" autocomplete="off" placeholder="Search..." /> 
				<button type="button" class="btn btn-secondary btn-sm search-btn" onclick="goSearch()"><i class="fas fa-search"></i></button>
			</div>
			
			<button type="button" id="registerResourceBtn" onclick="registerResource()">신규 자원등록</button>
		</div>			
	</form>
	
	<table id="table" class="table table-bordered table-hover">
		<thead>
			<tr style="background-color: #e6eeff;">
	 			<th>자원분류</th>
	            <th>자원명</th>
	            <th>자원상태</th>
	            <th style="width: 18%;">관리</th>
			</tr>
		</thead>
		
		<tbody>
			<c:forEach items="${requestScope.resourceList}" var="resource">
				<tr id="main" >
					<td>${resource.category_name}</td>
					<td>${resource.resource_name}</td>
					<c:if test="${resource.resource_status eq 0}">
						<td style="color: red;">사용불가</td>
					</c:if>
					<c:if test="${resource.resource_status eq 1}">
						<td style="color: green;">사용가능</td>
					</c:if>
					<td>
						<div class="btn-container">
					        <c:choose>
			                    <c:when test="${requestScope.reservationCountMap[resource.resource_no] > 0}">
			                        <span style="color: orange; font-weight: bold;">예약중</span>
			                    </c:when>
			
			                    <c:otherwise>
			                        <button type="button" class="btn btn-sm btn-primary" id="editBtn" onclick="editResource('${resource.resource_no}', '${resource.resource_name}', '${resource.resource_status}', '${resource.category_name}', '${resource.fk_category_no}')">
			                            수정
			                        </button>
			                        <button type="button" class="btn btn-sm btn-danger" id="deleteBtn" onclick="deleteResource('${resource.resource_no}')">
			                            삭제
			                        </button>
			                    </c:otherwise>
			                </c:choose>
					    </div>
		            </td>
				</tr>
			</c:forEach>
		</tbody>
    </table>
    
    <%-- 페이지네이션  --%>
	<%-- li 태그 안의 a 태그의 주소값만 수정해주시면 됩니다. --%>
	<nav class="text-center">
		<ul class="pagination">
			<!-- 첫 페이지  -->
			<div class="pageBtn_box">
				<li><a href="ResourceManagement?searchType=${requestScope.paraMap.searchType}&searchWord=${requestScope.paraMap.searchWord}&curPage=1" data-page="1"><span aria-hidden="true"><img class="pageBtn" src="${pageContext.request.contextPath}/images/icon/go_first.svg" /></span></a></li>
				<!-- 이전 페이지 -->
				<c:if test="${pagingDTO.firstPage ne 1}">
					<li><a href="ResourceManagement?searchType=${requestScope.paraMap.searchType}&searchWord=${requestScope.paraMap.searchWord}&curPage=${i}?curPage=${pagingDTO.firstPage-1}" data-page="${pagingDTO.firstPage-1}"><span aria-hidden="true"><img class="pageBtn" src="${pageContext.request.contextPath}/images/icon/prev.svg" /></span></a></li>
					
				</c:if>
			</div>
			
			<div id="pageNo_box">
				<!-- 페이지 넘버링  -->
				<c:forEach begin="${pagingDTO.firstPage}" end="${pagingDTO.lastPage}" var="i" >
					
					<c:if test="${pagingDTO.curPage ne i}">
						<li><a class="pageNo"  href="ResourceManagement?searchType=${requestScope.paraMap.searchType}&searchWord=${requestScope.paraMap.searchWord}&curPage=${i}" data-page="${i}">${i}</a></li>
					</c:if>
					
					<c:if test="${pagingDTO.curPage eq i}">
						<li class="active"><a class="pageNo active" href="#">${i}</a></li>
					</c:if>
					
				</c:forEach>
			</div>
			
			<!-- 다음  페이지  -->
			<div class="pageBtn_box">
				<c:if test="${pagingDTO.lastPage ne pagingDTO.totalPageCount}">
					<li><a href="ResourceManagement?searchType=${requestScope.paraMap.searchType}&searchWord=${requestScope.paraMap.searchWord}&curPage=${pagingDTO.lastPage+1}" data-page="${pagingDTO.lastPage+1}"><span aria-hidden="true"><img class="pageBtn" src="${pageContext.request.contextPath}/images/icon/next.svg" /></span></a></li>
				</c:if>
				
				<!-- 마지막 페이지 -->
				<li><a href="ResourceManagement?searchType=${requestScope.paraMap.searchType}&searchWord=${requestScope.paraMap.searchWord}&curPage=${pagingDTO.totalPageCount}" data-page="${pagingDTO.totalPageCount}"><span aria-hidden="true"><img class="pageBtn" src="${pageContext.request.contextPath}/images/icon/go_last.svg" /></span></a></li>
			</div>
		</ul>
	</nav>
	<%-- 페이지네이션 --%>
    
    
     <%--모달 뒤의 배경 --%>
	<div id="modalBackground"  onclick="closeModal()"></div>
    
    <%-- 자원 추가 모달창 --%>
	<div id="registerResourceModal">
	    <div class="modal-content">
	        <h2>신규 자원 추가</h2>
	
	        <form id="registerResourceForm">
	        
	        	<%-- 자원분류 선택 --%>
				<div class="form-group">
	                <label for="fk_category_no">자원분류</label>
	                <select id="fk_category_no" name="fk_category_no">
						<option value="1">회의실</option>
						<option value="2">차량</option>
	                </select>	
	            </div>	        	
	        	
	            <%-- 자원명 입력 --%>
				<div class="form-group">
				    <label for="resource_name">자원명</label>
				    <input type="text" id="resource_name" name="resource_name" placeholder="자원명을 입력하세요" required>
				    <span id="check_message" style="margin-top: 15px;"></span>
				</div>
	
	            <div class="btn-container">
	                <button type="button" id="cancelRegisterResourceModal" class="btn btn-secondary" onclick="closeModal()">취소</button>
	                <button type="button" class="btn btn-success" id="registerBtn">등록</button>
	            </div>
	        </form>
	    </div>
	</div>
    
    
    <%-- 자원 수정 모달창 --%>
	<div id="editResourceModal" style="display:none;">
	    <div class="modal-content">
	        <h2 style="font-weight: bold;">자원 수정</h2>
	
	        <form id="editResourceForm">
	            <%-- 자원번호 (숨김 처리) --%>
	            <input type="hidden" id="editResourceNo" name="resource_no">
	            <%-- 분류번호 --%>
				<input type="hidden" id="fk_category_no" name="fk_category_no">
				
				<%-- 자원분류 (보여주기만) --%>
				<div class="form-group">
				    <label for="editResourceCategoryNo">자원분류</label>
				    <input type="text" id="editResourceCategoryName" name="resource_category_name" readonly>
				</div>
				
	            <%-- 자원명 수정 --%>
	            <div class="form-group">
	                <label for="editResourceName">자원명</label>
	                <input type="text" id="editResourceName" name="resource_name" placeholder="자원명을 입력하세요" required>
	            </div>
	
	            <%-- 자원 상태 변경 --%>
	            <div class="form-group">
	                <label for="editResourceStatus">자원 상태</label>
	                <select id="editResourceStatus" name="resource_status">
	                    <option value="1">사용가능</option>
	                    <option value="0">사용불가</option>
	                </select>
	            </div>
	
	            <div class="btn-container">
	                <button type="button" id="cancelEditResourceModal" class="btn btn-secondary" onclick="closeModal()">취소</button>
	                <button type="button" class="btn btn-primary" id="updateBtn">수정</button>
	            </div>
	        </form>
	    </div>
	</div>
	    