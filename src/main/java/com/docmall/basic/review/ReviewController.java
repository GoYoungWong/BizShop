package com.docmall.basic.review;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.common.dto.PageDTO;
import com.docmall.basic.common.util.FileManagerUtils;
import com.docmall.basic.user.UserVo;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/review/*")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;
	
	//상품이미지 업로드 경로
	@Value("${file.product.image.user}")
	private String uploadPath;
	
	// 리뷰리스트
	@GetMapping("/revlist/{pro_num}/{page}")
	public ResponseEntity<Map<String, Object>> revlist(@PathVariable("pro_num") int pro_num,@PathVariable("page") int page) {
		ResponseEntity<Map<String, Object>> entity = null;
		Map<String, Object> map = new HashMap<>();
		
		// Criteria 객체를 생성합니다. 이 객체는 페이지 번호와 페이지당 항목 수를 관리합니다.
		Criteria cri = new Criteria();
		cri.setAmount(2);
		cri.setPageNum(page);
		
		// 1) 후기목록
		List<ReviewVO> revlist = reviewService.rev_list(pro_num, cri);
		revlist.forEach(vo -> {
			vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
		});
		
		
		// 2) 페이징정보
		int revcount = reviewService.getCountReviewBypro_num(pro_num);
		PageDTO pageDTO = new PageDTO(cri, revcount);
		
		map.put("revlist", revlist);
		map.put("pageMaker", pageDTO);
		
		entity = new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
		
		return entity;
	}
	
	// 상품후기 저장
	@PostMapping(value = "/review_save", produces = {MediaType.TEXT_PLAIN_VALUE}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<String> review_save(@ModelAttribute ReviewVO vo,@RequestParam("uploadFile2") MultipartFile uploadFile ,HttpSession session) throws Exception {
		ResponseEntity<String> entity = null;
		
		String user_id = ((UserVo) session.getAttribute("login_status")).getUser_id();
		vo.setUser_id(user_id);
		
		// 1)상품이미지 업로드
		String dateFolder = FileManagerUtils.getDateFolder();
		String saveFilName = FileManagerUtils.uploadFile(uploadPath, dateFolder, uploadFile);
	
		vo.setPro_img(saveFilName);
		vo.setPro_up_folder(dateFolder);
		
		log.info("상품후기데이터: " + vo);
		reviewService.review_save(vo);
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		
		return entity;
	}

	// 상품후기 삭제
	@DeleteMapping("/review_delete/{rev_code}")
	public ResponseEntity<String> review_delete(@PathVariable("rev_code") Integer rev_code) throws Exception{
		ResponseEntity<String> entity = null;
		
		log.info("상품리뷰코드: " + rev_code);
		
		reviewService.review_delete(rev_code);
		entity = new ResponseEntity<String>("sueccss", HttpStatus.OK);
		
		
		return entity;
	}
	
	// 상품후기 수정폼(json으로 넘어오기 때문에 폼작업은 아님)
	@GetMapping("/rev_modify/{rev_code}")
	public ResponseEntity<ReviewVO> rev_modify(@PathVariable("rev_code") Integer rev_cod) throws Exception {
		ResponseEntity<ReviewVO> entity = null;
		
		entity = new ResponseEntity<ReviewVO>(reviewService.rev_modify(rev_cod), HttpStatus.OK);
		
		return entity;
	}
	
	// 상품후기 수정저장
	@PutMapping("/review_modify")
	public ResponseEntity<String> review_modify(@RequestBody ReviewVO vo) throws Exception {
		ResponseEntity<String> entity = null;
		
		log.info("리뷰저장데이터: " + vo);
		
		reviewService.save_reivew(vo);
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		return entity;
		
	}
	
	// 리뷰리스트에서 사용할 이미지보여주기
	@GetMapping("/image_display")
	public ResponseEntity<byte[]> image_display(String dateFolderName, String fileName) throws Exception {
		
		return FileManagerUtils.getFile(uploadPath + dateFolderName, fileName);
	}
	
	
}
