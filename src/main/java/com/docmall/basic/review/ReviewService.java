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
}
