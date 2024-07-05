package com.docmall.basic.admin.category;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class AdminCategoryService {

	private final AdminCategoryMapper adminCategoryMapper;
	
	public List<CategoryVo> getFirstCategoryList() {
		return adminCategoryMapper.getFirstCategoryList();
	}
	
	public List<CategoryVo> getSecondCategoryList(int cat_prtcode) {
		return adminCategoryMapper.getSecondCategoryList(cat_prtcode);
	}
	
	public CategoryVo getFirstCategoryBySecondCategory(int cat_code) {
		return adminCategoryMapper.getFirstCategoryBySecondCategory(cat_code);
	}
}
