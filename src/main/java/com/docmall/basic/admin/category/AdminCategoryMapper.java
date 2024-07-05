package com.docmall.basic.admin.category;

import java.util.List;

public interface AdminCategoryMapper {

	// 1차카테고리 목록
	List<CategoryVo> getFirstCategoryList();
	
	// 2차카테고리 목록
	List<CategoryVo> getSecondCategoryList(int cat_prtcode);
	
	// 2차카테고리정보를 이용한 1차카테고리정보
	CategoryVo getFirstCategoryBySecondCategory(int cat_code);
}
