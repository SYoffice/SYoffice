package com.syoffice.app.index.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.syoffice.app.index.model.IndexDAO;

// ==== #2. Service 선언 ====
// 트랜잭션 처리를 담당하는 곳, 업무를 처리하는 곳, 비지니스(Business)단
// @Component
@Service
public class IndexService_imple implements IndexService {

	// === #5. 의존객체 주입하기(DI: Dependency Injection) ===
	@Autowired  // Type에 따라 알아서 Bean 을 주입해준다.
	private IndexDAO dao;
	// Type 에 따라 Spring 컨테이너가 알아서 bean 으로 등록된 com.spring.app.index.model.IndexDAO_imple 의 bean 을 dao 에 주입시켜준다. 
	// 그러므로 dao 는 null 이 아니다.
		
	
	// === #8. 메인 페이지 요청 === //
	// 시작페이지에서 캐러젤을 보여주는 것
	@Override
	public List<Map<String, String>> getImgfilenameList() {
		
		List<Map<String, String>> mapList = dao.getImgfilenameList();
		return mapList;
	}

	
	
}
