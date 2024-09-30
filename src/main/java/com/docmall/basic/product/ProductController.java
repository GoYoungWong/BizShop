package com.docmall.basic.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.docmall.basic.adminproduct.AdminProductVO;
import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.common.dto.PageDTO;
import com.docmall.basic.common.util.FileManagerUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/product/*")
public class ProductController {

	private final ProductService productService;
	
	//상품이미지 업로드 경로
	@Value("${file.product.image.dir}")
	private String uploadPath;
	
	//CKeditor 파일업로드 경로
	@Value("${file.ckdir}")
	private String uploadCKPath;
	
	// 상품리스트
	@GetMapping("/pro_list")
	public void pro_list(@ModelAttribute("cat_code") int cat_code, @ModelAttribute("cat_name") String cat_name, Criteria cri, Model model) throws Exception {
		
	cri.setAmount(6);
		
	List<AdminProductVO> pro_list = productService.pro_list(cat_code, cri);
	// 클라이언트에 \를 /로 변환하여, model작업전에 처리함.  2024\07\01 -> 2024/07/01
	pro_list.forEach(vo -> {
		vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));   
	});
	
	model.addAttribute("pro_list", pro_list);
	
	// 상품이 없을때 페이징을 보이지 않게 하기 위해
	if(!pro_list.isEmpty()) {
		int totalCount = productService.getCountProductByCategory(cat_code);
		PageDTO pageDTO = new PageDTO(cri, totalCount);
		model.addAttribute("pageMaker", pageDTO);
	}
	
	
	}
	
	// 이미지출력
	@GetMapping("/image_display")
	public ResponseEntity<byte[]> image_display(String dateFolderName, String fileName) throws Exception {
		
		return FileManagerUtils.getFile(uploadPath + dateFolderName, fileName);
	}
	
	// 상품정보
	@GetMapping("pro_info_2")
	public void pro_info_2(int pro_num, Model model) throws Exception {
		
		AdminProductVO vo = productService.pro_info(pro_num);
		vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
		
		model.addAttribute("product", vo);
	}
	
	// 상품상세정보
	@GetMapping("/pro_detail")
	public void pro_detail(int pro_num,Model model) throws Exception {
		
		AdminProductVO vo = productService.pro_info(pro_num);
		vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
		
		model.addAttribute("product", vo);

		
	}
	
	
	
}
