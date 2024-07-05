package com.docmall.basic.kakaopay;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KakaopayService {

	private HttpHeaders getHeaderInfo() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "SECRET_KEY DEVFF7BA28D71B6BBF1464A8EBB571FF9725C6BA");
		headers.set("Content-type", "application/json;charset=utf-8");
		
		return headers;
	}
	
	public ReadyResponse payReady(Long odr_code, String itemName, int quantity, String mbsp_id, int totalAmount) {
		
		// 응답받은 데이터
		ReadyResponse readyResponse = null;
		try {
			MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
			
			parameters.add("cid", "TC0ONETIME"); // 가맹점 코드, 10자
			parameters.add("partner_order_id", "partner_order_id"); // 가맹점 주문번호(쇼핑몰 상품주문번호), 최대 100자
			parameters.add("partner_user_id", "partner_user_id"); // 가맹점 회원 id, 최대 100자
			parameters.add("item_name", "good");// 상품명, 최대 100자.   예> A상품외 2건
			parameters.add("quantity", "1"); // 상품 수량
			parameters.add("total_amount", "2200"); // 상품 총액
			parameters.add("vat_amount", "200"); // 상품 총액
			parameters.add("tax_free_amount", "0"); // 상품 비과세 금액

			
			parameters.add("approval_url", "http://localhost:9090/kakao/orderApproval"); // 결제 성공 시 redirect url, 최대 255자
			parameters.add("cancel_url", "http://localhost:9090/kakao/orderApproval"); // 결제 취소 시 redirect url, 최대 255자
			parameters.add("fail_url", "http://localhost:9090/kakao/orderApproval"); // 결제 실패 시 redirect url, 최대 255자
			
			
			// https://jung-story.tistory.com/132
			
			//헤더와 파라미터정보를 구성하는 작업
			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String,String>>(parameters, this.getHeaderInfo());
			
			//Kakao API 서버와 통신
			RestTemplate template = new RestTemplate();
			
			String kakaoReadyUrl = "https://open-api.kakaopay.com/online/v1/payment/ready"; 
			
			readyResponse = template.postForObject(kakaoReadyUrl, requestEntity, ReadyResponse.class);
			
			log.info("응답데이터: " + readyResponse);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return readyResponse;
	}

	public ApproveResponse payApprove(Long odr_code, String tid, String pgToken, String mbsp_id) {
	
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		
		parameters.add("cid", "TC0ONETIME");
		parameters.add("tid", tid);
		parameters.add("partner_order_id", String.valueOf(odr_code));
		parameters.add("partner_user_id", mbsp_id);
		parameters.add("pg_token", pgToken);
		
		//헤더와 파라미터정보를 구성하는 작업
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String,String>>(parameters, this.getHeaderInfo());
		
		//Kakao API 서버와 통신
		RestTemplate template = new RestTemplate();
		
		String kakaoApproveUrl = "https://kapi.kakao.com/v1/payment/approve"; 
		
		// 응답받은 데이터
		ApproveResponse approveResponse = template.postForObject(kakaoApproveUrl, requestEntity, ApproveResponse.class);
		
		return approveResponse;
	}
}
