package com.docmall.basic.review;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// rev_code, mbsp_id, pro_num, rev_content, rev_title, rev_rate, rev_date

@Setter
@Getter
@ToString
public class ReviewVO {

	private Integer  rev_code;
	private String   mbsp_id;
	private int      pro_num;
	private String   rev_content;
	private String   rev_title;
	private int      rev_rate;
	private Date     rev_date;
}
