package com.kh.mvc.common.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import com.kh.mvc.member.model.dto.Member;

/**
 * Application Lifecycle Listener implementation class MemberLoginListner
 *
 */
@WebListener
public class MemberLoginListener implements HttpSessionAttributeListener {

    /**
     * Default constructor. 
     */
    public MemberLoginListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * 세션에 등록된 loginMember 추적하기
     * 	- 속성 추가될때 호출된다.
     * 	- 특정 속성객체 가져오기
     */
    public void attributeAdded(HttpSessionBindingEvent se)  { 
         String name = se.getName();
         Object value = se.getValue();
//         System.out.println("[attributeAdded]" + name + " = " + value);
         
         if("loginMember".equals(name)) {
        	 //로그인한 회원정보 처리
        	 Member loginMember =(Member) value;
//        	 System.out.println("[회원로그인]" + loginMember.getMemberId() + "로그인!" );
         }
    }

	/**
     * 제거된 속성 추적
     */
    public void attributeRemoved(HttpSessionBindingEvent se)  { 
    	  String name = se.getName();
          Object value = se.getValue();
//          System.out.println("[attributeRemoved]" + name + " = " + value);
          
          if("loginMember".equals(name)) {
         	 //로그인한 회원정보 처리
         	 Member loginMember =(Member) value;
//         	 System.out.println("[회원로그아웃]" + loginMember.getMemberId() + "로그아웃!" );
          }
    }

	/**
     * 동일한 이름으로 다른객체 덮어쓰기할때 사용하는 메소드
     */
    public void attributeReplaced(HttpSessionBindingEvent se)  { 
         // TODO Auto-generated method stub
    }
	
}
