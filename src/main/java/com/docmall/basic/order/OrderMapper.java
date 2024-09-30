package com.docmall.basic.order;

import org.apache.ibatis.annotations.Param;

public interface OrderMapper {

	// 주문정보
	void order_insert(OrderVO vo);
	
	// 장바구니테이블을 기반으로 주문상세테이블에 데이터를 저장
	void orderDetail_insert(@Param("order_num") Long order_num,@Param("user_id")String user_id);
}
