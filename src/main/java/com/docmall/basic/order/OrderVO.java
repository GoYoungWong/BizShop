package com.docmall.basic.order;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderVO {

	// order_num, user_id, order_name, order_zipcode, order_addr, order_deaddr, order_pho, order_price, order_req,order_admin_memo ,order_redate
	
	private Long   order_num;        // 주문코드
	private String user_id;          // 사용자아이디
	private String order_name;       // 주문자이름
	private String order_zipcode;    // 우편번호
	private String order_addr;       // 기본주소
	private String order_deaddr;     // 상세주소
	private String order_pho;        // 연락처
	private int    order_price;      // 총결제금액
	private String order_req;        // 요청사항
	private String order_admin_memo;	 // 관리자 메모저장용
	private Date   order_redate;     // 주문날짜
	private String order_status;     // 배송현황 기본값 = 배송준비중
}
