package com.docmall.basic.kakaopay;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/kakao/*")
@Controller
public class KakaopayController {

	private final KakaopayService kakaopayService;
	
	@GetMapping("/kakaoPay")
	public String kakaoPay() {
		
		ReadyResponse readyResponse = kakaopayService.payReady(1L, "good", 10, "user01", 10);
		
		log.info("응답데이터: " + readyResponse);
		
		return "";
	}
	
	@GetMapping("/orderApproval")
	public void orderApproval() {
		
	}
}
