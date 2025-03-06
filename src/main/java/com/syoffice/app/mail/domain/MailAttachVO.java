package com.syoffice.app.mail.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailAttachVO {
    private String atmail_no;           // 파일첨부번호
    private String atmail_filename;     // WAS 올라간 파일명
    private String atmail_orgfilename;  // 원본파일명
    private String atmail_filesize;     // 파일사이즈
    private String fk_mail_no;          // 메일번호
    
    
	public String getAtmail_no() {
		return atmail_no;
	}
	public void setAtmail_no(String atmail_no) {
		this.atmail_no = atmail_no;
	}
	public String getAtmail_filename() {
		return atmail_filename;
	}
	public void setAtmail_filename(String atmail_filename) {
		this.atmail_filename = atmail_filename;
	}
	public String getAtmail_orgfilename() {
		return atmail_orgfilename;
	}
	public void setAtmail_orgfilename(String atmail_orgfilename) {
		this.atmail_orgfilename = atmail_orgfilename;
	}
	public String getAtmail_filesize() {
		return atmail_filesize;
	}
	public void setAtmail_filesize(String atmail_filesize) {
		this.atmail_filesize = atmail_filesize;
	}
	public String getFk_mail_no() {
		return fk_mail_no;
	}
	public void setFk_mail_no(String fk_mail_no) {
		this.fk_mail_no = fk_mail_no;
	}
    
    
    

}
