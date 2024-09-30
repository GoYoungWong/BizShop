package com.docmall.basic.admin.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.common.dto.PageDTO;
import com.docmall.basic.common.util.FileManagerUtils;
import com.docmall.basic.review.ReviewVO;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/admin/review/*")
@RequiredArgsConstructor
public class AdminReviewController {
	
	private final AdminReviewService adminReviewService;
	
	//상품이미지 업로드 경로
	@Value("${file.product.image.user}")
	private String uploadPath;

	// 리뷰리스트
	@GetMapping("/revlist")
	public void revlist(Criteria cri, Model model) {
		
		// 1) 후기목록
		List<ReviewVO> revlist = adminReviewService.adminrevlist(cri);
		revlist.forEach(vo -> {
			vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
		});
		
		// 2) 페이징정보
		int revcount = adminReviewService.getAdminRevCount(cri);
		PageDTO pageDTO = new PageDTO(cri, revcount);
		
		model.addAttribute("revlist", revlist);
		model.addAttribute("pageMaker", pageDTO);
	}
	
	// review리스트에서 사용할 이미지보여주기
	@GetMapping("/image_display")
	public ResponseEntity<byte[]> image_display(String dateFolderName, String fileName) throws Exception {
		
		// 로그 출력
	    log.info("File Path: " + fileName);
		
		return FileManagerUtils.getFile(uploadPath + dateFolderName, fileName);
	}
	
	
	
	
	
}
