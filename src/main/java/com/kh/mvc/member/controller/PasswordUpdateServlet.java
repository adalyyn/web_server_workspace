package com.kh.mvc.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.mvc.common.HelloMvcUtils;
import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.service.MemberService;

/**
 * Servlet implementation class PasswordUpdateServlet
 */
@WebServlet("/member/passwordUpdate")
public class PasswordUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/member/passwordUpdate.jsp").forward(request, response);
	}

	/**
	 * 비밀번호 변경 
	 * update member set member_password = ? where member_password = ?
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1. 인코딩처리
		request.setCharacterEncoding("utf-8");
//		
//		//2. 사용자 입력값처리
////		String oldPassword = HelloMvcUtils.getEncryptedPassword(request.getParameter("oldPassword"), request.getMemberId());
////		String newPassword = HelloMvcUtils.getEncryptedPassword(request.getParameter("newPassword"), request.getMemberId());
//		String oldPassword = request.getParameter("oldPassword");
//		String newPassword = request.getParameter("newPassword");
//		System.out.println("oldPassword = " + oldPassword + "newPassword = " + newPassword );
//		
//		//3. 업무로직 처리 : update 
//		
//		//a. 기존비밀번호 검증
////		oldPassword.equals(request.getMemberPassword());
//		
//		//b. 신규비밀번호 업데이트
//		int result = memberService.passwordUpdate(oldPassword, newPassword);
//		
//		//4. 응답처리
//		HttpSession session = request.getSession();
//		
//		if(result == 1) {
//			session.setAttribute("msg", "비밀번호 변경 성공!");
//			response.sendRedirect(request.getContextPath() + "/member/memberView");
//		} else {
//			session.setAttribute("msg", "비밀번호 변경 실패!");
//			response.sendRedirect(request.getContextPath() + "/member/passwordUpdate");
//		}
//		
		try {
			//1.사용자입력값처리
			//memberId 폼안에 히든으로 전송
			String memberId = request.getParameter("memberId");
			String oldPassword = HelloMvcUtils.getEncryptedPassword(request.getParameter("oldPassword"), memberId);
			String newPassword = HelloMvcUtils.getEncryptedPassword(request.getParameter("newPassword"), memberId);			
			
			//2.업무로직
			String msg = null;
			String location = request.getContextPath();
			Member member = memberService.findById(memberId);
			
				//a.기존 비밀번호 검증
			if(member != null && oldPassword.equals(member.getPassword())) {				
				//b.신규 비밀번호 업데이트
				//update member set password = ? where member_id = ?
				int result = memberService.passwordUpdate(oldPassword, memberId);
				
				msg = "비밀번호를 성공적으로 변경했습니다.";
				location += "/member/memberView";
				
				//세션 비밀번호도 갱신
				Member loginMember = (Member)request.getSession().getAttribute("loginMember");
				loginMember.setPassword(newPassword);
			}
			else {
				msg = "기존 비밀번호가 일치하지 않습니다.";
				location += "/member/passwordUpdate";
			}
			
			
			//3.응답
			//a. 비밀번호 정상 변경 후 내정보보기 페이지로 이동			
			//b. 비빌번호 변경실패시(기존비밀번호 불일치) 비밀번호 변경 페이지로 이동
			request.getSession().setAttribute("msg", msg);
			response.sendRedirect(location);
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
