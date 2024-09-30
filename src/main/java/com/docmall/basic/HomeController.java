package com.docmall.basic;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.docmall.basic.admin.StaticAnalysis.StaticAnalysisService;
import com.docmall.basic.admincategory.AdminCategoryService;
import com.docmall.basic.admincategory.AdminCategoryVO;
import com.docmall.basic.user.SNSUserDto;
import com.docmall.basic.user.UserVo;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
	
	private final AdminCategoryService categoryService;
	private final StaticAnalysisService staticAnalysisService;
	
//	@ResponseBody  // 이 어노테이션이 사용이 안되면, return "index" 는 데이터가 아니라, 타임리프 파일명으로 인식.
	@GetMapping("/")
	public String index(HttpSession session,Model model) {
		log.info("기본 주소.");
		
		List<AdminCategoryVO> cate_list = categoryService.getFirstCategoryList();
		model.addAttribute("user_cate_list", cate_list);
		
		String sessionid = session.getId();
		String userid = null;
		
		if(session.getAttribute("login_status") != null) {
			// 로그인한 사용자
			userid = ((UserVo) session.getAttribute("login_status")).getUser_id();
			// 로그인 시, 회원으로 방문기록 업데이트
			staticAnalysisService.updateVisitorLogToMember(userid, sessionid);
		}else {
			// 비회원 방문기록 추가
			if(session.getAttribute("Visit") == null) {
				staticAnalysisService.insertVisttorLog("비회원", sessionid, "N");
				session.setAttribute("Visit",true); // 방문기록 추가
			}
		}
		
		return "index";
		
	}
}
