package com.syoffice.app.index.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.syoffice.app.board.model.BoardDAO;
import com.syoffice.app.employee.model.EmployeeDAO;
import com.syoffice.app.index.model.IndexDAO;

@Service
public class IndexService_imple implements IndexService {

	@Autowired
	private IndexDAO mapper_dao;
	
	// 로그인 사용자 정보 조회
	@Override
	public Map<String, Object> getLoginUserMap(int emp_id) {
		 return mapper_dao.getLoginUserMap(emp_id);
	}


	

    
}
