package com.syoffice.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class SyofficeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SyofficeApplication.class, args);
	}

}
