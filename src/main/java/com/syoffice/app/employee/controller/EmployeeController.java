package com.syoffice.app.employee.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.syoffice.app.common.Sha256;
import com.syoffice.app.employee.domain.EmployeeVO;
import com.syoffice.app.employee.service.EmployeeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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

	// 비밀번호 변경 페이지 요청
	@GetMapping("/pwdChange")
	public String showPwdChangePage() {
		return "employee/pwdChange";
	}// end of public String showPwdChangePage() ----- 
	
	// 비밀번호 변경 요청
	@PostMapping("/pwdChange")
	public ModelAndView pwdchange(ModelAndView mav, HttpServletRequest request,
								  @RequestParam String newPwd) {
		// 세션 불러오기
		HttpSession session = request.getSession();
		// 세션에서 불러온 정보를 loginuser 에 담아주기
		EmployeeVO loginuser = (EmployeeVO) session.getAttribute("loginuser");
		
		// 세션에 있는 아이디 값 담아주기
		String emp_id = loginuser.getEmp_id();
		
		mav = service.pwdChange(newPwd, emp_id, mav, request);
				
		return mav;
	}// end of public ModelAndView pwdchange(ModelAndView mav, HttpServletRequest request, @RequestParam String newPwd) ----- 
	
	// 내정보 페이지 요청
	@GetMapping("/mypage")
	public String mypage() {
		return "employee/mypage";
	}// end of public String mypage() -----
	
}// end of public class EmployeeController -----
