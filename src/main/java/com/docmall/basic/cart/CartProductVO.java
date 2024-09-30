package com.docmall.basic.cart;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartProductVO {
	
	private Long   cart_code;        // 카트코드
	private int    pro_num;          // 상품코드
	private String pro_name;         // 상품이름
	private String pro_up_folder;    // 날짜폴더
	private String pro_img;          // 상품이미지
	private int    pro_price;        // 상품가격
	private int    cart_amount;       // 상품수량
}

// cart_code,pro_num,pro_name,pro_up_folder,pro_img,pro_price,pro_amount