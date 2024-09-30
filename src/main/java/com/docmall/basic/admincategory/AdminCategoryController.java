package com.docmall.basic.admincategory;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/admin/category/*")
@RequiredArgsConstructor
public class AdminCategoryController {

	private final AdminCategoryService adminCategoryService;
	
	@GetMapping("/secondcategory/{cat_prtcode}")
	public ResponseEntity<List<AdminCategoryVO>> getSecondCategoryList(@PathVariable("cat_prtcode") int cat_prtcode) throws Exception {
		
		log.info("1차카테고리코드: " + cat_prtcode);
		
		ResponseEntity<List<AdminCategoryVO>> entity = null;
		
		entity = new ResponseEntity<List<AdminCategoryVO>>(adminCategoryService.getSecondCategoryList(cat_prtcode), HttpStatus.OK);
		
		return entity;
	}
}
