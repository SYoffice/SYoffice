package com.syoffice.app.index.model;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IndexDAO {

	// 로그인 사용자 정보 조회
	Map<String, Object> getLoginUserMap(Integer emp_Id);
	
	
    // 내 일정 가져오기
    List<Map<String, String>> getMySchedule(Map<String, String> paramMap);

    // 내가 속한 부서 일정 가져오기
    List<Map<String, String>> getDeptSchedule(Map<String, String> paramMap);

    // 공지사항 
	List<Map<String, String>> getNoticeList();

	// 이번주 내 실적 
	List<Map<String, Object>> getWeeklyPerformance(Map<String, String> paramMap);

	// 부서 실적 
	List<Map<String, Object>> getDepartmentPerformance();

}
