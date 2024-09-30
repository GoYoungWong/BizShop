package com.docmall.basic.admin.notioe;

import java.util.List;

import org.springframework.stereotype.Service;

import com.docmall.basic.common.dto.Criteria;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeService {

	private final NoticeMapper noticeMapper;
	
	// 페이징 & 검색조건이 추가된 리스트
	public List<NoticeVO> NoticeList(Criteria cri) {
		return noticeMapper.NoticeList(cri);
	}
		
	// 데이터 총개수
	public int getNoticeCount(Criteria cri) {
		return noticeMapper.getNoticeCount(cri);
	}
	
	// 작성글 저장
	public void insert_notice(NoticeVO vo) {
		noticeMapper.insert_notice(vo);
	}
	
	// 작성글 수정폼
	public NoticeVO getNoticeinfo(Integer idx) {
		log.info("서비스에서 idx: " + idx);
		return noticeMapper.getNoticeinfo(idx);
	}
	
	// 작성글 수정저장
	public void update_notice(NoticeVO vo) {
		noticeMapper.update_notice(vo);
	}
	
	// 작성글 삭제
	public void notice_delete(Integer idx) {
		noticeMapper.notice_delete(idx);
	}
	
	
}
