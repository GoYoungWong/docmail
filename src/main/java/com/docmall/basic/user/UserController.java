package com.docmall.basic.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.docmall.basic.kakaologin.KakaoUserInfo;
import com.docmall.basic.mail.EmailDTO;
import com.docmall.basic.mail.EmailService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user/*")
@Controller
public class UserController {

	private final UserService userService;
	
	private final PasswordEncoder passwordEncoder; 
	
	private final EmailService emailService;
	
	@GetMapping("join")
	public void joinForm() {
		log.info("join");
	}
	
	@PostMapping("join")
	public String joinOk(UserVo vo) throws Exception {
		
		
//		log.info("비밀번호: " + passwordEncoder.encode(vo.getU_pwd()));
//		log.info("암호화길이" + "$2a$10$SAMzafj2Hdi3UQoSxiY2eOPAdcdj8VGq/9UPljS.gBHnRNgSLVXcS".length());
		
		//비밀번호 암호화로 변경.
		vo.setMbsp_password(passwordEncoder.encode(vo.getMbsp_password()));
		
		log.info("회원정보: " + vo);
				
		userService.join(vo);
		
		return "redirect:/user/login";

	}
	
	// 아이디중복체크.   ajax요청작업은 리턴타입이 ResponseEntity 사용해야한다. 그리고 @ResponseBody 어노테이션을 사용 할 필요가 없다.
	@GetMapping("/idCheck")
	public ResponseEntity<String> idCheck(String mbsp_id) throws Exception {
		
		log.info("아이디: " + mbsp_id);
		
		ResponseEntity<String> entity = null;
		
		String idUse = "";
		if(userService.idCheck(mbsp_id) != null) {
			idUse = "no"; // 사용불가능
		}else {
			idUse = "yes"; // 사용가능
		}
		
		entity = new ResponseEntity<String>(idUse, HttpStatus.OK);
		
		return entity;
		
	}
	
	//로그인 폼
	@GetMapping("/login")
	public void loginForm() {
		
	}
	
	//로그인체크
	@PostMapping("/login") //파라미터 1)LoginDTO dto   2)String mbsp_id, String mbsp_password
	public String loginOk(LoginDTO dto, HttpSession session, RedirectAttributes rttr) throws Exception {
		
		UserVo vo = userService.login(dto.getMbsp_id());
		
		String msg = "";
		String url = "/";  // "/" 메인주소
		
		if(vo != null) { // 아이디가 존재하는 경우
			//비밀번호 비교
			if(passwordEncoder.matches(dto.getMbsp_password(), vo.getMbsp_password())) {  // 사용자가 입력한 비밀번호가 암호화된 형태에 해당하는 것이라면.
				vo.setMbsp_password("");
				session.setAttribute("login_status", vo);
			}else {  // 사용자가 입력한 비밀번호가 암호화된 형태에 해당하지 않는 것이라면 
				msg = "failPW";
				url = "/user/login"; // 로그인폼 주소
			}
		}else {		     // 아이디가 존재하지 않을 경우
			msg = "failID";
			url = "/user/login";   // 로그인폼 주소
		}
		
		rttr.addFlashAttribute("msg", msg); // thymeleaf에서 msg변수를 사용목적
		
		return "redirect:" + url;  // 메인으로 이동.

	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		
		session.invalidate(); // 세션형태로 관리되는 모든 메모리는 소멸.
		
		return "redirect:/";
	}
	
	// 아이디찾기
	@GetMapping("/idfind")
	public void idfindForm() {
		
	}
	
	@PostMapping("/idfind")
	public String idfindOk(String mbsp_name, String mbsp_email, String authcode, HttpSession session, RedirectAttributes rttr) throws Exception {
		
		String url = "";
		String msg = "";
		
		// 인증코드 확인
		if(authcode.equals(session.getAttribute("authcode"))) {

			// 이름과 메일주소를 확인.
			String mbsp_id = userService.idfind(mbsp_name, mbsp_email);
			if(mbsp_id != null) {
								
				// 아이디를 내용으로 메일발송작업
				String subject = "DocMall 아이디를 보냅니다.";
				EmailDTO dto = new EmailDTO("DocMall", "DocMall", mbsp_email, subject, mbsp_id);
				
				emailService.sendMail("emailIDResult", dto, mbsp_id);
				
				session.removeAttribute("authcode");
				
				msg = "success";
				url = "/user/login";
				rttr.addFlashAttribute("msg", msg);
				
			}else {
				msg = "failID";
				url = "/user/idfind";
			}
			
			
		}else {
			msg = "failAuthCode";
			url = "/user/idfind";
		}
		
		rttr.addFlashAttribute("msg", msg);
		
		return "redirect:" + url;
		
	}
	
	@GetMapping("/pwfind")
	public void pwfindForm() {
		
	}
	
	
	@PostMapping("/pwfind")
	public String pwfindOk(String mbsp_id, String mbsp_name, String mbsp_email, String authcode, HttpSession session, RedirectAttributes rttr) throws Exception {
		
		String url = "";
		String msg = "";
		
		// 인증코드 확인
		if(authcode.equals(session.getAttribute("authcode"))) {
			
			// 사용자가 입력한 3개정보(아이디, 이름, 이메일)를 조건으로 사용하여, 이메일을 디비에서 가져온다.
			String d_email = userService.pwfind(mbsp_id, mbsp_name, mbsp_email);
			if(d_email != null)
			{
				//임시 비밀번호 생성(UUID 이용)
				String tempPw = userService.getTempPw();
				
				//암호화된 비밀번호
				String temp_enc_pw = passwordEncoder.encode(tempPw);
				
				// 암호화된 임시비번을 해당아이디에 업데이트 한다.
				userService.tempPwUpdate(mbsp_id, temp_enc_pw);
				
				EmailDTO dto = new EmailDTO("DocMall", "DocMall", d_email, "DocMall에서 임시비밀번호 보냅니다.", tempPw);
				
				emailService.sendMail("emailPwResult", dto, tempPw);
				
				session.removeAttribute("authcode");
				msg = "success";
				url = "/user/pwfind";
			}else {
				url = "/user/pwfind";
				msg = "failInput";
			}

		}else {
			url = "/user/pwfind";
			msg = "failAuth";
		}
		
		rttr.addFlashAttribute("msg", msg);
		
		return "redirect:" + url;

	}
	
	// 일반로그인 또는 카카오로그인 인지를 체크하는 작업
	@GetMapping("/mypage")
	public void mypage(HttpSession session, Model model) throws Exception {
		
		if(session.getAttribute("login_status") != null) {
			String mbsp_id = ((UserVo) session.getAttribute("login_status")).getMbsp_id();
			UserVo vo = userService.login(mbsp_id);
			model.addAttribute("user", vo);
		} else if(session.getAttribute("kakao_status") != null) {
			
			KakaoUserInfo kakaoUserInfo = (KakaoUserInfo) session.getAttribute("kakao_status");
			
			// MyPage에서 보여줄 정보를 선택적으로 작업.
			UserVo vo = new UserVo();
			vo.setMbsp_name(kakaoUserInfo.getNickname());
			vo.setMbsp_email(kakaoUserInfo.getEmail());

			model.addAttribute("user", vo);
			model.addAttribute("msg", "kakao_login");
			
		}
		
		
		
		
	}
	
	@PostMapping("/modify")
	public String modifyOk(UserVo vo, HttpSession session, RedirectAttributes rttr) throws Exception {
		
//		// 인터셉터 간단히 보충설명할 것.
		if(session.getAttribute("login_status") == null) return "redirect:/userinfo/login";
		
		log.info("회원수정: " + vo);
		
		String mbsp_id = ((UserVo) session.getAttribute("login_status")).getMbsp_id();
		vo.setMbsp_id(mbsp_id);
				
		userService.modify(vo);
		
		rttr.addFlashAttribute("msg", "success");

		return "redirect:/user/mypage";
	}
	
	@GetMapping("/changepw")
	public void changepwForm() {
		
	}
	
	@PostMapping("/changepw")
	public String changepwOk(String cur_mbsp_password, String new_mbsp_password, HttpSession session, RedirectAttributes rttr) {
		
		String mbsp_id = ((UserVo) session.getAttribute("login_status")).getMbsp_id();
		
		UserVo vo = userService.login(mbsp_id);
		
		String msg = "";
		
		if(vo != null) { // 아이디가 존재하는 경우
			//비밀번호 비교
			if(passwordEncoder.matches(cur_mbsp_password, vo.getMbsp_password())) {  // 사용자가 입력한 비밀번호가 암호화된 형태에 해당하는 것이라면.
				
				// 신규비밀번호 변경작업
				String enc_new_mbsp_password = passwordEncoder.encode(new_mbsp_password);
				userService.changePw(mbsp_id, enc_new_mbsp_password);
				msg = "success";
				
				
			}else {  // 사용자가 입력한 비밀번호가 암호화된 형태에 해당하지 않는 것이라면 
				msg = "failPW";
			}
		}
		
		rttr.addFlashAttribute("msg", msg); // jsp에서 msg변수를 사용목적
		
		return "redirect:/user/changepw";
	}
	
	@GetMapping("/delete")
	public void deleteForm() {
		
	}
	
	@PostMapping("/delete")
	public String deleteOk(String mbsp_password, HttpSession session, RedirectAttributes rttr) throws Exception {
		
		String mbsp_id = ((UserVo) session.getAttribute("login_status")).getMbsp_id();
		
		// 비밀번호컬럼 한개만 필요하지만, 로그인정보 사용해도 기능에 문제가 없어 사용함.
		UserVo vo = userService.login(mbsp_id);
		
		String msg = "";
		String url = "/";  // "/" 메인주소
		
		if(vo != null) { // 아이디가 존재하는 경우
			//비밀번호 비교
			if(passwordEncoder.matches(mbsp_password, vo.getMbsp_password())) {  // 사용자가 입력한 비밀번호가 암호화된 형태에 해당하는 것이라면.
				
				//회원탈퇴(삭제)작업
				userService.delete(mbsp_id);
				session.invalidate();
				
			}else {  // 사용자가 입력한 비밀번호가 암호화된 형태에 해당하지 않는 것이라면 
				msg = "failPW";
				url = "/user/delete"; // 회원탈퇴폼 주소
				
				rttr.addFlashAttribute("msg", msg);
			}
		}
		
		
		
		return "redirect:" + url;
	}
	
	
}
