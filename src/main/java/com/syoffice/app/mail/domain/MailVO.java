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
    private String receive_division;// 메일박스분류
    private String receive_status; 	// 메일수신상태


}
