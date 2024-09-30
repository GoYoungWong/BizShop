package com.docmall.basic.adminproduct;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.docmall.basic.admincategory.AdminCategoryService;
import com.docmall.basic.admincategory.AdminCategoryVO;
import com.docmall.basic.common.constants.Constants;
import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.common.dto.PageDTO;
import com.docmall.basic.common.util.FileManagerUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/admin/product/*")
@RequiredArgsConstructor
public class AdminProductController {

	private final AdminProductService adminProductService;
	
	private final AdminCategoryService adminCategoryService;
	
	// 상품이미지 업로드 경로
	@Value("${file.product.image.dir}")
	private String uploadPath;
	
	// Ckeditor 파일업로드 경로
	@Value("${file.ckdir}")
	private String uploadCkPath;
	
	
	// 상품등록 폼
	@GetMapping("pro_insert")
	public void pro_insert(Model model) {
		
		List<AdminCategoryVO> cate_list = adminCategoryService.getFirstCategoryList();
		model.addAttribute("cate_list", cate_list);
	
	}
	
	// 상품등록
	@PostMapping("pro_insert")
	public String pro_insertOk(AdminProductVO vo, MultipartFile uploadFile) throws Exception {
		
		// 1)상품이미지 업로드
		String dateFolder = FileManagerUtils.getDateFolder();
		String saveFilName = FileManagerUtils.uploadFile(uploadPath, dateFolder, uploadFile);
	
		vo.setPro_img(saveFilName);
		vo.setPro_up_folder(dateFolder);
		
		log.info("상품정보: " + vo);
		
		adminProductService.pro_insert(vo);
	
		return "redirect:/admin/product/pro_list";
	
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
				
				String ckUploadPath = uploadCkPath + fileName; // "C:\\Dev\\portfolio\\ckeditor\\" + "abc.gif"
				
				out = new FileOutputStream(ckUploadPath); // "C:\\Dev\\portfolio\\ckeditor\\abc.gif" 파일생성. 0 byte
				
				out.write(bytes); // 빨대(스트림)의 공간에 업로드할 파일의 바이트배열을 채운상태.
				out.flush(); // "C:\\Dev\\portfolio\\ckeditor\\abc.gif" 0 byte -> 크기가 채워진 정상적인 파일로 인식.
				
				//2)업로드한 이미지파일에 대한 정보를 클라이언트에게 보내는 작업
				
				printWriter = response.getWriter();
				
				// 한글파일 인코딩 설정문제 발생.
				String fileUrl = "/admin/product/display/" + fileName; // 매핑주소/이미지파일명
//					String fileUrl = fileName;
				
				
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
				entity = FileManagerUtils.getFile(uploadCkPath, fileName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return entity;
			
		}
	
	// 상품리스트
	@GetMapping("pro_list")
	public void pro_list(Criteria cri, Model model) throws Exception {
		
		cri.setAmount(Constants.ADMIN_PRODUCT_LIST_AOMUNT);
		
		List<AdminProductVO> pro_list = adminProductService.pro_list(cri);
		pro_list.forEach(vo -> {
			vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
		});
		
		int totalCount = adminProductService.getTotalCount(cri);
		
		model.addAttribute("pro_list", pro_list);
		model.addAttribute("pageMaker", new PageDTO(cri, totalCount));
		
		
	}
	
	// 상품리스트에서 사용할 이미지보여주기
	@GetMapping("/image_display")
	public ResponseEntity<byte[]> image_display(String dateFolderName, String fileName) throws Exception {
		
		return FileManagerUtils.getFile(uploadPath + dateFolderName, fileName);
	}
	
	// 상품수정폼
	@GetMapping("/pro_modify")
	public void pro_modify(Integer pro_num, Model model) throws Exception {
	
		// 1차카테고리 목록(편집 페이지에서 현재 편집 중인 상품이 어떤 1차 카테고리에 속하는지 표시해주기 위함) 
		List<AdminCategoryVO> cate_list = adminCategoryService.getFirstCategoryList();
		model.addAttribute("cate_list", cate_list);
		
		// 상품정보(2차카테고리)
		AdminProductVO vo = adminProductService.pro_modify(pro_num);
		// 클라이언트에 \를 /로 변환하여, model작업전에 처리함 2024\07\01 -> 2024/07/01
		vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
		model.addAttribute("Ad_productVO", vo);
		
		// 현재 상품의 2차 카테고리 코드를 통해 1차 카테고리 코드 조회
		int cat_code = vo.getCat_code(); // 상품테이블에 존재하는 2차카테고리
		int cat_prtcode = adminCategoryService.getFristCategoryBySecondCategory(cat_code).getCat_prtcode();
		model.addAttribute("cat_prtcode", cat_prtcode);
		
		// 선택된 1차 카테고리에 대한 2차 카테고리 목록 조회 및 모델에 추가
		model.addAttribute("sub_cat_list", adminCategoryService.getSecondCategoryList(cat_prtcode));
	}
	
	@PostMapping("/pro_modify")
	public String pro_modify(AdminProductVO vo, MultipartFile uploadFile, Criteria cri, RedirectAttributes rttr) throws Exception {
		
		
		
		// 상품이미지 변경 유무
		if(!uploadFile.isEmpty()) { // !uploadFile.isEmpty(): true면 변경된 이미지가 있다는 의미
			
			// 기존상품 이미지삭제. 날자폴더명, 파일명
			FileManagerUtils.delete(uploadPath, vo.getPro_up_folder(), vo.getPro_img(), "image");
		
			// 변경이미지 업로드
			String dateFolder = FileManagerUtils.getDateFolder();
			String saveFilName = FileManagerUtils.uploadFile(uploadPath, dateFolder, uploadFile);
		
			// 새로운 이미지파일명, 날짜폴더명
			vo.setPro_img(saveFilName);
			vo.setPro_up_folder(dateFolder);
		
		}
		adminProductService.pro_modify_ok(vo);
		
		return "redirect:/admin/product/pro_list" + cri.getListLink();
	}
	
	// 상품삭제
	@PostMapping("/pro_delete")
	public String pro_delete(Criteria cri, Integer pro_num) throws Exception{
		
		adminProductService.pro_delete(pro_num);
		
		return "redirect:/admin/product/pro_list" +cri.getListLink();
	}
	
	// 체크상품수정
	@PostMapping("/pro_check_box")
	public ResponseEntity<String> pro_check_box (
			@RequestParam("pro_num_arr") List<Integer> pro_num_arr,
			@RequestParam("pro_price_arr") List<Integer> pro_price_arr,
			@RequestParam("pro_buy_arr") List<String> pro_buy_arr) throws Exception {
		
		log.info("상품코드: " + pro_num_arr);
		log.info("상품가격: " + pro_price_arr);
		log.info("상품판매: " + pro_buy_arr);
		
		adminProductService.pro_check_box(pro_num_arr, pro_price_arr, pro_buy_arr);
		
		ResponseEntity<String> entity = null;
		
		entity = new ResponseEntity<>("success", HttpStatus.OK);
		
		return entity;
		
		
	}
	
	
	
	
	
	
	
}
