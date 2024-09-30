package com.docmall.basic.adminoption;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminProductOptionVO {

	private Long productOptionId;
    private Long proNum;
    private String optionName;
    private Long optionId;
    private String optionValue;
}
