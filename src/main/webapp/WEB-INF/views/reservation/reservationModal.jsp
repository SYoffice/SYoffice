<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<link href="${pageContext.request.contextPath}/css/reservation/reservation_modal.css" rel="stylesheet" type="text/css" />

<div id="myModal" class="modal">
	<div class="modal-content">
		<form id="reservationForm">
			<input type="hidden" id="id" name="id" />
			<input type="hidden" id="group" name="group" required />
			<input type="hidden" id="empl_name" name="empl_name" required />

			<div class="form-group">
				<label for="resource_name">
				<c:if test="${category_no eq '1'}">
				    회의실
				</c:if>
				<c:if test="${category_no eq '2'}">
				    차량
				</c:if>
					명
				</label>
				<input disabled type="text" id="resource_name" name="resource_name" required />
			</div>
			<div class="form-group">
				<!-- 예약 시작 -->
				<label for="start-time">예약 시작 <span class="input-group-addon" id="start-date"></span></label>
				<!-- 날짜 표시 -->
				<input type="time" class="form-control" id="start-time" placeholder="시작 시간" required />
			</div>
			<div class="form-group">
				<!-- 예약 종료 -->
				<label for="end-time">예약 종료 <span class="input-group-addon" id="end-date"></span></label>
				<!-- 날짜 표시 -->
				<input type="time" class="form-control" id="end-time" placeholder="종료 시간" required />
			</div>
			<div class="form-group">
				<button type="submit">예약하기</button>
			</div>
		</form>
	</div>
</div>