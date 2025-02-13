package com.syoffice.app.board.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.syoffice.app.board.domain.BoardVO;
import com.syoffice.app.board.model.BoardDAO;

@Service
public class BoardService_imple implements BoardService {

	@Autowired
	private BoardDAO mapper_dao;

	// === #3.파일첨부가 없는 글쓰기 === //
	@Override
	public int GroupWare_Write(BoardVO boardvo) {
		int n = mapper_dao.GroupWare_Write(boardvo);
		return n;
	}

	
}
