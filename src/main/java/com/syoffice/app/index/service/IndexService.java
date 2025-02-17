package com.syoffice.app.index.service;

import java.util.Map;

public interface IndexService {
	
   
	// 로그인 사용자 정보 조회
    Map<String, Object> getLoginUserMap(int emp_id);
}
