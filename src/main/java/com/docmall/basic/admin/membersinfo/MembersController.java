package com.docmall.basic.admin.membersinfo;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.docmall.basic.common.constants.Constants;
import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.common.dto.PageDTO;
import com.docmall.basic.common.util.FileManagerUtils;
import com.docmall.basic.mail.EmailDTO;
import com.docmall.basic.mail.EmailService;
import com.docmall.basic.user.UserService;
import com.docmall.basic.user.UserVo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/members/*")
public class MembersController {

	private final MembersService membersService;
	
	private final UserService userService;
	
	private final PasswordEncoder passwordEncoder;
	
	private final EmailService emailService;
	
	//CKeditor 파일업로드 경로
	@Value("${file.ckdir}")
	private String uploadCKPath;
	
	// 회원리스트
	@GetMapping("/member_list")
	public void member_list(Criteria cri, Model model) {
		
		cri.setAmount(Constants.ADMIN_MEMBER_LIST_AOMUNT);
		
		List<UserVo> list = membersService.member_list(cri);
		log.info("회원목록 데이터: " + list);
		
		// 게시물 목록 20건
		model.addAttribute("member_list", list);
		
		// 전체데이터 개수
		int total = membersService.getTotalCount(cri);
		PageDTO pageDTO = new PageDTO(cri, total);
		
		log.info("페이징 기능데이터: " + pageDTO);
		model.addAttribute("pageMaker", pageDTO);
	}
	
	// 회원탕퇴
	@PostMapping("/member_delete")
	public String member_delete(Criteria cri, String user_id) {
		membersService.member_delete(user_id);
		
		return "redirect:/admin/members/member_list" + cri.getListLink();
	}
	
	// 회원등록 폼
	@GetMapping("/member_insert")
	public void member_insert() {
		
	}
	
	// 아이디중복체크
	@GetMapping("idCheck")
	public ResponseEntity<String> idCheck(String user_id) throws Exception {
		ResponseEntity<String> entity = null;
		
		String idUse = "";
		// 사용자가 입력한 아이디를 비교해서 있으면 null이 아니여서 true 사용불가
		// 사용자가 입력한 아이디를 비교해서 없으면 null이기 떄문에 false 사용가능
		if(userService.idCheck(user_id) != null) {
			idUse = "no";  // 사용불가능
		}else {
			idUse = "yes"; // 사용가능
		}
		
		entity = new ResponseEntity<String>(idUse, HttpStatus.OK);
		
		return entity;
	}
	
	// 회원가입 등록
	@PostMapping("member_join")
	public String member_joinOk(UserVo vo) throws Exception {
		
		vo.setUser_pwd(passwordEncoder.encode(vo.getUser_pwd()));
		
		log.info("회원정보: " + vo);
		
		userService.join(vo);
		
		return "redirect:/admin/members/member_list";
	}
	
	
	// 메일발송목록
	@GetMapping("/mem_mail_list")
	public void mem_mail_list(Criteria cri, String mtitle,Model model) {
		
		List<MembersVO> maillist = membersService.mem_mail_list(cri, mtitle);
		model.addAttribute("maillist", maillist);
		
		if(!maillist.isEmpty()) {
			int totalcount = membersService.getMailListCount(cri,mtitle);
			PageDTO pageDTO = new PageDTO(cri, totalcount);
			model.addAttribute("pageMaker", pageDTO);
		}
	}
	
	// 메일폼
	@GetMapping("/mem_mail_form")
	public void mem_mail_form(@ModelAttribute("vo") MembersVO vo) {
		log.info("폼 초기화: " + vo);
	}
	
	// 메일저장
	@PostMapping("mem_mail_save")
	public String mem_mail_save(@ModelAttribute("vo")MembersVO vo, Model model) {
		log.info("메일내용: " + vo);
		
		// 메일정보 DB저장
		membersService.mem_mail_save(vo);
		
		model.addAttribute("idx", vo.getIdx()); // 메일보내기 횟수작업 사용
		model.addAttribute("msg", "mem_save");
		return "/admin/members/mem_mail_form";
	}
	
	// 수신자등록
	@PostMapping("/save_recipients")
	public String save_recipients(@RequestBody List<ReceiverVO> r_vo) throws Exception {
		log.info("저장된메일: " + r_vo);
		
		// 중복을 피하기 위해 이메일 목록을 Set으로 변환
		Set<String> emailset = new HashSet<>();
		for(ReceiverVO receiver : r_vo) {
			emailset.add(receiver.getEmail());
		}
		
		// 수신자 등록을 처리하기 전에, 등록할 이메일 목록의 중복을 제거하고 기존 수신자를 삭제합니다.
		for(String email : emailset) {
			membersService.deleteRecipientByEmail(email);
		}
		// DB저장
		// 클라이언트에서 JSON 배열 형태로 수신자 이메일 정보가 서버로 전송됩니다.
		for(ReceiverVO receiver : r_vo) {
			membersService.save_recipients(receiver);
		}
		
		return "redirect:/admin/members/mem_mail_list";
	}
	
	// 메일발송 프로세서 
	@PostMapping("/mem_mail_send")
	public String mem_mail_send(@RequestBody EmailRequestVO vo, Model model) throws Exception {
		
		log.info("메일내용: " + vo);
		
		// 이메일 목록을 List<String>으로 변환
	    List<String> emailList = vo.getEmail();
		
		// 등록된 수신자메일 조회
		List<ReceiverVO> receivers = membersService.getReceiverList(emailList);
		
		// 수신자 목록이 비어 있으면 전체 회원 메일 정보를 조회
		String[] emailArr;
		if(receivers.isEmpty()) {
			emailArr = membersService.getALLMemberMail();
		}else {
			// 수신자 이메일 주소를 배열로 변환
			emailArr = new String[receivers.size()];
			for(int i = 0 ; i<receivers.size(); i++) {
				emailArr[i] = receivers.get(i).getEmail();
			}
		}
		
		// 이 객체는 발신자 이름, 발신자 이메일, 수신자 이메일(빈 문자열), 메일 제목, 메일 내용을 포함합니다.
		EmailDTO emailDTO = new EmailDTO("BizShop", "BizShop", "", vo.getMtitle(),vo.getMcontent());
//		log.info("EmailDTO 제목: " + emailDTO.getSubject());
//		log.info("EmailDTO 내용: " + emailDTO.getMessage());
		
		emailService.snedMail(emailDTO, emailArr);
		
		
		// 메일발송 횟수 업데이트
		membersService.mailSendCountUpdate(vo.getMail_idx());
		
		model.addAttribute("msg", "send");
		
		return "redirect:/admin/members/mem_mail_form";
	}
	
	
	// 메일수정폼
	@GetMapping("/mailmodifyform")
	public void mailmodifyform(int idx,Model model) throws Exception {
		
		MembersVO vo = membersService.getMailModifyinfo(idx);
		
		model.addAttribute("vo", vo);
	}
	
	// 수정
	@PostMapping("/mailedit")
	public String mailedit(@ModelAttribute("vo") MembersVO vo, Model model) throws Exception {
		
		membersService.mailedit(vo);
		
		model.addAttribute("msg", "mem_modify");
		
		return "/admin/members/mailmodifyform";
		
	}
	
	// 메일삭제
	@DeleteMapping("/mail_delete/{idx}")
	public ResponseEntity<String> member_delete(@PathVariable("idx") int idx) {
		ResponseEntity<String> entity = null;
		
		membersService.maildelete(idx);
		entity = new ResponseEntity<String>(HttpStatus.OK);
		
		return entity;
	}
	
	// 이메읿발송시 db에서 수신자정보 삭제
	@PostMapping("/delete_recipients")
	public ResponseEntity<String> delete_recipients (@RequestBody List<String> email) throws Exception{
		ResponseEntity<String> entity = null;
		
		membersService.deletedbRecipientbyEmail(email);
		entity = new ResponseEntity<String>(HttpStatus.OK);
		
		return entity;
	}
	
	
	
	// 수신자메일리스트
	@GetMapping("/email_list")
	@ResponseBody
	public String[] getEmaillist() throws Exception {
		
		// 회원테이블에서 전체회원 메일정보를 읽어오는 작업
		String[] emailList = membersService.getALLMemberMail();
		return emailList;
		
	}
	
	// 체크된 메일삭제
	@DeleteMapping("/delete_check_mail")
	public ResponseEntity<String> delete_check_mail(@RequestParam List<Long> mail_idx) throws Exception {
		ResponseEntity<String> entity = null;
		
		for(Long idx : mail_idx) {
			membersService.deltetcheckmail(idx);
		}
		entity = new ResponseEntity<>("success",HttpStatus.OK);
		
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
//						String fileUrl = fileName;
			
			
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
