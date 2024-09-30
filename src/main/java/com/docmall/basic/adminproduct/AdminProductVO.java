package com.docmall.basic.adminproduct;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminProductVO {

	// pro_num, cat_code, pro_name, pro_price, pro_publisher, pro_content, pro_up_folder, pro_img, pro_amount, pro_buy,pro_size, pro_color, pro_material, pro_date, pro_updatedate
	
	private Integer pro_num;
	private Integer cat_code;
	private String  pro_name;
	private int     pro_price;
	private String  pro_publisher;
	private String  pro_content;
	private String  pro_up_folder;
	private String  pro_img;
	private int     pro_amount;
	private String  pro_buy;
	private String  pro_size;
	private String  pro_color;
	private String  pro_material;
	private Date    pro_date;
	private Date    pro_updatedate;
}
