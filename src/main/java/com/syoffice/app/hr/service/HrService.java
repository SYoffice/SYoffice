package com.syoffice.app.hr.service;

import java.util.List;
import java.util.Map;

import com.syoffice.app.branch.domain.BranchVO;
import com.syoffice.app.department.domain.DepartmentVO;
import com.syoffice.app.employee.domain.EmployeeVO;
import com.syoffice.app.grade.domain.GradeVO;

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


	
	
}
