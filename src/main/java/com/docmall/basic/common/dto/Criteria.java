package com.docmall.basic.common.dto;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Criteria {
	
	private int pageNum;   // 1  2  3  4  5 선택된 페이지번호
	private int amount;    // 페이지마다 출력할 게시물 개수
	
	// 검색용도
	private String type;   // 선택한 검색종류   1.제목(T)  2.내용(C)  3.작성자(W)  4.제목 OR 내용(TC)  5.제목 OR 작성자(TW)  6.제목 OR 작성자 OR 내용(TWC)
	private String keyword; // 검색어
	
	//생성자
	public Criteria() {
		this(1, 10); // 1 : 첫번째 페이지  10: 페이지에 나오는 게시물 개수
	}
	
	// 매개변수가 있는 생성자
	public Criteria(int pageNum, int amount) {
		this.pageNum = pageNum ;
		this.amount = amount;
		System.out.println("pageNum: " + pageNum + ", amount: " + amount);
	}
	
	// 이 밑에 있는 코드는 먼저 만들어도 되고 나중에 필요할떄 만들어도 됨
	// 아래 메서드명은 getter메서드 이름규칙대로 작성해야 한다. get(접두사) + typeArr(필드) = getTypeArr 메서드명
	// 클라이언트로부터 검색종류가 (제목또는 작성자 또는 내용) 선택되어지면 type필드 TWC
	// type.split(""); -> "TWC".split("") -> "T"  "W"  "C"
	// BarodMapper.xml 파일에서 사용 typeArr(필드)이름으로 참조하지만, 실제는 아래의 getter메서드가 내부적으로 호출됨.
	// 이 메서드는 type 필드를 배열로 변환하는 메서드입니다. type이 null이면 빈 배열을 반환하고,
	// null이 아니면 각 문자를 배열의 요소로 분리하여 반환합니다.
	public String[] getTypeArr() {
		return type == null ? new String[] {} : type.split("");
	}
	
	// UriComponentsBuilder : 여러개의 파라미터들을 연결하여 URL형태로 만들어주는 기능
	// ?pageNum=2&amount=10&type=T&keyword=사과
	public String getListLink() {
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("")
		  			.queryParam("pageNum", this.pageNum)
					.queryParam("amount", this.amount)
					.queryParam("type", this.type)
					.queryParam("keyword", this.keyword);
				
			return builder.toUriString();
	}
	
}
