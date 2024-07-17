package com.docmall.basic.kakaopay;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docmall.basic.cart.CartProductVO;
import com.docmall.basic.cart.CartService;
import com.docmall.basic.order.OrderService;
import com.docmall.basic.order.OrderVO;
import com.docmall.basic.user.UserVo;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/kakao/*")
@Controller
public class KakaopayController {

	private final KakaoPayService kakaoPayService;
	private final CartService cartService;
	private final OrderService orderService;
	
	private OrderVO vo; // 전역변수
	
	private String mbsp_id; // 전역변수
	
	
	@GetMapping("/kakaoPayRequest")
	public void kakaoPayRequest() {
		
	}
	
	
	@ResponseBody
	@GetMapping(value =  "/kakaoPay")
	public ReadyResponse kakaoPay(OrderVO vo,HttpSession session) {
		
		log.info("주문자정보1: " + vo);
		
		// 1)결제준비요청(ready)
		// ready(String partnerOrderId, String partnerUserId, String itemName, int quantity, 
		// 					int totalAmount, int taxFreeAmount, int vatAmount)
		
		String mbsp_id = ((UserVo) session.getAttribute("login_status")).getMbsp_id();
		
		List<CartProductVO> cart_list = cartService.cart_list(mbsp_id);
		this.mbsp_id = mbsp_id;
		
		String itemName = "";
		int quantity = 0;
		int totalAmount = 0;
		int taxFreeAmount = 0;
		int vatAmount = 0;
		
		for(int i=0; i < cart_list.size(); i++) {
			itemName += cart_list.get(i).getPro_name() + "/";
			quantity += cart_list.get(i).getCart_amount();
			totalAmount += cart_list.get(i).getPro_price() * cart_list.get(i).getCart_amount();
		}
		
		String partnerOrderId = mbsp_id;
		String partnerUserId = mbsp_id;
		
		// 1) 결제준비요청
		ReadyResponse readyResponse = kakaoPayService.ready(partnerOrderId, partnerUserId, itemName, quantity, 
				 					totalAmount,  taxFreeAmount,  vatAmount);
		
		log.info("응답데이터: " + readyResponse);
		
		// 주문정보
		this.vo = vo;
		
		return readyResponse;
	}
	
	// 성공
	@GetMapping("/approval")
	public void approval(String pg_token) {
		log.info("pg_token: " + pg_token);
		
		// 2) 결제승인요청(approval)
		String approveResponse = kakaoPayService.approve(pg_token);
		
		log.info("최종결과: " + approveResponse);
		
		// 주문정보저장
		// aid값이 존재하면
		
		// 트랜잭션으로 처리: 주문테이블, 주문상세테이블, 결제테이블, 장바구니 비우기
		if(approveResponse.contains("aid")) {   // aid : 성공적으로 넘어왔을때 발생하는 키값
			log.info("주문자정보2: " + vo);
			orderService.order_process(vo, mbsp_id, "kakaopay", "완료", "kakaopay");
		}
		
		log.info("최종결과: " + approveResponse);
	}
	
	// 취소
	@GetMapping("/cancel")
	public void cancel() {
		
		
	}
	
	// 실패
	@GetMapping("/fail")
	public void fail() {
		
		
	}
}
