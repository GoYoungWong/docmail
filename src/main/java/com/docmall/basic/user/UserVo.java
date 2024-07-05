package com.docmall.basic.user;

import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 성능관점
   - 회원테이블(로그인 포함)
   - 회원테이블 + 로그인테이블
 * 
총 컬럼수 : 14개
mbsp_id, mbsp_name, mbsp_email, mbsp_password, mbsp_zipcode, mbsp_addr, mbsp_deaddr, 
mbsp_phone, mbsp_nick, mbsp_receive, mbsp_point, mbsp_lastlogin, mbsp_datesub, mbsp_updatedate
 */

//@Data
@Getter
@Setter
@ToString
public class UserVo {

	private String mbsp_id;
	private String mbsp_name;
	private String mbsp_email;
	private String mbsp_password;
	private String mbsp_zipcode;
	private String mbsp_addr;
	private String mbsp_deaddr;
	private String mbsp_phone;
	private String mbsp_nick;
	private String mbsp_receive;
	private int mbsp_point;
	private Date mbsp_lastlogin;
	private Date mbsp_datesub;
	private Date mbsp_updatedate;
	
	
}
