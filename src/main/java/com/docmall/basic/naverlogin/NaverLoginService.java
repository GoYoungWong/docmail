package com.docmall.basic.naverlogin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NaverLoginService {

	@Value("${naver.client.id}")
	private String clientId;
	
	@Value("${naver.redirect.uri}")
	private String redirectUri;
	
	@Value("${naver.client.secret}")
	private String clientSecret; 
	
	
	// https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=CLIENT_ID&state=STATE_STRING&redirect_uri=CALLBACK_URL
	public String getNaverAuthorizeUrl() throws UnsupportedEncodingException {
		
		UriComponents uriComponents = UriComponentsBuilder
				.fromUriString("https://nid.naver.com/oauth2.0/authorize")
				.queryParam("response_type", "code")
				.queryParam("client_id", clientId)
				.queryParam("state", URLEncoder.encode("1234", "UTF-8"))
				.queryParam("redirect_uri", URLEncoder.encode(redirectUri, "UTF-8"))
				.build();
		
		return uriComponents.toString();
	}
	
	// https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id=jyvqXeaVOVmV&client_secret=527300A0_COq1_XV33cf&code=EIc5bFrl4RibFls1&state=9kgsGTfH4j7IyAkg
	public String getNaverTokenUrl(NaverCallback callback)  {
		
		
		
		
		// access token을 받기위한 요청보내기작업후 결과값이 JSON으로 응답
		try {
			
			UriComponents uriComponents = UriComponentsBuilder
					.fromUriString("https://nid.naver.com/oauth2.0/token")
					.queryParam("grant_type", "authorization_code")
					.queryParam("client_id", clientId)
					.queryParam("client_secret", clientSecret)
					.queryParam("code", callback.getCode())
					.queryParam("state", URLEncoder.encode(callback.getState(), "UTF-8"))
					.queryParam("service_provider", "NAVER")
					.build();
			
			URL url = new URL(uriComponents.toString());
			
			// https://blueyikim.tistory.com/2199
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			
			int responseCode = conn.getResponseCode();
			BufferedReader br;
			
			// 입력스트림작업.  
			// conn.getInputStream() : 바이트스트림
			// InputStreamReader클래스 : 바이트기반의 스트림을 문자기반스트림으로 변환하는 기능.
			// BufferedReader : 버퍼기능제공 보조스트림
			if(responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			}else {
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			
			String inputLine;
			StringBuffer response = new StringBuffer();
			while((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			
			br.close();
			
			log.info("응답데이터: " + response.toString());
			
			return response.toString();
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public void getNaverTokenDelete(String access_token)  {
					
		
		// access token을 받기위한 요청보내기작업후 결과값이 JSON으로 응답
		try {
			
			UriComponents uriComponents = UriComponentsBuilder
					.fromUriString("https://nid.naver.com/oauth2.0/token")
					.queryParam("grant_type", "delete")
					.queryParam("client_id", clientId)
					.queryParam("client_secret", clientSecret)
//					.queryParam("code", callback.getCode())
//					.queryParam("state", URLEncoder.encode(callback.getState(), "UTF-8"))
					.queryParam("access_token", URLEncoder.encode(access_token, "UTF-8"))
					.build();
			
			URL url = new URL(uriComponents.toString());
			
			// https://blueyikim.tistory.com/2199
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			
			int responseCode = conn.getResponseCode();
			
			log.info("상태코드:" + responseCode);

		} catch (Exception e) {
			
			e.printStackTrace();
		} 
	}
	
	
	
	// https://openapi.naver.com/v1/nid/me
	/*
	 접근 토큰(access token)을 전달하는 헤더
다음과 같은 형식으로 헤더 값에 접근 토큰(access token)을 포함합니다. 토큰 타입은 "Bearer"로 값이 고정되어 있습니다.
Authorization: {토큰 타입] {접근 토큰]
	 */
	// "Authorization: Bearer AAAAPIuf0L+qfDkMABQ3IJ8heq2mlw71DojBj3oc2Z6OxMQESVSrtR0dbvsiQbPbP1/cxva23n7mQShtfK4pchdk/rc="
	// 사용자정보 받아오기
	public String getNaverUserByToken(NaverToken naverToken) {
		String accessToken = naverToken.getAccess_token();
		String tokenType = naverToken.getToken_type();
		
		try {
			URL url = new URL("https://openapi.naver.com/v1/nid/me");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", tokenType + " " + accessToken);
			
			int responseCode = conn.getResponseCode();
			BufferedReader br;
			
			// 입력스트림작업.  
			// conn.getInputStream() : 바이트스트림
			// InputStreamReader클래스 : 바이트기반의 스트림을 문자기반스트림으로 변환하는 기능.
			// BufferedReader : 버퍼기능제공 보조스트림
			if(responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			}else {
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			
			String inputLine;
			StringBuffer response = new StringBuffer();
			while((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			
			br.close();
			
			log.info("사용자정보 응답데이터: " + response.toString());
			
			return response.toString();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
