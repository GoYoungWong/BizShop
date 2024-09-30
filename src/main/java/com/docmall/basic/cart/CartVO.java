package com.docmall.basic.cart;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartVO {

	// cart_num, pro_num, user_id, cart_amount, cart_date
	private Integer cart_code;
	private int     pro_num;
	private String  user_id;
	private int     cart_amount;
	private Date    cart_date;
	
}
