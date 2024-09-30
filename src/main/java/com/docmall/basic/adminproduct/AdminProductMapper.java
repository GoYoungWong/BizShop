package com.docmall.basic.adminproduct;

import java.util.List;

import com.docmall.basic.common.dto.Criteria;

public interface AdminProductMapper {
	
	// 상품등록
	void pro_insert(AdminProductVO vo);
	
	// 상품리스트
	List<AdminProductVO> pro_list(Criteria cri);
	
	// 총데이터 개수
	int getTotalCount(Criteria cri);
	
	// 상품수정
	AdminProductVO pro_modify(Integer pro_num);
	
	// 상품수정(업데이트)
	void pro_modify_ok(AdminProductVO vo);
	
	// 상품삭제
	void pro_delete(Integer pro_num);
	
	// 체크상품 수정
	void pro_check_box(List<AdminProductDTO> pro_box_modify);
	
	

}
