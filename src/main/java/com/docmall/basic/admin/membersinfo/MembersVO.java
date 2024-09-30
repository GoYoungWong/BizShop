package com.docmall.basic.admin.membersinfo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MembersVO {

	// mail_idx, mail_title, mail_content, mail_gubun, mail_send_count, reg_date
	private Integer idx;
	private String mtitle;
	private String mcontent;
	private String mgubun;
	private int    mscount;
	private Date   date;
	
}
