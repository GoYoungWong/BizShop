package com.docmall.basic.mail;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController     // jsp사용 안함
@RequestMapping("/email/*")
@RequiredArgsConstructor
public class EmailController {

	private final EmailService emailService;
	
	// 백엔드는 한번사용된 변수는 모두 소멸하기 때문에 인증코드가 남아있지 않아 제대로 인증이 되지 않는다.
	// 그래서 발송한 인증코드를 세션객체를 이용하여 메모리에 저장하기 위해 HttpSession session 파라미터를 넣어준다.
	
	// 스프링이 밑에 작업을 자동으로 처리해준다.
	// EmailDTO dto = new EmailDTO();
	// dto.setReceiverMail("입력한 메일주소");
	@GetMapping("/authcode")                            
	public ResponseEntity<String> authcode(String type,EmailDTO dto, HttpSession session) {
		
		
		log.info("dto: " + dto);  // dto.toString() 호출
		ResponseEntity<String> entity = null;
		
		// 인증코드작업
		// 길이가 6인 랜덤한 숫자로 구성된 인증 코드를 생성합니다.
		String authcode ="";
		for(int i=0; i <6; i++) {
			authcode += String.valueOf((int)(Math.random() * 10));
		}
		
		log.info("인증코드: " + authcode);
		
		// 사용자가 자신의 메일에서 발급받은 인증코드를 읽고, 회원가입시 인증확인란에 입력을 하게되면, 서버에서 비교목적으로 세션방식으로 인증코드를 메모리에 저장해두어애 한다.
		// 생성된 인증 코드를 세션에 저장합니다.
		session.setAttribute("authcode", authcode); // tomcat서버내장 세션기본시간 30분. 30분이 지나면 세션 메모리에 저장된 인증코드가 사라짐
		
		try {
			// 메일발송.
			
			// 메일제목 작업
			// 이메일을 발송하는데, type에 따라 이메일 제목을 설정하고, 이메일 서비스를 통해 이메일을 전송합니다.
			if(type.equals("emailjoin")) {
				dto.setSubject("BizShop 회원가입 메일인증코드입니다.");
			}else if(type.equals("emailID")) {
				dto.setSubject("BizShop 아이디 메일인증코드입니다.");
			}else if(type.equals("emailPw")) {
				dto.setSubject("BizShop 비밀번호 메일인증코드입니다.");
			}
			
			
			// 성공적으로 이메일이 발송되면 "success" 문자열과 HTTP 상태 코드 200을 응답으로 반환합니다.
			// 이메일 발송 중 예외가 발생하면 서버 오류(HTTP 상태 코드 500)를 응답으로 반환합니다.
			emailService.sendMail(type ,dto, authcode);
			entity = new ResponseEntity<String>("success", HttpStatus.OK); // 200
		}catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR); // 500
		}
		
		return entity;
	}
	
	// 메일인증코드확인
	@GetMapping("/confirm_authcode")           // 클라이언트가 제출한 인증코드   // 세션정보에 접근하기위한 객체
	public ResponseEntity<String> confirm_authcode(String authcode, HttpSession session) {
		ResponseEntity<String> entity = null;
		
		// session.setAttribute("authcode", authcode); - 세션에 저장되는 인증코드값을 authcode로 저장함
		// 세션이 유지되고 있는 동안
		// 세션에 authcode가 저장되어 있는지 확인합니다.
		if(session.getAttribute("authcode") != null) {
		
			// 클라이언트가 제출한 인증 코드가 세션에 저장된 인증 코드와 일치하는지 확인합니다.
			if(authcode.equals(session.getAttribute("authcode"))) {
				// 클라이언트가 제출한 인증 코드와 세션에 저장된 인증 코드가 일치하면 "success" 문자열과 HTTP 상태 코드 200을 응답으로 반환합니다.
				entity = new ResponseEntity<String>("success", HttpStatus.OK); // 서버측에 인증코드가 일치한다는 뜻
				session.removeAttribute("authcode");  // 서버의 메모리를 초기화. 인증을 완료하고 필요없어진 세션메모리를 지워준다.
			}else {
				entity = new ResponseEntity<String>("fail", HttpStatus.OK); // 일치하지 않다는 뜻
			}
			
		}else {  // 세션이 소멸되었을 경우
			entity = new ResponseEntity<String>("request", HttpStatus.OK); // 인증코드를 다시 요청하게 하기위해
		}
		
		return entity;
	}
}











