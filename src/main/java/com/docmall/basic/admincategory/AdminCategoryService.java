package com.docmall.basic.admincategory;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {

	private final AdminCategoryMapper adminCategoryMapper;
	
	// 1차카테고리 목록
	public List<AdminCategoryVO> getFirstCategoryList() {
		return adminCategoryMapper.getFirstCategoryList();
	}
	
	// 2차카테고리 목록
	public List<AdminCategoryVO> getSecondCategoryList(int cat_prtcode) {
		return adminCategoryMapper.getSecondCategoryList(cat_prtcode);
	}
	
	// 2차 카테고리를 이용한 1차카테고리정보 가져오기
	public AdminCategoryVO getFristCategoryBySecondCategory(int cat_code) {
		return adminCategoryMapper.getFristCategoryBySecondCategory(cat_code);
	}
}
