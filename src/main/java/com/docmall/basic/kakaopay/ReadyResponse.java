package com.docmall.basic.kakaopay;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by kakaopay
 */
@Getter
@Setter
@ToString
public class ReadyResponse {
    private String tid;
    private String created_at;
    private String next_redirect_pc_url; // 결제진행 QR코드주소
    
}
