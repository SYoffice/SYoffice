	<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    
<jsp:include page="./sidebar.jsp" />

<%-- custom CSS --%>
<link href='<%= request.getContextPath() %>/css/kpi/kpi_register.css'rel='stylesheet' />

<%-- custom JS --%>
<script src='<%= request.getContextPath() %>/js/kpi/kpi_register.js'></script>

    <div class="contents_wrapper">
        <div class="contents_inner_wrapper">
        	<div id="formBox">
        	<div style="margin: 4% 0 4% 0">
		    	<span class="h3">목표 등록</span>
	   		</div>
	   		
				<form name="kpiFrm">
					<table id="kpi" class="table table-bordered">
						<tr>
							<th>소속</th>
							<td colspan="3">
								본사-인사부
							</td>
							
						</tr>
						<tr>
							<th>연도</th>
							<td>
								<select name="kpi_year">
									<option value="">선택하세요</option>
								</select>
							</td>
							<th>분기</th>
							<td>
								<select name="kpi_quarter">
									<option value="">선택하세요</option>
									<option value="1">1분기</option>
									<option value="2">2분기</option>
									<option value="3">3분기</option>
									<option value="4">4분기</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>목표실적액</th>
							<td colspan="3">
								<input type="text" size="50" id="inputKpi" />
								<input type="hidden" name="kpi_index" />
							</td>
						</tr>
					</table>
					<input type="hidden" value="${sessionScope.loginuser.fk_dept_id}" name="fk_dept_id"/>
				</form>
				<div style="float: right;">
					<%-- <button type="button" id="register" class="border-1 rounded-md" style="margin-right: 10px;">등록</button>--%>
					<button type="button" class="buttonBorder" id="register" style="margin-right: 10px; background-color: #99ccff;">등록</button>
					<button type="button" class="buttonBorder" style="background-color: ecf0f8;" onclick="javascript:location.href='<%= request.getContextPath()%>/schedule/scheduleIndex'">취소</button> 
				</div>
			</div>
		</div>
	</div>
</div>



<%-- JS 활용 용도 --%>
<input type="hidden" id="path" 	value="${pageContext.request.contextPath}" />