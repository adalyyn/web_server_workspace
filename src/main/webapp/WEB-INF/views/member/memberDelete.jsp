<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원탈퇴</title>
<style>
div#checkId-container{text-align:center; padding-top:50px;}
span#duplicated{color:red; font-weight:bold;}
</style>
</head>
<body>
	<div id="deleteMember-container">	
		<p>
			<span><%= request.getParameter("memberId") %></span>님 회원탈퇴 하시겠습니까?
		</p>
		<button type="button" onclick="">탈퇴하기</button>
	</div>
</body>
</html>