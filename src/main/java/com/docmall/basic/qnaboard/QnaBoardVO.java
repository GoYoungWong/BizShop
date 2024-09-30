package com.docmall.basic.qnaboard;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QnaBoardVO {

	/*
	테이블명: qnaboard_tbl
	컬럼: qna_idx, pro_num, user_id,que_title ,Que_Content,ans_title ,ans_content, anscheck, pro_up_folder, pro_img, question_date, answer_date
	인덱스:PK_QNA_IDX
	시퀸스:SEQ_QNABOARD_CODE
	*/
	private Long qna_idx;
	private int pro_num;
	private String user_id;
	private String que_title;
	private String que_content;
	private String ans_title;
	private String ans_content;
	private String anscheck;
	private String pro_up_folder;
	private String pro_img;
	private Date   question_date;
	private Date   answer_date;
}
