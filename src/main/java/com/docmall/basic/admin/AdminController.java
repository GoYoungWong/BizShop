package com.docmall.basic.admin;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin/*")
@Slf4j
@RequiredArgsConstructor
public class AdminController {
	
	private final AdminService adminService;
	
	private final PasswordEncoder passwordEncoder;
	
	
	// 관리자 로그인
	@GetMapping("admin_login")
	public void admin_loginForm() {
		
		
	}
	
	@PostMapping("admin_login")
	public String admin_loginOk(AdminLoginDTO ad, HttpSession session, RedirectAttributes rttr)  throws Exception {
		
		AdminVO d_vo = adminService.adminlogin(ad.getAdmin_id());
		log.info("d_vo: " + d_vo);  // 반환 값 확인
		
		
		String msg = "";
		String url = "/admin/admin_menu";
		
		
		if(d_vo != null) {
			
			// 비밀번호체크
			if(passwordEncoder.matches(ad.getAdmin_pw(), d_vo.getAdmin_pw())) {
				d_vo.setAdmin_pw(""); // 보안용
				session.setAttribute("admin_status", d_vo); // 비밀번호가 일치하면 관리자 정보를 세션에 admin_status라는 이름으로 저장합니다.
				msg="success";
			}else {
				msg = "failPW";
				url = "/admin/admin_login";
			}
		}else {
			msg = "failID";
			url = "/admin/admin_login";
		}
		
		rttr.addFlashAttribute("msg", msg); // thymeleaf에서 msg변수를 사용목적
		log.info("Redirect message: " + msg);  // 메시지 확인
		
		return "redirect:" + url;
	
	 	}
	
	@GetMapping("/admin_menu")
	public void admin_meun() {
		
	}
	
	// 로그아웃
	@GetMapping("/admin_logout")
	public String admin_logout(HttpSession session) {
		
		// 이거로 하면 세션정보가 전부 지워짐
//		session.invalidate();
		
		// admin에 있는 로그인 세션만 삭제
		session.removeAttribute("admin_status");
		
		return "redirect:/admin/admin_login";
	}
	
	
	














}	

