package com.docmall.basic.kakaoLogin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor // 모든 필드를 인자로 받는 생성자를 자동으로 생성
@Getter
@Setter
@ToString
public class KakaoUserInfo {

	private Long id;
	private String nickname;
	private String email;
	
}
