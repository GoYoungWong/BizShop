package com.docmall.basic.userpay;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class UserPayVO {

	// pay_id, order_num, user_id, paymethod, payinfo, pay_price, pay_status, pay_date
	private Integer pay_id;
	private Long    order_num;
	private String  user_id;
	private String 	paymethod;
	private String  payinfo;
	private int     pay_price;
	private String  pay_status;
	private Date    pay_date;
}
