package com.kh.mvc.member.controller;

import java.io.IOException;
import java.util.Enumeration;

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
 * Servlet implementation class memberDelete
 */
@WebServlet("/member/memberDelete")
public class memberDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();
	
	/**
	 * 회원탈퇴
	 * DB에 delete요청
	 * delete from member where mwmber_id = ?
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1. 사용자 인코딩처리
//		request.setCharacterEncoding("utf-8");
//		
//		//2. 사용자 입력값처리
//		String memberId = request.getParameter("memberId");
////		System.out.println(memberId);
//		
//		//3. 업무로직 : DB delete 1이거나 오류
//		int result = memberService.deleteMember(memberId);
//		System.out.println("resultMemberDeleteServlet = " + result);
//		//세션객체 폐기
//		HttpSession session = request.getSession(false);  
//		if(session != null)
//			session.invalidate();
//		
//		//4. 응답처리 : DML은 무조건 redirect처리
////		HttpSession session = request.getSession();
//		session.setAttribute("msg", "회원탈퇴 성공!");
//		response.sendRedirect(request.getContextPath() + "/");
//	}
		
		try {
			//1. 사용자 입력값 처리
			String memberId = request.getParameter("memberId");
			
			//2. 서비스로직호출
			int result = memberService.deleteMember(memberId);
			
			// 모든 속성 제거하기 (이렇게 안하면 탈퇴했는데 로그인됨..DB데이터랑 세션이랑 브라우저의 쿠키가 가각 관리되는게 달라서 그럼)
			// 탈퇴하면서 관련있는 모든것을 제거해야 한다.
			HttpSession session = request.getSession();
			//session.invalidate(); 이하코드를 간단하게 대체 할 수 있지만 이방법을 사용하면 메세지 전송이 힘들다. 이하코드에 세션에 메세지를 담을거라서
			Enumeration<String> names = session.getAttributeNames();
			while(names.hasMoreElements()) {	//다음요소 있니
				String name = names.nextElement();	//세션제거. session.invalidate해도 된다.
				session.removeAttribute(name);	//이름 하나씩 가져온 다음에 지운다. 세션객체는 무효화하지않고 안에 속성만 지우는 것.
			}
			// saveId cookie 제거
			Cookie c = new Cookie("saveId",memberId);
			c.setPath(request.getContextPath());
			c.setMaxAge(0);			//쿠키의 유효기간 0=> 즉시삭제
			response.addCookie(c);	

			//3. 리다이렉트 처리
			session.setAttribute("msg", "회원을 성공적으로 삭제했습니다.");
			response.sendRedirect(request.getContextPath() + "/");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	}

