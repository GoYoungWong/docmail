package com.docmall.basic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@MapperScan(basePackages = "com.docmall.basic.**")
// exclude = SecurityAutoConfiguration.class 스프링시큐리티 적용안됨.
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class DocmallApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocmallApplication.class, args);
	}

}
