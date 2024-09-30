package com.docmall.basic.userpay;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserPayService {

	private final UserPayMapper userPayMapper;
	
	// 결제정보 보기
	public UserPayVO order_pay_info(Long order_num) {
		return userPayMapper.order_pay_info(order_num);
	}
	
	// 입금현황변경
	public void order_pay_modify(Long order_num,String pay_status) {
		userPayMapper.order_pay_modify(order_num, pay_status);
	}
}
