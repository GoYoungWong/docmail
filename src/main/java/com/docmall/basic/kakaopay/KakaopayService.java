package com.docmall.basic.kakaopay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by kakaopay
 */
@Service
@Slf4j
public class KakaoPayService {
    @Value("${kakaopay.api.secret.key}")
    private String kakaopaySecretKey;

	@Value("${cid}")
	private String cid;
	
	@Value("${approvalUrl}")
	private String approval;
	
	@Value("${cancelUrl}")
	private String cancel;
	
	@Value("${failUrl}")
	private String fail;
//
//    @Value("${sample.host}")
//    private String sampleHost;

	// 전역변수: 이렇게 따로 뺀 이유는 결제준비요청(ready)도 쓰고 결제승인요청(approve) 여기도 사용해서 따로 뺌
    private String tid;
    
    private String partnerOrderId;
    
    private String partnerUserId;

    // 1)결제준비요청(ready)				이 매개변수 값은 받아와서 사용하기 위해 작성
    public ReadyResponse ready(String partnerOrderId, String partnerUserId, String itemName, int quantity, 
    														int totalAmount, int taxFreeAmount, int vatAmount) {
        // 1) Request header
        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "DEV_SECRET_KEY " + kakaopaySecretKey);
//        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "SECRET_KEY " + kakaopaySecretKey);
        headers.set("Content-type", "application/json;charset=utf-8");

        // 2) Request param
        ReadyRequest readyRequest = ReadyRequest.builder() 
        		// builder: 필드에 있는 것중에 필요한 것만 생성자를 만들때 사용된다. 
        		// 그리고 사용하기 위해서는 @Builder 어노테이션을 필드가 있는 클래스에 넣어야 한다.
        		// https://mangkyu.tistory.com/163
                .cid(cid)
                .partnerOrderId(partnerOrderId)  // 주문번호
                .partnerUserId(partnerUserId)  // 사용자아이디
                .itemName(itemName)   // 대표상품이름
                .quantity(quantity)  		// 수량
                .totalAmount(totalAmount)  // 전체가격
                .taxFreeAmount(taxFreeAmount)
                .vatAmount(vatAmount)
                .approvalUrl(approval) // 성공.  카카오페이 서버에서 이 주소를 찾아온다.
                .cancelUrl(cancel) // 취소
                .failUrl(fail)  // 실패.
                .build();

        // 3) Send reqeust
        HttpEntity<ReadyRequest> entityMap = new HttpEntity<>(readyRequest, headers);
        
        // RestTemplate: Spring Framework에서 제공하는 HTTP 클라이언트
        // postForEntity: RestTemplate에 POST요청시 사용
        ResponseEntity<ReadyResponse> response = new RestTemplate().postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                entityMap,
                ReadyResponse.class
        );
        ReadyResponse readyResponse = response.getBody();
        
        // 주문번호와 TID를 매핑해서 저장해놓는다.
        // Mapping TID with partner_order_id then save it to use for approval request.
        this.tid = readyResponse.getTid(); // 전역변수 작업
        
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        
        return readyResponse;
    }
    // 2) 결제승인요청(approve)
    public String approve(String pgToken) { // 리턴타입 String 이유? 카카오쪽에서 알아서 해라라는 뜻
        // ready할 때 저장해놓은 TID로 승인 요청
        // Call “Execute approved payment” API by pg_token, TID mapping to the current payment transaction and other parameters.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Request param
        ApproveRequest approveRequest = ApproveRequest.builder()
                .cid(cid)
                .tid(tid)
                .partnerOrderId(partnerOrderId)  
                .partnerUserId(partnerUserId)
                .pgToken(pgToken) 
                .build();

        // Send Request
        HttpEntity<ApproveRequest> entityMap = new HttpEntity<>(approveRequest, headers);
        try {
            ResponseEntity<String> response = new RestTemplate().postForEntity(
                    "https://open-api.kakaopay.com/online/v1/payment/approve",
                    entityMap,
                    String.class
            );
            // 승인 결과를 저장한다.
            // save the result of approval
            String approveResponse = response.getBody();
            return approveResponse;
        } catch (HttpStatusCodeException ex) {
            return ex.getResponseBodyAsString();
        }
    }
}
