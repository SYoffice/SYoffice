package com.syoffice.app.approval.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.syoffice.app.approval.domain.ApprovalLineVO;
import com.syoffice.app.approval.domain.ApprovalVO;
import com.syoffice.app.approval.domain.DraftformVO;
import com.syoffice.app.employee.domain.EmployeeVO;
import com.syoffice.app.hr.service.HrService;
import com.syoffice.app.approval.service.ApprovalService;
import com.syoffice.app.common.PagingDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/approval/*")
public class ApprovalController {

	@Autowired
	private ApprovalService apvservice;

	// 로그인한 사용자의 emp_id를 반환하는 메소드
	private String getEmpIdFromSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		EmployeeVO loginuser = (EmployeeVO) session.getAttribute("loginuser");
		return loginuser != null ? loginuser.getEmp_id() : null;
	}

	@GetMapping("approval_main")
	public ModelAndView approval(HttpServletRequest request, ModelAndView mav) { // 메인

		String emp_id = getEmpIdFromSession(request);

		// 내 apr_status 별 전자결재 문서 리스트 조회
		// 내가 기안한 기안인 상태의 전자결재 문서 리스트 조회
		String apr_status = "1";

		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("apr_status", apr_status);
		paraMap.put("emp_id", emp_id);

		List<ApprovalVO> aprList = apvservice.selectAprList(paraMap);

		// 내가 기안한 결재문서중 전결된 문서 리스트
		apr_status = "4";
		paraMap.put("apr_status", apr_status);
		List<ApprovalVO> aprList4 = apvservice.selectAprList(paraMap);

		Map<String, String> paraMap2 = new HashMap<>();
		paraMap2.put("emp_id", emp_id);

		// 내가 결재해야할 문서 리스트 조회
		List<ApprovalVO> myaprList = apvservice.selectMyAprList(paraMap2);

		mav.addObject("aprList", aprList);
		mav.addObject("aprList4", aprList4);
		mav.addObject("myaprList", myaprList);

		mav.setViewName("approval/approval_main");

		return mav;
	}

	// 기안문서함 화면용
	@GetMapping("my_approval_box")
	public ModelAndView my_approval_box(HttpServletRequest request, ModelAndView mav) {

		String emp_id = getEmpIdFromSession(request);

		Map<String, String> paraMap = new HashMap<>();

		////////////////////// 페이징 처리 /////////////////////////
		int curPage = 1;

		try {
			// 존재하지 않는 페이지를 URL을 통해 접근했을 때 예외처리
			curPage = Integer.parseInt(request.getParameter("curPage"));
		} catch (NumberFormatException e) {
			// 1페이지로 보내기
			curPage = 1;
		}

		PagingDTO pagingDTO = new PagingDTO(); // Pagind DTO 초기화
		pagingDTO.setPageSize(5); // 보여줄 페이지 수

		String apr_status = request.getParameter("apr_status");
		paraMap.put("apr_status", apr_status == null ? "0" : apr_status);
		paraMap.put("emp_id", emp_id);

		int totalRowCount = apvservice.selectAprCount(paraMap);

		// URL을 통해 비정상적인 접근을 한 경우 1페이지로 보내기
		if (curPage > totalRowCount || curPage < 1) {
			curPage = 1;
		}

		pagingDTO.setRowSizePerPage(5); // 한 페이지에 보여줄 행 수 가져오기

		pagingDTO.setCurPage(curPage); // 현재 페이지 가져오기
		pagingDTO.setTotalRowCount(totalRowCount); // 전체 행 개수 가져오기
		pagingDTO.pageSetting(); // 페이징 계산 공식

		// 시작행 번호
		String startRno = String.valueOf(pagingDTO.getFirstRow());
		// 마지막 행번호
		String endRno = String.valueOf(pagingDTO.getLastRow());

		// paraMap 에 넣어주기
		paraMap.put("startRno", startRno);
		paraMap.put("endRno", endRno);

		List<ApprovalVO> aprList = apvservice.selectAprList(paraMap);
		mav.addObject("aprList", aprList);
		mav.addObject("pagingDTO", pagingDTO);
		mav.addObject("paraMap", paraMap);
		////////////////////// 페이징 처리 /////////////////////////

		mav.setViewName("approval/my_approval_box");

		return mav;
	}

	// 기안문서함 데이터용 - 페이징 처리 적용 전 ajax 조회
	@ResponseBody
	@GetMapping("my_approval_box_json")
	public List<ApprovalVO> my_approval_box_json(HttpServletRequest request) { // 기안문서함

		String emp_id = getEmpIdFromSession(request);

		// 내 apr_status 별 전자결재 문서 리스트 조회
		Map<String, String> paraMap = new HashMap<>();

		// 내가 기안한 기안인 상태의 전자결재 문서 리스트 조회
		String apr_status = request.getParameter("apr_status");
		paraMap.put("apr_status", apr_status == null ? "0" : apr_status);
		paraMap.put("emp_id", emp_id);

		List<ApprovalVO> aprList = apvservice.selectAprList(paraMap);

		return aprList;
	}

	// 결재대기문서함
	@GetMapping("obtain_approval_box")
	public ModelAndView obtain_approval_box(HttpServletRequest request, ModelAndView mav) {

		String emp_id = getEmpIdFromSession(request);

		Map<String, String> paraMap = new HashMap<>();

		////////////////////// 페이징 처리 /////////////////////////
		int curPage = 1;

		try {
			// 존재하지 않는 페이지를 URL을 통해 접근했을 때 예외처리
			curPage = Integer.parseInt(request.getParameter("curPage"));
		} catch (NumberFormatException e) {
			// 1페이지로 보내기
			curPage = 1;
		}

		PagingDTO pagingDTO = new PagingDTO(); // Pagind DTO 초기화
		pagingDTO.setPageSize(5); // 보여줄 페이지 수

		// 종류
		String type = request.getParameter("type");

		// 기안자
		String emp_name = request.getParameter("emp_name");

		// 부서명
		String dept_name = request.getParameter("dept_name");

		paraMap.put("emp_id", emp_id);
		paraMap.put("type", type);
		paraMap.put("emp_name", emp_name);
		paraMap.put("dept_name", dept_name);

		int totalRowCount = apvservice.selectMyAprCount(paraMap);

		// URL을 통해 비정상적인 접근을 한 경우 1페이지로 보내기
		if (curPage > totalRowCount || curPage < 1) {
			curPage = 1;
		}

		pagingDTO.setRowSizePerPage(5); // 한 페이지에 보여줄 행 수 가져오기

		pagingDTO.setCurPage(curPage); // 현재 페이지 가져오기
		pagingDTO.setTotalRowCount(totalRowCount); // 전체 행 개수 가져오기
		pagingDTO.pageSetting(); // 페이징 계산 공식

		// 시작행 번호
		String startRno = String.valueOf(pagingDTO.getFirstRow());
		// 마지막 행번호
		String endRno = String.valueOf(pagingDTO.getLastRow());

		// paraMap 에 넣어주기
		paraMap.put("startRno", startRno);
		paraMap.put("endRno", endRno);

		List<ApprovalVO> aprList = apvservice.selectMyAprList(paraMap);
		mav.addObject("aprList", aprList);
		mav.addObject("pagingDTO", pagingDTO);
		mav.addObject("paraMap", paraMap);
		////////////////////// 페이징 처리 /////////////////////////

		mav.setViewName("approval/obtain_approval_box");

		return mav;
	}

	// 결재대기문서함 데이터용
	@ResponseBody
	@GetMapping("obtain_approval_box_json")
	public List<ApprovalVO> obtain_approval_box_json(HttpServletRequest request) {

		String emp_id = getEmpIdFromSession(request);

		Map<String, String> paraMap = new HashMap<>();

		// 종류
		String type = request.getParameter("type");

		// 기안자
		String emp_name = request.getParameter("emp_name");

		// 부서명
		String dept_name = request.getParameter("dept_name");

		paraMap.put("emp_id", emp_id);
		paraMap.put("emp_id", emp_id);
		paraMap.put("type", type);
		paraMap.put("emp_name", emp_name);
		paraMap.put("dept_name", dept_name);

		// 내가 결재해야할 문서 리스트 조회
		List<ApprovalVO> myaprList = apvservice.selectMyAprList(paraMap);

		return myaprList;
	}

	// 팀문서함 화면용
	@GetMapping("team_approval_box")
	public ModelAndView team_approval_box(HttpServletRequest request, ModelAndView mav) {

		String emp_id = getEmpIdFromSession(request);
		String type = null;

		Map<String, String> paraMap = new HashMap<>();

		////////////////////// 페이징 처리 /////////////////////////
		int curPage = 1;

		try {
			// 존재하지 않는 페이지를 URL을 통해 접근했을 때 예외처리
			curPage = Integer.parseInt(request.getParameter("curPage"));
		} catch (NumberFormatException e) {
			// 1페이지로 보내기
			curPage = 1;
		}

		PagingDTO pagingDTO = new PagingDTO(); // Pagind DTO 초기화
		pagingDTO.setPageSize(5); // 보여줄 페이지 수

		String param_type = request.getParameter("type");

		// 화면단에서 type 이 넘어오지 않았다면 type의 초기값 그대로 쿼리로 던지고
		// 화면단에서 type 이 넘어왔다면 (param_type이 null 이 아니라면) param_type을 쿼리로 던질 것
		if (param_type != null && !param_type.equals("0")) {
			type = param_type;
		}

		String search_type = request.getParameter("search_type");
		String search_text = request.getParameter("search_text");

		paraMap.put("emp_id", emp_id);
		paraMap.put("type", type);
		paraMap.put("search_type", search_type);
		paraMap.put("search_text", search_text);

		int totalRowCount = apvservice.selectTeamAprCount(paraMap);

		// URL을 통해 비정상적인 접근을 한 경우 1페이지로 보내기
		if (curPage > totalRowCount || curPage < 1) {
			curPage = 1;
		}

		pagingDTO.setRowSizePerPage(5); // 한 페이지에 보여줄 행 수 가져오기

		pagingDTO.setCurPage(curPage); // 현재 페이지 가져오기
		pagingDTO.setTotalRowCount(totalRowCount); // 전체 행 개수 가져오기
		pagingDTO.pageSetting(); // 페이징 계산 공식

		// 시작행 번호
		String startRno = String.valueOf(pagingDTO.getFirstRow());
		// 마지막 행번호
		String endRno = String.valueOf(pagingDTO.getLastRow());

		// paraMap 에 넣어주기
		paraMap.put("startRno", startRno);
		paraMap.put("endRno", endRno);

		System.out.println("team_approval_box paraMap:: " + paraMap);

		List<ApprovalVO> aprList = apvservice.selectTeamAprList(paraMap);
		mav.addObject("aprList", aprList);
		mav.addObject("pagingDTO", pagingDTO);
		mav.addObject("paraMap", paraMap);
		////////////////////// 페이징 처리 /////////////////////////

		mav.setViewName("approval/team_approval_box");

		return mav;
	}

	// 팀문서함 데이터용
	@ResponseBody
	@GetMapping("team_approval_box_json")
	public List<ApprovalVO> team_approval_box_json(HttpServletRequest request) {

		String emp_id = getEmpIdFromSession(request);

		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("emp_id", emp_id);

		List<ApprovalVO> aprList = apvservice.selectTeamAprList(paraMap);

		return aprList;
	}

	// == 전자결재 환경설정 페이지 (결재라인 리스트) == //
	@GetMapping("approval_setting")
	public ModelAndView approval_setting(HttpServletRequest request, ModelAndView mav) {

		String emp_id = getEmpIdFromSession(request);

		List<ApprovalLineVO> aprLineList = apvservice.selectAprLineList(emp_id);

		mav.addObject("aprLineList", aprLineList);
		mav.setViewName("approval/approval_setting");

		return mav;
	}

	// == 결재선 등록 페이지 == //
	@GetMapping("register_apr_line")
	public String registerApprovalLinePage() {

		return "approval/register_apr_line";
	}

	// == 결재선 등록 == //
	@ResponseBody
	@PostMapping("registerAprLine")
	public String registerAprLine(HttpServletRequest request, @RequestBody ApprovalLineVO aprLineVO) {

		String emp_id = getEmpIdFromSession(request);

		aprLineVO.setFk_emp_id(emp_id);

		Integer resultCount = apvservice.registerAprLine(aprLineVO);

		return resultCount.toString(); // 성공 응답
	}

	// == formType 별 기안 상신 (전자결재 작성) 페이지로 이동 == //
	@GetMapping("write")
	public ModelAndView writeFormPage(HttpServletRequest request, ModelAndView mav) {

		String formType = request.getParameter("formType");
		String viewName = "draft_form";

		if (formType.equals("leaveForm")) {
			viewName = "leave_form";
		} else if (formType.equals("expendForm")) {
			viewName = "expend_form";
		}

		mav.addObject("formType", formType);
		mav.setViewName("approval/apr_form/" + viewName);

		return mav;
	}

	// === 다중 체크박스를 사용시 sql문에서 in 절을 처리하는 예제 === //
	@ResponseBody
	@PostMapping("deleteAprLine")
	public int deleteAprLine(HttpServletRequest request, @RequestParam("str_checked") String str_checked) {

		List<String> checkedList = Arrays.asList(str_checked.split(","));

		System.out.println(checkedList);

		// 삭제 잘 되었는지 확인
		int n = apvservice.deleteAprLine(checkedList);

		return n;
	}

	// == 결재선 조회 ajax == //
	@ResponseBody
	@GetMapping("get_approval_line_json")
	public List<ApprovalLineVO> get_approval_line_json(HttpServletRequest request) {

		String emp_id = getEmpIdFromSession(request);

		List<ApprovalLineVO> aprLineList = apvservice.selectAprLineList(emp_id);

		return aprLineList;
	}

	@PostMapping("write_draft_form")
	public ModelAndView writeDraftForm(HttpServletRequest request, ModelAndView mav, ApprovalVO approval,
			@RequestParam(defaultValue = "") String draft_subject,
			@RequestParam(defaultValue = "") String draft_content) {

		String emp_id = getEmpIdFromSession(request);

		Map<String, String> paraMap = new HashMap<>();

		paraMap.put("fk_emp_id", emp_id);
		paraMap.put("draft_subject", draft_subject);
		paraMap.put("draft_content", draft_content);
		paraMap.put("apr_approver", approval.getApr_approver());
		paraMap.put("apr_approver2", approval.getApr_approver2());
		paraMap.put("apr_approver3", approval.getApr_approver3());
		paraMap.put("apr_important", approval.getApr_important());

		apvservice.registerApproval(paraMap, "1");

		mav.setViewName("redirect:/approval/approval_main");

		return mav;
	}

	@PostMapping("write_leave_form")
	public ModelAndView writeLeaveForm(HttpServletRequest request, ModelAndView mav, ApprovalVO approval,
			@RequestParam(defaultValue = "") String leave_type, @RequestParam(defaultValue = "") String leave_startdate,
			@RequestParam(defaultValue = "") String leave_enddate,
			@RequestParam(defaultValue = "") String leave_subject,
			@RequestParam(defaultValue = "") String leave_content) {

		String emp_id = getEmpIdFromSession(request);

		Map<String, String> paraMap = new HashMap<>();

		System.out.println(leave_enddate != null ? leave_enddate : leave_startdate);

		paraMap.put("fk_emp_id", emp_id);
		paraMap.put("leave_type", leave_type);
		paraMap.put("leave_startdate", leave_startdate);
		paraMap.put("leave_enddate", !leave_enddate.equals("") ? leave_enddate : leave_startdate);
		paraMap.put("leave_subject", leave_subject);
		paraMap.put("leave_content", leave_content);
		paraMap.put("apr_approver", approval.getApr_approver());
		paraMap.put("apr_approver2", approval.getApr_approver2());
		paraMap.put("apr_approver3", approval.getApr_approver3());
		paraMap.put("apr_important", approval.getApr_important());

		apvservice.registerApproval(paraMap, "3");

		mav.setViewName("redirect:/approval/approval_main");

		return mav;
	}

	// == 결재선 등록 페이지 == //
	@GetMapping("form_view/{apr_no}")
	public ModelAndView formView(ModelAndView mav, @PathVariable String apr_no) {

		ApprovalVO aprvo = apvservice.selectAprDetail(apr_no);

		mav.addObject("aprvo", aprvo);
		mav.setViewName("approval/form_view");

		return mav;
	}

	@ResponseBody
	@PostMapping("deleteMyForm")
	public int deleteMyForm(String apr_no, String form_type, String form_no) {

		int n = apvservice.deleteMyForm(apr_no, form_type, form_no);

		return n;
	}

	// 결재 승인
	@ResponseBody
	@PostMapping("acceptApr")
	public int acceptApr(HttpServletRequest request, String apr_no) {
		
		String emp_id = getEmpIdFromSession(request);

		int n = apvservice.acceptApr(apr_no, emp_id);

		return n;
	}

	// 결재 반려
	@ResponseBody
	@PostMapping("rejectApr")
	public int rejectApr(HttpServletRequest request, String apr_no, String apr_comment) {

		// 세션 불러오기
		HttpSession session = request.getSession();
		// 로그인 정보
		EmployeeVO loginuser = (EmployeeVO) session.getAttribute("loginuser");

		int n = apvservice.rejectApr(apr_no, apr_comment);

		return n;
	}

}
