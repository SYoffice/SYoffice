package com.syoffice.app.dataroom.model;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.syoffice.app.dataroom.domain.DataVO;
import com.syoffice.app.dataroom.domain.DatacategoryVO;

@Mapper
public interface DataroomDAO {

	// 자료실 폴더 목록 조회
	List<DatacategoryVO> getDataCategoryList();  

	// 특정 폴더의 파일 리스트 가져오기
    List<DataVO> getFileListByCategory(@Param("data_cateno") String data_cateno);
    

    // 새폴더 추가
	int insertCategory(DatacategoryVO category);

	// 폴더 안에 파일 삭제
	void deleteFilesInCategory(String data_cateno);
	
	// 폴더 삭제
	int deleteCategory(String data_cateno);

	// 전체 파일 조회
	int getTotalFile(Map<String, String> paraMap);
	
	// 검색, 페이징한 파일 리스트 가져오기
	List<DataVO> getFileList(Map<String, String> paraMap);
	
	//  파일 업로드 
    void insertFile(DataVO fileData);

    // 파일 다운로드
    DataVO getFile(String data_no);
    
    // 현재 선택된 폴더 이름(data_catename) 조회
 	String getCategoryName(String data_cateno);
    
    // 파일 삭제
	void deleteFile(String data_no);

	
	
}
