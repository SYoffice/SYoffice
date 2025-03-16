package com.syoffice.app.attendance.model;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.syoffice.app.attendance.domain.AttendanceVO;

@Mapper
public interface AttendanceDAO {
    AttendanceVO getTodayAttendance(String empId);
    void checkIn(String empId);
    void checkOut(String empId);
    String getWeeklyAccumulated(String empId);
    String getMonthlyAccumulated(String empId);
    int getUsedLeave(String empId);
    int updateAbsenceForEmployees();
    List<Map<String, Object>> selectOrganization(@Param("date") String date);
    List<Map<String, Object>> selectOrganizationByDept(Map<String, Object> param);
    List<Map<String, Object>> getCalendarEvents(Map<String, String> params);
    List<Map<String, Object>> selectLeaveHistory(String empId);
	List<Map<String, Object>> selectBranchDeptList();
	List<Map<String, Object>> selectDeptLeaveHistory(Map<String, Object> params);
	List<Map<String, Object>> selectBranchList();
	List<Map<String, Object>> selectDeptListByBranch(String branchNo);
	List<Map<String, Object>> selectDeptAttendance(String deptId, String date);
	List<Map<String, Object>> selectDeptLeaveHistory(String deptId, String date);
	int insertDailyAttendance();
	int updateLeaveCount();
	int updateAbsenceForEmployees(Map<String, Object> params);
	List<Map<String, Object>> selectLeaveInfo(String empId);
	Map<String, Object> getLeaveInfono(String empId);
	double getRemainingLeave(String empId);
	
}
