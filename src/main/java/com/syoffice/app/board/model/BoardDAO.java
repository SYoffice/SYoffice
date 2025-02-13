package com.syoffice.app.board.model;

import org.apache.ibatis.annotations.Mapper;

import com.syoffice.app.board.domain.BoardVO;

//=== SqlSessionTemplate 을 사용하지 않는 Mapper Interface 예제(myBatis 3.0 이상 부터 사용가능함) === //
@Mapper	  // @Mapper 어노테이션을 붙여서 DAO 역할의 Mapper 인터페이스 파일을 만든다. 
		  // EmpDAO 인터페이스를 구현한 DAO 클래스를 생성하면 오류가 뜨므로 절대로 DAO 클래스를 생성하면 안된다.!!! 
		  // @Mapper 어노테이션을 사용하면 빈으로 등록되며 Service단에서 @Autowired 하여 사용할 수 있게 된다. 

public interface BoardDAO {

	// === #4.파일첨부가 없는 글쓰기 === // 
	int GroupWare_Write(BoardVO boardvo);

}
