package com.kh.mvc.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.service.MemberService;

/**
 * Servlet implementation class MemberLoginServlet
 */
@WebServlet("/member/login")
public class MemberLoginServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private MemberService memberservice = new MemberService();

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			//1. 인코딩처리
			request.setCharacterEncoding("utf-8");
			
			//2. 사용자 입력값 처리(자바변수에 담기)
			String memberId = request.getParameter("memberId");
			String password = request.getParameter("password");
			String saveId = request.getParameter("saveID");
			System.out.println("memberId = " + memberId);
			System.out.println("password = " + password);
			System.out.println("saveId = " + saveId);	//체크했으면 "on" | null
			
			//3. 업무로직 처리 : 로그인 여부를 판단하기. memberId로 디비에서 회원정보조회해와서 패스워드 일치여부확인
			
			Member member = memberservice.findById(memberId);
			System.out.println("member@MemberLoginServlet = " + member);
			
			HttpSession session = request.getSession(true);	//세션이 존재하지 않으면 새로 생성해서 반환. true는 생략가능.
			System.out.println(session.getId());	//클라이언트와 동일한 아이디 확인가능.
			
			//로그인 성공
			if(member != null && password.equals(member.getPassword())) {
//			request.setAttribute("loginMember", member); 이렇게 하면 페이지 이동시 로그인이 풀린다. 
				session.setAttribute("loginMember", member);	//리다이렉트 요청에 대한 답변할때 이용
				
				//saveId처리(쿠키값이용)
				Cookie cookie = new Cookie("saveId", memberId);	//키는 saveId, 밸류는 멤버아이디
				cookie.setPath(request.getContextPath());	// /mvc라고 경로설정 -> /mvc로 시작하는 요청주소에 쿠키를 함께 전송.

				
				//saveId를 사용하는 경우 
				if(saveId != null) {
					//session 쿠키(서버에 접속한 동안만 클라이언트에 보관. 브라우저 종료시 소멸. 지정하지 않은 경우)
					//persistent 쿠키(MaxAge를 지정한 경우)
					cookie.setMaxAge(7 * 24 * 60 * 60);	//초단위로 유효기간 설정하기(7일 설정)
				}
				//saveID를 사용하지 않는 경우
				else {
					cookie.setMaxAge(0);	//즉시삭제
				}
				
				response.addCookie(cookie); 	//응답메세지에 set-cookie 항목으로 전송함
			}
			
			//로그인 실패(해당 아이디가 존재하지 않는 경우 || 비밀번호 틀린경우)
			else {
//			request.setAttribute("msg", "아이디 또는 비밀번호가 일치하지 않습니다.");
				session.setAttribute("msg", "아이디 또는 비밀번호가 일치하지 않습니다.");
			}
					
			
			//4. 응답 처리
//			RequestDispatcher reqDispatcher = request.getRequestDispatcher("/index.jsp");
//			reqDispatcher.forward(request, response);
			
			//4. 응답처리 : 로그인 후 url변경을 위해 리다이렉트 처리
			// 응답 302(The requested page has moved temporarily to a new URL)  redirect 전송.
			// 브라우저에게 location으로 재요청을 명령. (브라우져야 여기로 바로 다시 요청보내)
			response.sendRedirect(request.getContextPath() + "/");	
			// 인덱스페이지로 돌아가기위해 /mvc라고 하드코딩하지말고 메소드 사용하고, 디렉토리랑 헷갈리지 말라고 / 하나 더 넣어줌.
		} catch (IOException e) {
			e.printStackTrace();	//로깅은 컨트롤러에서만
			throw e; //톰캣한테 던지기
		}
	}

}
