package com.syoffice.app.employee.model;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.syoffice.app.employee.domain.EmployeeVO;

@Mapper
public interface EmployeeDAO {
	
	// 로그인 처리
	EmployeeVO login(Map<String, String> paraMap);
	
	// 로그인 시 마지막 비밀번호 변경일자가 3개월 이상 경과 시 비밀번호 변경이 필요한 상태로 전환
	void updatePwdChangeStatus(String emp_id);
	
	// 비밀번호 변경
	int pwdChange(String newPwd, String emp_id);
	
	// 비밀번호 변경 상태 업데이트
	void pwdStatusUpdate(String emp_id);

}
