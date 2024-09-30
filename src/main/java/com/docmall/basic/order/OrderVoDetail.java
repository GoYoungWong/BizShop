package com.docmall.basic.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderVoDetail {
	
	// order_det_num, order_num, item_amount, item_price
	private Long order_num;  // 주문상세번호
	private int  pro_num;      // 상품번호
	private int  item_amount;    // 
	private int  item_price;
}
