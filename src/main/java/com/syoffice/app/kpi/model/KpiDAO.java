package com.syoffice.app.kpi.model;

import java.util.List;
import java.util.Map;

import com.syoffice.app.kpi.domain.KpiVO;
import com.syoffice.app.kpi.domain.ResultVO;

public interface KpiDAO {

	// === 목표 실적액 등록하기 === //
	int kpiRegister(KpiVO kpivo);

	// === 특정 부서의 목표실적 가져오기 === //
	List<KpiVO> getDeptKpi(Map<String, String> paraMap);

	// === 기존에 등록한 목표가 있는지 확인하기 === //
	int getExistKpi(KpiVO kpivo);

	// === 목표실적 삭제 === //
	int kpiDelete(String kpi_no);

	// === 한개의 목표실적 가져오기 === //
	KpiVO getKpiOne(String kpi_no);

	// === 목표실적 수정 === //
	int editKpi(KpiVO kpivo);
	
	// === 실적입력을 위한 목표실적 번호 채번 === //
	String getKpi_no(Map<String, String> paraMap);

	// === 엑셀파일을 통한 실적 입력 === //
	int add_resultList(Map<String, String> paraMap);

	// === 연도, 분기별 부서원 실적 정보 === //
	List<ResultVO> getResultBydeptKpi(Map<String, String> paraMap);

}
