package com.syoffice.app.kpi.service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.syoffice.app.kpi.domain.KpiVO;
import com.syoffice.app.kpi.model.KpiDAO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class KpiService_imple implements KpiService {
	
	@Autowired
	private KpiDAO dao;

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
			mav.addObject("message", "목표 등록을 성공했습니다.");
			mav.addObject("loc", request.getContextPath()+"/kpi/index");
			mav.setViewName("common/msg");
		}
		else {
			mav.addObject("message", "목표 등록을 실패했습니다.");
			mav.addObject("loc", "javascript:history.back()");
			mav.setViewName("common/msg");
		}
		return mav;
	}// end of public ModelAndView kpiRegister(ModelAndView mav, KpiVO kpivo) --------------------- 


	// === 목표 관리 페이지 === //
	@Override
	public ModelAndView kpiManagement(HttpServletRequest request, ModelAndView mav, String fk_dept_id) {
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("fk_dept_id", fk_dept_id);
		
		// 특정 부서의 목표실적 가져오기
		List<KpiVO> deptKpiList = dao.getDeptKpi(paraMap);
		
		if (deptKpiList != null && deptKpiList.size() > 0) {
			// 조회 된 것이 있다면
			mav.addObject("deptKpiList", deptKpiList);
			mav.setViewName("kpi/kpi_management");
		}
		
		if (deptKpiList == null || deptKpiList.size() == 0) {
			mav.addObject("message", "등록된 목표 실적이 없습니다.\n목표실적 등록 페이지로 이동합니다.");
			mav.addObject("loc", request.getContextPath()+"/kpi/register");
			mav.setViewName("common/msg");
		}
		
		return mav;
	}// end of public ModelAndView kpiManagement(ModelAndView mav) ------------------ 


	
}
