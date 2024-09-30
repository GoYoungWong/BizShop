package com.docmall.basic.admin.StaticAnalysis;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisitorLogVO {

	// vi_id, user_id, session_id, vi_date, is_member
	
	private Integer vi_id;      // 일련번호
	private String user_id;     // 회원일경우 회원ID 아닐경우 NULL
	private String session_id;  // 비회원일경우 세션으로 비교
	private String is_member;   // 회원또는 비회원이 담김
	private String login_type;  // 로그인 타입
	private Date   vi_date;     // 로그기록시간
}
