package com.syoffice.app.kpi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.syoffice.app.kpi.domain.KpiVO;
import com.syoffice.app.kpi.service.KpiService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/kpi/*")
public class KpiController {

	@Autowired
	private KpiService service;
	
	// === 실적관리 메인페이지 === //
	@GetMapping("index")
	public String kpiIndex() {
		return "kpi/kpi_index";
	}// end of public String kpiIndex() --------------- 
	
	
	// === 목표 등록 페이지 === //
	@GetMapping("register")
	public String kpiRegister() {
		return "kpi/kpi_register";
	}// end of public String kpiRegister() -------------------
	
	
	// === 목표 등록 시 중복체크 === //
	@PostMapping("duplicateCheck")
	@ResponseBody
	public String kpiDuplicateCheck(KpiVO kpivo) {
		return service.kpiDuplicateCheck(kpivo);
	}// end of public String kpiDuplicateCheck(KpiVO kpivo) ----------------
	
	
	// === 목표 등록 기능 === //
	@PostMapping("register")
	public ModelAndView kpiRegister(HttpServletRequest request, ModelAndView mav, KpiVO kpivo) {
		return service.kpiRegister(request, mav, kpivo);
	}// end of public ModelAndView kpiRegister(ModelAndView mav, KpiVO kpivo) ---------------------- 
	
	
	// === 목표 관리 페이지 === //
	@GetMapping("management/{fk_dept_id}")
	public ModelAndView kpiManagement(HttpServletRequest request, ModelAndView mav, @PathVariable("fk_dept_id") String fk_dept_id) {
		return service.kpiManagement(request, mav, fk_dept_id);
	}// end of public String kpiIndex() ---------------
		
	
	 
	
}
