package com.docmall.basic.user;

import org.apache.ibatis.annotations.Param;

public interface UserMapper {

	void join(UserVo vo);
	
	String idCheck(String mbsp_id);
	
	UserVo login(String mbsp_id);
	
	String idfind(@Param("mbsp_name") String mbsp_name, @Param("mbsp_email") String mbsp_email);
	
	String pwfind(@Param("mbsp_id") String mbsp_id, @Param("mbsp_name") String mbsp_name, @Param("mbsp_email") String mbsp_email);

	void tempPwUpdate(@Param("mbsp_id") String mbsp_id, @Param("temp_enc_pw") String temp_enc_pw);
	
	void modify(UserVo vo);
	
	void changePw(@Param("mbsp_id") String mbsp_id, @Param("new_mbsp_password") String new_mbsp_password);
	
	void delete(String mbsp_id);
	
	String existsUserInfo(String sns_email);
	
	// sns user 중복체크
	String sns_user_check(String sns_email);
	
	void sns_user_insert(SNSUserDto dto);
	

		
}
