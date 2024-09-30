package com.docmall.basic.admin.qnaboard;

import java.util.List;

import org.springframework.stereotype.Service;

import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.qnaboard.QnaBoardVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminQnaService {

	private final AdminQnaMapper adminQnaMapper;
	
	// 관리자 qna리스트
	public List<QnaBoardVO> adminqnalist(Criteria cri) {
		return adminQnaMapper.adminqnalist(cri);
	}
		
		// 총데이터 개수
	public int getAdminQnaCount(Criteria cri) {
		return adminQnaMapper.getAdminQnaCount(cri);
	}
	
	// qna후기 폼
	public QnaBoardVO qna_modify(Long qna_idx) {
		return adminQnaMapper.qna_modify(qna_idx);	
		}
	
	// 답변저장
	public void update_qna(QnaBoardVO vo) {
		adminQnaMapper.update_qna(vo);
	}
	
	// QnA삭제
	public void qna_delete(Long qna_idx) {
		adminQnaMapper.qna_delete(qna_idx);	
		}
}
