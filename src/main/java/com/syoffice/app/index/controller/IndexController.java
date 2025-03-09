package com.syoffice.app.index.controller;

import com.syoffice.app.employee.domain.EmployeeVO;
import com.syoffice.app.index.domain.IndexVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/*")
public class IndexController {

    @GetMapping("/")
    public String root(HttpServletRequest request) {
        return "redirect:/login";
    }

    @GetMapping("login")
    public String login(HttpServletRequest request) {
        return "/main/login"; // /WEB-INF/views/main/login.jsp
    }
    
    @GetMapping("logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login"; // 로그아웃 후 로그인 페이지로 이동
    }
    
    @GetMapping("index")  // http://localhost:9090/myspring/index
    public String index(HttpServletRequest request) {
        HttpSession session = request.getSession();
        
        // 세션에 저장된 객체는 EmployeeVO입니다.
        EmployeeVO emp = (EmployeeVO) session.getAttribute("loginuser");
        
        System.out.println("아이디: " + emp);
        
        if (emp == null) {
            return "redirect:/login";
        }
        
        IndexVO loginUser = new IndexVO();
        BeanUtils.copyProperties(emp, loginUser);
        
        // JSP에서 사용할 수 있도록 request에 담습니다.
        request.setAttribute("loginUser", loginUser);
        return "/main/index"; // /WEB-INF/views/main/index.jsp
    }
}
