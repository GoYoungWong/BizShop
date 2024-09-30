package com.docmall.basic.admin.StaticAnalysis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface StaticAnalysisMapper {

	// 카테고리별월별매출현황
	List<Map<String, Object>> Monthlysalesstatus(String ordDate);
	
	// 방문기록 삽입
	void insertVisttorLog(VisitorLogVO visitorLogVO);
	
	// 방문기록업데이트
	void updateVisitorLogToMember(@Param("user_id") String user_id,@Param("session_id") String session_id);
	
	// 회원 및 비회원 방문 횟수 조회
	List<Map<String, Object>> getVisitCount(String UserDate);
	
	
}
