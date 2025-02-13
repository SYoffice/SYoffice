package com.syoffice.app.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value="/*")
public class AttendanceController {
	
	@Autowired  // Type에 따라 알아서 Bean 을 주입해준다.
	
	

	@GetMapping("attendance")
	public String attendance(HttpServletRequest request) {
		
		return "main/attendance/attendance";
		//   /WEB-INF/views/main/attendance.jsp 페이지를 만들어야 한다.
	}

}
