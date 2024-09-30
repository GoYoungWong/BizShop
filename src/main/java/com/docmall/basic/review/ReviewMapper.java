package com.docmall.basic.review;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.docmall.basic.common.dto.Criteria;

public interface ReviewMapper {
	
	// 후기 리스트
	List<ReviewVO> rev_list(@Param("pro_num") Integer pro_num,@Param("cri") Criteria cri);

	// 후기 총개수
	int getCountReviewBypro_num(Integer pro_num);
	
	// 상품후기 저장
	void review_save(ReviewVO vo);
	
	// 상품후기 삭제
	void review_delete(Integer rev_code);
	
	// 상품후기 수정폼
	ReviewVO rev_modify(Integer rev_code);
	
	// 상품후기 저장
	void updat_rev(ReviewVO vo);
	
}
