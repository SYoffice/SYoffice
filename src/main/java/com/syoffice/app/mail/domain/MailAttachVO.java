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

}
