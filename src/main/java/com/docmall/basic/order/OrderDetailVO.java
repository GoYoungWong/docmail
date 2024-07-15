package com.docmall.basic.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
ordetail_tbl
ord_code, pro_num, dt_amount, dt_price
*/

@Getter
@Setter
@ToString
public class OrderDetailVO {

	private Long ord_code;
	private int  pro_num;
	private int  dt_amount;
	private int  dt_price;
}
