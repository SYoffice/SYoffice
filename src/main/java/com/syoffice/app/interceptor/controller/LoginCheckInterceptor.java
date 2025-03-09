package com.syoffice.app.interceptor.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


/*
	Interceptor 는 Spring 에서 제공해주는 기능이다.
	이 Interceptor 를 통해서 특정 URL 요청이 Controller 에서 실행하기전에 먼저 가로채서 다른 특정 작업을 진행할 수 있도록 해준다.
	이를 통해서 특정 URL 요청의 전,후 처리가 가능해진다. 
*/

//public class LoginCheckInterceptor extends HandlerInterceptorAdapter{
// spring 5.3 미만 버전에서 사용하는 것	
@Component
public class LoginCheckInterceptor implements HandlerInterceptor{
	// spring 5.3 이상 버전에서는 HandlerInterceptorAdapter 를 더이상 사용하지 않는다(deprecated)고 함.
	// extends HandlerInterceptorAdapter 대신에  implements HandlerInterceptor 를 사용해야 함.
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {    
		
		// 로그인 유무 검사
		HttpSession session = request.getSession();
		
		if (session.getAttribute("loginuser") == null) {
			// 로그인이 되지 않은 상태(비로그인)
			String message = "로그인 후에 사용가능합니다.";
			String loc = request.getContextPath()+ "/login";		// 로그인 페이지로 이동
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
			request.getRequestDispatcher("/WEB-INF/views/common/msg.jsp").forward(request, response);
			
			return false;
		}// end of if() ----
		
		return true;

		
	}// end of public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {  } -----
	
}// end of public class LoginCheckInterceptor implements HandlerInterceptor{ } ----
