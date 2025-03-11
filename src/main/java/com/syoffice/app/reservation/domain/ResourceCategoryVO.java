package com.syoffice.app.reservation.domain;

public class ResourceCategoryVO {
	
	private String category_no;    /* 자원분류번호 */
	private String category_name;  /* 자원분류명(회의실, 차량 등..) */
	
	
	
	public String getCategory_no() {
		return category_no;
	}
	
	public void setCategory_no(String category_no) {
		this.category_no = category_no;
	}
	
	public String getCategory_name() {
		return category_name;
	}
	
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
}
