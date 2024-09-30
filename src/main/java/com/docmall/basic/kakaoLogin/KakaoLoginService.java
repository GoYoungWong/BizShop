package com.docmall.basic.kakaoLogin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class KakaoLoginService {

	@Value("${kakao.client.id}")
	private String clientId;
	
	@Value("${kakao.redirect.uri}")
	private String redirectUri;
	
	@Value("${kakao.client.secret}")
	private String clientsecret;
	
	@Value("${kakao.oauth.tokenuri}")
	private String tokenUri;
	
	@Value("${kakao.oauth.userinfouri}")
	private String userinfoUri;
	
	@Value("${kakao.user.logout}")
	private String kakaologout;
	
	
	// 액세스 토큰을 받기위한 정보
		// 주소: https://kauth.kakao.com/oauth/token 주소호출
		// 요청방식: post
		// 헤더(Header) : Content-type: application/x-www-form-urlencoded;charset=utf-8
		/* 본문(Body) : 
		  grant_type : authorization_code
		  client_id : 앱 REST API 키
		  redirect_uri : 인가 코드가 리다이렉트된 URI
		  code : 인가 코드 받기 요청으로 얻은 인가 코드
		  client_secret : 토큰 발급 시, 보안을 강화하기 위해 추가 확인하는 코드
		  
		 */
	
	public String getAccessToken(String code) throws JsonProcessingException {
		
		// 1) Http Header 설정
		HttpHeaders headers = new HttpHeaders(); 
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// 2) Http Body 생성
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", clientId);
		body.add("redirect_uri", redirectUri);
		body.add("code", code);
		body.add("client_secret",clientsecret);
		
		// 3) Header + Body 정보를 Entity로 구성.
		HttpEntity<MultiValueMap<String, String>> tokenKakaoRequest = new HttpEntity<MultiValueMap<String,String>>(body, headers);
		
		// 4)Http 요청보내기.(API Server와 통신을 담당하는 기능을 제공하는 클래스)  https://adjh54.tistory.com/234#google_vignette
		RestTemplate restTemplate = new RestTemplate();
		
		// 5)Http 응답(JSON) -> Access Token 추출(Parsing)작업
		ResponseEntity<String> response = restTemplate.exchange(tokenUri, HttpMethod.POST, tokenKakaoRequest, String.class);
		
		String responseBody = response.getBody();
		
		log.info("응답데이터: " + responseBody);
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);
		
		return jsonNode.get("access_token").asText();
	}
	
	//액세스토큰을 이용한 사용자정보 받아오기
	public KakaoUserInfo getKakaoUserInfo(String accessToken) throws JsonProcessingException {
		
		// 1) Header 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// 2)Body 생성안함. API 매뉴얼에서 필수가 아님.
		
		// 3) Header + Body 정보를 Entity로 구성.
		HttpEntity<MultiValueMap<String, String>> userInfoKakaoRequest = new HttpEntity<>(headers);
		
		// 4)Http 요청보내기.(API Server와 통신을 담당하는 기능을 제공하는 클래스)  https://adjh54.tistory.com/234#google_vignette
		RestTemplate restTemplate = new RestTemplate();
		
		// 5)Http 응답(JSON) -> Access Token 추출(Parsing)작업
		ResponseEntity<String> responseEntity = restTemplate.exchange(userinfoUri, HttpMethod.POST, userInfoKakaoRequest, String.class);
		
		String responseBody = responseEntity.getBody();
		
		log.info("응답사용자정보: " + responseBody);
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);
		
		// 인증토큰을 이용한 카카오 사용자에 대한 정보를 받아옴. PropertyKeys
		Long id = jsonNode.get("id").asLong();
		String email = jsonNode.get("kakao_account").get("email").asText();
		String nickname = jsonNode.get("properties").get("nickname").asText();
		
		return new KakaoUserInfo(id, nickname, email);
		
	}
	
	// 카카오 로그아웃  헤더는 있고, 파라미터는 없는 경우.
	// 헤더 Authorization: Bearer ${ACCESS_TOKEN}
	public void kakaologout(String accessToken) throws JsonProcessingException {
		
		// Http Header 생성.
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization","Bearer " + accessToken);
		headers.add("Content-type", "application/x-www-form-urlencoded");
		
		// Http 요청작업
		HttpEntity<MultiValueMap<String, String>> kakaoLogoutRequest = new HttpEntity<>(headers);
	
		// Http 요청하기
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(kakaologout, HttpMethod.POST, kakaoLogoutRequest, String.class);
		
		String responseBody = response.getBody();
		log.info("responseBody" + responseBody);
	
		// JSON문자열을 Java객체로 역직렬화 하거나 Java객체를 JSON으로 직렬화 할 때 사용하는 Jackson라이브러리의 클래스이다.
		// ObjectMapper 생성 비용이 비싸기때문에 bena/static 으로 처리하는 것이 성능에 좋다.
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);
		
		Long id = jsonNode.get("id").asLong();
		
		log.info("id: " + id);
		
		
	}
	
	
	
	
	
	
}
