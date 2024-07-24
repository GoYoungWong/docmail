package com.docmall.basic.admin.order;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.order.OrderVO;

public interface AdminOrderMapper {

	// 주문관리 리스트
	List<OrderVO> order_list(@Param("cri")Criteria cri,@Param("start_date") String start_date,@Param("end_date") String end_date);
	
	// 리스트 총개수
	int getTotalCount(@Param("cri")Criteria cri,@Param("start_date") String start_date,@Param("end_date") String end_date);
	
	// 주문자(수령인)정보
	OrderVO order_info(Long ord_code);
	
	// 주문상품정보 http://www.manual.oneware.co.kr/official.php/home/info/2037
	List<OrderDetailInfoVo> order_detail_info(Long ord_code);
	
	// 개별삭제
	void order_product_delete(@Param("ord_code") Long ord_code, @Param("pro_num") int pro_num);
	
	// 주문기본정보 수정
	void order_basic_modify(OrderVO vo);
	
	// 주문테이블 주문금액 총금액변경.
	void order_tot_price_change(Long ord_code);
	
	
}
