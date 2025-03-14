package com.syoffice.app.approval.domain;

public class ApprovalLineVO {
	private String apline_no; /* 결재라인번호 */
	private String fk_emp_id; /* 결재라인지정자 */
	private String apline_name; /* 결재라인명 */
	private String apline_approver; /* 결재자 */
	private String apline_approver2; /* 결재자2 */
	private String apline_approver3; /* 결재자3 */

	//
	private String empl_name;
	private String approver_name;
	private String apline_approver_name;
	private String apline_approver2_name;
	private String apline_approver3_name;
	private String approval_chain_names;

	public String getApline_no() {
		return apline_no;
	}

	public void setApline_no(String apline_no) {
		this.apline_no = apline_no;
	}

	public String getFk_emp_id() {
		return fk_emp_id;
	}

	public void setFk_emp_id(String fk_emp_id) {
		this.fk_emp_id = fk_emp_id;
	}

	public String getApline_name() {
		return apline_name;
	}

	public void setApline_name(String apline_name) {
		this.apline_name = apline_name;
	}

	public String getApline_approver() {
		return apline_approver;
	}

	public void setApline_approver(String apline_approver) {
		this.apline_approver = apline_approver;
	}

	public String getEmpl_name() {
		return empl_name;
	}

	public void setEmpl_name(String empl_name) {
		this.empl_name = empl_name;
	}

	public String getApprover_name() {
		return approver_name;
	}

	public void setApprover_name(String approver_name) {
		this.approver_name = approver_name;
	}

	public String getApline_approver2() {
		return apline_approver2;
	}

	public void setApline_approver2(String apline_approver2) {
		this.apline_approver2 = apline_approver2;
	}

	public String getApline_approver3() {
		return apline_approver3;
	}

	public void setApline_approver3(String apline_approver3) {
		this.apline_approver3 = apline_approver3;
	}

	public String getApproval_chain_names() {
		return approval_chain_names;
	}

	public void setApproval_chain_names(String approval_chain_names) {
		this.approval_chain_names = approval_chain_names;
	}

	public String getApline_approver_name() {
		return apline_approver_name;
	}

	public void setApline_approver_name(String apline_approver_name) {
		this.apline_approver_name = apline_approver_name;
	}

	public String getApline_approver2_name() {
		return apline_approver2_name;
	}

	public void setApline_approver2_name(String apline_approver2_name) {
		this.apline_approver2_name = apline_approver2_name;
	}

	public String getApline_approver3_name() {
		return apline_approver3_name;
	}

	public void setApline_approver3_name(String apline_approver3_name) {
		this.apline_approver3_name = apline_approver3_name;
	}
	
}
