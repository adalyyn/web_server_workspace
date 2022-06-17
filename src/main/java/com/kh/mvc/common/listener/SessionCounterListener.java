package com.kh.mvc.common.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @WebListener
 * - web.xml에 listener등록처리. 
 * - 따로 등록하지 않아도 된다.
 *
 */
@WebListener
public class SessionCounterListener implements HttpSessionListener {

	private static int activeSessions;
	
    /**
     * Default constructor. 
     */
    public SessionCounterListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     * 세션 생성되면 실행되는 이벤트리스너. 자동으로 톰캣에의해서 호출된다.
     */
    public void sessionCreated(HttpSessionEvent se)  { 
        activeSessions++; 
    	System.out.println("[sessionCreated] 현재 세션 수 : " + activeSessions);
    }

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent se)  { 
    	 if(activeSessions > 0)
    		 activeSessions--; 
     	System.out.println("[sessionDestloyed] 현재 세션 수 : " + activeSessions);
    }
	
}
