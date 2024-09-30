package com.docmall.basic.admin.notioe;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class NoticeVO {

	// no_idx, no_title, no_content, no_readcount,no_class ,no_regdate
	private Integer idx;
	private String title;
	private String content;
	private int    readcount;
	private String noticlass;
	private Date   regdate;
	

}
