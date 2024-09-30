package com.docmall.basic.notioe;

import java.util.List;

import org.springframework.stereotype.Service;

import com.docmall.basic.admin.notioe.NoticeVO;
import com.docmall.basic.common.dto.Criteria;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotioeService {
	
	private final NotioeMapper notioeMapper;
	
	// 공지사항 리스트(페이징 추가, 검색조건)
	public List<NoticeVO> NoticeList(Criteria cri) {
		return notioeMapper.NoticeList(cri);
	}
	
	// 데이터 총개수
	public int getNoticeCount(Criteria cri) {
		return notioeMapper.getNoticeCount(cri);
	}

}
