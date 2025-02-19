package com.syoffice.app.kpi.service;

import org.springframework.web.servlet.ModelAndView;

import com.syoffice.app.kpi.domain.KpiVO;

import jakarta.servlet.http.HttpServletRequest;

public interface KpiService {

	// === 목표 실적액 등록하기 === //
	ModelAndView kpiRegister(HttpServletRequest request, ModelAndView mav, KpiVO kpivo);

	// === 목표 관리 페이지 === //
	ModelAndView kpiManagement(HttpServletRequest request, ModelAndView mav, String fk_dept_id);
	
	// === 목표 등록 시 중복체크 === //
	String kpiDuplicateCheck(KpiVO kpivo);

}
