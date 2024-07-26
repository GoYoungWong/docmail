package com.docmall.basic.admin.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.docmall.basic.common.dto.Criteria;

public interface AdminUserMapper {

	// 메일정보 DB저장
	void mailing_save(MailMngVO vo);
	
	// 회원테이블에서 전체회원 메일정보를 읽어오는 작업 
	String[] getALLMeilAddress();
	
	// 메일발송 횟수 증가
	void mailSendCountUpdate(int idx);
	
	// 메일링목록
	List<MailMngVO> getMailinfoList(@Param("cri") Criteria cri,@Param("title") String title);
	
	// 메일리스트 개수
	int getMailListCount(String title);
	
	// 메일목록에서 메일발송
	MailMngVO getMailSendinfo(int idx);
	
	// 메일수정
	void mailingedit(MailMngVO vo);
	
}
