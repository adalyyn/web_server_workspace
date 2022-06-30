<%@page import="com.kh.mvc.board.model.dto.BoardExt"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="com.kh.mvc.board.model.dto.Board"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%
	List<Board> list = (List<Board>) request.getAttribute("list");
%>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/board.css" />
<section id="board-container">
	<h2>게시판 </h2>
	<% if(loginMember != null) { %>
	<input type="button" value="글쓰기" id="btn-add"
		onclick="location.href='<%= request.getContextPath() %>/board/boardEnroll';"/>
	<%} %>
	<table id="tbl-board">
		<tr>
			<th>번호</th>
			<th>제목</th>
			<th>작성자</th>
			<th>작성일</th>
			<th>첨부파일</th><%--첨부파일이 있는 경우 /images/file.png 표시 width:16px --%>
			<th>조회수</th>
		</tr>
	<% if(list != null && !list.isEmpty()) { 
			for(Board _board : list) {
					BoardExt board = (BoardExt) _board;				
	%>
			<tr>
				<td><%= board.getNo() %></td>
				<td><%= board.getTitle() %></td>
				<td><%= board.getWriter() %></td>
				<td><%= new SimpleDateFormat("yyyy-MM-dd HH:mm").format(board.getRegDate()) %></td>
				<td>
					<% if(board.getAttachCount() > 0) { %>
					<img src="<%= request.getContextPath() %>/images/file.png" alt="" style="width:16px;" />
					<%} %>
				</td>
				<td><%= board.getReadCount() %></td>
			</tr>			
			
	<%   	} 
		} else { %>
		<tr>
			<td colspan="6">조회된 게시글이 없습니다. </td>
		</tr>
	<% } %>	
	</table>

	<div id='pagebar'>
		<%= request.getAttribute("pagebar") %>
	</div>
</section>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
