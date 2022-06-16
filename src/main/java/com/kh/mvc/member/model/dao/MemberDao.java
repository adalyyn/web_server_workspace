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

public class MemberDao {

	private Properties prop = new Properties();
	
	public MemberDao() {
		String filename = MemberDao.class.getResource("/sql/member/member-query.properties").getPath();
		System.out.println("filename@MemberDao = " + filename);
		try {
			prop.load(new FileReader(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/*
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
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				memberId = rset.getString("member_id");
				String password = rset.getString("password");
				String memberName = rset.getString("member_name");
				MemberRole memberRole = MemberRole.valueOf(rset.getString("member_role"));
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
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
	
		
		return member;
	}
}









