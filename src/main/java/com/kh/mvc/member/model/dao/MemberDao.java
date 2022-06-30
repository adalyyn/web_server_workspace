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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
				member = handleMemberResultSet(rset);
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

	private Member handleMemberResultSet(ResultSet rset) throws SQLException {
		
		String memberId = rset.getString("member_id");
		String password = rset.getString("password");
		String memberName = rset.getString("member_name");
		MemberRole memberRole = MemberRole.valueOf(rset.getString("member_role"));	//enum타입으로 변환하는 메소드
		String _gender = rset.getString("gender");
		Gender gender = _gender != null ? Gender.valueOf(_gender) : null;
		Date birthday = rset.getDate("birthday");
		String email = rset.getString("email");
		String phone = rset.getString("phone");
		String hobby = rset.getString("hobby");
		int point = rset.getInt("point");
		Timestamp emrollDate = rset.getTimestamp("enroll_date");
			 
		return new Member(memberId, password, memberName, memberRole, gender, birthday, email, phone, hobby, point, emrollDate);
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

//	public int deleteMember(Connection conn, String memberId) {
//		PreparedStatement pstmt = null;
//		int result = 0;
//		String sql = prop.getProperty("deleteMember");
//		
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, memberId);
//			
//			result = pstmt.executeUpdate();
//		} catch (SQLException e) {			
//			e.printStackTrace();
//		} finally {
//			close(pstmt);
//		}		
//		return result;
//	}
	
	public int deleteMember(Connection conn, String membmerId) {
		int result = 0;
		PreparedStatement pstmt = null;
		String query = prop.getProperty("deleteMember"); 

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, membmerId);
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new MemberException("회원 삭제 오류!", e);
		} finally {
			close(pstmt);
		} 
		
		return result;
	}
	
	public int updateMember(Connection conn, Member member) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("updateMember");
		// update member set password = ?, member_name = ?, gender = ?, birthday = ?, email = ?, phone = ?, hobby = ? where member_id = ?

		try {
			pstmt = conn.prepareStatement(sql);
			//pstmt.setString(1, member.getPassword());
			pstmt.setString(1, member.getMemberName());
			pstmt.setString(2, member.getGender() != null ? member.getGender().name() : null); // Gender.M
			pstmt.setDate(3, member.getBirthday());
			pstmt.setString(4, member.getEmail());
			pstmt.setString(5, member.getPhone());
			pstmt.setString(6, member.getHobby());
			pstmt.setString(7, member.getMemberId());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// service 예외 던짐(unchecked, 비지니스를 설명가능한 구체적 커스텀예외 전환)
			throw new MemberException("회원정보수정 오류", e);
		} finally {
			close(pstmt);
		}
		return result;
	}

	
	public int passwordUpdate(Connection conn, String memberId, String newPassword) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("updatePassword");	//update member set password = ? where member_id = ?
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, newPassword);
			pstmt.setString(2, memberId);
			
			result = pstmt.executeUpdate();			
		} 
		catch (SQLException e) {
			throw new MemberException("비밀번호 변경 오류!", e);
		} 
		finally {
			close(pstmt);
		}
			
		return result;
	}

	public List<Member> findAll(Connection conn, Map<String, Object> param) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Member> list = new ArrayList<>();	//조회된 행이 없으면 비어있는 리스트가 넘어간다. 
		String sql = prop.getProperty("findAll");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (int) param.get("start"));
			pstmt.setInt(2, (int) param.get("end"));
			rset = pstmt.executeQuery();
			while(rset.next()) {
				Member member = handleMemberResultSet(rset);
				list.add(member);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return list;
	}

	//DQL
	public int getTotalContent(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int totalContent = 0;
		String sql = prop.getProperty("getTotalContent");
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			if(rset.next())  //1행일게 확실하면 while말고 if도 가능하다. 
				totalContent = rset.getInt(1);	//db에서 인덱스는 1부터
		} catch (SQLException e) {
			throw new MemberException("전체회원수 조회 오류!", e);
		} finally {
			close(rset);
			close(pstmt);
		}
	
		return totalContent;
	}

	public List<Member> findMemberLike(Connection conn, Map<String, Object> param) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Member> list = new ArrayList<>();
		String sql = prop.getProperty("findMemberLike");	//null point exception이면 sql이 잘못돼있을 확률이 높음
		//select * from member where # like ?
		//#값(컬럼명) 처리하기 : 이건 pstmt가 처리를 못함
		String col = (String) param.get("searchType");
		String val = (String) param.get("searchKeyword");
		int start = (int) param.get("start");
		int end = (int) param.get("end");
		sql = sql.replace("#", col);
		
		// ?값처리는 pstmt안에서 처리	
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + val + "%");
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			rset = pstmt.executeQuery();
			while(rset.next())
				list.add(handleMemberResultSet(rset));	//핸들러가 Member객체 리턴하기 때문에 한줄로 씀.
			
			
		} catch (SQLException e) {
			throw new MemberException("관리자 회원검색 오류!", e);
		} finally {
			close(rset);
			close(pstmt);
		}		
		return list;
	}

	public int getTotalContentLike(Connection conn, Map<String, Object> param) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int totalContent = 0;
		String sql = prop.getProperty("getTotalContentLike");
		String col = (String) param.get("searchType");
		String val = (String) param.get("searchKeyword");
		sql = sql.replace("#", col);
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + val + "%");
			rset = pstmt.executeQuery();
			if(rset.next())
				totalContent = rset.getInt(1);
		} catch (SQLException e) {
			throw new MemberException("관리자 검색된 회원수 조회 오류!", e);
		} finally {
			close(rset);
			close(pstmt);
		}
	
		return totalContent;
	}

	public int updateMemberRole(Connection conn, Member member) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("updateMemberRole");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberRole().name());
			pstmt.setString(2, member.getMemberId());
			result = pstmt.executeUpdate();
		}
		catch(Exception e){
			throw new MemberException("회원권한정보수정 오류!", e);
		}
		finally {
			close(pstmt);
		}		
		return result;
	}
}

	
	
	











