package com.syoffice.app.board.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.syoffice.app.board.domain.BoardVO;
import com.syoffice.app.board.domain.CommentVO;
import com.syoffice.app.board.domain.NoticeBoardVO;
import com.syoffice.app.board.model.BoardDAO;

// service 단 선언하기
@Service
public class BoardService_imple implements BoardService {

	// BoardDAO를 DI 설정하기
	@Autowired
	private BoardDAO mapper_dao;

//////////////////////////////////////////////////////////
	
///////////////////////////////////////// 공지사항 시작 ///////////////////////////////////////////////////		
	
	// 부서번호로 부서명 알아오기
	@Override
	public NoticeBoardVO getDeptName(String fk_dept_id) {
		NoticeBoardVO dept_Name = mapper_dao.getDeptName(fk_dept_id);
		return dept_Name;
	}
	
	
	// 공지사항 게시판 글쓰기 insert(파일첨부가 없는) 
	@Override
	public int noticeBoardWrite(NoticeBoardVO noticevo) {
		int n = mapper_dao.noticeBoardWrite(noticevo);
		return n;
	}
	
	
	// 공지사항게시판 마지막 글번호 조회(파일첨부시 필요)
	@Override
	public Integer notice_no() {
		Integer notice_no = mapper_dao.notice_no();
		return notice_no;
	}
	
	
	// 공지사항 게시판 테이블 및 파일첨부 테이블에 insert(파일 첨부가 있는)
	@Override
	public int NoticeWrite_withFile(NoticeBoardVO noticevo) {
		int n = mapper_dao.NoticeWrite_withFile(noticevo);
		return n;
	}
	
	
    // 페이징 처리를 안한 검색어가 없는 공지사항 게시판 전체 글목록 보여주기
	@Override
	public List<Map<String, String>> noticeBoardListNoSearch() {
		List<Map<String, String>> noticeBoardList = mapper_dao.noticeBoardListNoSearch();
		return noticeBoardList;
	}
	
	
    // 공지사항 게시판의 총 게시물 건수 (totalCount를 구해야 페이징처리 가능)
	@Override
	public int getNoticeTotalCount(Map<String, String> paraMap) {
		int totalCount = mapper_dao.getNoticeTotalCount(paraMap);
		return totalCount;
	}
	
	
    // 공지사항 글목록 가져오기(페이징 처리 했으며, 검색어가 있는것 또는 검색어가 없는 것 모두 다 포함한 것이다.)
	@Override
	public List<Map<String, String>> noticeBoardListSearch_withPaging(Map<String, String> paraMap) {
		List<Map<String, String>> noticeBoardList = mapper_dao.noticeBoardListSearch_withPaging(paraMap);
		return noticeBoardList;
	}
	
	
	// 글 조회수 증가는 없고 단순히 공지사항 게시판의 글 1개만 조회를 해오는 것
	@Override
	public NoticeBoardVO getNoticeBoardView_no_increase_readCount(Map<String, String> paraMap) {
		NoticeBoardVO noticeboardvo = mapper_dao.getView(paraMap); // 공지사항 게시판 글 1개 조회하기
		return noticeboardvo;
	}
	
	
	// 글 조회수 증가와 함께 공지사항 게시판의 글 1개를 조회를 해오는 것
	@Override
	public NoticeBoardVO getView(Map<String, String> paraMap) {
		
		NoticeBoardVO noticeboardvo = mapper_dao.getView(paraMap); // 공지사항 게시판 글 1개 조회하기
		
		String login_userid = paraMap.get("login_userid");
		// paraMap.get("login_userid") 은 로그인을 한 상태이라면 로그인한 사용자의 emp_id 이고,
	    // 로그인을 하지 않은 상태이라면  paraMap.get("login_userid") 은 null 이다.
		// 글 조회수 증가와 함께(update) select 가 주목적
		
		if(login_userid != null &&  // 로그인을 했고
		   noticeboardvo != null && // 글목록이 있고(조회할 글이 있고)
		  !login_userid.equals(noticeboardvo.getFk_emp_id())) { 
		  // 글작성자가 로그인되어져 있는 사용자와 다를 경우(다른 사람의 글을 조회할 경우)
		  // 글조회수 증가는 로그인을 한 상태에서 다른 사람의 글을 읽을때만 증가하도록 한다.
			
			int n = mapper_dao.increase_noticeViewCount(paraMap.get("notice_no")); // 공지사항 게시판 글 조회수 1증가하기
//			System.out.println("조회수 증가 확인 => " + n);
			if(n == 1) {
				noticeboardvo.setNotice_viewcount(( String.valueOf(Integer.parseInt(noticeboardvo.getNotice_viewcount()) + 1)));
			}
		}
		return noticeboardvo;
	}
	
	
	// 공지사항 게시판 글수정하기
	@Override
	public int update_notice_board(NoticeBoardVO noticeboardvo) {
		int n = mapper_dao.update_notice_board(noticeboardvo);
		return n;
	}
	
	
	// 공지사항 게시글에 첨부파일 또는 사진이 있는지 확인하는 메서드 호출
	@Override
	public List<Map<String, String>> ischeckAttachfile(String notice_no) {
		List<Map<String, String>> attachList = mapper_dao.ischeckAttachfile(notice_no);
		return attachList;
	}
	
	
	// (파일첨부, 사진이미지가 들었는 경우 포함) 공지사항 글 삭제하기
	@Override
	public int noticeBoardDel(String notice_no) {
		int n = mapper_dao.noticeBoardDel(notice_no);
		return n;
	}
	
	
	// 공지사항 테이블에 임시저장상태(status = 2 ) 로 insert 해주는 메소드  
	@Override
	public int noticeTempBoardWrite(NoticeBoardVO noticevo) {
		int n = mapper_dao.noticeTempBoardWrite(noticevo);
		return n;
	}
	
	
	// 파일첨부가 있을 경우에 공지사항 테이블에 임시저장상태(status =2 ) 로 insert 해주는 메소드  
	@Override
	public int TempNoticeWrite_withFile(NoticeBoardVO noticevo) {
		int n = mapper_dao.NoticeWrite_withFile(noticevo);
		return n;
	}
	
	
	// 공지사항 게시판에 임시저장글 상태(status = 2 )로 저장된 글의 정보를 가져온다.(페이징처리가 되지 않은 검색어가 없는 모든 임시저장글을 싹다 가져온다.)
    @Override
    public List<Map<String, String>> noticeTemporaryBoardList(Map<String, Object> paraMap) {
        return mapper_dao.noticeTemporaryBoardList(paraMap);
    }
	
	
	// 공지사항 전체 임시저장 글 수 조회
    @Override
    public int getTemporaryBoardCount(String fk_emp_id) {
        return mapper_dao.getTemporaryBoardCount(fk_emp_id);
    }
    
	//공지사항 임시저장글 조회하기
	@Override
	public NoticeBoardVO getTemporaryNotice(NoticeBoardVO noticevo) {
		NoticeBoardVO notice = mapper_dao.getTemporaryNotice(noticevo);
		return notice;
	}
	
	// 공지사항 임시저장글 내용과 status = 2 를 1로 업데이트 해주기
	@Override
	public int updateNoticeTemporary(NoticeBoardVO noticevo) {
		int n = mapper_dao.updateNoticeTemporary(noticevo);
		return n;
	}
///////////////////////////////////////// 공지사항 끝 ///////////////////////////////////////////////////	
   
    
///////////////////////////////////////// 부서게시판 시작 ///////////////////////////////////////////////////	    
    
	
	// 부서 게시판 글쓰기 insert(파일첨부가 없는) 
	@Override
	public int deptBoardWrite(BoardVO boardvo) {
		int n = mapper_dao.deptBoardWrite(boardvo);
		return n;
	}
	
	// 부서 게시판 마지막글번호 조회해오기(파일첨부시 필요)  
	@Override
	public Integer board_no() {
		Integer board_no = mapper_dao.board_no();
		return board_no;
	}

	// 부서별 게시판 파일첨부 테이블에 insert(파일 첨부가 있는 경우라면)
	@Override
	public int BoardWrite_withFile(BoardVO boardvo) {
		int n = mapper_dao.BoardWrite_withFile(boardvo);
		return n;
	}
	
	// 페이징 처리를 안한 검색어가 없는 공지사항 게시판 또는 부서게시판 전체 글목록 보여주기
	@Override
	public List<Map<String, String>> boardListNoSearch() {
		List<Map<String, String>> boardList = mapper_dao.boardListNoSearch();
		return boardList;
	}

	// 총 게시물 건수 (totalCount) 구하기 --> 검색이 있을 때와 검색이 없을때로 나뉜다.
	@Override
	public int getTotalCount(Map<String, String> paraMap) {
		int totalCount = mapper_dao.getTotalCount(paraMap);
		return totalCount;
	}

	// 글목록 가져오기(페이징 처리 했으며, 검색어가 있는것 또는 검색어가 없는 것 모두 다 포함한 것이다.)
	@Override
	public List<Map<String, String>> boardListSearch_withPaging(Map<String, String> paraMap) {
		List<Map<String, String>> boardList = mapper_dao.boardListSearch_withPaging(paraMap);
		return boardList;
	}

	// 글 조회수 증가는 없고 단순히 부서 게시판의 글 1개만 조회를 해오는 것
	@Override
	public BoardVO getBoardView_no_increase_readCount(Map<String, String> paraMap) {
		BoardVO boardvo = mapper_dao.getBoardView(paraMap);
		return boardvo;
	}

	// 글 조회수 증가와 함께 부서 게시판의 글 1개를 조회를 해오는 것
	@Override
	public BoardVO getBoardView(Map<String, String> paraMap) {
		BoardVO boardvo = mapper_dao.getBoardView(paraMap); // 부서 게시판 글 1개 조회하기
		
		String login_userid = paraMap.get("login_userid");
		// paraMap.get("login_userid") 은 로그인을 한 상태이라면 로그인한 사용자의 emp_id 이고,
	    // 로그인을 하지 않은 상태이라면  paraMap.get("login_userid") 은 null 이다.
		// 글 조회수 증가와 함께(update) select 가 주목적
		
		if(login_userid != null &&  // 로그인을 했고
		   boardvo != null && 		// 글목록이 있고(조회할 글이 있고)
		  !login_userid.equals(boardvo.getFk_emp_id())) { 
		  // 글작성자가 로그인되어져 있는 사용자와 다를 경우(다른 사람의 글을 조회할 경우)
		  // 글조회수 증가는 로그인을 한 상태에서 다른 사람의 글을 읽을때만 증가하도록 한다.
			
			int n = mapper_dao.increase_BoardViewCount(paraMap.get("board_no")); // 부서 게시판 글 조회수 1증가하기
//			System.out.println("조회수 증가 확인 => " + n);
			if(n == 1) {
				boardvo.setView_count(( String.valueOf(Integer.parseInt(boardvo.getView_count()) + 1)));
			}
		}
		return boardvo;
	}


	// 부서 게시판 글수정하기
	@Override
	public int update_board(BoardVO boardvo) {
		int n = mapper_dao.update_board(boardvo);
		return n;
	}


	// 부서 게시글에 첨부파일 또는 사진이 있는지 확인하는 메서드 호출
	@Override
	public List<Map<String, String>> ischeckBoardAttachfile(String board_no) {
		List<Map<String, String>> attachList = mapper_dao.ischeckBoardAttachfile(board_no);
		return attachList;
	}

	// 부서 게시판 글삭제
	@Override
	public int BoardDel(String board_no) {
		int n = mapper_dao.BoardDel(board_no);
		return n;
	}


	// 파일첨부가 없을 경우에 부서 테이블에 임시저장상태(status =2 ) 로 insert 해주는 메소드 
	@Override
	public int TempBoardWrite(BoardVO boardvo) {
		int n = mapper_dao.TempBoardWrite(boardvo);
		return n;
	}


	 // 파일첨부가 있을 경우에 부서 파일첨부 테이블에 해당 게시글번호를 가진 게시글에 insert 해주는 메소드
	@Override
	public int TempWrite_withFile(BoardVO boardvo) {
		int n = mapper_dao.BoardWrite_withFile(boardvo);
		return n;
	}


	// 부서 게시판 임시저장 글 목록 조회(페이징 처리)
	@Override
	public List<Map<String, String>> TemporaryBoardList(Map<String, Object> paraMap) {
		List<Map<String, String>> boardTemporaryList = mapper_dao.TemporaryBoardList(paraMap);
		return boardTemporaryList;
	}


	// 부서 게시판 전체 임시저장 글 수 조회
	@Override
	public int getTemporaryCount(String fk_emp_id) {
		int n = mapper_dao.getTemporaryCount(fk_emp_id);
		return n;
	}

	// 부서게시판 임시저장글 조회하기
	@Override
	public BoardVO getTemporaryBoard(BoardVO boardvo) {
		BoardVO board = mapper_dao.getTemporaryBoard(boardvo);
		return board;
	}
	
	// 부서 게시판 임시저장글 내용과 status = 2 를 1로 업데이트 해주기
	@Override
	public int updateBoardTemporary(BoardVO boardvo) {
		int n = mapper_dao.updateBoardTemporary(boardvo);
		return n;
	}
// ------------------------------------------- 부서 게시판 댓글 ------------------------------------ //	
	// comment 테이블에 댓글 insert 해주는 메소드 
	@Override
	public int addBoardComment(Map<String, String> paraMap) {
		int n = mapper_dao.addBoardComment(paraMap);
		return n;
	}


	// 등록된 댓글 조회하기(페이징처리된 댓글목록)
	@Override
	public List<Map<String, Object>> CommentList_withPaging(Map<String, String> paraMap) {
		List<Map<String, Object>> cmtList = mapper_dao.CommentList_withPaging(paraMap);
		return cmtList;
	}

	// 페이징처리를 위한 각 게시글의 총 댓글수 알아오기
	@Override
	public int getCmtTotalCount(String fk_board_no) {
		int totalCount = mapper_dao.getCmtTotalCount(fk_board_no);
		return totalCount;
	}


	// 해당 게시글의 댓글을 삭제하는 메소드
	@Override
	public int cmtDel(CommentVO cmtvo) {
		int n = mapper_dao.cmtDel(cmtvo);
		return n;
	}


	// 웹에서 수정한 댓글 저장하기
	@Override
	public int cmtSave(CommentVO cmtvo) {
		int n = mapper_dao.cmtSave(cmtvo);
		return n;
	}

// ------------------------------------------- 부서 게시판 좋아요 ---------------------------------- // 	

	// 좋아요 버튼을 최초로 한 번 눌렀을 경우 tbl_like 테이블에 insert 해줌과 동시에 하트가 빨간색으로 칠해진다.
	@Override
	public int addLikeCount(String board_no, String fk_emp_id) {
		
		int m = 0;
		
		int n = mapper_dao.isExistLike(board_no,fk_emp_id); 
		// 해당 게시글에 loginuser 가 좋아요를 눌렀는지 안 눌렀는지 조회해오기
		// 하트를 눌렀을 경우 n==1 , 하트를 누르지 않았을 경우 n==0 이 나온다. 
		
		if(n==0) { // 하트를 최초로 클릭했을 경우(0이 나왔을 경우)에만 tbl_like 테이블에 insert 해준다.
			m = mapper_dao.addlike(board_no, fk_emp_id);
		}
		return m;
	}


	// 좋아요 버튼을 누른 기록이 있을 경우 다시 한번 더 누르면 tbl_like 테이블에서 눌렀던 기록을 delete 해줌과 동시에 하트가 빈 하트로 바뀐다.
	@Override
	public int removeLikeCount(String board_no, String fk_emp_id) {
		
		int m = 0;
		
		int n = mapper_dao.isExistLike(board_no,fk_emp_id); 
		// 해당 게시글에 loginuser 가 좋아요를 눌렀는지 안 눌렀는지 조회해오기
		// 하트를 눌렀을 경우 n==1 , 하트를 누르지 않았을 경우 n==0 이 나온다. 
		
		if(n==1) { // 하트를 눌렀던 상태에서 한 번더 눌렀을 경우(1이 나왔을 경우)에만 tbl_like 테이블에 delete 해준다.
			m = mapper_dao.removeLike(board_no, fk_emp_id);
		}
		return m;
	}


	// 해당 게시글에 사용자가 좋아요를 눌렀는지 여부 알아오기(뷰단에 데이터 저장하여 좋아요 상태 유지 용도)
	@Override
	public boolean isExistLike(String board_no, String fk_emp_id) {
		int cnt = mapper_dao.isExistLike(board_no,fk_emp_id);
		
		if(cnt > 0) {
			return true; // cnt 가 0보다 크다면 true 작다면 false
		}
		else {
			return false; // cnt 가 0보다 크다면 true 작다면 false
		}
	}


	// 해당 게시글의 좋아요 수 알아오기 시작
	@Override
	public int likeCnt(String board_no) {
		int n = mapper_dao.likeCnt(board_no);
		return n;
	}

	// 내가 좋아요 누른 총 게시물 건수(페이징 처리)
	@Override
	public int getLikeTotalCount(String emp_id) {
		int totalCount = mapper_dao.getLikeTotalCount(emp_id);
		return totalCount;
	}

	// 페이징 처리된 내가 좋아요 누른 게시글 목록 조회하기
	@Override
	public List<BoardVO> goLike_withPaging(Map<String, String> paraMap) {
		List<BoardVO> boardList = mapper_dao.goLike_withPaging(paraMap);
		return boardList;
	}

	
}
