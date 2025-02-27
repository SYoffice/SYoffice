package com.syoffice.app.attendance.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.syoffice.app.attendance.model.AttendanceDAO;
import com.syoffice.app.attendance.domain.AttendanceVO;


import java.util.List;
import java.util.Map;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceDAO attendanceDAO;
    


    
    @Override
    public AttendanceVO getTodayAttendance(String empId) {
        return attendanceDAO.getTodayAttendance(empId);
    }
    
    @Override
    public void checkIn(String empId) {
        attendanceDAO.checkIn(empId);
    }
    
    @Override
    public void checkOut(String empId) {
        attendanceDAO.checkOut(empId);
    }
    
    @Override
    public List<AttendanceVO> getCalendarEvents(String empId) {
    	List<AttendanceVO> events = attendanceDAO.getCalendarEvents(empId);
        return events;
    }
    
   /* @Override
    public List<AttendanceVO> getAttendanceHistory(String empId) {
        return attendanceDAO.getAttendanceHistory(empId);
    }*/
    
    /*
    @Override
    public List<AttendanceVO> getLeaveList(String empId) {
        return attendanceDAO.getLeaveList(empId);
    }*/
    
    @Override
    public String getWeeklyAccumulated(String empId) {
        String result = attendanceDAO.getWeeklyAccumulated(empId);
        return result != null ? result : "0h 0m";  // NULL 방지
    }

    @Override
    public String getMonthlyAccumulated(String empId) {
        String result = attendanceDAO.getMonthlyAccumulated(empId);
        return result != null ? result : "0h 0m";  // NULL 방지
    }
   
    // 연차 내역 리스트 조회 (각 행을 Map<String, Object>로 구성)
    @Override
    public List<Map<String, Object>> getLeaveList(String empId) {
        
        return attendanceDAO.getLeaveList(empId);
    }
    /*
    
    // 사용자의 총 연차 일수 조회
    @Override
    public int getTotalLeave(String empId) {
    	
        return attendanceDAO.getTotalLeave(empId);
    }
 */
    // 잔여 연차 
    @Override
    public int getUsedLeave(String empId) {
       
        return attendanceDAO.getUsedLeave(empId);
    }
    
   
    
    @Override
    public void updateAbsenceForEmployees() {
        attendanceDAO.updateAbsenceForEmployees();
    }

    // 조직도 데이터 가져오기
    public List<Map<String, Object>> selectOrganization(String date) {
        return attendanceDAO.selectOrganization(date);  // 기존 메소드 그대로 사용
    }

 	@Override
 	public List<Map<String, Object>> selectOrganizationByDept(Map<String, Object> param) {
 	    System.out.println("✅ 전달받은 파라미터: " + param); // 기존 로그
 	    System.out.println("🟢 실행되는 SQL: SELECT * FROM tbl_attendance WHERE dept_name = '"
 	        + param.get("dept_name") + "' AND branch_name = '" + param.get("branch_name") + "'");

 	    return attendanceDAO.selectOrganizationByDept(param);
 	}


}
