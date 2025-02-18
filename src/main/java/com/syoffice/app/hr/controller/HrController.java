package com.syoffice.app.hr.controller;


import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.syoffice.app.branch.domain.BranchVO;
import com.syoffice.app.common.FileManager;
import com.syoffice.app.common.PagingDTO;
import com.syoffice.app.department.domain.DepartmentVO;
import com.syoffice.app.employee.domain.EmployeeVO;
import com.syoffice.app.grade.domain.GradeVO;
import com.syoffice.app.hr.service.HrService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;



/* 인사관리(HR) 컨트롤러 선언 */
@Controller
@RequestMapping("/hr/*")
public class HrController {
	
	@Autowired	// Type 에 따라 자동적으로 Bean 을 주입해준다.
	private HrService service;
	
	// 파일업로드 및 파일다운로드를 해주는 FileManager 클래스 의존객체 주입하기(DI : Dependency Injection) //
	@Autowired
	private FileManager fileManager;
	
	// 인사관리 페이지 요정
	@GetMapping("hrIndex")
	public ModelAndView hrIndex(HttpServletRequest request, ModelAndView mav,
			 					@RequestParam(defaultValue = "") String searchType,
			 					@RequestParam(defaultValue = "") String searchWord,
			 					@RequestParam(defaultValue = "1") String currentShowPageNo) {
		
		List<EmployeeVO> employeeList = null;
		
		// 검색어 공백제거
		searchWord = searchWord.trim();
		
		Map<String, String> paraMap = new HashMap<>();
		
		// paraMap에 넘어온 검색타입과 검색어 넣어주기
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		
//		System.out.println("~~~ 확인용 searchType : " + searchType);
//		System.out.println("~~~ 확인용 searchWord : " + searchWord);
		
		//////////////////////////////////////////////////////////
		////////////////////// 페이징 처리 /////////////////////////
		int curPage = 1;
		
		try {
			// 존재하지 않는 페이지를 URL을 통해 접근했을 때 예외처리
			curPage = Integer.parseInt(request.getParameter("curPage"));
		} catch (NumberFormatException e) {
			// 1페이지로 보내기
			curPage = 1;
		}
		
		PagingDTO pagingDTO = new PagingDTO();	// Pagind DTO 초기화
		pagingDTO.setPageSize(5);	// 보여줄 페이지 수
		
		int totalRowCount = service.getTotalCount(paraMap);	// 총 사원 수 알아오기
		
		// URL을 통해 비정상적인 접근을 한 경우 1페이지로 보내기
		if(curPage > totalRowCount || curPage < 1) {
			curPage = 1;
		}
		
		pagingDTO.setRowSizePerPage(10);	// 한 페이지에 보여줄 행 수 가져오기
		
		pagingDTO.setCurPage(curPage);	// 현재 페이지 가져오기
		pagingDTO.setTotalRowCount(totalRowCount);	// 전체 행 개수 가져오기
		pagingDTO.pageSetting();	// 페이징 계산 공식
		
		// 시작행 번호
		String startRno = String.valueOf(pagingDTO.getFirstRow());
		// 마지막 행번호
		String endRno = String.valueOf(pagingDTO.getLastRow());
		
		// paraMap 에 넣어주기
		paraMap.put("startRno", startRno);
		paraMap.put("endRno", endRno);
		
		// 페이징과 검색이 포함된 사원목록 가져오기
		// 각자 불러오고 싶은 리스트 메소드로 수정하시면 됩니다.
		employeeList = service.selctEmployeeList(paraMap);
		
		// 페이징DTO 보내주기
		mav.addObject("pagingDTO", pagingDTO);
		
		//////////////////////페이징 처리 /////////////////////////
		//////////////////////////////////////////////////////////		
		
		// 페이징과 검색이 포함된 사원목록 보내주기
		mav.addObject("employeeList", employeeList);
		
		mav.setViewName("hr/hrIndex");
		
		return mav;
	}// end of public ModelAndView hrIndex(HttpServletRequest request, ModelAndView mav, @RequestParam(defaultValue = "") String searchType, @RequestParam(defaultValue = "") String searchWord, @RequestParam(defaultValue = "1") String currentShowPageNo) -----
	
	
	// GET 요청인 경우 신규 사원 추가 페이지로 이동	
	@GetMapping("employeeRegister")
	public ModelAndView employeeRegister(HttpServletRequest request, ModelAndView mav) {
		
		// 지점 정보 조회
		List<BranchVO> branchList = service.selectBranchList();
		
		// 부서 정보 조회
		List<DepartmentVO> departmentList =  service.selectDepartmentList();
		
		// 직급 정보 조회
		List<GradeVO> gradeList = service.selectGradeList();
		
		
		mav.addObject("branchList", branchList);
		mav.addObject("departmentList", departmentList);
		mav.addObject("gradeList", gradeList);
		mav.setViewName("/hr/employeeRegister");
		
		return mav;
	}
	
	// POST 요청인 경우 신규 사원 데이터 처리
	@PostMapping("employeeRegister")
	public ModelAndView employeeRegister(HttpServletRequest request, 
										 ModelAndView mav, 
										 @RequestParam Map<String, String> paraMap,
										 MultipartHttpServletRequest mrequest,
										 @RequestParam("profile_img") MultipartFile file) {
		
		paraMap.put("profile_img", file.getOriginalFilename());
		
		// WAS 의 webapp 의 절대경로를 알아와야 한다.
		HttpSession session = mrequest.getSession();
		String root = session.getServletContext().getRealPath("/");	// 최상위 루트 패키지의 경로를 가져온다.
		
	//	System.out.println("~~~ 확인용 webapp 의 절대경로 : "+ root);
	//	~~~ 확인용 webapp 의 절대경로 : C:\Users\User\git\SYoffice\src\main\webapp\
		
		String path = root+"images"+File.separator+"profile";
	
		/* 	File.separator 는 운영체제에서 사용하는 폴더와 파일의 구분자이다.
        	운영체제가 Windows 이라면 File.separator 는  "\" 이고,
	        운영체제가 UNIX, Linux, 매킨토시(맥) 이라면  File.separator 는 "/" 이다. 
		 */
		
		// path 가 첨부파일이 저장될 WAS(톰캣)의 폴더가 된다.
	//	System.out.println("~~~ 확인용 path 경로 : "+ path);
//		~~~ 확인용 path 경로 : C:\Users\User\git\SYoffice\src\main\webapp\images\profile
		
		byte[] bytes = null;
		// 첨부파일의 내용물을 담는 것
		
		long filesize = 0;
		// 첨부파일의 크기
		
		try {
			bytes = file.getBytes();
			// 첨부파일의 내용물을 읽어 오는 것
			
			String originalFilename = file.getOriginalFilename();
			// attach.getOriginalFilename()은 첨부파일의 파일명을 가져온다. 파일명 예(강아지.png)
			
	//		System.out.println("~~~ 확인용 originalFilename: "+ originalFilename);
		
			// 첨부되어진 파일을 업로드 하는 것이다.
			fileManager.doProfileUpload(bytes, originalFilename, path);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String fileName = file.getOriginalFilename();
		
		// 신규 사원정보 insert
		int n = service.employeeRegister(paraMap);
		
		if(n != 1) {	// 가입에 실패한 경우
			String message = "사원등록에 실패했습니다.";
			String loc = request.getContextPath() + "/hr/hrIndex"; 
			
			mav.addObject("message", message);
			mav.addObject("loc", loc);

			mav.setViewName("common/msg");
		}
		else {	// 가입에 성공한 경우
			String message = "사원등록에 성공했습니다.";
			String loc = request.getContextPath() + "/hr/hrIndex"; 
			
			mav.addObject("message", message);
			mav.addObject("loc", loc);

			mav.setViewName("common/msg");
		}
		
		return mav;
	}// end of public ModelAndView employeeRegister(HttpServletRequest request, ModelAndView mav, RequestParam Map<String, String> paraMap) -----
	
	
}// end of public class HrController ----- 
