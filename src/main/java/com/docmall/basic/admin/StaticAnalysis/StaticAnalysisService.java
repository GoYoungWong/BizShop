package com.docmall.basic.admin.StaticAnalysis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StaticAnalysisService {

	private final StaticAnalysisMapper staticAnalysisMapper;
	
	// 카테고리별월별매출현황
	public List<Map<String, Object>> Monthlysalesstatus(String ordDate) {
		return staticAnalysisMapper.Monthlysalesstatus(ordDate);
	}
	
	// 방문기록 삽입
	public void insertVisttorLog(String user_id,String session_id,String is_member) {
		VisitorLogVO visitorLogVO = new VisitorLogVO();
		visitorLogVO.setUser_id(user_id);
		visitorLogVO.setSession_id(session_id);
		visitorLogVO.setIs_member(is_member);
		staticAnalysisMapper.insertVisttorLog(visitorLogVO);
	}
	
	// 방문기록업데이트
	public void updateVisitorLogToMember(String user_id,String session_id) {
		staticAnalysisMapper.updateVisitorLogToMember(user_id, session_id);
	}
		
	// 회원 및 비회원 방문 횟수 조회
	public List<Map<String, Object>> getVisitCount(String UserDate) {
		return staticAnalysisMapper.getVisitCount(UserDate);
	}
	
	
}
