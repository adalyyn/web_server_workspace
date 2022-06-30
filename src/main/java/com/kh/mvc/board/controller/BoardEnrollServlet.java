package com.kh.mvc.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.mvc.board.common.HelloMvcRenameFileRenamePolicy;
import com.kh.mvc.board.model.dto.Attachment;
import com.kh.mvc.board.model.dto.BoardExt;
import com.kh.mvc.board.model.service.BoardService;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.FileRenamePolicy;

/**
 * Servlet implementation class BoardEmrollServlet
 */
@WebServlet("/board/boardEnroll")
public class BoardEnrollServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService();

	/**
	 *GET 게시글 등록폼 요청
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/board/boardEnroll.jsp").forward(request, response);
	}

	/**
	 * POST db insert요청
	 * 
	 * 첨부파일이 포함된 게시글 등록
	 * - 1. 서버 컴퓨터에 파일 저장 -> cos.jar한테 맡김
	 * 		- MultipartRequest객체 생성
	 * 			- HttpServletRequest
	 * 			- saveDirectory 저장할 경로
	 * 			- maxPostSize 파일용량제한
	 * 			- encoding
	 * 			- FileRenamePolicy - DefaultFileRenamePolicy(기본) 파일명이 같은 객체 업로드되면 넘버링해주는 객체
	 * 		##유의사항 : 기존 request객체가 아닌 MultipartRequest객체에서 모든 사용자 입력값을 가져와야 한다. HttpRequest에서는 아무것도 가져올 수 없다.
	 * 
	 * - 2. 저장된 파일정보 attachment 레코드로 등록
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		
		try {
			//0. 첨부파일 처리
			ServletContext application = getServletContext();	//가장 수명이 긴 객체가 절대경로 알고 있음.
			String saveDirectory = application.getRealPath("/upload/board");	//업로드한 파일 저장되는 곳(서버). 첫번째 슬래시가 웹루트 디렉토리다.
			int maxPostSize = 1024 * 1024 * 10;	//정하기 나름이지만 10MB로 처리.
			String encoding = "utf-8";
			FileRenamePolicy policy = new HelloMvcRenameFileRenamePolicy(); 
			
			MultipartRequest multiReq = new MultipartRequest(request, saveDirectory, maxPostSize, encoding, policy);
			
		
			//저장된 파일 확인
			String originalFilename = multiReq.getOriginalFileName("upfile1");
			String renamedFilename = multiReq.getFilesystemName("upfile1");
//			System.out.println("originalFilename " + originalFilename );
//			System.out.println("renamedFilename" + renamedFilename);
			
		
			//1. 사용자 입력값처리 (MultipartRequest만들고나면 request이용 불가.), attachment도 같이 보낼 준비하기
			String title = multiReq.getParameter("title");
			String writer = multiReq.getParameter("writer");
			String content = multiReq.getParameter("content");
			BoardExt board = new BoardExt(0, title, writer, content, 0, null);
			System.out.println("board = " + board);
			
				//첨부파일이 있을수도 있고 없을수도 있다. 
			File upFile1 = multiReq.getFile("upfile1");
			File upFile2 = multiReq.getFile("upfile2");
			if(upFile1 != null || upFile2 != null) {	//둘 중 파일이 존재한다면 실행될 코드
				List<Attachment> attachments = new ArrayList<>();
				if(upFile1 != null) {
					Attachment attach = new Attachment();
					attach.setOriginalFilename(multiReq.getOriginalFileName("upfile1"));	//업로드한 파일명 넣어주기
					attach.setRenamedFilename(multiReq.getFilesystemName("upfile1"));
					attachments.add(attach);
				}
				if(upFile2 != null) {
					Attachment attach = new Attachment();
					attach.setOriginalFilename(multiReq.getOriginalFileName("upfile2"));	//업로드한 파일명 넣어주기
					attach.setRenamedFilename(multiReq.getFilesystemName("upfile2"));	
					attachments.add(attach);
				}
				board.setAttachments(attachments);
			}
			
			//2. 업무로직 : attachment에 들어갈 내용 + board에 들어갈내용 묶어서 요청 보내야 한다.
			int result = boardService.insertBoard(board);
			
			//3. 리다이렉트처리
			request.getSession().setAttribute("msg", "게시글 등록 성공!");	//수정된게 확인이 된다면 피드백은 생략해도 된다. 
			response.sendRedirect(request.getContextPath() + "/board/boardList");
			
			
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}

}
