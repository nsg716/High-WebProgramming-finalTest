/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.aaa.controller;

import java.time.LocalDateTime;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.ac.kku.cs.wp.wsd.nsg716.support.sql.HibernateUtil;
import kr.ac.kku.cs.wp.wsd.nsg716.user.dao.AdminDAO;
import kr.ac.kku.cs.wp.wsd.nsg716.user.dto.UserDTO;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Admin;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.User;
/**
 * SignupController
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
@Controller
public class SignupController {

	public static Logger logger = LogManager.getLogger(LoginController.class);

	 @Autowired
     private AdminDAO adminDAO;
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public ModelAndView signup() {
		logger.entry();
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/auth/signup"); // signup.jsp로 포워딩
        return mav;
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ModelAndView signupPost(@Valid @ModelAttribute("userDTO") UserDTO userDTO, BindingResult bindingResult, HttpServletRequest request) {
		logger.entry();
		ModelAndView mav = new ModelAndView();
		
		HttpSession session = request.getSession(false);
		Admin admin = (Admin)session.getAttribute("admin");
	    // ID 검증 - admin 문자열 포함 여부 확인
 		if (userDTO.getId() != null && userDTO.getId().toLowerCase().contains("admin")) {
 			logger.warn("ID 검증 오류: admin은 포함할 수 없습니다. ID: {}", userDTO.getId());
 			bindingResult.addError(new FieldError("userDTO", "id", "ID에 'admin'을 포함할 수 없습니다."));
 			userDTO.setId(""); // ID 초기화
 		}
	    // ID 중복 확인
	    Session sqlsession = HibernateUtil.getSessionFactory().openSession();
	    try {
	        String existingUserQuery = "select u from User u where ID = :userId";
	        User existingUser = sqlsession.createQuery(existingUserQuery, User.class)
	                                   .setParameter("userId", userDTO.getId())
	                                   .uniqueResult();
	        if (existingUser != null) {
	            logger.warn("ID 중복 오류: 이미 사용 중인 ID입니다. ID: {}", userDTO.getId());
	            bindingResult.addError(new FieldError("userDTO", "id", "ID가 중복되었습니다."));
	            userDTO.setId(""); // ID 초기화
	        }
	    } catch (HibernateException e) {
	        logger.error("ID 고유성 확인 중 오류 발생: {}", e.getMessage());
	    } finally {
	    	sqlsession.close();
	    }

	    // 검증 오류가 있는 경우 확인 
	    bindingResult.getFieldErrors().forEach(error -> {
	        String errorMessage = error.getDefaultMessage();
	        logger.info("검증 오류 발생: 필드: {}, 오류 메시지: {}", error.getField(), errorMessage);
	        request.setAttribute(error.getField() + "Error", errorMessage);
	        
	        // 오류가 발생한 필드의 값을 초기화
	        if ("id".equals(error.getField())) {
	            userDTO.setId("");
	        } else if ("name".equals(error.getField())) {
	            userDTO.setName("");
	        } else if ("email".equals(error.getField())) {
	            userDTO.setEmail("");
	        } else if ("password".equals(error.getField())) {
	            userDTO.setPassword("");
	        }
	    });

	    if (bindingResult.hasErrors()) {
	        logger.info("회원가입 검증 오류 발생. 오류 수: {}", bindingResult.getErrorCount());
        	mav.setViewName("/auth/signup");
//	        return "/auth/signup"; // 검증 오류 시 회원가입 페이지로 이동
	        return mav;
	    }

	    // 사용자 저장 로직
	    Transaction tx = null;
	    try {

	    	sqlsession = HibernateUtil.getSessionFactory().openSession();
	        tx = sqlsession.beginTransaction();

	        User user = new User();
	        user.setUserId(userDTO.getId());
	        user.setName(userDTO.getName());
	        user.setEmail(userDTO.getEmail());
	        user.setPassword(userDTO.getPassword());
	        user.setStatus("active");
	        user.setPhoto(null);
	        user.setCreateDate(LocalDateTime.now());
	        user.setUpdateDate(LocalDateTime.now());

	        sqlsession.persist(user);
	        tx.commit();
			if (session != null) {
			   if (admin == null) {
			        logger.debug("세션에 Admin 객체가 없습니다.");
			    } else {
			        logger.debug("관리자 접속 허용: {}", admin.getId());
			        logger.debug("사용자 등록 성공: {}", user.getUserId());
			        mav.setViewName("redirect:/");
			        return mav;
			    } 
			   
			}
	        logger.debug("사용자 등록 성공: {}", user.getUserId());
	        mav.setViewName("/auth/login");
	        
	    } catch (Exception e) {
	        logger.error("회원가입 중 오류 발생: {}", e.getMessage());
	        if (tx != null && tx.isActive()) {
	            tx.rollback();
	        }
	        request.setAttribute("errorMessage", "회원가입 중 오류가 발생했습니다.");

	        mav.setViewName("/auth/signup");
//	        return "/auth/signup";
	    } finally {
	        if (sqlsession != null && sqlsession.isOpen()) {
	        	sqlsession.close();
	        }
	    }
	     return mav;
	}
}