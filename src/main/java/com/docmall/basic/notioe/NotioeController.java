package com.docmall.basic.notioe;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.docmall.basic.admin.notioe.NoticeVO;
import com.docmall.basic.common.constants.Constants;
import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.common.dto.PageDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/usernotioe/*")
@RequiredArgsConstructor
@Slf4j
public class NotioeController {
	
	private final NotioeService notioeService;
	
	// 공지사항 리스트
	@GetMapping("/notice_list")
	public void notice_list(Criteria cri, Model model) {
			
		// 공지사항 출력개수
		cri.setAmount(Constants.ADMIN_NOTICE_LIST_AOMUNT);
			
		List<NoticeVO> Noticelist = notioeService.NoticeList(cri);
		log.info("공지사항 목록: " + Noticelist);
			
		model.addAttribute("noticelist", Noticelist);
			
		// 전체데이터 개수
			
		int notitotal = notioeService.getNoticeCount(cri);
		PageDTO pageDTO = new PageDTO(cri, notitotal);
		log.info("페이징 기능데이터: " + pageDTO);
		model.addAttribute("pageMaker", pageDTO);
			
		}

}
