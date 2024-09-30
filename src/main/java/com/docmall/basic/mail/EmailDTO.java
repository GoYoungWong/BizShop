package com.docmall.basic.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // 매개변수가 있는 생성자를 만들어줌
public class EmailDTO {

	private String senderName; // 발신자
	private String senderMail; // 발신자 메일주소
	private String receiverMail;  // 수신자 메일주소 일부러 따로 받기위해 밑에 생성자에서 뺌
	private String subject; // 제목
	private String message; //  내용
	
	public EmailDTO() {
		this.senderMail = "BizShop";
		this.senderName = "BizShop";
		this.subject = "BizShop 회원가입 메일인증코드입니다.";
		this.message = "메일 인증코드를 확인하시고, 회원가입시 인증코드 입련란에 입력바랍니다.";
	}

	@Override
	public String toString() {
		return "EmailDTO [senderName=" + senderName + ", senderMail=" + senderMail + ", receiverMail=" + receiverMail
				+ ", subject=" + subject + ", message=" + message + "]";
	}
	
	
}
