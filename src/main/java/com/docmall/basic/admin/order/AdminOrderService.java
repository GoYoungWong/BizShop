package com.docmall.basic.admin.order;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.order.OrderVO;
import com.docmall.basic.userpay.UserPayMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminOrderService {

	private final AdminOrderMapper adminOrderMapper;
	
	private final UserPayMapper userPayMapper;
	
	// 주문관리 리스트
	public List<OrderVO> order_list(Criteria cri, String start_date, String end_date) {
		return adminOrderMapper.order_list(cri,start_date,end_date);
	}
		
	// 리스트 총새수
	public int getTotalCount(Criteria cri, String start_date, String end_date) {
		return adminOrderMapper.getTotalCount(cri,start_date,end_date);
	}
	
	// 주문자(수령인)정보
	public OrderVO order_info(Long order_num) {
		return adminOrderMapper.order_info(order_num);
	}
	
	// 주문상품정보
	public List<AdminOrderVO> order_admin_info(Long order_num) {
		return adminOrderMapper.order_admin_info(order_num);
	}
	
	// 주문상품 개별삭제
	@Transactional
	public void order_individual_delete(Long order_num,int pro_num) {
		
		// 주문상품 개별삭제
		adminOrderMapper.order_individual_delete(order_num, pro_num);
	
		// 주문테이블 주문금액 변경.
		adminOrderMapper.order_tot_price_change(order_num);
		
		// 결제테이블 주문금액 변경.
		userPayMapper.pay_tot_price_change(order_num);
	
	}
	
	// 기본정보(수령인)수정
	public void order_base_modify(OrderVO vo) {
		adminOrderMapper.order_base_modify(vo);	
		}
	
}
