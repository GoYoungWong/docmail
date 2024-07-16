package com.docmall.basic.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.docmall.basic.cart.CartMapper;
import com.docmall.basic.payinfo.PayInfMapper;
import com.docmall.basic.payinfo.PayInfoVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

	private final OrderMapper orderMapper;
	
	private final PayInfMapper payInfMapper; 
	
	private final CartMapper cartMapper;
	
	// @Transactional: 하나의 단위로 여러개를 할려면 이거 넣으면됨 (원해는 데이터베이스기능임)
	@Transactional
	public void order_process(OrderVO vo, String mbsp_id) {
		
		// 1)주문테이블(insert)
		vo.setMbsp_id(mbsp_id);
		orderMapper.order_insert(vo);
		
		// 2)주문살세테이블(insert)
		orderMapper.orderDetail_insert(vo.getOrd_code(), mbsp_id);
		
		// 3)결체테이블(insert)
		PayInfoVO p_vo = PayInfoVO.builder()
				.ord_code(vo.getOrd_code())
				.p_price(vo.getOrd_price())
				.paymethod("kakaopay")
				.p_status("완납")
				.build();
		
		payInfMapper.payinfo_insert(p_vo);
		
		// 4)장바구니테이블(delete)
		cartMapper.cart_empty(mbsp_id);
	}
}
