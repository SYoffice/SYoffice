package com.syoffice.app.index.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.syoffice.app.index.service.IndexService;

import jakarta.servlet.http.HttpServletRequest;

/*
	사용자 웹브라우저 요청(View) <==> Application 클래스(MyspringApplication) ==> @Controller 클래스 <==>> Service단(핵심업무로직단, business logic단) <==>> Model단[Repository](DAO, DTO) <==>> myBatis <==>> DB(오라클)           
	(http://localhost:9090/myspring/...  )   ↑                                    |                                                                                                                              
	                                         |                              View Resolver
	                                         |                                    ↓
	                                         |                              View단(.jsp 또는 Bean명)
	                                         -------------------------------------|  
	
	사용자(클라이언트)가 웹브라우저에서 http://localhost:9090/myspring/index 을 실행하면
	Application 클래스인 com.spring.app.MyspringApplication 이 작동된다.
	MyspringApplication 은 bean 으로 등록된 객체중 controller 빈을 찾아서 URL값이 "/myspring/index" 로
	매핑된 메소드를 실행시키게 된다.                                               
	Service(서비스)단 객체를 업무 로직단(비지니스 로직단)이라고 부른다.
	Service(서비스)단 객체가 하는 일은 Model단에서 작성된 데이터베이스 관련 여러 메소드들 중 관련있는것들만을 모아 모아서
	하나의 트랜잭션 처리 작업이 이루어지도록 만들어주는 객체이다.
	여기서 업무라는 것은 데이터베이스와 관련된 처리 업무를 말하는 것으로 Model 단에서 작성된 메소드를 말하는 것이다.
	이 서비스 객체는 @Controller 단에서 넘겨받은 어떤 값을 가지고 Model 단에서 작성된 여러 메소드를 호출하여 실행되어지도록 해주는 것이다.
	실행되어진 결과값을 @Controller 단으로 넘겨준다.
*/

// ==== #1. 컨트롤러 선언 ====
// @Component
/* 클래스명 앞에 @Component 어노테이션을 적어주면 해당 클래스는 bean으로 자동 등록된다. 
   그리고 bean의 이름은 해당 클래스명에서 첫글자만 소문자로 바뀐 이름으로 되어진다. 
   즉, 여기서 bean의 이름은 beginController 인 것이다. 
   여기서 @Controller 를 사용하므로 @Controller 에는 @Component 기능이 이미 포함되어 있으므로 @Component를 명기하지 않아도 BeginController 는 bean 으로 등록되어 스프링컨테이너가 자동적으로 관리해준다. 
*/
@Controller
@RequestMapping(value="/*")
public class IndexController {

	
	
	
	// === #7. 메인 페이지 요청 === //
	@GetMapping("/")   // http://localhost:9090/myspring/
	public String main() {
		return "redirect:/index";  //  http://localhost:9090/myspring/index
	}
	
	@GetMapping("index")  //  http://localhost:9090/myspring/index
	public String index(HttpServletRequest request) {
		
//		List<Map<String, String>> mapList = service.getImgfilenameList();
		
//		request.setAttribute("mapList", mapList);
		
		return "mycontent1/main/index";
		//   /WEB-INF/views/mycontent1/main/index.jsp 페이지를 만들어야 한다.
	}
	
	
	
	
}
