package com.docmall.basic.order;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
order_tbl
ord_code, mbsp_id, ord_name, ord_addr_zipcode, ord_addr_basic, ord_addr_detail, ord_tel, ord_price, ord_desc,ord_admin_memo ,ord_regdate
SEQ_ORD_CODE
*/

@Getter
@Setter
@ToString
public class OrderVO {
	
	// 주문자정보
	private Long   ord_code;
	private String mbsp_id;
	private String ord_name;
	private String ord_addr_zipcode;
	private String ord_addr_basic;
	private String ord_addr_detail;
	private String ord_tel;
	private int    ord_price;
	private String ord_desc;
	private String ord_admin_memo;  // 관리자메모
	private Date   ord_regdate;

	
	
	
}
