package com.kh.mvc.member.model.service;

//Jdbc템플릿 하위의 모든 스테틱 메소드를 가져다 쓰겠다.
import static com.kh.mvc.common.JdbcTemplate.*;
import java.sql.Connection;

import com.kh.mvc.member.model.dao.MemberDao;
import com.kh.mvc.member.model.dto.Member;

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
}







