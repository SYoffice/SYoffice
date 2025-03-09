package com.syoffice.app.organization.model;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OraganizationDAO {

	// 조직도 조회
	List<Map<String, Object>> selectOrganization();
	
	
	// 조직도 차트 
    List<Map<String, Object>> selectOrganizationByDept(@Param("dept_name") String dept_name, 
    												   @Param("branch_name") String branch_name);
    
    // 부서들 조회
    List<Map<String, Object>> selectDepartments();
    
    // 지점들 조회
    List<Map<String, Object>> selectBranches();
    
    
}
