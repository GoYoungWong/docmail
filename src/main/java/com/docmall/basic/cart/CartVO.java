package com.docmall.basic.cart;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartVO {

	// cart_code, pro_num, mbsp_id, cart_amount, cart_date
	private Long	cart_code;
	private int 	pro_num;
	private String  mbsp_id;
	private int     cart_amount;
	private Date    cart_date;   // Carlendar, LocalDate, LocalTiem, LocalDateTiem
}
