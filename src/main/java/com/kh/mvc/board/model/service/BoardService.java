package com.kh.mvc.board.model.service;

import static com.kh.mvc.common.JdbcTemplate.*;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.kh.mvc.board.model.dao.BoardDao;
import com.kh.mvc.board.model.dto.Board;

/**
 * 
 * DML
 * 
 * DQL
 * - conn생성/반환
 * - dao 요청
 * - 
 *
 */
public class BoardService {
	private BoardDao boardDao = new BoardDao();
	
	/**
	 * DQL
	 * @param param
	 * @return
	 */
	public List<Board> findAll(Map<String, Object> param) {
		Connection conn = getConnection();
		List<Board> list = boardDao.findAll(conn, param);
		close(conn);
		return list;
	}

	/**
	 * DQL
	 * @return
	 */
	public int getTotalContent() {
		Connection conn = getConnection();
		int totalContent = boardDao.getTotalContent(conn);
		close(conn);
		return totalContent;
	}

	/**
	 * DML
	 * @param board
	 * @return
	 */
	public int insertBoard(Board board) {
		Connection conn = getConnection();
		int result = 0;
		
		try {
			result = boardDao.insertBoard(conn, board);
			commit(conn);
		} catch(Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		return result;
	}

}
