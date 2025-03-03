package com.syoffice.app.mail.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.syoffice.app.common.FileManager;
import com.syoffice.app.mail.domain.MailAttachVO;
import com.syoffice.app.mail.domain.MailVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.syoffice.app.mail.model.MailDAO;

import jakarta.servlet.http.HttpSession;

@Service
public class MailService_imple implements MailService {
	
	@Autowired
	private MailDAO dao;

	@Autowired
	private FileManager fileManager;


	// === 메일박스 가져오기 === //
	@Override
	public List<Map<String, String>> getMailBox(Map<String, String> paraMap) {
		List<Map<String, String>> mailList = dao.getMailBox(paraMap);
		return mailList;
	}// end of public List<Map<String, String>> getMailBox(String division) ------------------ 


	// === 메일박스의 총 페이지수 가져오기 === //
	@Override
	public int getMailBoxTotalPage(Map<String, String> paraMap) {
		int n = dao.getMailBoxTotalPage(paraMap);
		return n;
	}// end of public int getMailBoxTotalPage(Map<String, String> paraMap) ------------------ 


	// === 메일의 개수 가져오기 === //
	@Override
	public Map<String, Integer> getMailCount(Map<String, String> paraMap) {
		Map<String, Integer> mailCntMap = dao.getMailCount(paraMap);
		return mailCntMap;
	}// end of public Map<String, Integer> getMailCount(Map<String, String> paraMap) ----------------------


	// === 조직도 활용한 수신,참조인 추가용 회원 한 명의 정보를 가져오기 === //
	@Override
	public Map<String, String> getEmployeeInfo(Long emp_id) {
		return dao.getEmployeeInfo(emp_id);
	}// end of public EmployeeVO getEmployeeInfo(Long emp_id) ------------------------ 


	// === 메일 전송하기 (타인) === //
	@Override
	@Transactional(value = "transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor= {Throwable.class})
	public String sendMail(MultipartHttpServletRequest mtp_request) {
		String recipient = mtp_request.getParameter("recipient");	// 수신인
		String ccuser = mtp_request.getParameter("ccuser");			// 참조인
		String sender = mtp_request.getParameter("sender");			// 발신인
		String mail_subject = mtp_request.getParameter("mail_subject");		// 메일 제목
		String mail_content = mtp_request.getParameter("mail_content");		// 메일 내용
		String mail_important = mtp_request.getParameter("mail_important");	// 중요메일여부

//		System.out.println("확인용 ccuser : "+ ccuser);
//		System.out.println("확인용 recipient : "+ recipient);

	    String[] recipientArr = recipient.split("[,]");		// 메일 수신인 목록

		int mailNo = dao.getMailNo(); 	// 메일 번호 채번
		
		// 메일발송 테이블에 insert 
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("sender", sender);
		paraMap.put("mail_subject", mail_subject);
		paraMap.put("mail_content", mail_content);
		paraMap.put("mail_important", mail_important);
		paraMap.put("mailNo", String.valueOf(mailNo));

		int n1 = dao.sendMail(paraMap);	// 메일 정보 입력
		
		int n2=0, n3=0, result=0;
		
		if (n1 == 1) {
			// 메일수신인 들에게 발송
			for (String fk_emp_id : recipientArr) {
		    	// 수신인 수만큼반복 DB insert
				paraMap.put("recipient", fk_emp_id);
				paraMap.put("receive_division", "0");
				paraMap.put("receivercc", "0");
				
		    	n2 += dao.intoMailBox(paraMap);
		    }// end of for() ----------------

			if (n2 == recipientArr.length) {result = 1;}

			if (n2 == recipientArr.length && !"".equals(ccuser.trim())) {
				result = 0;
				String[] ccuserArr = ccuser.split("[,]");		// 메일 참조인 목록
				
				for (String cc_user : ccuserArr) {
					paraMap.put("recipient", cc_user);
					paraMap.put("receive_division", "0");
					paraMap.put("receivercc", "1");
					
			    	n3 += dao.intoMailBox(paraMap);
				}// end of for() ---------------

				if (n3 == ccuserArr.length) {result = 1;}
			}
			// 첨부파일 정보 DB에 넣어야 함!!!!!!!!!!!!! 인생 망했
		}

//		fileUpload(mtp_request, mailNo);

		List<MultipartFile> fileList = mtp_request.getFiles("file_arr");

		if (fileList != null && fileList.size() > 0) {	// 첨부파일이 존재할 때

			HttpSession session = mtp_request.getSession();
			String root = session.getServletContext().getRealPath("/");
			String path = root + "resources"+File.separator+"mail";


			File dir = new File(path);	// path 경로를 가진 file 객체 생성(폴더)
			if(!dir.exists()) {	// path 폴더가 존재하지 않다면
				dir.mkdirs();	// 폴더 생성
			}

			// >>>> 첨부파일을 위의 path 경로에 올리기 <<<< //

			for (int i=0; i<fileList.size(); i++) {
				MultipartFile mtfile = fileList.get(i);
				Map<String, Object> paraFileMap = new HashMap<>();
//	    		System.out.println("파일명 : "+ mtfile.getOriginalFilename() +" / 파일크기 : "+ mtfile.getSize());
	    		/*
	    		 	파일명 : LG_싸이킹청소기_사용설명서.pdf 		/ 파일크기 : 3623117
					파일명 : PHILIPS_헤어드라이기_사용설명서.pdf 	/ 파일크기 : 628964
					파일명 : berkelekle심플라운드01.jpg 			/ 파일크기 : 71317
	    		 */
				byte[] bytes = null;	// 첨부파일의 내용을 담음

				try {
					bytes = mtfile.getBytes();
					String originalFilename = mtfile.getOriginalFilename();
					String newFileName = fileManager.doFileUpload(bytes, originalFilename, path);
					long fileSize = mtfile.getSize();	// 첨부파일의 크기

					paraFileMap.put("newFileName", newFileName);
					paraFileMap.put("originalFilename", originalFilename);
					paraFileMap.put("fileSize", fileSize);
					paraFileMap.put("mailNo", mailNo);

					dao.insertAttachFile(paraFileMap);		// 첨부파일 정보를 DB에 저장.

				} catch(Exception e) {
					e.printStackTrace();
				}
			}// end of for() -----------------

		}// end of if (fileList != null && fileList.size() > 0) -----------------------


		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result", result);
	    
		return jsonObject.toString();
	}// end of public String sendMail(MultipartHttpServletRequest mtp_request) -------------------


	// === 메일 상세조회하기 === //
	@Override
	public List<MailVO> getMailDetail(Map<String, String> paraMap) {
		List<MailVO> mailVOList = dao.getMailDetail(paraMap);

		if (mailVOList != null && mailVOList.size() > 0) {
			dao.mailRead(paraMap);	// 메일 읽음처리
			return mailVOList;
		}
		return null;
	}// end of public MailVO getMailDetail(String fkMailNo) --------------------


	// === 메일 첨부파일, 발신인 정보 가져오기 === //
	@Override
	public List<MailVO> getMailAttachFile(Map<String, String> paraMap) {
		List<MailVO> getMailAttachFile = dao.getMailAttachFile(paraMap);
		return getMailAttachFile;
	}// end of public List<MailVO> getMailAttachFile(Map<String, String> paraMap) ------------------------


	// === 한개의 첨부파일 정보를 가져오기 === //
	@Override
	public void downloadMailOneAttachFile(HttpServletRequest request, HttpServletResponse response, String atmailNo) {
		response.setContentType("text/html; charset=UTF-8");	// html 로 결과를 보여줄 것이며 문자인코딩은 UTF-8 이다.
		PrintWriter out = null;

		MailAttachVO mailAttachVO = dao.getMailOneAttachFile(atmailNo);

		try {
			if (mailAttachVO == null || mailAttachVO.getAtmail_filename() == null) {
				// 없는 메일 혹은 파일번호를 요청할 경우
				out = response.getWriter();
				out.println("<script type='text/javascript'>alert('파일다운로드가 불가합니다.'); history.back();</script>");
				return;
			}

			String fileName = mailAttachVO.getAtmail_filename();
			String originalFilename = mailAttachVO.getAtmail_orgfilename();

			HttpSession session = request.getSession();
			String root = session.getServletContext().getRealPath("/");    // WAS에 업로드 된 경로

			String path = root + "resources" + File.separator + "mail";

			boolean flag = false;    // file 다운로드 성공, 실패 여부를 알려주는 용도

			flag = fileManager.doFileDownload(fileName, originalFilename, path, response);

			if(!flag) {
				// 다운로드가 실패한 경우 메시지를 띄운다.
				out = response.getWriter();
				// out 은 웹브라우저에 기술하는 대상체(웹 화면이라 생각)

				out.println("<script type='text/javascript'>alert('파일다운로드가 실패되었습니다.'); history.back();</script>");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}// end of public void downloadMailOneAttachFile(HttpServletRequest request, HttpServletResponse response, String atmailNo) ----------------


}
