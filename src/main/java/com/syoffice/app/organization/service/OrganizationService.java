package com.syoffice.app.organization.service;

import java.util.List;
import java.util.Map;

public interface OrganizationService {
	
	// 조직도 데이터 가져오기
	List<Map<String, Object>> selectOrganization();
	

	// 부서별로 조직도 차트 
    List<Map<String, Object>> selectOrganizationByDept(String dept_name, String branch_name);
}
