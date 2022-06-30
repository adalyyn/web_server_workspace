<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

	<section id=enroll-container>
		<h2>비밀번호 변경</h2>
		<form 
			name="passwordUpdateFrm" 
			action="<%=request.getContextPath()%>/member/passwordUpdate" 
			method="post" >
			<table>
				<tr>
					<th>현재 비밀번호</th>
					<td><input type="password" name="oldPassword" id="oldPassword" required></td>
				</tr>
				<tr>
					<th>변경할 비밀번호</th>
					<td>
						<input type="password" name="newPassword" id="newPassword" required>
					</td>
				</tr>
				<tr>
					<th>비밀번호 확인</th>
					<td>	
						<input type="password" id="newPasswordCheck" required><br>
					</td>
				</tr>
				<tr>
					<td colspan="2" style="text-align: center;">
						<input type="submit"  value="변경" />
					</td>
				</tr>
			</table>
			<input type="hidden" name="memberId" />
		</form>
	</section>
	<script>
	
	/**
	 * 비밀번호 일치여부 검사
	 */
	document.querySelector("#passwordCheck").onblur = (e)=>{
		const password = document.querySelector("#_password");
		const passwordCheck = e.target;
		if(password.value !==passwordCheck.value){
			alert("비밀번호가 일치하지 않습니다.");
			password.select();
		}
	};
	
	document.passwordUpdateFrm.onsubmit = (e) => {
		//현재 비밀번호가 맞는지 검사
		
		//비밀번호 유효성 검사
		const newPassword = document.querySelector("#newPassword");
		 if(!/^[a-zA-Z0-9!@#$%^&*()]{4,}$/.test(newPassword.value)){
			 alert("비밀번호는 영문자/숫자/!@#$%^&*()로 최소 4글자 이상이어야 합니다.");
			 newPassword.select();
			 return false;
		 }
		
		//새로운비밀번호, 비밀번호확인 일치 검사
		 const newPasswordCheck = document.querySelector("#newPasswordCheck");
		 if(newPassword.value !== newPasswordCheck.value){
				alert("비밀번호가 일치하지 않습니다.");
				newPassword.select();
				return false;
		 }
	};
	
	</script>
	
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
