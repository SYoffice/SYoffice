package com.syoffice.app.approval.service;

import java.util.List;
import java.util.Map;

import com.syoffice.app.approval.domain.ApprovalLineVO;
import com.syoffice.app.approval.domain.ApprovalVO;

public interface ApprovalService {

	// === 결재선 ===
	
	// 자동 결재선 조회
	List<ApprovalLineVO> selectAprLineList(String emp_id);
	
	// 자동 결재선 등록
	int registerAprLine(ApprovalLineVO aprLine);
	
	// 자동 결재선 삭제 
	int deleteAprLine(List<String> checkedList);
	
	// === 전자결재 ===

	// 내 apr_status 별 전자결재 문서 리스트 개수 조회
	int selectAprCount(Map<String, String> paraMap);
	
	// 내 apr_status 별 전자결재 문서 리스트 조회
	List<ApprovalVO> selectAprList(Map<String, String> paraMap);

	// 전자결재 등록 - 품의서, 근태신청서
	int registerApproval(Map<String, String> paraMap, String formType);

	// 내가 결재해야할 문서 리스트 개수 조회
	int selectMyAprCount(Map<String, String> paraMap);
	
	// 내가 결재해야할 문서 리스트 조회
	List<ApprovalVO> selectMyAprList(Map<String, String> paraMap);

	// 팀 문서함 총 개수조회
	int selectTeamAprCount(Map<String, String> paraMap);

	// 팀 문서함 조회
	List<ApprovalVO> selectTeamAprList(Map<String, String> paraMap);
	
	// 상세 조회
	ApprovalVO selectAprDetail(String apr_no);

	// 내기안문서삭제
	int deleteMyForm(String apr_no, String form_type, String form_no);

	// 결재 승인
	int acceptApr(String apr_no, String emp_id);
	
	// 결재 반려
	int rejectApr(String apr_no, String apr_comment);


}
