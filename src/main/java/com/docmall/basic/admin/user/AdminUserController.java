package com.docmall.basic.admin.user;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.docmall.basic.common.constants.Constants;
import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.common.dto.PageDTO;
import com.docmall.basic.common.util.FileManagerUtils;
import com.docmall.basic.mail.EmailDTO;
import com.docmall.basic.mail.EmailService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/user/*")
public class AdminUserController {
	
	private final AdminUserService adminUserService;
	
	private final EmailService emailService;
	
	//CKeditor 파일업로드 경로
	@Value("${file.ckdir}")
	private String uploadCKPath;

	// https://www.onedaynet.co.kr/ 사이트 참고
	
	// 회원목록(검색,페이징
	
	// 회원조회및수정
	
	// 메일발송 목록
	@GetMapping("/mailinglist")
	public void mailnglist(Criteria cri,String title, Model model) throws Exception {
		
		List<MailMngVO> maillist = adminUserService.getMailinfoList(cri, title);
		
		int totalcount = adminUserService.getMailListCount(title);
		PageDTO pageDTO = new PageDTO(cri,totalcount );
		
		model.addAttribute("maillist", maillist);
		model.addAttribute("pageMaker", pageDTO);
	}
	
	// 스프링기본
	// 일반메서드를 호출 하는 경우에는 파라미터(매개변수)값을 제공해야 한다.
	// 주소에 의하여 호출되는 메서드는 파라미터(매개변수)를 스프링이 관여하여 객체를 먼저 생성한다. 그리고 사용자가 입력한 값이 setter 메서드에 의하여 객체에 저장된다.
	// 메일발송폼(CkEditer) - 구분 1.광고/이벤트 2. 일반
	@GetMapping("/mailingform")
	public void mailform(@ModelAttribute("vo") MailMngVO vo) {
		
	}
	
	// 메일저장
	@PostMapping("/mailingsave")   
	public String mailingsave(@ModelAttribute("vo") MailMngVO vo, Model model ,RedirectAttributes rttr) throws Exception {
		log.info("메일내용: " + vo);
		
		// 메일정보 DB저장
		adminUserService.mailing_save(vo); // vo 참조(주소)값
		
		log.info("시퀀스: " + vo.getIdx());
		
		model.addAttribute("idx", vo.getIdx()); // 메일보내기 횟수작업 사용
		
//		rttr.addFlashAttribute("msg", "save");  // redirect 사용시 적용됨.
		
		model.addAttribute("msg", "save");
		
		return "/admin/user/mailingform"; //  redirect 사용 안할 경우에는 주소가 아니라 타입리프 파일명으로 해석된다. 
		
		
	}
	
	
	// 메일발송 프로세서
	@PostMapping("/mailingsend")
	public String mailprocess(MailMngVO vo, RedirectAttributes rttr) throws Exception {
		
		log.info("메일내용: " + vo);
		
		// 1) 메일발송
		// 1.1) 회원테이블에서 전체회원 메일정보를 읽어오는 작업
		String[] emailArr = adminUserService.getALLMeilAddress();
		
		EmailDTO dto = new EmailDTO("DocMall", "DocMall", "", vo.getTitle(), vo.getContent());
		
		emailService.sendMail(dto,emailArr);
		
		// 메일발송 횟수 업데이트
		adminUserService.mailSendCountUpdate(vo.getIdx());
		
		rttr.addFlashAttribute("msg", "send");
		
		return "redirect:/admin/user/mailingform";
		
	}
	
	// 메일목록에서 메일발송
	@GetMapping("/mailingsendform")
	public void mailsendform(int idx, Model model) throws Exception {
		
		MailMngVO vo = adminUserService.getMailSendinfo(idx);
		
		model.addAttribute("vo", vo);
	}
	
	// 수정
	@PostMapping("/mailingedit")
	public String mailingedit(@ModelAttribute("vo") MailMngVO vo, Model model) throws Exception {
		
		// db수정작업
		adminUserService.mailingedit(vo);
		
		model.addAttribute("msg", "modify");
		
		return "/admin/user/mailingsendform";
	}
	
	
	// CKEditor 상품설명 이미지 업로드
		// MultipartFile upload : CKeditor의 업로드탭에서 나온 파일첨부 <input type="file" name="upload"> 을 참조함.
		// HttpServletRequest request : 클라이언트의 요청정보를 가지고 있는 객체.
		// HttpServletResponse response : 서버에서 클라이언트에게 보낼 정보를 응답하는 객체
		@PostMapping("/imageupload")
		public void imageupload(HttpServletRequest request, HttpServletResponse response, MultipartFile upload) {
			
			OutputStream out = null;
			PrintWriter printWriter = null; // 서버에서 클라이언트에게 응답정보를 보낼때 사용.(업로드한 이미지정보를 브라우저에게 보내는 작업용도)
			
			try {
				//1)CKeditor를 이용한 파일업로드 처리.
				String fileName = upload.getOriginalFilename(); // 업로드 할 클라이언트 파일이름
				byte[] bytes = upload.getBytes(); // 업로드 할 파일의 바이트배열
				
				String ckUploadPath = uploadCKPath + fileName; // "C:\\Dev\\upload\\ckeditor\\" + "abc.gif"
				
				out = new FileOutputStream(ckUploadPath); // "C:\\Dev\\upload\\ckeditor\\abc.gif" 파일생성. 0 byte
				
				out.write(bytes); // 빨대(스트림)의 공간에 업로드할 파일의 바이트배열을 채운상태.
				out.flush(); // "C:\\Dev\\upload\\ckeditor\\abc.gif" 0 byte -> 크기가 채워진 정상적인 파일로 인식.
				
				//2)업로드한 이미지파일에 대한 정보를 클라이언트에게 보내는 작업
				
				printWriter = response.getWriter();
				
				// 한글파일 인코딩 설정문제 발생.  Constants.ROOR_URL: 이메일에 이미지 안깨지게 하는 설정
				String fileUrl = Constants.ROOR_URL + "/admin/product/display/" + fileName; // 매핑주소/이미지파일명
//					String fileUrl = fileName;
				
				
				// Ckeditor 4.12에서는 파일정보를 다음과 같이 구성하여 보내야 한다.
				// {"filename" : "abc.gif", "uploaded":1, "url":"/ckupload/abc.gif"}
				// {"filename" : 변수, "uploaded":1, "url":변수}
				printWriter.println("{\"filename\" :\"" + fileName + "\", \"uploaded\":1,\"url\":\"" + fileUrl + "\"}"); // 스트림에 채움.
				printWriter.flush();
				
			}catch(Exception ex) {
				ex.printStackTrace();
			}finally {
				if(out != null) {
					try {
						out.close();
					}catch(Exception ex) {
						ex.printStackTrace();
					}
				}
				if(printWriter != null) printWriter.close();
			}
		}
		
		@GetMapping("/display/{fileName}")
		public ResponseEntity<byte[]> getFile(@PathVariable("fileName") String fileName) {
			
			log.info("파일이미지: " + fileName);
			
			
			ResponseEntity<byte[]> entity = null;
			
			try {
				entity = FileManagerUtils.getFile(uploadCKPath, fileName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return entity;
			
		}
}
