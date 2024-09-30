package com.docmall.basic.admincategory;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// cat_code, cat_prtcode, cat_name

@Getter
@Setter
@ToString
public class AdminCategoryVO {
	
	private Integer cat_code;
	private int     cat_prtcode;
	private String  cat_name;
}
