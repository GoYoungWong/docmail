package com.docmall.basic.review;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.common.dto.PageDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// pro_detail.html 이 파일이에서 상품후기 처리.

@RestController // AJAX로 사용될 것이다는 표시
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/review/*")
public class ReviewController {

	private final ReviewService reviewService;
	
	// <> : 제너릭(타입파라미터)
	// 가지고오는 클래스가 다르면 MAP 같으면 List로 사용
	// ajax 요청받는 주소에는 Model을 사용하지 않는다.
	// 상품라뷰목록과페이징. Rest API 개발방법혼   /rev_list/상품코드/페이지번호     /rev_list/10/1 -> 매핑주소에 파트부분을 매개변수로 사용하고자 할 경우 @PathVariable 사용
	// 전통적인 주소: /revlist?pro_num=10&page=1
	@GetMapping("/revlist/{pro_num}/{page}")  // 목록과 페이징작업을 할 경우는 Criteria 파라미터를 지금까지는 사용했다.
	public ResponseEntity<Map<String, Object>> revlist(@PathVariable("pro_num") int pro_num, @PathVariable("page") int page) throws Exception {
		ResponseEntity<Map<String, Object>> entity = null;
		Map<String, Object> map = new HashMap<>();
		
		
		Criteria cri = new Criteria();
		cri.setAmount(2);
		cri.setPageNum(page);
		
		// 1)후기목록
		List<ReviewVO> revlist = reviewService.rev_list(pro_num, cri);
		
		// 2)페이징정보
		int revcount = reviewService.getCountReviewBypro_num(pro_num);
		PageDTO pageMaker = new PageDTO(cri, revcount);
		
		map.put("revlist", revlist);
		map.put("pageMaker", pageMaker);
		
		entity = new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
		
		
		return entity;
	}
}