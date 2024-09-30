package com.docmall.basic.admin;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// admin_id, admin_pw, admin_name, admin_email, admin_phone, admin_updatedate

@Getter
@Setter
@ToString
public class AdminVO {

	private String admin_id;
	private String admin_pw;
	private String admin_name;
	private String admin_email;
	private String admin_phone;
	private Date   admin_updatedate;
	
}
