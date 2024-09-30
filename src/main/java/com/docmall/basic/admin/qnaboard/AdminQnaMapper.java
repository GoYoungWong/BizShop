package com.docmall.basic.admin.qnaboard;

import java.util.List;

import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.qnaboard.QnaBoardVO;

public interface AdminQnaMapper {
	
	// 관리자 qna리스트
	List<QnaBoardVO> adminqnalist(Criteria cri);
	
	// 총데이터 개수
	int getAdminQnaCount(Criteria cri);
	
	// qna후기 폼
	QnaBoardVO qna_modify(Long qna_idx);
	
	// 답변저장
	void update_qna(QnaBoardVO vo);
	
	// QnA삭제
	void qna_delete(Long qna_idx);
	
}
