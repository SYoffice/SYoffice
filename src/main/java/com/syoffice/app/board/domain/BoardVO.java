package com.syoffice.app.board.domain;

import org.springframework.web.multipart.MultipartFile;

// === 부서별 게시판 VO 생성하기 === //
// 먼저, 오라클에서 tbl_board 테이블을 생성해야한다.
public class BoardVO {

   private String board_no;     // 글번호(int)
   private String fk_dept_id;   // 부서번호
   private String fk_emp_id;    // 사용자ID
   private String subject;      // 글제목
   private String content;      // 글내용
   private String view_count;   // 글조회수(int)
   private String board_status; // 글삭제여부(int)   1:사용가능한 글,  0:삭제된글 
   private String board_regDate;// 작성일자
   private String fk_bcate_no;  // 카테고리번호
   private String board_show;	// 공개여부		   0: 비공개, 1: 공개
   private String name;			// 성명
// private String pw;           // 글암호(int)
// insert 용(String으로 호환 가능) 	
   
   private String previousseq;      // 이전글번호(int)
   private String previoussubject;  // 이전글제목
   private String nextseq;          // 다음글번호(int)
   private String nextsubject;      // 다음글제목
// select 용(String으로 호환 가능)     
   
   
   // === #56. 댓글형 게시판을 위한 commentCount 필드 추가하기 === //
   //          먼저 tbl_board 테이블에 commentCount 라는 컬럼이 존재해야 한다.
   private String commentCount;    // 댓글의 개수
   private String likeCount;       // 좋아요 개수
   
   
    private MultipartFile attach; // jsp 파일의 파일선택 name 과 같게 써야한다.
    /*
     	form 태그에서 type="file" 인 파일을 받아서 저장되는 필드이다.
        진짜파일 ==> WAS(톰캣) 디스크에 저장됨.
        조심할것은 MultipartFile attach 는 오라클 데이터베이스 tbl_board 테이블의 컬럼이 아니다.  
        /myspring/src/main/webapp/WEB-INF/views/board/WriteHome.jsp 파일에서 input type="file" 인 name 의 이름(attach)와
        동일해야만 파일첨부가 가능해진다. !!!!!
    */
    private String fileName; 	//WAS(톰캣)에 저장될 파일명(2025020709291535243254235235234.png)                          (파일첨부 용도)                                 
    private String orgFilename; //진짜 파일명(강아지.png)  // 사용자가 파일을 업로드 하거나 파일을 다운로드 할때 사용되어지는 파일명  		(파일첨부 용도)    
    private String fileSize; 	//파일크기                                                                             (파일첨부 용도) 
    
    
    
	public BoardVO() {} // 파라미터가 있는 생성자를 만들면 기본생성자가 사라지므로 직접 만들어주어야한다.
	
	public BoardVO(String board_no, String fk_dept_id, String fk_emp_id, String subject, String content, String view_count, String board_status,
				   String board_regDate, String fk_bcate_no, String board_show) { // 파라미터가 있는 생성자를 만든다.
	this.board_no = board_no;
	this.fk_dept_id = fk_dept_id;
	this.fk_emp_id = fk_emp_id;
	this.subject = subject;
	this.content = content;
	this.view_count = view_count;
	this.board_status = board_status;
	this.board_regDate = board_regDate;
	this.fk_bcate_no = fk_bcate_no;
	this.board_show = board_show;
}

	public String getBoard_no() {
		return board_no;
	}

	public void setBoard_no(String board_no) {
		this.board_no = board_no;
	}

	public String getFk_dept_id() {
		return fk_dept_id;
	}

	public void setFk_dept_id(String fk_dept_id) {
		this.fk_dept_id = fk_dept_id;
	}

	public String getFk_emp_id() {
		return fk_emp_id;
	}

	public void setFk_emp_id(String fk_emp_id) {
		this.fk_emp_id = fk_emp_id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getView_count() {
		return view_count;
	}

	public void setView_count(String view_count) {
		this.view_count = view_count;
	}

	public String getBoard_status() {
		return board_status;
	}

	public void setBoard_status(String board_status) {
		this.board_status = board_status;
	}

	public String getBoard_regDate() {
		return board_regDate;
	}

	public void setBoard_regDate(String board_regDate) {
		this.board_regDate = board_regDate;
	}

	public String getFk_bcate_no() {
		return fk_bcate_no;
	}

	public void setFk_bcate_no(String fk_bcate_no) {
		this.fk_bcate_no = fk_bcate_no;
	}

	public String getBoard_show() {
		return board_show;
	}

	public void setBoard_show(String board_show) {
		this.board_show = board_show;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPreviousseq() {
		return previousseq;
	}

	public void setPreviousseq(String previousseq) {
		this.previousseq = previousseq;
	}

	public String getPrevioussubject() {
		return previoussubject;
	}

	public void setPrevioussubject(String previoussubject) {
		this.previoussubject = previoussubject;
	}

	public String getNextseq() {
		return nextseq;
	}

	public void setNextseq(String nextseq) {
		this.nextseq = nextseq;
	}

	public String getNextsubject() {
		return nextsubject;
	}

	public void setNextsubject(String nextsubject) {
		this.nextsubject = nextsubject;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}

	public String getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(String likeCount) {
		this.likeCount = likeCount;
	}

	public MultipartFile getAttach() {
		return attach;
	}

	public void setAttach(MultipartFile attach) {
		this.attach = attach;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOrgFilename() {
		return orgFilename;
	}

	public void setOrgFilename(String orgFilename) {
		this.orgFilename = orgFilename;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	
	

	
}
