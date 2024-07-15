package com.docmall.basic.order;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.docmall.basic.cart.CartProductVO;
import com.docmall.basic.cart.CartService;
import com.docmall.basic.cart.CartVO;
import com.docmall.basic.user.UserService;
import com.docmall.basic.user.UserVo;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/order/*")
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderService orderService;
	
	private final CartService cartService;
	
	private final UserService userService;
	


	/*
	   1) pro_list.html(Modal) 바로구매 2)pro_detail.html 바로구매 3) 장바구니에서 주문하기 
	 	1) 2)번은 CartVO vo 파라미터를 사용하는것 같다.  3)번은 CartVO vo 파라미터를 사용하지 않기때문에 필요가 없다.
	 */
	@GetMapping("/orderinfo")
	public String orderinfo(@RequestParam(value = "type", defaultValue = "direct") String type, CartVO vo,Model model ,HttpSession session) throws Exception {
		
		String mbsp_id = ((UserVo) session.getAttribute("login_status")).getMbsp_id();
		vo.setMbsp_id(mbsp_id);
		
		if(!type.equals("cartorder")) {
			// 1) 장바구니 저장
			cartService.cart_add(vo);
			
		}

		// 2) 주문하기
		List<CartProductVO> cart_list = cartService.cart_list(mbsp_id);
		int total_price = 0;
		cart_list.forEach(d_vo -> {
			d_vo.setPro_up_folder(d_vo.getPro_up_folder().replace("\\", "/"));
//			total_price += (d_vo.getCart_amount() * d_vo.getPro_price());
			}
		);
		
		for(int i=0; i < cart_list.size(); i++) {
			total_price += (cart_list.get(i).getPro_price() * cart_list.get(i).getCart_amount());
			
		}
		
		
		model.addAttribute("cart_list", cart_list);
		model.addAttribute("total_price", total_price);
		return "/order/orderinfo";
	}
	
	// 주문자와 동일
	@GetMapping("/ordersame")
	public ResponseEntity<UserVo> ordersame(HttpSession session) throws Exception {
		ResponseEntity<UserVo> entity = null;
		
		String mbsp_id = ((UserVo) session.getAttribute("login_status")).getMbsp_id();
		// 회원정보를 가리고 오기위해 작성
		entity = new ResponseEntity<UserVo>(userService.login(mbsp_id), HttpStatus.OK);
		
		return entity;
	}
}






