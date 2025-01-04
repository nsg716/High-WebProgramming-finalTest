/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.aaa.controller;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.ac.kku.cs.wp.wsd.nsg716.tools.message.MessageException;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Admin;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.User;

/**
 * AuthenticationFilter
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */

@WebFilter(urlPatterns = {"/model/*","/report/*"})
public class AuthenticationFilter implements Filter{
	private static final Logger logger = LogManager.getLogger(AuthenticationFilter.class);
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
	        throws IOException, ServletException {
		logger.entry();
	    HttpServletRequest req = (HttpServletRequest) request;
	    HttpServletResponse res = (HttpServletResponse) response;
	    HttpSession session = req.getSession(false); // 세션이 없을 경우 null 반환

	    if (session != null) {
	        User user = (User) session.getAttribute("user");
	        Admin admin = (Admin) session.getAttribute("admin");
	        if (user != null) {
	            if ("active".equalsIgnoreCase(user.getStatus())) {
	                logger.info("{} accessed {} ", user.getUserId(), req.getRequestURI());
	                chain.doFilter(request, response); // 요청을 통과시킴
	            } else{
	            	
	            	logger.warn("Inactive user tried to access: {}", req.getRequestURI());
	                throw new MessageException("status_required");
	            }
	        } else {
                chain.doFilter(request, response); // 요청을 통과시킴
            }
        }
    }
}


