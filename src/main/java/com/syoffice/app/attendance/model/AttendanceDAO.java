package com.syoffice.app.attendance.model;

import java.util.List;
import java.util.Map;

import com.syoffice.app.attendance.domain.AttendanceVO;

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
    // List<Map<String, Object>> getLeaveList(String empId);
    
    // 총 연차 일수 조회
    // int getTotalLeave(String empId);
    
    // 사용한 연차 일수 조회
    // int getUsedLeave(String empId);
}
