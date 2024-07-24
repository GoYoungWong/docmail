package com.docmall.basic.payinfo;

public interface PayInfoMapper {

	// 결제정보 삽입
	void payinfo_insert(PayInfoVO vo);
	
	// 결제정보 보기
	PayInfoVO ord_pay_info(Long ord_code);
	
	// 결제테이블의 총금액변경
	void pay_tot_price_change(Long ord_code);
}
