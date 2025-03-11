package com.syoffice.app.hr.model;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.syoffice.app.branch.domain.BranchVO;
import com.syoffice.app.department.domain.DepartmentVO;
import com.syoffice.app.employee.domain.EmployeeVO;
import com.syoffice.app.grade.domain.GradeVO;
import com.syoffice.app.reservation.domain.ResourceVO;

@Mapper
public interface HrDAO {
	
	// 총 사원의 수 구해오기
	int getTotalCount(Map<String, String> paraMap);
	
	// 페이징과 검색이 포함된 사원목록 가져오기
	List<EmployeeVO> selctEmployeeList(Map<String, String> paraMap);
	
	// 신규 사원등록 시 지점 정보조회
	List<BranchVO> selectBranchList();
	
	// 신규 사원등록 시 부서 정보조회
	List<DepartmentVO> selectDepartmentList();
	
	// 신규 사원등록 시 직급 정보조회	
	List<GradeVO> selectGradeLst();
	
	// 신규 사원정보 insert
	int employeeRegister(Map<String, String> paraMap);
	
	// 사원 한명의 정보를 select
	EmployeeVO employeeDetail(String emp_id);
	
	// 사원 등록 시 사내이메일 중복체크
	String checkMail(String mail);
	
	// 직속 상관 사번 알아오기
	String getLeaderId(String dept_id, String branch_no);
	
	// 사원정보 update
	int employeeEdit(Map<String, String> paraMap);
	
	// 검색만 있는 부서 목록 조회 (부서관리용)
	List<DepartmentVO> DepartmentList(Map<String, String> paraMap);
	
	// 부서별 사원리스트
	List<EmployeeVO> DepartmentEmployeeInfo(String dept_id, String branch_no);

	// 부서 추가할 때 필요한 부서장 리스트 가져오기
	List<EmployeeVO> mananagerList();
	
	// 부서 추가할 때 필요한 임원진 리스트 가져오기
	List<EmployeeVO> executiveList();
	
	// 신규 등록 될 부서 번호 알아오기 (select)
	Integer getDept_id();
	// 신규 부서등록하기 (insert)
	int RegisterDepartment(Map<String, Object> paraMap);
	// 부서장 정보 업데이트 (update)
	void updateManagerInfo(Map<String, Object> updateMap);
	// 부서명 중복검사
	int checkDeptName(String dept_name);

	// 수정모달 사용 시 특정 부서의 정보 조회(판매부 제외)
	DepartmentVO getDepartmentInfo(Map<String, String> map);
	// 수정모달 사용 시 특정 부서의 정보 조회(판매부만)
	DepartmentVO getDepartmentInfo_sales(Map<String, String> map);
	// 모든 부서장이 될 수 있는 사원들 조회
	List<EmployeeVO> getAllManagers();
	// 모든 담당임원이 될 수 있는 사원들 조회
	List<EmployeeVO> getAllExecutives();
	
	// 부서가 판매부가 아닐 떄 부서 정보 수정
	int editDepartment(Map<String, String> paraMap);
	// 부서가 판매부일 떄 부서 정보 수정
	int editDepartment_sales(Map<String, String> paraMap);
	// 부서장의 전 소속 부서번호 알아오기
	String getPreviousDeptId(String manager_id);
	// 해당 부서 소속 사원들의 직속상관아이디(leader_id) 새로운 부서장으로 교체(update)
	void updateEmpLeaderId(Map<String, String> paraMap);
	// 새로 임명된 부서장의 직속상관아이디(leader_id) 그 부서의 담당임원 아이디로 교체(update)
	void updateManagerLeaderId(Map<String, String> paraMap);
	// 다른 부서의 부서장이였던 경우 전 소속 부서테이블의 부서장아이디 null 값으로 교체(update)
	void updatePreviousManagerId(Map<String, String> paraMap);
	// 전 소속 부서원들의 직속상관아이디(leader_id) null 값으로 교체(update)
	void updatePreviousLeaderId(Map<String, String> paraMap);
	// 새로 온 지점장의 정보 변경
	void updateLeaderInfo(Map<String, String> paraMap);
	// 변경된 지점에 소속된 사원들의 정보변경
	void updateBranchInfo(Map<String, String> paraMap);
	// 지점장의 전 소속 지점번호 알아오기
	String getPreviousBranchNo(String manager_id);
	// 전 소속 지점의 사원들의 정보변경
	void updatePreviousBranchInfo(Map<String, String> paraMap);
	// 전 소속 지점의 지점장 자동설정을 위해 다음 직급이 높은 사원 알아오기
	String getNewLeaderId(Map<String, String> paraMap);
	
	// 부서에 소속된 직원 수 조회
	int getEmployeeCountByDeptId(String dept_id);
	// 부서 삭제
	int deleteDepartment(String dept_id);
	
	// 총 자원 수 알아오기
	int getResourceCount(Map<String, String> paraMap);

	// 페이징과 검색이 포함된 자원목록 가져오기
	List<ResourceVO> resourceList(Map<String, String> paraMap);
	
	// 자원명 중복검사
	int checkResourceName(Map<String, String> paraMap);
	
	// 신규 자원등록
	int registerResource(Map<String, String> paraMap);
	
	// 자원 수정
	int updateResource(Map<String, String> paraMap);
	
	// 자원삭제
	int deleteResource(String resource_no);

	
}
