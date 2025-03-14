package com.syoffice.app.attendance.service;

import java.util.List;
import java.util.Map;
import com.syoffice.app.attendance.domain.AttendanceVO;

public interface AttendanceService {

    // 사원 개인의 오늘 출퇴근 정보 조회
    AttendanceVO getTodayAttendance(String empId);

    // 사원의 출근 처리
    void checkIn(String empId);

    // 사원의 퇴근 처리
    void checkOut(String empId);

    // 캘린더에 표시할 출퇴근/연차 이벤트 조회
    List<Map<String, Object>> getCalendarEvents(Map<String, String> params);

    // 주간 누적 근무 시간 (예: "40h 15m")
    String getWeeklyAccumulated(String empId);

    // 월간 누적 근무 시간 (예: "160h 30m")
    String getMonthlyAccumulated(String empId);

    // 사원의 사용한 연차 수량(또는 연차 내역 중 집계값)
    int getUsedLeave(String empId);

    // 매일 결근 상태 업데이트 (스케줄러에서 호출)
    //void updateAbsenceForEmployees();


    // 특정 지점, 부서의 사원들의 근태 내역 조회 (관리자용)
    // 파라미터 순서는 branchName, deptName, date 로 통일
    List<Map<String, Object>> getDeptAttendance(String branchName, String deptName, String date);

    // 사원의 연차 내역(또는 연차 사용 내역) 조회
    List<Map<String, Object>> getLeaveHistory(String empId);

    // 1) 지점-부서 데이터 조회
	List<Map<String, Object>> getBranchDeptList();

	List<Map<String, Object>> getDeptLeaveHistory(String branchName, String deptName, String date);

	
	
	////////////////////////////////////////////
	List<Map<String, Object>> getBranchList();

	List<Map<String, Object>> getDeptListByBranch(String branchNo);

	List<Map<String, Object>> getDeptAttendanceById(String deptId, String date);

	List<Map<String, Object>> getDeptLeaveHistoryById(String deptId, String date);

	int insertDailyAttendance();

	int updateLeaveCount();

	int updateAbsenceForEmployees();

	List<Map<String, Object>> getLeaveInfo(String empId);

	Map<String, Object> getLeaveInfono(String empId);
	
	

	



}
