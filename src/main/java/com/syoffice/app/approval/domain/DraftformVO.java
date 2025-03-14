package com.syoffice.app.approval.domain;

public class DraftformVO {

	private String draft_no;      /* 품의서, 시퀀스사용(draft_seq) */
	private String fk_doc_no ;    /* 기안문서번호, 참조테이블(tbl_document) */
	private String draft_subject; /* 품의서 제목 */
	private String draft_content; /* 품의서 내용 */
	
	
	
	public String getDraft_no() {
		return draft_no;
	}
	
	public void setDraft_no(String draft_no) {
		this.draft_no = draft_no;
	}
	
	public String getFk_doc_no() {
		return fk_doc_no;
	}
	
	public void setFk_doc_no(String fk_doc_no) {
		this.fk_doc_no = fk_doc_no;
	}
	
	public String getDraft_subject() {
		return draft_subject;
	}
	
	public void setDraft_subject(String draft_subject) {
		this.draft_subject = draft_subject;
	}
	
	public String getDraft_content() {
		return draft_content;
	}
	
	public void setDraft_content(String draft_content) {
		this.draft_content = draft_content;
	}
}
