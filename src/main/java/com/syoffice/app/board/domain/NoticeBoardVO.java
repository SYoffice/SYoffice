package com.syoffice.app.board.domain;

import org.springframework.web.multipart.MultipartFile;

// === 부서별 게시판 VO 생성하기 === //
// 먼저, 오라클에서 tbl_notice 테이블을 생성해야한다.
public class NoticeBoardVO {

   private String notice_no;     	// 글번호(int)
   private String notice_subject;   // 공지제목
   private String notice_content;   // 공지내용
   private String notice_regdate;   // 공지작성일자
   private String notice_viewcount; // 공지게시글 조회수
   private String fk_emp_id;   		// 사용자ID
   private String notice_status; 	// 글삭제여부(int)   1:사용가능한 글,  0:삭제된글 		   0: 비공개, 1: 공개
   private String name;				// 성명
// private String pw;           	// 글암호(int)
// insert 용(String으로 호환 가능) 	
   
   private String previousseq;      // 이전글번호(int)
   private String previoussubject;  // 이전글제목
   private String nextseq;          // 다음글번호(int)
   private String nextsubject;      // 다음글제목
// select 용(String으로 호환 가능)     
   
   
   /*
      === #147. 파일을 첨부하도록 VO 수정하기@@@
      			먼저, 오라클에서 tbl_board 테이블에 3개 컬럼(fileName, orgFilename, fileSize)을 추가한 다음에 아래의 작업을 한다.
   */
   
    private MultipartFile attach; // jsp 파일의 파일선택 name 과 같게 써야한다.
    /*
     	form 태그에서 type="file" 인 파일을 받아서 저장되는 필드이다. @@@
        진짜파일 ==> WAS(톰캣) 디스크에 저장됨.
        조심할것은 MultipartFile attach 는 오라클 데이터베이스 tbl_board 테이블의 컬럼이 아니다.  
        /myspring/src/main/webapp/WEB-INF/views/mycontent1/board/add.jsp 파일에서 input type="file" 인 name 의 이름(attach)와
        동일해야만 파일첨부가 가능해진다. !!!!!
    */
    private String atnotice_filename; 	//WAS(톰캣)에 저장될 파일명(2025020709291535243254235235234.png)                          (파일첨부 용도)                                 
    private String atnotice_orgfilename; //진짜 파일명(강아지.png)  // 사용자가 파일을 업로드 하거나 파일을 다운로드 할때 사용되어지는 파일명  		(파일첨부 용도)    
    private String atnotice_filesize; 	//파일크기                                                                             (파일첨부 용도) 
    
    
	public NoticeBoardVO() {} // 파라미터가 있는 생성자를 만들면 기본생성자가 사라지므로 직접 만들어주어야한다.
	
	public NoticeBoardVO(String notice_no, String notice_subject, String notice_content, String notice_regdate, String notice_viewcount, String fk_emp_id, String notice_status) { // 파라미터가 있는 생성자를 만든다.
	this.notice_no = notice_no;
	this.notice_subject = notice_subject;
	this.notice_content = notice_content;
	this.notice_regdate = notice_regdate;
	this.notice_viewcount = notice_viewcount;
	this.fk_emp_id = fk_emp_id;
	this.notice_status = notice_status;
}

	public String getNotice_no() {
		return notice_no;
	}

	public void setNotice_no(String notice_no) {
		this.notice_no = notice_no;
	}

	public String getNotice_subject() {
		return notice_subject;
	}

	public void setNotice_subject(String notice_subject) {
		this.notice_subject = notice_subject;
	}

	public String getNotice_content() {
		return notice_content;
	}

	public void setNotice_content(String notice_content) {
		this.notice_content = notice_content;
	}

	public String getNotice_regdate() {
		return notice_regdate;
	}

	public void setNotice_regdate(String notice_regdate) {
		this.notice_regdate = notice_regdate;
	}

	public String getNotice_viewcount() {
		return notice_viewcount;
	}

	public void setNotice_viewcount(String notice_viewcount) {
		this.notice_viewcount = notice_viewcount;
	}

	public String getFk_emp_id() {
		return fk_emp_id;
	}

	public void setFk_emp_id(String fk_emp_id) {
		this.fk_emp_id = fk_emp_id;
	}

	public String getNotice_status() {
		return notice_status;
	}

	public void setNotice_status(String notice_status) {
		this.notice_status = notice_status;
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

	public MultipartFile getAttach() {
		return attach;
	}

	public void setAttach(MultipartFile attach) {
		this.attach = attach;
	}

	public String getAtnotice_filename() {
		return atnotice_filename;
	}

	public void setAtnotice_filename(String atnotice_filename) {
		this.atnotice_filename = atnotice_filename;
	}

	public String getAtnotice_orgfilename() {
		return atnotice_orgfilename;
	}

	public void setAtnotice_orgfilename(String atnotice_orgfilename) {
		this.atnotice_orgfilename = atnotice_orgfilename;
	}

	public String getAtnotice_filesize() {
		return atnotice_filesize;
	}

	public void setAtnotice_filesize(String atnotice_filesize) {
		this.atnotice_filesize = atnotice_filesize;
	}

	



	
}
