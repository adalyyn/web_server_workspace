package com.kh.mvc.admin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.mvc.common.HelloMvcUtils;
import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.service.MemberService;

/**
 * Servlet implementation class AdminMemberListServlet
 */
@WebServlet("/admin/memberList")
public class AdminMemberListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			//1. 사용자 입력값
			int cPage = 1;	//기본값 먼저 선언해두고 오류발생하면 넘어가도록.
			int numPerPage = 10;	//임의로 고정. 바꿀수도 있음. 한페이지에 몇페이지씩 볼건지.
			
			//사용자 입력값이 null이면 -> 형변환하면 예외 발생
			try {
				cPage = Integer.parseInt(request.getParameter("cPage"));
			} catch (NumberFormatException e) {}	//캐치절로 잡되 아무것도 안하고 작업, 기존값 1이 계속 유지되도록!
			
	
			//2. 업무로직 : 전체조회라는 기능을 어디에서 구현할까? 맥락 고려..관리자만의 뭔가 필요하다면 adminService만들기
			//a. content영역 - paging query날리기
			//start, end
			int start = (cPage -1) * numPerPage + 1;
			int end = cPage * numPerPage;
			Map<String, Object> param = new HashMap<>();
			param.put("start", start);
			param.put("end", end);
			//로깅
			System.out.printf("cPage = %s, numPerPage = %s, start = %s, end = %s%n", cPage, numPerPage, start, end);
			
			// select * from member order by enroll_date (과거쿼리)
			List<Member> list = memberService.findAll(param);	//조회된 행이 없어도 비어있는 리스트 객체라도 보냄. 오류나면 호버해서 3번째 선택 param추가
			System.out.println("list = " + list);
			
			//b. pagebar영역
			// select count(*) from member 총몇페이지인지 알아야 몇으로 나눌지 앎. 컬럼명 말고 인덱스로 가져오기. 
			int totalContent = memberService.getTotalContent();	//총 게시글 몇건인지
			String url = request.getRequestURI();	//다음주소
			System.out.println("totalContent = " + totalContent);
			String pagebar = HelloMvcUtils.getPagebar(cPage, numPerPage, totalContent, url); 	//페이지바 만들기
			System.out.println("pagebar = " + pagebar);
			
			
			
			//3. view단 응답처리
			request.setAttribute("list", list);
			request.setAttribute("pagebar", pagebar);
			request.getRequestDispatcher("/WEB-INF/views/admin/memberList.jsp").forward(request, response);
			
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
