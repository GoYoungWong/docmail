package com.docmall.basic.admin.category;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/admin/category/*")
@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminCategoryController {

	private final AdminCategoryService adminCategoryService;
	
	
	// 2차카테고리 목록
	@GetMapping("/secondcategory/{cat_prtcode}")
	public ResponseEntity<List<CategoryVo>> getSecondCategoryList(@PathVariable("cat_prtcode") int cat_prtcode) throws Exception {
		
		log.info("1차카테고리코드: " + cat_prtcode);
		
		ResponseEntity<List<CategoryVo>> entity = null;
		
		entity = new ResponseEntity<List<CategoryVo>>(adminCategoryService.getSecondCategoryList(cat_prtcode), HttpStatus.OK);
		
		return entity;
	}
}
