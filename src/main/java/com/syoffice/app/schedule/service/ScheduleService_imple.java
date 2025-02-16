package com.syoffice.app.schedule.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.syoffice.app.schedule.model.ScheduleDAO;

@Service
public class ScheduleService_imple implements ScheduleService {
	
	@Autowired
	private ScheduleDAO dao;

	
	// === 일정 등록시 내캘린더,사내캘린더 선택에 따른 서브캘린더 종류를 알아오기 === //
	@Override
	public List<Map<String, String>> selectSmallCategory(Map<String, String> paraMap) {
		 List<Map<String, String>> small_category_List = dao.selectSmallCategory(paraMap);
		return small_category_List;
	}// end of public List<Map<String, String>> selectSmallCategory(Map<String, String> paraMap) ---------------------- 


	// === 공유자를 찾기 위한 특정글자가 들어간 회원명단 불러오기 === //
	@Override
	public List<Map<String, String>> searchJoinUserList(String joinSearchWord) {
		List<Map<String, String>> joinUserList = dao.searchJoinUserList(joinSearchWord);
		return joinUserList;
	}// end of public List<EmployeeVO> searchJoinUserList(String joinUserName) ------------------- 

	// === 일정 등록하기 === //
	@Override
	public int registerSchedule_end(Map<String, String> paraMap) {
		int n = dao.registerSchedule_end(paraMap);
		return n;
	}// end of public int registerSchedule_end(Map<String, String> paraMap) ---------------------- 


	// === 내 일정 소분류 보여주기 === //
	@Override
	public List<Map<String, String>> showMyCalendar(String fk_emp_id) {
		List<Map<String, String>> mycalendar_small_categoryList = dao.showMyCalendar(fk_emp_id);
		return mycalendar_small_categoryList;
	}// end of public List<Map<String, String>> showMyCalendar(String fk_emp_id) ----------------------- 


	// === 일정 소분류 추가하기 === //
	@Override
	public int addCalendar(Map<String, String> paraMap) {
		
		int n = 0;
		// 이미 있는 소분류인지 알아오기
		int m = dao.existCalendar(paraMap);
		
		
		if (m == 0) {
			// 조회된 것이 없다면 소분류 추가
			n = dao.addCalendar(paraMap);			
		}
		return n;
	}// end of public int addCalendar(Map<String, String> paraMap) ------------------- 

	// === 전사 일정 소분류 가져오기 === //
	@Override
	public List<Map<String, String>> showCompanyCalendar() {
		List<Map<String, String>> calendar_small_category_CompanyList = dao.showCompanyCalendar();
		return calendar_small_category_CompanyList;
	}// end of public List<Map<String, String>> showCompanyCalendar() ----------------------  


	// === 등록된 모든 일정 불러오기 === //
	@Override
	public List<Map<String, String>> selectSchedule(Map<String, String> paraMap) {
		List<Map<String, String>> scheduleList = dao.selectSchedule(paraMap);
		return scheduleList;
	}
	
}
