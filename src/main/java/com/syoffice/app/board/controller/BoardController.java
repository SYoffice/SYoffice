package com.syoffice.app.board.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.syoffice.app.board.domain.BoardVO;
import com.syoffice.app.board.service.BoardService;
import com.syoffice.app.common.FileManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

// 게시판 컨트롤러 선언하기
@Controller
@RequestMapping(value="/board/*") //  /board/ 가 있는 모든 패키지와 맵핑
public class BoardController {

	@Autowired
	private BoardService service;
	
	// === 파일업로드 및 파일다운로드를 해주는 FileManager 클래스 의존객체 주입하기(DI : Dependency Injection) === // 
//	@Autowired private FileManager fileManager;
	
	// === #1. 글쓰기 페이지 이동 === // 
	@GetMapping("GroupWare_Write")
	public ModelAndView GroupWare_Write(ModelAndView mav) { // 글쓰기 페이지 이동 컨트롤러
		mav.setViewName("board/WriteHome");
		return mav;
	}
	
	// === #2. 파일첨부가 없는 글쓰기 완료 === // 
	@PostMapping("GroupWare_Write") // post 방식으로 WriteHome.jsp 폼에서 값을 보내줌. > 이 데이터들을 db에 넣어줘야함.
	public ModelAndView GroupWare_Write(ModelAndView mav, BoardVO boardvo) {

		int n = service.GroupWare_Write(boardvo);
	   
		if( n == 1 ) { // insert 가 되어졌다면 해당 게시판 홈으로 이동해야한다.
		    mav.setViewName("redirect:/board/GroupWare_Board"); 
		    // /board/GroupWare_Board 페이지로 redirect(페이지이동) 해라는 말이다.
		    // redirect: url로 갈때 사용
	    }
	    else { // insert 가 실패했다면 에러창을 띄운다.
		    mav.setViewName("board/error/add_error");
		    //  /WEB-INF/views/board/error/add_error.jsp 파일을 생성한다.
	    }
		
		return mav;	
	
	}
	
	// === #=== // 
	@GetMapping("GroupWare_Board")	// 페이지 이동만 하는 컨트롤러
	public ModelAndView GroupWare_Board(ModelAndView mav) { // 게시판 홈페이지(전체 게시글 목록 조회 페이지)
		
		mav.setViewName("board/BoardHome");
		
		return mav;
	}
	
	
	
	
	
	
	
	
	
	
	@GetMapping("GroupWare_ViewOne")	// 페이지 이동만 하는 컨트롤러
	public ModelAndView GroupWare_ViewOne(ModelAndView mav) { // 게시글 1개 상세 조회 페이지
		
		mav.setViewName("board/ViewOne");
		
		return mav;
	}
	
	@GetMapping("GroupWare_Edit")	// 페이지 이동만 하는 컨트롤러
	public ModelAndView GroupWare_Edit(ModelAndView mav) { // 게시글 수정 페이지
		
		mav.setViewName("board/Edit");
		
		return mav;
	}
	
	
	@GetMapping("GroupWare_Del")	// 페이지 이동만 하는 컨트롤러
	public ModelAndView GroupWare_Del(ModelAndView mav) { // 게시글 삭제 페이지
		
		mav.setViewName("board/Del");
		
		return mav;
	}
	
	
	
	/*
	 * @PostMapping("like") public Map<String,Integer> like(@RequestParam
	 * Map<String, String> paraMap) {
	 * 
	 * int n = 0 ;
	 * 
	 * n = service.addLike(paraMap); // 좋아요를 누르면 해당 게시글에 좋아요개수 1증가
	 * 
	 * Map<String,Integer> map = new HashMap<>(); map.put("n", n); return map; //
	 * {"n":1} }
	 */
	
}
