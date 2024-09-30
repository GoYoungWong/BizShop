package com.docmall.basic.admin.review;

import java.util.List;

import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.review.ReviewVO;

public interface AdminReviewMapper {

	// 리뷰리스트
	List<ReviewVO> adminrevlist(Criteria cri);
	
	// 출력데이터 개수
	int getAdminRevCount(Criteria cri);
}
