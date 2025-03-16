package com.syoffice.app.approval.domain;

public class LeaveformVO {

	private String leave_no;        /* 근태신청서번호시퀀스사용(leave_seq) */
	private String fk_doc_no;       /* 기안문서번호private String 참조테이블(tbl_document) */
	private String leave_subject;   /* 근태신청서 제목 */
	private String leave_startdate; /* 근태신청 시작일 */
	private String leave_enddate;   /* 근태신청 종료일 */
	private String leave_content;   /* 근태신청내용 */
	private String type;			/* 근태 타입 */
	
	
	public String getLeave_no() {
		return leave_no;
	}
	
	public void setLeave_no(String leave_no) {
		this.leave_no = leave_no;
	}
	
	public String getFk_doc_no() {
		return fk_doc_no;
	}
	
	public void setFk_doc_no(String fk_doc_no) {
		this.fk_doc_no = fk_doc_no;
	}
	
	public String getLeave_subject() {
		return leave_subject;
	}
	
	public void setLeave_subject(String leave_subject) {
		this.leave_subject = leave_subject;
	}
	
	public String getLeave_startdate() {
		return leave_startdate;
	}
	
	public void setLeave_startdate(String leave_startdate) {
		this.leave_startdate = leave_startdate;
	}
	
	public String getLeave_enddate() {
		return leave_enddate;
	}
	
	public void setLeave_enddate(String leave_enddate) {
		this.leave_enddate = leave_enddate;
	}
	
	public String getLeave_content() {
		return leave_content;
	}
	
	public void setLeave_content(String leave_content) {
		this.leave_content = leave_content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
