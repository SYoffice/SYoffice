package com.syoffice.app.reservation.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReservationVO { // 자원예약

	private String reserv_no; /* 자원예약번호, 시퀀스사용(reserv_seq) */
	private String reserv_start; /* 자원예약 시작시간 */
	private String reserv_end; /* 자원예약 종료시간 */
	private String reserv_return; /* 자원 반납시간 */
	private String fk_resource_no; /* 자원번호, 참조테이블(tbl_resource) */
	private String fk_emp_id; /* 사원번호, 참조테이블(tbl_employee) */

	// 현재 취소 가능 여부 (false = 반납)
	private boolean posibleCancel;
	// 예약 종료 시간이 지났는지 여부
	private boolean isPassed;
	private String empl_name;
	private String resource_name;

	public String getReserv_no() {
		return reserv_no;
	}

	public void setReserv_no(String reserv_no) {
		this.reserv_no = reserv_no;
	}

	public String getReserv_start() {
		return reserv_start;
	}

	public void setReserv_start(String reserv_start) {
		this.reserv_start = reserv_start;
	}

	public String getReserv_end() {
		return reserv_end;
	}

	public void setReserv_end(String reserv_end) {
		this.reserv_end = reserv_end;
	}

	public String getReserv_return() {
		return reserv_return;
	}

	public void setReserv_return(String reserv_return) {
		this.reserv_return = reserv_return;
	}

	public String getFk_resource_no() {
		return fk_resource_no;
	}

	public void setFk_resource_no(String fk_resource_no) {
		this.fk_resource_no = fk_resource_no;
	}

	public String getFk_emp_id() {
		return fk_emp_id;
	}

	public void setFk_emp_id(String fk_emp_id) {
		this.fk_emp_id = fk_emp_id;
	}

	public String getEmpl_name() {
		return empl_name;
	}

	public void setEmpl_name(String name) {
		this.empl_name = name;
	}

	public String getResource_name() {
		return resource_name;
	}

	public void setResource_name(String resource_name) {
		this.resource_name = resource_name;
	}

	public boolean getPosibleCancel() {
		return posibleCancel;
	}

	public void setPosibleCancel() {
		this.posibleCancel = this.isEventOngoing();
	}

	public boolean getIsPassed() {
		return isPassed;
	}

	public void setPassed() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime eventEnd = LocalDateTime.parse(this.reserv_end, formatter);
		
		this.isPassed = currentTime.isAfter(eventEnd);
	}

	private boolean isEventOngoing() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime eventStart = LocalDateTime.parse(this.reserv_start, formatter);
		LocalDateTime eventEnd = LocalDateTime.parse(this.reserv_end, formatter);

		return !currentTime.isBefore(eventStart) && !currentTime.isAfter(eventEnd);
	}

}
