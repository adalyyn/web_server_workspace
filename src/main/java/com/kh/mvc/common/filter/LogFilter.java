package com.kh.mvc.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter 생명주기
 * - 객체생성 (생성자 호출)
 * - init (필터객체 전처리)
 * - doFilter (실제처리코드)
 * - destroy(메모리 반환 전 처리)
 */
//@WebFilter("/*") 순서 정하기 어려워져서 버림
public class LogFilter implements Filter {

    /**
     * Default constructor. 
     */
    public LogFilter() {
//        System.out.println("[LogFilter 생성]");
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
//		System.out.println("[LogFilter destroy]");
	}

	/**
	 * 필터를 통해  처리할 코드를 작성하는 곳.
	 * - 전처리
	 * - 후처리
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	
		//doFilter를 기준으로 전처리
		HttpServletRequest httpReq = (HttpServletRequest) request;
		String uri = httpReq.getRequestURI(); // /mvc/member/enroll
		String method = httpReq.getMethod();
		
		System.out.println("================================================");
		System.out.printf("%s %s%n", method, uri);
		System.out.println("------------------------------------------------");
		
		// filter chain의 다음 filter를 호출(마지막 필터라면 servlet 호출)
		chain.doFilter(request, response);
		
		// 후처리
		HttpServletResponse httpRes = (HttpServletResponse) response; 
		System.out.println("________________________________________________");
		System.out.println(httpRes.getStatus());
		System.out.println();
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
//		System.out.println("[LogFilter init]");
	}

}
