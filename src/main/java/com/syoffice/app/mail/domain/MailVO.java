package com.syoffice.app.mail.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailVO {

    // 단일 메일 가져오기 위한 VO

    // column insert
    private String mail_no;         // 메일번호
    private String fk_emp_id;       // 발신인
    private String mail_subject;    // 메일제목
    private String mail_content;    // 메일내용
    private String mail_senddate;   // 메일발송시각
    private String mail_important;  // 중요여부


    // select
    private String receiver;        // 수신인
    private String receivercc;      // 수신인 수신 or 참조여부
    private String receiver_name;   // 수신 or 참조인 이름
    private String receiver_mail;   // 수신 or 참조인 메일
    private String sender;          // 발신인
    private String sender_mail;     // 발신인 메일
    private String sender_name;     // 발신인 이름
    private String atmail_no;       // 첨부파일번호
    private String atmail_filename; // 첨부파일명(in WAS)
    private String atmail_orgfilename; // 첨부파일명(원본파일명)
    private String atmail_filesize; // 첨부파일크기
    private String receive_no; 		// 메일수신번호
    
    
	public String getMail_no() {
		return mail_no;
	}
	public void setMail_no(String mail_no) {
		this.mail_no = mail_no;
	}
	public String getFk_emp_id() {
		return fk_emp_id;
	}
	public void setFk_emp_id(String fk_emp_id) {
		this.fk_emp_id = fk_emp_id;
	}
	public String getMail_subject() {
		return mail_subject;
	}
	public void setMail_subject(String mail_subject) {
		this.mail_subject = mail_subject;
	}
	public String getMail_content() {
		return mail_content;
	}
	public void setMail_content(String mail_content) {
		this.mail_content = mail_content;
	}
	public String getMail_senddate() {
		return mail_senddate;
	}
	public void setMail_senddate(String mail_senddate) {
		this.mail_senddate = mail_senddate;
	}
	public String getMail_important() {
		return mail_important;
	}
	public void setMail_important(String mail_important) {
		this.mail_important = mail_important;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getReceivercc() {
		return receivercc;
	}
	public void setReceivercc(String receivercc) {
		this.receivercc = receivercc;
	}
	public String getReceiver_name() {
		return receiver_name;
	}
	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}
	public String getReceiver_mail() {
		return receiver_mail;
	}
	public void setReceiver_mail(String receiver_mail) {
		this.receiver_mail = receiver_mail;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getSender_mail() {
		return sender_mail;
	}
	public void setSender_mail(String sender_mail) {
		this.sender_mail = sender_mail;
	}
	public String getSender_name() {
		return sender_name;
	}
	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}
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
	public String getReceive_no() {
		return receive_no;
	}
	public void setReceive_no(String receive_no) {
		this.receive_no = receive_no;
	}


    
    
}
