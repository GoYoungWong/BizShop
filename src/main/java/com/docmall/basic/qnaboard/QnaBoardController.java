package com.docmall.basic.qnaboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/qnaboard/*")
@RequiredArgsConstructor
public class QnaBoardController {

	private final QnaBoardService qnaBoardService;
	
	//상품이미지 업로드 경로
	@Value("${file.product.image.user}")
	private String uploadPath;
	
	// Qna리스트
	@GetMapping("/qnalist/{pro_num}/{page}")
	public ResponseEntity<Map<String, Object>> qnalist(@PathVariable("pro_num")int pro_num, @PathVariable("page") int page) {
		ResponseEntity<Map<String, Object>> entity = null;
		
		Map<String, Object> map = new HashMap<>();
		Criteria cri = new Criteria();
		cri.setAmount(2);
		cri.setPageNum(page);
		
		// QnA목록
		List<QnaBoardVO> qnalist = qnaBoardService.qnalist(pro_num, cri);
		qnalist.forEach(vo -> {
			vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
		});
		
		// 페이징정보
		int qnacount = qnaBoardService.getCountQnaBypro_num(pro_num);
		PageDTO pageDTO = new PageDTO(cri, qnacount);
		
		map.put("qnalist", qnalist);
		map.put("pageqna", pageDTO);
		
		entity = new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		
		return entity;
	}
	
	// 상품문의 저장
	@PostMapping(value = "/qna_save", produces = {MediaType.TEXT_PLAIN_VALUE}) 
	public ResponseEntity<String> qna_save(@RequestParam("pro_num") int pro_num,
	        @RequestParam("que_title") String que_title,
	        @RequestParam("que_content") String que_content,
	        @RequestParam("uploadFile") MultipartFile uploadFile,
	        HttpSession session) throws Exception{
		ResponseEntity<String> entity = null;
		
		String user_id = ((UserVo) session.getAttribute("login_status")).getUser_id();
		
		QnaBoardVO vo = new QnaBoardVO();
		vo.setPro_num(pro_num);
	    vo.setQue_title(que_title);
	    vo.setQue_content(que_content);
	    vo.setUser_id(user_id);
		
		// 1)상품이미지 업로드
		String dateFolder = FileManagerUtils.getDateFolder();
		String saveFilName = FileManagerUtils.uploadFile(uploadPath, dateFolder, uploadFile);
	
		vo.setPro_img(saveFilName);
		vo.setPro_up_folder(dateFolder);
		
		qnaBoardService.qna_save(vo);
		
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		return entity;
	}
	
	// Qna리스트에서 사용할 이미지보여주기
	@GetMapping("/image_display")
	public ResponseEntity<byte[]> image_display(String dateFolderName, String fileName) throws Exception {
		
		return FileManagerUtils.getFile(uploadPath + dateFolderName, fileName);
	}
	
	
	
	
}
