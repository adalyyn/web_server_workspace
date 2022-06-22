package com.kh.mvc.member.model.dao;

import static com.kh.mvc.common.JdbcTemplate.close;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import com.kh.mvc.member.model.dto.Gender;
import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.dto.MemberRole;
import com.kh.mvc.member.model.exception.MemberException;

public class MemberDao {

	private Properties prop = new Properties();	//쿼리 읽어오기 위해서 properties객체 생성
	
	public MemberDao() {
		String filename = MemberDao.class.getResource("/sql/member/member-query.properties").getPath();
//		System.out.println("filename@MemberDao = " + filename);
		try {
			prop.load(new FileReader(filename));	//멀티캐치
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * DQL요청일때 Dao의 처리 프로세스
	 * 1. PreparedStatement 객체 생성 (SQL전달) & 값대입(?채우기)
	 * 2. 쿼리실행 executeQuery - ResultSet 반환
	 * 3. ResultSet처리 - dto객체로 변환한다는 의미.
	 * 4. ResultSet, PreparedStatement 객체 반환 (Connection은 반환하지 않음)
	 * 
	 */
	public Member findById(Connection conn, String memberId) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Member member = null;
		String sql = prop.getProperty("findById");
		// select * from member where member_id = ?
		
		try {
			//1. PreparedStatement 객체 생성 (SQL전달) & 값대입(?채우기)
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			
			//2. 쿼리실행 executeQuery - ResultSet 반환
			rset = pstmt.executeQuery();
			
			//3. ResultSet처리 - dto객체로 변환한다는 의미.
			//getString에 컬럼명 오타주의 (대소문자는 구분하지 않음)
			while(rset.next()) {
				memberId = rset.getString("member_id");
				String password = rset.getString("password");
				String memberName = rset.getString("member_name");
				MemberRole memberRole = MemberRole.valueOf(rset.getString("member_role"));	//enum타입으로 변환하는 메소드
				Gender gender = Gender.valueOf(rset.getString("gender"));
				Date birthday = rset.getDate("birthday");
				String email = rset.getString("email");
				String phone = rset.getString("phone");
				String hobby = rset.getString("hobby");
				int point = rset.getInt("point");
				Timestamp emrollDate = rset.getTimestamp("enroll_date");
				
				member = new Member(memberId, password, memberName, memberRole, gender, birthday, email, phone, hobby, point, emrollDate);
			}
			
		} catch (SQLException e) {
			throw new MemberException("회원 아이디 조회 오류", e);
		} finally {
			//4. ResultSet, PreparedStatement 객체 반환
			close(rset);
			close(pstmt);
		}
	
		return member;
	}

	
	
	/**
	 * DML요청일때 Dao의 처리 프로세스 (크게 안달라짐)
	 * 1. PreparedStatement 객체 생성 (SQL전달) & 값대입(?채우기)
	 * 2. 쿼리실행 executeUpdate - int 반환 (insert는 무조건 1 정상처리됐다면)
	 * 3. PreparedStatement 객체 반환 (Connection은 반환하지 않음)
	 * 
	 */
	public int insertMember(Connection conn, Member member) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("insertMember");
		//insertMember = insert into member values (?, ?, ?, default, ?, ?, ?, ?, ?, default, default)
		
		try {
			//DB는 index가 1부터. jdbc도 1부터 (값대입이 안된 상태로 값과 쿼리를 DB에 그대로 보내서 DB에서 처리됨, 재사용성을 높이기 위해서)
 			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberId());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getMemberName());
			pstmt.setString(4, member.getGender() != null ? member.getGender().name() : null);	//enum을 반환, 값을 꺼내줌. name(), toString도 된다.
			pstmt.setDate(5, member.getBirthday());
			pstmt.setString(6, member.getEmail());
			pstmt.setString(7, member.getPhone());
			pstmt.setString(8, member.getHobby());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			//로깅, 분기 다 컨드롤러로 던짐.
			//서비스로 예외던짐(unchecked, 비니지스를 설명가능한 구체적 커스텀예외로 전환해서 던지기)
			throw new MemberException("회원가입오류", e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}
}










