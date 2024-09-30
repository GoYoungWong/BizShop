package com.docmall.basic.notioe;

import java.util.List;

import com.docmall.basic.admin.notioe.NoticeVO;
import com.docmall.basic.common.dto.Criteria;

public interface NotioeMapper {
	
	// 공지사항 리스트(페이징 추가, 검색조건)
	List<NoticeVO> NoticeList(Criteria cri);
   
	// 데이터 총개수
	int getNoticeCount(Criteria cri);

}
