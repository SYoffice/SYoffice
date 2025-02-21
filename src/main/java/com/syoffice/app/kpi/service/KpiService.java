package com.syoffice.app.kpi.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import com.syoffice.app.kpi.domain.KpiVO;

import jakarta.servlet.http.HttpServletRequest;

public interface KpiService {

	// === 목표 실적액 등록하기 === //
	ModelAndView kpiRegister(HttpServletRequest request, ModelAndView mav, KpiVO kpivo);

	// === 목표실적 관리 페이지(부서별 목표실적 정보 가져감) === //
	ModelAndView kpiManagement(HttpServletRequest request, ModelAndView mav, String fk_dept_id);
	
	// === 목표실적 등록 시 중복체크 === //
	String kpiDuplicateCheck(KpiVO kpivo);
	
	// === 목표실적 삭제 === //
	String kpiDelete(String kpi_no);

	// == 부서별 연도, 분기 목표실적 조회 == //
	List<KpiVO> deptKpiByYear(Map<String, String> paraMap);
	
	// === 목표실적 수정하기 페이지 === //
	ModelAndView kpiEdit(HttpServletRequest request, ModelAndView mav, String kpi_no);
	
	// === 목표실적 수정하기 === //
	ModelAndView kpiEditEnd(HttpServletRequest request, ModelAndView mav, KpiVO kpivo);

}
