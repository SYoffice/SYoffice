package com.syoffice.app.employee.domain;

public class EmployeeVO {
	
	private String emp_id;			/* 사원번호 */
	
	private String fk_dept_id;		/* 부서번호 */
	
	private String name;			/* 직원명 */
	
	private String password;		/* 비밀번호 */
	
	private String tel;				/* 연락처 */
	
	private String personal_mail;	/* 개인이메일 */
	
	private String hire_date;		/* 입사일자 */
	
	private String mail;			/* 업무이메일 */
	
	private String postcode;		/* 우편번호 */
	
	private String address;			/* 주소 */
	
	private String detailaddress;	/* 상세주소 */
	
	private String extraaddress;	/* 참고사항 */
	
	private String gender;			/* 성별(남/여) */
	
	private String birthday;		/* 생년월일 */
	
	private String fk_grade_no;		/* 직급번호 */
	
	private String status;			/* 고용상태(1: 정상근무, 2:휴직, 3:퇴직) */
	
	private String pwdchangedate;	/* 비번변경일 */
	
	private String pwdchangestatus;	/* 비번변경필요여부(0: 변경필요, 1:정상) */
	
	private String leave_count;		/* 잔여연차 */
	
	private String profile_img;		/* 프로필이미지명 */
	
	//////////////////////////////////////////////////////////////////////
	
	private int pwdchangegap;	/* 마지막 비번변경일이 3개월 이상 지났을지 비번변경필요여부를 변경하기 위한 용도 */
	
	///////// 검색 및 join 해오기 위한 용도 ///////// 
	private String branch_name;	/* 지점명 */
	
    private String grade_name;	/* 직급명 */
    
    private String dept_name;	/* 부서명 */
    
    private String manager_id;	/* 부서장 사원번호 */
    
    private String leader_id;	/* 직속 상관 사원번호 */	
    ///////// 검색 및 join 해오기 위한 용도 /////////

	//////////////////////////////////////////////////////////////////////
	
	
	
	public String getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(String emp_id) {
		this.emp_id = emp_id;
	}

	
	public String getFk_dept_id() {
		return fk_dept_id;
	}

	public void setFk_dept_id(String fk_dept_id) {
		this.fk_dept_id = fk_dept_id;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	

	public String getPersonal_mail() {
		return personal_mail;
	}

	public void setPersonal_mail(String personal_mail) {
		this.personal_mail = personal_mail;
	}

	
	public String getHire_date() {
		return hire_date;
	}

	public void setHire_date(String hire_date) {
		this.hire_date = hire_date;
	}

	
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	

	public String getDetailaddress() {
		return detailaddress;
	}

	public void setDetailaddress(String detailaddress) {
		this.detailaddress = detailaddress;
	}
	

	public String getExtraaddress() {
		return extraaddress;
	}

	public void setExtraaddress(String extraaddress) {
		this.extraaddress = extraaddress;
	}

	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	
	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	

	public String getFk_grade_no() {
		return fk_grade_no;
	}

	public void setFk_grade_no(String fk_grade_no) {
		this.fk_grade_no = fk_grade_no;
	}

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public String getPwdchangedate() {
		return pwdchangedate;
	}

	public void setPwdchangedate(String pwdchangedate) {
		this.pwdchangedate = pwdchangedate;
	}

	
	public String getPwdchangestatus() {
		return pwdchangestatus;
	}

	public void setPwdchangestatus(String pwdchangestatus) {
		this.pwdchangestatus = pwdchangestatus;
	}

	
	public String getLeave_count() {
		return leave_count;
	}

	public void setLeave_count(String leave_count) {
		this.leave_count = leave_count;
	}

	
	public String getProfile_img() {
		return profile_img;
	}

	public void setProfile_img(String profile_img) {
		this.profile_img = profile_img;
	}

	/* 마지막 비번변경일이 3개월 이상 지났을지 비번변경필요여부를 변경하기 위한 용도 */
	public int getPwdchangegap() {
		return pwdchangegap;
	}

	public void setPwdchangegap(int pwdchangegap) {
		this.pwdchangegap = pwdchangegap;
	}

	
	public String getBranch_name() {
		return branch_name;
	}

	public void setBranch_name(String branch_name) {
		this.branch_name = branch_name;
	}

	
	public String getGrade_name() {
		return grade_name;
	}

	public void setGrade_name(String grade_name) {
		this.grade_name = grade_name;
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

	
	public String getLeader_id() {
		return leader_id;
	}

	public void setLeader_id(String leader_id) {
		this.leader_id = leader_id;
	}
	
	
}// end of public class EmployeeVO ------ 