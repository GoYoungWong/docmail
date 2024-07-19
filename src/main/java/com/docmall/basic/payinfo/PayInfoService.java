package com.docmall.basic.payinfo;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PayInfoService {

	private final PayInfoMapper payInfoMapper;
	
	// 결제정보 보기
	public PayInfoVO ord_pay_info(Long ord_code) {
		return payInfoMapper.ord_pay_info(ord_code);
	}
}
