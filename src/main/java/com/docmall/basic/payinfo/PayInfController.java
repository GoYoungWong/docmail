package com.docmall.basic.payinfo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/payinfo/*")
@RequiredArgsConstructor
public class PayInfController {

	private final PayInfService payInfService;
}
