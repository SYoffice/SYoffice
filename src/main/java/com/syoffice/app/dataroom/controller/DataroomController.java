package com.syoffice.app.dataroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.syoffice.app.dataroom.service.DataroomService;
import com.syoffice.app.organization.service.OrganizationService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value="/dataroom/*")
public class DataroomController {

	@Autowired  // Type에 따라 알아서 Bean 을 주입해준다.
	private DataroomService service;
	
	
	@GetMapping("index")
	public String dataroom(HttpServletRequest request) {
		
		return "/dataroom/index";
		//   /WEB-INF/views/dataroom/dataindex.jsp 페이지를 만들어야 한다.
	}
	
}
