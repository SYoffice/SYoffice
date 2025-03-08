package com.syoffice.app.mail.service;

import java.util.List;
import java.util.Map;

import com.syoffice.app.mail.domain.MailVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartHttpServletRequest;


public interface MailService {
	
	// === 메일박스 가져오기 === //
	List<Map<String, String>> getMailBox(Map<String, String> paraMap);

	// === 메일박스의 총 페이지수 가져오기 === //
	int getMailBoxTotalPage(Map<String, String> paraMap);

	// === 메일의 개수 가져오기 === //
	Map<String, Integer> getMailCount(Map<String, String> paraMap);
	
	// === 조직도 활용한 수신,참조인 추가용 회원 한 명의 정보를 가져오기 === //
	Map<String, String> getEmployeeInfo(Long emp_id);

	// === 메일 전송하기 (타인) === //
	String sendMail(MultipartHttpServletRequest mtp_request);

	// === 메일 상세조회하기 === //
	List<MailVO> getMailDetail(Map<String, String> paraMap);

	// === 메일 첨부파일, 발신인 정보 가져오기 === //
	List<MailVO> getMailAttachFile(Map<String, String> paraMap);

	// === 첨부파일 다운로드 === //
	void downloadMailOneAttachFile(HttpServletRequest request, HttpServletResponse response, String atmailNo);
	
	// === 메일 삭제하기(휴지통으로 이동) === //
	String deleteMail(String mail_no, String fk_emp_id);

	// === 메일 전송하기 (자신) === //
	String sendMailToMe(MultipartHttpServletRequest mtp_request);

	// === 메일 임시저장 === //
	String mailStoreTemp(MultipartHttpServletRequest mtp_request);

	// === 메일 수신인 정보 가져오기 === //
	String getMailRecipientInfo(String mail_no, String fk_emp_id);
	
	// === 메일 작성정보 가져오기 === //
	MailVO getMailInfo(String mail_no, String fk_emp_id);

	// === 메일 임시저장 (수정) === //
	String mailStoreTemp(HttpServletRequest request, MailVO mailVO);

	// === 휴지통, 스팸메일 개수 알아오기 === //
	List<Map<String, Integer>> getMailTashCnt(String fk_emp_id);
	
	// === 휴지통, 스팸메일함 비우기 === //
	Map<String, Integer> emptyMailBox(Map<String, String> paraMap);

	// === 임시저장 메일 삭제(휴지통으로 이동) === //
	String deleteTempMail(String mail_no, String fk_emp_id);

	// === 메일 한 개를 읽음처리 해준다 === //
	String readMail(Map<String, String> paraMap);

	// === 이전, 다음메일 조회해오기 === //
	Map<String, String> getPrevNextMail(Map<String, String> paraMap);

	// === 메일 한 개를 스팸메일로 등록 === //
//	String addSpamMail(Map<String, String> paraMap);
}
