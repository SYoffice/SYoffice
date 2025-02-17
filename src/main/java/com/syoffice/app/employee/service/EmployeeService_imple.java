package com.syoffice.app.employee.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.syoffice.app.common.Sha256;
import com.syoffice.app.employee.domain.EmployeeVO;
import com.syoffice.app.employee.model.EmployeeDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

// 서비스(Service) 선언
@Service
public class EmployeeService_imple implements EmployeeService {
	
	@Autowired	// Type 에 따라 알아서 Bean 을 주입해준다.
	private EmployeeDAO dao;
	
	
	// 로그인 처리
	@Override
	public ModelAndView login(Map<String, String> paraMap, ModelAndView mav, HttpServletRequest request) {
		
		paraMap.put("password", Sha256.encrypt(paraMap.get("password"))); // 비밀번호를 암호화 시키기
		
		// 로그인 메소드 실행 후 EmployeeVO loginuser 에 저장
		EmployeeVO loginuser = dao.login(paraMap);
		
		// 로그인에 성공한 경우
		if(loginuser != null) {
			
			// 메모리에 생성되어져 있는 session 을 불러오기
			HttpSession session = request.getSession();

			// session(세션)에 로그인 되어진 사용자 정보인 loginuser 를 저장
			session.setAttribute("loginuser", loginuser);
			
			// 마지막 비밀번호 변경일자가 3개월 이상 경과 시 
			if(loginuser.getPwdchangegap() >= 3) {
				// 비밀번호 변경이 필요한 상태로 전환
				loginuser.setPwdchangestatus("0");	
				// tbl_employee 에서 비밀번호 변경이 필요한 상태로 update
				dao.updatePwdChangeStatus(paraMap.get("emp_id"));
			}
			
			// 비밀번호 변경이 필요한 상태일 경우
			if("0".equals(loginuser.getPwdchangestatus())) {  
				String message = "비밀번호 변경이 필요하여 페이지로 이동합니다";
				String loc = request.getContextPath() + "/employee/pwdChange";	// 비밀번호 변경페이지로 바로 이동 
				
				mav.addObject("message", message);
				mav.addObject("loc", loc);
	
				mav.setViewName("common/msg");
			}
			
			// 비밀번호 변경이 필요없이 로그인에 성공한 경우
			else {
				String message = "로그인 성공!!";
				String loc = request.getContextPath() + "/index";	// 메인페이지로 이동시키기 
				
				mav.addObject("message", message);
				mav.addObject("loc", loc);
	
				mav.setViewName("common/msg");
			}
			
		}// end of if(loginuser != null) -----
		
		// 로그인에 실패한 경우
		if(loginuser == null) {
			String message = "아이디 또는 암호가 틀립니다.";
			String loc = "javascript:history.back()";

			mav.addObject("message", message);
			mav.addObject("loc", loc);

			mav.setViewName("common/msg");
		}// end of if(loginuser == null) -----
		
		return mav;
	}// end of public ModelAndView login(Map<String, String> paraMap, ModelAndView mav, HttpServletRequest request) -----
	

	

}
