package com.syoffice.app.kpi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.syoffice.app.kpi.domain.KpiVO;
import com.syoffice.app.kpi.service.KpiService;

@Controller
@RestController
@RequestMapping("/api/kpi/*")
public class ApiKpiController {
	
	@Autowired
	private KpiService service;
	
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
	
	
	@GetMapping("deptKpiByYear")
	public List<KpiVO> deptKpiByYear (@RequestParam Map<String, String> paraMap) {
		return service.deptKpiByYear(paraMap);
	}// end of public List<KpiVo> deptKpiByYear (@RequestParam Map<String, String> paraMap) ------------------------- 
}
