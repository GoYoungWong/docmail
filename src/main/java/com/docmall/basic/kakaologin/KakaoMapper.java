package com.docmall.basic.kakaologin;

public interface KakaoMapper {
	
	KakaoUserInfo existskakaoInfo(String sns_email);
	
	void kakao_insert(KakaoUserInfo kakaoUserInfo);
}
