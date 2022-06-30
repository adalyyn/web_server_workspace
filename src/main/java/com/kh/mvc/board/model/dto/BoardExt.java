package com.kh.mvc.board.model.dto;

import java.sql.Timestamp;
import java.util.List;

public class BoardExt extends Board {

	private int attachCount;
	private List<Attachment> attachments;	//생성자 필요없고, 게터세터 추가, 부모생성자 포함해서 toString다시만들기

	public BoardExt() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BoardExt(int no, String title, String writer, String content, int readCount, Timestamp regDate) {
		super(no, title, writer, content, readCount, regDate);
		this.attachCount = attachCount;
	}

	public int getAttachCount() {
		return attachCount;
	}

	public void setAttachCount(int attachCount) {
		this.attachCount = attachCount;
	}

	
	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	@Override
	public String toString() {
		return "BoardExt [attachCount=" + attachCount + ", attachments=" + attachments + ", toString()="
				+ super.toString() + "]";
	}

	
	
	
}
