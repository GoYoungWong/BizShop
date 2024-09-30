package com.docmall.basic.common.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

//import com.sun.mail.util.MailSSLSocketFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j // 로그출력
@Configuration // bean등록작업  스프링이 시작되면서 생성자를 자동으로 생성되어, bean을 만들어준다.
@PropertySource("classpath:application.properties")
public class EmailConfg {

	public EmailConfg() throws Exception {
		log.info("EmailConfg.java constructor called.");
	}
	// 여기에 @Value 이거 않하고 직접 값을 쓸수 있지만 나중에 값이 변경되거나 할때 성능이 좋지 않기때문에
	// 따로 빼서 관리하고 @Value("${spring.mail.properties.mail.smtp.auth}") 이런식으로 변수(auth)에 값을 넣어준다.
	// email.properties파일의 설정정보를 참조
	
	// 사용 안함. 영향은 주지않음
	@Value("${spring.mail.transport.protocol}")
	private String protocol;  // smtp
	
	// 사용 안함. 영향은 주지않음
	@Value("${spring.mail.debug}")
	private boolean debug;
	
	// 사용 7개.
	@Value("${spring.mail.properties.mail.smtp.auth}")
	private boolean auth;
	
	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private boolean starttls;
	
	
	
	@Value("${spring.mail.host}")
	private String host;
	
	@Value("${spring.mail.port}")
	private int port;
	
	@Value("${spring.mail.username}")
	private String username;
	
	@Value("${spring.mail.password}")
	private String password;
	
	@Value("${spring.mail.default-encoding}")
	private String encoding;
	
	@Bean // 스프링이 시작되면서 javaMailSender bean생성및 스프링컨테이너에 등록.
		  // bean의 목적은 의존성 주입(DI) 목적이다.
	public JavaMailSender javaMailSender() { // JavaMailSender(인터페이스) : 스프링에서 메일발송하는 객체.
		
//		log.info("host" + host);
		
		// JavaMailSenderImpl클래스가 어떤 메일서버를 이용하여 메일발송할지 서버에 대한 정보를 구성하는 작업
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
		Properties properties = new Properties();  // https://dev-cini.tistory.com/82#google_vignette
		
		//추가
		properties.put("mail.smtp.auth", auth);
		properties.put("mail.smtp.starttls.enable", starttls);

		mailSender.setHost(host);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		mailSender.setPort(port);
		mailSender.setJavaMailProperties(properties);
		mailSender.setDefaultEncoding(encoding);
		
		log.info("메일서버: " + host);
		return mailSender;
	}
}
