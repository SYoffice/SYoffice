package com.syoffice.app.mail.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.syoffice.app.mail.domain.MailVO;
import com.syoffice.app.mail.service.MailService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/mail/*")
@RestController
public class ApiMailController {
	
	@Autowired
	private MailService service;
	
	
	// === 조직도 활용한 수신,참조인 추가용 회원 한 명의 정보를 가져오기 === //
	@GetMapping("getEmployeeInfo")
	public Map<String, String> getEmployeeInfo(@RequestParam Long emp_id) {
		return service.getEmployeeInfo(emp_id);
	}// end of public EmployeeVO getEmployeeInfo(@RequestParam Long emp_id) ---------------------- 
		
	
	// === 메일 전송하기 (타인) === //
	@PostMapping("sendMail")
	public String sendMail(MultipartHttpServletRequest mtp_request) {
		return service.sendMail(mtp_request);
	}// end of public String sendMail(MultipartHttpServletRequest mtp_request) -------------------- 
	
	
	// === 메일 삭제하기(휴지통으로 이동) === //
	@DeleteMapping("{mail_no}")
	public String deleteMail(@RequestParam String mail_no, @RequestParam String fk_emp_id) {
		return service.deleteMail(mail_no, fk_emp_id);
	}// end of public String deleteMail(@RequestParam String mail_no) ----------------------------- 
	
	
	// === 메일 전송하기 (자신) === //
	@PostMapping("sendMailToMe")
	public String sendMailToMe(MultipartHttpServletRequest mtp_request) {
		return service.sendMailToMe(mtp_request);
	}// end of public String sendMailToMe(MultipartHttpServletRequest mtp_request) ---------------------- 
	
	
	// === 메일 임시저장 === //
	@PostMapping("mailStoreTemp")
	public String mailStoreTemp(MultipartHttpServletRequest mtp_request) {
		return service.mailStoreTemp(mtp_request);
	}// end of public String mailStoreTemp(MultipartHttpServletRequest mtp_request) ------------------------- 
	
	
	// === 메일 수신인 정보 가져오기 === //
	@GetMapping("{mail_no}")
	public String getMailRecipientInfo(@PathVariable String mail_no, @RequestParam String fk_emp_id) {
		return service.getMailRecipientInfo(mail_no, fk_emp_id);
	}// end of public String getMailInfo(@RequestParam String mail_no) ------------------------ 
	
	
	// === 메일 작성정보 가져오기(첨부파일포함) === //
	@GetMapping("/file/{mail_no}")
	public MailVO getMailInfo(@PathVariable String mail_no, @RequestParam String fk_emp_id) {
		return service.getMailInfo(mail_no, fk_emp_id);
	}// end of public String getMailInfo(@RequestParam String mail_no) ------------------------ 
	
	
	// === 메일 임시저장 (수정) === //
	@PutMapping("mailStoreTemp")
	public String mailStoreTemp(HttpServletRequest request, MailVO mailVO) {
		return service.mailStoreTemp(request, mailVO);
	}// end of public String mailStoreTemp(MultipartHttpServletRequest mtp_request) ------------------------- 
	
	
	// === 휴지통, 스팸메일 개수 알아오기 === //
	@GetMapping("trash")
	public List<Map<String, Integer>> getMailTashCnt(@RequestParam String fk_emp_id) {
		return service.getMailTashCnt(fk_emp_id);
	}// end of public String getMailTashCnt() ------------------- 
	
	
	// === 휴지통, 스팸메일함 비우기 === //
	@DeleteMapping("trash")
	public Map<String, Integer> emptyMailBox(@RequestParam String receive_division, @RequestParam String fk_emp_id) {
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("receive_division", receive_division);
		paraMap.put("fk_emp_id", fk_emp_id);
		
		return service.emptyMailBox(paraMap);
	}// end of public Map<String, Integer> emptyMailBox(@RequestParam String receive_division, @RequestParam String fk_emp_id) ----------------- 
	
	
	// === 임시저장 메일 삭제하기(휴지통으로 이동) === //
	@DeleteMapping("/temp/{mail_no}")
	public String deleteTempMail(@RequestParam String mail_no, @RequestParam String fk_emp_id) {
		return service.deleteTempMail(mail_no, fk_emp_id);
	}// end of public String deleteMail(@RequestParam String mail_no) ----------------------------- 
	
}	
