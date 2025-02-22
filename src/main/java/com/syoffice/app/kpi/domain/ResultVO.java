package com.syoffice.app.kpi.domain;

public class ResultVO {
	
	// column
	private String result_no; 		// 실적번호
	private String fk_emp_id; 		// 사원번호
	private String result_name; 	// 실적명
	private String result_date; 	// 실적발생일
	private String result_price; 	// 실적액
	private String fk_kpi_no; 		// 목표 번호
	
	
	
	// select
	private String branch_name; 	// 지점명
	private String dept_name; 		// 부서명
	private String name;			// 사원명			
	private String grade_name;		// 직급번호			
	private String kpi_year;		// 목표실적연도			
	private String kpi_quarter;		// 목표실적분기			
	private String fk_branch_no;	// 지점번호
	private String fk_dept_id; 		// 부서번호
	
	
	
	// getter & setter
	public String getResult_no() {
		return result_no;
	}
	public void setResult_no(String result_no) {
		this.result_no = result_no;
	}
	public String getFk_emp_id() {
		return fk_emp_id;
	}
	public void setFk_emp_id(String fk_emp_id) {
		this.fk_emp_id = fk_emp_id;
	}
	public String getResult_name() {
		return result_name;
	}
	public void setResult_name(String result_name) {
		this.result_name = result_name;
	}
	public String getResult_date() {
		return result_date;
	}
	public void setResult_date(String result_date) {
		this.result_date = result_date;
	}
	public String getResult_price() {
		return result_price;
	}
	public void setResult_price(String result_price) {
		this.result_price = result_price;
	}
	public String getFk_kpi_no() {
		return fk_kpi_no;
	}
	public void setFk_kpi_no(String fk_kpi_no) {
		this.fk_kpi_no = fk_kpi_no;
	}
	public String getBranch_name() {
		return branch_name;
	}
	public void setBranch_name(String branch_name) {
		this.branch_name = branch_name;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGrade_name() {
		return grade_name;
	}
	public void setGrade_name(String grade_name) {
		this.grade_name = grade_name;
	}
	public String getKpi_year() {
		return kpi_year;
	}
	public void setKpi_year(String kpi_year) {
		this.kpi_year = kpi_year;
	}
	public String getKpi_quarter() {
		return kpi_quarter;
	}
	public void setKpi_quarter(String kpi_quarter) {
		this.kpi_quarter = kpi_quarter;
	}
	public String getFk_branch_no() {
		return fk_branch_no;
	}
	public void setFk_branch_no(String fk_branch_no) {
		this.fk_branch_no = fk_branch_no;
	}
	public String getFk_dept_id() {
		return fk_dept_id;
	}
	public void setFk_dept_id(String fk_dept_id) {
		this.fk_dept_id = fk_dept_id;
	}
	
	
}
