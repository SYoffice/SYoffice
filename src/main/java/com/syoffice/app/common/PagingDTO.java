package com.syoffice.app.common;

/*
 * 페이징을 위해 페이지 정보를 담는 DTO 
 */
public class PagingDTO {
	
	// 입력 데이터
	private int curPage = 1; // 현재 페이지 번호

	private int rowSizePerPage; // 페이지 당 행의 수

	private int pageSize; // 페이지리스트에서 보여줄 페이지 개수 (1페이지, 2페이지 ...)

	private int totalRowCount; // 전체 레코드(데이터) 개수

	// 계산되는 데이터
	private int firstRow; // 시작 행 번호 ex) 1번 레코드

	private int lastRow; // 마지막 행 번호 ex) 10번 레코드

	private int totalPageCount; // 총 페이지 수

	private int firstPage; // 페이지 리스트에서 시작 페이지 번호 ex) [6], [7], [8], [9], [10] 에서 6를 뜻함

	private int lastPage; // 페이지 리스트에서 마지막 페이지 번호 ex) [6], [7], [8], [9], [10] 에서 10를 뜻함
	
	// 기본 생성자
	public PagingDTO() {}
	
	// 부분 생성자 
	// 현재 페이지 및 전체 행 개수를 받아 나머지 페이징 정보 연산
	public PagingDTO(int curPage, int totalRowCount) {
		// curPage 유효성 검사
		if(curPage > totalRowCount || curPage < 1 ) {
			curPage = 1;
		}
		
		this.curPage = curPage;
		this.totalRowCount = totalRowCount;
		this.pageSize = 5;
		this.rowSizePerPage = 8;
		this.pageSetting(); // 페이징 시 필요한 나머지 정보 계산
	}

	public void pageSetting() {
		totalPageCount= (totalRowCount-1) / rowSizePerPage + 1;
		
		firstRow = (curPage - 1) * rowSizePerPage + 1;
		
		lastRow = firstRow + rowSizePerPage - 1;
		
		if(lastRow > totalRowCount) {
			lastRow = totalRowCount;
		}

		// 현재 보고 있는 페이지 라인 기준 첫번째 페이지이다.
		// ex) [6], [7], [8], [9], [10] 이 중 어떤 페이지라도 6으로 나온다
		firstPage = (curPage - 1) / pageSize * pageSize + 1;  
 
		// 현재 보고 있는 페이지 라인 기준 마지막 페이지이다.
		// ex) [6], [7], [8], [9], [10] 이 중 어떤 페이지라도 10으로 나온다
		lastPage = firstPage+pageSize-1;
		
		if(lastPage > totalPageCount) {
			lastPage = totalPageCount;
		}
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getRowSizePerPage() {
		return rowSizePerPage;
	}

	public void setRowSizePerPage(int rowSizePerPage) {
		this.rowSizePerPage = rowSizePerPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalRowCount() {
		return totalRowCount;
	}

	public void setTotalRowCount(int totalRowCount) {
		this.totalRowCount = totalRowCount;
	}

	public int getFirstRow() {
		return firstRow;
	}

	public void setFirstRow(int firstRow) {
		this.firstRow = firstRow;
	}

	public int getLastRow() {
		return lastRow;
	}

	public void setLastRow(int lastRow) {
		this.lastRow = lastRow;
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

	public int getFirstPage() {
		return firstPage;
	}

	public void setFirstPage(int firstPage) {
		this.firstPage = firstPage;
	}

	public int getLastPage() {
		return lastPage;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

}
