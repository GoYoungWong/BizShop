package com.docmall.basic.orderinquiry;

import java.util.List;

import org.springframework.stereotype.Service;

import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.order.OrderVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderInquiryService {

	private final OrderInquiryMapper orderInquiryMapper;
	
	// 주문관리 리스트
	public List<OrderVO> order_list(Criteria cri,String start_date,String end_date ) {
		return orderInquiryMapper.order_list(cri, start_date, end_date);
	}
			
	// 리스트 총새수
	public int getTotalCount(Criteria cri,String start_date,String end_date ) {
		return orderInquiryMapper.getTotalCount(cri, start_date, end_date);
	}
	
	// 주문자(수령인)정보
	public OrderVO order_info(Long order_num) {
		return orderInquiryMapper.order_info(order_num);
	}
		
	// 주문상품정보
	public List<OrderInQuiryVO> order_user_info(Long order_num) {
		return orderInquiryMapper.order_user_info(order_num);
	}
	
}
