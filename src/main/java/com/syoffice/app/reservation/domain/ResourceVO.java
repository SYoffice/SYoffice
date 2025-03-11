package com.syoffice.app.reservation.domain;

public class ResourceVO {
	
	private String resource_no;     /* 자원번호, 시퀀스사용(resource_seq) */
	private String fk_category_no;  /* 자원분류번호 */
	private String resource_name;   /* 자원명(2층 회의실, 123가4566 등..) */
	private String resource_status; /* 자원상태(0:사용가능, 1:사용중) */
	private String category_name;  /* 자원분류명(회의실, 차량 등..) */
	
	
	public String getResource_no() {
		return resource_no;
	}
	
	public void setResource_no(String resource_no) {
		this.resource_no = resource_no;
	}
	
	public String getFk_category_no() {
		return fk_category_no;
	}
	
	public void setFk_category_no(String fk_category_no) {
		this.fk_category_no = fk_category_no;
	}
	
	public String getResource_name() {
		return resource_name;
	}
	
	public void setResource_name(String resource_name) {
		this.resource_name = resource_name;
	}
	
	public String getResource_status() {
		return resource_status;
	}
	
	public void setResource_status(String resource_status) {
		this.resource_status = resource_status;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	
}
