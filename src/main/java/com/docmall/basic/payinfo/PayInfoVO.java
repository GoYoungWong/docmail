package com.docmall.basic.payinfo;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//p_id, ord_code, paymethod, p_price, p_status, p_date

@Builder
@Getter
@Setter
@ToString
public class PayInfoVO {
	
	private Integer  p_id;
	private Long     ord_code;
	private String  paymethod;
	private int		p_price;
	private String  p_status;
	private Date    p_date;
}
