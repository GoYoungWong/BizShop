package com.docmall.basic.cart;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.common.util.FileManagerUtils;
import com.docmall.basic.user.UserVo;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/cart/*")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;
	
	//상품이미지 업로드 경로
	@Value("${file.product.image.dir}")
	private String uploadPath;
	
	// 장바구니 추가
	@GetMapping("cart_add")
	public ResponseEntity<String> cart_add(CartVO vo, HttpSession session) throws Exception {
		
		log.info("장바구니: " + vo);
		
		ResponseEntity<String> entity = null;
		
		String user_id = ((UserVo) session.getAttribute("login_status")).getUser_id();
		vo.setUser_id(user_id);
		
		cartService.cart_add(vo);
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		return entity;
	}
	
	// 장바구니 목록
	// 장바구니에 들어갈때 장바구니에서 수량을 늘리면 가격이 그만큼 곱해지는데 CartVO로는 처리가 안되서 클래스를 하나 추가했다.
	@GetMapping("cart_list") // 장바구니 목록을 보여주기위해 사용자아이디를 가지고옴
	public void cart_list(HttpSession session, Model model) {
		
		String user_id = ((UserVo) session.getAttribute("login_status")).getUser_id();
		
		List<CartProductVO> cart_list = cartService.cart_list(user_id);
		cart_list.forEach(vo -> vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/")));
		
		model.addAttribute("cart_list", cart_list);
	}
	
	// 이미지출력
	@GetMapping("/image_display")
	public ResponseEntity<byte[]> image_display(String dateFolderName, String fileName) throws Exception {
		
	return FileManagerUtils.getFile(uploadPath + dateFolderName, fileName);
	}
	
	// 장바구니 삭제
	@GetMapping("/cart_delete")
	public String cart_delete(Integer cart_code) throws Exception {
		
		cartService.cart_delete(cart_code);
		
		return "redirect:/cart/cart_list";
	}
	
	// 장바구니 수량변경
	@GetMapping("/cart_change")
	public String cart_change(Integer cart_code, int cart_amount) throws Exception {
		
		
		cartService.cart_change(cart_code, cart_amount);
		
		log.info("장바구니코드: " + cart_code);
		log.info("장바구니수량: " + cart_amount);
		
		return "redirect:/cart/cart_list";
	}

	// 장바구니 비우기
	@GetMapping("/cart_empty")
	public String cart_empty(HttpSession session ) throws Exception{
		
	        // 세션에서 로그인된 사용자 정보에서 user_id를 가져옵니다.
	        String user_id = ((UserVo) session.getAttribute("login_status")).getUser_id();
        
            cartService.cart_empty(user_id);
            
            return "redirect:/cart/cart_list";
	}

	
	
}
