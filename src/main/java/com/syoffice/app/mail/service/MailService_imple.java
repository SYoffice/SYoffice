package com.syoffice.app.mail.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		
//		System.out.println("확인용 recipient : " + recipient);
//		System.out.println("확인용 mail_subject : " + mail_subject);
//		System.out.println("확인용 ccuser : " + ccuser);
//		System.out.println("확인용 mail_content : " + mail_content);
		
		
		
		List<MultipartFile> fileList = mtp_request.getFiles("file_arr");
		
		
		HttpSession session = mtp_request.getSession();
	    String root = session.getServletContext().getRealPath("/");
	    String path = root + "resources"+File.separator+"mail";
		
	    File dir = new File(path);	// path 경로를 가진 file 객체 생성(폴더)
	    if(!dir.exists()) {	// path 폴더가 존재하지 않다면
	    	dir.mkdirs();	// 폴더 생성
	    }
	    
	 // >>>> 첨부파일을 위의 path 경로에 올리기 <<<< //
	    String[] arr_attachFilename = null; // 첨부파일명들을 기록하기 위한 용도
	    
	    if (fileList != null && fileList.size() > 0) {	// 첨부파일이 존재할 때
	    	arr_attachFilename = new String[fileList.size()];
	    	
	    	for (int i=0; i<fileList.size(); i++) {
	    		MultipartFile mtfile = fileList.get(i);
//	    		System.out.println("파일명 : "+ mtfile.getOriginalFilename() +" / 파일크기 : "+ mtfile.getSize());
	    		/*
	    		 	파일명 : LG_싸이킹청소기_사용설명서.pdf 		/ 파일크기 : 3623117
					파일명 : PHILIPS_헤어드라이기_사용설명서.pdf 	/ 파일크기 : 628964
					파일명 : berkelekle심플라운드01.jpg 			/ 파일크기 : 71317
	    		 */
	    		
	    		try {
		    		// === MultipartFile 을 File 로 변환하여 탐색기 저장폴더에 저장하기 시작 === //
		    		File attachFile = new File(path + File.separator + mtfile.getOriginalFilename());
		    		mtfile.transferTo(attachFile);	// !!!!! 이것이 파일을 업로드해주는 것이다. !!!!!!
		    		/*
	                	form 태그로 부터 전송받은 MultipartFile mtfile 파일을 지정된 대상 파일(attachFile)로 전송한다.
	                    만약에 대상 파일(attachFile)이 이미 존재하는 경우 먼저 삭제된다.
		    		 */
		    		// 탐색기에서 /Users/lee/git/servertest/myspring/src/main/webapp/resources/email_attach_file 폴더에 보면
		    		// 첨부한 파일이 생성되어 있음을 확인한다.
		    		
		    		// === MultipartFile 을 File 로 변환하여 탐색기 저장폴더에 저장하기 끝 === //
		    		
		    		// 첨부파일명들을 기록한다.
		    		arr_attachFilename[i] = mtfile.getOriginalFilename();
	    		} catch(Exception e) {
	    			e.printStackTrace();
	    		}
	    	}// end of for() -----------------
	    	
	    }// end of if (fileList != null && fileList.size() > 0) -----------------------
	    
	    String[] recipientArr = recipient.split("[,]");		// 메일 수신인 목록
		
		
		int mailNo = dao.getMailNo(); 	// 메일 번호 채번
		
		// 메일발송 테이블에 insert 
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("sender", sender);
		paraMap.put("mail_subject", mail_subject);
		paraMap.put("mail_content", mail_content);
		paraMap.put("mail_important", mail_important);
		paraMap.put("mailNo", String.valueOf(mailNo));
		
		int n1 = dao.sendMail(paraMap);		// 메일 정보 입력
		
		int n2=0, n3=0;
		
		if (n1 == 1) {
			// 메일수신인 들에게 발송
			
			for (String fk_emp_id : recipientArr) {
		    	// 수신인 수만큼반복 DB insert
				paraMap.put("recipient", fk_emp_id);
				paraMap.put("receive_division", "0");
				paraMap.put("receivercc", "0");
				
		    	n2 += dao.intoMailBox(paraMap);
		    }// end of for() ----------------
			
			if (n2 == recipientArr.length && !"".equals(ccuser)) {
				String[] ccuserArr = ccuser.split("[,]");		// 메일 참조인 목록
				
				for (String cc_user : ccuserArr) {
					paraMap.put("recipient", cc_user);
					paraMap.put("receive_division", "0");
					paraMap.put("receivercc", "1");
					
			    	n3 += dao.intoMailBox(paraMap);
				}// end of for() ---------------
			}
			
			// 첨부파일 정보 DB에 넣어야 함!!!!!!!!!!!!! 인생 망했
		}
		
		
	    
		return null;
	}// end of public String sendMail(MultipartHttpServletRequest mtp_request) ------------------- 
	
	
	
}
