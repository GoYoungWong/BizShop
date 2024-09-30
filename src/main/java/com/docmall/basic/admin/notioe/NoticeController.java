package com.docmall.basic.admin.notioe;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.docmall.basic.admin.AdminVO;
import com.docmall.basic.common.constants.Constants;
import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.common.dto.PageDTO;
import com.docmall.basic.common.util.FileManagerUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/notice")
public class NoticeController {
	
	private final NoticeService noticeService;
	
	//CKeditor 파일업로드 경로
	@Value("${file.ckdir}")
	private String uploadCKPath;
	
	// 공지사항 리스트
	@GetMapping("/notice_list")
	public void notice_list(Criteria cri, Model model) {
		
		// 공지사항 출력개수
		cri.setAmount(Constants.ADMIN_NOTICE_LIST_AOMUNT);
		
		List<NoticeVO> Noticelist = noticeService.NoticeList(cri);
		log.info("공지사항 목록: " + Noticelist);
		
		model.addAttribute("noticelist", Noticelist);
		
		// 전체데이터 개수
		
		int notitotal = noticeService.getNoticeCount(cri);
		PageDTO pageDTO = new PageDTO(cri, notitotal);
		log.info("페이징 기능데이터: " + pageDTO);
		model.addAttribute("pageMaker", pageDTO);
		
	}
	
	// 작성폼
	@GetMapping("/notice_form")
	public void notice_form(@ModelAttribute("vo")NoticeVO vo) {
		log.info("폼 초기화:" + vo);
	}
	
	// 작성글 저장
	@PostMapping("/notice_save")
	public ResponseEntity<String> notice_save(@RequestBody NoticeVO vo) throws Exception{
		ResponseEntity<String> entity = null;
		
		noticeService.insert_notice(vo);
		log.info("저장내용: " + vo);
		entity = new ResponseEntity<String>("success",HttpStatus.OK);
		
		return entity;
	}
	
//	// 작성글 수정폼
//	@GetMapping("/notice_modify")
//	public void notice_modify(@RequestParam("idx")Integer idx,Model model) throws Exception {
//		 log.info("수정할 idx: " + idx); // 로그로 idx 값 출력
//		NoticeVO vo = noticeService.getNoticeinfo(idx);
//		log.info("수정idx: " + vo);
//		model.addAttribute("notice", vo);
//	}
//	
//	// 작성글 수정저장
//	@PostMapping("/notice_modify")
//	public String notice_modify(NoticeVO vo,Criteria cri) throws Exception {
//		
//		noticeService.update_notice(vo);
//		
//		return "redirect:/admin/notice/notice_list" + cri.getListLink();
//		
//	}
	
	// 공지사랑 삭제
	@PostMapping("/notice_delete")
	public ResponseEntity<String> member_delete(Criteria cri, @RequestParam("idx")Integer idx) throws Exception {
		ResponseEntity<String> entity = null;
		
		noticeService.notice_delete(idx);
		
		entity = new ResponseEntity<String>("success",HttpStatus.OK);
		return entity;
	}
	
	

	// CKEditor 상품설명 이미지 업로드
	// MultipartFile upload : CKeditor의 업로드탭에서 나온 파일첨부 <input type="file" name="upload"> 을 참조함.
	// HttpServletRequest request : 클라이언트의 요청정보를 가지고 있는 객체.
	// HttpServletResponse response : 서버에서 클라이언트에게 보낼 정보를 응답하는 객체
	@PostMapping("/imageupload")
	public void imageupload(HttpServletRequest request, HttpServletResponse response, MultipartFile upload) {
		
		OutputStream out = null;
		PrintWriter printWriter = null; // 서버에서 클라이언트에게 응답정보를 보낼때 사용.(업로드한 이미지정보를 브라우저에게 보내는 작업용도)
		
		try {
			//1)CKeditor를 이용한 파일업로드 처리.
			String fileName = upload.getOriginalFilename(); // 업로드 할 클라이언트 파일이름
			byte[] bytes = upload.getBytes(); // 업로드 할 파일의 바이트배열
			
			String ckUploadPath = uploadCKPath + fileName; // "C:\\Dev\\upload\\ckeditor\\" + "abc.gif"
			
			out = new FileOutputStream(ckUploadPath); // "C:\\Dev\\upload\\ckeditor\\abc.gif" 파일생성. 0 byte
			
			out.write(bytes); // 빨대(스트림)의 공간에 업로드할 파일의 바이트배열을 채운상태.
			out.flush(); // "C:\\Dev\\upload\\ckeditor\\abc.gif" 0 byte -> 크기가 채워진 정상적인 파일로 인식.
			
			//2)업로드한 이미지파일에 대한 정보를 클라이언트에게 보내는 작업
			
			printWriter = response.getWriter();
			
			// 한글파일 인코딩 설정문제 발생.  Constants.ROOR_URL: 이메일에 이미지 안깨지게 하는 설정
			String fileUrl = Constants.ROOR_URL + "/admin/product/display/" + fileName; // 매핑주소/이미지파일명
//							String fileUrl = fileName;
			
			
			// Ckeditor 4.12에서는 파일정보를 다음과 같이 구성하여 보내야 한다.
			// {"filename" : "abc.gif", "uploaded":1, "url":"/ckupload/abc.gif"}
			// {"filename" : 변수, "uploaded":1, "url":변수}
			printWriter.println("{\"filename\" :\"" + fileName + "\", \"uploaded\":1,\"url\":\"" + fileUrl + "\"}"); // 스트림에 채움.
			printWriter.flush();
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(out != null) {
				try {
					out.close();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			if(printWriter != null) printWriter.close();
		}
	}
	
	@GetMapping("/display/{fileName}")
	public ResponseEntity<byte[]> getFile(@PathVariable("fileName") String fileName) {
		
		log.info("파일이미지: " + fileName);
		
		
		ResponseEntity<byte[]> entity = null;
		
		try {
			entity = FileManagerUtils.getFile(uploadCKPath, fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return entity;
		
	}
	
}
