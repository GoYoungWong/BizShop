package com.docmall.basic.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.docmall.basic.cart.CartMapper;
import com.docmall.basic.userpay.UserPayMapper;
import com.docmall.basic.userpay.UserPayVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

	
	private final OrderMapper orderMapper;
	
	private final CartMapper cartMapper;
	
	private final UserPayMapper userPayMapper;
	
	// @Transactional: 하나의 단위로 여러개를 할려면 이거 넣으면됨 (원래는 데이터베이스기능임)
	@Transactional
	public void order_process(OrderVO vo, String user_id, String paymethod, String pay_status, String payinfo) {
		
		// 1) 주문테이블(insert)
		vo.setUser_id(user_id);
		orderMapper.order_insert(vo);
		
		// 2) 주문상세테이블(insert)
		orderMapper.orderDetail_insert(vo.getOrder_num(), user_id);
		
		// 3)결제테이블(insert)
		UserPayVO up_vo = UserPayVO.builder()
				.order_num(vo.getOrder_num())
				.pay_price(vo.getOrder_price())
				.user_id(user_id)
				.paymethod(paymethod)
				.payinfo(payinfo)
				.pay_status(pay_status)
				.build();
		
		userPayMapper.userpay_insert(up_vo);
		
		// 4) 장바구니테이블 비우기
		cartMapper.cart_empty(user_id);
	}
	
	
}
