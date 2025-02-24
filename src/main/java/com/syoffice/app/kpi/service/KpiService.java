package com.syoffice.app.kpi.service;

import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.syoffice.app.kpi.domain.KpiVO;
import com.syoffice.app.kpi.domain.ResultVO;

import jakarta.servlet.http.HttpServletRequest;

public interface KpiService {

	// === 실적관리 메인페이지(부서원 실적 정보 담김) === //
	ModelAndView getKpiInfo(ModelAndView mav, String kpi_year, String kpi_quarter, HttpServletRequest request);
	
	// === 목표 실적액 등록하기 === //
	ModelAndView kpiRegister(HttpServletRequest request, ModelAndView mav, KpiVO kpivo);

	// === 목표실적 관리 페이지(부서별 목표실적 정보 가져감) === //
	ModelAndView kpiManagement(HttpServletRequest request, ModelAndView mav, String fk_dept_id);
	
	// === 목표실적 등록 시 중복체크 === //
	String kpiDuplicateCheck(KpiVO kpivo);
	
	// === 목표실적 삭제 === //
	String kpiDelete(String kpi_no);

	// == 부서별 연도, 분기 목표실적 조회 == //
	List<KpiVO> deptKpiByYearQuarter(Map<String, String> paraMap);
	
	// === 목표실적 수정하기 페이지 === //
	ModelAndView kpiEdit(HttpServletRequest request, ModelAndView mav, String kpi_no);
	
	// === 목표실적 수정하기 === //
	ModelAndView kpiEditEnd(HttpServletRequest request, ModelAndView mav, KpiVO kpivo);
	
	// === 실적입력을 위한 목표실적 번호 채번 === //
	String getKpi_no(Map<String, String> paraMap);
	
	// === 엑셀파일을 통한 실적 입력 === //
	int add_resultList(List<Map<String, String>> paraMapList);

	// === 엑셀다운로드 === //
	void kpiResult_to_Excel(Map<String, String> paraMap, Model model);

	// === 부서별 개인 실적 내역 조회 === //
	List<ResultVO> getDeptResultByYearQuarter(Map<String, String> paraMap);

}
