package com.docmall.basic.qnaboard;

import java.util.List;

import org.springframework.stereotype.Service;

import com.docmall.basic.common.dto.Criteria;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class QnaBoardService {

	private final QnaBoardMapper qnaBoardMapper;
	
	// QnA리스트
	public List<QnaBoardVO> qnalist(int pro_num,Criteria cri) {
		return qnaBoardMapper.qnalist(pro_num, cri);
		}

	// QnA리스트 총개수
	public int getCountQnaBypro_num(int pro_num) {
		return qnaBoardMapper.getCountQnaBypro_num(pro_num);
	}
	
	// QnA저장
	public void qna_save(QnaBoardVO vo) {
		qnaBoardMapper.qna_save(vo);
	}
}
