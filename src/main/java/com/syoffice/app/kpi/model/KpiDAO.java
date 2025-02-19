package com.syoffice.app.kpi.model;

import java.util.List;
import java.util.Map;

import com.syoffice.app.kpi.domain.KpiVO;

public interface KpiDAO {

	// === 목표 실적액 등록하기 === //
	int kpiRegister(KpiVO kpivo);

	// === 특정 부서의 목표실적 가져오기 === //
	List<KpiVO> getDeptKpi(Map<String, String> paraMap);

	// === 기존에 등록한 목표가 있는지 확인하기 === //
	int getExistKpi(KpiVO kpivo);

}
