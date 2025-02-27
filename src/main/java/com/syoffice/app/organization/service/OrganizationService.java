package com.syoffice.app.organization.service;

import java.util.List;
import java.util.Map;

public interface OrganizationService {
	
	// 조직도 데이터 가져오기
	List<Map<String, Object>> selectOrganization();
	
	// 조직도 차트 
    List<Map<String, Object>> selectOrganizationByDept(String dept_name, String branch_name);
    
    // 부서들 조회
    List<Map<String, Object>> selectDepartments();
    
    // 지점들 조회
    List<Map<String, Object>> selectBranches();
    
    
    
    
    
    
    
    
}
