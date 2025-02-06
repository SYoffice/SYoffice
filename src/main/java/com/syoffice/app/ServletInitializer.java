package com.syoffice.app;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//ServletInitializer 클래스는 개발이 끝나고 war 파일로 배포하여 운영할 때 내부 Tomcat 이 아닌 Servlet Container 환경(외부 Tomcat)내에서 
//Spring Boot 애플리케이션이 동작 가능 하도록 해주는 클래스 이다.
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SyofficeApplication.class);
	}

}
