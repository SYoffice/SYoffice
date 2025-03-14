package com.syoffice.app.dataroom.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
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

import com.syoffice.app.common.FileManager;
import com.syoffice.app.common.PagingDTO;
import com.syoffice.app.dataroom.domain.DataVO;
import com.syoffice.app.dataroom.domain.DatacategoryVO;
import com.syoffice.app.dataroom.service.DataroomService;
import com.syoffice.app.employee.domain.EmployeeVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value="/dataroom/*")
public class DataroomController {

	@Autowired  // Type에 따라 알아서 Bean 을 주입해준다.
	private DataroomService service;
	
	// 파일업로드 및 파일다운로드를 해주는 FileManager 클래스 의존객체 주입하기(DI : Dependency Injection) //
	@Autowired
	private FileManager fileManager;
	
	
	@GetMapping("index")
	public ModelAndView dataroom(HttpServletRequest request, ModelAndView mav,
	                             @RequestParam(value = "data_cateno", required = false) String data_cateno,
	                             @RequestParam(defaultValue = "") String searchType,
	                             @RequestParam(defaultValue = "") String searchWord,
	                             @RequestParam(defaultValue = "1") int curPage) {

	    //  폴더 목록 조회
	    List<DatacategoryVO> categoryList = service.getDataCategoryList();

	    //  data_cateno가 없으면 첫 번째 카테고리를 자동 선택
	    if (data_cateno == null || data_cateno.trim().isEmpty()) {
	        if (!categoryList.isEmpty()) {
	            data_cateno = categoryList.get(0).getData_cateno(); // 첫 번째 카테고리 자동 선택
	        }
	        else {
	            data_cateno = "1"; // 기본값 (업무 양식)
	        }

	        return new ModelAndView("redirect:/dataroom/index?data_cateno=" + data_cateno);
	    }

	    //  검색 타입 기본값 설정
	    if (searchType == null || searchType.trim().isEmpty()) {
	        searchType = "data_orgfilename";
	    }

	    // 검색 조건 저장
	    Map<String, String> paraMap = new HashMap<>();
	    paraMap.put("searchType", searchType);
	    paraMap.put("searchWord", searchWord.trim());
	    paraMap.put("data_cateno", data_cateno);

	    
	    // 총 파일 개수 가져오기
	    int totalRowCount = service.getTotalFile(paraMap);

	    PagingDTO pagingDTO = new PagingDTO(curPage, totalRowCount);
	    
	    pagingDTO.setCurPage(curPage);	// 현재 페이지 가져오기
	    pagingDTO.setRowSizePerPage(6); // 한 페이지당 6개 설정
	    pagingDTO.pageSetting();  // 페이징 계산 공식
	    
	    if(curPage > totalRowCount || curPage < 1) {
			curPage = 1;
		}
	    
	    
	    paraMap.put("startRno", String.valueOf(pagingDTO.getFirstRow())); // 시작행 번호
	    paraMap.put("endRno", String.valueOf(pagingDTO.getLastRow()));    // 끝행 번호

	    // 파일 목록 조회
	    List<DataVO> fileList = service.getFileList(paraMap);

	    // 선택된 폴더명 설정
	    String selectedCategoryName = "업무양식";
	    for (DatacategoryVO category : categoryList) {
	        if (category.getData_cateno().equals(data_cateno)) {
	            selectedCategoryName = category.getData_catename();
	            break;
	        }
	    }

	   
	    mav.addObject("fileList", fileList);
	    mav.addObject("selectedCategoryName", selectedCategoryName);
	    mav.addObject("categoryList", categoryList);
	    mav.addObject("pagingDTO", pagingDTO);
	    mav.addObject("paraMap", paraMap);
	    mav.setViewName("/dataroom/index");

	    return mav;
	}

	
	// 새폴더 추가
	@PostMapping("addCategory")
	public String addCategory(@RequestParam("categoryName") String categoryName, DatacategoryVO category) {
		
	    category.setData_catename(categoryName); // 폴더이름 설정
	    
	    // 새폴더 추가
	    int n = service.insertCategory(category);
	    
	    if (n == 1) {
	    	return "redirect:/dataroom/index";
	    }
	    
	    return "redirect:/dataroom/index";
	}
	
	
	// 폴더 삭제
	@PostMapping("deleteCategory")
	public String deleteCategory(@RequestParam("data_cateno") String data_cateno, HttpServletRequest request) {
		
		// 선택된 폴더에 있는 모든 파일 가져오기
	    List<DataVO> fileList = service.getFileListByCategory(data_cateno);

	    // 선택된 폴더 이름 조회
	    String data_catename = service.getCategoryName(data_cateno);

	    // WAS의 webapp 절대 경로 가져오기
	    String root = request.getSession().getServletContext().getRealPath("/");
	    String folderPath;

	    if ("카달로그".equals(data_catename)) {
	        folderPath = root + "resources" + File.separator + "library" + File.separator + "catalog" + File.separator;
	    } else {
	        folderPath = root + "resources" + File.separator + "library" + File.separator + "document" + File.separator;
	    }
	    
	    
	    // 폴더 안의 파일 삭제
	    for (DataVO fileData : fileList) { 
	        String filePath = folderPath + fileData.getData_filename();  // 삭제할 파일의 경로
	        File file = new File(filePath);
	        file.delete(); // 서버에서 파일 삭제
	    }
	    
	    
	    // 해당 폴더 및 파일 삭제
	    int n = service.deleteCategoryWithFiles(data_cateno);

	    if (n == 1) {
	        return "redirect:/dataroom/index";
	    }

	    return "redirect:/dataroom/index";  
	}
	
	
	// 파일 업로드
	@PostMapping("uploadFile")
	public String uploadFile(HttpServletRequest request,
							 MultipartHttpServletRequest mrequest,
	                         @RequestParam("uploadFile") MultipartFile uploadFile,
	                         @RequestParam(value = "data_cateno", required = false) String data_cateno,
	                         HttpSession session) throws Exception {

	    // 로그인한 사용자 정보 가져오기(fk_emp_id 알아와야 업로드 가능)
	    EmployeeVO loginUser = (EmployeeVO) session.getAttribute("loginuser");

	    //  현재 선택된 폴더 이름 조회
	    String data_catename = service.getCategoryName(data_cateno);
	    
	    // WAS 의 webapp 의 절대경로를 알아와야 한다.
 		HttpSession msession = mrequest.getSession();
 		String root = msession.getServletContext().getRealPath("/");	// 최상위 루트 패키지의 경로를 가져온다.
	    String uploadPath;
	    String newFileName = "";
	    
	    if ("카달로그".equals(data_catename)) {
	        uploadPath = root + "resources" + File.separator + "library" + File.separator + "catalog" + File.separator;
	    } 
	    else {
	        uploadPath = root + "resources" + File.separator + "library" + File.separator + "document" + File.separator;
	    }

	    byte[] bytes = null;
		// 첨부파일의 내용물을 담는 것(byte타입으로 받아야함)

		long fileSize = 0;
		// 첨부파일의 크기
		
		try {
			bytes = uploadFile.getBytes();
			// 첨부파일의 내용물을 읽어오는 것

			String originalFilename = uploadFile.getOriginalFilename();
			// attach.getOriginalFilename() 이 첨부파일명의 파일명(예: 강아지.png)이다.

		   // System.out.println("확인용 => originalFilename" +originalFilename);

			// 첨부되어진 파일을 path 에 업로드 하는 것이다.
			newFileName = fileManager.doFileUpload(bytes, originalFilename, uploadPath);// 업로드해줄 bytes(파일의 내용물), originalFilename(파일의 원본명), path(파일을 업로드해줄 경로)
		    // System.out.println(newFileName); // 나노시간으로 바뀐 새로운 파일명


	    // 서버에 파일 저장
	    File File = new File(uploadPath + newFileName);
	    uploadFile.transferTo(File);
	    
	    //파일 정보 설정
	    DataVO fileData = new DataVO();
	    fileData.setFk_emp_id(loginUser.getEmp_id()); // 로그인한 사용자의 사원번호
	    fileData.setFk_data_cateno(data_cateno); // 현재 선택된 폴더 번호
	    fileData.setData_filename(newFileName); // 그냥 파일
	    fileData.setData_orgfilename(uploadFile.getOriginalFilename()); // 원본 파일명
	    fileData.setData_filesize(String.valueOf(uploadFile.getSize())); // 파일 크기

	    // 파일 정보 저장
	    service.insertFile(fileData);

	    
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	    // 업로드 후 해당 폴더로 이동
	    return "redirect:/dataroom/index?data_cateno=" + data_cateno;
		
	}


	
	
	// 파일 다운로드
	@GetMapping("downloadFile")
	public void downloadFile(@RequestParam("data_no") String data_no,
	                         HttpServletRequest request, HttpServletResponse response) throws Exception {

	    // 파일 정보 가져오기
	    DataVO fileData = service.getFile(data_no);

	    if (fileData != null) { // 파일이 있다면
	    	
	        // 현재 선택된 폴더 이름 가져오기
	        String data_catename = service.getCategoryName(fileData.getFk_data_cateno());

	        // WAS 의 webapp 의 절대경로를 알아와야 한다.
	        String root = request.getSession().getServletContext().getRealPath("/");
	        String downloadPath;

	       
	        if ("카달로그".equals(data_catename)) {
	            downloadPath = root + "resources" + File.separator + "library" + File.separator + "catalog" + File.separator;
	        } else {
	            downloadPath = root + "resources" + File.separator + "library" + File.separator + "document" + File.separator;
	        }


	        // 파일 다운로드
	        fileManager.doFileDownload(fileData.getData_filename(), fileData.getData_orgfilename(), downloadPath, response);

	        
	    } 
	}
	
	
	// 파일 삭제
	@PostMapping("deleteFile")
	public String deleteFile(@RequestParam("data_no") String data_no,
	                         HttpServletRequest request, HttpSession session) throws Exception {


	    // 삭제할 파일 정보 가져오기
	    DataVO fileData = service.getFile(data_no);
	    if (fileData != null) {

	        // 현재 선택된 폴더 이름(data_catename) 조회
	        String data_catename = service.getCategoryName(fileData.getFk_data_cateno());

	        // 파일 경로 설정 (카달로그인 경우 다르게 설정)
	        String root = request.getSession().getServletContext().getRealPath("/");
	        String filePath;

	        if ("카달로그".equals(data_catename)) {
	            filePath = root + "resources" + File.separator + "library" + File.separator + "catalog" + File.separator + fileData.getData_filename();
	        } 
	        
	        else {
	            filePath = root + "resources" + File.separator + "library" + File.separator + "document" + File.separator + fileData.getData_filename();
	        }
	        
	        
	        // 파일 삭제
	        File file = new File(filePath);
	        if (file.exists()) {
	            file.delete(); // 서버에서 파일 삭제
	        }

	        // 파일 정보 삭제
	        service.deleteFile(data_no);
	    }

	    // 삭제 후 해당 폴더 페이지로 이동
	    return "redirect:/dataroom/index?data_cateno=" + fileData.getFk_data_cateno();
	}

	
}
