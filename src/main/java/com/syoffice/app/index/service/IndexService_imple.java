package com.syoffice.app.index.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.syoffice.app.board.model.BoardDAO;
import com.syoffice.app.employee.model.EmployeeDAO;
import com.syoffice.app.index.model.IndexDAO;

@Service
public class IndexService_imple implements IndexService {

	@Autowired
	private IndexDAO indexDAO;
	
	// 로그인 사용자 정보 조회
	@Override
	public Map<String, Object> getLoginUserMap(int emp_id) {
		 return indexDAO.getLoginUserMap(emp_id);
	}

	// 내 일정 
    @Override
    public List<Map<String, String>> getMySchedule(Map<String, String> paramMap) {
        return indexDAO.getMySchedule(paramMap);
    }

    // 부서 일정 
    @Override
    public List<Map<String, String>> getDeptSchedule(Map<String, String> dep_paramMap) {
        return indexDAO.getDeptSchedule(dep_paramMap);
    }

    // 공지사항  
    @Override
    public List<Map<String, String>> getNoticeList() {
        return indexDAO.getNoticeList();
    }
    
    // 이번주 내 실적 
    @Override
    public List<Map<String, Object>> getWeeklyPerformance(Map<String, String> paramMap) {
        return indexDAO.getWeeklyPerformance(paramMap);
    }

    @Override
    public List<Map<String, Object>> getDepartmentPerformance(String empId) {
        // 부서 실적과 개인 실적을 비교하는 데이터 반환
        return indexDAO.getDepartmentPerformance(empId);
    }

    
}
