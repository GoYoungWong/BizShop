package com.docmall.basic.admin.membersinfo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EmailRequestVO {

	private int mail_idx;
    private List<String> email;  // 이메일 목록
    private String mtitle;        // 메일 제목
    private String mcontent;      // 메일 내용

}
