package com.syoffice.app.board.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.syoffice.app.board.domain.BoardVO;
import com.syoffice.app.board.domain.NoticeBoardVO;
import com.syoffice.app.board.model.BoardDAO;

@Service
public class BoardService_imple implements BoardService {

	@Autowired
	private BoardDAO mapper_dao;
	
	// === 부서 게시판 글쓰기 insert(파일첨부가 없는) === //
	@Override
	public int deptBoardWrite(BoardVO boardvo) {
		int n = mapper_dao.deptBoardWrite(boardvo);
		return n;
	}
	
	// === 부서 게시판 마지막글번호 조회해오기(파일첨부시 필요) === //
	@Override
	public List<Integer> board_no() {
		List<Integer> board_no = mapper_dao.board_no();
		return board_no;
	}

	// === 부서별 게시판 파일첨부 테이블에 insert(파일 첨부가 있는 경우라면) === /
	@Override
	public int BoardWrite_withFile(BoardVO boardvo) {
		int n = mapper_dao.BoardWrite_withFile(boardvo);
		return n;
	}
	
	// === 공지사항 게시판 글쓰기 insert(파일첨부가 없는) === //
	@Override
	public int noticeBoardWrite(NoticeBoardVO noticevo) {
		int n = mapper_dao.noticeBoardWrite(noticevo);
		return n;
	}
	
	// === 공지사항게시판 마지막 글번호 조회(파일첨부시 필요) === //
	@Override
	public List<Integer> notice_no() {
		List<Integer> notice_no = mapper_dao.notice_no();
		return notice_no;
	}

	// === 공지사항 게시판 파일첨부 테이블에 insert(파일 첨부가 있는 경우라면) === //
	@Override
	public int NoticeWrite_withFile(NoticeBoardVO noticevo) {
		int n = mapper_dao.NoticeWrite_withFile(noticevo);
		return n;
	}
	
	
	// === 페이징 처리를 안한 검색어가 없는 공지사항 게시판 또는 부서게시판 전체 글목록 보여주기 === //
	@Override
	public List<Map<String, String>> boardListNoSearch(String boardLocation) {
		List<Map<String, String>> boardList = mapper_dao.boardListNoSearch(boardLocation);
		return boardList;
	}

	// === 총 게시물 건수 (totalCount) 구하기 --> 검색이 있을 때와 검색이 없을때로 나뉜다. === //
	@Override
	public int getTotalCount(Map<String, String> paraMap) {
		int totalCount = mapper_dao.getTotalCount(paraMap);
		return totalCount;
	}

	// === 글목록 가져오기(페이징 처리 했으며, 검색어가 있는것 또는 검색어가 없는 것 모두 다 포함한 것이다.) === //
	@Override
	public List<Map<String, String>> boardListSearch_withPaging(Map<String, String> paraMap) {
		List<Map<String, String>> boardList = mapper_dao.boardListSearch_withPaging(paraMap);
		return boardList;
	}

	// === 글 조회수 증가는 없고 단순히 공지사항 게시판의 글 1개만 조회를 해오는 것 === //
	@Override
	public NoticeBoardVO getNoticeBoardView_no_increase_readCount(Map<String, String> paraMap) {
		NoticeBoardVO noticeboardList = mapper_dao.getView(paraMap);
		return noticeboardList;
	}

	// 파일첨부가 있는 댓글 및 답변글쓰기 게시판에서 글 1개 조회하기
	@Override
	public NoticeBoardVO getView(Map<String, String> paraMap) {
		
		NoticeBoardVO noticeboardvo = mapper_dao.getView(paraMap); // 글 1개 조회하기
		
		String login_userid = paraMap.get("login_userid");
		// paraMap.get("login_userid") 은 로그인을 한 상태이라면 로그인한 사용자의 userid 이고,
	    // 로그인을 하지 않은 상태이라면  paraMap.get("login_userid") 은 null 이다.
		// 글 조회수 증가와 함께(update) select 가 주목적
		
		/*if(login_userid != null && 
		   noticeboardvo != null && 
		  !login_userid.equals(noticeboardvo.getFk_emp_id())) { // 글조회수 증가는 로그인을 한 상태에서 다른 사람의 글을 읽을때만 증가하도록 한다.
			
		//	int n = mapper_dao.increase_noticeViewCount(paraMap.get("notice_no")); // 공지사항 게시판 글 조회수 1증가하기
			
		//	if(n == 1) {
		//		noticeboardvo.setNotice_viewcount(( String.valueOf(Integer.parseInt(noticeboardvo.getNotice_viewcount()) + 1)));
		//	}
		}*/
		return noticeboardvo;
	}

	// 공지사항 게시판 글수정하기
	@Override
	public int update_notice_board(NoticeBoardVO noticeboardvo) {
		int n = mapper_dao.update_notice_board(noticeboardvo);
		return n;
	}

	// 파일첨부가 있는 글 삭제
	@Override
	public Map<String, String> noticeBoardView_delete(String notice_no) { 
		Map<String, String> noticeboardmap = mapper_dao.noticeBoardView_delete(notice_no);
		return noticeboardmap;
	}

	// 파일첨부, 사진이미지가 들었는 경우의 글 삭제하기
	@Override
	public int noticeBoarDel(String notice_no) {
		int n = mapper_dao.noticeBoarDel(notice_no);
		return n;
	}

	// 사진 또는 파일첨부가 있는지 없는지 알아오는 메소드
	@Override
	public List<Map<String, String>> ischeckAttachfile(String notice_no) {
		List<Map<String, String>> attachList = mapper_dao.ischeckAttachfile(notice_no);
		return attachList;
	}
	
	

	
	
	
	
	
	
	
	
/*	


	// === 부서별게시판과 공지사항게시판에 글쓰기 insert(파일 첨부가 없는 경우라면) === //
	@Override
	public int Board_Write(Map<String, String> paraMap) {
		int n = mapper_dao.Board_Write(paraMap);
		return n;
	}
	

	

	
	// === 부서별 게시판과 공지사항 게시판 글쓰기에서 작성한 임시저장글 insert 해주기 === //
	@Override
	public int TempBoard_Write(Map<String, String> paraMap) {
		int m = mapper_dao.TempBoard_Write(paraMap);
		return m;
	}

	// === 페이징 처리를 안한 검색어가 없는 공지사항게시판 또는 부서게시판에 작성한 임시저장글 목록 보여주기 === //
	@Override
	public List<Map<String, String>> temporaryBoardSearch(String boardLocation) {
		List<Map<String, String>> tmapList = mapper_dao.temporaryBoardSearch(boardLocation);
		return tmapList;
	}

*/



































	

	
}
