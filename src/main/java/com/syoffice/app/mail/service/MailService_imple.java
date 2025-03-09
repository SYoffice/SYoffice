package com.syoffice.app.mail.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.syoffice.app.common.FileManager;
import com.syoffice.app.employee.domain.EmployeeVO;
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
	
	// === 첨부파일 업로드하기 === //
	private void fileUpload(MultipartHttpServletRequest mtp_request, int mailNo) {
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
//		    		System.out.println("파일명 : "+ mtfile.getOriginalFilename() +" / 파일크기 : "+ mtfile.getSize());
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
		
	}// end of private void fileUpload(MultipartHttpServletRequest mtp_request, int mailNo)  ---------------------------


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

//		System.out.println("확인용 sender : "+ sender);
//		System.out.println("확인용 mail_subject : "+ mail_subject);
//		System.out.println("확인용 mail_content : "+ mail_content);
//		System.out.println("확인용 ccuser : "+ ccuser);
//		System.out.println("확인용 recipient : "+ recipient);
//		System.out.println("확인용 mail_important : "+ mail_important);

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
				paraMap.put("receive_status", "0");
				paraMap.put("receivercc", "0");
				
		    	n2 += dao.intoMailBox(paraMap);
		    }// end of for() ----------------

			if (n2 == recipientArr.length) {
				HttpSession session = mtp_request.getSession();
				EmployeeVO empvo = (EmployeeVO) session.getAttribute("loginuser");
								
				paraMap.put("recipient", empvo.getEmp_id());
				paraMap.put("receive_division", "1");	// 보낸 메일함에 등록
				paraMap.put("receive_status", "1");		//  읽은 상태
				paraMap.put("receivercc", "0");
				
				result = dao.intoMailBox(paraMap);
			}

			if (n2 == recipientArr.length && !"".equals(ccuser.trim())) {
				result = 0;
				String[] ccuserArr = ccuser.split("[,]");		// 메일 참조인 목록
				
				for (String cc_user : ccuserArr) {
					paraMap.put("recipient", cc_user);
					paraMap.put("receive_division", "0");
					paraMap.put("receive_status", "0");
					paraMap.put("receivercc", "1");
					
			    	n3 += dao.intoMailBox(paraMap);
				}// end of for() ---------------

				if (n3 == ccuserArr.length) {result = 1;}
			}
			// 첨부파일 정보 DB에 넣어야 함!!!!!!!!!!!!! 인생 망했
		}

		fileUpload(mtp_request, mailNo);

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

	
	// === 메일 삭제하기(휴지통으로 이동) === //
	@Override
	public String deleteMail(String mail_no, String fk_emp_id) {
		int n = dao.deleteMail(mail_no, fk_emp_id);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result", n);
		
		return jsonObject.toString();
	}// end of public String deleteMail(String mail_no) ------------------------- 

	
	
	// === 메일 전송하기 (자신) === //
	@Override
	public String sendMailToMe(MultipartHttpServletRequest mtp_request) {
		String recipient = mtp_request.getParameter("recipient");			// 수신인
		String sender = mtp_request.getParameter("sender");					// 발신인
		String mail_subject = mtp_request.getParameter("mail_subject");		// 메일 제목
		String mail_content = mtp_request.getParameter("mail_content");		// 메일 내용
		String mail_important = mtp_request.getParameter("mail_important");	// 중요메일여부
		
		int mailNo = dao.getMailNo(); 	// 메일 번호 채번
		
		// 메일발송 테이블에 insert 
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("sender", sender);
		paraMap.put("mail_subject", mail_subject);
		paraMap.put("mail_content", mail_content);
		paraMap.put("mail_important", mail_important);
		paraMap.put("mailNo", String.valueOf(mailNo));
		
		int n1 = dao.sendMail(paraMap);	// 메일 정보 입력
		
		int result = 0;
		
		if (n1 == 1) {
			paraMap.put("recipient", recipient);
			paraMap.put("receive_division", "3");
			paraMap.put("receive_status", "0");
			paraMap.put("receivercc", "0");
			
			result = dao.intoMailBox(paraMap);
		}
		
		fileUpload(mtp_request, mailNo);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result", result);
	    
		return jsonObject.toString();
	}// end of public String sendMailToMe(MultipartHttpServletRequest mtp_request) ------------------ 


	// === 메일 임시저장 === //
	@Override
	@Transactional(value = "transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor= {Throwable.class})
	public String mailStoreTemp(MultipartHttpServletRequest mtp_request) {
		String recipient = mtp_request.getParameter("recipient");	// 수신인
		String ccuser = mtp_request.getParameter("ccuser");			// 참조인
		String sender = mtp_request.getParameter("sender");			// 발신인
		String mail_subject = mtp_request.getParameter("mail_subject");		// 메일 제목
		String mail_content = mtp_request.getParameter("mail_content");		// 메일 내용
		String mail_important = mtp_request.getParameter("mail_important");	// 중요메일여부

//		System.out.println("mail_important: "+ mail_important);
		
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
				paraMap.put("receive_division", "9");	// 임시 저장용
				paraMap.put("receive_status", "1");		// 임시 저장용
				paraMap.put("receivercc", "0");
				
		    	n2 += dao.intoMailBox(paraMap);
		    }// end of for() ----------------

			if (n2 == recipientArr.length) {
				HttpSession session = mtp_request.getSession();
				EmployeeVO empvo = (EmployeeVO) session.getAttribute("loginuser");
								
				paraMap.put("recipient", empvo.getEmp_id());
				paraMap.put("receive_division", "2");	// 임시 보관 메일함에 등록
				paraMap.put("receive_status", "1");		// 읽은 상태
				paraMap.put("receivercc", "0");
				
				result = dao.intoMailBox(paraMap);
			}

			if (n2 == recipientArr.length && !"".equals(ccuser.trim())) {
				result = 0;
				String[] ccuserArr = ccuser.split("[,]");		// 메일 참조인 목록
				
				for (String cc_user : ccuserArr) {
					paraMap.put("recipient", cc_user);
					paraMap.put("receive_division", "9");	
					paraMap.put("receive_status", "1");
					paraMap.put("receivercc", "1");
					
			    	n3 += dao.intoMailBox(paraMap);
				}// end of for() ---------------

				if (n3 == ccuserArr.length) {result = 1;}
			}
			// 첨부파일 정보 DB에 넣어야 함!!!!!!!!!!!!! 인생 망했
		}

//		fileUpload(mtp_request, mailNo);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result", result);
	    
		return jsonObject.toString();
	}// end of public String mailStoreTemp(MultipartHttpServletRequest mtp_request) ------------------- 


	// === 메일 수신인 정보 가져오기 === //
	@Override
	public String getMailRecipientInfo(String mail_no, String fk_emp_id) {
		
//		System.out.println("mail_no: "+ mail_no);
//		System.out.println("fk_emp_id: "+ fk_emp_id);
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("fk_emp_id", fk_emp_id);
		paraMap.put("fk_mail_no", mail_no);
		
		List<MailVO> mailVOList = dao.getMailDetail(paraMap);
		
		if (mailVOList != null && mailVOList.size() > 0) {			
			JsonArray jsonArr = new JsonArray();
			for (MailVO mailvo: mailVOList) {
				JsonObject jsonObj = new JsonObject();
				
				jsonObj.addProperty("receive_no", mailvo.getReceive_no());
				jsonObj.addProperty("receiver", mailvo.getReceiver());
				jsonObj.addProperty("receivercc", mailvo.getReceivercc());
				jsonObj.addProperty("receiver_name", mailvo.getReceiver_name());
				jsonObj.addProperty("receiver_mail", mailvo.getReceiver_mail());
				jsonObj.addProperty("receive_division", mailvo.getReceive_division());
				jsonObj.addProperty("receive_status", mailvo.getReceive_status());

				jsonArr.add(jsonObj);
			}// end of for() ----------------------
			
			return new Gson().toJson(jsonArr);
		}
			
		return null;
	}// end of public String getMailInfo(String mail_no, String fk_emp_id) --------------------------- 


	// === 메일 작성정보 가져오기 === //
	@Override
	public MailVO getMailInfo(String mail_no, String fk_emp_id) {
		MailVO mailVO = dao.getMailInfoOne(mail_no);
		return mailVO;
	}// end of public String getMailFileInfo(String mail_no, String fk_emp_id) -------------------- 


	// === 메일 임시저장 (수정) === //
	@Override
	public String mailStoreTemp(HttpServletRequest request, MailVO mailVO) {
		String recipient = request.getParameter("recipient");	// 수신인
		String ccuser = request.getParameter("ccuser");			// 참조인
		String sender = request.getParameter("sender");			// 발신인
		String mail_subject = request.getParameter("mail_subject");		// 메일 제목
		String mail_content = request.getParameter("mail_content");		// 메일 내용
		String mail_important = request.getParameter("mail_important");	// 중요메일여부
		String mail_no = request.getParameter("mail_no");		// 메일 번호
		
		String[] recipientArr = recipient.split("[,]");		// 메일 수신인 목록
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("sender", sender);
		paraMap.put("mail_subject", mail_subject);
		paraMap.put("mail_content", mail_content);
		paraMap.put("mail_important", mail_important);
		paraMap.put("mailNo", mail_no);
		
		int n = dao.updateMail(paraMap);	// 메일 정보 수정
		int m=0, k=0, result=0;
		if (n == 1) {
			// 수정 성공 시 기존 수신 정보 삭제 후 새로 넣기
			dao.deleteMailReciptInfo(mail_no);
			// 메일수신인 들에게 발송
			for (String fk_emp_id : recipientArr) {
		    	// 수신인 수만큼반복 DB insert
				paraMap.put("recipient", fk_emp_id);
				paraMap.put("receive_division", "9");	// 임시 저장용
				paraMap.put("receive_status", "1");		// 임시 저장용
				paraMap.put("receivercc", "0");
				
		    	m += dao.intoMailBox(paraMap);
		    }// end of for() ----------------

			if (m == recipientArr.length) {
				HttpSession session = request.getSession();
				EmployeeVO empvo = (EmployeeVO) session.getAttribute("loginuser");
								
				paraMap.put("recipient", empvo.getEmp_id());
				paraMap.put("receive_division", "2");	// 임시 보관 메일함에 등록
				paraMap.put("receive_status", "1");		// 읽은 상태
				paraMap.put("receivercc", "0");
				
				result = dao.intoMailBox(paraMap);
			}

			if (m == recipientArr.length && !"".equals(ccuser.trim())) {
				result = 0;
				String[] ccuserArr = ccuser.split("[,]");		// 메일 참조인 목록
				
				for (String cc_user : ccuserArr) {
					paraMap.put("recipient", cc_user);
					paraMap.put("receive_division", "9");	
					paraMap.put("receive_status", "1");
					paraMap.put("receivercc", "1");
					
			    	k += dao.intoMailBox(paraMap);
				}// end of for() ---------------

				if (k == ccuserArr.length) {result = 1;}
			}
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result", result);
	    
		return jsonObject.toString();
	}// end of public String mailStoreTemp(HttpServletRequest request) ----------------- 


	// === 휴지통, 스팸메일 개수 알아오기 === //
	@Override
	public List<Map<String, Integer>> getMailTashCnt(String fk_emp_id) {
		return dao.getMailTashCnt(fk_emp_id);
	}// end of public String getMailTashCnt(String fk_emp_id) ----------------------- 

	
	// === 휴지통, 스팸메일함 비우기 === //
	@Override
	public Map<String, Integer> emptyMailBox(Map<String, String> paraMap) {
		
//		List<Map<String, Integer>> mailNoList = dao.getDeleteMailNo(paraMap);
		
		int n = dao.emptyMailBox(paraMap);
		
//		int result = 0;
//		if (mailNoList.size() == n) {
//			for(int i=0; i<mailNoList.size(); i++) {
//				result += dao.deletePermanentMail(mailNoList.get(i).get("mail_no"));
//			}// end of for() -------------------
//		}
		
		Map<String, Integer> resultMap = new HashMap<>();
		resultMap.put("result", n);
		return resultMap;
	}// end of public Map<String, Integer> emptyMailBox(Map<String, String> paraMap) --------------------- 


	// === 임시저장 메일 삭제(휴지통으로 이동) === //
	@Override
	public String deleteTempMail(String mail_no, String fk_emp_id) {
		
		int n = dao.deleteMail(mail_no, fk_emp_id);
		
		if (n == 1) {
			dao.deleteTempMail(mail_no);
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result", n);
		
		return jsonObject.toString();
	}// end of public String deleteTempMail(String mail_no, String fk_emp_id) -----------------------


	// === 메일 한 개를 읽음처리 해준다 === //
	@Override
	public String readMail(Map<String, String> paraMap) {
		dao.mailRead(paraMap);
		return "{\"result\": 1}";
	}// end of public String readMail(Map<String, String> paraMap) ----------------


	// === 이전, 다음메일 조회해오기 === //
	@Override
	public Map<String, String> getPrevNextMail(Map<String, String> paraMap) {
		return dao.getPrevNextMail(paraMap);
	}// end of public Map<String, String> getPrevNextMail(Map<String, String> paraMap) -------------------


	// === 메일 한 개를 스팸메일로 등록 === //
//	@Override
//	public String addSpamMail(Map<String, String> paraMap) {
//		int n = dao.addSpamMail(paraMap);
//		return "";
//	}// end of public String addSpamMail(Map<String, String> paraMap) -----------------


}
