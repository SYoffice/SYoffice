package com.syoffice.app.schedule.model;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ScheduleDAO {

	// === 일정 등록시 내캘린더,사내캘린더 선택에 따른 서브캘린더 종류를 알아오기 === //
	List<Map<String, String>> selectSmallCategory(Map<String, String> paraMap);

	// === 공유자를 찾기 위한 특정글자가 들어간 회원명단 불러오기 === //
	List<Map<String, String>> searchJoinUserList(String joinSearchWord);

	// === 일정 등록하기 === //
	int registerSchedule_end(Map<String, String> paraMap);

	// === 내 일정 소분류 보여주기 === //
	List<Map<String, String>> showMyCalendar(String fk_emp_id);

	// === 일정 소분류 추가하기 === //
	int addCalendar(Map<String, String> paraMap);

	// === 이미 등록한 소분류인지 확인하기 === //
	int existCalendar(Map<String, String> paraMap);

	// === 전사 일정 소분류 가져오기 === //
	List<Map<String, String>> showCompanyCalendar();

	// === 등록된 모든 일정 불러오기 === //
	List<Map<String, String>> selectSchedule(Map<String, String> paraMap);

	// === 일정상세보기 === //
	Map<String, String> detailSchedule(String scheduleno);

	// === 소분류 수정 전 소분류명이 존재하는지 알아보기 === //
	int existsCalendar(Map<String, String> paraMap);

	// === 소분류 수정하기 === //
	int editCalendar(Map<String, String> paraMap);

	// === 일정 소분류 삭제하기 === //
	int deleteSubCalendar(String smcatego_no);

	// === 일정 삭제하기 === //
	int deleteSchedule(String schedule_no);

	// === 일정 수정하기 === //
	int editSchedule_end(Map<String, String> paraMap);
	
}
