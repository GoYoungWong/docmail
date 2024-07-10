package com.docmall.basic.review;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.docmall.basic.common.dto.Criteria;

public interface ReviewMapper {

	// 후기 리스트
	List<ReviewVO> rev_list(@Param("pro_num") Integer pro_num,@Param("cri") Criteria cri );
	
	// 후기리스트 총개수
	int getCountReviewBypro_num(Integer pro_num);
}
