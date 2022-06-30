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
 * Servlet implementation class AdminMemberFinder
 */
@WebServlet("/admin/memberFinder")
public class AdminMemberFinder extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			//1.사용자 입력값처리
			int cPage = 1;
			int numPerPage = 10;
			try {
				cPage = Integer.parseInt(request.getParameter("cPage"));
			} catch (NumberFormatException e) {}  //이 외의 오류는 밑에서 잡음. 
			
			String searchType = request.getParameter("searchType");
			String searchKeyword = request.getParameter("searchKeyword");
			Map<String, Object> param = new HashMap<>();
			param.put("searchType", searchType);
			param.put("searchKeyword", searchKeyword);
			param.put("start", (cPage - 1) * numPerPage + 1);
			param.put("end", cPage * numPerPage);
//			System.out.println(param);
			
			//2. 업무로직
			//a. content 영역
			// select * from member where #(컬럼명) like ?
			List<Member> list = memberService.findMemberLike(param);
			
			
			//b. pagebar 영역
			int totalContent = memberService.getTotalContentLike(param);
			System.out.println("totalContent = " + totalContent);
			String url = request.getRequestURI() + "?searchType=" + searchType + "&searchKeyword=" + searchKeyword;
			String pagebar = HelloMvcUtils.getPagebar(cPage, numPerPage, totalContent, url);
			System.out.println("pagebar = " + pagebar);
			
			//3. view단 처리
			request.setAttribute("list", list);
			request.setAttribute("pagebar", pagebar);
			request.getRequestDispatcher("WEB-INF/views/admin/memberList.jsp").forward(request,response);
			
		}
		catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}

}
