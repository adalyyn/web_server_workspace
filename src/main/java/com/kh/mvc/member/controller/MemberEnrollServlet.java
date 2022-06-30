package com.kh.mvc.member.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.mvc.common.HelloMvcUtils;
import com.kh.mvc.member.model.dto.Gender;
import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.service.MemberService;

/**
 * Servlet implementation class MemberEnrollServlet
 */
@WebServlet("/member/memberEnroll")	//같은 url을 사용
public class MemberEnrollServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();

	/**
	 * GET : 회원가입 폼 요청
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/member/memberEnroll.jsp").forward(request, response);
	}

	/**
	 * POST : DB에 insert요청(DML)
	 * 서블릿에서부터 어떤 쿼리를 날리지 생각해놓기!
	 * insert into member values (?, ?, ?, default,? , ?, ?, ?, ?, default, default)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			//1. 인코딩처리
			request.setCharacterEncoding("utf-8");
			
			//2. 사용자 입력값 처리
			String memberId = request.getParameter("memberId");
			String password = HelloMvcUtils.getEncryptedPassword(request.getParameter("password"), memberId);
			String memberName = request.getParameter("memberName");
			String _birthday = request.getParameter("birthday");
			String _gender = request.getParameter("gender");
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			String[] hobbies = request.getParameterValues("hobby");
			
			//후처리 필요한 값들(enum타입, 체크박스 처리, 모두 문자열로 날아오기때문에 변환), null처리 유의!! 
			Gender gender = _gender != null ? Gender.valueOf(_gender) : null;
			String hobby = hobbies != null ? String.join(",", hobbies) : null;
			Date birthday = (_birthday != null && !"".equals(_birthday)) ? Date.valueOf(_birthday) : null;
			
			
			Member member = new Member(memberId, password, memberName, null, gender, birthday, email, phone, hobby, 0, null);
			
			System.out.println("member@MemberEnrollServlet = " + member);
			
			//3. 업무로직 : DB insert, 1 이거나 오류
			int result = memberService.insertMember(member);
//			System.out.println("resultMemberEnrollServlet = " + result);
			
			
			//4. 응답처리 : DML을 무조건 redirect처리
			HttpSession session = request.getSession();
			session.setAttribute("msg", "회원가입 성공!");
			response.sendRedirect(request.getContextPath() + "/");
			
			
		} catch (IOException e) {
			e.printStackTrace();	//로깅
			throw e; //WAS(톰캣)에 예외던지기. 톰캣이 받아서 에러페이지로 전환해준다(응답출력해준다).
		}
		
		
	}

}









