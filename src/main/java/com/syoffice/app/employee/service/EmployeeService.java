package com.syoffice.app.employee.service;

import java.util.Map;

import org.springframework.web.servlet.ModelAndView;


import jakarta.servlet.http.HttpServletRequest;

public interface EmployeeService {

	// 로그인 처리
	ModelAndView login(Map<String, String> paraMap, ModelAndView mav, HttpServletRequest request);
	
	// 비밀번호 변경
	ModelAndView pwdChange(String newPwd, String emp_id, ModelAndView mav, HttpServletRequest request);
	
}
