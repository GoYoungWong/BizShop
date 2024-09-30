package com.docmall.basic.admin.notioe;

import java.util.List;

import com.docmall.basic.common.dto.Criteria;

public interface NoticeMapper {
	
	// 페이징 & 검색조건이 추가된 리스트
	List<NoticeVO> NoticeList(Criteria cri);
	
	// 데이터 총개수
	int getNoticeCount(Criteria cri);
	
	// 작성글 저장
	void insert_notice(NoticeVO vo);
	
	// 작성글 수정폼
	NoticeVO getNoticeinfo(Integer idx);
	
	// 작성글 수정저장
	void update_notice(NoticeVO vo);
	
	// 작성글 삭제
	void notice_delete(Integer idx);
	

}
