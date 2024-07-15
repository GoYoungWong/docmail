package com.docmall.basic.kakaopay;

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
//    @Value("${kakaopay.api.secret.key}")
//    private String kakaopaySecretKey;
//
//    @Value("${cid}")
//    private String cid;
//
//    @Value("${sample.host}")
//    private String sampleHost;

    private String tid;

    // 1)결제준비요청(ready)
    public ReadyResponse ready() {
        // 1) Request header
        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "DEV_SECRET_KEY " + kakaopaySecretKey);
//        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "SECRET_KEY DEV5611B37223A9D8322A9BED1D37EFF6D7478C5");
        headers.set("Content-type", "application/json;charset=utf-8");

        // 2) Request param
        ReadyRequest readyRequest = ReadyRequest.builder() 
        		// builder: 필드에 있는 것중에 필요한 것만 생성자를 만들때 사용된다. 
        		// 그리고 사용하기 위해서는 @Builder 어노테이션을 필드가 있는 클래스에 넣어야 한다.
        		// https://mangkyu.tistory.com/163
                .cid("TC0ONETIME")
                .partnerOrderId("1")
                .partnerUserId("1")
                .itemName("상품명")
                .quantity(1)
                .totalAmount(1100)
                .taxFreeAmount(0)
                .vatAmount(100)
                .approvalUrl("http://localhost:9090/kakao/approval") // 성공.  카카오페이 서버에서 이 주소를 찾아온다.
                .cancelUrl("http://localhost:9090/kakao/cancel") // 취소
                .failUrl("http://localhost:9090/kakao/fail")  // 실패.
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
        
        return readyResponse;
    }
    // 2) 결제승인요청(approve)
    public String approve(String pgToken) { // 리턴타입 String 이유? 카카오쪽에서 알아서 해라라는 뜻
        // ready할 때 저장해놓은 TID로 승인 요청
        // Call “Execute approved payment” API by pg_token, TID mapping to the current payment transaction and other parameters.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY DEV5611B37223A9D8322A9BED1D37EFF6D7478C5");
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Request param
        ApproveRequest approveRequest = ApproveRequest.builder()
                .cid("TC0ONETIME")
                .tid(tid)
                .partnerOrderId("1")
                .partnerUserId("1")
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
