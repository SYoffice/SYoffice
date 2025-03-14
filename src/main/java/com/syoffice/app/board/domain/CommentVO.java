package com.syoffice.app.board.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// === 댓글용 VO 생성하기 === //
// 먼저, 오라클에서 tbl_comment 테이블을 생성한다.
// 또한 BoardVO에 commentCount 컬럼을 추가한다. ===== 
public class CommentVO {
	
	private String comment_no; 	  // 댓글번호
	private String fk_board_no;	  // 게시글번호
	private String fk_emp_id; 	  // 사용자ID
	private String cmt_content;   // 댓글내용
	private String cmt_regdate;   // 작성일자
	private String cmt_status;    // 댓글삭제여부 	1: 게시 	delete: 삭제 (tbl_board와 delete on cascade)
}
