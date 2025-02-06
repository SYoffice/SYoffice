package com.syoffice.app.aop;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.syoffice.app.common.MyUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// == #25. 공통 관심사 클래스(Aspect 클래스) 만들기
@Aspect // 공통 관심사 클래스로 등록
@Component
public class CommonAop {
	// ===== Before Advice(보조업무) 만들기 ====== //
	/*
	 * 주업무(<예: 글쓰기, 글수정, 댓글쓰기, 직원목록조회 등등>)를 실행하기 앞서서 이러한 주업무들은 먼저 로그인을 해야만 사용가능한
	 * 작업이므로 주업무에 대한 보조업무<예: 로그인 유무검사> 객체로 로그인 여부를 체크하는 관심 클래스(Aspect 클래스)를 생성하여
	 * 포인트컷(주업무)과 어드바이스(보조업무)를 생성하여 동작하도록 만들겠다.
	*/
	// == pointCut(주업무) 설정 //
	@Pointcut("execution(public * com.syoffice.app..*Controller.requiredLogin_*(..))")
	public void requiredLogin() {

	}

	// ===== Before Advice(공통관심사, 보조업무)를 구현한다. ===== //
	@Before("requiredLogin()")
	public void loginCheck(JoinPoint joinpoint) { // 로그인 유무 검사를 하는 메소드 작성하기
		// JoinPoint joinpoint 는 포인트컷 되어진 주업의 메소드이다.

		// 로그인 유무를 확인하기 위해서는 request 를 통해 session 을 얻어와야 한다.
		HttpServletRequest request = (HttpServletRequest) joinpoint.getArgs()[0]; // 주업무 메소드의 첫번째 파라미터를 얻어오는 것이다.
		HttpServletResponse response = (HttpServletResponse) joinpoint.getArgs()[1]; // 주업무 메소드의 두번째 파라미터를 얻어오는 것이다.

		HttpSession session = request.getSession();

		if (session.getAttribute("loginuser") == null) {
			String message = "먼저 로그인 하세요~~ (AOP Before Advice 활용)";
			String loc = request.getContextPath() + "/member/login";

			request.setAttribute("message", message);
			request.setAttribute("loc", loc);

			// 로그인 성공 후 로그인 하기전 페이지로 돌아가는 작업
			String url = MyUtil.getCurrentURL(request);
			session.setAttribute("goBackURL", url); // 세션에 돌아갈 페이지 url 정보 저장

			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/msg.jsp");
			
			try {
				dispatcher.forward(request, response);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}

		}

	}// end of public void loginCheck(JoinPoint joinpoint) { } ---------

	// ===== #77. After Advice(보조업무) 만들기 시작 ====== //
	/*
	    주업무(<예: 글쓰기, 제품구매 등등>)를 실행한 다음에  
	    회원의 포인트를 특정점수(예: 100점, 200점, 300점) 증가해 주는 것이 공통의 관심사(보조업무)라고 보자.
	    관심 클래스(Aspect 클래스)를 생성하여 포인트컷(주업무)과 어드바이스(보조업무)를 생성하여
	    동작하도록 만들겠다.
	*/
	// == PointCut(주업무) 설정 == //
	// Pointcut 이란 공통관심사를 필요로하는 메소드를 말한다.
	@Pointcut("execution(public * com.spring.app..*Controller.pointPlus_*(..))")
	public void pointPlus() {}
	
	/* 참고하실 분들은 참고하세요
	@Autowired	// Type 에 따라 Bean 을 주입해준다.
	private BoardService service;
	
	// === After Advice(공통관심사, 보조업무)를 구현한다. === //
	// 회원의 포인트를 특정점수(예: 100점, 200점, 300점) 만큼 증가시키는 메소드 생성하기 
	@SuppressWarnings("unchecked")	// 앞으로는 경고 표시를 하지말라는 뜻
// 	@After("pointPlus()")	// @After 는 주업무 메소드(포인트 컷)가 실행된 후에 무조건 실행된다.
	@AfterReturning("pointPlus()")	// @AfterReturning 은 주업무 메소드가 성공한 후에 발생
//	@AfterThrowing("pointPlus()")	// @AfterThrowing 은 주업무 메소드가 실패한 후에 발생
	public void pointPlus(JoinPoint joinpoint) {
		// JoinPoint joinpoint 는 포인트컷 되어진 주업무의 메소드이다.
		
		Map<String, String> paraMap = (Map<String, String>) joinpoint.getArgs()[0];
		// 주업무 메소드의 첫번째 파라미터를 얻어노느 것이다.
		
		service.pointPlus(paraMap);
	}// end of public void pointPlus(JoinPoint joinpoint) {} ----
	*/
	
	// ===== Around Advice(보조업무) 만들기 ====== //
	
}
