package com.syoffice.app.kpi.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.syoffice.app.kpi.domain.KpiVO;
import com.syoffice.app.kpi.domain.ResultVO;
import com.syoffice.app.kpi.service.KpiService;

import jakarta.servlet.http.HttpSession;

@Controller
@RestController
@RequestMapping("/api/kpi/*")
public class ApiKpiController {
	
	@Autowired
	private KpiService service;
	
	// === 엑셀 파일 업로드 시 문자열로 만들어주기 위함 === //
	@SuppressWarnings("incomplete-switch")
	private static String cellReader(XSSFCell cell) {
		String value = "";
		CellType ct = cell.getCellType();		// import apache.poi
		if(ct != null) {
			switch(cell.getCellType()) {
            	case FORMULA:	// 수식
            		value = cell.getCellFormula()+"";
            		break;
            	case NUMERIC:	// 숫자
            		value = cell.getNumericCellValue()+"";
            		break;
            	case STRING:	// 문자
	                value = cell.getStringCellValue()+"";
	                break;
            	case BOOLEAN:	// T/F
	                value = cell.getBooleanCellValue()+"";
	                break;
            	case ERROR:		// ERROR
	                value = cell.getErrorCellValue()+"";
	                break;
			}
		}
		return value; 
	}// end of private static String cellReader(XSSFCell cell) ----------------------- 
	
	
	// === 목표실적 등록 시 중복체크 === //
	@PostMapping("duplicateCheck")
	public String kpiDuplicateCheck(KpiVO kpivo) {
		return service.kpiDuplicateCheck(kpivo);
	}// end of public String kpiDuplicateCheck(KpiVO kpivo) ----------------
	
	
	// === 목표실적 삭제 === //
	@DeleteMapping("delete")
	public String kpiDelete(@RequestParam(name = "kpi_no") String kpi_no) {
		return service.kpiDelete(kpi_no);
	}// end of public String kpiDelete(@RequestParam String kpi_no) -------------------- 
	
	
	// == 부서별 연도, 분기 목표실적 조회 == // 
	@GetMapping("deptKpiByYear")
	public List<KpiVO> deptKpiByYearQuarter (@RequestParam Map<String, String> paraMap) {
		return service.deptKpiByYearQuarter(paraMap);
	}// end of public List<KpiVo> deptKpiByYear (@RequestParam Map<String, String> paraMap) ------------------------- 
	
	
	// === 개인실적 엑셀파일 업로드 === //
	@PostMapping("uploadExcelFile")
	public String uploadExcelFile(MultipartHttpServletRequest mtp_request) {
		MultipartFile mtp_excel_file = mtp_request.getFile("excel_file");	// input:file 태그의 name
		
		JSONObject jsonObj = new JSONObject();
		
		if (mtp_excel_file != null) {
			try {
				// == MultipartFile 을 File 로 변환하기 시작 == 
	            // WAS 의 webapp 의 절대경로를 알아와야 한다.
	            HttpSession session = mtp_request.getSession();
	            String root = session.getServletContext().getRealPath("/");
	            String path = root + "resources"+File.separator+"files"; 	// import java.io
	            
	            File excel_file = new File(path + File.separator + mtp_excel_file.getOriginalFilename());	// 경로와 원본파일명을 가진 파일객체 생성 
	            mtp_excel_file.transferTo(excel_file);
	            // == MultipartFile 을 File 로 변환하기 끝 == //
	            
	            OPCPackage opcPackage = OPCPackage.open(excel_file);
	            
	            XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);	// 엑셀파일 불러오기
	            
	            XSSFSheet sheet = workbook.getSheetAt(0);	// 첫번째 시트 불러오기
	            List<Map<String, String>> paraMapList = new ArrayList<>();
	            
	            for(int i=1; i<sheet.getLastRowNum()+1; i++) {	// sheet.getLastRowNum() 은 마지막 행 번호(인덱스) 제목을 제외해야 하므로 1부터 시작
	            	Map<String, String> paraMap = new HashMap<>();
	            	
	            	XSSFRow row = sheet.getRow(i);		// 시트의 i 번째 행 (1~sheet.getLastRowNum()+1)
	            	
	            	if (row == null) {	// 행에 값이 존재하지 않으면 건너 뛴다.
	            		continue;
	            	}
	            	
	            	// 행의 2번째 열(부서번호) - select
	            	XSSFCell cell = row.getCell(1);
	            	if (cell != null) {
	            		paraMap.put("fk_dept_id", String.valueOf(cellReader(cell)));
//	            		System.out.println("부서번호 : "+ String.valueOf(cellReader(cell)));
	            	}
	            	
	            	// 행의 4번째 열(사원번호) - insert
	            	 cell = row.getCell(3);
	            	if (cell != null) {
	            		paraMap.put("fk_emp_id", String.valueOf(cellReader(cell)));
//	            		System.out.println("사원번호 : "+ String.valueOf(cellReader(cell)));
	            	}
	            	
	            	// 행의 6번째 열(실적명) - insert
	            	cell = row.getCell(5);
	            	if (cell != null) {
	            		paraMap.put("result_name", String.valueOf(cellReader(cell)));
//	            		System.out.println("실적명 : "+ String.valueOf(cellReader(cell)));
	            	}
	            	
	            	// 행의 7번째 열(실적발생일) - insert
	            	cell = row.getCell(6);
	            	if (cell != null) {
	            		String result_date = String.valueOf(cellReader(cell));
		            	// 2025-02-17
	            		String kpi_year = result_date.substring(0, 4);	// 실적 발생일 연도
	            		String kpi_month = result_date.substring(5, 7);	// 실적 발생일 월
	            		
	            		int month = Integer.parseInt(kpi_month);
	            		if (1 <= month && month <= 3) {
	            			paraMap.put("kpi_quarter", "1");
	            		}
	            		else if (4 <= month && month <= 6) {
	            			paraMap.put("kpi_quarter", "2");
	            		}
	            		else if (7 <= month && month <= 9) {
	            			paraMap.put("kpi_quarter", "3");
	            		}
	            		else if (10 <= month && month <= 12) {
	            			paraMap.put("kpi_quarter", "4");
	            		}
	            		
//	            		System.out.println("확인용 kpi_year : "+ kpi_year);
//	            		System.out.println("확인용 kpi_month : "+ kpi_month);
//	            		System.out.println("확인용 month : "+ month);
//	            		System.out.println("실적발생일 : "+ String.valueOf(cellReader(cell)));
	            		paraMap.put("kpi_year", kpi_year);
	            		paraMap.put("result_date", String.valueOf(cellReader(cell)));
	            	}
	            	
	            	// 행의 8번째 열(실적액) - insert
	            	cell = row.getCell(7);
	            	if (cell != null) {
	            		paraMap.put("result_price", String.valueOf(cellReader(cell)));
//	            		System.out.println("실적액 : "+ String.valueOf(cellReader(cell)));
	            	}
	            	
	            	String fk_kpi_no = service.getKpi_no(paraMap); 	// 실적 입력을 위한 목표실적번호 채번
//	            	System.out.println("fk_kpi_no : "+ fk_kpi_no);
	            	
	            	paraMap.put("fk_kpi_no", fk_kpi_no);
	            	
	            	paraMapList.add(paraMap);
	            	
	            }// end of for() ---------------------
	            workbook.close();
				excel_file.delete();	// 업로드 된 파일 삭제하기

	            int insert_count = service.add_resultList(paraMapList);
	            
	            if (insert_count == paraMapList.size()) {
	            	jsonObj.put("result", 1);
	            }
	            else {
	            	// 엑셀파일의 데이터 등록을 일부 실패한 경우
	            	jsonObj.put("result", 0);
	            }
	            	

	            
			} catch (Exception e) {
				e.printStackTrace();
				jsonObj.put("result", 0);
			}
		}// end of if (mtp_excel_file != null) ------------------- 
		else {
			// 파일이 없을 경우
			jsonObj.put("result", 0);
		}
		
		return jsonObj.toString();
	}// end of public String uploadExcelFile(MultipartHttpServletRequest mtp_request) --------------------------- 
	
	
	// === 부서별 개인 실적 내역 조회 === //
	@GetMapping("deptResultByYearQuarter")
	public List<ResultVO> deptResultByYearQuarter(@RequestParam Map<String, String> paraMap) {		
		return service.getDeptResultByYearQuarter(paraMap);
	}// end of public List<ResultVO> deptResultByYearQuarter(@RequestParam Map<String, String> paraMap) -----------------------
	
	
}
