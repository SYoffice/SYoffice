package com.syoffice.app.dataroom.service;

import java.util.List;
import java.util.Map;

import com.syoffice.app.dataroom.domain.DataVO;
import com.syoffice.app.dataroom.domain.DatacategoryVO;

public interface DataroomService {

	// 자료실 폴더 목록 조회
	List<DatacategoryVO> getDataCategoryList();
	
	// 특정 폴더의 파일 리스트 가져오기
    List<DataVO> getFileListByCategory(String data_cateno);
    
    // 새폴더 추가
	int insertCategory(DatacategoryVO category);
	
	// 폴더 삭제
	int deleteCategoryWithFiles(String data_cateno);
	
	// 전체 파일 조회
	int getTotalFile(Map<String, String> paraMap);

	// 검색, 페이징한 파일 리스트 가져오기
	List<DataVO> getFileList(Map<String, String> paraMap);
	
	// 파일 업로드 (DB에 저장)
    void insertFile(DataVO fileData);

    // 특정 파일 정보 가져오기 (다운로드 용)
    DataVO getFile(String data_no);
    
    // 파일삭제
	void deleteFile(String data_no);
	
	// 선택된 폴더 이름 조회
	String getCategoryName(String fk_data_cateno);

    
    
    
    
    
}
