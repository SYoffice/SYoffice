package com.syoffice.app.dataroom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.syoffice.app.dataroom.model.DataroomDAO;

//==== #2. Service 선언 ====
//트랜잭션 처리를 담당하는 곳, 업무를 처리하는 곳, 비지니스(Business)단
//@Component
@Service
public class DataroomService_imple implements DataroomService {

	// === #5. 의존객체 주입하기(DI: Dependency Injection) ===
	@Autowired  // Type에 따라 알아서 Bean 을 주입해준다.
	private DataroomDAO dao;
	// Type 에 따라 Spring 컨테이너가 알아서 bean 으로 등록된 com.spring.app.index.model.IndexDAO_imple 의 bean 을 dao 에 주입시켜준다. 
	// 그러므로 dao 는 null 이 아니다.
	
	
}
