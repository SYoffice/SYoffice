package com.syoffice.app.mail.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.syoffice.app.mail.service.MailService;

@Controller
@RequestMapping("/api/mail/*")
@RestController
public class ApiMailController {
	
	@Autowired
	private MailService service;
	
	
	// === 조직도 활용한 수신,참조인 추가용 회원 한 명의 정보를 가져오기 === //
	@GetMapping("getEmployeeInfo")
	public Map<String, String> getEmployeeInfo(@RequestParam Long emp_id) {
		return service.getEmployeeInfo(emp_id);
	}// end of public EmployeeVO getEmployeeInfo(@RequestParam Long emp_id) ---------------------- 
		
	
	// === 메일 전송하기 (타인) === //
	@PostMapping("sendMail")
	public String sendMail(MultipartHttpServletRequest mtp_request) {
		return service.sendMail(mtp_request);
	}// end of public String sendMail(MultipartHttpServletRequest mtp_request) -------------------- 
	
}	
