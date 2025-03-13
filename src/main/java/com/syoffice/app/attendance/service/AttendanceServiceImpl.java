package com.syoffice.app.attendance.service;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.syoffice.app.attendance.model.AttendanceDAO;
import com.syoffice.app.attendance.domain.AttendanceVO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    public String getWeeklyAccumulated(String empId) {
        String result = attendanceDAO.getWeeklyAccumulated(empId);
        return result != null ? result : "0h 0m";
    }

    @Override
    public String getMonthlyAccumulated(String empId) {
        String result = attendanceDAO.getMonthlyAccumulated(empId);
        return result != null ? result : "0h 0m";
    }

    @Override
    public int getUsedLeave(String empId) {
        return attendanceDAO.getUsedLeave(empId);
    }

    @Override
    public List<Map<String, Object>> getCalendarEvents(Map<String, String> params) {
        return attendanceDAO.getCalendarEvents(params);
    }

    /**
     * ✅ 결근 처리: 출근 기록이 없는 경우 직원들의 상태를 결근으로 변경
     */
    @Override
    @Transactional
    public int updateAbsenceForEmployees() {
        return attendanceDAO.updateAbsenceForEmployees();
    }

    

    @Override
    public List<Map<String, Object>> getLeaveHistory(String empId) {
        return attendanceDAO.selectLeaveHistory(empId);
    }

    @Override
    public List<Map<String, Object>> getDeptAttendance(String branchName, String deptName, String date) {
        Map<String, Object> params = new HashMap<>();
        params.put("branch_name", branchName);
        params.put("dept_name", deptName);
        params.put("date", date);
        return attendanceDAO.selectOrganizationByDept(params);
    }

    @Override
    public List<Map<String, Object>> getBranchDeptList() {
        return attendanceDAO.selectBranchDeptList();
    }

    @Override
    public List<Map<String, Object>> getDeptLeaveHistory(String branchName, String deptName, String date) {
        Map<String, Object> params = new HashMap<>();
        params.put("branch_name", branchName);
        params.put("dept_name", deptName);
        params.put("date", date);
        return attendanceDAO.selectDeptLeaveHistory(params);
    }

    @Override
    public List<Map<String, Object>> getBranchList() {
        return attendanceDAO.selectBranchList();
    }

    @Override
    public List<Map<String, Object>> getDeptListByBranch(String branchNo) {
        return attendanceDAO.selectDeptListByBranch(branchNo);
    }

    @Override
    public List<Map<String, Object>> getDeptAttendanceById(String deptId, String date) {
        return attendanceDAO.selectDeptAttendance(deptId, date);
    }

    @Override
    public List<Map<String, Object>> getDeptLeaveHistoryById(String deptId, String date) {
        return attendanceDAO.selectDeptLeaveHistory(deptId, date);
    }

    @Override
    public int insertDailyAttendance() { 
        return attendanceDAO.insertDailyAttendance();
    }

    @Override
    public int updateLeaveCount() {
        return attendanceDAO.updateLeaveCount();
    }
/*
    @Override
    public Map<String, Object> getLeaveInfo(String empId) {
        Map<String, Object> leaveInfo = attendanceDAO.selectLeaveInfo(empId);

        // 🔹 MyBatis에서 받은 원본 데이터 확인
        System.out.println("✅ MyBatis에서 받은 leaveInfo: " + leaveInfo);

        if (leaveInfo == null) {
            System.out.println("❌ leaveInfo가 NULL 입니다! 기본값을 반환합니다.");
            leaveInfo = new HashMap<>();
            leaveInfo.put("usedLeave", 0);
            leaveInfo.put("leaveCount", 0);
            return leaveInfo;
        }

        // 🔹 대소문자 문제 해결: LEAVECOUNT → leaveCount, USEDLEAVE → usedLeave
        int usedLeave = leaveInfo.containsKey("USEDLEAVE") ? Integer.parseInt(leaveInfo.get("USEDLEAVE").toString()) : 0;
        int leaveCount = leaveInfo.containsKey("LEAVECOUNT") ? Integer.parseInt(leaveInfo.get("LEAVECOUNT").toString()) : 0;

        // 🔹 변환 후 값 확인
        System.out.println("🟢 변환 후 사용한 연차: " + usedLeave);
        System.out.println("🟢 변환 후 잔여 연차: " + leaveCount);

        leaveInfo.put("usedLeave", usedLeave);
        leaveInfo.put("leaveCount", leaveCount);

        return leaveInfo;
    }
*/

    @Override
    public List<Map<String, Object>> getLeaveInfo(String empId) {
        // 🔹 DAO 호출: selectLeaveInfo(empId)가 List<Map<String, Object>> 형태로 반환되도록 수정
        return attendanceDAO.selectLeaveInfo(empId);
    }
    
    
    
}
