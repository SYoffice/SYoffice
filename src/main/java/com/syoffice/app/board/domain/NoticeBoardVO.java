package com.syoffice.app.board.domain;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// === 공지사항 게시판 VO 생성하기 === //
// 먼저, 오라클에서 tbl_notice 테이블을 생성해야한다.
public class NoticeBoardVO {

   // insert 용(String으로 호환 가능) 	
   private String notice_no;     	// 글번호(int)
   private String notice_subject;   // 공지제목
   private String notice_content;   // 공지내용
   private String notice_regdate;   // 공지작성일자
   private String notice_viewcount; // 공지게시글 조회수
   private String fk_emp_id;   		// 사용자ID
   private String notice_status; 	// 글삭제여부(int)   1:사용가능한 글,  delete:삭제된글 2:임시저장된 글
   private String name;				// 성명

   ///////////////////////////////////////////////////////////////////   
   // select 용(String으로 호환 가능)
   private String previousseq;      // 이전글번호(int)
   private String previoussubject;  // 이전글제목
   private String nextseq;          // 다음글번호(int)
   private String nextsubject;      // 다음글제목
   private String fk_dept_id;		// 부서번호
   private String dept_name;		// 부서이름
   private String profile_img;		// 사원이미지 
   ///////////////////////////////////////////////////////////////////
   
   private MultipartFile attach;    // jsp 파일의 파일선택 name 과 같게 써야한다.
    /*
     	form 태그에서 type="file" 인 파일을 받아서 저장되는 필드이다.
        진짜파일 ==> WAS(톰캣) 디스크에 저장됨.
        조심할것은 MultipartFile attach 는 오라클 데이터베이스 tbl_notice 테이블의 컬럼이 아니다.  
        /myspring/src/main/webapp/WEB-INF/views/board/WriteHome.jsp 파일에서 input type="file" 인 name 의 이름(attach)와
        동일해야만 파일첨부가 가능해진다. !!!!!
    */
    private String atnotice_filename; 	 //WAS(톰캣)에 저장될 파일명(2025020709291535243254235235234.png)                          (파일첨부 용도)                                 
    private String atnotice_orgfilename; //진짜 파일명(강아지.png)  // 사용자가 파일을 업로드 하거나 파일을 다운로드 할때 사용되어지는 파일명  		 (파일첨부 용도)    
    private String atnotice_filesize; 	 //파일크기                                                                             (파일첨부 용도) 
    
    // 파라미터가 있는 생성자를 만들면 기본생성자가 사라지므로 직접 만들어주어야한다.
	public NoticeBoardVO() {} 
	
	// 파라미터가 있는 생성자를 만든다.
	public NoticeBoardVO(String notice_no, String notice_subject, String notice_content, String notice_regdate, String notice_viewcount, String fk_emp_id, String notice_status) {
	this.notice_no = notice_no;
	this.notice_subject = notice_subject;
	this.notice_content = notice_content;
	this.notice_regdate = notice_regdate;
	this.notice_viewcount = notice_viewcount;
	this.fk_emp_id = fk_emp_id;
	this.notice_status = notice_status;
	}
}
