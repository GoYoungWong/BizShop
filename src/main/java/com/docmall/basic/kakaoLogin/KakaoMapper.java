package com.docmall.basic.kakaoLogin;

public interface KakaoMapper {
	
	// 카카오계정 대표 이메일
	// 사용자 정보 가져오기의 kakao_account.email에 해당
	KakaoUserInfo existskakaoInfo(String sns_email);
	
}
