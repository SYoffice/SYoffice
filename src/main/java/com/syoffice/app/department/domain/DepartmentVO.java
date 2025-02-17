package com.syoffice.app.department.domain;

public class DepartmentVO {
	
	/* 반복문 돌리려고 가져옴 */
	
	private String dept_id;			/* 부서번호 */
	
	private String dept_name;		/* 부서명 */
	
	private String manager_id;		/* 부서장 */

	////////////////////////////////////////////
	
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
	
	
}// end of public class DepartmentVO() ----------
