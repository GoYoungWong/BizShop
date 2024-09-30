package com.docmall.basic.admincategory;

import java.util.List;

public interface AdminCategoryMapper {

	// 1차카테고리 목록
	List<AdminCategoryVO> getFirstCategoryList();
	
	// 2차카테고리 목록
	// int cat_prtcode: 어떤 1차 카테고리에 속한 2차 카테고리를 조회할지를 지정하기 위해서입니다.
	List<AdminCategoryVO> getSecondCategoryList(int cat_prtcode);

	// 2차 카테고리를 이용한 1차카테고리정보 가져오기
	AdminCategoryVO getFristCategoryBySecondCategory(int cat_code);
		
}
