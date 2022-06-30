package com.kh.mvc.board.common;

import java.io.File;
import java.io.IOException;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.oreilly.servlet.multipart.FileRenamePolicy;


/**
 * 우리의 목표
 * abc.txt -> 20220628_183030333_123.txt
 * @author younj
 *
 */
public class HelloMvcRenameFileRenamePolicy implements FileRenamePolicy {

	@Override
	public File rename(File oldFile) {
		File newFile = null;
		do {
			//새 파일명 형식 지정
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS_");
			DecimalFormat df = new DecimalFormat("000");	//난수생성. 30 -> 030으로 자릿수 맞춰주는 역할
			
			//기존 파일명에서 확장자 추출
			String oldName = oldFile.getName();	//업로드한 파일의 이름가져오기
			String ext = "";	//확장자 분리하기
			int dot = oldName.lastIndexOf(".");	//마지막 닷(.)의 위치 찾기
			if(dot > -1)	//닷이 하나라도 존재한다면
				ext = oldName.substring(dot);	//.txt가 ext에 담김
			
			//새 파일명 생성
			String newName = sdf.format(new Date()) + df.format(Math.random() * 1000) + ext;
			
			//파일객체 새로 생성
			newFile = new File(oldFile.getParent(), newName);	//(부모디렉토리경로, 새로운파일객체) 경로는 같다. 
			
		} while (!creatNewFile(newFile));	//실패하면 다시 실행하도록.
				
		return newFile;
	}
	
	/**
	 * DefaultFileRenamePolicy에서 카피해옴.
	 * 
	 * - 실제 파일이 존재하지 않는경우. 파일생성 후 true 리턴.
	 * - 실제 파일이 존재하는 경우. 만들면 안됨 덮어쓰니까. IOException 발생! 
	 * 
	 * @param f
	 * @return
	 */
	private boolean creatNewFile(File f) {
		try {
			return f.createNewFile();
		} 
		catch(IOException ignored) {
			return false;
		}
		
	}

}
