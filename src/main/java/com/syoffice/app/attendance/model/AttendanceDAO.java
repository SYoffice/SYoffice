package com.syoffice.app.attendance.model;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.syoffice.app.attendance.domain.AttendanceVO;

@Mapper
public interface AttendanceDAO {
    // 오늘의 출퇴근 정보 조회
    AttendanceVO getTodayAttendance(String empId);
    
    // 출근 처리
    void checkIn(String empId);
    
    // 퇴근 처리
    void checkOut(String empId);
    
    // 캘린더 이벤트 조회 (출근/퇴근 + 연차)
    List<AttendanceVO> getCalendarEvents(String empId);
    
    // 근태 이력(출퇴근 리스트) 조회
   // List<AttendanceVO> getAttendanceHistory(String empId);
    
    // 연차(근태신청) 이력 조회
    // List<AttendanceVO> getLeaveList(String empId);
    
    // 주간 누적 근무 시간 계산
    String getWeeklyAccumulated(String empId);
    
    // 월간 누적 근무 시간 계산
    String getMonthlyAccumulated(String empId);
    
    // 연차 내역 리스트 조회 (각 행을 Map<String, Object>로 구성)
    List<Map<String, Object>> getLeaveList(String empId);
    
    // 총 연차 일수 조회
    // int getTotalLeave(String empId);
    
    // 잔여연차 
    int getUsedLeave(String empId);

	// List<Map<String, Object>> getAllDeptAttendanceStats(String date);

	//List<Map<String, Object>> selectDeptAttendance(int deptId, String date);

	//List<Map<String, Object>> getAllDeptAttendance(String date);

	//List<Map<String, Object>> getDeptAttendance(@Param("deptId") int deptId, @Param("date") String date);
   
	void updateAbsenceForEmployees();

	public List<Map<String, Object>> selectOrganization(@Param("date") String date);


	//List<Map<String, Object>> selectOrganizationByDept(String dept_name, String branch_name, String date);

	List<Map<String, Object>> selectOrganizationByDept(Map<String, Object> param);
	
	// 부서별 연차 통계 
	// List<Map<String, Object>> getAllLeaveHistory();
    
    
    
    
    
}
