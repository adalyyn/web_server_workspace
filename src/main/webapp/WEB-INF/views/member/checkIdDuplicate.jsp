<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	boolean available = (boolean) request.getAttribute("available");
%>    

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>아이디중복검사</title>
<style>
div#checkId-container{text-align:center; padding-top:50px;}
span#duplicated{color:red; font-weight:bold;}
</style>
</head>
<body>
	<div id="checkId-container">
	<% if(available) { %>
		<%-- 아이디 사용 가능한 경우 --%>
		<p>
			<span><%= request.getParameter("memberId") %></span>는 사용가능한 아이디 입니다.
		</p>
		<button type="button" onclick="closePopup();">닫기</button>

	<% } else { %>
		<%-- 아이디 사용불가한 경우 --%>
		<p>
			<span id="duplicated"><%= request.getParameter("memberId") %></span>는 이미 사용중입니다.
		</p>
		<form action="<%= request.getContextPath() %>/member/checkIdDuplicate" name="checkIdDuplicateFrm">
			<input type="text" name="memberId" placeholder="아이디를 입력하세요."/>
			<input type="submit" value="중복검사" />
		</form>
		<script>
		document.checkIdDuplicateFrm.onsubmit = (e) => {
			const frm = e.target;	//현재 폼
			if(!/^[a-zA-z0-9]{4,}$/.test(frm.memberId.value)){
				alert("유효한 아이디를 입력하세요");
				frm.memberId.select();
				return false;
			}
		};
		</script>	
<%	} %>
	</div>
	<script>
	const closePopup = () => {
		// opener : 부모 윈도우 객체
		const frm = opener.document.memberEnrollFrm;
		frm.idValid.value = 1;	
		frm.memberId.value = "<%= request.getParameter("memberId") %>";	//팝업창에 남긴 아이디값이 팝업창 닫으면 회원가입 아이디에 반영되도록.
		self.close();	//현재창 닫기
	};
	</script>
</body>
</html>
