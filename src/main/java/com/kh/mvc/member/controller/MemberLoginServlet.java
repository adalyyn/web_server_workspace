package com.kh.mvc.member.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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
		//1. 인코딩처리
		request.setCharacterEncoding("utf-8");
		
		//2. 사용자 입력값 처리(자바변수에 담기)
		String memberId = request.getParameter("memberId");
		String password = request.getParameter("password");
		System.out.println("memberId = " + memberId);
		System.out.println("password = " + password);
		
		//3. 업무로직 처리 : 로그인 여부를 판단하기. memberId로 디비에서 회원정보조회해와서 패스워드 일치여부확인
		
		Member member = memberservice.findById(memberId);
		System.out.println("member@MemberLoginServlet = " + member);
		HttpSession session = request.getSession(true);	//세션이 존재하지 않으면 새로 생성해서 반환. true는 생략가능.
		
		
		//로그인 성공
		if(member != null && password.equals(member.getPassword())) {
			session.setAttribute("loginMember", member);
		}
		//로그인 실패(해당 아이디가 존재하지 않는 경우 || 비밀번호 틀린경우)
		else {
			session.setAttribute("msg", "아이디 또는 비밀번호가 일치하지 않습니다.");
		}
				
		
		//4. 응답 처리
//		RequestDispatcher reqDispatcher = request.getRequestDispatcher("/index.jsp");
//		reqDispatcher.forward(request, response);
		
		//4. 응답처리 : 로그인 후 url변경을 위해 리다이렉트 처리
		// 응답 302(The requested page has moved temporarily to a new URL)  redirect 전송.
		// 브라우저에게 location으로 재요청을 명령. (브라우져야 여기로 바로 요청보내)
		response.sendRedirect(request.getContextPath() + "/");	// /mvc라고 하드코딩하지말고, 디렉토리랑 헷갈리지 말라고 / 하나 더 넣어줌.
	}

}
