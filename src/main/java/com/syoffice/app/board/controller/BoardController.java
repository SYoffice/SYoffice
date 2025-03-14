package com.syoffice.app.board.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.syoffice.app.common.MyUtil;
import com.syoffice.app.employee.domain.EmployeeVO;
import com.syoffice.app.board.domain.CommentVO;
import com.syoffice.app.board.domain.BoardVO;
import com.syoffice.app.board.domain.NoticeBoardVO;
import com.syoffice.app.board.service.BoardService;
import com.syoffice.app.common.FileManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// 게시판 컨트롤러 선언하기
@Controller
///board/ 가 있는 모든 URL과 맵핑
@RequestMapping(value = "/board/*") 
public class BoardController {

	// BoardService의 의존객체 주입하기(DI : Dependency Injection)
	@Autowired
	private BoardService service;

	// 파일업로드 및 파일다운로드를 해주는 FileManager 클래스 의존객체 주입하기(DI : Dependency Injection)
	@Autowired
	private FileManager fileManager;
////////////////////////////////////////////////////////////////////////////////	

	
////////////////////////////////////////////////////////////======= 공지사항 시작 ======//////////////////////////////////////////////////////////////////////////////////

	// 글쓰기 페이지로 이동하는 URL 생성하기
	@GetMapping("GroupWare_Write")
	public ModelAndView GroupWare_Write(HttpServletRequest request
									  , HttpServletResponse response
									  , ModelAndView mav) {
		mav.setViewName("board/WriteHome"); // 뷰단 페이지 보여주기
		return mav;
	}

	// 뷰단 페이지("board/WriteHome")에서 넘겨받은 데이터를 tbl_notice에 insert 하기
	@PostMapping("GroupWare_noticeWrite") // 위의 뷰단 페이지에서 작성 위치를 공지사항 게시판에 두고 "등록" 버튼을 누르면 폼 데이터를 post 방식으로 넘겨받음.
	public ModelAndView GroupWare_noticeWrite(ModelAndView mav
											, NoticeBoardVO noticevo
											, @RequestParam(defaultValue = "1") String board_show // 부서게시판 게시글 공개설정 여부
											, @RequestParam String boardLocation				  // 게시글 등록 위치
											, MultipartHttpServletRequest mrequest) { 			  // 파일첨부를 받아올 수 있는 Servlet, HttpServlet은 불가능
// 공지사항 게시판 글쓰기 시작 (파일첨부가 있는 글쓰기와 없는 글쓰기 분류)
		int n = 0;
		Integer notice_no = 0; // 공지사항게시판 글번호 알아오는 용도(파일첨부시 필요)

		if (boardLocation.equals("notice")) { // 공지사항 게시판에 글을 쓰는 경우라면

			// 넘겨받은 데이터를 NoticeBoardVO에 맞는 필드명으로 바꿔주기
			String notice_subject = mrequest.getParameter("subject");
			String notice_content = mrequest.getParameter("content");
			String fk_emp_id = mrequest.getParameter("fk_emp_id");
			String fk_dept_id = mrequest.getParameter("fk_dept_id");

			// NoticeBoardVO에 각 데이터 셋팅하기(NoticeBoardVO에 있는 필드명과 뷰단의 name 이 같았다면 굳이 파라미터로 받아올 필요없이 자동 set 되어진다.)
			noticevo.setNotice_subject(notice_subject);
			noticevo.setNotice_content(notice_content);
			noticevo.setFk_emp_id(fk_emp_id);
			noticevo.setFk_dept_id(fk_dept_id);

			// === 공지사항 게시판 글쓰기 (파일첨부가 없는 경우) === //
			// n = service.noticeBoardWrite(noticevo); 
			// 파일첨부가 없는 경우, tbl_notice 가 요구하는 컬럼(사용자 id, 글제목, 글내용 외엔 default)에 맞춰서 값을 넣어준다.

			// ===  공지사항 게시판 글쓰기 (파일첨부가 있는 경우) 시작 === //	
			MultipartFile attach = noticevo.getAttach();
			// 뷰단에 파일첨부되는 input의 name값을 attach로 설정해준 후, NoticeBoardVO 에 필드명을 적고 getter,setter 해주면 따로 파라미터로 받아오지 않아도 input에 들어간 첨부파일이 NoticeBoardVO attach()로 set 되어진다.

			if (!attach.isEmpty()) { // 파일첨부가 있는 경우라면

				// 뷰단에서 받아온 파일을 WAS(톰캣) 저장해둘 경로 지정하기
				HttpSession session = mrequest.getSession();
				String root = session.getServletContext().getRealPath("/"); // 절대경로 알아오기

				String path = root + "resources" + File.separator + "files"; // 업로드 해줄 파일경로

				String newFileName = "";
				// WAS(톰캣)의 디스크에 저장될 파일명

				byte[] bytes = null;
				// 첨부파일의 내용물을 담는 것(byte타입으로 받아야함)

				long fileSize = 0;
				// 첨부파일의 크기

				try {
					bytes = attach.getBytes();
					// 첨부파일의 내용물을 읽어오는 것

					String originalFilename = attach.getOriginalFilename();
					// attach.getOriginalFilename() 이 첨부파일명의 파일명(예: 강아지.png)이다.

//				   System.out.println("확인용 => originalFilename" +originalFilename);

					// 첨부되어진 파일을 path 에 업로드 하는 것이다.
					newFileName = fileManager.doFileUpload(bytes, originalFilename, path);// 업로드해줄 bytes(파일의 내용물), originalFilename(파일의 원본명), path(파일을 업로드해줄 경로)
//				    System.out.println(newFileName); // 나노시간으로 바뀐 새로운 파일명

					// NoticeBoardVO noticevo 에 fileName 값과 orgFilename 값과 fileSize 값을 넣어주기
					noticevo.setAtnotice_filename(newFileName);
					// WAS(톰캣)에 저장된 파일명(2025020709291535243254235235234.png)

					noticevo.setAtnotice_orgfilename(originalFilename); // 실제파일명도 함께 set 해주기
					// 게시판 페이지에서 첨부된 파일(강아지.png)을 보여줄 때 사용.
					// 또한 사용자가 파일을 다운로드 할때 사용되어지는 파일명으로 사용.

					fileSize = attach.getSize(); // 첨부파일의 크기(단위는 byte)
					noticevo.setAtnotice_filesize(String.valueOf(fileSize));

				} catch (Exception e) {
					e.printStackTrace();
				}

			} // end of if(!attach.isEmpty())----------------------------

// === 파일 첨부가 있는 글쓰기 또는 파일첨부가 없는 글쓰기로 나뉘어서 service를 호출하기 시작 === //
			if (attach.isEmpty()) { // 파일 첨부가 없는 경우라면
				n = service.noticeBoardWrite(noticevo); // 위의것을 주석처리하고 파일첨부 여부에 따라 나누기
			} else {// 파일 첨부가 있는 경우라면

				n = service.noticeBoardWrite(noticevo); // 우선 파일첨부 외의 데이터들을 insert

				// 글번호 조회해오기 //
				notice_no = service.notice_no();// 등록된 게시글의 마지막 글번호를 알아오는 용도(공지사항 파일첨부 테이블에 데이터 넣을시 notice_no(외래키)로 해당 게시글에 파일첨부 하기 위해 필요)
				noticevo.setNotice_no(String.valueOf(notice_no));
//	    		System.out.println("확인용 => " + noticevo.getNotice_no());
				
				n = service.NoticeWrite_withFile(noticevo); // 알아온 notice_no로 해당 글번호에 맞춰 파일첨부 insert
			}
// === 파일 첨부가 있는 글쓰기 또는 파일첨부가 없는 글쓰기로 나뉘어서 service를 호출하기 끝 === //

		} // end of if(boardLocation.equals("notice"))------------------------------------------------------

		if (n == 1) {// insert 가 되어졌다면
//	    	System.out.println("파일첨부 insert 성공!");
			mav.setViewName("redirect:/board/GroupWare_noticeBoard");
			// /board/GroupWare_noticeBoard 페이지로 redirect(페이지이동)
		} else {// insert 가 실패했다면
			mav.setViewName("board/error/add_error");
			// /WEB-INF/views/board/error/add_error.jsp 파일을 생성한다.
		}
		return mav;
	}
// =========================== 공지사항 게시판 글쓰기 끝 (파일첨부가 있는 글쓰기와 없는 글쓰기 분류) =================================== //

// ================ 공지사항 게시판 홈페이지(전체 게시글 목록 조회 페이지) db에 있는 데이터들을 select 한 후 뷰단에 뿌리기 시작 ================= // 
	@GetMapping("GroupWare_noticeBoard")
	public ModelAndView GroupWare_noticeBoard(HttpServletRequest request
											, HttpServletResponse response
											, ModelAndView mav // 뷰단에 뿌리기 용도
											, @RequestParam(defaultValue = "1") String currentShowPageNo
											, @RequestParam(defaultValue = "") String searchType
											, @RequestParam(defaultValue = "") String searchWord) {

// === 페이징 처리를 안한 검색어가 없는 공지사항 게시판 전체 글목록 보여주기(일단 공지사항 게시판 전체 데이터를 추출) === // 
		List<Map<String, String>> noticeBoardList = null;

		// List<Map<String, String>> noticeBoardList =
		// service.noticeBoardListNoSearch();
		// 공지사항 게시판 목록에 뿌려줄때 필요한 정보 => (순번,제목,작성자,작성일자,조회수,페이징바에 필요한 데이터들)


		/////////////////////////////////////////////////////////////////////////
		// 글조회수(readCount)증가 (DML문 update)는
		// 반드시 목록보기(NoticeBoardHome 페이지)에 와서 해당 글제목을 클릭했을 경우에만 증가되고,
		// 웹브라우저에서 새로고침(F5)을 했을 경우에는 증가가 되지 않도록 해야 한다.
		// 이것을 하기 위해서는 session 을 사용하여 처리하면 된다.
		HttpSession session = request.getSession();
		session.setAttribute("readCountPermission", "yes");
		/*
		  session 에 "readCountPermission" 키값으로 저장된 value값은 "yes" 이다. session 에 "readCountPermission" 키값에 해당하는 value값 "yes"를 얻으려면 반드시 웹브라우저에서 주소창에 "/board/GroupWare_noticeBoard" 이라고 입력해야만 얻어올 수 있다.
		*/
		////////////////////////////////////////////////////////////////////////

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);

// ===  페이징 처리를 한 검색어가 있는 전체 글목록 보여주기 === //
		// 먼저, 총 게시물 건수(totalCount) 를 구해와야 한다.
		// 총 게시물 건수(totalCount)는 검색조건이 있을 때와 검색조건이 없을때로 나뉘어진다.
		int totalCount = 0; // 총 게시물 건수
		int sizePerPage = 10; // 한 페이지당 보여줄 게시물 건수
		int totalPage = 0; // 총 페이지 수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)

		int n_currentShowPageNo = 0;// db에 넘어가기 위한 용도

		// 공지사항 게시판의 총 게시물 건수 (totalCount를 구해야 페이징처리 가능)
		totalCount = service.getNoticeTotalCount(paraMap);

		totalPage = (int) Math.ceil((double) totalCount / sizePerPage);
		// 한 페이지당 10개씩 보여준다. 총 개시물이 30개라면 3페이지가 필요하므로 10/30 해주고, Math.ceil를 사용하여 소수점 이하가 조금이라도 있으면 무조건 “올림(ceil)” 처리하여 더 큰 정수를 반환한다.

		try {
			n_currentShowPageNo = Integer.parseInt(currentShowPageNo); // 현재 페이지는 디폴트 1로 설정
			if (n_currentShowPageNo < 1 || n_currentShowPageNo > totalPage) { // 현재페이지가 1보다 작거나 총 페이지보다 큰 경우 1페이지로 이동한다.
				// get 방식이므로 사용자가 currentShowPageNo 에 입력한 값이 0 또는 음수를 입력하여 장난친 경우
				// get 방식이므로 사용자가 currentShowPageNo 에 입력한 값이 실제 데이터베이스에 존재하는 페이지수 보다 더 큰값을 입력하여 장난친 경우
				n_currentShowPageNo = 1;
			}

		} catch (NumberFormatException e) {
			// get 방식이므로 currentShowPageNo에 입력한 값이 숫자가 아닌 문자를 입력하거나 int 범위를 초과한 경우
			n_currentShowPageNo = 1; // 장난친 경우 1페이지로 이동
		}

		// db 에서 row_number() => 각 행에 1부터 순차적인 번호를 부여하는 함수로 페이지마다 보여줄 게시글의 행번호 설정
		int startRno = ((n_currentShowPageNo - 1) * sizePerPage) + 1; // 시작 행번호
		int endRno = startRno + sizePerPage - 1; // 끝 행번호
		
		paraMap.put("startRno", String.valueOf(startRno)); // Oracle 11g 와 호환되는 것으로 사용
		paraMap.put("endRno", String.valueOf(endRno)); // Oracle 11g 와 호환되는 것으로 사용

		paraMap.put("currentShowPageNo", String.valueOf(currentShowPageNo)); // Oracle 12c 이상으로 사용

		noticeBoardList = service.noticeBoardListSearch_withPaging(paraMap);
		// 공지사항 글목록 가져오기(페이징 처리 했으며, 검색어가 있는것 또는 검색어가 없는 것 모두 다 포함한 것이다.)

		mav.addObject("noticeBoardList", noticeBoardList);

		// 검색시 검색조건 및 검색어 유지시키기
		if ("subject".equals(searchType) || "content".equals(searchType) || "subject_content".equals(searchType)
				|| "name".equals(searchType)) {

			// === 이전글제목, 다음글제목 보여줄때 검색이 있는지 여부를 넘겨주기 시작 === //
			paraMap.put("searchType", searchType);
			paraMap.put("searchWord", searchWord);
			// === 이전글제목, 다음글제목 보여줄때 검색이 있는지 여부를 넘겨주기 끝 === //

			mav.addObject("paraMap", paraMap);
		}

		// === 페이지바 만들기 === //
		int blockSize = 10; // 페이지바를 10개씩 잘라서 보여주겠다. 10개 끝나면 [다음] 이런식임.

		int loop = 1; // 현재 블록에서 페이지번호를 몇 개 출력했는지 세는 변수

		int pageNo = ((n_currentShowPageNo - 1) / blockSize) * blockSize + 1; // 페이지번호는 1,2,3,4,5, 순차적으로 가는 공식임

		String pageBar = "<ul style='list-style:none;'>";
		String url = "GroupWare_noticeBoard";

		// === [맨처음][이전] 만들기 === //
		pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='" + url + "?searchType="
				+ searchType + "&searchWord=" + searchWord + "&currentShowPageNo=1'>[맨처음]</a></li>";

		if (Integer.parseInt(currentShowPageNo) > 1) {
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='" + url + "?searchType="
					+ searchType + "&searchWord=" + searchWord + "&currentShowPageNo="
					+ (Integer.parseInt(currentShowPageNo) - 1) + "'>[이전]</a></li>";
		}

		while (!(loop > blockSize || pageNo > totalPage)) {

			if (pageNo == Integer.parseInt(currentShowPageNo)) {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; border:solid 1px gray; padding:2px 4px;'>"
						+ pageNo + "</li>";
			} else {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='" + url
						+ "?searchType=" + searchType + "&searchWord=" + searchWord + "&currentShowPageNo=" + pageNo
						+ "'>" + pageNo + "</a></li>";
			}

			loop++;
			pageNo++;
		} // end of while()---------------------

		// === [다음][마지막] 만들기 === //

		if (Integer.parseInt(currentShowPageNo) < totalPage) {
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='" + url + "?searchType="
					+ searchType + "&searchWord=" + searchWord + "&currentShowPageNo="
					+ (Integer.parseInt(currentShowPageNo) + 1) + "'>[다음]</a></li>";
		}

		pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='" + url + "?searchType="
				+ searchType + "&searchWord=" + searchWord + "&currentShowPageNo=" + totalPage + "'>[마지막]</a></li>";

		pageBar += "</ul>";

		mav.addObject("pageBar", pageBar);

		///////////////////////////////////////////////////////////////////////////////////////////////////
		mav.addObject("startRno", startRno); // 몇번째 게시물부터 보여줄지 정하기 위한 것임.
		mav.addObject("endRno", endRno); // 몇번째 게시물부터 보여줄지 정하기 위한 것임.
		mav.addObject("totalCount", totalCount); // 페이징 처리시 보여주는 순번을 나타내기 위한 것임.
		mav.addObject("currentShowPageNo", n_currentShowPageNo); // 페이징 처리시 보여주는 순번을 나타내기 위한 것임.
		mav.addObject("sizePerPage", sizePerPage); // 페이징 처리시 보여주는 순번을 나타내기 위한 것임.

		String currentURL = MyUtil.getCurrentURL(request); // 현재 페이지
		mav.addObject("goBackURL", currentURL); // 현재 페이지 => 돌아갈 페이지(새로고침기능)

		mav.setViewName("board/NoticeBoardHome");
		return mav;
	}

	// 특정글을 조회한 후 "검색된결과목록보기" 버튼을 클릭했을 때 돌아갈 페이지를 만들기 위함.
	// 공지사항 게시글 목록 조회에서 글 1개를 클릭하면 상세조회할 수 있는 URL // session에 담긴
	// readCountPermission에 따라 조회수 증가 설정
	// 위의 NoticeBoardHome 뷰단에서 글제목을 클릭하면 (notice_no(hidden 처리),goBackURL(hidden
	// 처리),searchType,searchWord) 데이터를 post 방식으로 보내주도록 설정함.
	@RequestMapping("viewOne")
	public ModelAndView viewOne(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {

		String notice_no = "";
		String goBackURL = "";
		String searchType = "";
		String searchWord = "";

		// === noticeViewList 에서 redirect 해온것을 처리해주기(구분지어주는 것임) 시작 === //
		Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

		if (inputFlashMap != null) { // redirect 되어서 넘어온 데이터가 있다라면

			@SuppressWarnings("unchecked") // 경고표시를 하지말라는 뜻이다.
			Map<String, String> redirect_map = (Map<String, String>) inputFlashMap.get("redirect_map"); // redirect 되어서 넘어온 데이터
			// "redirect_map" 값은 /noticeViewList 에서 redirectAttr.addFlashAttribute("키", 밸류값); 을 할때 준 "키" 이다.
			// "키" 값을 주어서 redirect 되어서 넘어온 데이터를 꺼내어 온다.
			// "키" 값을 주어서 redirect 되어서 넘어온 데이터의 값은 Map<String, String> 이므로 Map<String, String> 으로 casting 해준다.

			notice_no = redirect_map.get("notice_no");
			searchType = redirect_map.get("searchType");

			try {
				searchWord = URLDecoder.decode(redirect_map.get("searchWord"), "UTF-8"); // db에서 조회하기 위해 다시 decode 해옴 // 한글데이터가 포함되어 있으면 반드시 한글로 복구해 주어야 한다.그래야만 db 에서 조회할 수 있다.
				goBackURL = URLDecoder.decode(redirect_map.get("goBackURL"), "UTF-8");
			} catch (UnsupportedEncodingException e) {// UnsupportedEncodingException는 UTF-8 이렇게 제대로 써야한다는 말이다.
				e.printStackTrace();
			}
			// === noticeViewList에서 redirect 해온것을 처리해주기(구분지어주는 것임) 끝 === //
		}

		else { // redirect 되어서 넘어온 데이터가 아닌 경우
			notice_no = request.getParameter("notice_no");
			goBackURL = request.getParameter("goBackURL");
			searchType = request.getParameter("searchType");
			searchWord = request.getParameter("searchWord");

			if (searchType == null) { // searchType 값이 없을 경우 공백으로 초기화. null 값은 더이상 들어갈 수 없게 설정
				searchType = "";
			}

			if (searchWord == null) { // searchType 값이 없을 경우 공백으로 초기화. null 값은 더이상 들어갈 수 없게 설정
				searchWord = "";
			}
		} // end of if ~ else --------------------------------------

		HttpSession session = request.getSession();
		EmployeeVO loginuser = (EmployeeVO) session.getAttribute("loginuser");

		String login_userid = null; // 로그인을 하지 않은 상태에서도 글목록을 볼 수 있다.(로그인유저 session을 가져왔지만 로그인을 해야 볼수 있다는 설정을 하진 않았기 때문에)

		if (loginuser != null) { // 로그인한 사용자가 있을 경우
			login_userid = loginuser.getEmp_id(); // 사용자의 사원번호를 가져와서 login_userid에 대입
			// login_userid 는 로그인 되어진 사용자의 emp_id 이다.
		}
		try {
			Integer.parseInt(notice_no);

			mav.addObject("goBackURL", goBackURL);

			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("notice_no", notice_no);
			paraMap.put("login_userid", login_userid);
			paraMap.put("searchType", searchType);
			paraMap.put("searchWord", searchWord);

			NoticeBoardVO noticeboardvo = null; // 조회해온 글 1개의 정보를 NoticeBoardVO에 담아온다.

			if ("yes".equals((String) session.getAttribute("readCountPermission"))) {
				// 글목록보기 페이지에서 제목을 클릭한 후 들어온 경우
				// readCountPermission의 value 값이 String 타입이기 떄문에 (String)
				// 공지사항 게시판의 글목록보기인 /GroupWare_noticeBoard 페이지를 클릭한 다음에 특정글을 조회해온 경우이다.

				noticeboardvo = service.getView(paraMap);
				// service 단에서 조회수 1증가 처리
				// 글 조회수 증가와 함께 공지사항 게시판의 글 1개를 조회를 해오는 것

				// 중요함!! session 에 저장된 readCountPermission 을 삭제한다.(새로고침해도 조회수 증가가 되지 않도록 방지)
				session.removeAttribute("readCountPermission");
			}

			else { // 그 외의 경로로 들어온 경우
				noticeboardvo = service.getNoticeBoardView_no_increase_readCount(paraMap);
				// 글 조회수 증가는 없고 단순히 공지사항 게시판의 글 1개만 조회를 해오는 것

				if (noticeboardvo == null) { // 다른 경로로 들어왔을 경우 조회할 글이 없다면 ViewOne 뷰단 페이지로 이동
					return mav;
				}

				// 또는 redirect 해주기(조회수 올라가는것을 방지하기 위해서 새로고침하면 다시 GroupWare_noticeBoard 페이지로 가게 설정 이전글보기 다음글보기 때문에 위에것으로 진행)
				/*
				   	mav.setViewName("redirect:/board/GroupWare_noticeBoard"); return mav;
				*/

			}
			mav.addObject("noticeboardvo", noticeboardvo);
			mav.addObject("paraMap", paraMap);

			mav.setViewName("board/ViewOne");

		} catch (NumberFormatException e) {
			// NumberFormatException(-21~-21억까지만 받아줌)해도 터무니없는 숫자를 입력하면 GroupWare_noticeBoard 로 돌아옴
			mav.setViewName("redirect:/board/GroupWare_noticeBoard");
		}

		return mav;
	}

//	 === 이전글보기, 다음글보기를 클릭할때 글조회수 증가를 하기 위한 용도이다. ===	
	@PostMapping("noticeViewList")
	public ModelAndView noticeViewList(ModelAndView mav, @RequestParam(defaultValue = "") String notice_no,
			@RequestParam(defaultValue = "") String goBackURL, @RequestParam(defaultValue = "") String searchType,
			@RequestParam(defaultValue = "") String searchWord, HttpServletRequest request,
			RedirectAttributes redirectArr) {// RedirectAttributes 는 Redirect 시 넘겨줄 데이터를 의미한다.

		try {
			searchWord = URLEncoder.encode(searchWord, "UTF-8");
			goBackURL = URLEncoder.encode(goBackURL, "UTF-8");

			// System.out.println("~~~ noticeViewList 의 searchWord : " + searchWord); ~~~ noticeViewList 의 searchWord : %EC%9D%B4%EC%97%B0%EC%A7%84

			// System.out.println("~~~ noticeViewList 의 searchWord : " + URLDecoder.decode(searchWord,"UTF-8")); // URL인코딩 되어진 한글을 원래 한글모양으로 되돌려주는 것임. ~~~ noticeViewList 의 searchWord : 이연진 로 다시 돌아온다.

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// System.out.println("~~~ noticeViewList 의 searchWord : " + searchWord); ~~~ noticeViewList 의 searchWord : 이연진 <= 이렇게 나오면 안되고 http://localhost:9090/myspring/board/list?searchType=name&searchWord=%EC%9D%B4%EC%97%B0%EC%A7%84 이렇게 나와야하기 떄문에 위의 과정( URLEncoder.encode(searchWord, "UTF-8");)을 해준다.

		HttpSession session = request.getSession();
		session.setAttribute("readCountPermission", "yes");

		// ==== redirect(GET방식임) 시 데이터를 넘길때 GET 방식이 아닌 POST 방식처럼 데이터를 넘기려면
		// RedirectAttributes 를 사용하면 된다. 시작 ==== //
		Map<String, String> redirect_map = new HashMap<>();
		redirect_map.put("notice_no", notice_no);
		redirect_map.put("goBackURL", goBackURL);
		redirect_map.put("searchType", searchType);
		redirect_map.put("searchWord", searchWord);

		redirectArr.addFlashAttribute("redirect_map", redirect_map); // POST 방식처럼 데이터를 넘기는 방법.
		// redirectAttr.addFlashAttribute("키", 밸류값); 으로 사용하는데 오로지 1개의 데이터만 담을 수 있으므로 여러개의 데이터를 담으려면 Map 을 사용해야 한다.

		mav.setViewName("redirect:/board/viewOne"); // 원래는 ("redirect:/board/viewOne?notice_no=&goBackURL=&searchType=&searchWord=")인것임.
		// 실제로 redirect:/board/viewOne 은 POST 방식이 아닌 GET 방식이다.
		// ==== redirect(GET방식임) 시 데이터를 넘길때 GET 방식이 아닌 POST 방식처럼 데이터를 넘기려면 RedirectAttributes 를 사용하면 된다. 끝 ==== //

		return mav;

	}

	@GetMapping("GroupWare_Edit/{notice_no}")
	public ModelAndView GroupWare_Edit(HttpServletRequest request, HttpServletResponse response, ModelAndView mav,
			@PathVariable String notice_no) {
		try {
			Long.parseLong(notice_no);

			// 글 수정해야할 글 1개 내용 가져오기
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("notice_no", notice_no);

			NoticeBoardVO noticeboardvo = service.getNoticeBoardView_no_increase_readCount(paraMap);
			// 글 조회수 증가는 없고 단순히 글 1개만 조회를 해오는 것

//		System.out.println("확인용: noticeboardList" + noticeboardvo.getNotice_subject());
//		System.out.println("확인용: noticeboardList" + noticeboardvo.getNotice_content());
//		System.out.println("확인용: noticeboardList" + noticeboardvo.getAtnotice_orgfilename());

			if (noticeboardvo == null) { // 공지사항 게시판에 글이 없다면
				mav.setViewName("redirect:/board/GroupWare_noticeBoard");
			} else { // 글이 있다면

				HttpSession session = request.getSession();
				EmployeeVO loginuser = (EmployeeVO) session.getAttribute("loginuser");

				if (loginuser.getEmp_id().equals(noticeboardvo.getFk_emp_id())) {
					// 자신의 글을 수정할 경우 가져온 1개 글을 글수정할 폼이 있는 view 단으로 보내준다.

					mav.addObject("noticeboardvo", noticeboardvo);
					mav.setViewName("board/EditHome");
				} else { // 자신의 글이 아닌 다른 사람의 글을 수정할 경우
					mav.addObject("message", "다른 사용자의 글은 수정이 불가합니다.!!");
					mav.addObject("loc", "javascript:history.back()");

					mav.setViewName("common/msg");
				}

			}
			return mav;

		} catch (NumberFormatException e) {
			mav.setViewName("redirect:/board/GroupWare_noticeBoard");
			return mav;
		}

	}

	@PostMapping("GroupWare_Edit")
	public ModelAndView GroupWare_Edit(ModelAndView mav, HttpServletRequest request) {

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

		if (n == 1) {
//			System.out.println("글수정 확인용 : " +noticeboardvo.getNotice_no());
			mav.addObject("message", "글수정 성공!!");
			mav.addObject("loc", request.getContextPath() + "/board/viewOne?notice_no=" + noticeboardvo.getNotice_no());
			mav.setViewName("common/msg");
		}

		return mav;
	}

	// === 글을 삭제하는 페이지 요청 === //
	@PostMapping("GroupWare_Del")
	public ModelAndView GroupWare_Del(@RequestParam String notice_no, HttpServletRequest request, ModelAndView mav) {
		int n = 0;

		// 공지사항 게시글에 첨부파일 또는 사진이 있는지 확인하는 메서드 호출
		List<Map<String, String>> attachList = service.ischeckAttachfile(notice_no);

		// === 첨부파일 및 사진이 있을 경우 처리 ===
		if (attachList != null && !attachList.isEmpty()) { // 리스트가 null이 아니고, 비어 있지 않은 경우만 실행
			HttpSession session = request.getSession();
			String root = session.getServletContext().getRealPath("/");

			String filepath = root + "resources" + File.separator + "files"; // 첨부파일 경로
			String photo_upload_path = root + "resources" + File.separator + "photo_upload"; // 사진파일 경로

			for (Map<String, String> attach : attachList) {
				// 첨부파일 삭제 처리
				String filename = attach.get("atnotice_filename");
				if (filename != null && !filename.isEmpty()) { // 파일명이 존재하는 경우에만 삭제
					try {
						fileManager.doFileDelete(filename, filepath);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// 사진파일 삭제 처리
				String photofilename = attach.get("photofilename");
				if (photofilename != null && !photofilename.isEmpty()) { // photofilename이 null이 아닌 경우 처리
					if (photofilename.contains("/")) { // 여러 개의 이미지가 있는 경우
						String[] arr_photofilename = photofilename.split("/");

						for (String photo : arr_photofilename) { // 여러 개의 사진 삭제
							try {
								fileManager.doFileDelete(photo, photo_upload_path);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else { // 사진이 하나만 존재하는 경우
						try {
							fileManager.doFileDelete(photofilename, photo_upload_path);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

			// 모든 파일 삭제 후 DB에서 해당 글 삭제
			n = service.noticeBoardDel(notice_no);
		} else {
			// 첨부파일이 없는 경우 바로 글 삭제
			n = service.noticeBoardDel(notice_no);
		}

		// === 삭제 결과 처리 ===
		if (n == 1) {
			mav.addObject("message", "글 삭제 성공!!");
			mav.addObject("loc", request.getContextPath() + "/board/GroupWare_noticeBoard");
			mav.setViewName("common/msg");
		}

		return mav;
	}

	// === 첨부파일 다운로드 받기 === //
	@GetMapping("download") // viewOne.jsp 에서 notice_no를 같이 보내줬음
	public void download(HttpServletRequest request, HttpServletResponse response) {

		String notice_no = request.getParameter("notice_no");
		// 첨부파일이 있는 글번호

//		System.out.println("notice_no ===>>" + notice_no);
		/*
		  	첨부파일이 있는 글번호에서 20250207130548558161004053900.jpg 처럼 이러한 fileName 값을 DB 에서 가져와야 한다. 또한 orgFilename 값도 DB 에서 가져와야 한다.(다운로드할 때는 orgFilename 이걸로 다운받아야하기 때문에.)
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

			NoticeBoardVO noticeboardvo = service.getNoticeBoardView_no_increase_readCount(paraMap);

			if (noticeboardvo == null || (noticeboardvo != null && noticeboardvo.getAtnotice_filename() == null)) { 
				// 존재하지 않는 notice_no가 들어온다면 또는 notice_no는 존재하지만 첨부파일이 없는 경우
				
				out = response.getWriter();
				// out(== 웹브라우저) 은 웹브라우저에 기술하는 대상체라고 생각하자.

				out.println("<script type='text/javascript'>alert('파일다운로드가 불가합니다.'); history.back();</script>"); 
				// html을 넣을 수 있음
				
				return; // 종료
			}

			else { // 정상적으로 다운로드를 할 경우

				String fileName = noticeboardvo.getAtnotice_filename();
				// 숫자로 되어진 파일네임(20250207130548558161004053900.jpg) 이것이 바로 WAS(톰캣) 디스크에 저장된 파일명이다.

				String orgFilename = noticeboardvo.getAtnotice_orgfilename(); // berkelekle단가라포인트03.jpg 다운로드시 보여줄 파일명

				/*
				 	첨부파일이 저장되어있는 WAS(톰캣) 디스크 경로명을 알아와야만 다운로드를 해줄 수 있다. 이 경로는 우리가 파일첨부를 위해서 @PostMapping("GroupWare_noticeWrite") 에서 설정해두었던 경로와 똑같아야한다.
				 */
				// WAS 의 webapp 의 절대경로를 알아와야 한다.
				HttpSession session = request.getSession();
				String root = session.getServletContext().getRealPath("/");

				// System.out.println("~~~ 확인용 webapp의 절대경로 ==> " + root); ~~~ 확인용 webapp의 절대경로 ==> C:\NCS\workspace_spring_boot_17\syoffice\src\main\webapp\

				String path = root + "resources" + File.separator + "files"; // 여기로 업로드 해주도록 할 것이다
				/*
				 	File.separator 는 운영체제에서 사용하는 폴더와 파일의 구분자이다.@@@ 운영체제가 Windows 이라면 File.separator 는 "\" 이고, 운영체제가 UNIX, Linux, 매킨토시(맥) 이라면 File.separator 는 "/" 이다.
				*/

				// path 가 첨부파일이 저장될 WAS(톰캣)의 폴더가 된다.
				// System.out.println("~~~ 확인용 path ==> " + path); ~~~ 확인용 webapp의 절대경로 ==> C:\NCS\workspace_spring_boot_17\syoffice\src\main\webapp\resources\files

				// ***** file 다운로드 하기 ***** //
				boolean flag = false; // file 다운로드 성공, 실패인지 여부를 알려주는 용도

				flag = fileManager.doFileDownload(fileName, orgFilename, path, response);
				// file 다운로드 성공시 flag = true,
				// file 다운로드 실패시 flag = false 를 가진다.

				if (!flag) { // file 다운로드 실패시 메시지를 띄운다.
					out = response.getWriter();
					// out(== 웹브라우저) 은 웹브라우저에 기술하는 대상체라고 생각하자.

					out.println("<script type='text/javascript'>alert('파일다운로드가 실패되었습니다.'); history.back();</script>");
				}

			}
		} catch (NumberFormatException | IOException e) { // 숫자가 아니라면

			try {
				out = response.getWriter();
				// out(== 웹브라우저) 은 웹브라우저에 기술하는 대상체라고 생각하자.

				out.println("<script type='text/javascript'>alert('파일다운로드가 불가합니다.'); history.back();</script>"); 
				// html 을 넣을 수 있음
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

	}

	// === 스마트에디터. 글쓰기 또는 글수정시 드래그앤드롭을 이용한 다중 사진 파일 업로드 하기 === //
	@PostMapping("image/multiplePhotoUpload") // photouploader.html 의 form에 post 방식으로 설정해둠
	public void multiplePhotoUpload(HttpServletRequest request, HttpServletResponse response) {

		/*
		 	1. 사용자가 보낸 파일을 WAS(톰캣)의 특정 폴더에 저장해주어야 한다. >>>> 파일이 업로드 되어질 특정 경로(폴더)지정해주기 우리는 WAS 의 webapp/resources/photo_upload 라는 폴더로 지정해준다.
		*/

		// WAS 의 webapp 의 절대경로를 알아와야 한다.
		HttpSession session = request.getSession();
		String root = session.getServletContext().getRealPath("/");
		String path = root + "resources" + File.separator + "photo_upload";
		// path 가 첨부파일들을 저장할 WAS(톰캣)의 폴더가 된다.

		// System.out.println("~~~ 확인용 path => " + path); ~~~ 확인용 path => C:\NCS\workspace_spring_boot_17\syoffice\src\main\webapp\resources\photo_upload

		File dir = new File(path);
		if (!dir.exists()) { // photo_upload파일이 없다면 자동으로 만들어준다.
			dir.mkdirs();
		}

		try {
			String filename = request.getHeader("file-name"); // 파일명(문자열)을 받는다 - 일반 원본파일명 네이버 스마트에디터를 사용한 파일업로드시 싱글파일업로드와는 다르게 멀티파일업로드는 파일명이 header 속에 담겨져 넘어오게 되어있다.

			/*
			 * [참고] HttpServletRequest의 getHeader() 메소드를 통해 클라이언트의 정보를 알아올 수 있다.
			 * 
			 * request.getHeader("referer"); // 접속 경로(이전 URL)
			 * request.getHeader("user-agent"); // 클라이언트 사용자의 시스템 정보
			 * request.getHeader("User-Agent"); // 클라이언트 브라우저 정보
			 * request.getHeader("X-Forwarded-For"); // 클라이언트 ip 주소
			 * request.getHeader("host"); // Host 네임 예: 로컬 환경일 경우 ==> localhost:9090
			 */

			// System.out.println(">>> 확인용 filename ==> " + filename); >>> 확인용 filename ==> berkelekle%EC%8B%AC%ED%94%8C%EB%9D%BC%EC%9A%B4%EB%93%9C01.jpg

			InputStream is = request.getInputStream(); // is는 네이버 스마트 에디터를 사용하여 사진첨부하기 된 이미지 파일명임.

			// === 사진 이미지 파일 업로드 하기 === //
			String newFilename = fileManager.doFileUpload(is, filename, path);
			// System.out.println("### 확인용 newFilename ==> " + newFilename); ### 확인용 newFilename ==> 20250210165110401783618706200.jpg

			// === 웹브라우저 상에 업로드 되어진 사진 이미지 파일 이미지를 쓰기 === //
			String ctxPath = request.getContextPath(); //

			String strURL = "";
			strURL += "&bNewLine=true&sFileName=" + newFilename;
			strURL += "&sFileURL=" + ctxPath + "/resources/photo_upload/" + newFilename;

			PrintWriter out = response.getWriter();
			out.print(strURL);

			// 글쓰기 또는 글수정시 이미지를 추가한 후 이미지를 마우스로 클릭하면 사진 사이즈 조절 레이어 에디터가 보여진다. 여기서 사진의 크기를 조절하면 된다.!!
			// 사진의 크기 조절은 네이버 스마트에디터 소스속에 자바스크립트로 구현이 되어진 것이다. Ctrl + Alt + Shit + L 하여 검색어에 '사진 사이즈 조절 레이어' 를 하면 보여진다.

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

// =================================== 공지사항 임시저장글 시작 ========================================== //		

	// 공지사항 테이블에 임시저장글 status=2 로 저장시켜주는 URL(임시저장은 파일첨부가 불가하므로 글과 사진만 저장해주도록한다. 임시저장을 눌렀을 경우 파일)
	@PostMapping("noticeTemporaryBoard") // 임시저장하기를 누르면 writeHome.jsp 폼이 이 경로로 들어온다. // 공지사항 테이블에 임시저장글 status=2 로 저장시켜주는 URL
	@ResponseBody
	public Map<String, Object> noticeTemporaryBoard(MultipartHttpServletRequest mrequest,   // 파일첨부시 사용(공지사항 테이블에 담은 후 공지사항 파일첨부 테이블에 insert 해주는 역할)
													NoticeBoardVO noticevo) { 				// 데이터 담아주기

		int n = 0;
		Integer notice_no = 0; // 공지사항게시판 글번호 알아오는 용도(파일첨부시 필요)
		Map<String, Object> response = new HashMap<>();

		String fk_emp_id = mrequest.getParameter("fk_emp_id");
		String notice_subject = mrequest.getParameter("subject");
		String notice_content = mrequest.getParameter("content");

		noticevo.setFk_emp_id(fk_emp_id);
		noticevo.setNotice_subject(notice_subject);
		noticevo.setNotice_content(notice_content);

		// === 파일첨부가 있는 경우 작업 시작 === //
		MultipartFile attach = noticevo.getAttach();
		if (!attach.isEmpty()) {// 파일첨부가 있는 경우라면

			HttpSession session = mrequest.getSession();
			String root = session.getServletContext().getRealPath("/"); // 절대경로 알아오기

			String path = root + "resources" + File.separator + "files"; // 업로드 해줄 파일경로

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

//		    System.out.println("확인용 => originalFilename" +originalFilename);

				// 첨부되어진 파일을 업로드 하는 것이다.
				newFileName = fileManager.doFileUpload(bytes, originalFilename, path);
//		    System.out.println(newFileName);

				// === NoticeBoardVO noticevo 에 fileName 값과 orgFilename 값과 fileSize 값을 넣어주기 === //
				noticevo.setAtnotice_filename(newFileName);
				// WAS(톰캣)에 저장된 파일명(2025020709291535243254235235234.png)

				noticevo.setAtnotice_orgfilename(originalFilename); // 실제파일명도 함께 넣어줘야함 게시판 페이지에서 첨부된 파일(강아지.png)을 보여줄 때 사용.
																	// 또한 사용자가 파일을 다운로드 할때 사용되어지는 파일명으로 사용.

				fileSize = attach.getSize(); // 첨부파일의 크기(단위는 byte)
				noticevo.setAtnotice_filesize(String.valueOf(fileSize));

			} catch (Exception e) {
				e.printStackTrace();
			}

		} // end of if(!attach.isEmpty())----------------------------

		// === 파일 첨부가 있는 글쓰기 또는 파일첨부가 없는 글쓰기로 나뉘어서 service를 호출하기 시작 === //
		if (attach.isEmpty()) {// 파일 첨부가 없는 경우라면
			n = service.noticeTempBoardWrite(noticevo);// 파일첨부가 없을 경우에 공지사항 테이블에 임시저장상태(status =2 ) 로 insert 해주는 메소드
		} else {// 파일 첨부가 있는 경우라면 일단 상태 2로 게시글을 등록한 후 등록된 게시글의 번호로 공지사항 파일첨부 테이블에 파일 데이터를 넣어준다.

			n = service.noticeTempBoardWrite(noticevo);// 파일첨부가 있을 경우에 공지사항 테이블에 임시저장상태(status =2 ) 로 insert 해주는 메소드

			// 글번호 조회해오기 //
			notice_no = service.notice_no();// 등록된 게시글의 마지막 글번호를 알아오는 용도(공지사항 파일첨부 테이블에 데이터 넣을시 notice_no(외래키)로 해당 게시글에 파일첨부 하기 위해 필요)

			noticevo.setNotice_no(String.valueOf(notice_no));

			// === 파일 첨부가 있는 글쓰기 또는 파일첨부가 없는 글쓰기로 나뉘어서 service를 호출하기 끝 === //
			try {
				n = service.TempNoticeWrite_withFile(noticevo); // 파일첨부가 있을 경우에 공지사항 파일첨부 테이블에 해당 게시글번호를 가진 게시글에 insert 해주는 메소드

			} catch (Exception e) {
				response.put("success", false);
				response.put("message", "오류가 발생했습니다: " + e.getMessage());
			}

		}

		if (n == 1) { // insert 성공
//	         System.out.println("임시저장 insert 성공!");
			response.put("success", true);
			response.put("message", "임시저장이 완료되었습니다.");
		} else { // insert 실패
			response.put("success", false);
			response.put("message", "임시저장에 실패했습니다.");
		}

		return response;
	}

	// 공지사항 임시저장글 목록 조회
	@GetMapping("noticeTemporaryBoardList")
	@ResponseBody
	public Map<String, Object> noticeTemporaryBoardList(HttpServletRequest request) {

		String fk_emp_id = request.getParameter("fk_emp_id");
//		System.out.println("확인용 =>" + fk_emp_id);

		Map<String, Object> response = new HashMap<>();

		try {
			// 페이징 파라미터 받기
			int currentPage = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));
			int itemsPerPage = Integer.parseInt(request.getParameter("itemsPerPage") == null ? "5" : request.getParameter("itemsPerPage"));

			// 시작과 끝 row 계산
			int startRow = (currentPage - 1) * itemsPerPage + 1;
			int endRow = currentPage * itemsPerPage;

			// 파라미터 Map 생성
			Map<String, Object> paraMap = new HashMap<>();
			paraMap.put("fk_emp_id", request.getParameter("fk_emp_id"));
			paraMap.put("startRow", startRow);
			paraMap.put("endRow", endRow);

			// 공지사항 게시판 임시저장 글 목록 조회(페이징처리 포함)
			List<Map<String, String>> temporaryList = service.noticeTemporaryBoardList(paraMap);

			// 페이징 처리를 위한 loginuser가 작성한 공지사항 전체 임시저장 글 수 조회
			int totalCount = service.getTemporaryBoardCount(fk_emp_id);

			// 응답 데이터 설정
			response.put("data", temporaryList);
			response.put("totalCount", totalCount);
			response.put("currentPage", currentPage);
			response.put("itemsPerPage", itemsPerPage);

		} catch (Exception e) {
			response.put("error", e.getMessage());
		}

		return response;
	}

	// 공지사항 임시저장글 삭제하기
	@GetMapping("deleteNoticeTemporary")
	public ModelAndView deleteNoticeTemporary(@RequestParam String notice_no
											, HttpServletRequest request
											, ModelAndView mav) {

//	System.out.println("확인용 :: notice_no" + notice_no);

		int n = 0;

		// 공지사항 게시글에 첨부파일 또는 사진이 있는지 확인하는 메서드 호출
		List<Map<String, String>> attachList = service.ischeckAttachfile(notice_no);

		// === 첨부파일 및 사진이 있을 경우 처리 ===
		if (attachList != null && !attachList.isEmpty()) { // 리스트가 null이 아니고, 비어 있지 않은 경우만 실행
			HttpSession session = request.getSession();
			String root = session.getServletContext().getRealPath("/");

			String filepath = root + "resources" + File.separator + "files"; // 첨부파일 경로
			String photo_upload_path = root + "resources" + File.separator + "photo_upload"; // 사진파일 경로

			for (Map<String, String> attach : attachList) {
				// 첨부파일 삭제 처리
				String filename = attach.get("atnotice_filename");
				if (filename != null && !filename.isEmpty()) { // 파일명이 존재하는 경우에만 삭제
					try {
						fileManager.doFileDelete(filename, filepath);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// 사진파일 삭제 처리
				String photofilename = attach.get("photofilename");
				if (photofilename != null && !photofilename.isEmpty()) { // photofilename이 null이 아닌 경우 처리
					if (photofilename.contains("/")) { // 여러 개의 이미지가 있는 경우
						String[] arr_photofilename = photofilename.split("/");

						for (String photo : arr_photofilename) { // 여러 개의 사진 삭제
							try {
								fileManager.doFileDelete(photo, photo_upload_path);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else { // 사진이 하나만 존재하는 경우
						try {
							fileManager.doFileDelete(photofilename, photo_upload_path);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

			// 모든 파일 삭제 후 DB에서 해당 글 삭제
			n = service.noticeBoardDel(notice_no);
		} else {
			// 첨부파일이 없는 경우 바로 글 삭제
			n = service.noticeBoardDel(notice_no);
		}

		// === 삭제 결과 처리 ===
		if (n == 1) {
			mav.addObject("message", "임시글 삭제 성공!!");
			mav.addObject("loc", request.getContextPath() + "/board/GroupWare_Write");
			mav.setViewName("common/msg");
		}

		return mav;
	}
	
	
	// 공지사항 임시저장글 불러오기
	@GetMapping("getNoticeTemporary")
	@ResponseBody
	public Map<String, Object> getNoticeTemporary(NoticeBoardVO noticevo) {
		
//		System.out.println("확인용 :  "+ noticevo.getNotice_no());
//		System.out.println("확인용 :  "+ noticevo.getFk_emp_id());
		
	    Map<String, Object> result = new HashMap<>();
	    
	    try {
	        NoticeBoardVO notice = service.getTemporaryNotice(noticevo); //공지사항 임시저장글 조회하기 DB에서 데이터 가져오기(필요한 데이터 => 글제목, 글내용)

	        if (notice != null) { // 공지사항 임시저장글이 있다면
	            result.put("success", true);
	            result.put("data", notice);
	        } else {
	            result.put("success", false);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        result.put("success", false);
	    }
	    
	    return result;
	}

	
	// 뷰단 페이지에서 넘겨받은 데이터를 가지고 공지사항 게시판 글쓰기 "DB에 update 하는 과정"
	@PostMapping("updateNoticeTemporary") // 위의 뷰단 페이지에서 작성 위치를 공지사항 게시판에 두고 "등록" 버튼을 누르면 폼 데이터를 post 방식으로 넘겨받음.
	@ResponseBody
	public Map<String, Object> updateNoticeTemporary(NoticeBoardVO noticevo
												  , @RequestParam(defaultValue = "1") String board_show // 부서게시판 게시글 공개설정 여부
												  , @RequestParam String boardLocation 				    // 게시글 등록 위치
												  , MultipartHttpServletRequest mrequest) { 			// 파일첨부를 받아올 수 있는 Servlet, HttpServlet은 불가능

		int n = 0;
		Map<String, Object> response = new HashMap<>();
		
		if (boardLocation.equals("notice")) { // 공지사항 게시판에 글을 쓰는 경우라면

			// 넘겨받은 데이터를 NoticeBoardVO에 맞는 필드명으로 바꿔주기
			String notice_no = mrequest.getParameter("notice_no");
			String notice_subject = mrequest.getParameter("subject");
			String notice_content = mrequest.getParameter("content");
			String fk_emp_id = mrequest.getParameter("fk_emp_id");
			String fk_dept_id = mrequest.getParameter("fk_dept_id");

/*			
		    System.out.println("확인용 notice_no : " + notice_no);
		    System.out.println("확인용 notice_subject : " + notice_subject);
		    System.out.println("확인용 notice_content : " + notice_content);
		    System.out.println("확인용 fk_emp_id : " + fk_emp_id);
		    System.out.println("확인용 fk_dept_id : " + fk_dept_id);
*/		 

			// NoticeBoardVO에 각 데이터 셋팅하기(NoticeBoardVO에 있는 필드명과 뷰단의 name 이 같았다면 굳이 파라미터로 받아올 필요없이 자동 set 되어진다.)
			noticevo.setNotice_no(notice_no);
			noticevo.setNotice_subject(notice_subject);
			noticevo.setNotice_content(notice_content);
			noticevo.setFk_emp_id(fk_emp_id);
			noticevo.setFk_dept_id(fk_dept_id);
			
// === 공지사항 게시판 글쓰기 (파일첨부가 없는 경우) === //
//				n = service.noticeBoardWrite(noticevo); 
			// 파일첨부가 없는 경우, tbl_notice 가 요구하는 컬럼(사용자 id, 글제목, 글내용 외엔 default)에 맞춰서 값을 넣어준다.

// ===  공지사항 게시판 글쓰기 (파일첨부가 있는 경우) 시작 === //	
			MultipartFile attach = noticevo.getAttach();
			// 뷰단에 파일첨부되는 input의 name값을 attach로 설정해준 후, NoticeBoardVO 에 필드명을 적고
			// getter,setter 해주면
			// 따로 파라미터로 받아오지 않아도 input에 들어간 첨부파일이 NoticeBoardVO attach()로 set 되어진다.

			if (!attach.isEmpty()) { // 파일첨부가 있는 경우라면

				// 뷰단에서 받아온 파일을 WAS(톰캣) 저장해둘 경로 지정하기
				HttpSession session = mrequest.getSession();
				String root = session.getServletContext().getRealPath("/"); // 절대경로 알아오기

				String path = root + "resources" + File.separator + "files"; // 업로드 해줄 파일경로

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

//					   System.out.println("확인용 => originalFilename" +originalFilename);

					// 첨부되어진 파일을 path 에 업로드 하는 것이다.
					newFileName = fileManager.doFileUpload(bytes, originalFilename, path);// 업로드해줄 bytes(파일의 내용물),
																							// originalFilename(파일의
																							// 원본명), path(파일을 업로드해줄 경로)
//					   System.out.println(newFileName); // 나노시간으로 바뀐 새로운 파일명

					// NoticeBoardVO noticevo 에 fileName 값과 orgFilename 값과 fileSize 값을 넣어주기
					noticevo.setAtnotice_filename(newFileName);
					// WAS(톰캣)에 저장된 파일명(2025020709291535243254235235234.png)

					noticevo.setAtnotice_orgfilename(originalFilename); // 실제파일명도 함께 set 해주기
					// 게시판 페이지에서 첨부된 파일(강아지.png)을 보여줄 때 사용.
					// 또한 사용자가 파일을 다운로드 할때 사용되어지는 파일명으로 사용.

					fileSize = attach.getSize(); // 첨부파일의 크기(단위는 byte)
					noticevo.setAtnotice_filesize(String.valueOf(fileSize));

				} catch (Exception e) {
					e.printStackTrace();
				}

			} // end of if(!attach.isEmpty())----------------------------

// === 파일 첨부가 있는 글쓰기 또는 파일첨부가 없는 글쓰기로 나뉘어서 service를 호출하기 시작 === //
			if (attach.isEmpty()) { // 파일 첨부가 없는 경우라면
				n = service.updateNoticeTemporary(noticevo); // 공지사항 임시저장글 내용과 status = 2 를 1로 업데이트 해주기
			} else {// 파일 첨부가 있는 경우라면

				n = service.updateNoticeTemporary(noticevo); // 공지사항 임시저장글 내용과 status = 2 를 1로 업데이트 해주기
		    	// System.out.println("확인용 => " + noticevo.getNotice_no());
		    	
				n = service.NoticeWrite_withFile(noticevo); // 알아온 notice_no로 해당 글번호에 맞춰 파일첨부 insert
			}
// === 파일 첨부가 있는 글쓰기 또는 파일첨부가 없는 글쓰기로 나뉘어서 service를 호출하기 끝 === //

		} // end of if(boardLocation.equals("notice"))------------------------------------------------------

		if (n == 1) { // update 성공
			response.put("success", true);
		} else { // update 실패
			response.put("success", false);
		}

		return response; // WriteHome.jsp 의 현재 URL ajax를 실행해준 곳으로 돌아감
	}


	

// =================================== 공지사항 임시저장글 끝 ========================================== //		

//////////////////////////////////////////////////////////// ====== 공지사항 끝 =======//////////////////////////////////////////////////////////////////////////////////







//////////////////////////////////////////////////////////// ======= 부서게시판 시작 ======//////////////////////////////////////////////////////////////////////////////////

	// 뷰단 페이지에서 넘겨받은 데이터를 가지고 부서 게시판 글쓰기 "DB에 insert 하는 과정"
	@PostMapping("GroupWare_deptWrite") // WriteHome.jsp 뷰단 페이지에서 작성 위치를 부서 게시판에 두고 "등록" 버튼을 누르면 폼 데이터를 post 방식으로 넘겨받음.
	public ModelAndView GroupWare_Write(ModelAndView mav, BoardVO boardvo
//									  , @RequestParam(defaultValue="1") String board_show // 공개설정
			, @RequestParam String boardLocation // 게시글 등록 위치
			, MultipartHttpServletRequest mrequest) { // 파일첨부

		// ============== 글쓰기 =============== //
		int n = 0;
		Integer board_no = 0; // 부서게시판 글번호 알아오는 용도(테이블이 다른 부서게시판 파일첨부 테이블에 tbl_board의 고유키인 notice_no을 외래키로
								// 가져와서 같은 글번호를 가진 행에 파일을 첨부해주기 위한 용도)

		if (boardLocation.equals("boardDept")) { // 부서 게시판에 글을 쓰는 경우

//			n = service.deptBoardWrite(boardvo); // 부서 게시판 (파일첨부가 없는)글쓰기 insert

			// === 파일첨부가 있는 경우 작업 시작 === //
			MultipartFile attach = boardvo.getAttach();

			if (!attach.isEmpty()) {// 첨부파일이 있는 경우라면

				HttpSession session = mrequest.getSession();
				String root = session.getServletContext().getRealPath("/"); // 절대경로 알아오기

				String path = root + "resources" + File.separator + "files"; // 업로드 해줄 파일경로

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
//				   System.out.println(newFileName);

					// === BoardVO boardvo 에 fileName 값과 orgFilename 값과 fileSize 값을 넣어주기 === //
					boardvo.setAtboard_filename(newFileName);
					// WAS(톰캣)에 저장된 파일명(2025020709291535243254235235234.png)

					boardvo.setAtboard_orgfilename(originalFilename);
					// 게시판 페이지에서 첨부된 파일(강아지.png)을 보여줄 때 사용.
					// 또한 사용자가 파일을 다운로드 할때 사용되어지는 파일명으로 사용.

					fileSize = attach.getSize(); // 첨부파일의 크기(단위는 byte)
					boardvo.setAtboard_filesize(String.valueOf(fileSize));

				} catch (Exception e) {
					e.printStackTrace();
				}

			} // end of if(!attach.isEmpty())----------------------------

			// === 파일 첨부가 있는 글쓰기 또는 파일첨부가 없는 글쓰기로 나뉘어서 service를 호출하기 시작 === //

			if (attach.isEmpty()) { // 파일 첨부가 없는 경우라면
				n = service.deptBoardWrite(boardvo); // 부서 게시판 (파일첨부가 없는)글쓰기 insert
			} else { // 파일 첨부가 있는 경우라면
				n = service.deptBoardWrite(boardvo); // 부서 게시판 (파일첨부가 없는)글쓰기 insert
				// 우선 모든 데이터를 tbl_board 테이블에 넣은 후 글번호 알아와서 파일첨부테이블에 데이터 따로 insert

				// 글번호 조회해오기 //
				board_no = service.board_no(); // 등록된 게시글의 마지막 글번호를 알아오는 용도(부서게시판 파일첨부 테이블에 데이터 넣을 용도)
				boardvo.setBoard_no(String.valueOf(board_no));

				n = service.BoardWrite_withFile(boardvo); // 부서별 게시판 파일첨부 테이블에 insert(파일 첨부가 있는 경우라면)
//		   	    System.out.println("Insert 결과: " + n);
			}
			// === 파일 첨부가 있는 글쓰기 또는 파일첨부가 없는 글쓰기로 나뉘어서 service를 호출하기 끝 === //

		} // end of
			// if(boardLocation.equals("boardDept"))-------------------------------------------------------

		if (n == 1) {// insert 가 되어졌다면
//	    	System.out.println("파일첨부 insert 성공!");
			mav.setViewName("redirect:/board/GroupWare_Board");
			// /board/GroupWare_Board 페이지로 redirect(페이지이동)
		} else {// insert 가 실패했다면
			mav.setViewName("board/error/add_error");
			// /WEB-INF/views/board/error/add_error.jsp 파일을 생성한다.
		}

		return mav;
	}
// =========================== 부서 게시판 글쓰기 끝 (파일첨부가 있는 글쓰기와 없는 글쓰기 분류) =================================== //	

	// === 부서 게시판 홈페이지(전체 게시글 목록 조회 페이지) db에 있는 데이터들을 select === //
	@GetMapping("GroupWare_Board")
	public ModelAndView requiredLogin_GroupWare_Board(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mav, @RequestParam(defaultValue = "1") String currentShowPageNo,
			@RequestParam(defaultValue = "") String searchType, @RequestParam(defaultValue = "") String searchWord) {

//		System.out.println("currentShowPageNo==>" +currentShowPageNo);
//		System.out.println("searchType==>" +searchType);
//		System.out.println("searchWord==>" +searchWord);

// === 페이징 처리를 안한 검색어가 없는 부서 게시판 전체 글목록 보여주기(일단 부서 게시판 전체 데이터를 추출) === //
		List<Map<String, String>> boardList = null;
//		boardList = service.boardListNoSearch();
		// 부서 게시판 목록에 뿌려줄때 필요한 정보 => (순번,제목,작성자,작성일자,조회수,페이징바에 필요한 데이터들)

		/*
		 * System.out.println("확인용 boardList : " + boardList.get(0).get("name"));
		 * System.out.println("확인용 boardList : " + boardList.get(0).get("board_no"));
		 * System.out.println("확인용 boardList : " + boardList.get(0).get("fk_emp_id"));
		 * System.out.println("확인용 boardList : " + boardList.get(0).get("subject"));
		 * System.out.println("확인용 boardList : " + boardList.get(0).get("content"));
		 * System.out.println("확인용 boardList : " + boardList.get(0).get("view_count"));
		 * System.out.println("확인용 boardList : " +
		 * boardList.get(0).get("board_regDate")); System.out.println("확인용 boardList : "
		 * + boardList.get(0).get("fk_dept_id"));
		 */

		/////////////////////////////////////////////////////
		// === 글조회수(readCount)증가 (DML문 update)는
		// 반드시 목록보기(GroupWare_Board 페이지)에 와서 해당 글제목을 클릭했을 경우에만 증가되고,
		// 웹브라우저에서 새로고침(F5)을 했을 경우에는 증가가 되지 않도록 해야 한다.
		// 이것을 하기 위해서는 session 을 사용하여 처리하면 된다.
		HttpSession session = request.getSession();
		session.setAttribute("readCountPermission", "yes");
		/*
		 * session 에 "readCountPermission" 키값으로 저장된 value값은 "yes" 이다. session 에
		 * "readCountPermission" 키값에 해당하는 value값 "yes"를 얻으려면 반드시 웹브라우저에서 주소창에
		 * "/board/GroupWare_Board" 이라고 입력해야만 얻어올 수 있다.
		 */
		/////////////////////////////////////////////////////
// === 페이징 처리를 안한 검색어가 없는 부서 게시판 전체 글목록 보여주기 === // 

		// 부서별 게시글을 조회해오기 위한 용도
		EmployeeVO loginuser = (EmployeeVO) session.getAttribute("loginuser");
		String fk_dept_id = loginuser.getFk_dept_id();

//		System.out.println("확인용 fk_dept_id : " + fk_dept_id);

		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		paraMap.put("fk_dept_id", fk_dept_id); // 부서별 게시글 총건수 구하는 용도

		// === 페이징 처리를 한 검색어가 있는 전체 글목록 보여주기 === //
		// 먼저, 총 게시물 건수(totalCount) 를 구해와야 한다.
		// 총 게시물 건수(totalCount)는 검색조건이 있을 때와 검색조건이 없을때로 나뉘어진다.
		int totalCount = 0; // 총 게시물 건수
		int sizePerPage = 10; // 한 페이지당 보여줄 게시물 건수
		int totalPage = 0; // 총 페이지 수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)

		int n_currentShowPageNo = 0;// db에 넘어가기 위한 용도

		// 부서 게시판의 부서별 게시글의 총 게시물 건수 (totalCount)
		totalCount = service.getTotalCount(paraMap);
//		System.out.println("~~~ 확인용 totalCount :" + totalCount);

		totalPage = (int) Math.ceil((double) totalCount / sizePerPage);

		try {
			n_currentShowPageNo = Integer.parseInt(currentShowPageNo);

			if (n_currentShowPageNo < 1 || n_currentShowPageNo > totalPage) {
				// get 방식이므로 사용자가 currentShowPageNo 에 입력한 값이 0 또는 음수를 입력하여 장난친 경우
				// get 방식이므로 사용자가 currentShowPageNo 에 입력한 값이 실제 데이터베이스에 존재하는 페이지수 보다 더 큰값을 입력하여
				// 장난친 경우
				n_currentShowPageNo = 1;
			}

		} catch (NumberFormatException e) {
			// get 방식이므로 currentShowPageNo에 입력한 값이 숫자가 아닌 문자를 입력하거나
			// int 범위를 초과한 경우
			n_currentShowPageNo = 1;
		}

		int startRno = ((n_currentShowPageNo - 1) * sizePerPage) + 1; // 시작 행번호
		int endRno = startRno + sizePerPage - 1; // 끝 행번호
//		System.out.println("startRno==>" +startRno);
//		System.out.println("endRno==>" +endRno);

		paraMap.put("startRno", String.valueOf(startRno)); // Oracle 11g 와 호환되는 것으로 사용
		paraMap.put("endRno", String.valueOf(endRno)); // Oracle 11g 와 호환되는 것으로 사용

		paraMap.put("currentShowPageNo", String.valueOf(currentShowPageNo)); // Oracle 12c 이상으로 사용

		boardList = service.boardListSearch_withPaging(paraMap);
		// 글목록 가져오기(페이징 처리 했으며, 검색어가 있는것 또는 검색어가 없는 것 모두 다 포함한 것이다.)

		mav.addObject("boardList", boardList);

		// 검색시 검색조건 및 검색어 유지시키기
		if ("subject".equals(searchType) || "content".equals(searchType) || "subject_content".equals(searchType)
				|| "name".equals(searchType)) {

			// === 이전글제목, 다음글제목 보여줄때 검색이 있는지 여부를 넘겨주기 시작 === //
			paraMap.put("searchType", searchType);
			paraMap.put("searchWord", searchWord);
			// === 이전글제목, 다음글제목 보여줄때 검색이 있는지 여부를 넘겨주기 끝 === //

			mav.addObject("paraMap", paraMap);
		}

		// === 페이지바 만들기 === //
		int blockSize = 10;

		int loop = 1;

		int pageNo = ((n_currentShowPageNo - 1) / blockSize) * blockSize + 1;

		String pageBar = "<ul style='list-style:none;'>";
		String url = "GroupWare_Board";

		// === [맨처음][이전] 만들기 === //
		pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='" + url + "?searchType="
				+ searchType + "&searchWord=" + searchWord + "&currentShowPageNo=1'>[맨처음]</a></li>";

		if (Integer.parseInt(currentShowPageNo) > 1) {
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='" + url + "?searchType="
					+ searchType + "&searchWord=" + searchWord + "&currentShowPageNo="
					+ (Integer.parseInt(currentShowPageNo) - 1) + "'>[이전]</a></li>";
		}

		while (!(loop > blockSize || pageNo > totalPage)) {

			if (pageNo == Integer.parseInt(currentShowPageNo)) {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; border:solid 1px gray; padding:2px 4px;'>"
						+ pageNo + "</li>";
			} else {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='" + url
						+ "?searchType=" + searchType + "&searchWord=" + searchWord + "&currentShowPageNo=" + pageNo
						+ "'>" + pageNo + "</a></li>";
			}

			loop++;
			pageNo++;
		} // end of while()---------------------

		// === [다음][마지막] 만들기 === //
		if (Integer.parseInt(currentShowPageNo) < totalPage) {
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='" + url + "?searchType="
					+ searchType + "&searchWord=" + searchWord + "&currentShowPageNo="
					+ (Integer.parseInt(currentShowPageNo) + 1) + "'>[다음]</a></li>";
		}

		pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='" + url + "?searchType="
				+ searchType + "&searchWord=" + searchWord + "&currentShowPageNo=" + totalPage + "'>[마지막]</a></li>";

		pageBar += "</ul>";

		mav.addObject("pageBar", pageBar);

		///////////////////////////////////////////////////////////////////////////////////////////////////

		mav.addObject("totalCount", totalCount); // 페이징 처리시 보여주는 순번을 나타내기 위한 것임.
		mav.addObject("currentShowPageNo", currentShowPageNo); // 페이징 처리시 보여주는 순번을 나타내기 위한 것임.
		mav.addObject("sizePerPage", sizePerPage); // 페이징 처리시 보여주는 순번을 나타내기 위한 것임.

		String currentURL = MyUtil.getCurrentURL(request); // 현재 페이지

		mav.addObject("goBackURL", currentURL); // 현재 페이지 => 돌아갈 페이지(새로고침기능)

//		request.setAttribute("boardLocation", boardLocation);	
		mav.setViewName("board/BoardHome");

		return mav;
	}

	// 특정글을 조회한 후 "검색된결과목록보기" 버튼을 클릭했을 때 돌아갈 페이지를 만들기 위함.
	// 부서 게시글 목록 조회에서 글 1개를 클릭하면 상세조회할 수 있는 URL // session에 담긴 readCountPermission에
	// 따라 조회수 증가 설정
	// 위의 BoardHome 뷰단에서 글제목을 클릭하면 (board_no(hidden 처리),goBackURL(hidden
	// 처리),searchType,searchWord) 데이터를 post 방식으로 보내주도록 설정함.
	@RequestMapping("BoardviewOne")
	public ModelAndView BoardviewOne(HttpServletRequest request, HttpServletResponse response,
			BoardVO boardvo, ModelAndView mav) {

		String board_no = "";
		String goBackURL = "";
		String searchType = "";
		String searchWord = "";

		// === BoardViewList 에서 redirect 해온것을 처리해주기(구분지어주는 것임) 시작 === //
		Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

		if (inputFlashMap != null) { // redirect 되어서 넘어온 데이터가 있다라면
			@SuppressWarnings("unchecked") // 경고표시를 하지말라는 뜻이다.
			Map<String, String> redirect_map = (Map<String, String>) inputFlashMap.get("redirect_map"); // redirect 되어서 넘어온 데이터
			// "redirect_map" 값은 /BoardViewList 에서 redirectAttr.addFlashAttribute("키", 밸류값);
			// 을 할때 준 "키" 이다.
			// "키" 값을 주어서 redirect 되어서 넘어온 데이터를 꺼내어 온다.
			// "키" 값을 주어서 redirect 되어서 넘어온 데이터의 값은 Map<String, String> 이므로 Map<String,
			// String> 으로 casting 해준다.

//	 			System.out.println("~~ 확인용 board_no : " + redirect_map.get("board_no"));
			// ~~ 확인용 board_no : 70
			// 또는(맨처음글일 경우에 이전글이 없을 경우)
			// ~~ 확인용 board_no :
			
			board_no = redirect_map.get("board_no");
			searchType = redirect_map.get("searchType");

			
			try {
				searchWord = URLDecoder.decode(redirect_map.get("searchWord"), "UTF-8"); // db에서 조회하기 위해 다시 decode 해옴 //
																							// 한글데이터가 포함되어 있으면 반드시 한글로
																							// 복구해 주어야 한다.그래야만 db 에서 조회할
																							// 수 있다.
				goBackURL = URLDecoder.decode(redirect_map.get("goBackURL"), "UTF-8");
			} catch (UnsupportedEncodingException e) {// UnsupportedEncodingException는 UTF-8 이렇게 제대로 써야한다는 말이다.
				e.printStackTrace();
			}
			/*
			 * System.out.println("~~ 확인용 board_no : " + board_no);
			 * System.out.println("~~ 확인용 searchType : " + searchType);
			 * System.out.println("~~ 확인용 searchWord : " + searchWord);
			 * System.out.println("~~ 확인용 goBackURL : " + goBackURL);
			 */

			/*
			 * ~~ 확인용 board_no : 171 ~~ 확인용 searchType : ~~ 확인용 searchWord : ~~ 확인용
			 * goBackURL : /board/GroupWare_Board
			 */

			// === BoardViewList에서 redirect 해온것을 처리해주기(구분지어주는 것임) 끝 === //
		}

		else { // redirect 되어서 넘어온 데이터가 아닌 경우
			board_no = request.getParameter("board_no");
			goBackURL = request.getParameter("goBackURL");
			searchType = request.getParameter("searchType");
			searchWord = request.getParameter("searchWord");

			if (searchType == null) { // searchType 값이 없을 경우 공백으로 초기화. null 값은 더이상 들어갈 수 없게 설정
				searchType = "";
			}

			if (searchWord == null) { // searchType 값이 없을 경우 공백으로 초기화. null 값은 더이상 들어갈 수 없게 설정
				searchWord = "";
			}
		} // end of if ~ else --------------------------------------

		HttpSession session = request.getSession();
		EmployeeVO loginuser = (EmployeeVO) session.getAttribute("loginuser");

		String fk_dept_id = loginuser.getFk_dept_id();
		String fk_emp_id = loginuser.getEmp_id();
		
		String login_userid = null; // 로그인을 하지 않은 상태에서도 글목록을 볼 수 있다.(로그인유저 session을 가져왔지만 로그인을 해야 볼수 있다는 설정을 하진 않았기
									// 떄문에)

		if (loginuser != null) { // 로그인한 사용자가 있을 경우
			login_userid = loginuser.getEmp_id(); // 사용자의 사원번호를 가져와서 login_userid에 대입
			// login_userid 는 로그인 되어진 사용자의 emp_id 이다.

//			System.out.println("확인용 board_no : " + board_no);

//			boardvo.setBoard_no(board_no);
//			boardvo.setFk_emp_id(loginuser.getEmp_id());
			
			//////////////////////////////////////////////////////////
			// 좋아요를 클릭했을 경우 또는 취소했을 경우 상태 유지하기 시작 //
			Map<String, Object> like_paraMap = new HashMap<>();
			boolean isLike = service.isExistLike(board_no,fk_emp_id); // 해당 게시글에 사용자가 좋아요를 눌렀는지 여부 알아오기
			like_paraMap.put("isLike", isLike);

//		    System.out.println("좋아요를 누른 상태 인지 확인 : " + isLike);

			mav.addObject("like_paraMap", like_paraMap);
			// 좋아요를 클릭했을 경우 또는 취소했을 경우 상태 유지하기 끝 //
			//////////////////////////////////////////////////////////

			/////////////////////////////////////////////////////////
			// 해당 게시글의 좋아요 수 알아오기 시작 //
			int n = service.likeCnt(board_no);
			// 해당 게시글의 좋아요 수 알아오기 끝 //
			mav.addObject("n", n);
			/////////////////////////////////////////////////////////

		}
		try {
			Integer.parseInt(board_no);

			mav.addObject("goBackURL", goBackURL);
			
			String redirect_like = request.getParameter("redirect_like");
			
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("board_no", board_no);
			paraMap.put("login_userid", login_userid);
			paraMap.put("searchType", searchType);
			paraMap.put("searchWord", searchWord);
			paraMap.put("fk_dept_id", fk_dept_id); // 이전글, 다음글 보기에서 자신의 부서 게시글만 보이게 하는 용도
			paraMap.put("redirect_like", redirect_like);
			boardvo = null; // 조회해온 글 1개의 정보를 BoardVO에 담아온다.

			if ("yes".equals((String) session.getAttribute("readCountPermission"))) {
				// 글목록보기 페이지에서 제목을 클릭한 후 들어온 경우
				// readCountPermission의 value 값이 String 타입이기 떄문에 (String)
				// 부서 게시판의 글목록보기인 /GroupWare_Board 페이지를 클릭한 다음에 특정글을 조회해온 경우이다.

				boardvo = service.getBoardView(paraMap); // 부서 게시판 글 1개를 조회를 해오는 것
				// service 단에서 조회수 1증가 처리
				// 글 조회수 증가와 함께 부서 게시판의 글 1개를 조회를 해오는 것

				// 중요함!! session 에 저장된 readCountPermission 을 삭제한다.(새로고침해도 조회수 증가가 되지 않도록 방지)
				session.removeAttribute("readCountPermission");
			}

			else { // 그 외의 경로로 들어온 경우
				boardvo = service.getBoardView_no_increase_readCount(paraMap);
				// 글 조회수 증가는 없고 단순히 부서 게시판의 글 1개만 조회를 해오는 것

//		System.out.println("previousseq" + boardvo.getPreviousseq());
//		System.out.println("previoussubject"+ boardvo.getPrevioussubject()); 
//		System.out.println("board_no"+ boardvo.getBoard_no());
//		System.out.println("fk_emp_id"+ boardvo.getFk_emp_id());
//		System.out.println("name"+ boardvo.getName());
//		System.out.println("subject"+ boardvo.getSubject());
//		System.out.println("content"+ boardvo.getContent());
//		System.out.println("view_count"+ boardvo.getView_count());
//		System.out.println("board_regdate"+ boardvo.getBoard_regdate());
//		System.out.println("nextseq"+ boardvo.getNextseq());
//		System.out.println("nextsubject"+ boardvo.getNextsubject());


				if (boardvo == null) { // 다른 경로로 들어왔을 경우 조회할 글이 없다면 BoardViewOne 뷰단 페이지로 이동
					return mav;
				}

				// 또는 redirect 해주기(조회수 올라가는것을 방지하기 위해서 새로고침하면 다시 GroupWare_Board 페이지로 가게 설정
				// 이전글보기 다음글보기 때문에 위에것으로 진행)
				/*
				 * mav.setViewName("redirect:/board/GroupWare_Board"); return mav;
				 */

			}
			mav.addObject("boardvo", boardvo);
			mav.addObject("paraMap", paraMap);

			mav.setViewName("board/BoardViewOne");

		} catch (NumberFormatException e) {
			// NumberFormatException(-21~-21억까지만 받아줌)해도 터무니없는 숫자를 입력하면 GroupWare_Board 로 돌아옴
			mav.setViewName("redirect:/board/GroupWare_Board");
		}

		return mav;
	}

// 이전글보기, 다음글보기를 클릭할때 글조회수 증가를 하기 위한 용도이다.
	@PostMapping("BoardViewList")
	public ModelAndView BoardViewList(ModelAndView mav
									,@RequestParam(defaultValue = "") String board_no
									,@RequestParam(defaultValue = "") String goBackURL
									,@RequestParam(defaultValue = "") String searchType
									,@RequestParam(defaultValue = "") String searchWord
									,@RequestParam(defaultValue = "") String fk_emp_id
									,HttpServletRequest request
									,RedirectAttributes redirectArr) {// RedirectAttributes 는 Redirect 시 넘겨줄 데이터를 의미한다.

		try {
			searchWord = URLEncoder.encode(searchWord, "UTF-8");
			goBackURL = URLEncoder.encode(goBackURL, "UTF-8");

			// System.out.println("~~~ BoardViewList 의 searchWord : " + searchWord);
			// ~~~ BoardViewList 의 searchWord : %EC%9D%B4%EC%97%B0%EC%A7%84

			// System.out.println("~~~ BoardViewList 의 searchWord : " +
			// URLDecoder.decode(searchWord,"UTF-8")); // URL인코딩 되어진 한글을 원래 한글모양으로 되돌려주는 것임.
			// ~~~ BoardViewList 의 searchWord : 이연진 로 다시 돌아온다.

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// System.out.println("~~~ BoardViewList 의 searchWord : " + searchWord);
		// ~~~ BoardViewList 의 searchWord : 이연진 <= 이렇게 나오면 안되고
		// http://localhost:9090/myspring/board/GroupWare_Board?searchType=name&searchWord=%EC%9D%B4%EC%97%B0%EC%A7%84
		// 이렇게 나와야하기 떄문에 위의 과정( URLEncoder.encode(searchWord, "UTF-8");)을 해준다.

		HttpSession session = request.getSession();
		session.setAttribute("readCountPermission", "yes");

		// ==== redirect(GET방식임) 시 데이터를 넘길때 GET 방식이 아닌 POST 방식처럼 데이터를 넘기려면
		// RedirectAttributes 를 사용하면 된다. 시작 ==== //
		Map<String, String> redirect_map = new HashMap<>();
		redirect_map.put("board_no", board_no);
		redirect_map.put("goBackURL", goBackURL);
//		System.out.println("goBackURL확인용 : "+goBackURL);
//		System.out.println("board_no확인용 : "+board_no);
		redirect_map.put("searchType", searchType);
		redirect_map.put("searchWord", searchWord);
		redirect_map.put("fk_emp_id", fk_emp_id);

		
		redirectArr.addFlashAttribute("redirect_map", redirect_map); // POST 방식처럼 데이터를 넘기는 방법.
		// redirectAttr.addFlashAttribute("키", 밸류값); 으로 사용하는데 오로지 1개의 데이터만 담을 수 있으므로
		// 여러개의 데이터를 담으려면 Map 을 사용해야 한다.
		mav.setViewName("redirect:/board/BoardviewOne"); 
		// 원래는 ("redirect:/board/BoardviewOne?board_no=&goBackURL=&searchType=&searchWord=")인것임.
		// 실제로 redirect:/board/BoardViewOne 은 POST 방식이 아닌 GET 방식이다.
		// ==== redirect(GET방식임) 시 데이터를 넘길때 GET 방식이 아닌 POST 방식처럼 데이터를 넘기려면
		// RedirectAttributes 를 사용하면 된다. 끝 ==== //

		return mav;

	}

	// 부서 게시판 글수정 페이지
	@GetMapping("GroupWare_BoardEdit/{board_no}")
	public ModelAndView GroupWare_BoardEdit(HttpServletRequest request, HttpServletResponse response, ModelAndView mav,
			@PathVariable String board_no) {

		HttpSession session = request.getSession();
		EmployeeVO loginuser = (EmployeeVO) session.getAttribute("loginuser");

		String fk_dept_id = loginuser.getFk_dept_id();

		try {
			Long.parseLong(board_no);

			// 글 수정해야할 글 1개 내용 가져오기
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("board_no", board_no);
			paraMap.put("fk_dept_id", fk_dept_id);

			BoardVO boardvo = service.getBoardView_no_increase_readCount(paraMap);
			// 글 조회수 증가는 없고 단순히 글 1개만 조회를 해오는 것

//		System.out.println("확인용: boardvo" + boardvo.getSubject());
//		System.out.println("확인용: boardvo" + boardvo.getContent());
//		System.out.println("확인용: boardvo" + boardvo.getAtboard_orgfilename());

			if (boardvo == null) { // 부서 게시판에 글이 없다면
				mav.setViewName("redirect:/board/GroupWare_Board");
			} else { // 글이 있다면
				loginuser = (EmployeeVO) session.getAttribute("loginuser");

				if (loginuser.getEmp_id().equals(boardvo.getFk_emp_id())) {
					// 자신의 글을 수정할 경우
					// 가져온 1개 글을 글수정할 폼이 있는 view 단으로 보내준다.

					mav.addObject("boardvo", boardvo);
					mav.setViewName("board/BoardEditHome");
				} else { // 자신의 글이 아닌 다른 사람의 글을 수정할 경우
					mav.addObject("message", "다른 사용자의 글은 수정이 불가합니다.!!");
					mav.addObject("loc", "javascript:history.back()");

					mav.setViewName("common/msg");
				}

			}
			return mav;

		} catch (NumberFormatException e) {
			mav.setViewName("redirect:/board/GroupWare_Board");
			return mav;
		}

	}

	// 부서 게시판 글수정 완료 페이지
	@PostMapping("GroupWare_BoardEdit")
	public ModelAndView GroupWare_BoardEdit(ModelAndView mav, HttpServletRequest request) {

		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		String board_no = request.getParameter("board_no");
		String fk_bcate_no = request.getParameter("fk_bcate_no");

//		System.out.println("확인용 subject : " + subject);
//		System.out.println("확인용 content : " + content);
//		System.out.println("확인용 board_no : " + board_no);
//		System.out.println("확인용 fk_bcate_no : " + fk_bcate_no);

		BoardVO boardvo = new BoardVO();
		boardvo.setSubject(subject);
		boardvo.setContent(content);
		boardvo.setBoard_no(board_no);
		boardvo.setFk_bcate_no(fk_bcate_no);

		// 부서 게시판 글수정하기
		int n = service.update_board(boardvo);

		if (n == 1) {
			mav.addObject("message", "글수정 성공!!");
			mav.addObject("loc", request.getContextPath() + "/board/BoardviewOne?board_no=" + boardvo.getBoard_no());
			mav.setViewName("common/msg");
		}

		return mav;
	}

	// 부서 게시판 게시글을 삭제하는 페이지 요청
	@PostMapping("GroupWare_BoardDel")
	public ModelAndView GroupWare_BoardDel(@RequestParam String board_no, HttpServletRequest request,
			ModelAndView mav) {
		int n = 0;

		// 부서 게시글에 첨부파일 또는 사진이 있는지 확인하는 메서드 호출
		List<Map<String, String>> attachList = service.ischeckBoardAttachfile(board_no);

		// 첨부파일 및 사진이 있을 경우 처리
		if (attachList != null && !attachList.isEmpty()) { // 리스트가 null이 아니고, 비어 있지 않은 경우만 실행
			HttpSession session = request.getSession();
			String root = session.getServletContext().getRealPath("/");

			String filepath = root + "resources" + File.separator + "files"; // 첨부파일 경로
			String photo_upload_path = root + "resources" + File.separator + "photo_upload"; // 사진파일 경로

			for (Map<String, String> attach : attachList) {
				// 첨부파일 삭제 처리
				String filename = attach.get("atboard_filename");
				if (filename != null && !filename.isEmpty()) { // 파일명이 존재하는 경우에만 삭제
					try {
						fileManager.doFileDelete(filename, filepath);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// 사진파일 삭제 처리
				String photofilename = attach.get("photofilename");
				if (photofilename != null && !photofilename.isEmpty()) { // photofilename이 null이 아닌 경우 처리
					if (photofilename.contains("/")) { // 여러 개의 이미지가 있는 경우
						String[] arr_photofilename = photofilename.split("/");

						for (String photo : arr_photofilename) { // 여러 개의 사진 삭제
							try {
								fileManager.doFileDelete(photo, photo_upload_path);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else { // 사진이 하나만 존재하는 경우
						try {
							fileManager.doFileDelete(photofilename, photo_upload_path);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

			// 모든 파일 삭제 후 DB에서 해당 글 삭제
			n = service.BoardDel(board_no);
		} else {
			// 첨부파일이 없는 경우 바로 글 삭제
			n = service.BoardDel(board_no);
		}

		// === 삭제 결과 처리 ===
		if (n == 1) {
			mav.addObject("message", "글 삭제 성공!!");
			mav.addObject("loc", request.getContextPath() + "/board/GroupWare_Board");
			mav.setViewName("common/msg");
		}

		return mav;

	}

// =================================== 부서 임시저장글 시작 ========================================== //	

	@PostMapping("TemporaryBoard") // 임시저장하기를 누르면 writeHome.jsp 폼이 이 경로로 들어온다. // 부서 테이블에 임시저장글 status=2 로 저장시켜주는 URL(insert)
	@ResponseBody
	public Map<String, Object> TemporaryBoard(MultipartHttpServletRequest mrequest, // 파일첨부시 사용(부서 테이블에 담은 후 부서 파일첨부
																					// 테이블에 insert 해주는 역할)
			BoardVO boardvo) { // 데이터 담아주기

		int n = 0;
		Integer board_no = 0; // 부서게시판 글번호 알아오는 용도(파일첨부시 필요)
		Map<String, Object> response = new HashMap<>();

		String board_show = mrequest.getParameter("board_show");
		String fk_bcate_no = mrequest.getParameter("fk_bcate_no");
		String fk_emp_id = mrequest.getParameter("fk_emp_id");
		String fk_dept_id = mrequest.getParameter("fk_dept_id");
		String subject = mrequest.getParameter("subject");
		String content = mrequest.getParameter("content");

		boardvo.setFk_emp_id(fk_emp_id);
		boardvo.setSubject(subject);
		boardvo.setContent(content);
		boardvo.setFk_dept_id(fk_dept_id);
		boardvo.setFk_bcate_no(fk_bcate_no);
		boardvo.setBoard_show(board_show);

		/*
		 * System.out.println("확인용 ==> " + boardvo.getFk_emp_id());
		 * System.out.println("확인용 ==> " + boardvo.getSubject());
		 * System.out.println("확인용 ==> " + boardvo.getContent());
		 * System.out.println("확인용 ==> " + boardvo.getFk_dept_id());
		 * System.out.println("확인용 ==> " + boardvo.getFk_bcate_no());
		 */

		// === 파일첨부가 있는 경우 작업 시작 === //
		MultipartFile attach = boardvo.getAttach();
		if (!attach.isEmpty()) {// 파일첨부가 있는 경우라면

			HttpSession session = mrequest.getSession();
			String root = session.getServletContext().getRealPath("/"); // 절대경로 알아오기

			String path = root + "resources" + File.separator + "files"; // 업로드 해줄 파일경로

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

//			    System.out.println("확인용 => originalFilename" +originalFilename);

				// 첨부되어진 파일을 업로드 하는 것이다.
				newFileName = fileManager.doFileUpload(bytes, originalFilename, path);
//			    System.out.println(newFileName);

				// === BoardVO boardvo 에 fileName 값과 orgFilename 값과 fileSize 값을 넣어주기 === //
				boardvo.setAtboard_filename(newFileName);
				// WAS(톰캣)에 저장된 파일명(2025020709291535243254235235234.png)

				boardvo.setAtboard_orgfilename(originalFilename); // 실제파일명도 함께 넣어줘야함
				// 게시판 페이지에서 첨부된 파일(강아지.png)을 보여줄 때 사용.
				// 또한 사용자가 파일을 다운로드 할때 사용되어지는 파일명으로 사용.

				fileSize = attach.getSize(); // 첨부파일의 크기(단위는 byte)
				boardvo.setAtboard_filesize(String.valueOf(fileSize));

			} catch (Exception e) {
				e.printStackTrace();
			}

		} // end of if(!attach.isEmpty())----------------------------

		// === 파일 첨부가 있는 글쓰기 또는 파일첨부가 없는 글쓰기로 나뉘어서 service를 호출하기 시작 === //
		if (attach.isEmpty()) {// 파일 첨부가 없는 경우라면
			n = service.TempBoardWrite(boardvo); // 파일첨부가 없을 경우에 부서 테이블에 임시저장상태(status =2 ) 로 insert 해주는 메소드
		} else {// 파일 첨부가 있는 경우라면 일단 상태 2로 게시글을 등록한 후 등록된 게시글의 번호로 부서 파일첨부 테이블에 파일 데이터를 넣어준다.

			n = service.TempBoardWrite(boardvo);// 파일첨부가 있을 경우에 부서 테이블에 임시저장상태(status =2 ) 로 insert 해주는 메소드

			// 글번호 조회해오기 //
			board_no = service.board_no();// 등록된 게시글의 마지막 글번호를 알아오는 용도(부서 파일첨부 테이블에 데이터 넣을시 board_no(외래키)로 해당 게시글에 파일첨부
											// 하기 위해 필요)

			boardvo.setBoard_no(String.valueOf(board_no));

			// === 파일 첨부가 있는 글쓰기 또는 파일첨부가 없는 글쓰기로 나뉘어서 service를 호출하기 끝 === //
			try {
				n = service.TempWrite_withFile(boardvo); // 파일첨부가 있을 경우에 부서 파일첨부 테이블에 해당 게시글번호를 가진 게시글에 insert 해주는 메소드
			} catch (Exception e) {
				response.put("success", false);
				response.put("message", "오류가 발생했습니다: " + e.getMessage());
			}
		}
		if (n == 1) { // insert 성공
			// System.out.println("임시저장 insert 성공!");
			response.put("success", true);
			response.put("message", "임시저장이 완료되었습니다.");
		} else { // insert 실패
			response.put("success", false);
			response.put("message", "임시저장에 실패했습니다.");
		}

		return response; // WriteHome.jsp 의 현재 URL ajax를 실행해준 곳으로 돌아감
	}

	// 부서 게시판 임시저장글 select 페이지(모달창)
	@GetMapping("TemporaryBoardList")
	@ResponseBody
	public Map<String, Object> TemporaryBoardList(HttpServletRequest request) {

		String fk_emp_id = request.getParameter("fk_emp_id");
//		System.out.println("확인용 =>" + fk_emp_id);

		Map<String, Object> response = new HashMap<>();

		try {
			// 페이징 파라미터 받기
			int currentPage = Integer
					.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));
			int itemsPerPage = Integer.parseInt(
					request.getParameter("itemsPerPage") == null ? "5" : request.getParameter("itemsPerPage"));

			// 시작과 끝 row 계산
			int startRow = (currentPage - 1) * itemsPerPage + 1;
			int endRow = currentPage * itemsPerPage;

			/*
			 * System.out.println("확인용 currentPage =>" + currentPage);
			 * System.out.println("확인용  itemsPerPage=>" + itemsPerPage);
			 * System.out.println("확인용  startRow=>" + startRow);
			 * System.out.println("확인용 endRow =>" + endRow);
			 */
			// 파라미터 Map 생성
			Map<String, Object> paraMap = new HashMap<>();
			paraMap.put("fk_emp_id", request.getParameter("fk_emp_id"));
			paraMap.put("startRow", startRow);
			paraMap.put("endRow", endRow);

			// 부서 게시판 임시저장 글 목록 조회(페이징 처리)
			List<Map<String, String>> boardTemporaryList = service.TemporaryBoardList(paraMap);

			// 부서 게시판 전체 임시저장 글 수 조회
			int totalCount = service.getTemporaryCount(fk_emp_id);

			// 응답 데이터 설정
			response.put("data", boardTemporaryList);
			response.put("totalCount", totalCount);
			response.put("currentPage", currentPage);
			response.put("itemsPerPage", itemsPerPage);

		} catch (Exception e) {
			response.put("error", e.getMessage());
		}

		return response;
	}

	// 부서 게시판 임시저장글 삭제하기
	@GetMapping("deleteBoardTemporary")
	public ModelAndView deleteBoardTemporary(@RequestParam String board_no, HttpServletRequest request,
			ModelAndView mav) {

//	System.out.println("확인용 :: board_no" + board_no);

		int n = 0;

		// 부서 게시글에 첨부파일 또는 사진이 있는지 확인하는 메서드 호출
		List<Map<String, String>> attachList = service.ischeckBoardAttachfile(board_no);

		// 첨부파일 및 사진이 있을 경우 처리
		if (attachList != null && !attachList.isEmpty()) { // 리스트가 null이 아니고, 비어 있지 않은 경우만 실행
			HttpSession session = request.getSession();
			String root = session.getServletContext().getRealPath("/");

			String filepath = root + "resources" + File.separator + "files"; // 첨부파일 경로
			String photo_upload_path = root + "resources" + File.separator + "photo_upload"; // 사진파일 경로

			for (Map<String, String> attach : attachList) {
				// 첨부파일 삭제 처리
				String filename = attach.get("atboard_filename");
				if (filename != null && !filename.isEmpty()) { // 파일명이 존재하는 경우에만 삭제
					try {
						fileManager.doFileDelete(filename, filepath);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// 사진파일 삭제 처리
				String photofilename = attach.get("photofilename");

				if (photofilename != null && !photofilename.isEmpty()) { // photofilename이 null이 아닌 경우 처리
					if (photofilename.contains("/")) { // 여러 개의 이미지가 있는 경우
						String[] arr_photofilename = photofilename.split("/");

						for (String photo : arr_photofilename) { // 여러 개의 사진 삭제
							try {
								fileManager.doFileDelete(photo, photo_upload_path);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else { // 사진이 하나만 존재하는 경우
						try {
							fileManager.doFileDelete(photofilename, photo_upload_path);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} // end of if (photofilename != null && !photofilename.isEmpty())----------
			} // end of for()--------------------------------------------

			// 모든 파일 삭제 후 DB에서 해당 글 삭제
			n = service.BoardDel(board_no);
		} else {
			// 첨부파일이 없는 경우 바로 글 삭제
			n = service.BoardDel(board_no);
		}

		// 삭제 결과 처리
		if (n == 1) {
			mav.addObject("message", "임시글 삭제 성공!!");
			mav.addObject("loc", request.getContextPath() + "/board/GroupWare_Write");
			mav.setViewName("common/msg");
		}

		return mav;
	}
	
	// 부서 게시판 임시저장글 불러오기
	@GetMapping("getDeptBoardTemporary")
	@ResponseBody
	public Map<String, Object> getDeptBoardTemporary(BoardVO boardvo) {
		
//		System.out.println("확인용 :  "+ boardvo.getBoard_no());
//		System.out.println("확인용 :  "+ boardvo.getFk_emp_id());
		
	    Map<String, Object> result = new HashMap<>();
	    
	    try {
	        BoardVO board = service.getTemporaryBoard(boardvo); // 부서게시판 임시저장글 조회하기  
	        													// DB에서 데이터 가져오기(필요한 데이터 => 글제목, 글내용)
	        if (board != null) { // 부서게시판의 임시저장글이 있다면
	            result.put("success", true);
	            result.put("data", board);
	        } else {
	            result.put("success", false);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        result.put("success", false);
	    }
	    
	    return result;
	}
	
	
	
	
	
	// 뷰단 페이지에서 넘겨받은 데이터를 가지고 부서 게시판 글쓰기 "DB에 update 하는 과정"
	@PostMapping("updateBoardTemporary") // 위의 뷰단 페이지에서 작성 위치를 부서 게시판에 두고 "등록" 버튼을 누르면 폼 데이터를 post 방식으로 넘겨받음.
	@ResponseBody
	public Map<String, Object> updateBoardTemporary(BoardVO boardvo
												  , @RequestParam(defaultValue = "1") String board_show // 부서게시판 게시글 공개설정 여부
												  , @RequestParam String boardLocation 				    // 게시글 등록 위치
												  , MultipartHttpServletRequest mrequest) { 		    // 파일첨부를 받아올 수 있는 Servlet, HttpServlet은 불가능

		int n = 0;
		Map<String, Object> response = new HashMap<>();
		
		if (boardLocation.equals("boardDept")) { // 부서 게시판에 글을 쓰는 경우라면

			
			String board_no = mrequest.getParameter("board_no");
			String subject = mrequest.getParameter("subject");
			String content = mrequest.getParameter("content");
			String fk_emp_id = mrequest.getParameter("fk_emp_id");
			String fk_dept_id = mrequest.getParameter("fk_dept_id");
			String fk_bcate_no = mrequest.getParameter("fk_bcate_no");

/*		
		    System.out.println("확인용 notice_no : " + board_no);
		    System.out.println("확인용 notice_subject : " + subject);
		    System.out.println("확인용 notice_content : " + content);
		    System.out.println("확인용 fk_emp_id : " + fk_emp_id);
		    System.out.println("확인용 fk_dept_id : " + fk_dept_id);
*/	 

			// BoardVO에 각 데이터 셋팅하기(BoardVO에 있는 필드명과 뷰단의 name 이 같았다면 굳이 파라미터로 받아올 필요없이 자동 set 되어진다.)
		    boardvo.setBoard_no(board_no);
		    boardvo.setSubject(subject);
		    boardvo.setContent(content);
		    boardvo.setFk_emp_id(fk_emp_id);
		    boardvo.setFk_dept_id(fk_dept_id);
		    boardvo.setFk_bcate_no(fk_bcate_no);
		    
// ===  공지사항 게시판 글쓰기 (파일첨부가 있는 경우) 시작 === //	
			MultipartFile attach = boardvo.getAttach();
			// 뷰단에 파일첨부되는 input의 name값을 attach로 설정해준 후, BoardVO 에 필드명을 적고 getter,setter 해주면 따로 파라미터로 받아오지 않아도 input에 들어간 첨부파일이 BoardVO attach()로 set 되어진다.

			if (!attach.isEmpty()) { // 파일첨부가 있는 경우라면

				// 뷰단에서 받아온 파일을 WAS(톰캣) 저장해둘 경로 지정하기
				HttpSession session = mrequest.getSession();
				String root = session.getServletContext().getRealPath("/"); // 절대경로 알아오기

				String path = root + "resources" + File.separator + "files"; // 업로드 해줄 파일경로

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

//					System.out.println("확인용 => originalFilename" +originalFilename);

					// 첨부되어진 파일을 path 에 업로드 하는 것이다.
					newFileName = fileManager.doFileUpload(bytes, originalFilename, path);  // 업로드해줄 bytes(파일의 내용물), originalFilename(파일의 원본명), path(파일을 업로드해줄 경로)
//				    System.out.println(newFileName); // 나노시간으로 바뀐 새로운 파일명

					// BoardVO boardvo 에 fileName 값과 orgFilename 값과 fileSize 값을 넣어주기
					boardvo.setAtboard_filename(newFileName);
					// WAS(톰캣)에 저장된 파일명(2025020709291535243254235235234.png)

					boardvo.setAtboard_orgfilename(originalFilename); // 실제파일명도 함께 set 해주기
					// 게시판 페이지에서 첨부된 파일(강아지.png)을 보여줄 때 사용.
					// 또한 사용자가 파일을 다운로드 할때 사용되어지는 파일명으로 사용.

					fileSize = attach.getSize(); // 첨부파일의 크기(단위는 byte)
					boardvo.setAtboard_filesize(String.valueOf(fileSize));

				} catch (Exception e) {
					e.printStackTrace();
				}

			} // end of if(!attach.isEmpty())----------------------------

// === 파일 첨부가 있는 글쓰기 또는 파일첨부가 없는 글쓰기로 나뉘어서 service를 호출하기 시작 === //
			if (attach.isEmpty()) { // 파일 첨부가 없는 경우라면
				n = service.updateBoardTemporary(boardvo); // 부서 게시판 임시저장글 내용과 status = 2 를 1로 업데이트 해주기
			} else {// 파일 첨부가 있는 경우라면

				n = service.updateBoardTemporary(boardvo); // 부서 게시판 임시저장글 내용과 status = 2 를 1로 업데이트 해주기
		    	
				n = service.BoardWrite_withFile(boardvo); // 알아온 notice_no로 해당 글번호에 맞춰 파일첨부 insert
			}
// === 파일 첨부가 있는 글쓰기 또는 파일첨부가 없는 글쓰기로 나뉘어서 service를 호출하기 끝 === //

		} // end of if(boardLocation.equals("boardDept"))------------------------------------------------------

		if (n == 1) { // update 성공
			response.put("success", true);
		} else { // update 실패
			response.put("success", false);
		}

		return response; // WriteHome.jsp 의 현재 URL ajax를 실행해준 곳으로 돌아감
	}
// =================================== 부서 임시저장글 끝 ========================================== //		

	// === 첨부파일 다운로드 받기 === //
	@GetMapping("Bdownload") // BoardViewOne.jsp 에서 board_no를 같이 보내줬음
	public void Bdownload(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		EmployeeVO loginuser = (EmployeeVO) session.getAttribute("loginuser");

		String fk_dept_id = loginuser.getFk_dept_id();

		String board_no = request.getParameter("board_no");
		// 첨부파일이 있는 글번호

//		System.out.println("board_no ===>>" + board_no);
		/*
		 * 첨부파일이 있는 글번호에서 20250207130548558161004053900.jpg 처럼 이러한 fileName 값을 DB 에서
		 * 가져와야 한다. 또한 orgFilename 값도 DB 에서 가져와야 한다.(다운로드할 때는 orgFilename 이걸로 다운받아야하기
		 * 떄문에.)
		 */

		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("board_no", board_no);
		paraMap.put("fk_dept_id", fk_dept_id);
		paraMap.put("searchType", "");
		paraMap.put("searchWord", "");

		// **** 웹브라우저에 출력하기 시작 **** //
		// HttpServletResponse response 객체는 전송되어져온 데이터를 조작해서 결과물을 나타내고자 할 때 쓰인다.
		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = null;
		// out(== 웹브라우저) 은 웹브라우저에 기술하는 대상체라고 생각하자.

		try {
			Integer.parseInt(board_no);

			BoardVO boardvo = service.getBoardView_no_increase_readCount(paraMap);

			if (boardvo == null || (boardvo != null && boardvo.getAtboard_filename() == null)) { // 존재하지 않는 board_no가
																									// 들어온다면 또는
																									// board_no는 존재하지만
																									// 첨부파일이 없는 경우
				out = response.getWriter();
				// out(== 웹브라우저) 은 웹브라우저에 기술하는 대상체라고 생각하자.

				out.println("<script type='text/javascript'>alert('파일다운로드가 불가합니다.'); history.back();</script>"); // html
																													// 을
																													// 넣을
																													// 수
																													// 있음.
				return; // 종료
			}

			else { // 정상적으로 다운로드를 할 경우

				String fileName = boardvo.getAtboard_filename();
				// 숫자로 되어진 파일네임(20250207130548558161004053900.jpg) 이것이 바로 WAS(톰캣) 디스크에 저장된 파일
				// 명이다.

				String orgFilename = boardvo.getAtboard_orgfilename(); // berkelekle단가라포인트03.jpg 다운로드시 보여줄 파일명

				/*
				 * 첨부파일이 저장되어있는 WAS(톰캣) 디스크 경로명을 알아와야만 다운로드를 해줄 수 있다. 이 경로는 우리가 파일첨부를
				 * 위해서 @PostMapping("GroupWare_noticeWrite") 에서 설정해두었던 경로와 똑같아야한다.
				 */
				// WAS 의 webapp 의 절대경로를 알아와야 한다.
				session = request.getSession();
				String root = session.getServletContext().getRealPath("/");

				// System.out.println("~~~ 확인용 webapp의 절대경로 ==> " + root);
				// ~~~ 확인용 webapp의 절대경로 ==>
				// C:\NCS\workspace_spring_boot_17\syoffice\src\main\webapp\

				String path = root + "resources" + File.separator + "files"; // 여기로 업로드 해주도록 할 것이다
				/*
				 * File.separator 는 운영체제에서 사용하는 폴더와 파일의 구분자이다.@@@ 운영체제가 Windows 이라면
				 * File.separator 는 "\" 이고, 운영체제가 UNIX, Linux, 매킨토시(맥) 이라면 File.separator 는 "/"
				 * 이다.
				 */

				// path 가 첨부파일이 저장될 WAS(톰캣)의 폴더가 된다.
				// System.out.println("~~~ 확인용 path ==> " + path);
				// ~~~ 확인용 webapp의 절대경로 ==>
				// C:\NCS\workspace_spring_boot_17\syoffice\src\main\webapp\resources\files

				// ***** file 다운로드 하기 ***** //
				boolean flag = false; // file 다운로드 성공, 실패인지 여부를 알려주는 용도

				flag = fileManager.doFileDownload(fileName, orgFilename, path, response);
				// file 다운로드 성공시 flag = true,
				// file 다운로드 실패시 flag = false 를 가진다.

				if (!flag) { // file 다운로드 실패시 메시지를 띄운다.
					out = response.getWriter();
					// out(== 웹브라우저) 은 웹브라우저에 기술하는 대상체라고 생각하자.

					out.println("<script type='text/javascript'>alert('파일다운로드가 실패되었습니다.'); history.back();</script>");
				}

			}
		} catch (NumberFormatException | IOException e) { // 숫자가 아니라면

			try {
				out = response.getWriter();
				// out(== 웹브라우저) 은 웹브라우저에 기술하는 대상체라고 생각하자.

				out.println("<script type='text/javascript'>alert('파일다운로드가 불가합니다.'); history.back();</script>"); // html
																													// 을
																													// 넣을
																													// 수
																													// 있음.
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

// =========================== 부서 게시판 댓글쓰기 시작 ================================= //	

	// comment 테이블에 댓글 insert 해주기
	@PostMapping("BoardComment") // BoardViewOne.jsp 에서 등록 버튼을 누르면
	@ResponseBody
	public Map<String,Object> BoardComment(HttpServletRequest request
								   		 , HttpServletResponse response) {
		int n = 0;
		
		String cmt_content = request.getParameter("cmt_content");
		String fk_emp_id = request.getParameter("fk_emp_id");
		String fk_board_no = request.getParameter("fk_board_no");

/*
		System.out.println("확인용 comment : " + cmt_content);
		System.out.println("확인용 fk_emp_id : " + fk_emp_id);
		System.out.println("확인용 fk_board_no : " + fk_board_no);
*/

		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("cmt_content", cmt_content);
		paraMap.put("fk_emp_id", fk_emp_id);
		paraMap.put("fk_board_no", fk_board_no);

		try {
		// comment 테이블에 댓글 insert 해주는 메소드
		n = service.addBoardComment(paraMap);
		}catch(Throwable e) {
			e.printStackTrace();
		}
		
		Map<String,Object> map = new HashMap<>();
		map.put("n", n);
		
		return map;
	}


	 // db에 insert 된 댓글들의 목록을 select 해오기(댓글 페이징 ajax 처리)
	 @GetMapping("CommentList")
	 @ResponseBody 
	 public String CommentList(@RequestParam(defaultValue = "") String fk_board_no,
			  				   @RequestParam(defaultValue = "1") String currentShowPageNo ) {
	 
//		 System.out.println("확인용 board_no : " + fk_board_no);
//		 System.out.println("확인용 currentShowPageNo : " + currentShowPageNo);
		 
	 	 int sizePerPage = 5;
	 
	 	 int startRno = ((Integer.parseInt(currentShowPageNo) - 1) * sizePerPage) + 1; // 시작 행번호 
		 int endRno = startRno + sizePerPage - 1;					 				   //  끝 행번호
		
		 Map<String, String> paraMap = new HashMap<>();
		 paraMap.put("fk_board_no", fk_board_no);									  // 원글에 딸린 댓글이므로 부모글 필요
		
		 paraMap.put("startRno", String.valueOf(startRno));  						  // Oracle 11g 와 호환되는 것으로 사용
		 paraMap.put("endRno", String.valueOf(endRno));								  // Oracle 11g 와 호환되는 것으로 사용
		  
		 paraMap.put("currentShowPageNo", String.valueOf(currentShowPageNo));		  // Oracle 12c 이상으로 사용
		 paraMap.put("sizePerPage", String.valueOf(sizePerPage));					  // Oracle 12c 이상으로 사용
		
		 // 페이징 처리된 댓글 목록 가져오기
		 List<Map<String,Object>> cmtList = service.CommentList_withPaging(paraMap);
		 
		 // 페이징처리를 위한 각 게시글의 총 댓글수 알아오기
		 int totalCount = service.getCmtTotalCount(fk_board_no); // 페이징 처리시 보여주는 순번을 나타내기 위한 것임.(총 댓글수를 조회하기 위한 것임)
//		 System.out.println("확인용 totalCount : " + totalCount);
		 
		 JSONArray jsonArr = new JSONArray(); // []
		
		 if(cmtList != null) {// 댓글의 개수가 있다면
			for(Map<String, Object> commentList :cmtList) {
//				System.out.println("확인용 commentList: " + commentList.get("cmt_content"));
//				System.out.println("확인용 commentList: " + commentList.get("name"));
//				System.out.println("확인용 commentList: " + commentList.get("cmt_regdate"));
				JSONObject jsonObj = new JSONObject(); // {}
				jsonObj.put("commentList", commentList); // 댓글 목록을 가져옴
				jsonObj.put("totalCount", totalCount);   // 페이징 처리시 보여주는 순번을 나타내기 위한 것임.
				jsonObj.put("sizePerPage", sizePerPage); // 페이징 처리시 보여주는 순번을 나타내기 위한 것임.
			
				jsonArr.put(jsonObj);
			}// end of for(CommentVO cmtvo :commentList)-------------
		}
	
		return jsonArr.toString();
	 }
	
 
	 // 댓글 삭제하기
	 @PostMapping("cmtDel")
	 @ResponseBody
	 public Map<String, Object> cmtDel(HttpServletRequest request, CommentVO cmtvo) {
	     
	     Map<String, Object> result = new HashMap<>();
	     
	     String fk_emp_id = request.getParameter("fk_emp_id");
	     String comment_no = request.getParameter("comment_no");

	     cmtvo.setFk_emp_id(fk_emp_id);
	     cmtvo.setComment_no(comment_no);

	     int n = service.cmtDel(cmtvo); // 해당 게시글의 댓글을 삭제하는 메소드

	     if(n == 1) { // 삭제 성공
	         result.put("success", true);
	         result.put("message", "댓글 삭제 성공!");
	     } else { // 삭제 실패
	         result.put("success", false);
	         result.put("message", "댓글 삭제를 실패했습니다.");
	     }

	     return result;
	 }

	 // 댓글 수정 저장하기
	 @PostMapping("cmtSave")
	 @ResponseBody
	 public Map<String, Object> cmtSave(HttpServletRequest request, CommentVO cmtvo) {
	     
	     Map<String, Object> result = new HashMap<>();
	     
	     String fk_board_no = request.getParameter("board_no");
	     String comment_no = request.getParameter("comment_no");
	     String fk_emp_id = request.getParameter("fk_emp_id");
	     String cmt_content = request.getParameter("cmt_content");

	     cmtvo.setFk_board_no(fk_board_no);
	     cmtvo.setFk_emp_id(fk_emp_id);
	     cmtvo.setComment_no(comment_no);
	     cmtvo.setCmt_content(cmt_content);
//	     System.out.println("확인필요 : " +  fk_board_no);
	     int n = service.cmtSave(cmtvo); // 웹에서 수정한 댓글 저장


	     
	     if(n == 1) { // 수정 성공
	    	 result.put("cmtvo", cmtvo);
	         result.put("success", true);
	         result.put("message", "댓글 수정 성공!");
	     } else { // 수정 실패
	         result.put("success", false);
	         result.put("message", "댓글 수정을 실패했습니다.");
	     }

	     return result;
	 }

// =========================== 부서 게시판 댓글쓰기 끝 ================================= //		

// =========================== 부서 게시판 좋아요 누르기 시작 ================================= //	
	@PostMapping("addLike")
	@ResponseBody
	public Map<String, Object> addLike(HttpServletRequest request, BoardVO boardvo) {

		Map<String, Object> result = new HashMap<>();

		String board_no = request.getParameter("fk_board_no");
		String fk_emp_id = request.getParameter("fk_emp_id");

//	 System.out.println("확인용 board_no : " + board_no);
		// 확인용 board_no : 125

//	 System.out.println("확인용 fk_emp_id : " + fk_emp_id);
		// 확인용 fk_emp_id : 9999

		int m = service.addLikeCount(board_no, fk_emp_id);
		// 좋아요 버튼을 최초로 한번 눌렀을 경우 tbl_like 테이블에 insert 해줌과 동시에 하트가 빨간색으로 칠해진다.

		if (m == 1) {// insert 되어졌다면

			/////////////////////////////////////////////////////////
			// 해당 게시글의 좋아요 수 알아오기 시작 //
			int n = service.likeCnt(board_no);
			// 해당 게시글의 좋아요 수 알아오기 끝 //
			/////////////////////////////////////////////////////////
			result.put("n", n);
			result.put("success", true);
		}
		return result;
	}

	// 좋아요 취소하기
	@PostMapping("removeLike")
	@ResponseBody
	public Map<String, Object> removeLike(HttpServletRequest request, BoardVO boardvo) {

		Map<String, Object> result = new HashMap<>();

		String board_no = request.getParameter("fk_board_no");
		String fk_emp_id = request.getParameter("fk_emp_id");

//	 System.out.println("확인용 board_no : " + board_no);
		// 확인용 fk_board_no : 125

//	 System.out.println("확인용 fk_emp_id : " + fk_emp_id);
		// 확인용 fk_emp_id : 9999

		int m = service.removeLikeCount(board_no, fk_emp_id);
		// 좋아요 버튼을 누른 기록이 있을 경우 다시 한번 더 누르면 tbl_like 테이블에서 눌렀던 기록을 delete 해줌과 동시에 하트가 빈
		// 하트로 바뀐다.

		if (m == 1) {// delete 되어졌다면

			/////////////////////////////////////////////////////////
			// 해당 게시글의 좋아요 수 알아오기 시작 //
			int n = service.likeCnt(board_no);
			// 해당 게시글의 좋아요 수 알아오기 끝 //
			/////////////////////////////////////////////////////////
			result.put("n", n);
			result.put("success", true);
		}
		return result;

	}
// =========================== 부서 게시판 좋아요 누르기 끝 ================================= //	
	
	// 내가 좋아요 누른 게시글 보기
	@GetMapping("goLike")
	public ModelAndView goLike(ModelAndView mav, String emp_id, String board_no , @RequestParam(defaultValue = "1") String currentShowPageNo) {
		
//		System.out.println("확인용 emp_id : " + emp_id);
		
// ===  페이징 처리를 한 검색어가 있는 전체 글목록 보여주기 === //
		// 먼저, 총 게시물 건수(totalCount) 를 구해와야 한다.
		// 총 게시물 건수(totalCount)는 검색조건이 있을 때와 검색조건이 없을때로 나뉘어진다.
		int totalCount = 0; 	// 총 게시물 건수
		int sizePerPage = 10; 	// 한 페이지당 보여줄 게시물 건수
		int totalPage = 0; 		// 총 페이지 수

		int n_currentShowPageNo = 0;

		// 내가 좋아요 누른 총 게시물 건수
		totalCount = service.getLikeTotalCount(emp_id);
//		System.out.println("~~~ 확인용 totalCount :" + totalCount);

		totalPage = (int) Math.ceil((double) totalCount / sizePerPage);

		try {
			n_currentShowPageNo = Integer.parseInt(currentShowPageNo); // 현재 페이지는 디폴트 1로 설정
//				System.out.println("확인용 currentShowPageNo => "+currentShowPageNo);
			if (n_currentShowPageNo < 1 || n_currentShowPageNo > totalPage) { // 현재페이지가 1보다 작거나 총 페이지보다 큰 경우 1페이지로 이동한다.
				// get 방식이므로 사용자가 currentShowPageNo 에 입력한 값이 0 또는 음수를 입력하여 장난친 경우
				// get 방식이므로 사용자가 currentShowPageNo 에 입력한 값이 실제 데이터베이스에 존재하는 페이지수 보다 더 큰값을 입력하여 장난친 경우
				n_currentShowPageNo = 1;
			}

		} catch (NumberFormatException e) {
			n_currentShowPageNo = 1; // 장난친 경우 1페이지로 이동
		}

		int startRno = ((n_currentShowPageNo - 1) * sizePerPage) + 1; // 시작 행번호
		int endRno = startRno + sizePerPage - 1;					  // 끝 행번호

//		System.out.println("startRno" +startRno );
//		System.out.println("endRno" +endRno );

		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("startRno", String.valueOf(startRno)); 
		paraMap.put("endRno", String.valueOf(endRno)); 	
		paraMap.put("currentShowPageNo", String.valueOf(currentShowPageNo));
		paraMap.put("emp_id", emp_id); 	
		paraMap.put("board_no", board_no); 
		
		List<BoardVO> boardList = service.goLike_withPaging(paraMap); // 페이징 처리된 내가 좋아요 누른 게시글 목록 조회하기

		mav.addObject("boardList", boardList);

		// === 페이지바 만들기 === //
		int blockSize = 10; // 페이지바를 10개씩 잘라서 보여주겠다. 10개 끝나면 [다음]

		int loop = 1; // 현재 블록에서 페이지번호를 몇 개 출력했는지 세는 변수

		int pageNo = ((n_currentShowPageNo - 1) / blockSize) * blockSize + 1;

		String pageBar = "<ul style='list-style:none;'>";
		String url = "goLike";

		// === [맨처음][이전] 만들기 === //
		pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='" + url + "?emp_id="+emp_id+"&currentShowPageNo=1'>[맨처음]</a></li>";

		if (Integer.parseInt(currentShowPageNo) > 1) {
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='" + url + "?emp_id="+emp_id+"&currentShowPageNo="
					+ (Integer.parseInt(currentShowPageNo) - 1) + "'>[이전]</a></li>";
		}

		while (!(loop > blockSize || pageNo > totalPage)) {

			if (pageNo == Integer.parseInt(currentShowPageNo)) {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; border:solid 1px gray; padding:2px 4px;'>"
						+ pageNo + "</li>";
			} else {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='" + url
						+ "?emp_id="+emp_id+"&currentShowPageNo=" + pageNo
						+ "'>" + pageNo + "</a></li>";
			}

			loop++;
			pageNo++;
		} // end of while()---------------------

		// === [다음][마지막] 만들기 === //

		if (Integer.parseInt(currentShowPageNo) < totalPage) {
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='" + url + "?emp_id="+emp_id+"&currentShowPageNo="
					+ (Integer.parseInt(currentShowPageNo) + 1) + "'>[다음]</a></li>";
		}

		pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='" + url + "?emp_id="+emp_id+"&currentShowPageNo=" + totalPage + "'>[마지막]</a></li>";

		pageBar += "</ul>";

		mav.addObject("pageBar", pageBar);

		///////////////////////////////////////////////////////////////////////////////////////////////////
		mav.addObject("startRno", startRno); // 몇번째 게시물부터 보여줄지 정하기 위한 것임.
		mav.addObject("endRno", endRno); // 몇번째 게시물부터 보여줄지 정하기 위한 것임.
		mav.addObject("totalCount", totalCount); // 페이징 처리시 보여주는 순번을 나타내기 위한 것임.
		mav.addObject("currentShowPageNo", n_currentShowPageNo); // 페이징 처리시 보여주는 순번을 나타내기 위한 것임.
		mav.addObject("sizePerPage", sizePerPage); // 페이징 처리시 보여주는 순번을 나타내기 위한 것임.


		mav.setViewName("board/goLike");
		return mav;
	}
////////////////////////////////////////////////////////////======= 부서게시판 끝 ======//////////////////////////////////////////////////////////////////////////////////

}
