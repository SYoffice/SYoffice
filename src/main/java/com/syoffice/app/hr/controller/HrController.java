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
		// 검색어 및 검색타입이 담긴 파라맵 보내주기
		// 검색이 있는 경우에만 하시면 됩니다.
		mav.addObject("paraMap", paraMap);
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
		
		String mail = paraMap.get("mailId")+paraMap.get("mailAddr");
		paraMap.put("mail", mail);
		paraMap.put("profile_img", file.getOriginalFilename());
		
		// WAS 의 webapp 의 절대경로를 알아와야 한다.
		HttpSession session = mrequest.getSession();
		String root = session.getServletContext().getRealPath("/");	// 최상위 루트 패키지의 경로를 가져온다.
		
	//	System.out.println("~~~ 확인용 webapp 의 절대경로 : "+ root);
	//	~~~ 확인용 webapp 의 절대경로 : C:\Users\User\git\SYoffice\src\main\webapp\
		
		String path = root+"resources"+File.separator+"profile";
		
		// path 가 첨부파일이 저장될 WAS(톰캣)의 폴더가 된다.
	//	System.out.println("~~~ 확인용 path 경로 : "+ path);
	//	~~~ 확인용 path 경로 : C:\Users\User\git\SYoffice\src\main\webapp\resources\profile
		
		byte[] bytes = null;
		// 첨부파일의 내용물을 담는 것
		
		try {
			bytes = file.getBytes();
			
			String originalFilename = file.getOriginalFilename();
			
	//		System.out.println("~~~ 확인용 originalFilename: "+ originalFilename);
		
			// 첨부되어진 파일을 업로드 하는 것이다.
			fileManager.doProfileUpload(bytes, originalFilename, path);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
	
	// 사원 등록 시 사내이메일 중복체크
	@PostMapping("/checkMail")
	@ResponseBody
	public String checkMail(@RequestParam String mail) {
	//	System.out.println("컨트롤러의 mail : " + mail);
		
		String result = service.checkMail(mail);
		// result 는 중복이면 1 중복이 아니면 0을 반환
		
	//	System.out.println("확인용 컨트롤러의 result : " + result);
		
		return result;
	}// end of public String checkMail(@RequestParam("mail") String mail ) -----
	
	
	// 사원 상세정보 조회 페이지
	@GetMapping("employeeDetail")
	public ModelAndView employeeDetail(ModelAndView mav, @RequestParam String emp_id) {
		
		// EmployeeVO 초기화
		EmployeeVO employeevo = null;
		
		// 사원 한명의 정보를 조회
		employeevo = service.employeeDetail(emp_id);
		
		mav.addObject("employeevo", employeevo);
		mav.setViewName("/hr/employeeDetail");
		return mav;
	}// end of public ModelAndView employeeDetail(ModelAndView mav, @RequestParam String emp_id) -----
	
	
	// 사원 정보 수정페이지 요청
	@GetMapping("employeeEdit")
	public ModelAndView employeeEdit(ModelAndView mav, @RequestParam String emp_id) {
		
		// 지점 정보 조회
		List<BranchVO> branchList = service.selectBranchList();
		
		// 부서 정보 조회
		List<DepartmentVO> departmentList =  service.selectDepartmentList();
		
		// 직급 정보 조회
		List<GradeVO> gradeList = service.selectGradeList();
		
		
		// EmployeeVO 초기화
		EmployeeVO employeevo = null;
		
		// 사원 한명의 정보를 조회
		employeevo = service.employeeDetail(emp_id);
		
		mav.addObject("branchList", branchList);
		mav.addObject("departmentList", departmentList);
		mav.addObject("gradeList", gradeList);
		mav.addObject("employeevo", employeevo);
		
		mav.setViewName("/hr/employeeEdit");
		return mav;
	}// end of public String employeeEdit ----
	
	// Post 요청인 경우 사원 정보 변경
	@PostMapping("employeeEdit")
	public ModelAndView employeeEdit(HttpServletRequest request, ModelAndView mav, 
									 MultipartHttpServletRequest mrequest,
									 @RequestParam Map<String, String> paraMap,
									 @RequestParam("profile_img") MultipartFile file) {
		
		String mail = paraMap.get("mailId")+paraMap.get("mailAddr");
		paraMap.put("mail", mail);
		
		String originalProfileImg = paraMap.get("originalProfileImg");
		
		// 프로필 사진을 바꾸지 않은 경우
		if (file.isEmpty()) {
			// 기존에 있던 파일명을 다시 맵에 넣어주기
			paraMap.put("profile_img", originalProfileImg);
		}
		// 프로필 사진을 바꾼 경우
		else {
			// 새로 첨부된 사진명을 맵에 담아준다.
			paraMap.put("profile_img", file.getOriginalFilename());
			
			HttpSession session = mrequest.getSession();
			String root = session.getServletContext().getRealPath("/");
			
			String path = root+"resources"+File.separator+"profile";
			
			byte[] bytes = null;
			
			try {
				bytes = file.getBytes();
				
				String originalFilename = file.getOriginalFilename();

				// 첨부되어진 사진 업로드
				fileManager.doProfileUpload(bytes, originalFilename, path);
				
				// 기존에 있던 프로필 사진은 삭제해준다.
				fileManager.doFileDelete(originalProfileImg, path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// 사원정보 update
		int n = service.employeeEdit(paraMap);
		
		String emp_id = paraMap.get("emp_id");
		
		if(n != 1) {	// 수정에 실패한 경우	
			String message = "사원 정보수정이 실패했습니다.";
			String loc = request.getContextPath() + "/hr/employeeDetail?emp_id="+emp_id; 
			
			mav.addObject("message", message);
			mav.addObject("loc", loc);

			mav.setViewName("common/msg");
		}
		else {	// 수정에 성공한 경우
			
			String message = "사원 정보수정이 완료되었습니다.";
			String loc = request.getContextPath() + "/hr/employeeDetail?emp_id="+emp_id; 
			
			mav.addObject("message", message);
			mav.addObject("loc", loc);

			mav.setViewName("common/msg");
		}
		
		return mav;
	}// end of public ModelAndView employeeEdit( ) -----
	
	
	////////////////////// 부서관리 //////////////////////
	@GetMapping("DepartmentManagement")
	public ModelAndView DepartmentManagement (HttpServletRequest request, ModelAndView mav,
											  @RequestParam(defaultValue = "") String searchType,
											  @RequestParam(defaultValue = "") String searchWord) {
		
		List<DepartmentVO> departmentList = null;
		
		// 검색어 공백제거
		searchWord = searchWord.trim();
		
		Map<String, String> paraMap = new HashMap<>();
		
		// paraMap에 넘어온 검색타입과 검색어 넣어주기
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);		
		
		// 검색만 있는 부서 목록 조회 (부서관리용)
		departmentList = service.DepartmentList(paraMap);
		
		//////////////////////////////////////////////////
		
		////////// 등록하기 모달에서 사용 //////////
		// 부서 추가할 때 필요한 부서장 리스트 가져오기
		List<EmployeeVO> managerList = service.managerList();
		// 부서 추가할 때 필요한 임원진 리스트 가져오기
		List<EmployeeVO> executiveList = service.executiveList();
		// 부서장 리스트 보내주기
		mav.addObject("managerList", managerList);
		// 임원진 리스트 보내주기
		mav.addObject("executiveList", executiveList);
		
		////////// 수정하기 모달에서 사용 ////////// 
		// 모든 부서장이 될 수 있는 사원들 조회
        List<EmployeeVO> allManagerList = service.getAllManagers();
        // 모든 담당임원이 될 수 있는 사원들 조회
        List<EmployeeVO> allExecutiveList = service.getAllExecutives();
        // 모든 부서장이 될 수 있는 사원들 보내주기
        mav.addObject("allManagerList", allManagerList);
        // 모든 담당임원이 될 수 있는 사원들 보내주기
        mav.addObject("allExecutiveList", allExecutiveList);
        
		// 검색이 포함된 부서목록 보내주기
		mav.addObject("departmentList", departmentList);
		mav.addObject("paraMap", paraMap);
		
		mav.setViewName("/hr/DepartmentManagement");
		
		return mav;
	}// end of public String DepartmentManagement () -----
	
	
	// 부서별 사원리스트
	@GetMapping("DepartmentEmployeeInfo")
	@ResponseBody
	public List<EmployeeVO> DepartmentEmployeeInfo(@RequestParam String dept_id, @RequestParam String branch_no) {
		List<EmployeeVO> employeeList = service.DepartmentEmployeeInfo(dept_id, branch_no);
		return employeeList;
	}// end of public List<EmployeeVO> DepartmentMemberInfo(@RequestParam String dept_id) -----
	
	
	// 부서명 중복검사
	@GetMapping("checkDeptName")
	@ResponseBody
	public Map<String, String> checkDeptName(@RequestParam String dept_name) {
	    Map<String, String> paraMap = new HashMap<>();

	    if (dept_name == null || dept_name.trim().isEmpty()) {
	    	paraMap.put("status", "0");
	    	paraMap.put("message", "부서명을 입력하세요.");
	        return paraMap;
	    }
	    // 부서명 중복검사
	    boolean exists = service.checkDeptName(dept_name);

	    if (exists) {
	    	paraMap.put("status", "1");  // 중복 있음
	    } else {
	    	paraMap.put("status", "0");  // 중복 없음
	    }

	    return paraMap;
	}// end of public Map<String, String> checkDeptName(@RequestParam String dept_name) ----- 

	
	// 신규 부서등록하기
	@PostMapping("RegisterDepartment")
	@ResponseBody
	public Map<String, String> RegisterDepartment(@RequestParam Map<String, String> paraMap) {
		String dept_name = paraMap.get("dept_name");
	    String manager_id = paraMap.get("manager_id");
	    String executive_id = paraMap.get("executive_id");

	    Map<String, String> map = new HashMap<>();
	    
	    // 부서명을 입력하지 않았을 때
	    if (dept_name == null || dept_name.trim().isEmpty()) {
	    	map.put("status", "0");
	    	map.put("message", "부서명을 입력하세요.");
	        return map;
	    }
	    
	    // 부서장을 선택하지 않았을 때
	    if (manager_id == null || manager_id.trim().isEmpty()) {
	        map.put("status", "0");
	        map.put("message", "부서장을 선택하세요.");
	        return map;
	    }
	    
	    try {
	        int result = service.RegisterDepartment(dept_name, manager_id, executive_id);
	        
	        // 부서등록에 성공했을 때
	        if (result == 1) {
	        	map.put("status", "1");
	        	map.put("message", "부서 추가 완료");
	        }
	        // 부서등록에 실패했을 때
	        else {
	        	map.put("status", "0");
	        	map.put("message", "부서 추가 실패");
	        }
	    } catch (Exception e) {
	    	map.put("status", "0");
	        map.put("message", "서버 오류 발생: " + e.getMessage());
	    }

	    return map;
	}// end of public Map<String, String> RegisterDepartment(@Reque stParam Map<String, String> paraMap) -----
	
	
	// 특정 부서 정보 가져오기
	@GetMapping("getDepartmentInfo")
	@ResponseBody
	public Map<String, Object> getDepartmentInfo(@RequestParam Map<String, String> paraMap) {
		String dept_id = paraMap.get("dept_id");
		String dept_name = paraMap.get("dept_name");
		String branch_no = paraMap.get("branch_no");
		
		Map<String, String> map = new HashMap<>();
		map.put("dept_id", dept_id);
		map.put("dept_name", dept_name);
		map.put("branch_no", branch_no);
		
	    Map<String, Object> responseMap = new HashMap<>();

	    try {
	    	// 1. 해당 부서의 정보 조회
	        DepartmentVO dept_info = service.getDepartmentInfo(map);
	        if (dept_info == null) {
	        	responseMap.put("status", "0");
	        	responseMap.put("message", "부서 정보를 찾을 수 없습니다.");
	            return responseMap;
	        }

	        // 2️. 응답 데이터 설정
	        responseMap.put("status", "1");
	        responseMap.put("data", dept_info);
	    } catch (Exception e) {
	    	responseMap.put("status", "0");
	    	responseMap.put("message", "서버 오류 발생: " + e.getMessage());
	    }

	    return responseMap;
	}// end of public Map<String, Object> getDepartmentInfo(@RequestParam int dept_id) -----
	
	
	// 부서 정보 수정
	@PostMapping("editDepartment")
	@ResponseBody
	public Map<String, String> editDepartment(@RequestParam Map<String, String> paraMap) {
		
		// 결과물을 담을 Map 생성
		Map<String, String> map = new HashMap<>();
		
		try {
			// 부서 정보 수정
	        Integer result = service.editDepartment(paraMap);
	        
	        // 정보 수정에 실패했을 때
	        if (result == 0) {
	        	map.put("status", "0");
	        	map.put("message", "부서 수정에 실패했습니다");
	            return map;
	        }
	        // 정보 수정에 성공했을 때
	        else {
		        map.put("status", "1");
		        map.put("message", "부서 수정에 성공했습니다");
	        }
	        
	    } catch (Exception e) {
	    	map.put("status", "0");
	    	map.put("message", "서버 오류 발생: " + e.getMessage());
	    }

	    return map;

	}// end of public Map<String, String> editDepartment(@RequestParam Map<String, String> paraMap) -----
	
	// 부서 삭제
	@PostMapping("/deleteDepartment")
	@ResponseBody
	public Map<String, String> deleteDepartment(@RequestParam String dept_id) {
        Map<String, String> paraMap = new HashMap<>();

        try {
            boolean isDeleted = service.deleteDepartment(dept_id);

            if (isDeleted) {
            	paraMap.put("status", "1");  // 성공
            	paraMap.put("message", "부서가 성공적으로 삭제되었습니다.");
            } else {
            	paraMap.put("status", "0");  // 실패
            	paraMap.put("message", "부서에 소속된 직원이 있어 삭제할 수 없습니다.");
            }

        } catch (Exception e) {
        	paraMap.put("status", "0");
        	paraMap.put("message", "서버 오류로 인해 삭제에 실패했습니다.");
            e.printStackTrace();
        }

        return paraMap;
    }// end of public Map<String, String> deleteDepartment(@RequestParam String deptId) -----
	
}// end of public class HrController ----- 
