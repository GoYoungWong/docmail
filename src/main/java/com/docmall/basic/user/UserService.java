package com.docmall.basic.user;

import java.util.UUID;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;


// 구현클래스
@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserMapper userMapper;

	public void join(UserVo vo) {
		userMapper.join(vo);
		
	}

	public String idCheck(String mbsp_id) {
		// TODO Auto-generated method stub
		return userMapper.idCheck(mbsp_id);
	}
	
	public UserVo login(String mbsp_id) {
		return userMapper.login(mbsp_id);
	}
	
	public String idfind(String mbsp_name, String mbsp_email) {
		return userMapper.idfind(mbsp_name, mbsp_email);
	}
	
	public String pwfind(String mbsp_id, String mbsp_name, String mbsp_email) {
		return userMapper.pwfind(mbsp_id, mbsp_name, mbsp_email);
	}
	
	//임시비밀번호 10자리
	public String getTempPw() {
		
		return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
	}
	
	public void tempPwUpdate(String mbsp_id, String temp_enc_pw) {
		userMapper.tempPwUpdate(mbsp_id, temp_enc_pw);
	}
	
	public void modify(UserVo vo) {
		userMapper.modify(vo);
	}
	
	public void changePw(String mbsp_id, String new_mbsp_password) {
		userMapper.changePw(mbsp_id, new_mbsp_password);
	}
	
	public void delete(String mbsp_id) {
		userMapper.delete(mbsp_id);
	}
	
	public String existsUserInfo(String sns_email) {
		return userMapper.existsUserInfo(sns_email);
	}
	
	public String sns_user_check(String sns_email) {
		return userMapper.sns_user_check(sns_email);
	}
	
	public void sns_user_insert(SNSUserDto dto) {
		userMapper.sns_user_insert(dto);
	}
	
	
	
}
