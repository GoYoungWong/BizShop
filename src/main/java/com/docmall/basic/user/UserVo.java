package com.docmall.basic.user;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 성능관련
   - 회원테이블(로그인 포함)
   - 회원테이블 + 로그인테이블
 총 컬럼수 : 11
user_id, user_pwd, user_name, user_email, user_zipcode, user_addr, user_deaddr, user_phone,user_point ,user_datesub, user_update
*/

@Getter
@Setter
@ToString
public class UserVo {

	private String user_id;
	private String user_pwd;
	private String user_name;
	private String user_email;
	private String user_zipcode;
	private String user_addr;
	private String user_deaddr;
	private String user_phone;
	private int    user_point;
	private Date   user_datesub;
	private Date   user_update;
}
