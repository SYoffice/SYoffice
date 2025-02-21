package com.syoffice.app.board.domain;

// === #57. 댓글용 VO 생성하기 === //
//먼저, 오라클에서 tbl_comment 테이블을 생성한다.
//또한 tbl_board 테이블에 commentCount 컬럼을 추가한다. ===== 
public class CommentVO {
	
	private String comment_no; 	  // 댓글번호
	private String fk_board_no;	  // 게시글번호
	private String fk_emp_id; 	  //사용자ID
	private String cmt_content;   //댓글내용
	private String cmt_regdate;   //작성일자
	private String cmt_status;    // 글삭제여부 	0: 삭제, 1: 게시
	
	public String getComment_no() {
		return comment_no;
	}
	
	public void setComment_no(String comment_no) {
		this.comment_no = comment_no;
	}
	
	
	public String getFk_board_no() {
		return fk_board_no;
	}

	public void setFk_board_no(String fk_board_no) {
		this.fk_board_no = fk_board_no;
	}

	public String getFk_emp_id() {
		return fk_emp_id;
	}
	
	public void setFk_emp_id(String fk_emp_id) {
		this.fk_emp_id = fk_emp_id;
	}
	
	public String getCmt_content() {
		return cmt_content;
	}
	
	public void setCmt_content(String cmt_content) {
		this.cmt_content = cmt_content;
	}
	
	public String getCmt_regdate() {
		return cmt_regdate;
	}
	
	public void setCmt_regdate(String cmt_regdate) {
		this.cmt_regdate = cmt_regdate;
	}
	
	public String getCmt_status() {
		return cmt_status;
	}
	
	public void setCmt_status(String cmt_status) {
		this.cmt_status = cmt_status;
	}
	
	
}
