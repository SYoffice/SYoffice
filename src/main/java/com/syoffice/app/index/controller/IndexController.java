package com.syoffice.app.index.controller;

import com.syoffice.app.index.service.IndexService;
import com.syoffice.app.employee.domain.EmployeeVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/*")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("login")
    public String login() {
        return "/main/login"; // /WEB-INF/views/main/login.jsp
    }

    @GetMapping("index")
    public String index(HttpServletRequest request) {
        HttpSession session = request.getSession();
        
        // 로그인한 사용자 정보 세션에서 가져오기
        EmployeeVO loginUser = (EmployeeVO) session.getAttribute("loginuser");

        if (loginUser == null) {
            return "redirect:/login";  // 로그인 정보 없으면 로그인 페이지로 이동
        }

        String fk_emp_id = String.valueOf(loginUser.getEmp_id()); // 세션에서 가져온 사원 ID

        // 내 일정 가져오기
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("fk_emp_id", fk_emp_id);
        List<Map<String, String>> mySchedule = indexService.getMySchedule(paramMap);

        // 내가 속한 부서 일정 가져오기
        Map<String, String> deptParamMap = new HashMap<>();
        deptParamMap.put("fk_emp_id", fk_emp_id);
        List<Map<String, String>> deptSchedule = indexService.getDeptSchedule(deptParamMap);

         // 공지사항 목록 조회
        List<Map<String, String>> noticeList = indexService.getNoticeList(); 
        //System.out.println(">>> noticeList.size() = " + noticeList);


        // 내 실적 가져오기 (최근 7일)
        paramMap.put("fk_emp_id", fk_emp_id);
        List<Map<String, Object>> weeklyPerformance = indexService.getWeeklyPerformance(paramMap);

        // 부서별 실적 가져오기
        List<Map<String, Object>> departmentPerformance = indexService.getDepartmentPerformance();
        
        // JSP로 데이터 전달
        request.setAttribute("loginUser", loginUser);
        request.setAttribute("mySchedule", mySchedule);
        request.setAttribute("deptSchedule", deptSchedule);
        request.setAttribute("noticeList", noticeList);
        request.setAttribute("weeklyPerformance", weeklyPerformance);
        request.setAttribute("departmentPerformance", departmentPerformance);
        

        return "/main/index"; // /WEB-INF/views/main/index.jsp
    }
}
