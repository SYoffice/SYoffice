package com.syoffice.app.index.domain;

public class IndexVO {
    private int empId;
    private String name;
    private String mail;
    private String gradeName; 
    private String maanager_id;
    

    public String getMaanager_id() {
		return maanager_id;
	}
	public void setMaanager_id(String maanager_id) {
		this.maanager_id = maanager_id;
	}
	public int getEmpId() {
        return empId;
    }
    public void setEmpId(int empId) {
        this.empId = empId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public String getGradeName() {
        return gradeName;
    }
    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }
}
