package com.docmall.basic.adminproduct;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.docmall.basic.common.dto.Criteria;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminProductService {

	private final AdminProductMapper adminProductMapper;
	
	// 상품등록
	public void pro_insert(AdminProductVO vo) {
		adminProductMapper.pro_insert(vo);
	}
	
	// 상품리스트
	public List<AdminProductVO> pro_list(Criteria cri) {
		return adminProductMapper.pro_list(cri);
	}
	
	
	// 총데이터 개수
	public int getTotalCount(Criteria cri) {
		return adminProductMapper.getTotalCount(cri);
	}
	
	// 상품수정
	public AdminProductVO pro_modify(Integer pro_num) {
		return adminProductMapper.pro_modify(pro_num);
	}
	
	// 상품수정(업데이트)
	public void pro_modify_ok(AdminProductVO vo) {
		adminProductMapper.pro_modify_ok(vo);
	}
	
	// 상품삭제
	public void pro_delete(Integer pro_num) {
		adminProductMapper.pro_delete(pro_num);
	}
	
	// 체크상품 수정
	public void pro_check_box(List<Integer> pro_num_arr, List<Integer> pro_price_arr, List<String> pro_buy_arr) {
		
		List<AdminProductDTO> pro_modify_list = new ArrayList<>();
		
		for(int i=0; i<pro_num_arr.size(); i++) {
			AdminProductDTO adminProductDTO = new AdminProductDTO(pro_num_arr.get(i),pro_price_arr.get(i), pro_buy_arr.get(i));
			pro_modify_list.add(adminProductDTO);
		}
		adminProductMapper.pro_check_box(pro_modify_list);
		
	}
}
