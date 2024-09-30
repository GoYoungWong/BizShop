package com.docmall.basic.admin;

public interface AdminMapper {

	// 관리자 가입
	void Admin_join(AdminVO vo);
	
	// 관리자로그인
	AdminVO adminlogin(String admin_id);
}
