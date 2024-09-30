package com.docmall.basic.order;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.docmall.basic.cart.CartProductVO;
import com.docmall.basic.cart.CartService;
import com.docmall.basic.cart.CartVO;
import com.docmall.basic.user.UserService;
import com.docmall.basic.user.UserVo;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/order/*")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	
	private final CartService cartService;
	
	private final UserService userService;
	
	// 주문리스트
	@GetMapping("/productorder")
	public String productorder(@RequestParam(value = "type", defaultValue = "direct") String type, CartVO vo, Model model, HttpSession session) throws Exception {
		
		String user_id = ((UserVo) session.getAttribute("login_status")).getUser_id();
		vo.setUser_id(user_id);
		
		// 직접구매인경우 장바구니에 항목을 추가
		if(!type.equals("cartorder")) {
			// 1) 장바구니 저장
			cartService.cart_add(vo);
		}
		
		// 2) 주문하기
		List<CartProductVO> cart_list = cartService.cart_list(user_id);
		
		cart_list.forEach(d_vo -> {
			d_vo.setPro_up_folder(d_vo.getPro_up_folder().replace("\\", "/"));
		}
	);
		
	// 총상품가격 계산
	int pro_total = 0;
	for(int i=0; i< cart_list.size(); i++) {
		pro_total += (cart_list.get(i).getPro_price() * cart_list.get(i).getCart_amount());
	}
		
	
		model.addAttribute("cart_list", cart_list);
		model.addAttribute("pro_total", pro_total);
		return "/order/productorder";
	
	}
	
	// 주문자와 동일
	@GetMapping("/orderagree")
	public ResponseEntity<UserVo> orderagree(HttpSession session) throws Exception {
		ResponseEntity<UserVo> entity = null;
		
		String user_id = ((UserVo) session.getAttribute("login_status")).getUser_id();
		
		entity = new ResponseEntity<UserVo>(userService.login(user_id), HttpStatus.OK);
		
		return entity;
	}
	
	// 무통장 입금
	@PostMapping("/ordernobankok")			// 은행명					// 예금주
	public String orderok(OrderVO vo, String pay_nobank, String pay_nobank_user,HttpSession session) throws Exception {
		
		log.info("주문정보: " + vo);
		log.info("입금은행: " + pay_nobank);
		log.info("예금주: " + pay_nobank_user);
		
		String user_id = ((UserVo) session.getAttribute("login_status")).getUser_id();
		vo.setUser_id(user_id);
		
		String payinfo = pay_nobank + "/" + pay_nobank_user;
		orderService.order_process(vo, user_id,"무통장입금","미납", payinfo);
		
		
		return "redirect:/order/ordercomplete";
		
	}
	
	// 주문완료
	@GetMapping("/ordercomplete")
	public void ordercomplete() throws Exception {
		
	}
}
