package com.syoffice.app.hr.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.syoffice.app.branch.domain.BranchVO;
import com.syoffice.app.common.Sha256;
import com.syoffice.app.department.domain.DepartmentVO;
import com.syoffice.app.employee.domain.EmployeeVO;
import com.syoffice.app.grade.domain.GradeVO;
import com.syoffice.app.hr.model.HrDAO;
import com.syoffice.app.reservation.domain.ResourceVO;

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
		 
		// 직속상관 사번을 알아오기 위한 용도(부서번호)
		String dept_id = paraMap.get("dept_id");
		
		// 직속상관 사번을 알아오기 위한 용도(지점번호)
		String branch_no = paraMap.get("branch_no");
		
		// 직속 상관 사번 알아오기
		String leader_id = dao.getLeaderId(dept_id, branch_no);
		
		paraMap.put("leader_id", leader_id);
		
		int n = dao.employeeRegister(paraMap);
		return n;
	}// end of public int employeeRegister(Map<String, String> paraMap) -----
	
	
	// 사원 한명의 정보 select
	@Override
	public EmployeeVO employeeDetail(String emp_id) {
		
		EmployeeVO employeevo = dao.employeeDetail(emp_id);
		
		return employeevo;
	}// end of public EmployeeVO employeeDetail(String emp_id) -----

	
	// 사원 등록 시 사내이메일 중복체크
	@Override
	public String checkMail(String mail) {
		
		String result = dao.checkMail(mail);
		
		if("1".equals(result)) {
			// 이메일이 중복된 경우 1 보내주기
			result = "1";
			return result;
		}
		else {
			// 이메일이 중복되지 않은 경우 0 보내주기
			result = "0";
			return result;
		}
		
	}// end of public String checkMail(String mail) ----
	
	// 사원정보 update
	@Override
	public int employeeEdit(Map<String, String> paraMap) {
		
		// 직속상관 사번을 알아오기 위한 용도(부서번호)
		String dept_id = paraMap.get("dept_id");
		
		// 직속상관 사번을 알아오기 위한 용도(지점번호)
		String branch_no = paraMap.get("branch_no");
		
		// 직속 상관 사번 알아오기
		String leader_id = dao.getLeaderId(dept_id, branch_no);
		
		// 직속 상관 사번도 Map 에 담아주기
		paraMap.put("leader_id", leader_id);
		
		int n = dao.employeeEdit(paraMap);
		
		return n;
	}// end of public int employeeEdit(Map<String, String> paraMap) ----
	
	// 검색만 있는 부서 목록 조회 (부서관리용)
	@Override
	public List<DepartmentVO> DepartmentList(Map<String, String> paraMap) {
		List<DepartmentVO> departmentList = dao.DepartmentList(paraMap);
		return departmentList;
	}// end of public List<DepartmentVO> DepartmentList(Map<String, String> paraMap) ----- 

	
	// 부서별 사원리스트
	@Override
	public List<EmployeeVO> DepartmentEmployeeInfo(String dept_id, String branch_no ) {
		List<EmployeeVO> employeeList = dao.DepartmentEmployeeInfo(dept_id, branch_no);
		return employeeList;
	}// end of public List<EmployeeVO> DepartmentMemberInfo(String dept_id) -----
	
	
	// 신규 부서등록하기
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class})	// 부서장과 담당임원이 중복되지 않기 위해 트랜잭션 처리
	@Override
	public int RegisterDepartment(String dept_name, String manager_id, String executive_id) {
		
		// 1. 신규 등록 될 부서 번호 알아오기(select)
		Integer new_dept_id = dao.getDept_id();
	//	System.out.println("생성된 부서번호: " + new_dept_id);
		
		// 부서 번호 생성 실패했을 때
		if (new_dept_id == null) {
            return 0;
        }
		
		// 2. 부서등록 데이터 설정
		Map<String, Object> paraMap = new HashMap<>();
		
		paraMap.put("dept_name", dept_name);
		paraMap.put("manager_id", manager_id);
		paraMap.put("executive_id", executive_id);
		paraMap.put("new_dept_id", new_dept_id);
		
		// 3. 알아온 부서번호로 부서등록 (insert)
		int result = dao.RegisterDepartment(paraMap);
		
		// 부서등록에 실패했을 때
		if(result != 1) {
			return 0;
		}
		
		// 4. 부서장 정보 업데이트 데이터 설정
		Map<String, Object> updateMap = new HashMap<>();
		updateMap.put("manager_id", manager_id);	// 부서장의 사원번호
		updateMap.put("new_dept_id", new_dept_id);	// 새로 생성된 부서 번호

		// 담당 임원이 없을 때는 null 값으로 처리
		if (executive_id != null && !executive_id.trim().isEmpty()) {
			updateMap.put("leader_id", executive_id);
		} else {
			updateMap.put("leader_id", null);
		}
		
		// 5. 부서장 정보 업데이트 (update)
		dao.updateManagerInfo(updateMap);
		
		return result;
	}// end of public int RegisterDepartment(String dept_name, String manager_id, String executive_id) ---- 
	
	// 부서 추가할 때 필요한 부서장 리스트 가져오기
	@Override
	public List<EmployeeVO> managerList() {
		List<EmployeeVO> managerList = dao.mananagerList();
		return managerList;
	}// end of public List<EmployeeVO> managerList() -----
	
	// 부서 추가할 때 필요한 임원진 리스트 가져오기
	@Override
	public List<EmployeeVO> executiveList() {
		List<EmployeeVO> executiveList = dao.executiveList();
		return executiveList;
	}// end of public List<EmployeeVO> executiveList() -----
	
	// 부서명 중복검사
	@Override
	public boolean checkDeptName(String dept_name) {
		int result = dao.checkDeptName(dept_name);
		return result > 0 ;
	}// end of public boolean checkDeptName(String dept_name) ------
	
	
	// 수정 시 특정 부서의 정보 조회
	@Override
	public DepartmentVO getDepartmentInfo(Map<String, String> map) {
		// 부서VO 초기화
		DepartmentVO dept_info = null;
		
		String dept_id = map.get("dept_id");
		
		// 부서가 판매부가 아닐 때
		if(!"8".equals(dept_id)) {
			// 판매부 제외 부서정보
			dept_info = dao.getDepartmentInfo(map);
		}
		// 부서가 판매부 일 때
		else {
			dept_info = dao.getDepartmentInfo_sales(map);
		}
		
		return dept_info;
	}// end of public DepartmentVO getDepartmentInfo(int dept_id) -----
	
	// 모든 부서장이 될 수 있는 사원들 조회
	@Override
	public List<EmployeeVO> getAllManagers() {
		List<EmployeeVO> allManagerList = dao.getAllManagers();
		return allManagerList;
	}// end of public List<EmployeeVO> getAllManagers() -----
	
	// 모든 담당임원이 될 수 있는 사원들 조회
	@Override
	public List<EmployeeVO> getAllExecutives() {
		List<EmployeeVO> allExecutiveList = dao.getAllExecutives();
		return allExecutiveList;
	}// end of public List<EmployeeVO> getAllExecutives() ----
	
	// 부서 정보 수정
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class})
	@Override
	public Integer editDepartment(Map<String, String> paraMap) {
		
		String dept_id = paraMap.get("dept_id");
		String manager_id = paraMap.get("manager_id");
		String branch_no = paraMap.get("branch_no");
		
		// 부서장의 전 소속 부서번호 알아오기
		String previous_dept_id = dao.getPreviousDeptId(manager_id);
		
		paraMap.put("previous_dept_id", previous_dept_id);
		// 지점장의 전 소속 지점번호 알아오기
		String previous_branch_no = dao.getPreviousBranchNo(manager_id);
		
		paraMap.put("previous_branch_no", previous_branch_no);
		
		int result = 0; 
		
		// 부서가 판매부가 아닐 떄
		if(!"8".equals(dept_id)) {
			result = dao.editDepartment(paraMap);
			// 부서 정보 수정에 성공했을 떄
			if(result == 1) {
				// 본사에서 부서이동이 일어났을 때
				if("1".equals(previous_branch_no)) {
					// 부서장의 부서이동이 없을 때
					if(dept_id.equals(previous_dept_id)) {						
						// 해당 부서 소속 사원들의 직속상관아이디(leader_id) 새로운 부서장으로 교체(update)
						dao.updateEmpLeaderId(paraMap);
						
						// 새로 임명된 부서장의 직속상관아이디(leader_id) 그 부서의 담당임원 아이디로 교체(update)
						dao.updateManagerLeaderId(paraMap);
					}
					// 기존 부서장이 공석이였을 경우
					else if(previous_dept_id == null) {
						// 해당 부서 소속 사원들의 직속상관아이디(leader_id) 새로운 부서장으로 교체(update)
						dao.updateEmpLeaderId(paraMap);
						
						// 새로 임명된 부서장의 직속상관아이디(leader_id) 그 부서의 담당임원 아이디로 교체(update)
						dao.updateManagerLeaderId(paraMap);
					}
					// 부서장의 부서이동이 있을 때
					else {
						// 새로 임명된 부서장의 직속상관아이디(leader_id) 그 부서의 담당임원 아이디로 교체(update)
						dao.updateManagerLeaderId(paraMap);
						
						// 다른 부서의 부서장이였던 경우 전 소속 부서테이블의 부서장아이디 null 값으로 교체(update)
						dao.updatePreviousManagerId(paraMap);
						
						// 전 소속 부서원들의 직속상관아이디(leader_id) null 값으로 교체(update)
						dao.updatePreviousLeaderId(paraMap);
					}
				}
				// 본사가 아닌 지점에서 부서이동 일어났을 떄
				else {
					// 해당 부서 소속 사원들의 직속상관아이디(leader_id) 새로운 부서장으로 교체(update)
					dao.updateEmpLeaderId(paraMap);
					// 새로 임명된 부서장의 직속상관아이디(leader_id) 그 부서의 담당임원 아이디로 교체(update)
					dao.updateManagerLeaderId(paraMap);
					
					// 그 사람이 다른 지점의 지점장이였을 때
					String new_leader_id = dao.getNewLeaderId(paraMap);
					
					paraMap.put("new_leader_id", new_leader_id);
					
					// 전 소속 지점의 사원들의 정보변경
					dao.updatePreviousBranchInfo(paraMap);
				}
				
			}
			
		}
		// 부서가 판매부일 때
		else {
			result = dao.editDepartment_sales(paraMap);
			// 부서 수정에 성공했을 때
			if(result > 0) {
				// 새로 임명된 지점장이 본사 소속이 아닐 경우
				if(!"1".equals(previous_branch_no)) {
					
				//	System.out.println("~~ 새로 임명된 지점장이 본사 소속이 아닐 경우 ~~");
					
					if(previous_branch_no.equals(branch_no)) {
						// 새로 온 지점장의 정보 변경 - 같은 지점의 사원이 지점장이 된 경우
						dao.updateLeaderInfo(paraMap);	// 부서, 지점, leader_id 업데이트 - 조건절 emp_id = #{manager_id}
						// 변경된 지점에 소속된 사원들의 정보변경
						dao.updateBranchInfo(paraMap);	// 부서, 지점, leader_id 업데이트 - 조건절 fk_branch_no = #{branch_no}
					}// end of if(previous_branch_no == branch_no)
					// 다른 지점사람이 지점장이 되었을 경우
					else {
						// 그 사람이 지점장이였을 경우
						dao.updateLeaderInfo(paraMap);	// 부서, 지점, leader_id 업데이트 - 조건절 emp_id = #{manager_id}
						
						// 변경된 지점에 소속된 사원들의 정보변경
						dao.updateBranchInfo(paraMap);	// 부서, 지점, leader_id 업데이트 - 조건절 fk_branch_no = #{branch_no}
						
						// 전 소속 지점의 지점장 자동설정을 위해 다음 직급이 높은 사원 알아오기
						String new_leader_id = dao.getNewLeaderId(paraMap);
						
						paraMap.put("new_leader_id", new_leader_id);
						
						// 전 소속 지점의 사원들의 정보변경
						dao.updatePreviousBranchInfo(paraMap);
						// 아니였을 때 - 그 사람의 정보만 수정하면됨 전 지점의 정보는 수정 X
						
					}	
					
				}
				// 새로 임명된 지점장이 본사 소속이였을 경우
				else {
				//	System.out.println("== 새로 임명된 지점장이 본사 소속이였을 경우 실행됨 == ");
					
					if(previous_dept_id != null) {
						// 새로 임명된 지점장이 - 본사소속 다른 부서의 부서장이였을 경우
						// 새로 임명된 지점장의 정보 update 지점번호, 부서번호, leader_id 	
						dao.updateLeaderInfo(paraMap);
						// 변경된 지점에 소속된 사원들의 정보변경
						dao.updateBranchInfo(paraMap);
						// 전 부서의 manager_id NULL 값으로 변경
						dao.updatePreviousManagerId(paraMap);
					}
					else if (previous_dept_id == null) {
						// 새로 임명된 지점장의 정보 update 지점번호, 부서번호, leader_id 	
						dao.updateLeaderInfo(paraMap);
						// 변경된 지점에 소속된 사원들의 정보변경
						dao.updateBranchInfo(paraMap);
					}
					
				}
				
			}
						
		}
				
		return result;
	}// end of public Integer editDepartment(Map<String, String> paraMap) ----

	
	// 부서 삭제 여부
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteDepartment(String dept_id) {
		// 1. 부서에 소속된 직원이 있는지 확인
        int employeeCount = dao.getEmployeeCountByDeptId(dept_id);

        // 2. 직원이 존재하면 삭제 불가
        if (employeeCount > 0) {
            return false;
        }

        // 3. 직원이 없는 경우 삭제 진행
        int result = dao.deleteDepartment(dept_id);

        // 4. 삭제 성공 여부 반환
        return result > 0;
	}

	// 총 자원 수 알아오기
	@Override
	public int getResourceCount(Map<String, String> paraMap) {
		int totalCount = dao.getResourceCount(paraMap);
		return totalCount;
	}// end of public int getResourceCount(Map<String, String> paraMap) ----- 
	
	// 페이징과 검색이 포함된 자원목록 가져오기
	@Override
	public List<ResourceVO> resourceList(Map<String, String> paraMap) {
		List<ResourceVO> resourceList = dao.resourceList(paraMap);
		return resourceList;
	}// end of public List<ResourceVO> resourceList(Map<String, String> paraMap) 00000
	
	// 자원명 중복검사 
	@Override
	public boolean checkResourceName(Map<String, String> paraMap) {
		int count = dao.checkResourceName(paraMap);
	    return count > 0;
	}// end of public boolean checkResourceName(String resource_name) -----
	
	// 신규 자원등록
	@Override
	public int registerResource(Map<String, String> paraMap) {
		int result = dao.registerResource(paraMap);
		return result;
	}// end of public int registerResource(Map<String, String> paraMap) -----
	
	// 자원 수정
	@Override
	public int updateResource(Map<String, String> paraMap) {
		int result = dao.updateResource(paraMap);
		return result;
	}// end of public int updateResource(Map<String, String> paraMap) -----
	
	// 자원 삭제
	@Override
	public int deleteResource(String resource_no) {
		int result = dao.deleteResource(resource_no);
		return result;
	}
	
}// end of public class HrService_imple implements HrService{} -----
