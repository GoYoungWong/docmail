package com.docmall.basic.admin.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.docmall.basic.common.dto.Criteria;

public interface AdminProductMapper {

	void pro_insert(ProductVo vo);
	
	List<ProductVo> pro_list(Criteria cri);
	
	int getTotalCount(Criteria cri);
	
	ProductVo pro_edit(Integer pro_num);
	
	void pro_edit_ok(ProductVo vo);
	
	void pro_delete(Integer pro_num);
	
	void pro_checked_modify1(@Param("pro_num") Integer pro_num, @Param("pro_price") Integer pro_price, @Param("pro_buy") String pro_buy);
	
	void pro_checked_modify2(List<ProductDTO> pro_modify_list);
	
	
}
