package com.syoffice.app.hr.service;

import java.util.List;
import java.util.Map;

import com.syoffice.app.branch.domain.BranchVO;
import com.syoffice.app.department.domain.DepartmentVO;
import com.syoffice.app.employee.domain.EmployeeVO;
import com.syoffice.app.grade.domain.GradeVO;
import com.syoffice.app.reservation.domain.ResourceVO;

public interface HrService {
	
	// 총 사원의 수 구해오기
	int getTotalCount(Map<String, String> paraMap);
	
	// 페이징과 검색이 포함된 사원목록 가져오기
	List<EmployeeVO> selctEmployeeList(Map<String, String> paraMap);
	
	// 신규 사원등록 시 지점 정보조회
	List<BranchVO> selectBranchList();
	
	// 신규 사원등록 시 부서 정보조회
	List<DepartmentVO> selectDepartmentList();
	
	// 신규 사원 등록 시 직급 정보조회
	List<GradeVO> selectGradeList();

	// 신규 사원정보 insert
	int employeeRegister(Map<String, String> paraMap);
	
	// 사원 등록 시 사내이메일 중복체크
	String checkMail(String mail);
	
	// 사원 한명의 정보를 조회
	EmployeeVO employeeDetail(String emp_id);
	
	// 사원정보 update
	int employeeEdit(Map<String, String> paraMap);
	
	// 검색만 있는 부서 목록 조회 (부서관리용)
	List<DepartmentVO> DepartmentList(Map<String, String> paraMap);

	// 부서별 사원리스트
	List<EmployeeVO> DepartmentEmployeeInfo(String dept_id, String branch_no);
	
	// 신규 부서등록하기
	int RegisterDepartment(String dept_name, String manager_id, String executive_id);
	
	// 부서 추가할 때 필요한 부서장 리스트 가져오기
	List<EmployeeVO> managerList();
	
	// 부서 추가할 때 필요한 임원진 리스트 가져오기
	List<EmployeeVO> executiveList();
	
	// 부서명 중복검사
	boolean checkDeptName(String dept_name);

	// 수정 시 특정 부서의 정보 조회
	DepartmentVO getDepartmentInfo(Map<String, String> map);
	// 모든 부서장이 될 수 있는 사원들 조회
	List<EmployeeVO> getAllManagers();
	// 모든 담당임원이 될 수 있는 사원들 조회
	List<EmployeeVO> getAllExecutives();
	// 부서 정보 수정
	Integer editDepartment(Map<String, String> paraMap);

	// 부서삭제 여부
	boolean deleteDepartment(String dept_id);

	// 총 자원 수 알아오기
	int getResourceCount(Map<String, String> paraMap);
	
	// 페이징과 검색이 포함된 자원목록 가져오기
	List<ResourceVO> resourceList(Map<String, String> paraMap);
	
	// 자원명 중복검사
	boolean checkResourceName(Map<String, String> paraMap);
	
	// 신규 자원등록
	int registerResource(Map<String, String> paraMap);
	
	// 자원 수정
	int updateResource(Map<String, String> paraMap);

	// 자원 삭제
	int deleteResource(String resource_no);




	
	
}
