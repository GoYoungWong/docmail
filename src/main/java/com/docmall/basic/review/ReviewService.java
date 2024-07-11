package com.docmall.basic.review;

import java.util.List;

import org.springframework.stereotype.Service;

import com.docmall.basic.common.dto.Criteria;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewMapper reviewMapper;
	
	// 후기 리스트
	public List<ReviewVO> rev_list(Integer pro_num,Criteria cri ) {
		return reviewMapper.rev_list(pro_num, cri);
	}
		
	// 후기리스트 총개수
	public int getCountReviewBypro_num(Integer pro_num) {
		return reviewMapper.getCountReviewBypro_num(pro_num);
	}
	
	// 상품후기 저장
	public void review_save(ReviewVO vo) {
		reviewMapper.review_save(vo);
	}
	
	// 상품리뷰 삭제
	public void review_delete(Integer rev_code) {
		reviewMapper.review_delete(rev_code);
	}
}
