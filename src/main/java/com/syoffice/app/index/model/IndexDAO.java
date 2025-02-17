package com.syoffice.app.index.model;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IndexDAO {

	// 로그인 사용자 정보 조회
	Map<String, Object> getLoginUserMap(Integer emp_Id);

}
