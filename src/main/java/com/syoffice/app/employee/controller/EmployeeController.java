package com.syoffice.app.employee.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.syoffice.app.employee.service.EmployeeService;

import jakarta.servlet.http.HttpServletRequest;

/* 임직원 컨트롤러 선언 */
@Controller
@RequestMapping(path="/employee/*")
public class EmployeeController {

	@Autowired	// Type 에 따라 알아서 Bean 을 알아서 주입
	private EmployeeService service;
	
	// 로그인 처리
	@PostMapping("login")
	public ModelAndView login(ModelAndView mav, HttpServletRequest request, @RequestParam Map<String, String> paraMap) {
		
	//	System.out.println(paraMap.get("emp_id"));
	//	System.out.println(paraMap.get("password"));
		mav = service.login(paraMap, mav, request);
		
		return mav;
	}// end of public ModelAndView login(ModelAndView mav, HttpServletRequest request, @RequestParam Map<String, String> paraMap) -------


}// end of public class EmployeeController -----
