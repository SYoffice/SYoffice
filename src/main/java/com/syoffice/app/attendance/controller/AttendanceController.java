package com.syoffice.app.attendance.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.syoffice.app.attendance.domain.AttendanceVO;
import com.syoffice.app.attendance.service.AttendanceService;
import com.syoffice.app.attendance.service.HolidayService;
import com.syoffice.app.employee.domain.EmployeeVO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;
    
    @Autowired
    private HolidayService holidayService;
    
    //일반 사원 근태 페이지
    @GetMapping
    public ModelAndView attendance(HttpSession session) {
        EmployeeVO loginuser = (EmployeeVO) session.getAttribute("loginuser");
        if (loginuser == null) {
            return new ModelAndView("redirect:/login");
        }

        String fkDeptId = String.valueOf(loginuser.getFk_dept_id()).trim();
        if ("2".equals(fkDeptId)) {
            return new ModelAndView("redirect:/attendance/manager");
        }

        return getAttendanceView(session, "attendance/attendance"); 
    }

    // 관리자 근태 페이지
    @GetMapping("/manager")
    public ModelAndView managerAttendance(HttpSession session,
                                          @RequestParam(value = "date", required = false) String date) {
        EmployeeVO loginuser = (EmployeeVO) session.getAttribute("loginuser");
        if (loginuser == null) {
            return new ModelAndView("redirect:/login");
        }

        if (date == null) {
            date = LocalDate.now().toString();
        }

        ModelAndView mav = new ModelAndView("attendance/mattendance"); 
        mav.addObject("date", date);
        return mav;
    }

    

    /**
     *  출퇴근 체크 (출근/퇴근 처리)
     */
    @PostMapping("/check")
    public ModelAndView checkAttendance(@RequestParam("action") String action, HttpSession session) {
        EmployeeVO loginuser = (EmployeeVO) session.getAttribute("loginuser");
        if (loginuser == null) {
            return new ModelAndView("redirect:/login");
        }
        String empId = loginuser.getEmp_id();
        try {
            if ("checkIn".equals(action)) {
                attendanceService.checkIn(empId);
            } else if ("checkOut".equals(action)) {
                attendanceService.checkOut(empId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/attendance");
    }

    /**
     * ✅ 특정 사원의 근태 캘린더 이벤트 조회 API
     */
    @GetMapping("/calendarEvents")
    @ResponseBody
    public ResponseEntity<?> getCalendarEvents(
            @RequestParam(value = "empId", required = false) String empId,
            @RequestParam(value = "year", required = false) String year,
            @RequestParam(value = "month", required = false) String month) {
        if (year == null || month == null) {
            LocalDate today = LocalDate.now();
            year = String.valueOf(today.getYear());
            month = String.format("%02d", today.getMonthValue());
        }
        if (empId == null || empId.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "유효하지 않은 empId"));
        }
        try {
            Map<String, String> params = new HashMap<>();
            params.put("empId", empId);
            params.put("year", year);
            params.put("month", month);
            List<Map<String, Object>> events = attendanceService.getCalendarEvents(params);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "서버 오류 발생"));
        }
    }

    /**
     * 일반 사원 근태 페이지 데이터 조회 (내 근태 정보)
     */
    private ModelAndView getAttendanceView(HttpSession session, String viewName) {
        ModelAndView mav = new ModelAndView(viewName);
        EmployeeVO loginuser = (EmployeeVO) session.getAttribute("loginuser");
        if (loginuser == null) {
            return new ModelAndView("redirect:/login");
        }
        String empId = loginuser.getEmp_id();
        try {
            AttendanceVO todayAtt = attendanceService.getTodayAttendance(empId);
            mav.addObject("attendanceVO", todayAtt);
            mav.addObject("employee", loginuser);
            boolean canCheckIn = (todayAtt == null || todayAtt.getAttendStart() == null);
            boolean canCheckOut = (todayAtt != null && todayAtt.getAttendStart() != null && todayAtt.getAttendEnd() == null);
            mav.addObject("canCheckIn", canCheckIn);
            mav.addObject("canCheckOut", canCheckOut);
            String weeklyAccumulated = attendanceService.getWeeklyAccumulated(empId);
            String monthlyAccumulated = attendanceService.getMonthlyAccumulated(empId);
            mav.addObject("weeklyAccumulated", weeklyAccumulated);
            mav.addObject("monthlyAccumulated", monthlyAccumulated);
        } catch (Exception e) {
            System.err.println("[ERROR] 근태 정보 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return mav;
    }
    /**
     * ✅ 🔹 지점 목록 조회 API
     */
    @GetMapping("/branches")
    @ResponseBody
    public ResponseEntity<?> getAllBranches() {
        try {
            List<Map<String, Object>> branches = attendanceService.getBranchList();
           // System.out.println("[DEBUG] 지점 목록: " + branches);
            return ResponseEntity.ok(branches);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "지점 목록 조회 실패"));
        }
    }

    /**
     *  🔹 선택한 지점의 부서 목록 조회 API
     */
    @GetMapping("/departments")
    @ResponseBody
    public ResponseEntity<?> getDepartmentsByBranch(@RequestParam("branch_no") String branchNo) {
        try {
            List<Map<String, Object>> departments = attendanceService.getDeptListByBranch(branchNo);
           // System.out.println("[DEBUG] 부서 목록: " + departments);
            return ResponseEntity.ok(departments);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "부서 목록 조회 실패"));
        }
    }

    /**
     *  🔹 선택한 부서의 근태 및 연차 데이터 조회 API
     */
    
    @GetMapping("/dataByDept")
    @ResponseBody
    public ResponseEntity<?> getDeptData(
            @RequestParam("deptId") String deptId,
            @RequestParam("date") String date,
            @RequestParam("type") String type) {

        try {
            //System.out.println("[DEBUG] 받은 deptId: " + deptId);
            //System.out.println("[DEBUG] 받은 date: " + date);
            //System.out.println("[DEBUG] 받은 type: " + type);

            List<Map<String, Object>> result;
            if ("attendance".equalsIgnoreCase(type)) {
                result = attendanceService.getDeptAttendanceById(deptId, date);
            } else {
                result = attendanceService.getDeptLeaveHistoryById(deptId, date);
            }

            //System.out.println("[DEBUG] 조회 결과: " + result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("[ERROR] 데이터 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();  // 에러 전체 로그 출력
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "데이터 조회 중 오류 발생"));
        }
    }
    
	///////////// 스케쥴러 //////////////
	/**
	*  오전 12시 출근 전 
	*/
	@Scheduled(cron = "0 0 0 * * ?")  // 매일 자정 실행
	@Transactional
	public void insertDailyAttendance() {
		try {
		    System.out.println("근태 데이터 자동 삽입 실행");
		    LocalDate today = LocalDate.now();
		
		    Map<String, Object> params = new HashMap<>();
		    params.put("today", today);
		
		    int insertedRows = attendanceService.insertDailyAttendance();  
		
		    System.out.println(insertedRows + "개의 근태 데이터가 삽입");
		} catch (Exception e) {
		    System.err.println("근태 데이터 삽입 중 오류 발생: " + e.getMessage());
		    e.printStackTrace();
		}
	}
	
	
	/**
     * 🔹 매일 자정(00:00)마다 실행되는 연차 자동 증가 스케줄러
     */
    @Scheduled(cron = "0 0 0 * * ?")  // 매일 자정 실행
    @Transactional
    public void updateDailyLeaveCount() {
        try {
            System.out.println("[INFO] 연차 자동 증가 실행...");
            int updatedRows = attendanceService.updateLeaveCount();
            System.out.println("[INFO] " + updatedRows + "명의 연차가 증가되었습니다.");
        } catch (Exception e) {
            System.err.println("[ERROR] 연차 자동 증가 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    /**
     * ✅ 🔹 매일 오후 6시 결근 처리 스케줄러
     */
    @Scheduled(cron = "0 0 18 * * ?")  // 매일 오후 6시 실행
    @Transactional
    public void updateAbsenceStatus() {
        try {
            System.out.println("[INFO] 결근 처리 스케줄러 실행...");

            LocalDate today = LocalDate.now();
            String todayStr = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            //  공휴일인지 확인
            boolean isHoliday = holidayService.isHoliday(todayStr);
            if (isHoliday) {
                System.out.println("[INFO] 오늘(" + todayStr + ")은 공휴일이므로 결근 처리를 하지 않습니다.");
                return; // 공휴일이면 결근 처리 중단
            }

            // 공휴일이 아닐 경우 결근 처리 실행 (AttendanceService에서 수행)
            int updatedRows = attendanceService.updateAbsenceForEmployees();
            System.out.println("[INFO] " + updatedRows + "명의 직원이 결근 처리되었습니다.");
        } catch (Exception e) {
            System.err.println("[ERROR] 결근 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }


	
	


    
    

}
