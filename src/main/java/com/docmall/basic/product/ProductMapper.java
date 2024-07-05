package com.docmall.basic.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.docmall.basic.admin.product.ProductVo;
import com.docmall.basic.common.dto.Criteria;

public interface ProductMapper {

	// 상품리스트
	List<ProductVo> pro_list(@Param("cat_code") int cat_code, @Param("cri") Criteria cri);
	
	// 상품 총 개수
	int getCountProductByCategory(int cat_code);
	
	// 상품정보
	ProductVo pro_info(int pro_num);
}
