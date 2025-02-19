package com.syoffice.app.hr.service;

import java.util.List;
import java.util.Map;

import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.syoffice.app.branch.domain.BranchVO;
import com.syoffice.app.common.Sha256;
import com.syoffice.app.department.domain.DepartmentVO;
import com.syoffice.app.employee.domain.EmployeeVO;
import com.syoffice.app.grade.domain.GradeVO;
import com.syoffice.app.hr.model.HrDAO;

//서비스(Service) 선언
@Service
public class HrService_imple implements HrService {
	
	@Autowired	// Type 에 따라 알아서 Bean 을 주입해준다.
	private HrDAO dao;
	
	// 총 사원의 수 구해오기
	@Override
	public int getTotalCount(Map<String, String> paraMap) {
		int totalCount = dao.getTotalCount(paraMap);
		return totalCount;
	}// end of public int getTotalCount(Map<String, String> paraMap) -----
	
	// 페이징과 검색이 포함된 사원목록 가져오기
	@Override
	public List<EmployeeVO> selctEmployeeList(Map<String, String> paraMap) {
		List<EmployeeVO> employeeList = dao.selctEmployeeList(paraMap);
		
//		System.out.println(paraMap.get("searchType"));
//		System.out.println(paraMap.get("searchWord"));
		
		return employeeList;
	}// end of public List<EmployeeVO> selctEmployeeList(Map<String, String> paraMap) -----
	
	
	// 신규 사원등록 시 지점 정보조회
	@Override
	public List<BranchVO> selectBranchList() {
		List<BranchVO> branchList = dao.selectBranchList();
		return branchList;
	}// end of public List<BranchVO> selectBranchList() -----
	
	// 신규 사원등록 시 부서 정보조회
	@Override
	public List<DepartmentVO> selectDepartmentList() {
		List<DepartmentVO> departmentList = dao.selectDepartmentList();
		return departmentList;
	}// end of public List<DepartmentVO> selectDepartmentList() -----

	// 신규 사원 등록 시 직급 정보조회
	@Override
	public List<GradeVO> selectGradeList() {
		List<GradeVO> gradeList = dao.selectGradeLst();
		return gradeList;
	}// end of public List<GradeVO> selectGradeList() ----
	
	// 신규 사원정보 insert
	@Override
	public int employeeRegister(Map<String, String> paraMap) {
		
	//	System.out.println("Service_imple" + paraMap.get("profile")); 
		
		int n = dao.employeeRegister(paraMap);
		return n;
	}// end of public int employeeRegister(Map<String, String> paraMap) -----
	
	
	// 사원 한명의 정보 select
	@Override
	public EmployeeVO employeeDetail(String emp_id) {
		
		EmployeeVO employeevo = dao.employeeDetail(emp_id);
		
		return employeevo;
	}// end of public EmployeeVO employeeDetail(String emp_id) -----



}
