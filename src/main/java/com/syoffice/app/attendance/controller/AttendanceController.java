package com.syoffice.app.attendance.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.syoffice.app.attendance.domain.AttendanceVO;
import com.syoffice.app.attendance.service.AttendanceService;
import com.syoffice.app.employee.domain.EmployeeVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping
    public ModelAndView attendance(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return getAttendanceView(session); // 아래 private 메서드에서 JSP 세팅
    }

    // 출퇴근  
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
        // 출퇴근 후 다시 메인 페이지
        return getAttendanceView(session);
    }

    // 캘린더 
    @GetMapping("/calendarEvents")
    @ResponseBody
    public List<Map<String, Object>> getCalendarEvents(@RequestParam("empId") String empId) {
        System.out.println("[INFO] getCalendarEvents() 호출, empId=" + empId);

        List<AttendanceVO> attendanceList = attendanceService.getCalendarEvents(empId);

       
        return attendanceList.stream().map(att -> {
            Map<String, Object> event = new HashMap<>();
            event.put("attendDate",  att.getAttendDate()  != null ? att.getAttendDate()  : "N/A");
            event.put("attendStart", att.getAttendStart() != null ? att.getAttendStart() : "");
            event.put("attendEnd",   att.getAttendEnd()   != null ? att.getAttendEnd()   : "");
            event.put("attendStatus", att.getAttendStatus());
            return event;
        }).collect(Collectors.toList());
    }

    /**
      연차  
     
    @GetMapping("/leave/getLeaveList")
    @ResponseBody
    public Map<String, Object> getLeaveList(@RequestParam("empId") String empId, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> leaveList = attendanceService.getLeaveList(empId);
            int totalLeave = attendanceService.getTotalLeave(empId);
            int usedLeave = attendanceService.getUsedLeave(empId);
            int remainingLeave = totalLeave - usedLeave;
            
            EmployeeVO loginuser = (EmployeeVO) session.getAttribute("loginuser");
            String userName = (loginuser != null) ? loginuser.getName() : "";
            
            result.put("totalLeave", totalLeave);
            result.put("usedLeave", usedLeave);
            result.put("remainingLeave", remainingLeave);
            result.put("userName", userName);
            result.put("leaveList", leaveList);
        } catch (Exception e) {
            System.err.println("[ERROR] 연차 정보를 가져오는 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            result.put("error", "연차 정보를 가져오는 중 오류 발생");
        }
        return result;
    }
*/
    
    
    private ModelAndView getAttendanceView(HttpSession session) {
        ModelAndView mav = new ModelAndView("attendance/attendance");
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

            String weeklyAccumulated   = attendanceService.getWeeklyAccumulated(empId);
            String monthlyAccumulated  = attendanceService.getMonthlyAccumulated(empId);

            mav.addObject("weeklyAccumulated",   weeklyAccumulated);
            mav.addObject("monthlyAccumulated",  monthlyAccumulated);

           

        } catch (Exception e) {

            e.printStackTrace();
        }

        return mav;
    }
}
