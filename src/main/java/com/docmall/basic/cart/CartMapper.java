package com.docmall.basic.cart;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface CartMapper {
	
	// 장바구니 추가
	void cart_add(CartVO vo);
	
	// 장바구니 목록
	List<CartProductVO> cart_list(String user_id);
	
	// 장바구니 삭제
	void cart_delete(Integer cart_code);
	
	// 장바구니 수량변경
	void cart_change(@Param("cart_code") Integer cart_code, @Param("cart_amount") int cart_amount);

	// 장바구니 비우기
	void cart_empty(String user_id);
}
