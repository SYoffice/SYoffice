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


	// === 일정상세보기 === //
	@Override
	public Map<String, String> detailSchedule(String scheduleno) {
		Map<String,String> map = dao.detailSchedule(scheduleno);
		return map;
	}// end of public Map<String, String> detailSchedule(String scheduleno) ---------------


	// === 전사일정 소분류 수정하기 === //
	@Override
	public int editCalendar(Map<String, String> paraMap) {
		
		int n = 0;
		
		int m = dao.existsCalendar(paraMap); 
		// 수정된 (사내캘린더 또는 내캘린더)속의 소분류 카테고리명이 이미 해당 사용자가 만든 소분류 카테고리명으로 존재하는지 유무 알아오기  
		
		if(m == 0) {
			n = dao.editCalendar(paraMap);	
		}
		
		return n;		
	}// end of public int editCalendar(Map<String, String> paraMap) ------------------------ 

	
	// === 일정 소분류 삭제하기 === //
	@Override
	public int deleteSubCalendar(String smcatego_no) {
		int n = dao.deleteSubCalendar(smcatego_no); 
		return n;
	}// end of public int deleteSubCalendar(String smcatego_no) ---------------- 


	// === 일정 삭제하기 === //
	@Override
	public int deleteSchedule(String schedule_no) {
		int n = dao.deleteSchedule(schedule_no);
		return n;
	}// end of public int deleteSchedule(String schedule_no) ----------------------- 


	// === 일정 수정하기 === //
	@Override
	public int editSchedule_end(Map<String, String> paraMap) {
		int n = dao.editSchedule_end(paraMap);
		return n;
	}// end of public int editSchedule_end(Map<String, String> paraMap) ------------------ 
	
}
