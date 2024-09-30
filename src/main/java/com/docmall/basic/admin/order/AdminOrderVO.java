package com.docmall.basic.admin.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminOrderVO {

	private Long order_num;   // 주문상세코드
	private Integer pro_num;  // 상품코드
	private int    item_amount; // 상품별 개수
	private int    item_price;  // 상품별 가격
	private String order_name;  // 주문자 이름
	private String pro_name;    // 상품이름
	private String pro_up_folder;  // 날짜폴더
	private String pro_img;        // 이미지
}
