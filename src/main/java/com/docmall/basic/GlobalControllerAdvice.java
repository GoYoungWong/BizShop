package com.docmall.basic;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.docmall.basic.admincategory.AdminCategoryService;
import com.docmall.basic.admincategory.AdminCategoryVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@ControllerAdvice(basePackages = {"com.docmall.basic.product","com.docmall.basic.cart"})
public class GlobalControllerAdvice {
	
	private final AdminCategoryService categoryService;
	
	@ModelAttribute
	public void comm_category(Model model) {
		log.info("공통코드");
		List<AdminCategoryVO> cate_list = categoryService.getFirstCategoryList();
		model.addAttribute("user_cate_list", cate_list);
	}

}
