package com.syoffice.app.schedule.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.syoffice.app.schedule.service.ScheduleService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
	
	
	// === 일정 등록시 내캘린더,사내캘린더 선택에 따른 서브캘린더 종류를 알아오기 === //
	@GetMapping("selectSmallCategory")
	@ResponseBody
	public String selectSmallCategory(HttpServletRequest request) {
		
		String fk_lgcatego_no = request.getParameter("fk_lgcatego_no"); // 캘린더 대분류 번호
		String fk_emp_id = request.getParameter("fk_emp_id");       // 사용자아이디
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("fk_lgcatego_no", fk_lgcatego_no);
		paraMap.put("fk_emp_id", fk_emp_id);
		
		List<Map<String, String>> small_category_List = service.selectSmallCategory(paraMap);
		
		JSONArray jsArr = new JSONArray();
		if(small_category_List != null) {
			for(Map<String, String> map : small_category_List) {
				JSONObject jsObj = new JSONObject();
				jsObj.put("smcatego_no", map.get("smcatego_no"));
				jsObj.put("fk_lgcatego_no", map.get("fk_lgcatego_no"));
				jsObj.put("smcatego_name", map.get("smcatego_name"));
				
				jsArr.put(jsObj);
			}
		}
//		System.out.println("확인용 jsArr : "+ jsArr.toString());
		
		return jsArr.toString();
	}// end of public String selectSmallCategory(HttpServletRequest request) --------------------------
	
	
	// === 공유자를 찾기 위한 특정글자가 들어간 회원명단 불러오기 === //
	@GetMapping("insertSchedule/searchJoinUserList")
	@ResponseBody
	public String searchJoinUserList(HttpServletRequest request) {
		String joinSearchWord = request.getParameter("joinSearchWord");
		
		// 사원 명단 불러오기
		List<Map<String, String>> joinUserList = service.searchJoinUserList(joinSearchWord);

		JSONArray jsonArr = new JSONArray();
		if(joinUserList != null && joinUserList.size() > 0) {
			for(Map<String, String> map : joinUserList) {
				JSONObject jsObj = new JSONObject();
				jsObj.put("empid", map.get("emp_id"));
				jsObj.put("name", map.get("name"));
				jsObj.put("dept_name", map.get("dept_name"));
				jsObj.put("branch_name", map.get("branch_name"));
				
				jsonArr.put(jsObj);
			}
		}
		return jsonArr.toString();
	}// end of public String searchJoinUserList(HttpServletRequest request) ------------------------- 
	
	
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
	
	
	
	// === 내 일정 소분류 보여주기 === //
	@GetMapping("showMyCalendar")
	@ResponseBody
	public String showMyCalendar(HttpServletRequest request) {
		
		String fk_emp_id = request.getParameter("fk_emp_id");
		
		List<Map<String, String>> mycalendar_small_categoryList = service.showMyCalendar(fk_emp_id);
		
		JSONArray jsonArr = new JSONArray();
		
		if(mycalendar_small_categoryList != null) {
			for(Map<String, String> map : mycalendar_small_categoryList) {
				JSONObject jsObj = new JSONObject();
				
				jsObj.put("smcatego_no", map.get("smcatego_no"));
				jsObj.put("fk_lgcatego_no", map.get("fk_lgcatego_no"));
				jsObj.put("smcatego_name", map.get("smcatego_name"));
				
				jsonArr.put(jsObj);
			}// end of for() --------------
		}
		
//		System.out.println("확인용 jsonArr: "+ jsonArr);
		
		return jsonArr.toString();
	}// end of public String showMyCalendar(HttpServletRequest request) -----------------------
	
	
	// === 일정 소분류 추가하기 === //
	@PostMapping("addCalendar")
	@ResponseBody
	public String addMyCalendar(HttpServletRequest request) throws Throwable {
		
		String smcatego_name = request.getParameter("smcatego_name");
		String fk_emp_id = request.getParameter("fk_emp_id");
		String fk_lgcatego_no = request.getParameter("fk_lgcatego_no");
		
//		System.out.println("확인용 my_smcatego_name : "+ my_smcatego_name);
//		System.out.println("확인용 fk_emp_id : "+ fk_emp_id);
//		System.out.println("확인용 fk_lgcatego_no : "+ fk_lgcatego_no);
		
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("smcatego_name",smcatego_name);
		paraMap.put("fk_emp_id",fk_emp_id);
		paraMap.put("fk_lgcatego_no",fk_lgcatego_no);
		
		int n = service.addCalendar(paraMap);
				
		JSONObject jsObj = new JSONObject();
		jsObj.put("n", n);
		
		return jsObj.toString();
	}// end of public String addMyCalendar(HttpServletRequest request) throws Throwable ------------------------- 
	
	
	// === 전사 일정 소분류 가져오기 === //
	@GetMapping("showCompanyCalendar")
	@ResponseBody
	public String showCompanyCalendar() {
		
		List<Map<String, String>> calendar_small_category_CompanyList = service.showCompanyCalendar();
		
		JSONArray jsonArr = new JSONArray();
		
		if(calendar_small_category_CompanyList != null) {
			for(Map<String, String> map : calendar_small_category_CompanyList) {
				JSONObject jsObj = new JSONObject();
				jsObj.put("smcatego_no", map.get("smcatego_no"));
				jsObj.put("fk_lgcatego_no", map.get("fk_lgcatego_no"));
				jsObj.put("smcatego_name", map.get("smcatego_name"));
				
				jsonArr.put(jsObj);
			}// end of for() -------------------
		}
		
		return jsonArr.toString();
	}// end of public String showCompanyCalendar() -------------------------------------- 
	
	
	// === 등록된 모든 일정 불러오기 === //
	@GetMapping("selectSchedule")
	@ResponseBody
	public String selectSchedule(HttpServletRequest request) {
		
		// 등록된 일정 가져오기
		String fk_emp_id = request.getParameter("fk_emp_id");
		String name = request.getParameter("name");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("fk_emp_id", fk_emp_id);
		paraMap.put("name", name);
		
		List<Map<String, String>> scheduleList = service.selectSchedule(paraMap);
		
		JSONArray jsonArr = new JSONArray();
		
		if(scheduleList != null && scheduleList.size() > 0) {
			
			for(Map<String, String> map : scheduleList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("schedule_no", map.get("schedule_no"));
				jsonObj.put("schedule_startdate", map.get("schedule_startdate"));
				jsonObj.put("schedule_enddate", map.get("schedule_enddate"));
				jsonObj.put("schedule_name", map.get("schedule_name"));
				jsonObj.put("schedule_color", map.get("schedule_color"));
				jsonObj.put("schedule_place", map.get("schedule_place"));
				jsonObj.put("schedule_joinemp", map.get("schedule_joinemp"));
				jsonObj.put("schedule_content", map.get("schedule_content"));
				jsonObj.put("fk_smcatego_no", map.get("fk_smcatego_no"));
				jsonObj.put("fk_lgcatego_no", map.get("fk_lgcatego_no"));
				jsonObj.put("fk_emp_id", map.get("fk_emp_id"));
			
				jsonArr.put(jsonObj);
			}// end of for-------------------------------------
		}
		
		return jsonArr.toString();
	}// end of public String selectSchedule(HttpServletRequest request) -------------------------------- 
	
}
