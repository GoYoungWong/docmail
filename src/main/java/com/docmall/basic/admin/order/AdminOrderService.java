package com.docmall.basic.admin.order;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.order.OrderVO;
import com.docmall.basic.payinfo.PayInfoMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminOrderService {

	private final AdminOrderMapper adminOrderMapper;
	
	private final PayInfoMapper payInfoMapper;
	
	// 주문관리 리스트
	public List<OrderVO> order_list(Criteria cri, String start_date, String end_date) {
		return adminOrderMapper.order_list(cri,start_date,end_date);
	}
		
	// 리스트 총개수
	public int getTotalCount(Criteria cri,String start_date, String end_date) {
		return adminOrderMapper.getTotalCount(cri,start_date,end_date);
	}
	
	// 주문자(수령인)정보
	public OrderVO order_info(Long ord_code) {
		return adminOrderMapper.order_info(ord_code);
	}
	
	// 주문상품정보 
	public List<OrderDetailInfoVo> order_detail_info(Long ord_code) {
		return adminOrderMapper.order_detail_info(ord_code);
	}
	
	// 개별삭제
	@Transactional
	public void order_product_delete(Long ord_code, int pro_num) {
		
		// 주문상품 개별삭제
		adminOrderMapper.order_product_delete(ord_code, pro_num);
		
		// 주문테이블 주문금액 변경.
		adminOrderMapper.order_tot_price_change(ord_code);
		
		// 결제테이블 주문금액 변경.
		payInfoMapper.pay_tot_price_change(ord_code);
				
		
		
	}
	
	// 주문기본정보 수정
	public void order_basic_modify(OrderVO vo) {
		adminOrderMapper.order_basic_modify(vo);
	}
}
