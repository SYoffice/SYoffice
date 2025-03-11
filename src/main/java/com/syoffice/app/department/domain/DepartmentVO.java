package com.syoffice.app.department.domain;

public class DepartmentVO {
	
	/* 반복문 돌리려고 가져옴 */
	
	private String dept_id;			/* 부서번호 */
	
	private String dept_name;		/* 부서명 */
	
	private String manager_id;		/* 부서장 사원번호 */
	
	private String executive_id;	/* 담당임원 사원번호 */	
	////////////////////////////////////////////
	
	/* 부서관리용 */
	private String branch_name;   // 소속 지점명 (본사, 강남지점, 강북지점)
	
    private String branch_no;     // 지점 번호
    
    private String employee_count;// 직원 수
    
    private String manager_name;  // 부서장 이름
    
    private String manager_grade; // 부서장 직급명
    
    private String manager_email; // 부서장 이메일
	
	////////////////////////////////////////////
	// 기본생성자
    public DepartmentVO() {}
    
    
	public String getDept_id() {
		return dept_id;
	}

	public void setDept_id(String dept_id) {
		this.dept_id = dept_id;
	}

	
	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	
	public String getManager_id() {
		return manager_id;
	}

	public void setManager_id(String manager_id) {
		this.manager_id = manager_id;
	}


	public String getExecutive_id() {
		return executive_id;
	}

	public void setExecutive_id(String executive_id) {
		this.executive_id = executive_id;
	}


	public String getBranch_name() {
		return branch_name;
	}

	public void setBranch_name(String branch_name) {
		this.branch_name = branch_name;
	}


	public String getBranch_no() {
		return branch_no;
	}

	public void setBranch_no(String branch_no) {
		this.branch_no = branch_no;
	}


	public String getEmployee_count() {
		return employee_count;
	}

	public void setEmployee_count(String employee_count) {
		this.employee_count = employee_count;
	}


	public String getManager_name() {
		return manager_name;
	}

	public void setManager_name(String manager_name) {
		this.manager_name = manager_name;
	}


	public String getManager_grade() {
		return manager_grade;
	}

	public void setManager_grade(String manager_grade) {
		this.manager_grade = manager_grade;
	}


	public String getManager_email() {
		return manager_email;
	}

	public void setManager_email(String manager_email) {
		this.manager_email = manager_email;
	}
	
	
}// end of public class DepartmentVO() ----------
