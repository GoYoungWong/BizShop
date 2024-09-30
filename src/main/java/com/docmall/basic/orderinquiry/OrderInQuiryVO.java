package com.docmall.basic.orderinquiry;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderInQuiryVO {

	private Long order_num;   // 주문상세코드
	private Integer pro_num;  // 상품코드
	private int    item_amount; // 상품별 개수
	private int    item_price;  // 상품별 가격
	private String order_name;  // 주문자 이름
	private String pro_name;    // 상품이름
	private String order_req;   // 배송요청사항
	private String pro_up_folder;  // 날짜폴더
	private String pro_img;        // 이미지
}
