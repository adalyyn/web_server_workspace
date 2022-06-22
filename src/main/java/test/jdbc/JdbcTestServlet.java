package test.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class JdbcTestServlet
 */
@WebServlet("/jdbc/test")
public class JdbcTestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain; charset=utf-8");	//한글깨지지말라고
		response.getWriter().append("Database 연결 테스트... - 서버콘솔을 확인하세요.");		//사용자 요청이 들어왔을때
		
		try {
			testDatabaseConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void testDatabaseConnection() throws ClassNotFoundException, SQLException {
		String driverClass = "oracle.jdbc.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";	//db접속프로토콜+@+url+포트+db이름 (클라우드 쓰면 달라져야 한다.)
		String user = "web";
		String password = "web";		
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "select * from member";
		
		// 1. DriverClass 등록
		Class.forName(driverClass);
		System.out.println(" > DriverClass 등록 성공!");
		
		// 2. Connection객체 생성
		conn = DriverManager.getConnection(url, user, password);
		System.out.println("> Connection객체 생성 성공!");
		
		// 3. Pstmt 객체 생성 , 미완성 쿼리 값 대입
		pstmt = conn.prepareStatement(sql);
		System.out.println("> PrepareStatement 객체 생성 성공!");
		
		// 4. 쿼리실행, ResultSet반환
		//DQL pstmt.executeQuery : Result
		//DML pstmt.executeUpdate : int
		rset = pstmt.executeQuery();
		System.out.println("> 쿼리 실행 및 ResultSet 반환 성공!");
		
		// 5. ResultSet 처리 (DML은 커밋/롤백처리필요)
		while(rset.next()) {
			String memberId = rset.getString("member_id");	//컬럼명
			String memberName = rset.getString("member_name");
			Date birthday = rset.getDate("birthday");
			int point = rset.getInt("point");
			
			System.out.printf("%s\t%s\t%s\t%s%n", memberId, memberName, birthday, point);
			
		}
		
		// 6. 사용한 자원 반납
		rset.close();
		pstmt.close();
		conn.close();
		System.out.println("> 자원반납 성공!");
	}

}




