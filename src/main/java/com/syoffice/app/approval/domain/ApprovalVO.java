package com.syoffice.app.approval.domain;

public class ApprovalVO {
   
   private String apr_no;        /* 전자결재번호, 시퀀스사용(apr_seq) */
   private String apr_startdate; /* 기안일 */
   private String apr_enddate;   /* 결재완료일 */
   private String apr_status;    /* 문서결재상태 (1: 기안, 2: 진행중, 3: 전결대기,4: 전결, 0: 반려) */
   private String apr_important; /* 문서긴급여부 (0: 기본, 1: 긴급) */
   private String fk_leave_no;   /* 근태신청서번호, 참조테이블(tbl_leaveform) */
   private String fk_draft_no;   /* 품의서번호, 참조테이블(tbl_draftform) */
   private String fk_expend_no;  /* 지출결의서번호, 참조테이블(tbl_expendform) */
   private String apr_comment;   /* 결재 의견 */
   private String apr_approver;  
   private String apr_approver2;
   private String apr_approver3;
   private String type;           /* 전자결재종류 1 품의서, 2 지출결의서, 3 근태신청서 */
   private String fk_emp_id;
   private String apr_acceptday1;
   private String apr_acceptday2;
   private String apr_acceptday3;

   // select 
   private String typename;
   private String statusname;
   private String name;    // 기안자명
   private String dept_name;
   
   private String leave_subject;
   private String leave_content;
   private String leave_type;
   private String leave_startdate;
   private String leave_enddate;
   
   private String apr_approver_name;  
   private String apr_approver2_name;
   private String apr_approver3_name;
   
   private String draft_subject;
   private String draft_content;

   
   
   public String getApr_no() {
      return apr_no;
   }
   
   public void setApr_no(String apr_no) {
      this.apr_no = apr_no;
   }
   
   public String getApr_startdate() {
      return apr_startdate;
   }
   
   public void setApr_startdate(String apr_startdate) {
      this.apr_startdate = apr_startdate;
   }
   
   public String getApr_enddate() {
      return apr_enddate;
   }
   
   public void setApr_enddate(String apr_enddate) {
      this.apr_enddate = apr_enddate;
   }
   
   public String getApr_status() {
      return apr_status;
   }
   
   public void setApr_status(String apr_status) {
      this.apr_status = apr_status;
   }
   
   public String getApr_important() {
      return apr_important;
   }
   
   public void setApr_important(String apr_important) {
      this.apr_important = apr_important;
   }
   
   public String getFk_leave_no() {
      return fk_leave_no;
   }
   
   public void setFk_leave_no(String fk_leave_no) {
      this.fk_leave_no = fk_leave_no;
   }
   
   public String getFk_draft_no() {
      return fk_draft_no;
   }
   
   public void setFk_draft_no(String fk_draft_no) {
      this.fk_draft_no = fk_draft_no;
   }
   
   public String getFk_expend_no() {
      return fk_expend_no;
   }
   
   public void setFk_expend_no(String fk_expend_no) {
      this.fk_expend_no = fk_expend_no;
   }
   
   public String getApr_comment() {
      return apr_comment;
   }
   
   public void setApr_comment(String apr_comment) {
      this.apr_comment = apr_comment;
   }
   
   public String getApr_approver() {
      return apr_approver;
   }

   public void setApr_approver(String apr_approver) {
      this.apr_approver = apr_approver;
   }

   public String getApr_approver2() {
      return apr_approver2;
   }

   public void setApr_approver2(String apr_approver2) {
      this.apr_approver2 = apr_approver2;
   }

   public String getApr_approver3() {
      return apr_approver3;
   }

   public void setApr_approver3(String apr_approver3) {
      this.apr_approver3 = apr_approver3;
   }
   
   public String getDraft_subject() {
      return draft_subject;
   }

   public void setDraft_subject(String draft_subject) {
      this.draft_subject = draft_subject;
   }
   
   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getFk_emp_id() {
      return fk_emp_id;
   }

   public void setFk_emp_id(String fk_emp_id) {
      this.fk_emp_id = fk_emp_id;
   }

   public String getApr_acceptday1() {
      return apr_acceptday1;
   }

   public void setApr_acceptday1(String apr_acceptday1) {
      this.apr_acceptday1 = apr_acceptday1;
   }

   public String getApr_acceptday2() {
      return apr_acceptday2;
   }

   public void setApr_acceptday2(String apr_acceptday2) {
      this.apr_acceptday2 = apr_acceptday2;
   }

   public String getApr_acceptday3() {
      return apr_acceptday3;
   }

   public void setApr_acceptday3(String apr_acceptday3) {
      this.apr_acceptday3 = apr_acceptday3;
   }

   public String getTypename() {
      return typename;
   }

   public void setTypename(String typename) {
      this.typename = typename;
   }

   public String getStatusname() {
      return statusname;
   }

   public void setStatusname(String statusname) {
      this.statusname = statusname;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDept_name() {
      return dept_name;
   }

   public void setDept_name(String dept_name) {
      this.dept_name = dept_name;
   }

   public String getLeave_subject() {
      return leave_subject;
   }

   public void setLeave_subject(String leave_subject) {
      this.leave_subject = leave_subject;
   }

   public String getLeave_content() {
      return leave_content != null ? leave_content.replaceAll("\\n", "<br>") : null;
   }

   public void setLeave_content(String leave_content) {
      this.leave_content = leave_content;
   }

   public String getLeave_type() {
      return leave_type;
   }

   public void setLeave_type(String leave_type) {
      this.leave_type = leave_type;
   }

   public String getLeave_startdate() {
      return leave_startdate;
   }

   public void setLeave_startdate(String leave_startdate) {
      this.leave_startdate = leave_startdate;
   }

   public String getLeave_enddate() {
      return leave_enddate;
   }

   public void setLeave_enddate(String leave_enddate) {
      this.leave_enddate = leave_enddate;
   }

   public String getDraft_content() {
      return draft_content != null ? draft_content.replaceAll("\\n", "<br>") : null;
   }

   public void setDraft_content(String draft_content) {
      this.draft_content = draft_content;
   }

   public String getApr_approver_name() {
      return apr_approver_name;
   }

   public void setApr_approver_name(String apr_approver_name) {
      this.apr_approver_name = apr_approver_name;
   }

   public String getApr_approver2_name() {
      return apr_approver2_name;
   }

   public void setApr_approver2_name(String apr_approver2_name) {
      this.apr_approver2_name = apr_approver2_name;
   }

   public String getApr_approver3_name() {
      return apr_approver3_name;
   }

   public void setApr_approver3_name(String apr_approver3_name) {
      this.apr_approver3_name = apr_approver3_name;
   }
   
   

   
   
   
}
