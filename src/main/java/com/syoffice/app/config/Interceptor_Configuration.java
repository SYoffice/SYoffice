package com.syoffice.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.syoffice.app.interceptor.controller.LoginCheckInterceptor;



@Configuration	// Spring 컨테이너가 처리해주는 클래스로서, 클래스내에 하나 이상의 @Bean 메소드를 선언만 해주면 런타임시 해당 빈에 대해 정의되어진 대로 요청을 처리해준다.
public class Interceptor_Configuration implements WebMvcConfigurer{	// 인터셉터 설정을 하려면 implements WebMvcConfigurer 꼭 해주어야 함
	
	// 로그인 Interceptor 설정하기
	@Autowired
	LoginCheckInterceptor loginCheckInterceptor;
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginCheckInterceptor)		// 생성한 로그인 검사 Intercepter 를 등록한다.
		.addPathPatterns("/**/*")
		.excludePathPatterns("/", "/login", "/employee/login") // 로그인 관련 페이지는 제외	
		.excludePathPatterns("/css/**", "/js/**", "/images/**", "/bootstrap-4.6.2-dist/**", "/jquery-ui-1.13.1.custom/**"); // 정적 리소스 예외
		/*
	        addInterceptor() : 인터셉터를 등록해준다.
	        addPathPatterns() : 인터셉터를 호출하는 주소와 경로를 추가한다. 
	        excludePathPatterns() : 인터셉터 호출에서 제외하는 주소와 경로를 추가한다. 
	     */

		
	}// end of public void addInterceptors(InterceptorRegistry registry) ----------------------------

}// end of public class Interceptor_Configuration implements WebMvcConfigurer{ } ----- 
