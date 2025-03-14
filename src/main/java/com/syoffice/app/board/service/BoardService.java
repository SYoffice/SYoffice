package com.syoffice.app.board.service;

import java.util.List;
import java.util.Map;

import com.syoffice.app.board.domain.BoardVO;
import com.syoffice.app.board.domain.CommentVO;
import com.syoffice.app.board.domain.NoticeBoardVO;

public interface BoardService {

	
///////////////////////////////////////// 공지사항 시작 ///////////////////////////////////////////////////
	// 부서번호로 부서명 알아오기
	NoticeBoardVO getDeptName(String fk_dept_id);

	// 공지사항 게시판 글쓰기 insert(파일첨부가 없는)
	int noticeBoardWrite(NoticeBoardVO noticevo);
	
	// 공지사항게시판 마지막 글번호 조회(파일첨부시 필요)
	Integer notice_no();
	
	// 공지사항 게시판 파일첨부 테이블에 insert(파일 첨부가 있는 경우라면) 
	int NoticeWrite_withFile(NoticeBoardVO noticevo);
	
    // 페이징 처리를 안한 검색어가 없는 공지사항 게시판 전체 글목록 보여주기
	List<Map<String, String>> noticeBoardListNoSearch();
	
	// 공지사항 게시판의 총 게시물 건수 (totalCount를 구해야 페이징처리 가능)
	int getNoticeTotalCount(Map<String, String> paraMap);
	
    // 공지사항 글목록 가져오기(페이징 처리 했으며, 검색어가 있는것 또는 검색어가 없는 것 모두 다 포함한 것이다.)
	List<Map<String, String>> noticeBoardListSearch_withPaging(Map<String, String> paraMap);

	// 글 조회수 증가는 없고 단순히 공지사항 게시판의 글 1개만 조회를 해오는 것
	NoticeBoardVO getNoticeBoardView_no_increase_readCount(Map<String, String> paraMap);
	
	// 글 조회수 증가와 함께 공지사항 게시판의 글 1개를 조회를 해오는 것
	NoticeBoardVO getView(Map<String, String> paraMap);
	
	// 공지사항 게시판 글수정하기
	int update_notice_board(NoticeBoardVO noticeboardvo);
	
	// 공지사항 게시글에 첨부파일 또는 사진이 있는지 확인하는 메서드 호출
	List<Map<String, String>> ischeckAttachfile(String notice_no);
	
	// (파일첨부, 사진이미지가 들었는 경우 포함) 공지사항 글 삭제하기
	int noticeBoardDel(String notice_no);

// ------------------------------------ 공지사항 임시저장글 -------------------------------------------------------//	
	
	// 공지사항 테이블에 임시저장상태(status = 2 ) 로 insert 해주는 메소드  
	int noticeTempBoardWrite(NoticeBoardVO noticevo);
	
	// 파일첨부가 있을 경우에 공지사항 테이블에 임시저장상태(status =2 ) 로 insert 해주는 메소드  
	int TempNoticeWrite_withFile(NoticeBoardVO noticevo);
	
    //  공지사항 게시판에 임시저장글 상태(status = 2 )로 저장된 글의 정보를 가져온다. (페이징 처리)
    List<Map<String, String>> noticeTemporaryBoardList(Map<String, Object> paraMap);
	
	// 공지사항 전체 임시저장 글 수 조회
    int getTemporaryBoardCount(String fk_emp_id);

	// 공지사항 임시저장글 조회하기
	NoticeBoardVO getTemporaryNotice(NoticeBoardVO noticevo);
    
	// 공지사항 임시저장글 내용과 status = 2 를 1로 업데이트 해주기
	int updateNoticeTemporary(NoticeBoardVO noticevo);
///////////////////////////////////////// 공지사항 끝 ///////////////////////////////////////////////////	
	
    
///////////////////////////////////////// 부서 게시판 시작 ////////////////////////////////////////////////
	// 부서 게시판 글쓰기 insert(파일첨부가 없는)
	int deptBoardWrite(BoardVO boardvo);
	
	// 부서 게시판 마지막글번호 조회해오기(파일첨부시 필요)
	Integer board_no();
	
	// 부서별 게시판 파일첨부 테이블에 insert(파일 첨부가 있는 경우라면)
	int BoardWrite_withFile(BoardVO boardvo);
	
	// 페이징 처리를 안한 검색어가 없는 공지사항 게시판 또는 부서게시판 전체 글목록 보여주기
	List<Map<String, String>> boardListNoSearch();

	// 총 게시물 건수 (totalCount) 구하기 --> 검색이 있을 때와 검색이 없을때로 나뉜다.
	int getTotalCount(Map<String, String> paraMap);

	// 글목록 가져오기(페이징 처리 했으며, 검색어가 있는것 또는 검색어가 없는 것 모두 다 포함한 것이다.)
	List<Map<String, String>> boardListSearch_withPaging(Map<String, String> paraMap);

	// 글 조회수 증가는 없고 단순히 부서 게시판의 글 1개만 조회를 해오는 것
	BoardVO getBoardView_no_increase_readCount(Map<String, String> paraMap);
	
	// 글 조회수 증가와 함께 부서 게시판의 글 1개를 조회를 해오는 것
	BoardVO getBoardView(Map<String, String> paraMap);

	// 부서 게시판 글수정하기
	int update_board(BoardVO boardvo);

	// 부서 게시글에 첨부파일 또는 사진이 있는지 확인하는 메서드 호출
	List<Map<String, String>> ischeckBoardAttachfile(String board_no);

	// 부서 게시판 글삭제
	int BoardDel(String board_no);

// ------------------------------------ 부서 게시판 임시저장글 -------------------------------------------------------//	
	
	// 파일첨부가 없을 경우에 부서 테이블에 임시저장상태(status =2 ) 로 insert 해주는 메소드 
	int TempBoardWrite(BoardVO boardvo);

	 // 파일첨부가 있을 경우에 부서 파일첨부 테이블에 해당 게시글번호를 가진 게시글에 insert 해주는 메소드
	int TempWrite_withFile(BoardVO boardvo);

	// 부서 게시판 임시저장 글 목록 조회(페이징 처리)
	List<Map<String, String>> TemporaryBoardList(Map<String, Object> paraMap);

	// 부서 게시판 전체 임시저장 글 수 조회
	int getTemporaryCount(String fk_emp_id);
	
	// 부서게시판 임시저장글 조회하기
	BoardVO getTemporaryBoard(BoardVO boardvo);
	
	// 부서 게시판 임시저장글 내용과 status = 2 를 1로 업데이트 해주기
	int updateBoardTemporary(BoardVO boardvo);
// ------------------------------------------- 부서 게시판 댓글 ------------------------------------ //
	
	// comment 테이블에 댓글 insert 해주는 메소드 
	int addBoardComment(Map<String, String> paraMap);

	// 등록된 댓글 조회하기(페이징처리된 댓글목록)
	List<Map<String, Object>> CommentList_withPaging(Map<String, String> paraMap);

	// 페이징처리를 위한 각 게시글의 총 댓글수 알아오기
	int getCmtTotalCount(String fk_board_no);

	// 해당 게시글의 댓글을 삭제하는 메소드
	int cmtDel(CommentVO cmtvo);

	// 웹에서 수정한 댓글 저장하기
	int cmtSave(CommentVO cmtvo);
// ------------------------------------------- 부서 게시판 좋아요 ---------------------------------- // 
	
	// 좋아요 버튼을 최초로 한번 눌렀을 경우 tbl_like 테이블에 insert 해줌과 동시에 하트가 빨간색으로 칠해진다.
	int addLikeCount(String board_no, String fk_emp_id);

	// 좋아요 버튼을 누른 기록이 있을 경우 다시 한번 더 누르면 tbl_like 테이블에서 눌렀던 기록을 delete 해줌과 동시에 하트가 빈 하트로 바뀐다.
	int removeLikeCount(String board_no, String fk_emp_id);

	// 해당 게시글에 사용자가 좋아요를 눌렀는지 여부 알아오기(뷰단에 데이터 저장하여 좋아요 상태 유지 용도)
	boolean isExistLike(String board_no, String fk_emp_id);

	// 해당 게시글의 좋아요 수 알아오기
	int likeCnt(String board_no);
	
	// 내가 좋아요 누른 총 게시물 건수(페이징 처리)
	int getLikeTotalCount(String emp_id);

	// 페이징 처리된 내가 좋아요 누른 게시글 목록 조회하기
	List<BoardVO> goLike_withPaging(Map<String, String> paraMap);












	


}
