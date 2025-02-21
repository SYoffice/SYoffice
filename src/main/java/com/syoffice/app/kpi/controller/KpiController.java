package com.syoffice.app.kpi.controller;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.syoffice.app.kpi.domain.KpiVO;
import com.syoffice.app.kpi.service.KpiService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/kpi/*")
public class KpiController {

	@Autowired
	private KpiService service;
	
	
	
	// === 실적관리 메인페이지(부서원 실적 정보 담김) === //
	@GetMapping("index")
	public ModelAndView kpiIndex(ModelAndView mav, @RequestParam String kpi_year
												 , @RequestParam String kpi_quarter
												 , HttpServletRequest request) {
		
		if (kpi_year == null) {
			Calendar cal = Calendar.getInstance();
			kpi_year = String.valueOf(cal.get(Calendar.YEAR));
		}
		
		if (kpi_quarter == null) {
			Calendar cal = Calendar.getInstance();
			int month = cal.get(Calendar.MONTH)+1;
			
			if (1 <= month && month <= 3) {
				kpi_quarter = "1";
			}
			else if (4 <= month && month <= 6) {
				kpi_quarter = "2";
			}
			else if (7 <= month && month <= 9) {
				kpi_quarter = "3";
			}
			else if (10 <= month && month <= 12) {
				kpi_quarter = "4";
			}
		}		
		return service.getKpiInfo(mav, kpi_year, kpi_quarter, request);
	}// end of public String kpiIndex() --------------- 
	
	
	// === 목표 등록 페이지 === //
	@GetMapping("register")
	public String kpiRegister() {
		return "kpi/kpi_register";
	}// end of public String kpiRegister() -------------------
	
	
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
		
	// === 목표실적 수정 페이지 === //
	@GetMapping("edit/{kpi_no}")
	public ModelAndView kpiEdit(HttpServletRequest request, ModelAndView mav, @PathVariable("kpi_no") String kpi_no) {
		return service.kpiEdit(request, mav, kpi_no);
	}// end of public ModelAndView kpiEdit(HttpServletRequest request, ModelAndView mav, @PathVariable("fk_dept_id") String kpi_no) ------------------- 
	
	
	// === 목표 실적 수정 === //
	@PostMapping("edit")
	public ModelAndView kpiEditEnd(HttpServletRequest request, ModelAndView mav, KpiVO kpivo) {
		return service.kpiEditEnd(request, mav, kpivo);
	}// end of public ModelAndView kpiEditEnd(ModelAndView mav, KpiVO kpivo) -------------------------- 
	
	
	// === 실적 등록 페이지 === //
	@GetMapping("resultRegister")
	public String kpiEditEnd() {
		return "kpi/kpi_resultRegister";
	}// end of public ModelAndView kpiEditEnd(ModelAndView mav, KpiVO kpivo) -------------------------- 
	
}
