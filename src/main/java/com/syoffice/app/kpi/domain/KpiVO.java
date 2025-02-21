package com.syoffice.app.kpi.domain;

public class KpiVO {
	
	// column
	private String kpi_no;		// 목표설정번호(시퀀스)
	private String fk_dept_id;	// 부서번호
	private String kpi_year;	// 연도
	private String kpi_quarter;	// 분기
	private String kpi_index;	// 목표실적액
	
	
	// select
	private String dept_name;	// 부서명
	private String manager_id; 	// 부서장 사원번호
	private String branch_name; // 지점명
	
	private String sum_result_price; // 해당 분기 실적 달성 총액
	private String result_pct; 	// 해당 분기 실적 달성 비율
	

	
	// Getter & Setter
	
	public String getKpi_no() {
		return kpi_no;
	}
	public void setKpi_no(String kpi_no) {
		this.kpi_no = kpi_no;
	}
	public String getFk_dept_id() {
		return fk_dept_id;
	}
	public void setFk_dept_id(String fk_dept_id) {
		this.fk_dept_id = fk_dept_id;
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
	public String getKpi_index() {
		return kpi_index;
	}
	public void setKpi_index(String kpi_index) {
		this.kpi_index = kpi_index;
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
	public String getBranch_name() {
		return branch_name;
	}
	public void setBranch_name(String branch_name) {
		this.branch_name = branch_name;
	}
	public String getSum_result_price() {
		return sum_result_price;
	}
	public void setSum_result_price(String sum_result_price) {
		this.sum_result_price = sum_result_price;
	}
	public String getResult_pct() {
		return result_pct;
	}
	public void setResult_pct(String result_pct) {
		this.result_pct = result_pct;
	}
	
	
}
