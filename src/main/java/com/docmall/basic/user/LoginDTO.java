package com.docmall.basic.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginDTO {

	private String user_id;
	private String user_pwd;
	private int    user_visitcount;
}
