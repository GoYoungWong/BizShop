package com.docmall.basic.admin;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	
	private final AdminMapper adminMapper;
	
	// 관리자 가입
	public void Admin_join(AdminVO vo) {
		adminMapper.Admin_join(vo);
	}
	
	// 관리자로그인
	public AdminVO adminlogin(String admin_id) {
		return adminMapper.adminlogin(admin_id);
	}
	
}
