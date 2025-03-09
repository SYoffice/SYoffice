package com.syoffice.app.dataroom.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.syoffice.app.dataroom.domain.DataVO;
import com.syoffice.app.dataroom.domain.DatacategoryVO;
import com.syoffice.app.dataroom.model.DataroomDAO;
import com.syoffice.app.employee.domain.EmployeeVO;

//==== #2. Service 선언 ====
//트랜잭션 처리를 담당하는 곳, 업무를 처리하는 곳, 비지니스(Business)단
//@Component
@Service
public class DataroomService_imple implements DataroomService {

	// === #5. 의존객체 주입하기(DI: Dependency Injection) ===
	@Autowired  // Type에 따라 알아서 Bean 을 주입해준다.
	private DataroomDAO dao;
	// Type 에 따라 Spring 컨테이너가 알아서 bean 으로 등록된 com.spring.app.index.model.IndexDAO_imple 의 bean 을 dao 에 주입시켜준다. 
	// 그러므로 dao 는 null 이 아니다.
	
	// 자료실 폴더 목록 조회
	@Override
	public List<DatacategoryVO> getDataCategoryList() {
		
		return dao.getDataCategoryList();  
	}
	
	
	// 특정 폴더의 파일 조회
	@Override
    public List<DataVO> getFileListByCategory(String data_cateno) {
        return dao.getFileListByCategory(data_cateno);  
    }

	// 새폴더 추가
	@Override
	public int insertCategory(DatacategoryVO category) {
		return dao.insertCategory(category);
	}


	// 폴더 삭제
	@Override
    public int deleteCategoryWithFiles(String data_cateno) {
        // 폴더 안에 파일 삭제
        dao.deleteFilesInCategory(data_cateno);

        // 폴더 삭제
        return dao.deleteCategory(data_cateno);
    }

	// 전체 파일 조회
	@Override
	public int getTotalFile(Map<String, String> paraMap) {
	    return dao.getTotalFile(paraMap);
	}
	
	// 검색, 페이징한 파일 리스트 가져오기
	@Override
	public List<DataVO> getFileList(Map<String, String> paraMap) {
	    return dao.getFileList(paraMap);
	}
	
	
	 //  파일 업로드 
    @Override
    public void insertFile(DataVO fileData) {
        dao.insertFile(fileData);
    }

    //  파일 다운로드
    @Override
    public DataVO getFile(String data_no) {
        return dao.getFile(data_no);
    }
    
    // 선택된 폴더 이름 조회
    @Override
    public String getCategoryName(String data_cateno) {
        return dao.getCategoryName(data_cateno);
    }
    
    // 파일 삭제
	@Override
	public void deleteFile(String data_no) {
		dao.deleteFile(data_no);
	}

	
	
	
	
	
	
}
