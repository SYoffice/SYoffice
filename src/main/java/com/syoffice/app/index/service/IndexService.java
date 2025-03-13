package com.syoffice.app.index.service;

import java.util.List;
import java.util.Map;

public interface IndexService {
	
   
	// 로그인 사용자 정보 조회
    Map<String, Object> getLoginUserMap(int emp_id);
    
    // 내 일정 가져오기
    List<Map<String, String>> getMySchedule(Map<String, String> paramMap);

    // 부서 일정 가져오기
    List<Map<String, String>> getDeptSchedule(Map<String, String> dep_paramMap);

    // 공지사항 
	List<Map<String, String>> getNoticeList();

	// 이번주 내 실적 
	List<Map<String, Object>> getWeeklyPerformance(Map<String, String> paramMap);

	// 내 실적 부서 실적 비교 
	List<Map<String, Object>> getDepartmentPerformance(String emp_id);

	
    
	
  
}
