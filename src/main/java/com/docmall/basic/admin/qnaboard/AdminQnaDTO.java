package com.docmall.basic.admin.qnaboard;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class AdminQnaDTO {
	
	private Long qna_idx;
	private String ans_title;
	private String ans_content;
	private String anscheck;

}
