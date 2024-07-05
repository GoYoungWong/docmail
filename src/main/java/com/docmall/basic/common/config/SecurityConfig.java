package com.docmall.basic.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 설정목적으로 사용하는 클래스에는 어노테이션 적용
//@EnableWebSecurity
public class SecurityConfig {

    // 스프링시큐리티 설정. v2.7과 v3.x 버전 차이가 있음.
//	@Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf((csrf) -> csrf.disable());
////                .cors((c) -> c.disable())
////                .headers((headers) -> headers.disable());
//        return http.build();
//    }

	// 암호화기능 bean생성.  passwordEncoder bean 자동생성
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
