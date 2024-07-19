package com.docmall.basic.admin.order;

import java.util.List;

import org.springframework.stereotype.Service;

import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.order.OrderVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminOrderService {

	private final AdminOrderMapper adminOrderMapper;
	
	// 주문관리 리스트
	public List<OrderVO> order_list(Criteria cri) {
		return adminOrderMapper.order_list(cri);
	}
		
	// 리스트 총개수
	public int getTotalCount(Criteria cri) {
		return adminOrderMapper.getTotalCount(cri);
	}
	
	// 주문자(수령인)정보
	public OrderVO order_info(Long ord_code) {
		return adminOrderMapper.order_info(ord_code);
	}
	
	// 주문상품정보 
	public List<OrderDetailInfoVo> order_detail_info(Long ord_code) {
		return adminOrderMapper.order_detail_info(ord_code);
	}
}
