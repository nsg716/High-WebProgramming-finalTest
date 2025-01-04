/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.user.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.User;
import kr.ac.kku.cs.wp.wsd.nsg716.user.dao.UserDAO;


import java.util.List;


/**
 * UserController
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
@Controller
public class UserController {
    
    private static final Logger logger = LogManager.getLogger(UserController.class);
    
    @Autowired
    private UserDAO userDAO;

    @RequestMapping(value = "/user/userlist", method = RequestMethod.GET)
    public ModelAndView getUserList() {
        logger.entry();
        ModelAndView mav = new ModelAndView();
        try {
            List<User> users = userDAO.getUsers(null);
            mav.addObject("users", users);
            mav.setViewName("/user/userlist");
        } catch (Exception e) {
            logger.error("사용자 목록 조회 중 오류 발생: {}", e.getMessage());
            mav.addObject("errorMessage", "사용자 목록을 불러오는 중 오류가 발생했습니다.");
            mav.setViewName("/error/error");
        }
        return mav;
    }
    
    @RequestMapping(value = "/user/userlist/filter", method = RequestMethod.POST, consumes = "application/json")
    public ModelAndView filterUserList(@RequestBody(required = false) User filterParams) {
        logger.entry();
        ModelAndView mav = new ModelAndView();

        // 조건이 비어있는지 확인
        List<User> filteredUsers;
        if (filterParams == null || isEmptyFilter(filterParams)) {
            filteredUsers = userDAO.getAllUsers(); // 전체 사용자 리스트 가져오기
        } else {
            filteredUsers = userDAO.getUsers(filterParams); // 조건에 맞는 사용자 가져오기
        }

        mav.addObject("users", filteredUsers);
        mav.setViewName(filterParams != null ? "/user/userCard" : "/user/userlist");
        return mav;
    }

    // 필터가 비어있는지 확인하는 헬퍼 메서드
    private boolean isEmptyFilter(User filterParams) {
        return (filterParams.getUserId() == null || filterParams.getUserId().isEmpty()) &&
               (filterParams.getName() == null || filterParams.getName().isEmpty()) &&
               (filterParams.getEmail() == null || filterParams.getEmail().isEmpty()) &&
               (filterParams.getStatus() == null || filterParams.getStatus().isEmpty());
    }
}
    