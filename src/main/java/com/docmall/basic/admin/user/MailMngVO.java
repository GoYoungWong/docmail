package com.docmall.basic.admin.user;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailMngVO {

	// m_idx, m_title, m_content, m_gubun,m_send_count ,reg_date
	
	private Integer idx;
	private String title;
	private String content;
	private String gubun;
	private int    sendcount;
	private Date   regDate;
}
