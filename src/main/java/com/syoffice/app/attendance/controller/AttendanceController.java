package com.syoffice.app.attendance.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.syoffice.app.attendance.domain.AttendanceVO;
import com.syoffice.app.attendance.service.AttendanceService;
import com.syoffice.app.employee.domain.EmployeeVO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    /**
     * 일반 사원 근태 페이지
     */
    @GetMapping
    public ModelAndView attendance(HttpSession session) {
        EmployeeVO loginuser = (EmployeeVO) session.getAttribute("loginuser");

        if (loginuser == null) {
            return new ModelAndView("redirect:/login");
        }

        // 로그인한 사용자의 부서 ID 확인
        String fkDeptId = String.valueOf(loginuser.getFk_dept_id()).trim();
        System.out.println("[DEBUG] 로그인 유저 fk_dept_id: " + fkDeptId);

        // 관리자인 경우 관리자 페이지로 리디렉트
        if ("2".equals(fkDeptId)) {
            return new ModelAndView("redirect:/attendance/manager");
        }

        return getAttendanceView(session, "attendance/attendance"); // 일반 사원 JSP
    }

    /**
     * 관리자 근태 관리 페이지
     */
    @GetMapping("/manager")
    public ModelAndView managerAttendance(HttpSession session,
                                          @RequestParam(value = "date", required = false) String date) {
        EmployeeVO loginuser = (EmployeeVO) session.getAttribute("loginuser");

        if (loginuser == null) {
            return new ModelAndView("redirect:/login");
        }

        // 관리자가 아닌 경우 일반 페이지로 리디렉트
        String fkDeptId = String.valueOf(loginuser.getFk_dept_id()).trim();
        if (!"2".equals(fkDeptId)) {
            return new ModelAndView("redirect:/attendance");
        }

        // 기본값: 오늘 날짜
        if (date == null) {
            date = LocalDate.now().toString();
        }

        // 부서별 근태 통계 및 연차 내역 조회
        //List<Map<String, Object>> deptAttendanceStats = attendanceService.getAllDeptAttendanceStats(date);
        // List<Map<String, Object>> allLeaveHistory = attendanceService.getAllLeaveHistory();

        ModelAndView mav = new ModelAndView("attendance/mattendance"); // 관리자용 JSP
        //mav.addObject("deptAttendanceStats", deptAttendanceStats);
       // mav.addObject("allLeaveHistory", allLeaveHistory);
        mav.addObject("date", date);

        return mav;
    }
    
    // 매일 밤 12시에 결근 데이터 업데이트
    @Scheduled(cron = "0 0 0 * * ?")  // 매일 자정 실행
    public void updateAbsenceStatus() {
        attendanceService.updateAbsenceForEmployees();
        System.out.println("[INFO] 결근 데이터 자동 업데이트 완료");
    }

    /**
     * 출퇴근 체크
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
                System.out.println("[INFO] 출근 체크 완료 - empId: " + empId);
            } else if ("checkOut".equals(action)) {
                attendanceService.checkOut(empId);
                System.out.println("[INFO] 퇴근 체크 완료 - empId: " + empId);
            }
        } catch (Exception e) {
            System.err.println("[ERROR] 출퇴근 체크 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }

        return new ModelAndView("redirect:/attendance");
    }

    /**
     * 캘린더 이벤트 조회 (JSON 반환)
     */
    @GetMapping("/calendarEvents")
    @ResponseBody
    public List<Map<String, Object>> getCalendarEvents(@RequestParam("empId") String empId) {
        System.out.println("[INFO] getCalendarEvents() 호출, empId=" + empId);
        List<AttendanceVO> attendanceList = attendanceService.getCalendarEvents(empId);

        return attendanceList.stream().map(att -> {
            Map<String, Object> event = new HashMap<>();
            event.put("attendDate", att.getAttendDate() != null ? att.getAttendDate() : "N/A");
            event.put("attendStart", att.getAttendStart() != null ? att.getAttendStart() : "");
            event.put("attendEnd", att.getAttendEnd() != null ? att.getAttendEnd() : "");
            event.put("attendStatus", att.getAttendStatus());
            return event;
        }).collect(Collectors.toList());
    }

   

    /**
     * 근태 페이지 데이터 조회
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
    

    @GetMapping("/list")
    @ResponseBody
    public List<Map<String, Object>> selectOrganization(@RequestParam(value = "date", required = false) String date) {
        
        // 날짜 파라미터가 없으면 오늘 날짜 사용
        if (date == null || date.isEmpty()) {
            date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        List<Map<String, Object>> attData = attendanceService.selectOrganization(date);  // 기존 메소드 그대로 사용

        if (attData == null || attData.isEmpty()) {
            System.out.println("❌ " + date + " 조직도 데이터 없음");
        } else {
            System.out.println("✅ 가져온 조직도 데이터 (" + date + "): " + attData);
        }

        return attData;
    }




    
    @GetMapping("/dataByDept")
    @ResponseBody
    public List<Map<String, Object>> selectOrganizationByDept(
        @RequestParam("dept_name") String dept_name,
        @RequestParam("branch_name") String branch_name,
        @RequestParam(value = "date", required = false) String date) {

    	dept_name = String.valueOf(dept_name);
        branch_name = String.valueOf(branch_name);
    	
       
        System.out.println("✅ 부서 이름: " + dept_name);
        System.out.println("✅ 받은 지점 이름: " + branch_name);
        System.out.println("✅ 받은 날짜 : " + date);

      
        Map<String, Object> param = new HashMap<>();
        param.put("dept_name", dept_name);
        param.put("branch_name", branch_name);
        param.put("date", date);

        return attendanceService.selectOrganizationByDept(param);
    }



    
    
    
}
