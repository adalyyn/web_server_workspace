package com.kh.mvc.member.model.service;

//Jdbc템플릿 하위의 모든 스테틱 메소드를 가져다 쓰겠다.
import static com.kh.mvc.common.JdbcTemplate.*;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.kh.mvc.member.model.dao.MemberDao;
import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.exception.MemberException;

public class MemberService {

	private MemberDao memberDao = new MemberDao();

	/**
	 * DQL요청일때 service
	 * 1. Connection객체 생성
	 * 2. Dao 요청 & Connection 전달
	 * 3. Connection 반환 (close)
	 */
	public Member findById(String memberId) {		
		//Dao에 요청 보내기 위한 필드
		Connection conn = getConnection();
		Member member = memberDao.findById(conn, memberId);
		close(conn);
		return member;
	}

	/**
	 * DML요청 - service
	 * 1. Connection객체 생성
	 * 2. Dao요청 & conn전달 
	 * 3. 트랜잭션처리(정상처리시 commit, 예외발생시 rollback)
	 * 4. conn반납
	 * 
	 * @param member
	 * @return
	 */
	public int insertMember(Member member) {
		Connection conn = getConnection();
		int result = 0;
		try {
			result = memberDao.insertMember(conn, member);	//런타임예외
			commit(conn);
		} catch(Exception e) {
			rollback(conn);
			throw e;	//controller로 예외던지기.	
		} finally {
			close(conn);
		}		
		return result;
	}

//	public int deleteMember(String memberId) {
//		Connection conn = getConnection();
//		int result = 0;
//		
//		try {
//			result = memberDao.deleteMember(conn,memberId);
//			commit(conn);
//		} catch (Exception e) {
//			rollback(conn);
//			e.printStackTrace();
//		} finally {
//			close(conn);
//		}
//		
//		return result;
//	}
	
	
	public int deleteMember(String memberId) {
		Connection conn = null;
		int result = 0;
		try{
			conn = getConnection();
			result = memberDao.deleteMember(conn, memberId);
			if(result == 0)	//아이디없는데 요청이 들어왔을때. where절에 해당하는 레코드를 못찾으면 0일 수 있음 (update랑 delete)
				throw new MemberException("해당 회원은 존재하지 않습니다.");
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);			
		}
		return result;
	}
	
	public int updateMember(Member member) {
		Connection conn = getConnection();
		int result = 0;
		try {
			result = memberDao.updateMember(conn, member);
			commit(conn);
		} catch(Exception e) {
			rollback(conn);
			throw e; // controller에 예외 던짐.
		} finally {
			close(conn);
		}
		return result;
	}
	
	public int passwordUpdate(String memberId, String newPassword) {
		Connection conn = getConnection();
		int result = 0;
				
		try {
			result = memberDao.passwordUpdate(conn, memberId, newPassword);
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		
		return result;
	}

	//DQL
	public List<Member> findAll(Map<String, Object> param) {
		Connection conn = getConnection();
		List<Member> list = memberDao.findAll(conn, param);	//호버해서 3번째
		close(conn);
		return list;
	}

	//DQL
	public int getTotalContent() {
		Connection conn = getConnection();
		int totalContent = memberDao.getTotalContent(conn);
		close(conn);
		return totalContent;
	}

	public List<Member> findMemberLike(Map<String, Object> param) {
		Connection conn = getConnection();
		List<Member> list = memberDao.findMemberLike(conn, param);	
		close(conn);
		return list;
	}

	public int getTotalContentLike(Map<String, Object> param) {
		Connection conn = getConnection();
		int totalContent = memberDao.getTotalContentLike(conn, param);
		close(conn);
		return totalContent;
	}

	public int updateMemberRole(Member member) {
		Connection conn = getConnection();
		int result = 0;
		try {		
			result = memberDao.updateMemberRole(conn, member);
			commit(conn);
		} 
		catch(Exception e) {
			rollback(conn);
			throw e;
		} 
		finally {
			close(conn);
		}
		
		return result;
	}
}







