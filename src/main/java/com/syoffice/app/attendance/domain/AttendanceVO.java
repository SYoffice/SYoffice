package com.syoffice.app.attendance.domain;

import java.util.Date;

public class AttendanceVO {
    
    private int attendNo;         
    private Date attendDate;  
    private Date attendStart; 
    private Date attendEnd;    
    private String fkEmpId;  
    private int attendStatus;

    // 기존 사원 정보 필드
    private String empId;  
    private String name;
    private String email;
    
    // 달력 이벤트용 추가 필드
    private String title;      
    private String eventType; 

    public AttendanceVO() {}

    public int getAttendNo() {
        return attendNo;
    }

    public void setAttendNo(int attendNo) {
        this.attendNo = attendNo;
    }

    public Date getAttendDate() {
        return attendDate;
    }

    public void setAttendDate(Date attendDate) {
        this.attendDate = attendDate;
    }

    public Date getAttendStart() {
        return attendStart;
    }

    public void setAttendStart(Date attendStart) {
        this.attendStart = attendStart;
    }

    public Date getAttendEnd() {
        return attendEnd;
    }

    public void setAttendEnd(Date attendEnd) {
        this.attendEnd = attendEnd;
    }

    public String getFkEmpId() {
        return fkEmpId;
    }

    public void setFkEmpId(String fkEmpId) {
        this.fkEmpId = fkEmpId;
    }

    public int getAttendStatus() {
        return attendStatus;
    }

    public void setAttendStatus(int attendStatus) {
        this.attendStatus = attendStatus;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
