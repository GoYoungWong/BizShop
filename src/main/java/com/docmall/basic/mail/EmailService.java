package com.docmall.basic.mail;


import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.docmall.basic.common.constants.Constants;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	// EmilConfig.java파일의 JavaMailSender()메서드가 스프링시스템에서 실행되어, 리턴되는 타입의 객체
	// 즉 bean을 생성 및 등록작업을 하고, 아래 객체에 주입을 해준다.
	private final JavaMailSender mailSender;
	
	// 뷰 템플릿 타임리프 템플릿을 메일템플릿으로 사용하기위하여, 아래 필드가 선언됨
	private final SpringTemplateEngine templateEngine; // 매개변수 생성자
	
	
	public void sendMail(String type,EmailDTO dto, String authcode) {
		
		 // mail/파일이름
		 type = Constants.MAILFOLDERNAME + "/" + type;
		
		// 메일구성정보 담당(받는사람, 보내는사람, 받는사람 메일주소, 본문내용)
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		
		try {

			// 메일템플릿으로 타임리프 사용목적으로 아래코드가 구성됨.
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
			mimeMessageHelper.setTo(dto.getReceiverMail()); // 메일수신자
			mimeMessageHelper.setFrom(new InternetAddress(dto.getSenderMail(), dto.getSenderName()));
			mimeMessageHelper.setSubject(dto.getSubject()); // apdlfwpahr
			mimeMessageHelper.setText(setContext(authcode, type), true); // 메일 본문 내용, true: HTML 내용을 사용함
														// email.html파일을 가리킴
			// 메일발송기능
			mailSender.send(mimeMessage);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 관리자 회원목록에서 이메일 단체메일을 위해 하나 더 만든 메서드
	public void snedMail(EmailDTO dto, String[] emailArr) {
		// 메일구성정보 담당(받는사람, 보내는사람, 받는사람 메일주소, 본문내용)
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		
		try {

			// 메일템플릿으로 타임리프 사용목적으로 아래코드가 구성됨.
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
			mimeMessageHelper.setTo(emailArr); // 메일수신자
			mimeMessageHelper.setFrom(new InternetAddress(dto.getSenderMail(), dto.getSenderName()));
			mimeMessageHelper.setSubject(dto.getSubject()); // 메일제목
			mimeMessageHelper.setText(dto.getMessage(), true); // 메세지 내용, true: HTML 내용을 사용함
														
			// 메일발송기능 
			mailSender.send(mimeMessage);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
		
	// thymeleaf를 통한 html 적용
	// String authcode : 인증코드.  String type : email.html 확장자만 빠지고 들어옴
	public String setContext(String authcode,String type) {
		Context context = new Context();
		context.setVariable("authcode", authcode);
		return templateEngine.process(type, context);
	
	}

 }

