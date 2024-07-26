package com.docmall.basic.admin.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.docmall.basic.common.dto.Criteria;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUserService {

	private final AdminUserMapper adminUserMapper;
	
	// 메일정보 DB저장
	public void mailing_save(MailMngVO vo) {
		adminUserMapper.mailing_save(vo);
	}
	
	// 회원테이블에서 전체회원 메일정보를 읽어오는 작업
	public String[] getALLMeilAddress() {
		return adminUserMapper.getALLMeilAddress();
	}
	
	// 메일발송 횟수 증가
	public void mailSendCountUpdate(int idx) {
		adminUserMapper.mailSendCountUpdate(idx);
	}
	
	// 메일링목록
	public List<MailMngVO> getMailinfoList(Criteria cri,String title) {
		return adminUserMapper.getMailinfoList(cri, title);
	}
		
	// 메일리스트 개수
	public int getMailListCount(String title) {
		return adminUserMapper.getMailListCount(title);
	}
	
	// 메일목록에서 메일발송
	public MailMngVO getMailSendinfo(int idx) {
		return adminUserMapper.getMailSendinfo(idx);
	}
	
	// 메일수정
	public void mailingedit(MailMngVO vo) {
		adminUserMapper.mailingedit(vo);
		}
}
