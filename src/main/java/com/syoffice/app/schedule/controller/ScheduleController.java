package com.syoffice.app.schedule.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.syoffice.app.common.MyUtil;
import com.syoffice.app.employee.domain.EmployeeVO;
import com.syoffice.app.schedule.service.ScheduleService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/schedule/*")
public class ScheduleController {

	@Autowired
	private ScheduleService service;
	
	
	// === 일정관리 시작 페이지 === //
	@GetMapping("scheduleIndex")
	public ModelAndView showSchedule(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) { 
		
		mav.setViewName("schedule/scheduleIndex");

		return mav;
	}// end of public ModelAndView showSchedule(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) 
	
	
	// === 풀캘린더에서 날짜 클릭할 때 발생하는 이벤트(일정 등록창으로 넘어간다) ===
	@PostMapping("insertSchedule")
	public ModelAndView insertSchedule(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) { 
		
		// form 에서 받아온 날짜
		String chooseDate = request.getParameter("chooseDate");
		
		mav.addObject("chooseDate", chooseDate);
		mav.setViewName("schedule/insertSchedule");
		
		return mav;
	}// end of public ModelAndView insertSchedule(HttpServletRequest request, HttpServletResponse response, ModelAndView mav, @RequestParam String chooseDate) ------ 

	
	// === 일정 등록하기 === //
	@PostMapping("registerSchedule_end")
	public ModelAndView registerSchedule_end(ModelAndView mav, HttpServletRequest request) throws Throwable {
		
		String schedule_startdate = request.getParameter("schedule_startdate");	// 일정시작일
		String schedule_enddate   = request.getParameter("schedule_enddate");	// 일정종료일
		String schedule_name 	  = request.getParameter("schedule_name");		// 일정명
		String fk_lgcatego_no 	  = request.getParameter("fk_lgcatego_no");		// 일정대분류
		String fk_smcatego_no	  = request.getParameter("fk_smcatego_no");		// 일정분류
		String schedule_color 	  = request.getParameter("schedule_color");		// 일정배경색
		String schedule_place 	  = request.getParameter("schedule_place");		// 장소
		String schedule_joinemp	  = request.getParameter("schedule_joinemp");	// 공유
		String schedule_content   = request.getParameter("schedule_content");	// 일정내용
		String fk_emp_id 		  = request.getParameter("fk_emp_id");			// 일정등록자
		
//		System.out.println("~~~ 확인용 schedule_startdate : "+ schedule_startdate);
//		System.out.println("~~~ 확인용 schedule_enddate : "+ schedule_enddate);
//		System.out.println("~~~ 확인용 schedule_name : "+ schedule_name);
//		System.out.println("~~~ 확인용 fk_lgcatego_no : "+ fk_lgcatego_no);
//		System.out.println("~~~ 확인용 fk_smcatego_no : "+ fk_smcatego_no);
//		System.out.println("~~~ 확인용 schedule_color : "+ schedule_color);
//		System.out.println("~~~ 확인용 schedule_place : "+ schedule_place);
//		System.out.println("~~~ 확인용 schedule_joinemp : "+ schedule_joinemp);
//		System.out.println("~~~ 확인용 schedule_content : "+ schedule_content);
//		System.out.println("~~~ 확인용 fk_emp_id : "+ fk_emp_id);
		
		
		Map<String,String> paraMap = new HashMap<String, String>();
		paraMap.put("schedule_startdate", schedule_startdate);
		paraMap.put("schedule_enddate", schedule_enddate);
		paraMap.put("schedule_name", schedule_name);
		paraMap.put("fk_lgcatego_no",fk_lgcatego_no);
		paraMap.put("fk_smcatego_no", fk_smcatego_no);
		paraMap.put("schedule_color", schedule_color);
		paraMap.put("schedule_place", schedule_place);
		paraMap.put("schedule_joinemp", schedule_joinemp);
		paraMap.put("schedule_content", schedule_content);
		paraMap.put("fk_emp_id", fk_emp_id);
		
		
		int n = service.registerSchedule_end(paraMap);

		if(n == 0) {
			mav.addObject("message", "일정 등록에 실패하였습니다.");
		}
		else {
			mav.addObject("message", "일정 등록에 성공하였습니다.");
		}
		
		mav.addObject("loc", request.getContextPath()+"/schedule/scheduleIndex");
		
		mav.setViewName("common/msg");
		
		return mav;
	}// end of public ModelAndView registerSchedule_end(ModelAndView mav, HttpServletRequest request) throws Throwable ----------------

	
	// === 일정상세보기 === //
	@GetMapping(value="detailSchedule")
	public ModelAndView detailSchedule(ModelAndView mav, HttpServletRequest request) {
		
		String scheduleno = request.getParameter("scheduleno");
		
		// 검색하고 나서 취소 버튼 클릭했을 때 필요함
//		String listgobackURL_schedule = request.getParameter("listgobackURL_schedule");
//		mav.addObject("listgobackURL_schedule",listgobackURL_schedule);
		
//		System.out.println("확인용 scheduleno: "+ scheduleno);
		
		// 일정상세보기에서 일정수정하기로 넘어갔을 때 필요함
		String gobackURL_detailSchedule = MyUtil.getCurrentURL(request);
		mav.addObject("gobackURL_detailSchedule", gobackURL_detailSchedule);
		
		try {
			Integer.parseInt(scheduleno);
			Map<String,String> map = service.detailSchedule(scheduleno);
			mav.addObject("map", map);
			mav.setViewName("schedule/detailSchedule");
		} catch (NumberFormatException e) {
			mav.setViewName("redirect:/schedule/scheduleIndex");
		}
		
		return mav;
	}// end of public ModelAndView detailSchedule(ModelAndView mav, HttpServletRequest request, @RequestParam String scheduleno) ------------------- 
	
	
	// === 일정 수정하기 === //
	@PostMapping("editSchedule")
	public ModelAndView editSchedule(ModelAndView mav, HttpServletRequest request) {
		
		String schedule_no = request.getParameter("schedule_no");
   		
		try {
			Integer.parseInt(schedule_no);
			
			String gobackURL_detailSchedule = request.getParameter("gobackURL_detailSchedule");
			
			HttpSession session = request.getSession();
			EmployeeVO loginuser = (EmployeeVO) session.getAttribute("loginuser");
			
			Map<String,String> map = service.detailSchedule(schedule_no);
			
			if( !loginuser.getEmp_id().equals( map.get("fk_emp_id") ) ) {
				String message = "다른 사용자가 작성한 일정은 수정이 불가합니다.";
				String loc = "javascript:history.back()";
				
				mav.addObject("message", message);
				mav.addObject("loc", loc);
				mav.setViewName("msg");
			}
			else {
				mav.addObject("map", map);
				mav.addObject("gobackURL_detailSchedule", gobackURL_detailSchedule);
				
				mav.setViewName("schedule/editSchedule");
			}
			
		} catch (NumberFormatException e) {
			mav.setViewName("redirect:/schedule/scheduleIndex");
		}
		
		
		return mav;
		
	}// end of public ModelAndView editSchedule(ModelAndView mav, HttpServletRequest request) ---------------------------
	
	
	// === 일정 수정 완료하기 ===
	@PostMapping("editSchedule_end")
	public ModelAndView editSchedule_end(@RequestParam Map<String, String> paraMap, HttpServletRequest request, ModelAndView mav) {
		
//		System.out.println("확인용 :" + paraMap.get("schedule_startdate")) ;
		
		try {
			 int n = service.editSchedule_end(paraMap);
			 
			 if(n==1) {
				 mav.addObject("message", "일정을 수정하였습니다.");
				 mav.addObject("loc", request.getContextPath()+"/schedule/scheduleIndex");
			 }
			 else {
				 mav.addObject("message", "일정 수정에 실패하였습니다.");
				 mav.addObject("loc", "javascript:history.back()");
			 }
			 
			 mav.setViewName("common/msg");
		} catch (Throwable e) {	
			e.printStackTrace();
			mav.setViewName("redirect:/schedule/scheduleIndex");
		}
			
		return mav;
	}// end of public ModelAndView editSchedule_end(@RequestParam Map<String, String> paraMap, HttpServletRequest request, ModelAndView mav) ------------------------ 
	
}
