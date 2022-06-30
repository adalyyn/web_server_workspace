package com.kh.mvc.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;

public class HelloMvcUtils {

	
	/**
	 * 1. 암호화
	 * 2. 인코딩처리
	 * @param password
	 * @return
	 */
	public static String getEncryptedPassword(String rawPassword, String salt) {
		String encryptedPassword = null;
		
		try {
			//1. 암호화
			MessageDigest md = MessageDigest.getInstance("SHA-512");	//static메소드로 알고리즘을 전달.
			byte[] input = rawPassword.getBytes("utf-8");	//byte배열로 암호화처리
			byte[] saltBytes = salt.getBytes("utf-8");	//id값을 솔트값으로 처리	
			md.update(saltBytes);	//salt 전달
			byte[] encrytedBytes = md.digest(input);	//실제 암호화 메소드. 2진데이터 배열로 바뀜
			System.out.println(new String(encrytedBytes));	//깨진 데이터 객체 볼 수 있음
			
			//2. 인코딩처리
			Encoder encoder = Base64.getEncoder();	//인코더 객체 가져오기
			encryptedPassword = encoder.encodeToString(encrytedBytes);	//64개 문자열로 변환
			
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
				
		return encryptedPassword;
	}

	
	/**
	 * 준비물
	 * @param cPage 현재페이지
	 * @param numPerPage 페이지수
	 * @param totalContent 전체 목록개수
	 * @param url
	 * @return
	 * 
	 * totalPage : 전체 페이지 수
	 * pagebarSize : 한 페이지에 표시할 페이지 번호 개수 100페이지 중에 노출할 페이지 1~10페이지까지 보여주고 다음을 누르면 11~20
	 * pagebarStart ~ pagebarEnd : 시작번호와 마지막번호 정하기
	 * pageNo : 증감변수
	 * 
	 * 
	 * 영역 나눠서 처리하기 = 링크만들기
	 * 1. 이전영역 
	 * 2. pageNo영역
	 * 3. 다음영역
	 */
	public static String getPagebar(int cPage, int numPerPage, int totalContent, String url) {
		StringBuilder pagebar = new StringBuilder(); //계속 더할거라서 빌더로 만들기
//		url = "/mvc/admin/memberList?cPage=";
		url += (url.indexOf("?") < 0) ? "?cPage=" : "&cPage=" ;
		int totalPage = (int) Math.ceil((double) totalContent / numPerPage);  //121건이라면 13페이지가 필요
		//그냥 (totalContent / numPerPage) 하면 113/10 -> 정수연산은 정수값이 나옴!! 올림처리가 소용없어진다.
		int pagebarSize = 5; //1 2 3 4 5 / 6 7 8 9 10 / 11 12 이렇게 3가지 타입을 보여줌 pagebarStart는 1, 6, 11이됨. padebarEnd는 5, 10, 15
		int pagebarStart = ((cPage - 1) / pagebarSize * pagebarSize) + 1;
		int pagebarEnd = pagebarStart + pagebarSize - 1;
		int pageNo = pagebarStart;
		
		//이전영역 (1~5)일때는 이전으로 갈 수 없다.
		if(pageNo == 1) {
			
		}
		else {
			pagebar.append("<a href='"+ url + (pageNo - 1) +"'>이전</a>\n");		//개행도 해주기.
		}
		
		//pagdNo영역(1~5) (6~10) 보여주기
		while(pageNo <= pagebarEnd && pageNo <= totalPage) {
			//현재 페이지인경우 : 링크 만들어 줄 필요 없다.
			if(pageNo == cPage) {
				pagebar.append("<span class='cPage'>" + pageNo + "</span>\n");
			}
			//현재 페이지가 아닌경우
			else {
				pagebar.append("<a href='" + url + pageNo +"'>" + pageNo + "</a>\n");
			}			
			pageNo ++;
		}
		
		
		//다음영역
		if(pageNo > totalPage) {
			
		}
		else {
			pagebar.append("<a href='" + url + pageNo +"'>다음</a>\n");	//while문 탈출시 1이 더해진 상태로 나오기 떄문에 +1 하나고 그대로 쓴다.
		}		
		
		return pagebar.toString();
	}
	
	
	
	
	
	
	
}
