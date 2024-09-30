package com.docmall.basic.admin.order;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.order.OrderVO;

public interface AdminOrderMapper {
	
	// 주문관리 리스트
	List<OrderVO> order_list(@Param("cri") Criteria cri,@Param("start_date") String start_date,@Param("end_date") String end_date );
	
	// 리스트 총새수
	int getTotalCount(@Param("cri") Criteria cri,@Param("start_date") String start_date,@Param("end_date") String end_date );
	
	// 주문자(수령인)정보
	OrderVO order_info(Long order_num);
	
	// 주문상품정보
	List<AdminOrderVO> order_admin_info(Long order_num);
	
	// 주문상품 개별삭제
	void order_individual_delete(@Param("order_num") Long order_num, @Param("pro_num") int pro_num);

	// 기본정보(수령인)수정
	void order_base_modify(OrderVO vo);
	
	// 주문테이블 주문금액 총금액변경.
	void order_tot_price_change(Long order_num);
	
	// 입금대기 리스트
	List<OrderVO> getPendingOrders(@Param("cri") Criteria cri,@Param("start_date") String start_date,@Param("end_date") String end_date,@Param("pay_status") String pay_status );
		
	// 입금대기리스트 총새수
	int getPendingOrdersCount(@Param("cri") Criteria cri,@Param("start_date") String start_date,@Param("end_date") String end_date,@Param("pay_status") String pay_status );
}
