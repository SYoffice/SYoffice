package com.syoffice.app.kpi.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.apache.poi.ss.usermodel.Row;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.syoffice.app.employee.domain.EmployeeVO;
import com.syoffice.app.kpi.domain.KpiVO;
import com.syoffice.app.kpi.domain.ResultVO;
import com.syoffice.app.kpi.model.KpiDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class KpiService_imple implements KpiService {
	
	@Autowired
	private KpiDAO dao;
	
	
	// === 실적관리 메인페이지(부서원 실적 정보 담김) === //
	@Override
	public ModelAndView getKpiInfo(ModelAndView mav, String kpi_year, String kpi_quarter, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		
		EmployeeVO empvo = (EmployeeVO) session.getAttribute("loginuser");
		Map<String, String> paraMap = new HashMap<>();
		
		if (empvo != null) {
			paraMap.put("fk_dept_id", empvo.getFk_dept_id());
		}
		paraMap.put("kpi_year", kpi_year);
		paraMap.put("kpi_quarter", kpi_quarter);
		
		List<ResultVO> resultvoList = dao.getResultBydeptKpi(paraMap);	// 연도, 분기별 부서원 실적 정보
		
		if (resultvoList != null && resultvoList.size() > 0) {
			mav.addObject("resultvoList", resultvoList);
		}
		
		mav.setViewName("kpi/kpi_index");
		return mav;
	}// end of public ModelAndView getKpiInfo(ModelAndView mav, String kpi_year, String kpi_quarter, HttpServletRequest request) ----------------------

	
	
	// === 목표 등록 시 중복체크 === //
	@Override
	public String kpiDuplicateCheck(KpiVO kpivo) {
		int n = dao.getExistKpi(kpivo);		// 기존에 등록한 목표가 있는지 확인
		
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("result", n);
		
		Gson gson = new Gson();
		
		return gson.toJson(jsonObj);
	}// end of public String kpiDuplicateCheck(KpiVO kpivo) ------------------ 
	
	
	// === 목표 실적액 등록하기 === //
	@Override
	public ModelAndView kpiRegister(HttpServletRequest request, ModelAndView mav, KpiVO kpivo) {
		
		int n = dao.kpiRegister(kpivo);;		// 기존에 등록한 목표가 있는지 확인
		
		if (n == 1) {
			mav.addObject("message", "목표 실적 등록을 성공했습니다.");
			mav.addObject("loc", request.getContextPath()+"/kpi/index");
			mav.setViewName("common/msg");
		}
		else {
			mav.addObject("message", "목표 실적 등록을 실패했습니다.");
			mav.addObject("loc", "javascript:history.back()");
			mav.setViewName("common/msg");
		}
		return mav;
	}// end of public ModelAndView kpiRegister(ModelAndView mav, KpiVO kpivo) --------------------- 


	// === 목표 관리 페이지 === //
	@SuppressWarnings("static-access")
	@Override
	public ModelAndView kpiManagement(HttpServletRequest request, ModelAndView mav, String fk_dept_id) {
		
			String searchYear = request.getParameter("searchYear");
		
		if (searchYear == null || searchYear == "") {
			Calendar cal = Calendar.getInstance();
			searchYear = String.valueOf(cal.get(Calendar.YEAR)	);
//			System.out.println("확인용 searchYear : "+ searchYear);
		}
		
		Map<String, String> paraMap = new HashMap<>();
		
		paraMap.put("fk_dept_id", fk_dept_id);
		paraMap.put("searchYear", searchYear);
		
		// 특정 부서의 목표실적 가져오기
		List<KpiVO> deptKpiList = dao.getDeptKpi(paraMap);
		
		if (deptKpiList != null && deptKpiList.size() > 0) {
			// 조회 된 것이 있다면
			mav.addObject("deptKpiList", deptKpiList);
			mav.setViewName("kpi/kpi_management");
		}
		
		if (deptKpiList == null || deptKpiList.size() == 0) {
			mav.addObject("message", "등록된 목표 실적이 없습니다. \\n목표 실적을 먼저 등록하세요.");
			mav.addObject("loc", request.getContextPath()+"/kpi/index");
			mav.setViewName("common/msg");
		}
			
		return mav;
	}// end of public ModelAndView kpiManagement(ModelAndView mav) ------------------ 


	// === 목표실적 삭제 === //
	@Override
	public String kpiDelete(String kpi_no) {
		int n = dao.kpiDelete(kpi_no);
		
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("result", n);
		
		Gson gson = new Gson();
		
		return gson.toJson(jsonObj);
	}// end of public String kpiDelete(String kpi_no) ----------------------- 

	
	// == 부서별 연도, 분기 목표실적 조회 == //
	@Override
	public List<KpiVO> deptKpiByYearQuarter(Map<String, String> paraMap) {
		return dao.getDeptKpi(paraMap);
	}// end of public List<KpiVO> deptKpiByYear(Map<String, String> paraMap) ------------------------- 


	// === 목표실적 수정하기 === //
	@Override
	public ModelAndView kpiEdit(HttpServletRequest request, ModelAndView mav, String kpi_no) {
		
		try {
			Integer.parseInt(kpi_no);
			
			// 한 개의 목표실적 알아오기
			KpiVO kpivo = dao.getKpiOne(kpi_no);
			
			if (kpivo != null) {
				mav.addObject("kpivo", kpivo);
				mav.setViewName("kpi/kpi_edit");
			}
		} catch (NumberFormatException e) {
			mav.setViewName("redirect:/kpi/index");
		}
		
		return mav;
	}// end of public ModelAndView kpiEdit(HttpServletRequest request, ModelAndView mav, String kpi_no) -------------- 


	// === 목표실적 수정하기 === //
	@Override
	public ModelAndView kpiEditEnd(HttpServletRequest request, ModelAndView mav, KpiVO kpivo) {
		
		// 한 개의 목표실적 알아오기
		int n = dao.editKpi(kpivo);
		
		if (n == 1) {	
			mav.addObject("message", "목표 실적 수정을 성공했습니다.");
			mav.addObject("loc", request.getContextPath()+"/kpi/management/"+ kpivo.getFk_dept_id());
			mav.setViewName("common/msg");
		}
		else {
			mav.addObject("message", "목표 실적 수정을 실패했습니다.");
			mav.addObject("loc", "javascript:history.back()");
			mav.setViewName("common/msg");
		}
		
		return mav;
	}// end of public ModelAndView kpiEditEnd(HttpServletRequest request, ModelAndView mav, KpiVO kpivo) ---------------------

	
	
	// === 실적입력을 위한 목표실적 번호 채번 === //
	@Override
	public String getKpi_no(Map<String, String> paraMap) {
		return dao.getKpi_no(paraMap);
	}// end of public String getKpi_no(Map<String, String> paraMap) -----------------------

	
	
	// === 엑셀파일을 통한 실적 입력 === //
	@Override
	public int add_resultList(List<Map<String, String>> paraMapList) {
		int insert_count = 0;
		
		for(Map<String, String> paraMap :paraMapList) {
			insert_count += dao.add_resultList(paraMap);
		}// end of for() ---------------
		
		return insert_count;
	}// end of public int add_resultList(List<Map<String, String>> paraMapList) ------------------------ 


	// === 엑셀다운로드 === //
	@Override
	public void kpiResult_to_Excel(Map<String, String> paraMap, Model model) {
		SXSSFWorkbook workbook = new SXSSFWorkbook();	// 엑셀 파일 생성
		
		SXSSFSheet sheet = workbook.createSheet("실적내역");
		sheet.setDisplayGridlines(false);	// 눈금선 보이지 않도록
		
		// 시트 열 너비 설정
		sheet.setColumnWidth(0, 2000);	// 지점
		sheet.setColumnWidth(1, 2000);	// 부서명
		sheet.setColumnWidth(2, 3000);	// 사원번호
		sheet.setColumnWidth(3, 3000);	// 사원명
		sheet.setColumnWidth(4, 2200);	// 직급
		sheet.setColumnWidth(5, 8000);	// 실적명
		sheet.setColumnWidth(6, 3000);	// 실적발생일
		sheet.setColumnWidth(7, 3500);	// 실적액
		sheet.setColumnWidth(8, 1500);	// 연도
		sheet.setColumnWidth(9, 1500);	// 분기
		sheet.setColumnWidth(10, 2000);	// 지점번호
		sheet.setColumnWidth(11, 2000);	// 부서번호
		
		int cellCount = 11;		// 사용하는 열의 개수
		
		// 행의 위치를 나타내는 변수
		int rowLocation = 0;
		
		// 셀 중앙지정
		CellStyle mergeRowStyle = workbook.createCellStyle();
		mergeRowStyle.setAlignment(HorizontalAlignment.CENTER);
		mergeRowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		
		
		// CellStyle 배경색(ForegroundColor)만들기
		mergeRowStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		mergeRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		// 메뉴
		headerStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());  
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		// CellStyle 천단위 쉼표, 금액
        CellStyle moneyStyle = workbook.createCellStyle();
        moneyStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
        
        Font mergeRowFont = workbook.createFont(); // import org.apache.poi.ss.usermodel.Font; 으로 한다.
        mergeRowFont.setFontName("나눔고딕");			// 폰트
        mergeRowFont.setFontHeight((short)400);		// 폰트 크기(높이?)
        mergeRowFont.setColor(IndexedColors.BLACK.getIndex());	// 폰트 색상
        mergeRowFont.setBold(true);		// 폰트 굵기
		
        mergeRowStyle.setFont(mergeRowFont);		// 타이틀에 폰트 지정
        
        // CellStyle 테두리 Border (얇게)
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        
        Row mergeRow = sheet.createRow(rowLocation);	// 엑셀에서 행의 시작은 0부터 시작한다.
        
        List<ResultVO> kpiResultList = dao.getResultBydeptKpi(paraMap);
        
        // title 제작
        for (int i=0; i<cellCount; i++) {
        	
        	String year 	= kpiResultList.get(0).getKpi_year();
        	String quarter 	= kpiResultList.get(0).getKpi_quarter();
        	String deptName	= kpiResultList.get(0).getDept_name();
        		
        	Cell cell = mergeRow.createCell(i);		// i: 열의 위치
        	cell.setCellStyle(mergeRowStyle);		// 각 셀에 스타일 지정
        	cell.setCellValue(year+"년 "+quarter+"분기 "+ deptName +" 실적내역");		// 각 셀에 들어가는 내용 작성
        }// end of for() -----------------------
        
        // 셀 병합하기
        sheet.addMergedRegion(new CellRangeAddress(rowLocation, rowLocation, 0, cellCount)); // 시작 행, 끝 행, 시작 열, 끝 열
        // === 타이틀 만들기 끝 === //
        
        
        // 헤더 행 생성
        Row headerRow = sheet.createRow(++rowLocation);	// 엑셀에서 행의 시작은 0부터 시작한다., 헤더와 겹치지 않기 위해 전위연산자 사용 ++rowLocation
        
     // 해당 행의 첫번째 열 셀 생성
        Cell headerCell = headerRow.createCell(0);	// 엑셀에서 열의 시작은 0부터이다.
        headerCell.setCellValue("지점");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 두번째 열 셀 생성
        headerCell = headerRow.createCell(1);	
        headerCell.setCellValue("부서명");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 세번째 열 셀 생성
        headerCell = headerRow.createCell(2);	
        headerCell.setCellValue("사원번호");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 네번째 열 셀 생성
        headerCell = headerRow.createCell(3);	
        headerCell.setCellValue("사원명");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 다섯번째 열 셀 생성
        headerCell = headerRow.createCell(4);	
        headerCell.setCellValue("직급");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 여섯번째 열 셀 생성
        headerCell = headerRow.createCell(5);	
        headerCell.setCellValue("실적명");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 일곱째 열 셀 생성
        headerCell = headerRow.createCell(6);	
        headerCell.setCellValue("실적발생일");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 여덟번째 열 셀 생성
        headerCell = headerRow.createCell(7);	
        headerCell.setCellValue("실적액");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 아홉번째 열 셀 생성
        headerCell = headerRow.createCell(8);	
        headerCell.setCellValue("연도");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 열번째 열 셀 생성
        headerCell = headerRow.createCell(9);	
        headerCell.setCellValue("분기");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 열한 번째 열 셀 생성
        headerCell = headerRow.createCell(10);	
        headerCell.setCellValue("지점번호");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 열두 번째 열 셀 생성
        headerCell = headerRow.createCell(11);	
        headerCell.setCellValue("부서번호");
        headerCell.setCellStyle(headerStyle);
        
        // === 본문 행 및 셀 생성하기 === //
        Row bodyRow = null;
        Cell bodyCell = null;
        
        for (int i=0; i<kpiResultList.size(); i++) {
        	ResultVO resultVO = kpiResultList.get(i);
        	
        	// 행 만들기 시작
        	bodyRow = sheet.createRow(i + (rowLocation+1));		// 1번인 메뉴명 이후부터 만들어야 하므로 rowLocation+1
        	
        	// 데이터 지점명
        	bodyCell = bodyRow.createCell(0);
        	bodyCell.setCellValue(resultVO.getBranch_name());
        	
        	// 데이터 부서명 표시
        	bodyCell = bodyRow.createCell(1);
        	bodyCell.setCellValue(resultVO.getDept_name());
        	
        	// 데이터 사원번호 표시
        	bodyCell = bodyRow.createCell(2);
        	bodyCell.setCellValue(Integer.parseInt(resultVO.getFk_emp_id()));
        	
        	// 데이터 사원명 표시
        	bodyCell = bodyRow.createCell(3);
        	bodyCell.setCellValue(resultVO.getName());
        	
        	// 데이터 직급 표시
        	bodyCell = bodyRow.createCell(4);
        	bodyCell.setCellValue(resultVO.getGrade_name());
        	
        	// 데이터 실적명 표시
        	bodyCell = bodyRow.createCell(5);
        	bodyCell.setCellValue(resultVO.getResult_name());
        	
        	// 데이터 실적발생일 표시
        	bodyCell = bodyRow.createCell(6);
        	bodyCell.setCellValue(resultVO.getResult_date());
        	
        	// 데이터 실적액 표시
        	bodyCell = bodyRow.createCell(7);
        	bodyCell.setCellValue(Integer.parseInt(resultVO.getResult_price()));		// 천 단위 쉼표를 찍기 위해 int 형변환
        	bodyCell.setCellStyle(moneyStyle);
        	
        	// 데이터 연도 표시
        	bodyCell = bodyRow.createCell(8);
        	bodyCell.setCellValue(Integer.parseInt(resultVO.getKpi_year()));
        	
        	// 데이터 분기 표시
        	bodyCell = bodyRow.createCell(9);
        	bodyCell.setCellValue(Integer.parseInt(resultVO.getKpi_quarter()));
        	
        	// 데이터 지점번호 표시
        	bodyCell = bodyRow.createCell(10);
        	bodyCell.setCellValue(Integer.parseInt(resultVO.getFk_branch_no()));
        	
        	// 데이터 부서번호 표시
        	bodyCell = bodyRow.createCell(11);
        	bodyCell.setCellValue(Integer.parseInt(resultVO.getFk_dept_id()));
        	
        }// end of for() ------------------------

        
        model.addAttribute("locale", Locale.KOREA);		// import java.util, 한글 깨짐 방지 차원으로 지역 설정
        model.addAttribute("workbook", workbook);
        model.addAttribute("workbookName", "SYOFFICE_실적내역");
        
	}// end of public void kpiResult_to_Excel(Map<String, Object> paraMap, Model model) ----------------------------- 


	// === 부서별 개인 실적 내역 조회 === //
	@Override
	public List<ResultVO> getDeptResultByYearQuarter(Map<String, String> paraMap) {
		return dao.getResultBydeptKpi(paraMap);
	}// end of public List<ResultVO> getDeptResultByYearQuarter(Map<String, String> paraMap) ------------------------ 

}
