/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.aaa.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.ac.kku.cs.wp.wsd.nsg716.aaa.controller.LoginController;
import kr.ac.kku.cs.wp.wsd.nsg716.user.dao.AdminDAO;
import kr.ac.kku.cs.wp.wsd.nsg716.user.dao.UserDAO;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Admin;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.User;
/**
 * LoginController
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
@Controller
public class LoginController {
	public static Logger logger = LogManager.getLogger(LoginController.class);
	
	@Autowired
    private UserDAO userDAO;
	
	@Autowired
    private AdminDAO adminDAO;
	
	
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login() {
		logger.entry();
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/auth/login"); // login.jsp로 포워딩
        return mav;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request) {
		logger.entry();
		ModelAndView mav = new ModelAndView();
	    // 사용자 로그인 로직
	    User user = userDAO.getUserById(username);
	    Admin admin = adminDAO.getAdminById(username); // Admin 정보 가져오기
	    if (user != null && password.equals(user.getPassword())) { // User 비밀번호 비교
	        HttpSession session = request.getSession();
	        session.setAttribute("user", user); // User 객체를 세션에 저장

	        logger.info("{} has logged in as User", username);
	        mav.setViewName("redirect:/"); // User 로그인 성공 시 메인 페이지로 리다이렉트
	    } 
	    // Admin 로그인 로직
	    else if (admin != null && password.equals(admin.getPassword())) {
	        HttpSession session = request.getSession();
	        session.setAttribute("admin", admin); // Admin 객체를 세션에 저장

	        logger.info("{} has logged in as Admin", username);
	        mav.setViewName("redirect:/"); // Admin 로그인 성공 시 대시보드로 리다이렉트
	    } 
	    // 로그인 실패
	    else {
	        logger.info("{} failed to log in", username);
	        mav.setViewName("/auth/login"); // 로그인 실패 시 다시 로그인 페이지로
	        mav.addObject("error", "login_fail");
	    }
	    return mav;
	}


	
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ModelAndView logout(HttpServletRequest request) {
    	logger.entry();
    	ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession(false);
        if (session != null) {
            // User 객체를 세션에서 가져옴
            User user = (User) session.getAttribute("user");
            Admin admin = (Admin) session.getAttribute("admin");

            // User로 로그아웃
            if (user != null) {
                String username = user.getName();
                logger.info("{} has logged out as User", username);
                session.invalidate();
            }
            // Admin으로 로그아웃
            else if (admin != null) {
                String username = admin.getName();
                logger.info("{} has logged out as Admin", username);
                session.invalidate();
            }
        }
        mav.setViewName("redirect:/login"); // 로그아웃 후 로그인 페이지로 리다이렉트
        return mav;
    }

	
}
