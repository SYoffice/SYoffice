package com.syoffice.app.dataroom.domain;

public class DataVO {
	
	String data_no;              /* 자료번호, 시퀀스사용(data_seq) */
	String fk_emp_id;            /* 자료등록자, 참조 테이블(tbl_employee) */
	String data_filename;        
	String data_orgfilename;     
	String data_filesize;        /* 파일크기 */
	String fk_data_cateno;       /* 자료분류번호, 참조 테이블(tbl_data_category) */
	
	
	public String getData_no() {
		return data_no;
	}
	
	public void setData_no(String data_no) {
		this.data_no = data_no;
	}
	
	public String getFk_emp_id() {
		return fk_emp_id;
	}
	
	public void setFk_emp_id(String fk_emp_id) {
		this.fk_emp_id = fk_emp_id;
	}
	
	public String getData_filename() {
		return data_filename;
	}
	
	public void setData_filename(String data_filename) {
		this.data_filename = data_filename;
	}
	
	public String getData_orgfilename() {
		return data_orgfilename;
	}
	
	public void setData_orgfilename(String data_orgfilename) {
		this.data_orgfilename = data_orgfilename;
	}
	
	public String getData_filesize() {
		return data_filesize;
	}
	
	public void setData_filesize(String data_filesize) {
		this.data_filesize = data_filesize;
	}
	
	public String getFk_data_cateno() {
		return fk_data_cateno;
	}
	
	public void setFk_data_cateno(String fk_data_cateno) {
		this.fk_data_cateno = fk_data_cateno;
	}
	
	
	
	
	
	
	
	
	
	
	
}
