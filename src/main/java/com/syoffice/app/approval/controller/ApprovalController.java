package com.syoffice.app.approval.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value="/approval/*")
public class ApprovalController {
	
	//@Autowired
	//private ApprovalService apvservice;
	
	@GetMapping("approval_main")
	public String approval() {   // 메인
		
		return "/approval/approval_main";
	}

	@GetMapping("obtain_approval_box")
	public String obtain_approval_box() { // 결재대기문서함
		
		return "/approval/obtain_approval_box";
	}
}
