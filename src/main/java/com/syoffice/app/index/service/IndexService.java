package com.syoffice.app.index.service;

import java.util.List;
import java.util.Map;

public interface IndexService {

	// 시작페이지에서 캐러젤을 보여주는 것
	List<Map<String, String>> getImgfilenameList();
	
}
