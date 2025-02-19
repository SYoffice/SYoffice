package com.syoffice.app.schedule.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syoffice.app.schedule.service.ScheduleService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/schedule/*")
@RestController
public class ApiScheduleContoller {
	
	@Autowired
	private ScheduleService service;
	
	
	// === 일정 등록시 내캘린더,사내캘린더 선택에 따른 서브캘린더 종류를 알아오기 === //
	@GetMapping("selectSmallCategory")
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
//			System.out.println("확인용 jsArr : "+ jsArr.toString());
		
		return jsArr.toString();
	}// end of public String selectSmallCategory(HttpServletRequest request) --------------------------
	
	
	// === 공유자를 찾기 위한 특정글자가 들어간 회원명단 불러오기 === //
	@GetMapping("insertSchedule/searchJoinUserList")
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
	
	// === 내 일정 소분류 보여주기 === //
	@GetMapping("showMyCalendar")
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
		
//			System.out.println("확인용 jsonArr: "+ jsonArr);
		
		return jsonArr.toString();
	}// end of public String showMyCalendar(HttpServletRequest request) -----------------------
	
	
	// === 일정 소분류 추가하기 === //
	@PostMapping("addCalendar")
	public String addMyCalendar(HttpServletRequest request) throws Throwable {
		
		String smcatego_name = request.getParameter("smcatego_name");
		String fk_emp_id = request.getParameter("fk_emp_id");
		String fk_lgcatego_no = request.getParameter("fk_lgcatego_no");
		
//			System.out.println("확인용 my_smcatego_name : "+ my_smcatego_name);
//			System.out.println("확인용 fk_emp_id : "+ fk_emp_id);
//			System.out.println("확인용 fk_lgcatego_no : "+ fk_lgcatego_no);
		
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
	
	
	// === 전사일정 소분류 수정하기 === //
	@PutMapping("editComCalendar")
	public String editComCalendar(HttpServletRequest request) {
		
		String smcatego_no 		= request.getParameter("smcatego_no");
		String smcatego_name 	= request.getParameter("smcatego_name");
		String fk_emp_id 		= request.getParameter("fk_emp_id");
		String fk_lgcatego_no 	= request.getParameter("fk_lgcatego_no");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("smcatego_no", smcatego_no);
		paraMap.put("smcatego_name", smcatego_name);
		paraMap.put("fk_emp_id", fk_emp_id);
		paraMap.put("fk_lgcatego_no", fk_lgcatego_no);
		
		int n = service.editCalendar(paraMap);
		
		JSONObject jsObj = new JSONObject();
		jsObj.put("n", n);
			
		return jsObj.toString();
	}// end of public String editComCalendar(HttpServletRequest request) ------------------------
	
	// === 내 일정 소분류 수정하기 === //
	@PutMapping("editMyCalendar")
	public String editMyCalendar(HttpServletRequest request) {
		
		String smcatego_no 		= request.getParameter("smcatego_no");
		String smcatego_name 	= request.getParameter("smcatego_name");
		String fk_emp_id 		= request.getParameter("fk_emp_id");
		String fk_lgcatego_no 	= request.getParameter("fk_lgcatego_no");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("smcatego_no", smcatego_no);
		paraMap.put("smcatego_name", smcatego_name);
		paraMap.put("fk_emp_id", fk_emp_id);
		paraMap.put("fk_lgcatego_no", fk_lgcatego_no);
		
		int n = service.editCalendar(paraMap);
		
		JSONObject jsObj = new JSONObject();
		jsObj.put("n", n);
		
		return jsObj.toString();
	}// end of public String editComCalendar(HttpServletRequest request) ------------------------
	
	
	// === 일정 소분류 삭제하기 === //
	@DeleteMapping("deleteSubCalendar")
	public String deleteSubCalendar(HttpServletRequest request) {
		
		String smcatego_no = request.getParameter("smcatego_no");
				
		int n = service.deleteSubCalendar(smcatego_no);
		
		JSONObject jsObj = new JSONObject();
		jsObj.put("n", n);
			
		return jsObj.toString();
	}// end of public String deleteSubCalendar(HttpServletRequest request) --------------------
	
	// === 일정 삭제하기 === //
	@DeleteMapping("deleteSchedule")
	public String deleteSchedule(HttpServletRequest request) {
		
		String schedule_no = request.getParameter("schedule_no");
		
		int n = service.deleteSchedule(schedule_no);
		
		JSONObject jsObj = new JSONObject();
		jsObj.put("n", n);
		
		return jsObj.toString();
	}// end of public String deleteSubCalendar(HttpServletRequest request) -------------------- 
	
	
}
