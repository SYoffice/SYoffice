package com.syoffice.app.approval.model;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.syoffice.app.approval.domain.ApprovalLineVO;
import com.syoffice.app.approval.domain.ApprovalVO;

@Mapper
public interface ApprovalDAO {
	
	List<ApprovalLineVO> selectAprLineList(String fk_emp_id);
	
	int registerApprovalLine(Map<String, String> paraMap);

	int deleteAprLine(List<String> checkedList);
	
	// 내 apr_status 별 전자결재 문서 리스트 개수 조회
	int selectAprCount(Map<String, String> paraMap);
	
	// 내 apr_status 별 전자결재 문서 리스트 조회
	List<ApprovalVO> selectAprList(Map<String, String> paraMap);
	
	// draft_seq.NEXTVAL
	int getDraftKey();
	
	// leave_seq.NEXTVAL
	int getLeaveKey();
	
	// 전자결재 테이블에 insert
	int insertApr(Map<String, String> paraMap);
	
	// 품의서 테이블에 insert
	int insertDraft(Map<String, String> paraMap);

	// 근태신청서 테이블에 insert
	int insertLeave(Map<String, String> paraMap);
	
	// 내가 결재해야할 문서 리스트 개수 조회
	int selectMyAprCount(Map<String, String> paraMap);

	// 내가 결재해야할 문서 리스트 조회
	List<ApprovalVO> selectMyAprList(Map<String, String> paraMap);

	// 팀문서함 총 개수 조회
	int selectTeamAprCount(Map<String, String> paraMap);

	// 팀문서함 조회
	List<ApprovalVO> selectTeamAprList(Map<String, String> paraMap);

	// 상세 조회
	ApprovalVO selectAprDetail(String apr_no);

	// 내기안문서삭제
	int deleteApproval(String apr_no);

	int deleteDraft(String draft_no);

	int deleteLeave(String leave_no);

	int acceptApr(Map<String, String> paraMap);

	// 전자결재 조회
	ApprovalVO selectApr(String apr_no);

	int rejectApr(String apr_no, String apr_comment);


	
}
