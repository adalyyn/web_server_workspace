package com.kh.mvc.common.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * Servlet Filter implementation class EncordingFilter
 */
//@WebFilter("/*")
public class EncordingFilter implements Filter {

	private String encodingType;
	
    /**
     * Default constructor. 
     */
    public EncordingFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		request.setCharacterEncoding(encodingType);
		
//		System.out.println("[encodingFilter : " + encodingType + "처리]");
		
		chain.doFilter(request, response);
	}

	/**
	 * web.xml에서 읽어옴
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		this.encodingType = fConfig.getInitParameter("encordingType");
	}

}
