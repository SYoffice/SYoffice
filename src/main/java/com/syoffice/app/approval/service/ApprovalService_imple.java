package com.syoffice.app.approval.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import com.syoffice.app.approval.domain.ApprovalLineVO;
import com.syoffice.app.approval.domain.ApprovalVO;
import com.syoffice.app.approval.domain.LeaveformVO;
import com.syoffice.app.approval.model.ApprovalDAO;

@Service
public class ApprovalService_imple implements ApprovalService {

	// 자동 결재선 조회
	@Autowired
	private ApprovalDAO mapper_dao;

	@Override
	public List<ApprovalLineVO> selectAprLineList(String emp_id) {
		return mapper_dao.selectAprLineList(emp_id);
	}

	
	// 자동 결재선 등록
	@Override
	public int registerAprLine(ApprovalLineVO aprLineVO) {

		int result = 0;

		Map<String, String> paraMap = new HashMap<String, String>();

		paraMap.put("fk_emp_id", aprLineVO.getFk_emp_id());
		paraMap.put("apline_name", aprLineVO.getApline_name());
		paraMap.put("apline_approver", aprLineVO.getApline_approver());
		paraMap.put("apline_approver2", aprLineVO.getApline_approver2());
		paraMap.put("apline_approver3", aprLineVO.getApline_approver3());

		result += mapper_dao.registerApprovalLine(paraMap);
		return result;
	}



	@Override
	public int deleteAprLine(List<String> checkedList) {
		return mapper_dao.deleteAprLine(checkedList);
	}

	// 내 apr_status 별 전자결재 문서 리스트 개수 조회
	@Override
	public int selectAprCount(Map<String, String> paraMap) {
		return mapper_dao.selectAprCount(paraMap);
	}
	
	// 내 apr_status 별 전자결재 문서 리스트 조회
	@Override
	public List<ApprovalVO> selectAprList(Map<String, String> paraMap) {
		List<ApprovalVO> list = mapper_dao.selectAprList(paraMap);
		return list;
	}


	
	// 전자결재 등록 formType 1: 업무품의서, 2: 지출결의서, 3: 근태신청서
	@Transactional(value="transactionManager_apr")
	@Override
	public int registerApproval(Map<String, String> paraMap, String formType) {
		// key 채번
		int nextSeq = 0;
		
		if (formType.equals("1")) {
			nextSeq = mapper_dao.getDraftKey();
		}
		else if (formType.equals("3")) {
			nextSeq = mapper_dao.getLeaveKey();
		}
		
		// 날짜+시간+seq 로 채번
		Date now = new Date();
        SimpleDateFormat smdatefm = new SimpleDateFormat("yyMMddHHmm");
        
        String no = smdatefm.format(now) + nextSeq;
        
        int result = 0;
        
        if (formType.equals("1")) {
        	paraMap.put("draft_no", no);
        	// 품의서 저장
    		result = mapper_dao.insertDraft(paraMap);
			paraMap.put("fk_draft_no", no);
        }
        
        if (formType.equals("3")) {
        	paraMap.put("leave_no", no);
        	// 근태신청서 저장
			result = mapper_dao.insertLeave(paraMap);
			paraMap.put("fk_leave_no", no);
	    }

    	paraMap.put("type", formType);
        int a_result = mapper_dao.insertApr(paraMap);
		
		return result == 1 && a_result == 1 ? 1 : 0;
	}

	// 내가 결재해야할 문서 리스트 개수 조회
	@Override
	public int selectMyAprCount(Map<String, String> paraMap) {
		return mapper_dao.selectMyAprCount(paraMap);
	}

	// 내가 결재해야할 문서 리스트 조회
	@Override
	public List<ApprovalVO> selectMyAprList(Map<String, String> paraMap) {
		
		List<ApprovalVO> selectMyAprList = mapper_dao.selectMyAprList(paraMap);
		return selectMyAprList;  
	}

	// 팀문서함 총 개수 조회
	@Override
	public int selectTeamAprCount(Map<String, String> paraMap) {
		return mapper_dao.selectTeamAprCount(paraMap);		
	}

	// 팀문서함 조회
	@Override
	public List<ApprovalVO> selectTeamAprList(Map<String, String> paraMap) {
		List<ApprovalVO> selectTeamAprList = mapper_dao.selectTeamAprList(paraMap);
		
		return selectTeamAprList;
	}

	// 상세 조회
	@Override
	public ApprovalVO selectAprDetail(String apr_no) {
		return mapper_dao.selectAprDetail(apr_no);
	}

	// 내기안문서삭제
	@Transactional(value="transactionManager_apr")
	@Override
	public int deleteMyForm(String apr_no, String form_type, String form_no) {
		
		int result2 = mapper_dao.deleteApproval(apr_no);
		int result = 0;
		
		if (form_type.equals("1")) {
			result = mapper_dao.deleteDraft(form_no);
		}
		
		if (form_type.equals("3")) {
			result = mapper_dao.deleteLeave(form_no);
		}
		
		return (result + result2) == 2 ? 1 : 0;
	}

	// 결재 승인
	@Transactional(value="transactionManager_apr")
	@Override
	public int acceptApr(String apr_no, String emp_id) { 
		Map<String, String> paraMap = new HashMap<>();
		
		ApprovalVO aprvo = mapper_dao.selectApr(apr_no);
		
		boolean isFirst = false; // 처음 결재승인 인지
		boolean isLast = false; // 마지막 결재자인건지?
		
		// 결재자가 몇명인지 확인 
		int apr_approver_cnt = 1;
		
		String apr_approver2 = aprvo.getApr_approver2();
		String apr_approver3 = aprvo.getApr_approver3();
		
		if(!"".equals(apr_approver2) && apr_approver2 != null) {
			apr_approver_cnt++;
		}
		
		if(!"".equals(apr_approver3) && apr_approver3 != null) {
			apr_approver_cnt++;
		}
		
		// 최종 결재자가 승인한지 여부
		boolean isAcceptedLastApr = false;

		// 결재자가 한명도 승인을 안했을 경우
		int accepted_cnt = 0; // 승인한 결재자 
		
		String acceptDay1 = aprvo.getApr_acceptday1();
		String acceptDay2 = aprvo.getApr_acceptday2();
		String acceptDay3 = aprvo.getApr_acceptday3();
		
		if (acceptDay1 == null && acceptDay2 == null && acceptDay3 == null) {
			isFirst = true;
		}
		if (acceptDay1 != null) {
			accepted_cnt++;
		}
		if (acceptDay2 != null) {
			accepted_cnt++;
		}
		if (acceptDay3 != null) {
			accepted_cnt++;
		}
		
		// 결재자수와 (승인한 결재자 수 +1)가 같으면 마지막임
		if (apr_approver_cnt == (accepted_cnt +1)) {
			isLast = true;
		}
		

		// 최종 결재자가 승인했을때 
		if (apr_approver_cnt == 2 && apr_approver2.equals(emp_id)) {
			isAcceptedLastApr = true;
		}
		
		if (apr_approver_cnt == 3 && apr_approver3.equals(emp_id)) {
			isAcceptedLastApr = true;
		}
		
		// status를 확인해서 update를 2, 3, 4중 뭘로 할건지 
		if (isFirst) {
			paraMap.put("status", "2");
		}

		// 결재자 모두가 승인을 완료한 경우(status,APR_ENDDATE, APR_ACCEPTDAY1, APR_ACCEPTDAY2 ,APR_ACCEPTDAY3)
		// 혹은 최종 결재자가 승인한 경우
		if (isLast || isAcceptedLastApr) {
			paraMap.put("status", "4");

		}
	
		paraMap.put("emp_id", emp_id);
		paraMap.put("apr_no", apr_no);
		int acceptApr = mapper_dao.acceptApr(paraMap);
		
		// 연차감소 및 문서 테이블에 필요한 기안자의 아이디
		paraMap.put("requester_id", aprvo.getFk_emp_id());
		
		if((isLast || isAcceptedLastApr) && acceptApr == 1) {
			// 근태신청서일 경우 연차 삭감
			if (aprvo.getType().equals("3")) {
				LeaveformVO leaveform = mapper_dao.selectLeave(aprvo.getFk_leave_no());
				String count = "1";
				if (leaveform.getType().equals("2")) {
					count = "0.5";
				}
				if (leaveform.getType().equals("1")) {
			        count = String.valueOf(calDateBetween(leaveform.getLeave_startdate(), leaveform.getLeave_enddate()));
				}
				paraMap.put("count", count);
				
				// 삭감~
				mapper_dao.subtractLeaveCount(paraMap);
				
				// doc_no 채번 및 세팅
				paraMap.put("doc_no", "DOC_" + aprvo.getFk_leave_no());
			} else {
				// 업무품의서 doc_no 채번 및 세팅
				paraMap.put("doc_no", "DOC_" + aprvo.getFk_draft_no());
			}
			
			mapper_dao.insertDoc(paraMap);
			
			if (aprvo.getType().equals("3")) {
				paraMap.put("leave_no", aprvo.getFk_leave_no());
				mapper_dao.updateLeaveDocno(paraMap);
			} else if(aprvo.getType().equals("1")) {
				paraMap.put("draft_no", aprvo.getFk_draft_no());
				mapper_dao.updateDraftDocno(paraMap);
			}
		}
		
		return acceptApr;
	}


	// 결재 반려
	@ResponseBody
	@Override
	public int rejectApr(String apr_no, String apr_comment) {
		return mapper_dao.rejectApr(apr_no, apr_comment);
	}
	
	// 두 날짜 사이 차이를 구함
	private int calDateBetween(String start, String end) {

		// 날짜 문자열을 LocalDate로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate startDate = LocalDate.parse(start, formatter);
        LocalDate endDate = LocalDate.parse(end, formatter);
        
        int daysBetween = startDate.until(endDate).getDays();
        
        // 날짜 차이 계산
        return daysBetween + 1;
	}



}
