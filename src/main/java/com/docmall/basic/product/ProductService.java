package com.docmall.basic.product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.docmall.basic.adminproduct.AdminProductVO;
import com.docmall.basic.common.dto.Criteria;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

	private final ProductMapper productMapper;
	
	// 상품리스트
	public List<AdminProductVO> pro_list(int cat_code,Criteria cri ) {
		return productMapper.pro_list(cat_code, cri);
	}

	// 상품 총 개수
	public int getCountProductByCategory(int cat_code) {
		return productMapper.getCountProductByCategory(cat_code);
	}
	
	// 상품팝업
	public AdminProductVO pro_info(int pro_num) {
		return productMapper.pro_info(pro_num);
	}
}
