package com.kh.mvc.board.model.dao;
import static com.kh.mvc.common.JdbcTemplate.close;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.kh.mvc.board.model.dto.Board;
import com.kh.mvc.board.model.dto.BoardExt;
import com.kh.mvc.board.model.exception.BoardException;


/**
 * Properties 쿼리관리 객체 = sql/board/board-query.properties load!
 * DML
 * 
 * DQL
 * - pstmt 객체
 * - 미완성쿼리/값대입
 * - rset처리 (board객체로 옮겨담기)
 * - pstmt,rset 반납
 *
 */
public class BoardDao {

	private Properties prop = new Properties();
	
	public BoardDao() {
		String filename = BoardDao.class.getResource("/sql/board/board-query.properties").getPath();
		try {
			prop.load(new FileReader(filename));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * DQL : findAll
	 * @param param 
	 * @param conn 
	 * @return
	 */
	public List<Board> findAll(Connection conn, Map<String, Object> param) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Board> list = new ArrayList<>();	//한건조회시 null, 여러건조회시 빈 리스트객체 만들어놓기.
		String sql = prop.getProperty("findAll");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (int) param.get("start"));
			pstmt.setInt(2, (int) param.get("end"));
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				BoardExt board = handleBoardResultSet(rset);	//board테이블과 1:1 매칭되는 부분 처리
				board.setAttachCount(rset.getInt("attach_count"));	// 추가적인 부분 처리
				list.add(board);
			}
		} catch (SQLException e) {
			throw new BoardException("게시글 목록 조회 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}		
		return list;
	}


	private BoardExt handleBoardResultSet(ResultSet rset) throws SQLException {
	
		int no = rset.getInt("no");
		String title = rset.getString("title");
		String writer = rset.getString("writer");
		String content = rset.getString("content");
		int readCount = rset.getInt("read_count");
		Timestamp regDate = rset.getTimestamp("reg_date");
		
		return new BoardExt(no, title, writer, content, readCount, regDate);
	}


	public int getTotalContent(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int totalContent = 0;
		String sql = prop.getProperty("getTotalContent");
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			if(rset.next())
				totalContent = rset.getInt(1);
		} catch (SQLException e) {
			throw new BoardException("총 게시글 수 조회 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return totalContent;
	}

	/**
	 * insert into board (no, title, writer, content) values (seq_board_no.nextval, ?, ?, ?)
	 * @param conn
	 * @param board
	 * @return
	 */
	public int insertBoard(Connection conn, Board board) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("insertBoard");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getTitle());
			pstmt.setString(2, board.getWriter());
			pstmt.setString(3, board.getContent());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new BoardException("게시글 등록 오류!", e);
		} finally {
			close(pstmt);
		}
		return result;
	}
	
	
}
