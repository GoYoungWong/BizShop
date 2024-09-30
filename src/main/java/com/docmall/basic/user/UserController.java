package com.docmall.basic.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.docmall.basic.admin.StaticAnalysis.StaticAnalysisService;
import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.common.dto.PageDTO;
import com.docmall.basic.common.util.FileManagerUtils;
import com.docmall.basic.kakaoLogin.KakaoUserInfo;
import com.docmall.basic.mail.EmailDTO;
import com.docmall.basic.mail.EmailService;
import com.docmall.basic.qnaboard.QnaBoardVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user/*")
@Controller
public class UserController {

	private final UserService userService;
	
	private final PasswordEncoder passwordEncoder;
	
	private final EmailService emailService;
	
	private final StaticAnalysisService staticAnalysisService;
	
	//상품이미지 업로드 경로
	@Value("${file.product.image.user}")
	private String uploadPath;
	
	
	@GetMapping("join")
	public void joinForm() {
		log.info("join");
	}
	
	
	// 회원가입 저장
	@PostMapping("join")
	public String joinOk(UserVo vo) throws Exception {
		
		// 비밀번호 암호화호 변경
		vo.setUser_pwd(passwordEncoder.encode(vo.getUser_pwd()));
		
		log.info("회원정보: " + vo);
		 
		userService.join(vo);
		
		return "redirect:/user/login";
	}
	
		// 아이디중복체크
		@GetMapping("/idCheck")
		public ResponseEntity<String> idCheck(String user_id) throws Exception{
			log.info("아이디: " + user_id);
			
			ResponseEntity<String> entity = null;
			
			String idUse = "";
			// 사용자가 입력한 아이디를 비교해서 있으면 null이 아니여서 true 사용불가
			// 사용자가 입력한 아이디를 비교해서 없으면 null이기 떄문에 false 사용가능
			if(userService.idCheck(user_id) != null) { 
				idUse = "no"; // 사용불가능
			}else {
				idUse = "yes"; // 사용가능
			}
			
			// HttpStatus.OK: 상태코드 200번 
			entity = new ResponseEntity<String>(idUse, HttpStatus.OK);
			
			return entity;
			
		}
		
	// 로그인 폼 화면
	@GetMapping("/login")
	public void loginForm() {
		
	}
	
	// 로그인체크
	@PostMapping("/login")  // 파라미터 1)LoginDTO dto  2)String user_id, String user_pwd
	public String loginOk(LoginDTO dto, HttpSession session, RedirectAttributes rttr) throws Exception {
		
		
		UserVo vo = userService.login(dto.getUser_id());
		
		String msg = "";  
		String url = "/"; 
		
		if(vo != null) { // 사용자가 입력한 아이디에 해당하는 사용자 정보가 존재하는지 확인합니다.
			// 비밀번호 비교            
			if(passwordEncoder.matches(dto.getUser_pwd(), vo.getUser_pwd())) { // 사용자가 입력한 비밀번호(user_password)가 데이터베이스에 저장된 암호화된 비밀번호(vo.getUser_password)와 일치하는지 확인합니다.
				vo.setUser_pwd(""); // 보안적으로 사용
				session.setAttribute("login_status", vo); // 비밀번호가 일치하면 사용자 정보를 세션에 login_status라는 이름으로 저장합니다.
			
			
			}else { // 비밀번호가 존재하지 않을경우  
				msg = "failPW"; // 비밀번호가 일치하지 않으면 실패 메시지를 설정합니다.
				url = "/user/login";  // 로그인 폼 주소로 리디렉션할 URL을 설정합니다.
			}
		}else {          // 아이디가 존재하지 않을 경우
			msg = "failID"; // 아이디가 존재하지 않음을 나타내는 실패 메시지를 설정합니다.
			url = "/user/login";  // 로그인 폼 주소
		}
		
		
		
		rttr.addFlashAttribute("msg",msg); 
		
		return "redirect:" + url;  // 로그인 폼 주소로 리디렉션할 URL을 설정합니다.
		
		
		
	}
	
	// 로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		
		session.invalidate(); // 세션형태로 관리되는 모든 메모리는 소멸.
		
		return "redirect:/";
	}
		
	// 아이디찾기 화면
	@GetMapping("/idfind")
	public void idfindForm() {
	
	}
	
	// 아이디 찾기
	@PostMapping("/idfind")
	
	public String idfindOk(String user_name, String user_email,String authcode, HttpSession session, RedirectAttributes rttr) throws Exception {
		
		String url = ""; // 리다이렉트할 URL을 초기화합니다.
		String msg = ""; // 메시지를 초기화합니다.
		
		// 인증코드 확인
		// 사용자가 입력한 인증 코드(authcode)가 세션에 저장된 인증 코드와 일치하는지 확인합니다.
		if(authcode.equals(session.getAttribute("authcode"))) {

			// 이름(u_name)과 이메일(u_email)을 사용하여 데이터베이스에서 아이디(u_id)를 찾습니다.
			String user_id = userService.idfind(user_name, user_email);
			if(user_id != null) { // 아이디가 존재하는지 확인합니다.
				
				// 아이디를 내용으로 메일발송작업(이메일 제목을 설정합니다)
				String subject = "BizShop 아이디를 보냅니다.";
				
				
				// 이메일 발송에 필요한 정보를 담은 EmailDTO 객체를 생성합니다.
				EmailDTO dto = new EmailDTO("BizShop","BizShop", user_email, subject, user_id);
				
				// 이메일을 발송합니다.
				emailService.sendMail("emailIDResuit", dto, user_id);
				
				// 인증코드값 메모리에서 삭제
				session.removeAttribute("authcode");
				
				msg = "success"; // 성공 메시지를 설정합니다.
				url = "/user/login"; // 리다이렉트할 URL을 로그인 페이지로 설정합니다.
				rttr.addFlashAttribute("msg", msg); // 리다이렉트 후에도 유지되는 플래시 속성에 메시지를 추가합니다.
				
			}else {
				msg = "failID"; // 아이디를 찾지 못한 경우 실패 메시지를 설정합니다.
				url = "/user/idfind"; // 리다이렉트할 URL을 아이디 찾기 페이지로 설정합니다.
			}
			
			
		}else {  // 인증코드가 틀렸을때
			msg = "failAuthCode"; // 인증 코드가 틀린 경우 실패 메시지를 설정합니다.
			url = "/user/idfind";// 리다이렉트할 URL을 아이디 찾기 페이지로 설정합니다.
		}

		rttr.addFlashAttribute("msg", msg); // 리다이렉트 후에도 유지되는 플래시 속성에 메시지를 추가합니다.
		
		return "redirect:" + url; // 설정된 URL로 리다이렉트합니다.
	
		
	}
	
	// 비밀번호 재설정(비밀번호 찾기) 화면
	@GetMapping("/pwfind")
	public void pwfindForm() {
		
	}
	
	// 비밀번호 재설정(비밀번호 찾기) 
	@PostMapping("/pwfind")
	
	public String pwfindOk(String user_id, String user_name, String user_email,String authcode, HttpSession session, RedirectAttributes rttr) throws Exception {
		
		String url = ""; // 리다이렉트할 URL을 초기화합니다.
		String msg = ""; // 메세지를 초기화 합니다.
		
		// 인증코드 확인
		if(authcode.equals(session.getAttribute("authcode"))) { 
			
			// 사용자가 입력한 아이디(u_id), 이름(u_name), 이메일(u_email)을 조건으로 데이터베이스에서 이메일(d_email)을 가져옵니다.
			String d_email = userService.pwfind(user_id, user_name, user_email);
			if(d_email != null) { // 이메일이 존재하는지 확인합니다.
				
				// 임시 비밀번호 생성
				// 임시비밀번호 10자리 생성
				String tempPw = userService.getTempPw();
				
				// 암호화된 비밀번호(임시 비밀번호를 암호화합니다.)
				String temp_enc_pw = passwordEncoder.encode(tempPw);
				
				// 암호화된 임시 비밀번호를 데이터베이스에 업데이트합니다.
				userService.tempPwUpdate(user_id, temp_enc_pw);
				
				// 이메일 발송에 필요한 정보를 담은 EmailDTO 객체를 생성합니다.
				EmailDTO dto = new EmailDTO("BizShop", "BizShop", d_email, "BizShop에서 임시비밀번호를 보냅니다.", tempPw);
				
				// 임시 비밀번호를 이메일로 발송합니다.
				emailService.sendMail("emailPwResuit", dto, tempPw);  // "타임리프파일명"
				
				// 인증 코드를 세션에서 삭제합니다.
				session.removeAttribute("authcode"); 
				
				msg = "success";
				url = "/user/pwfind"; // 리다이렉트할 URL을 로그인 페이지로 설정합니다.
			
			}else {
				url = "/user/pwfind"; // 이메일이 존재하지 않을 경우 리다이렉트할 URL을 비밀번호 찾기 페이지로 설정합니다.
				msg = "failInput"; // 실패 메시지를 설정합니다.
			}
			
		}else {
			url = "/user/pwfind"; // 인증 코드가 일치하지 않을 경우 리다이렉트할 URL을 비밀번호 찾기 페이지로 설정합니다.
			msg = "failAuth"; // 인증 실패 메시지를 설정합니다.
		}
		rttr.addFlashAttribute("msg", msg); // 리다이렉트 후에도 유지되는 플래시 속성에 메시지를 추가합니다.
		
		return "redirect:" + url; // 설정된 URL로 리다이렉트합니다.
		
	}
	
	// 마이페이지 홈
	@GetMapping("/mypages")
	public void mypage(HttpSession session, Model model) throws Exception {
		
		// 세션에서 login_status라는 이름으로 저장된 사용자 정보를 가져옵니다. 
		// 이 정보는 UserInfoVO 객체로 캐스팅된 후, 사용자 아이디(u_id)를 추출합니다.
		if(session.getAttribute("login_status") != null) {
			String user_id = ((UserVo)session.getAttribute("login_status")).getUser_id();
			// user_id를 이용해 데이터베이스에서 사용자 정보를 가져옵니다.
			// userService.login(user_id) 메서드는 user_id에 해당하는 사용자 정보를 반환합니다.
			UserVo vo = userService.login(user_id);
			// 사용자 정보를 userpage라는 이름으로 모델에 추가합니다. 이렇게 추가된 정보는 타임리프 페이지에서 사용할 수 있습니다.
			model.addAttribute("userpage", vo);
		}else if(session.getAttribute("kakao_status") != null) {
			KakaoUserInfo kakaoUserInfo = (KakaoUserInfo) session.getAttribute("kakao_status");
		
			// MyPage에서 보여줄 정보를 선택적으로 작업.
			UserVo vo = new UserVo();
			vo.setUser_name(kakaoUserInfo.getNickname());
			vo.setUser_email(kakaoUserInfo.getEmail());

			model.addAttribute("userpage", vo);
			model.addAttribute("msg", "kakao_login");

		}
		
	}
	
	// 내정보 수정
	@PostMapping("/modify")
	public String modify(UserVo vo, HttpSession session, RedirectAttributes rttr) {
		
		if(session.getAttribute("login_status") == null) return "redirect:/user/login";
		
		String user_id = ((UserVo)session.getAttribute("login_status")).getUser_id();
		
		log.info("수정데이터: " + vo);
		
		userService.modify(vo);
		
		rttr.addFlashAttribute("msg", "success");
		
		return "redirect:/user/mypages";
	}
	
	
	// 회원탈퇴화면
	@GetMapping("delete")
	public void delete() {
		
	}
	
	// 회원탈퇴
	@PostMapping("delete")
	public String delete(String user_pwd, HttpSession session, RedirectAttributes rttr) {
		
		String user_id = ((UserVo)session.getAttribute("login_status")).getUser_id();
		
		UserVo vo = userService.login(user_id);
		
		String msg = "";
		String url = "";
		
		if(vo != null) {
			
			if(passwordEncoder.matches(user_pwd, vo.getUser_pwd())) {
				userService.delete(user_id);
				
				session.invalidate();
				msg = "success";
				url = "/";
			}else {
				msg = "failPW";
				url = "/user/delete";
			}
		}
		
		rttr.addFlashAttribute("msg", msg);
		
		return "redirect:" + url;
		
	}
	
	// 비밀번호 변경 화면
	@GetMapping("/changepw")
	public void changepwForm() {
		
	}
	
	// 비밀번호 변경
	@PostMapping("/changepw")
	public String changepw(String now_user_pwd, String new_user_pwd, HttpSession session, RedirectAttributes rttr) {
		
		String user_id = ((UserVo)session.getAttribute("login_status")).getUser_id(); // login_status이름으로 저장된 세션정보를 UserVo로 형변환 후 user_id에 저장
		
		UserVo vo = userService.login(user_id); // 사용자 아이디정보를 vo 이름으로 저장
		
		String msg = ""; // 메세지 초기화
		
		if(vo != null) { // 아이디가 존재하는 경우
			// 비밀번호 비교
			if(passwordEncoder.matches(now_user_pwd, vo.getUser_pwd())); // 사용자가 입력한 비밀번호가 암호화된 형태에 해당하는 것이라면.
			
			// 신규비밀번호 변경작업
			String enc_new_user_pwd = passwordEncoder.encode(new_user_pwd);
			userService.changePw(user_id, enc_new_user_pwd);
			msg = "success";
			
			// 세션무효화
			session.invalidate();
			
			rttr.addFlashAttribute("msg", msg);
            return "redirect:/"; // 홈으로 리다이렉트
		
		}else { // // 사용자가 입력한 비밀번호가 암호화된 형태에 해당하지 않는 것이라면.
			msg = "failPW";
		}
		
		
		rttr.addFlashAttribute("msg", msg);
		
		return "redirect:/user/changepw";
	}
	
	// 내 Qna보기
	@GetMapping("/myqna")
	public void myqna(HttpSession session,Criteria cri, Model model) throws Exception {
		
		// 세션에서 사용자 아이디를 가지고옴
		String user_id = ((UserVo)session.getAttribute("login_status")).getUser_id();
		
		// 사용자아이디로 QnA리스트와 총개수를 가지고옴
		List<QnaBoardVO> userqnalist = userService.userqnalist(user_id, cri);
		userqnalist.forEach(vo -> {
			vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
		});
		
		int userqnacount = userService.getCountQnaByUserId(user_id);
		PageDTO pageDTO = new PageDTO(cri, userqnacount);
		
		model.addAttribute("userqnalist", userqnalist);
		model.addAttribute("pageMaker", pageDTO);
		
		
	}
	
	// 답변보기 폼
	@GetMapping("/myqna_form/{qna_idx}")
	public ResponseEntity<QnaBoardVO> qna_modify(@PathVariable("qna_idx") Long qna_idx) throws Exception {
		ResponseEntity<QnaBoardVO> entity = null;
		
		QnaBoardVO qnaBoard = userService.myqna_form(qna_idx);
		
		// 파일 경로에 있는 역슬래시(\)를 슬래시(/)로 변경
		if(qnaBoard.getPro_up_folder() != null) {
			qnaBoard.setPro_up_folder(qnaBoard.getPro_up_folder().replace("\\", "/"));
		}
	
		 // 이미지 파일 이름에 대한 경로도 변경
		if(qnaBoard.getPro_img() != null) {
			qnaBoard.setPro_img(qnaBoard.getPro_img().replace("\\", "/"));
		}
		
		entity = new ResponseEntity<QnaBoardVO>(qnaBoard, HttpStatus.OK);
		
		return entity;
	}
	
	// QnA삭제 
	@DeleteMapping("/myqna_delete/{qna_idx}")
	public ResponseEntity<String> myqna_delete(@PathVariable("qna_idx") Long qna_idx) {
		ResponseEntity<String> entity = null;
		
		userService.myqna_delete(qna_idx);
		
		entity = new ResponseEntity<String>("sueccess",HttpStatus.OK);
		
		return entity;
	}
	
	
	// Qna리스트에서 사용할 이미지보여주기
	@GetMapping("/image_display")
	public ResponseEntity<byte[]> image_display(String dateFolderName, String fileName) throws Exception {
		
		// 로그 출력
	    log.info("File Path: " + fileName);
		
		return FileManagerUtils.getFile(uploadPath + dateFolderName, fileName);
	}
	
	
		
	

}
