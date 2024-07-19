package com.docmall.basic.admin.order;

import java.util.List;

import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.order.OrderVO;

public interface AdminOrderMapper {

	// 주문관리 리스트
	List<OrderVO> order_list(Criteria cri);
	
	// 리스트 총개수
	int getTotalCount(Criteria cri);
	
	// 주문자(수령인)정보
	OrderVO order_info(Long ord_code);
	
	// 주문상품정보 http://www.manual.oneware.co.kr/official.php/home/info/2037
	List<OrderDetailInfoVo> order_detail_info(Long ord_code);
	
	
}
