package com.docmall.basic.cart;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {

	private final CartMapper cartMapper;
	
	// 장바구니 추가
	public void cart_add(CartVO vo) {
		cartMapper.cart_add(vo);
	}
	
	// 장바구니 목록
	public List<CartProductVO> cart_list(String user_id) {
		return cartMapper.cart_list(user_id);
	}
	
	// 장바구니 삭제
	public void cart_delete(Integer cart_code) {
		cartMapper.cart_delete(cart_code);
	}
	
	// 장바구니 수량변경
	public void cart_change(Integer cart_code,int cart_amount) {
		cartMapper.cart_change(cart_code, cart_amount);	
}
	// 장바구니 비우기
	public void cart_empty(String user_id) {
		cartMapper.cart_empty(user_id);
		}
	
}
