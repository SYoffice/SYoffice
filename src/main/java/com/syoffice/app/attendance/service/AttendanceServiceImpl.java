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
    /*
 
    // 연차 내역 리스트 조회 (각 행을 Map<String, Object>로 구성)
    @Override
    public List<Map<String, Object>> getLeaveList(String empId) {
        
        return attendanceDAO.getLeaveList(empId);
    }

    // 사용자의 총 연차 일수 조회
    @Override
    public int getTotalLeave(String empId) {
    	
        return attendanceDAO.getTotalLeave(empId);
    }

    // 사용한 연차 일수 조회
    @Override
    public int getUsedLeave(String empId) {
       
        return attendanceDAO.getUsedLeave(empId);
    }
    */
}
