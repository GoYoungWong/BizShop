package com.docmall.basic.review;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 CREATE TABLE review_tbl (
    REV_CODE  NUMBER,
    USER_ID   VARCHAR2(15) NOT NULL,
    PRO_NUM   NUMBER NOT NULL,
    REV_CONTENT     VARCHAR2(200)  NOT NULL,     -- 후기 내용
    REV_TITLE       VARCHAR2(200)  NOT NULL,     -- 후기 제목
    REV_RATE        NUMBER         NOT NULL,     -- 평점     
    REV_DATE        DATE DEFAULT SYSDATE         -- 후기 작성날짜

);

-- rev_code, user_id, pro_num , rev_content, rev_title, rev_rate, rev_date
 */


@Getter
@Setter
@ToString
public class ReviewVO {

	private Integer rev_code;
	private String 	user_id;
	private int     pro_num;
	private String  pro_img;
	private String  pro_up_folder;
	private String  rev_content;
	private String  rev_title;
	private int     rev_rate;
	private Date    rev_date;
}
