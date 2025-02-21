package com.syoffice.app.board.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.syoffice.app.common.MyUtil;
import com.syoffice.app.employee.domain.EmployeeVO;
import com.syoffice.app.board.domain.BoardVO;
import com.syoffice.app.board.domain.NoticeBoardVO;
import com.syoffice.app.board.service.BoardService;
import com.syoffice.app.common.FileManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// === 게시판 컨트롤러 선언하기 === //
@Controller
@RequestMapping(value="/board/*") //  /board/ 가 있는 모든 URL과 맵핑
public class BoardController {

	@Autowired
	private BoardService service;
	
	// === 파일업로드 및 파일다운로드를 해주는 FileManager 클래스 의존객체 주입하기(DI : Dependency Injection) === // 
	@Autowired
	private FileManager fileManager;
	
	// === 글쓰기 페이지로 이동 === //
	@GetMapping("GroupWare_Write")
	public ModelAndView GroupWare_Write(HttpServletRequest request // 추후에 사용(Before Advice 를 사용하기)
									  , HttpServletResponse response
									  , ModelAndView mav) { 
		mav.setViewName("board/WriteHome");
		return mav;
	}
	
	// === 공지사항 게시판 글쓰기(파일첨부가 있는 글쓰기와 없는 글쓰기 분류) === //
	@PostMapping("GroupWare_noticeWrite")
	public ModelAndView GroupWare_noticeWrite(RedirectAttributes redirectAttributes
										    , ModelAndView mav
										    , NoticeBoardVO noticevo
										    , @RequestParam(defaultValue="1") String board_show   // 부서게시판 게시글 공개설정 여부
										    , @RequestParam String boardLocation 				  // 게시글 등록 위치
										    , MultipartHttpServletRequest mrequest) { 		      // 파일첨부
								
		// ====================== 공지사항 게시판 글쓰기 시작 ======================== //
		int n = 0;
		List<Integer> notice_no = new ArrayList<Integer>(); // 공지사항게시판 글번호 알아오는 용도(파일첨부시 필요)
		
		if(boardLocation.equals("notice")) { // 공지사항 게시판에 글을 쓰는 경우라면
			
			String notice_subject = mrequest.getParameter("subject");
			String notice_content = mrequest.getParameter("content");
			
			noticevo.setNotice_subject(notice_subject);
			noticevo.setNotice_content(notice_content);
			
			// === 공지사항 게시판 글쓰기 (파일첨부가 없는 경우) === //
//			n = service.noticeBoardWrite(noticevo); 
			
		   // ===  파일첨부가 있는 경우 작업 시작 === //	
		   MultipartFile attach = noticevo.getAttach();
		   if(!attach.isEmpty()) {// 파일첨부가 있는 경우라면
			
		   HttpSession session = mrequest.getSession();
		   String root = session.getServletContext().getRealPath("/"); // 절대경로 알아오기
			
		   String path = root+"resources"+File.separator+"files"; // 업로드 해줄 파일경로
			
		   String newFileName = "";
		   // WAS(톰캣)의 디스크에 저장될 파일명
		   
		   byte[] bytes = null;
		   // 첨부파일의 내용물을 담는 것(byte타입으로 받아야한다.)
		   
		   long fileSize = 0;
		   // 첨부파일의 크기
		   
		   try {
			   bytes = attach.getBytes();
			   // 첨부파일의 내용물을 읽어오는 것
			   
			   String originalFilename = attach.getOriginalFilename();
			   // attach.getOriginalFilename() 이 첨부파일명의 파일명(예: 강아지.png)이다.
			   
//			   System.out.println("확인용 => originalFilename" +originalFilename);
			   
			   // 첨부되어진 파일을 업로드 하는 것이다.
			   newFileName = fileManager.doFileUpload(bytes, originalFilename, path);
			   System.out.println(newFileName);
			   
			   // === NoticeBoardVO noticevo 에 fileName 값과 orgFilename 값과 fileSize 값을 넣어주기 === //
			   noticevo.setAtnotice_filename(newFileName);
			   // WAS(톰캣)에 저장된 파일명(2025020709291535243254235235234.png)
			   
			   noticevo.setAtnotice_orgfilename(newFileName); // 실제파일명도 함께 넣어줘야함
			   // 게시판 페이지에서 첨부된 파일(강아지.png)을 보여줄 때 사용.
	           // 또한 사용자가 파일을 다운로드 할때 사용되어지는 파일명으로 사용.
			   
			   fileSize = attach.getSize(); // 첨부파일의 크기(단위는 byte)
			   noticevo.setAtnotice_filesize(String.valueOf(fileSize));
			   
		   } catch (Exception e) {
				e.printStackTrace();
		   }
		   
	   }// end of if(!attach.isEmpty())----------------------------
			
		// === 파일 첨부가 있는 글쓰기 또는 파일첨부가 없는 글쓰기로 나뉘어서 service를 호출하기 시작 === //
	    if(attach.isEmpty()) {// 파일 첨부가 없는 경우라면
	    	n = service.noticeBoardWrite(noticevo);
	    }
	    else {// 파일 첨부가 있는 경우라면
	    	int lastnotice_no = 0; // 등록된 게시글의 마지막 글번호를 알아오는 용도(공지사항게시판 파일첨부 테이블에 데이터 넣을시 필요)
	    	
	    	n = service.noticeBoardWrite(noticevo);
	    	// 글번호 조회해오기 //
	    	notice_no = service.notice_no();

	    	if (!notice_no.isEmpty()) { // 리스트가 비어있지 않다면
	    		lastnotice_no = notice_no.get(notice_no.size() - 1);
//	            System.out.println("lastnotice_no: " + lastnotice_no); // 48
	        }
	    	
	    	noticevo.setNotice_no(String.valueOf(lastnotice_no));
	    	
	   	    n = service.NoticeWrite_withFile(noticevo); 
	    }
	    // === 파일 첨부가 있는 글쓰기 또는 파일첨부가 없는 글쓰기로 나뉘어서 service를 호출하기 끝 === //
		}	
		
		if(n == 1) {// insert 가 되어졌다면
//	    	System.out.println("파일첨부 insert 성공!");
	    	redirectAttributes.addAttribute("boardLocation", boardLocation); // 등록 위치(부서 or 공지사항 게시판) URL로 보내는 용도
		    mav.setViewName("redirect:/board/GroupWare_noticeBoard");
		   // /board/GroupWare_Board 페이지로 redirect(페이지이동)
	    }
	    else {// insert 가 실패했다면
		    mav.setViewName("board/error/add_error");
		    //  /WEB-INF/views/board/error/add_error.jsp 파일을 생성한다.
	    }  
		return mav;
	}
	
	// === 공지사항 게시판 홈페이지(전체 게시글 목록 조회 페이지) db에 있는 데이터들을 select === // 
	@GetMapping("GroupWare_noticeBoard")
	public ModelAndView GroupWare_noticeBoard(ModelAndView mav, HttpServletRequest request
										     ,@RequestParam String boardLocation
										     ,@RequestParam(defaultValue = "1") String currentShowPageNo
										     ,@RequestParam(defaultValue = "") String searchType
										     ,@RequestParam(defaultValue = "") String searchWord) { 

//	System.out.println("boardLocation==>" +boardLocation);
//	System.out.println("currentShowPageNo==>" +currentShowPageNo);
//	System.out.println("searchType==>" +searchType);
//	System.out.println("searchWord==>" +searchWord);
	
	List<Map<String, String>> boardList = null;
	
	/////////////////////////////////////////////////////
	// 	        글조회수(readCount)증가 (DML문 update)는
	//          반드시 목록보기(list 페이지)에 와서 해당 글제목을 클릭했을 경우에만 증가되고,
	//          웹브라우저에서 새로고침(F5)을 했을 경우에는 증가가 되지 않도록 해야 한다.
	//          이것을 하기 위해서는 session 을 사용하여 처리하면 된다.
	HttpSession session = request.getSession();
	session.setAttribute("readCountPermission", "yes");
	/*
	session 에  "readCountPermission" 키값으로 저장된 value값은 "yes" 이다.
	session 에  "readCountPermission" 키값에 해당하는 value값 "yes"를 얻으려면 
	반드시 웹브라우저에서 주소창에 "/board/GroupWare_Board" 이라고 입력해야만 얻어올 수 있다. 
	*/
	/////////////////////////////////////////////////////
	
	// === 페이징 처리를 안한 검색어가 없는 부서 게시판 전체 글목록 보여주기 === // 
	//boardList = service.boardListNoSearch(boardLocation);
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	Map<String,String> paraMap = new HashMap<>();
	paraMap.put("searchType", searchType);
	paraMap.put("searchWord", searchWord);
	paraMap.put("boardLocation", boardLocation);
	
	// ===  페이징 처리를 한 검색어가 있는 전체 글목록 보여주기 === //
	// 먼저, 총 게시물 건수(totalCount) 를 구해와야 한다.
	// 총 게시물 건수(totalCount)는 검색조건이 있을 때와 검색조건이 없을때로 나뉘어진다.
	int totalCount = 0; 		// 총 게시물 건수
	int sizePerPage = 10; 		// 한 페이지당 보여줄 게시물 건수
	int totalPage = 0; 			// 총 페이지 수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)
	
	int n_currentShowPageNo = 0;// db에 넘어가기 위한 용도
	
	// 총 게시물 건수 (totalCount)
	totalCount = service.getTotalCount(paraMap);
//	System.out.println("~~~ 확인용 totalCount :" + totalCount);
	
	totalPage = (int) Math.ceil((double)totalCount/sizePerPage);	
	
	try {
		n_currentShowPageNo = Integer.parseInt(currentShowPageNo);
	
	if(n_currentShowPageNo < 1 || n_currentShowPageNo > totalPage) {
		// get 방식이므로 사용자가 currentShowPageNo 에 입력한 값이 0 또는 음수를 입력하여 장난친 경우 
		// get 방식이므로 사용자가 currentShowPageNo 에 입력한 값이 실제 데이터베이스에 존재하는 페이지수 보다 더 큰값을 입력하여 장난친 경우
		n_currentShowPageNo = 1;
//		System.out.println("currentShowPageNo"+currentShowPageNo);
	}
	
	} catch(NumberFormatException e) {
		// get 방식이므로 currentShowPageNo에 입력한 값이 숫자가 아닌 문자를 입력하거나 
		// int 범위를 초과한 경우
		n_currentShowPageNo = 1;
	}
	
	int startRno = ((n_currentShowPageNo - 1) * sizePerPage) + 1; // 시작 행번호 
	int endRno = startRno + sizePerPage - 1;					  //  끝 행번호
	
//	System.out.println("startRno" +startRno );
//	System.out.println("endRno" +endRno );
	
	paraMap.put("startRno", String.valueOf(startRno));  						// Oracle 11g 와 호환되는 것으로 사용
	paraMap.put("endRno", String.valueOf(endRno));								// Oracle 11g 와 호환되는 것으로 사용
	
	paraMap.put("currentShowPageNo", String.valueOf(currentShowPageNo));		// Oracle 12c 이상으로 사용
	
	
	boardList = service.boardListSearch_withPaging(paraMap);
	// 글목록 가져오기(페이징 처리 했으며, 검색어가 있는것 또는 검색어가 없는 것 모두 다 포함한 것이다.)
	
	
	mav.addObject("boardList", boardList);
	
	// 검색시 검색조건 및 검색어 유지시키기
	if( "subject".equals(searchType) ||
		"content".equals(searchType) ||
		"subject_content".equals(searchType)||
		"name".equals(searchType)) {
	
		// === #107. 이전글제목, 다음글제목 보여줄때 검색이 있는지 여부를 넘겨주기 시작 === //
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		// === 이전글제목, 다음글제목 보여줄때 검색이 있는지 여부를 넘겨주기 끝 === //
		
		mav.addObject("paraMap",paraMap);
	}
	
		// === 페이지바 만들기 === //
		int blockSize = 10;
		
		int loop = 1;
		
		int pageNo = ((n_currentShowPageNo - 1)/blockSize) * blockSize + 1;
		
		String pageBar = "<ul style='list-style:none;'>";
		String url = "GroupWare_noticeBoard?boardLocation="+boardLocation+"";
	
		// === [맨처음][이전] 만들기 === //
		pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='"+url+"&searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo=1'>[맨처음]</a></li>";
	
	if(Integer.parseInt(currentShowPageNo) > 1) {
		pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='"+url+"&searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+(Integer.parseInt(currentShowPageNo)-1)+"'>[이전]</a></li>";
	}
	
	while(!(loop > blockSize || pageNo > totalPage)) {
	
	if(pageNo == Integer.parseInt(currentShowPageNo)) {
		pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; border:solid 1px gray; padding:2px 4px;'>"+pageNo+"</li>";
	}
	else {
		pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='"+url+"&searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
	}
	
		loop++;
		pageNo++;
	}// end of while()---------------------
	
	// === [다음][마지막] 만들기 === //
	
	if(Integer.parseInt(currentShowPageNo) < totalPage) {
		pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='"+url+"&searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+(Integer.parseInt(currentShowPageNo)+1)+"'>[다음]</a></li>";
	}
	
		pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='"+url+"&searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+totalPage+"'>[마지막]</a></li>";
	
	
		pageBar += "</ul>";
	
		mav.addObject("pageBar",pageBar);
	
		///////////////////////////////////////////////////////////////////////////////////////////////////
		
		mav.addObject("totalCount", totalCount); 				// 페이징 처리시 보여주는 순번을 나타내기 위한 것임.
		mav.addObject("currentShowPageNo", currentShowPageNo);  // 페이징 처리시 보여주는 순번을 나타내기 위한 것임.
		mav.addObject("sizePerPage", sizePerPage); 				// 페이징 처리시 보여주는 순번을 나타내기 위한 것임.
		
		String currentURL = MyUtil.getCurrentURL(request); // 현재 페이지
		
		mav.addObject("goBackURL",currentURL); // 현재 페이지 => 돌아갈 페이지(새로고침기능)
		
		request.setAttribute("boardLocation", boardLocation);	
		mav.addObject("boardList", boardList);
		mav.setViewName("board/NoticeBoardHome");
	
		return mav;
	}
	
	// 특정글을 조회한 후 "검색된결과목록보기" 버튼을 클릭했을 때 돌아갈 페이지를 만들기 위함.
	@RequestMapping("viewOne")
	public ModelAndView view(ModelAndView mav, 
			 				 HttpServletRequest request) { 
		
		String notice_no = "";
		String goBackURL = "";
		String searchType = "";
		String searchWord = "";
		
		notice_no = request.getParameter("notice_no");
		goBackURL = request.getParameter("goBackURL");
		searchType = request.getParameter("searchType");
		searchWord = request.getParameter("searchWord");
		
		if(searchType == null) {
			searchType = "";
		}
		
		if(searchWord == null) {
			searchWord = "";
		}
		
		HttpSession session = request.getSession();
		EmployeeVO loginuser = (EmployeeVO)session.getAttribute("loginuser");
		
		String login_userid = null; // 로그인을 하지 않은 상태에서도 글목록을 볼 수 있다.
		if(loginuser != null) {
			login_userid = loginuser.getEmp_id(); 
			// login_userid 는 로그인 되어진 사용자의 userid 이다.
		}
		
		mav.addObject("goBackURL",goBackURL);
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("notice_no", notice_no);
		paraMap.put("login_userid", login_userid);
		
		NoticeBoardVO noticeboardvo = null;
		
		if("yes".equals( (String)session.getAttribute("readCountPermission") ) ) { // readCountPermission의 value 값이 String 타입이기 떄문에 (String)
			// 글목록보기인 /GroupWare_noticeBoard 페이지를 클릭한 다음에 특정글을 조회해온 경우이다.
			noticeboardvo = service.getView(paraMap);
			
			// 글 조회수 증가와 함께 글 1개를 조회를 해오는 것
	        // System.out.println("~~ 확인용 글내용 : " + boardvo.getContent());
			
			// 중요함!! session 에 저장된 readCountPermission 을 삭제한다. 
			session.removeAttribute("readCountPermission");
		}
		
		else {
		
		noticeboardvo = service.getNoticeBoardView_no_increase_readCount(paraMap);
        // 글 조회수 증가는 없고 단순히 공지사항 게시판의 글 1개만 조회를 해오는 것		
/*		
		System.out.println("previousseq" + noticeboardvo.getPreviousseq());
		System.out.println("previoussubject"+ noticeboardvo.getPrevioussubject()); 
		System.out.println("notice_no"+ noticeboardvo.getNotice_no());
		System.out.println("fk_emp_id"+ noticeboardvo.getFk_emp_id());
		System.out.println("name"+ noticeboardvo.getName());
		System.out.println("notice_subject"+ noticeboardvo.getNotice_subject());
		System.out.println("notice_content"+ noticeboardvo.getNotice_content());
		System.out.println("notice_viewcount"+ noticeboardvo.getNotice_viewcount());
		System.out.println("notice_regdate"+ noticeboardvo.getNotice_regdate());
		System.out.println("nextseq"+ noticeboardvo.getNextseq());
		System.out.println("nextsubject"+ noticeboardvo.getNextsubject());
*/		
		/*
			previousseq null
			previoussubject ..
			notice_no 56
			fk_emp_id 1000
			name 이순신
			notice_subject 사진 이미지 추가해서 글쓰기
			notice_content <p>asdf</p>
			notice_viewcount 0
			notice_regdate 2025-02-17 17:13:26
			nextseq 55
			nextsubject ㅁㄴㅇㄹ
		 */
		
		if(noticeboardvo == null) {
			  
			return mav;
		}
		
		}
		mav.addObject("noticeboardvo", noticeboardvo);
		mav.addObject("paraMap", paraMap);
		
		mav.setViewName("board/ViewOne");
		
		return mav;
	}
	
	@GetMapping("GroupWare_Edit/{notice_no}")
	public ModelAndView GroupWare_Edit(HttpServletRequest request 
									 , HttpServletResponse response
									 , ModelAndView mav
									 , @PathVariable String notice_no) {
		
		Long.parseLong(notice_no);
		
		// 글 수정해야할 글 1개 내용 가져오기
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("notice_no", notice_no);
		
		NoticeBoardVO noticeboardvo = service.getNoticeBoardView_no_increase_readCount(paraMap);
		// 글 조회수 증가는 없고 단순히 글 1개만 조회를 해오는 것
		
//		System.out.println("확인용: noticeboardList" + noticeboardvo.getNotice_subject());
//		System.out.println("확인용: noticeboardList" + noticeboardvo.getNotice_content());
//		System.out.println("확인용: noticeboardList" + noticeboardvo.getAtnotice_orgfilename());
/*		
		if(noticeboardvo == null) { // 공지사항 게시판에 글이 없다면 
			mav.setViewName("redirect:/board/NoticeBoardHome");
			
		}
		else { // 글이 있다면
*/			
			mav.addObject("noticeboardvo",noticeboardvo);
			mav.setViewName("board/EditHome");
			
//		}
		
//		mav.setViewName("board/EditHome");
		return mav;
	}
	
	
	@PostMapping("GroupWare_Edit")
	public ModelAndView GroupWare_Edit(ModelAndView mav, 
							 		   HttpServletRequest request) { 
		
		String notice_subject = request.getParameter("subject");
		String notice_content = request.getParameter("content");
		String notice_no = request.getParameter("notice_no");
		
		
//		String notice_filesize = noticeboardvo.getAtnotice_filesize();
//		String notice_orgfilename = noticeboardvo.getAtnotice_orgfilename();
		
//		System.out.println("확인용 notice_subject : " + notice_subject);
//		System.out.println("확인용 notice_content : " + notice_content);
//		System.out.println("확인용 notice_no : " + notice_no);
//		System.out.println("확인용 notice_filesize : " + noticeboardvo.getAtnotice_filesize());
//		System.out.println("확인용 notice_orgfilename : " + noticeboardvo.getAtnotice_orgfilename());
		
		NoticeBoardVO noticeboardvo = new NoticeBoardVO();
		noticeboardvo.setNotice_subject(notice_subject);
		noticeboardvo.setNotice_content(notice_content);
		noticeboardvo.setNotice_no(notice_no);
		
		// 공지사항 게시판 글수정하기
		int n = service.update_notice_board(noticeboardvo);
		
		if(n==1) {
//			System.out.println("글수정 확인용 : " +noticeboardvo.getNotice_no());
			mav.addObject("message","글수정 성공!!");
			mav.addObject("loc", request.getContextPath()+"/board/viewOne?notice_no="+ noticeboardvo.getNotice_no());
			mav.setViewName("common/msg");
		}
		
		return mav;
	}
	
	
	// === 글을 삭제하는 페이지 요청 === //
	@PostMapping("GroupWare_Del")
	public ModelAndView GroupWare_Del(@RequestParam String notice_no,
	                                  HttpServletRequest request,
	                                  ModelAndView mav) {

	    int n = 0;

	    // 첨부파일 또는 사진이 있는지 확인하는 메서드 호출
	    List<Map<String, String>> attachList = service.ischeckAttachfile(notice_no);

	    // === 첨부파일 및 사진이 있을 경우 처리 ===
	    if (attachList != null && !attachList.isEmpty()) { //리스트가 null이 아니고, 비어 있지 않은 경우만 실행
	        HttpSession session = request.getSession();
	        String root = session.getServletContext().getRealPath("/");

	        String filepath = root + "resources" + File.separator + "files"; // 첨부파일 경로
	        String photo_upload_path = root + "resources" + File.separator + "photo_upload"; // 사진파일 경로

	        for (Map<String, String> attach : attachList) {
	            // 📌 첨부파일 삭제 처리
	            String filename = attach.get("atnotice_filename");
	            if (filename != null && !filename.isEmpty()) { //파일명이 존재하는 경우에만 삭제
	                try {
	                    fileManager.doFileDelete(filename, filepath);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }

	            //사진파일 삭제 처리
	            String photofilename = attach.get("photofilename");
	            if (photofilename != null && !photofilename.isEmpty()) { //photofilename이 null이 아닌 경우 처리
	                if (photofilename.contains("/")) { // ✅ 여러 개의 이미지가 있는 경우
	                    String[] arr_photofilename = photofilename.split("/");

	                    for (String photo : arr_photofilename) { // 여러 개의 사진 삭제
	                        try {
	                            fileManager.doFileDelete(photo, photo_upload_path);
	                        } catch (Exception e) {
	                            e.printStackTrace();
	                        }
	                    }
	                } else { // ✅ 사진이 하나만 존재하는 경우
	                    try {
	                        fileManager.doFileDelete(photofilename, photo_upload_path);
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	                }
	            }
	        }

	        // 모든 파일 삭제 후 DB에서 해당 글 삭제
	        n = service.noticeBoarDel(notice_no);
	    } else {
	        // 첨부파일이 없는 경우 바로 글 삭제
	        n = service.noticeBoarDel(notice_no);
	    }

	    // === 삭제 결과 처리 ===
	    if (n == 1) {
	        mav.addObject("message", "글 삭제 성공!!");
	        mav.addObject("loc", request.getContextPath() + "/board/GroupWare_noticeBoard?boardLocation=notice");
	        mav.setViewName("common/msg");
	    }

	    return mav;
	}

	//////////////////////////////////////////////////////////////======= 공지사항 끝 ======//////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// === #1002. 부서 게시판 파일첨부가 없는 글쓰기(파일첨부가 있는 글쓰기와 분류) === // 
	@PostMapping("GroupWare_deptWrite") // post 방식으로 WriteHome.jsp 폼에서 값을 보내줌. > 이 데이터들을 db에 넣어줘야함.
	public ModelAndView GroupWare_Write(RedirectAttributes redirectAttributes
									  , ModelAndView mav
									  , BoardVO boardvo
									  , @RequestParam(defaultValue="1") String board_show // 공개설정
									  , @RequestParam String boardLocation 				  // 게시글 등록 위치
									  , MultipartHttpServletRequest mrequest) { 		  // 파일첨부

		// ============== 글쓰기 =============== //
		int n = 0;
		List<Integer> board_no = new ArrayList<Integer>();  // 부서게시판 글번호 알아오는 용도
		
		if(boardLocation.equals("boardDept")) { // 부서 게시판에 글을 쓰는 경우
			
//			n = service.deptBoardWrite(boardvo); // 부서 게시판 (파일첨부가 없는)글쓰기 insert
			
		   // === 파일첨부가 있는 경우 작업 시작 === //	
		   MultipartFile attach = boardvo.getAttach();
		   
		   if(!attach.isEmpty()) {// 첨부파일이 있는 경우라면
		
		   HttpSession session = mrequest.getSession();
		   String root = session.getServletContext().getRealPath("/"); // 절대경로 알아오기
			
		   String path = root+"resources"+File.separator+"files"; // 업로드 해줄 파일경로
			
		   String newFileName = "";
		   // WAS(톰캣)의 디스크에 저장될 파일명
		   
		   byte[] bytes = null;
		   // 첨부파일의 내용물을 담는 용도
		   
		   long fileSize = 0;
		   // 첨부파일의 크기
		   
		   try {
			   bytes = attach.getBytes();
			   // 첨부파일의 내용물을 읽어오는 것
			   
			   String originalFilename = attach.getOriginalFilename();
			   // attach.getOriginalFilename() 이 첨부파일명의 파일명(예: 강아지.png)이다.
			   
			   // 첨부되어진 파일을 업로드 하는 것이다.
			   newFileName = fileManager.doFileUpload(bytes, originalFilename, path);
//			   System.out.println(newFileName);
			   // === #151. BoardVO boardvo 에 fileName 값과 orgFilename 값과 fileSize 값을 넣어주기 === //
			   boardvo.setFileName(newFileName);
			   // WAS(톰캣)에 저장된 파일명(2025020709291535243254235235234.png)
			   
			   boardvo.setOrgFilename(newFileName);
			   // 게시판 페이지에서 첨부된 파일(강아지.png)을 보여줄 때 사용.
	           // 또한 사용자가 파일을 다운로드 할때 사용되어지는 파일명으로 사용.
			   
			   fileSize = attach.getSize(); // 첨부파일의 크기(단위는 byte)
			   boardvo.setFileSize(String.valueOf(fileSize));
			   
			   
		   } catch (Exception e) {
				e.printStackTrace();
		   }
		   
	   }// end of if(!attach.isEmpty())----------------------------
			
		// === 파일 첨부가 있는 글쓰기 또는 파일첨부가 없는 글쓰기로 나뉘어서 service를 호출하기 시작 === //	   
		
	    if(attach.isEmpty()) {// 파일 첨부가 없는 경우라면
	    	n = service.deptBoardWrite(boardvo);
	    }
	    else {// 파일 첨부가 있는 경우라면
	    	int lastboard_no = 0; // 등록된 게시글의 마지막 글번호를 알아오는 용도(부서게시판 파일첨부 테이블에 데이터 넣을 용도)
	    	
	    	n = service.deptBoardWrite(boardvo);
	    	// 글번호 조회해오기 //
	    	board_no = service.board_no();

	    	if (!board_no.isEmpty()) { // 리스트가 비어있지 않다면
	    		lastboard_no = board_no.get(board_no.size() - 1);
//	            System.out.println("lastboard_no: " + lastboard_no); // 48
	        }
	    	
	    	boardvo.setBoard_no(String.valueOf(lastboard_no));
	    	
	   	    n = service.BoardWrite_withFile(boardvo); 
//	   	    System.out.println("Insert 결과: " + n);
	    }
	    // === 파일 첨부가 있는 글쓰기 또는 파일첨부가 없는 글쓰기로 나뉘어서 service를 호출하기 끝 === //
	    
		}// end of if(boardLocation.equals("boardDept"))-------------------------------------------------------
		
		
		
	    if(n == 1) {// insert 가 되어졌다면
//	    	System.out.println("파일첨부 insert 성공!");
	    	redirectAttributes.addAttribute("boardLocation", boardLocation); // 등록 위치(부서 or 공지사항 게시판) URL로 보내는 용도
		    mav.setViewName("redirect:/board/GroupWare_Board");
		   // /board/GroupWare_Board 페이지로 redirect(페이지이동)
	    }
	    else {// insert 가 실패했다면
		    mav.setViewName("board/error/add_error");
		    //  /WEB-INF/views/board/error/add_error.jsp 파일을 생성한다.
	    }  
		
		return mav;
		
	}
	
	
	// === 부서 게시판 홈페이지(전체 게시글 목록 조회 페이지) db에 있는 데이터들을 select === // 
	@GetMapping("GroupWare_Board")
	public ModelAndView GroupWare_Board(ModelAndView mav, HttpServletRequest request
									   ,@RequestParam String boardLocation
									   ,@RequestParam(defaultValue = "1") String currentShowPageNo
									   ,@RequestParam(defaultValue = "") String searchType
									   ,@RequestParam(defaultValue = "") String searchWord) { 

		System.out.println("boardLocation==>" +boardLocation);
		System.out.println("currentShowPageNo==>" +currentShowPageNo);
		System.out.println("searchType==>" +searchType);
		System.out.println("searchWord==>" +searchWord);
		
		List<Map<String, String>> boardList = null;
		
		/////////////////////////////////////////////////////
		// ===      글조회수(readCount)증가 (DML문 update)는
	    //          반드시 목록보기(list 페이지)에 와서 해당 글제목을 클릭했을 경우에만 증가되고,
	    //          웹브라우저에서 새로고침(F5)을 했을 경우에는 증가가 되지 않도록 해야 한다.
	    //          이것을 하기 위해서는 session 을 사용하여 처리하면 된다.
		HttpSession session = request.getSession();
		session.setAttribute("readCountPermission", "yes");
		/*
	        session 에  "readCountPermission" 키값으로 저장된 value값은 "yes" 이다.
	        session 에  "readCountPermission" 키값에 해당하는 value값 "yes"를 얻으려면 
	        반드시 웹브라우저에서 주소창에 "/board/GroupWare_Board" 이라고 입력해야만 얻어올 수 있다. 
	    */
		/////////////////////////////////////////////////////
		
		
		
		// === 페이징 처리를 안한 검색어가 없는 부서 게시판 전체 글목록 보여주기 === // 
//		boardList = service.boardListNoSearch(boardLocation);
		
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		paraMap.put("boardLocation", boardLocation);
		
		// ===  페이징 처리를 한 검색어가 있는 전체 글목록 보여주기 === //
		// 먼저, 총 게시물 건수(totalCount) 를 구해와야 한다.
		// 총 게시물 건수(totalCount)는 검색조건이 있을 때와 검색조건이 없을때로 나뉘어진다.
		int totalCount = 0; 		// 총 게시물 건수
		int sizePerPage = 10; 		// 한 페이지당 보여줄 게시물 건수
		int totalPage = 0; 			// 총 페이지 수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)

 		int n_currentShowPageNo = 0;// db에 넘어가기 위한 용도
		
 		// 총 게시물 건수 (totalCount)
 		totalCount = service.getTotalCount(paraMap);
 		System.out.println("~~~ 확인용 totalCount :" + totalCount);
		
		totalPage = (int) Math.ceil((double)totalCount/sizePerPage);		
		
		try {
			n_currentShowPageNo = Integer.parseInt(currentShowPageNo);
			
			if(n_currentShowPageNo < 1 || n_currentShowPageNo > totalPage) {
				// get 방식이므로 사용자가 currentShowPageNo 에 입력한 값이 0 또는 음수를 입력하여 장난친 경우 
	            // get 방식이므로 사용자가 currentShowPageNo 에 입력한 값이 실제 데이터베이스에 존재하는 페이지수 보다 더 큰값을 입력하여 장난친 경우
				n_currentShowPageNo = 1;
			}
			
		} catch(NumberFormatException e) {
			// get 방식이므로 currentShowPageNo에 입력한 값이 숫자가 아닌 문자를 입력하거나 
			// int 범위를 초과한 경우
			n_currentShowPageNo = 1;
		}
		
		int startRno = ((n_currentShowPageNo - 1) * sizePerPage) + 1; // 시작 행번호 
		int endRno = startRno + sizePerPage - 1;					  //  끝 행번호
		System.out.println("startRno==>" +startRno);
		System.out.println("endRno==>" +endRno);
		
		
		paraMap.put("startRno", String.valueOf(startRno));  						// Oracle 11g 와 호환되는 것으로 사용
		paraMap.put("endRno", String.valueOf(endRno));								// Oracle 11g 와 호환되는 것으로 사용
		
		paraMap.put("currentShowPageNo", String.valueOf(currentShowPageNo));		// Oracle 12c 이상으로 사용
		
		
		boardList = service.boardListSearch_withPaging(paraMap);
		// 글목록 가져오기(페이징 처리 했으며, 검색어가 있는것 또는 검색어가 없는 것 모두 다 포함한 것이다.)
		
		
		mav.addObject("boardList", boardList);
		
		
		// 검색시 검색조건 및 검색어 유지시키기
		if( "subject".equals(searchType) ||
			"content".equals(searchType) ||
			"subject_content".equals(searchType)||
			"name".equals(searchType)) {
			
			// === #107. 이전글제목, 다음글제목 보여줄때 검색이 있는지 여부를 넘겨주기 시작 === //
	 		paraMap.put("searchType", searchType);
	 		paraMap.put("searchWord", searchWord);
	 		// === 이전글제목, 다음글제목 보여줄때 검색이 있는지 여부를 넘겨주기 끝 === //
	 		
		    mav.addObject("paraMap",paraMap);
		}
		
		// === 페이지바 만들기 === //
		int blockSize = 10;
		
		int loop = 1;
		
		int pageNo = ((n_currentShowPageNo - 1)/blockSize) * blockSize + 1;
		
		String pageBar = "<ul style='list-style:none;'>";
	    String url = "GroupWare_Board?boardLocation="+boardLocation+"";
		
		// === [맨처음][이전] 만들기 === //
 		pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='"+url+"&searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo=1'>[맨처음]</a></li>";
 		
 		if(pageNo != 1) {
 			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='"+url+"&searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
 		}
 	    
 	    while(!(loop > blockSize || pageNo > totalPage)) {
 	    	
 	    	if(pageNo == Integer.parseInt(currentShowPageNo)) {
 	    		pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; border:solid 1px gray; padding:2px 4px;'>"+pageNo+"</li>";
 	    	}
 	    	else {
 	    		pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='"+url+"&searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
 	    	}
 	    	
 	    	loop++;
 	    	pageNo++;
 	    }// end of while()---------------------
 	    
 	    // === [다음][마지막] 만들기 === //
 		
 	    if(pageNo <= totalPage) {
 	    	pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='"+url+"&searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'>[다음]</a></li>";
 	    }
 	    
 		pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='"+url+"&searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+totalPage+"'>[마지막]</a></li>";

 	    
 	    
 	    pageBar += "</ul>";
 	    
 	    mav.addObject("pageBar",pageBar);
 	     
 	    ///////////////////////////////////////////////////////////////////////////////////////////////////
 	    
 	    mav.addObject("totalCount", totalCount); 				// 페이징 처리시 보여주는 순번을 나타내기 위한 것임.
 	    mav.addObject("currentShowPageNo", currentShowPageNo);  // 페이징 처리시 보여주는 순번을 나타내기 위한 것임.
 	    mav.addObject("sizePerPage", sizePerPage); 				// 페이징 처리시 보여주는 순번을 나타내기 위한 것임.
	    
 	    String currentURL = MyUtil.getCurrentURL(request); // 현재 페이지
 	   
 	    mav.addObject("goBackURL",currentURL); // 현재 페이지 => 돌아갈 페이지(새로고침기능)
 	    
		request.setAttribute("boardLocation", boardLocation);	
		mav.addObject("boardList", boardList);
		mav.setViewName("board/BoardHome");
		
		return mav;
	}

	
	
	
	// === 첨부파일 다운로드 받기 === //
	@GetMapping("download") // view.jsp 에서 seq를 같이 보내줬음
	public void download(HttpServletRequest request, HttpServletResponse response) { 
		
		String notice_no = request.getParameter("notice_no");
		// 첨부파일이 있는 글번호 
		
//		System.out.println("notice_no ===>>" + notice_no);
		/*
		 	첨부파일이 있는 글번호에서 
		 	20250207130548558161004053900.jpg 처럼
		 	이러한 fileName 값을 DB 에서 가져와야 한다.
		 	또한 orgFilename 값도 DB 에서 가져와야 한다.(다운로드할 때는 orgFilename 이걸로 다운받아야하기 떄문에.)
		*/
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("notice_no", notice_no);
		paraMap.put("searchType", "");
		paraMap.put("searchWord", "");
		
		// **** 웹브라우저에 출력하기 시작 **** //
		// HttpServletResponse response 객체는 전송되어져온 데이터를 조작해서 결과물을 나타내고자 할 때 쓰인다.
		response.setContentType("text/html; charset=UTF-8");
		
		PrintWriter out = null;
		// out(== 웹브라우저) 은 웹브라우저에 기술하는 대상체라고 생각하자.
		
		try {
			Integer.parseInt(notice_no);
			
			NoticeBoardVO noticeboardList = service.getNoticeBoardView_no_increase_readCount(paraMap);
			
			if(noticeboardList == null || (noticeboardList != null && noticeboardList.getAtnotice_filename() == null)) { // 존재하지 않는 notice_no가 들어온다면 또는 notice_no는 존재하지만 첨부파일이 없는 경우
				out = response.getWriter();
				// out(== 웹브라우저) 은 웹브라우저에 기술하는 대상체라고 생각하자. 
				
				out.println("<script type='text/javascript'>alert('파일다운로드가 불가합니다.'); history.back();</script>"); // html 을 넣을 수 있음.
				return; // 종료
			}	
			
			else { // 정상적으로 다운로드를 할 경우
				
				String fileName = noticeboardList.getAtnotice_filename();
				// 숫자로 되어진 파일네임(20250207130548558161004053900.jpg) 이것이 바로 WAS(톰캣) 디스크에 저장된 파일 명이다.
				
				String orgFilename = noticeboardList.getAtnotice_orgfilename(); // berkelekle단가라포인트03.jpg 다운로드시 보여줄 파일명
				
				/*
		    		첨부파일이 저장되어있는 WAS(톰캣) 디스크 경로명을 알아와야만 다운로드를 해줄 수 있다.
		    		이 경로는 우리가 파일첨부를 위해서 @PostMapping("add") 에서 설정해두었던 경로와 똑같아야한다.
			    */
			   // WAS 의 webapp 의 절대경로를 알아와야 한다.
			   HttpSession session = request.getSession();
			   String root = session.getServletContext().getRealPath("/");
			   
		//	   System.out.println("~~~ 확인용 webapp의 절대경로 ==> " + root);
			   // ~~~ 확인용 webapp의 절대경로 ==> C:\NCS\workspace_spring_boot_17\myspring\src\main\webapp\
			   
			   String path = root+"resources"+File.separator+"files"; // 여기로 업로드 해주도록 할 것이다
			   /*	
			    	File.separator 는 운영체제에서 사용하는 폴더와 파일의 구분자이다.@@@
	           		운영체제가 Windows 이라면 File.separator 는  "\" 이고,
	           		운영체제가 UNIX, Linux, 매킨토시(맥) 이라면  File.separator 는 "/" 이다. 
			   */
			   
			   // path 가 첨부파일이 저장될 WAS(톰캣)의 폴더가 된다.
		//	   System.out.println("~~~ 확인용 path ==> " + path);
			   // ~~~ 확인용 webapp의 절대경로 ==> C:\NCS\workspace_spring_boot_17\myspring\src\main\webapp\resources\files
			   
			   // ***** file 다운로드 하기 ***** //
			   boolean flag = false; // file 다운로드 성공, 실패인지 여부를 알려주는 용도 
			   
			   flag = fileManager.doFileDownload(fileName, orgFilename, path, response);
			   // file 다운로드 성공시 flag = true,
			   // file 다운로드 실패시 flag = false 를 가진다.
			   
			   if(!flag) { // file 다운로드 실패시 메시지를 띄운다.
				   out = response.getWriter();
				   // out(== 웹브라우저) 은 웹브라우저에 기술하는 대상체라고 생각하자. 
				   
				   out.println("<script type='text/javascript'>alert('파일다운로드가 실패되었습니다.'); history.back();</script>");
			   }
			   
			}
		} catch (NumberFormatException | IOException e) { // 숫자가 아니라면
			
			try {
				out = response.getWriter();
				// out(== 웹브라우저) 은 웹브라우저에 기술하는 대상체라고 생각하자. 
				
				out.println("<script type='text/javascript'>alert('파일다운로드가 불가합니다.'); history.back();</script>"); // html 을 넣을 수 있음.
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
		
	}

	
	
	// === 스마트에디터. 글쓰기 또는 글수정시 드래그앤드롭을 이용한 다중 사진 파일 업로드 하기 === //
	@PostMapping("image/multiplePhotoUpload") // photouploader.html 의 form에 post 방식으로 설정해둠
	public void multiplePhotoUpload(HttpServletRequest request, HttpServletResponse response) {
		
	  /*
         1. 사용자가 보낸 파일을 WAS(톰캣)의 특정 폴더에 저장해주어야 한다.
         >>>> 파일이 업로드 되어질 특정 경로(폴더)지정해주기
              우리는 WAS 의 webapp/resources/photo_upload 라는 폴더로 지정해준다.
      */
      
      // WAS 의 webapp 의 절대경로를 알아와야 한다.
      HttpSession session = request.getSession();
      String root = session.getServletContext().getRealPath("/");
      String path = root + "resources"+File.separator+"photo_upload";
      // path 가 첨부파일들을 저장할 WAS(톰캣)의 폴더가 된다.
      
    //  System.out.println("~~~ 확인용 path => " + path);
      //  ~~~ 확인용 path => C:\NCS\workspace_spring_boot_17\myspring\src\main\webapp\resources\photo_upload
      
      File dir = new File(path); 
      if(!dir.exists()) { // photo_upload파일이 없다면 자동으로 만들어준다.
         dir.mkdirs();
      }
      
      try {
          String filename = request.getHeader("file-name"); // 파일명(문자열)을 받는다 - 일반 원본파일명
          // 네이버 스마트에디터를 사용한 파일업로드시 싱글파일업로드와는 다르게 멀티파일업로드는 파일명이 header 속에 담겨져 넘어오게 되어있다. 
          
          /*
              [참고]
              HttpServletRequest의 getHeader() 메소드를 통해 클라이언트의 정보를 알아올 수 있다. 
    
             request.getHeader("referer");           // 접속 경로(이전 URL)
             request.getHeader("user-agent");        // 클라이언트 사용자의 시스템 정보
             request.getHeader("User-Agent");        // 클라이언트 브라우저 정보 
             request.getHeader("X-Forwarded-For");   // 클라이언트 ip 주소 
             request.getHeader("host");              // Host 네임  예: 로컬 환경일 경우 ==> localhost:9090    
          */
          
       //   System.out.println(">>> 확인용 filename ==> " + filename);
          // >>> 확인용 filename ==> berkelekle%EC%8B%AC%ED%94%8C%EB%9D%BC%EC%9A%B4%EB%93%9C01.jpg
          
          InputStream is = request.getInputStream(); // is는 네이버 스마트 에디터를 사용하여 사진첨부하기 된 이미지 파일명임.
          
          // === 사진 이미지 파일 업로드 하기 === //
          String newFilename = fileManager.doFileUpload(is, filename, path);
       //   System.out.println("### 확인용 newFilename ==> " + newFilename);
          //  ### 확인용 newFilename ==> 20250210165110401783618706200.jpg
          
          
          // === 웹브라우저 상에 업로드 되어진 사진 이미지 파일 이미지를 쓰기 === //
          String ctxPath = request.getContextPath(); //  
          
          String strURL = "";
          strURL += "&bNewLine=true&sFileName="+newFilename; 
          strURL += "&sFileURL="+ctxPath+"/resources/photo_upload/"+newFilename;
                   
          PrintWriter out = response.getWriter();
          out.print(strURL);
          
          // 글쓰기 또는 글수정시 이미지를 추가한 후 이미지를 마우스로 클릭하면
          // 사진 사이즈 조절 레이어 에디터가 보여진다. 여기서 사진의 크기를 조절하면 된다.!!
          // 사진의 크기 조절은 네이버 스마트에디터 소스속에 자바스크립트로 구현이 되어진 것이다.
          // Ctrl + Alt + Shit + L 하여 검색어에 '사진 사이즈 조절 레이어' 를 하면 보여진다.
          
       } catch(Exception e) {
          e.printStackTrace();
       }
      
	}

}
