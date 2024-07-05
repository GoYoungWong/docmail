package com.docmall.basic.kakaologin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.docmall.basic.user.SNSUserDto;
import com.docmall.basic.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/oauth2")
public class KakaoLoginController {

	private final KakaoLoginService kakaoLoginService;
	private final UserService userService;
	
	@Value("${kakao.client.id}")
	private String clientId;
	
	@Value("${kakao.redirect.uri}")
	private String redirectUri;
	
	@Value("${kakao.client.secret}")
	private String clientSecret;
	
	// Step1. 카카로 로그인 API Server에게 인가코드를 요청하는 작업
	// 헤더는 없고, 요청 파라미터가 있는 경우
	@GetMapping("/kakaologin")
	public String connect() {
		
		StringBuffer url = new StringBuffer();
		url.append("https://kauth.kakao.com/oauth/authorize?");
		url.append("response_type=code");
		url.append("&client_id=" + clientId);
		url.append("&redirect_uri=" + redirectUri); // 	http://localhost:9090/oauth2/callback/kakao
		
		//추가옵션.  다시 사용자 인증을 수행하고자 할 때 사용
		url.append("&prompt=login");
		
		log.info("인가코드: " + url.toString());
		
		return "redirect:" + url.toString();
	}
	
	// step2. 카카로 로그인 API Server 에서 개발사이트 Callback주소호출.  카카오개발자 사이트에서 애플리케이션 등록과정에서 아래주소를 설정을 이미 한상태.
	@GetMapping("/callback/kakao")
	public String callback(String code, HttpSession session) {
		
		log.info("code:" + code);
		
		String accessToken = "";
		KakaoUserInfo kakaoUserInfo = null;
		try {
			// 카카오 로그인 API서버에게 로그인 인증성공. 인증토큰을 이용하여 카카오사용자에 대한 정보를 제공.
			accessToken = kakaoLoginService.getAccessToken(code); // 인가코드를 통한 인증토큰을 요청
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			// 카카오 로그인 API서버에서 보내 온 사용자 정보
			kakaoUserInfo = kakaoLoginService.getKakaoUserInfo(accessToken);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		if(kakaoUserInfo != null) {
			
			log.info("사용자정보:" + kakaoUserInfo);
			
		
			//인증을 세션방식으로 처리.
			session.setAttribute("kakao_status", kakaoUserInfo); // 인증여부에 사용
			session.setAttribute("accessToken", accessToken);  // 카카오 로그아웃에 사용
			
			String sns_email = kakaoUserInfo.getEmail();
			
			String sns_login_type = userService.existsUserInfo(sns_email);
//			session.setAttribute("sns_type", sns_login_type);
			
			if(userService.existsUserInfo(sns_email) == null && userService.sns_user_check(sns_email) == null) {
				//SNS테이블 데이타 삽입작업.
				SNSUserDto dto = new SNSUserDto();
				dto.setId(kakaoUserInfo.getId().toString());
				dto.setEmail(kakaoUserInfo.getEmail());
				dto.setName(kakaoUserInfo.getNickname());
				dto.setSns_type("kakao");
				
				
				userService.sns_user_insert(dto);
			}
			
		}
		
		
		return "redirect:/";
	}
	
	@GetMapping("/kakaologout")
	public String kakaologout(HttpSession session) {
		
		String accessToken =  (String) session.getAttribute("accessToken");
		
		if(accessToken != null && !"".equals(accessToken)) {
			try {
				kakaoLoginService.kakaologout(accessToken);
			}catch(JsonProcessingException ex) {
				throw new RuntimeException(ex);
			}
			
			session.removeAttribute("kakao_status");
			session.removeAttribute("accessToken");
		}
		
		return "redirect:/";
	}
	
	
}
