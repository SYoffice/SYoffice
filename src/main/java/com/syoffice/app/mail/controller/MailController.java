package com.syoffice.app.mail.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.syoffice.app.mail.domain.MailVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.syoffice.app.employee.domain.EmployeeVO;
import com.syoffice.app.mail.service.MailService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mail/*")
public class MailController {
	
	@Autowired
	private MailService service;
	
	/*
	 	0: 받은 메일함
	 	1: 보낸 메일함
	 	2: 임시 보관함
	 	3: 내게 쓴 메일함
	 	4: 휴지통
	 	5: 스팸메일함	
	 */
	
	// === 메일박스 === //
	@GetMapping("/box/{division}")
	public ModelAndView mailBox(HttpServletRequest request, ModelAndView mav, @PathVariable String division, @RequestParam(defaultValue = "1") String page) {
		
		try {
			Integer.parseInt(division);
			
			HttpSession session = request.getSession();
			EmployeeVO empvo = (EmployeeVO) session.getAttribute("loginuser");
			
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("division", division);
			paraMap.put("fk_emp_id", empvo.getEmp_id());



			int sizePerPage = 10;	// 페이지당 보일 개수

			paraMap.put("sizePerPage", String.valueOf(sizePerPage));

			int totalPage = service.getMailBoxTotalPage(paraMap);	// 메일박스의 총 페이지수 가져오기

			try {
				if (Integer.parseInt(page) > totalPage || Integer.parseInt(page) <= 0) {
					page = "1";
				}
			} catch (NumberFormatException e) {
				page = "1";
			}
			int showPageNo = Integer.parseInt(page);	// 현재 보는 페이지번호
			

			int startRno = (showPageNo * sizePerPage) - (sizePerPage - 1);		// 페이지 시작번호
			int endRno 	 = (showPageNo * sizePerPage);							// 페이지 끝번호
			
			paraMap.put("startRno", String.valueOf(startRno));
			paraMap.put("endRno", String.valueOf(endRno));
			
			// *** === 페이지 바 만들기 시작 === *** //
			String pageBar = "";
			
			int blockSize = 5;		// 페이지당 보이는 블럭개수
			int loop = 1;			// 블럭 개수 증가 확인 용도
			
			String path = request.getContextPath();
			
			int pageNo  = ( (Integer.parseInt(page) - 1)/blockSize ) * blockSize + 1; 	// pageNo 는 페이지바에서 보여지는 첫번째 번호이다.
			pageBar += "<div class='pagination'>";
			
			// *** [처음]&[이전] 만들기 *** //
			pageBar += "<a href='"+path+"/mail/box/"+division+"?page=1'>&laquo;</a>";
			if (totalPage > blockSize) {
				pageBar += "<a href='"+path+"/mail/box/"+division+"?page="+(pageNo-1)+"'>&lsaquo;</a>"; 
			}
			
			while( !(loop > blockSize || pageNo > totalPage) ) {
				if(pageNo == Integer.parseInt(page)) {
					pageBar += "<a class='active' href='"+path+"/mail/box/"+division+"?page="+pageNo+"'>"+pageNo+"</a>";
				}
				else {
					pageBar += "<a href='"+path+"/mail/box/"+division+"?page="+pageNo+"'>"+pageNo+"</a>";
				}
				
				loop++;   // 1 2 3 4 5 6 7 8 9 10
				pageNo++;
			}// end of while( !(loop > blockSize || pageNo > totalPage) 
			
			// *** [다음]&[마지막] 만들기 *** //
			
			if (totalPage > blockSize) {
				pageBar += "<a href='"+path+"/mail/box/"+division+"?page="+pageNo+"'>&rsaquo;</a>"; 
			}
			pageBar += "<a href='"+path+"/mail/box/"+division+"?page="+totalPage+"'>&raquo;</a>";
			pageBar += "</div>";
			// *** === 페이지 바 만들기 끝 === *** //
			
			
			List<Map<String, String>> mailList	= service.getMailBox(paraMap);		// 메일함목록 가져오기
//			Map<String, Integer> mailCntMap 	= service.getMailCount(paraMap);	// 안읽은메일, 전체메일 개수 가져오기
			
//			System.out.println(mailList.toString());
			
			mav.addObject("mailList", mailList);
//			mav.addObject("mailCntMap", mailCntMap);
			mav.addObject("pageBar", pageBar);
			mav.addObject("division", division);
			mav.setViewName("/mail/mailbox_"+division);
			
		} catch (NumberFormatException e) {
			mav.addObject("message", "메일함이 존재하지 않습니다.\\n받은메일함으로 이동합니다.");
			mav.addObject("loc", request.getContextPath()+"/mail/box/0");
			mav.setViewName("/common/msg");
		}
		
		return mav;
	}// end of public ModelAndView mailBox(HttpServletRequest request, ModelAndView mav, @PathVariable String division) -------------------- 
	
	
	// === 메일쓰기 페이지 === //
	@GetMapping("new")
	public String mailWrite() {
		return "/mail/mailwrite";
	}// end of public String mailWrite() -------------------- 
	

	// === 메일 상세보기 === //
	@GetMapping("{fk_mail_no}")
	public ModelAndView mailViewDetail(HttpServletRequest request, ModelAndView mav, @PathVariable String fk_mail_no, @RequestParam String division) {
		try {
			Integer.parseInt(fk_mail_no);

			HttpSession session = request.getSession();
			EmployeeVO empvo = (EmployeeVO) session.getAttribute("loginuser");

			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("fk_emp_id", empvo.getEmp_id());
			paraMap.put("fk_mail_no", fk_mail_no);
			paraMap.put("division", division);

			List<MailVO> mailVOList 	= service.getMailDetail(paraMap);			// 수신인들 정보
			List<MailVO> mailVOFileList = service.getMailAttachFile(paraMap);		// 발신인, 첨부파일 정보
			Map<String, String> prevNextMailMap = service.getPrevNextMail(paraMap);	// 이전, 다음메일 조회해오기



			if (mailVOList == null || mailVOList.size() == 0) {
				// URL 로 없는 것을 조회한 경우
				mav.setViewName("redirect:/mail/box/0");
			}
			else {
				mav.addObject("mailVOList", mailVOList);
				mav.addObject("mailVOFileList", mailVOFileList);
				mav.addObject("prevNextMailMap", prevNextMailMap);
				mav.setViewName("/mail/maildetail");
			}

		} catch (NumberFormatException e) {
			mav.setViewName("redirect:/mail/box/0");
		}

		return mav;
	}// end of public ModelAndView mailViewDetail(HttpServletRequest request, ModelAndView mav, @PathVariable String fk_mail_no) -------------------------------


	// === 메일 첨부파일 다운로드 === //
	@GetMapping("/file/{atmail_no}")
	public void mailFileDownload(HttpServletRequest request, HttpServletResponse response, @PathVariable String atmail_no) {
		response.setContentType("text/html; charset=UTF-8");	// html 로 결과를 보여줄 것이며 문자인코딩은 UTF-8 이다.
		PrintWriter out = null;

		try{
			Integer.parseInt(atmail_no);

			service.downloadMailOneAttachFile(request, response, atmail_no);

		} catch (NumberFormatException e) {
			try {
				out = response.getWriter();
				out.println("<script type='text/javascript'>alert('비정상적인 접근입니다.'); history.back();</script>");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}// end of public void mailFileDownload(HttpServletRequest request, HttpServletResponse response, @PathVariable String atmail_no) ---------------------------

	
	// === 임시저장된 메일 클릭 시 새로운 메일 작성 폼으로 이동 === //
	@GetMapping("/new/{fk_mail_no}")
	public ModelAndView mailNewWrite(ModelAndView mav, @PathVariable String fk_mail_no) {
		mav.addObject("fk_mail_no", fk_mail_no);
		mav.setViewName("mail/mailedit");
		return mav;
	}// end of public ModelAndView mailNewWrite(ModelAndView mav, @PathVariable String fk_mail_no) ---------------------- 
	
}
