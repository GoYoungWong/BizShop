package com.docmall.basic.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// spring security 암호화를 사용하기 위한 설정파일
// 그 안에 메소드 타입을 빈으로 사용할려면 @Bean이걸 달아 bean을 생성할수 있다.
// bean으로 생성하면 필요한 곳에 자동으로 의존성주입을 한다.
// @Configuration @Bean 쌍으로 같이 만든다.

@Configuration // 설정목적으로 사용하는 클래스에는 이 어노테이션 @Configuration 적용
// @EnableWebSecurity  // 스프링 시큐리티를 구성한다고 알리는 어노테이션
public class SecurityConfig {

	// 스프링시큐리티 설정.  v2.7 과 v3.x 버전 차이가 있음
//	@Bean
//	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http
//					.csrf((csrf) -> csrf.disable());
////			          .cors((c) -> c.disable()) 
////					  .headers((headers) -> headers.disable());
//		return http.build();
//	}
	
	// 암호화기능 bean생성. passwordEncoder bean 자동생성.
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
