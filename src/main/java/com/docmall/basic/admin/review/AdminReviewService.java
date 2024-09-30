package com.docmall.basic.admin.review;

import java.util.List;

import org.springframework.stereotype.Service;

import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.review.ReviewVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminReviewService {

	private final AdminReviewMapper adminReviewMapper;
	
	// 리뷰리스트
	public List<ReviewVO> adminrevlist(Criteria cri) {
		return adminReviewMapper.adminrevlist(cri);
	}
		
	// 출력데이터 개수
	public int getAdminRevCount(Criteria cri) {
		return adminReviewMapper.getAdminRevCount(cri);
	}
}
