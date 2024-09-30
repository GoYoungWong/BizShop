package com.docmall.basic.admin.StaticAnalysis;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docmall.basic.user.UserVo;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/admin/staticanalysis/*")
@RequiredArgsConstructor
public class StaticAnalysisController {

	private final StaticAnalysisService staticAnalysisService;
	
	// 처음 통계로 들어갔을때 실행되면서 검색창에 현재날짜를 표시해준다.
	@GetMapping("/orderstats")
	public void orderstats(Model model) {
		
		LocalDate now = LocalDate.now();
		int year = now.getYear();
		int month = now.getMonthValue();
		
		model.addAttribute("year", year);  // 현재년도
		model.addAttribute("month", month); // 현재 달
	}
	
	// 1차카테고리 월별 매출현황
	@GetMapping("/Monthlysalesstatus")
	@ResponseBody
	public List<Map<String, Object>> Monthlysalesstatus(int year, int month) {
		String ordDate = String.format("%s/%s", year,(month < 10 ? "0" + String.valueOf(month) : month));
		log.info("선택일: " + ordDate);
		
		List<Map<String, Object>> listObjectmap = staticAnalysisService.Monthlysalesstatus(ordDate);
		return listObjectmap;
	}
	
	// 처음 통계로 들어갔을때 실행되면서 검색창에 현재날짜를 표시해준다.
	@GetMapping("/memberstats")
	public void memberstats(Model model) {
		
		LocalDate now = LocalDate.now();
		int year = now.getYear();
		int month = now.getMonthValue();
		
		model.addAttribute("year", year);  // 현재년도
		model.addAttribute("month", month); // 현재 달
	}
	
	// 방문 횟수 조회
	@GetMapping("/visitChart")
	@ResponseBody
	public List<Map<String, Object>> visitCount(@RequestParam int year, @RequestParam int month) {
		String UserDate = String.format("%s/%s", year,(month < 10 ? "0" + String.valueOf(month) : month));
		List<Map<String, Object>> visitCounts = staticAnalysisService.getVisitCount(UserDate);
		
		return visitCounts;
	}
	
	
	
	
}
