package com.docmall.basic.orderinquiry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.docmall.basic.admin.order.AdminOrderService;
import com.docmall.basic.admin.order.AdminOrderVO;
import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.common.dto.PageDTO;
import com.docmall.basic.common.util.FileManagerUtils;
import com.docmall.basic.order.OrderVO;
import com.docmall.basic.userpay.UserPayService;
import com.docmall.basic.userpay.UserPayVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/orderinquiry/*")
@RequiredArgsConstructor
public class OrderInquiryController {

	private final OrderInquiryService orderInquiryService;
	
	private final UserPayService userPayService;
	
	
	//상품이미지 업로드 경로
	@Value("${file.product.image.dir}")
	private String uploadPath;
	
	// 주문리스트 
	@GetMapping("/order_list")
	public void order_list(Criteria cri,
			@ModelAttribute("start_date") String start_date, 
			@ModelAttribute("end_date") String end_date
			,Model model) throws Exception {
		
		log.info("Criteria" +cri);
		cri.setAmount(2);
		
		// 주문리스트
		List<OrderVO> order_list = orderInquiryService.order_list(cri,start_date,end_date);
		
		log.info("order_list" + order_list);
		
		// 주문리스트 총새수
		int total = orderInquiryService.getTotalCount(cri,start_date,end_date);
		
		log.info("pagedto" + new PageDTO(cri, total));
		
		model.addAttribute("order_list", order_list);
		model.addAttribute("pageMaker", new PageDTO(cri, total));
	}
	
	// 주문상세정보에서 주문상품 이미지출력 1)<img src="매핑주소"> 2) <img src="test.gif">
	@GetMapping("/image_display")
	public ResponseEntity<byte[]> image_display(String dateFolderName, String fileName) throws Exception {
		
		return FileManagerUtils.getFile(uploadPath + dateFolderName, fileName);
	}
	
	// 주문상세정보
	@GetMapping("/order_detail_info")
	public ResponseEntity<Map<String, Object>> order_detail_info(Long order_num) throws Exception {
		ResponseEntity<Map<String, Object>> entity = null;
		
		Map<String, Object> map = new HashMap<>();
		
		// 주문자(수령인)정보
		OrderVO vo = orderInquiryService.order_info(order_num);
		map.put("order_basic", vo);
		
		// 주문상품정보
		List<OrderInQuiryVO> order_product_list = orderInquiryService.order_user_info(order_num);
		
		// 클라이언트에 \를 /로 변환하여, model작업전에 처리함.  2024\07\01 -> 2024/07/01
		order_product_list.forEach(ord_pro -> {
			ord_pro.setPro_up_folder(ord_pro.getPro_up_folder().replace("\\", "/"));
		});
		
		map.put("order_pro_list", order_product_list);
		
		// 결제정보
		UserPayVO u_po = userPayService.order_pay_info(order_num);
		map.put("payinfo", u_po);
		
		entity = new ResponseEntity<Map<String,Object>> (map,HttpStatus.OK);
		
		return entity;
		
	}
	
	// 입금상태 변경
	@PostMapping("/order_pay_modify")
	public ResponseEntity<String> order_pay_modify(@RequestBody UserPayVO vo) throws Exception {
		ResponseEntity<String> entity = null;
		
		userPayService.order_pay_modify(vo.getOrder_num(),"완료");
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		return entity;
	}
	
	
	
	
}
