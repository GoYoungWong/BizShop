package com.docmall.basic.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SNSUserDto {

	// id, name, email, sns_type
	private String id;
	private String name;
	private String email;
	private String sns_type;
	
}
