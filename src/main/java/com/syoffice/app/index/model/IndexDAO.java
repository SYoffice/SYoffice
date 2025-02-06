package com.syoffice.app.index.model;

import java.util.List;
import java.util.Map;

public interface IndexDAO {

	// 시작페이지에서 캐러젤을 보여주는 것
	List<Map<String, String>> getImgfilenameList();
	
}
