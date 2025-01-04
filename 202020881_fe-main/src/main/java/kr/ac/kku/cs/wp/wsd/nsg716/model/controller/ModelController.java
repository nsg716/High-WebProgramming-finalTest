/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.model.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.ac.kku.cs.wp.wsd.nsg716.model.dao.ModelDAO;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Admin;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Model;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.User;


import java.util.List;
/**
 * ModelController
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
@Controller
public class ModelController {
    private static final Logger logger = LogManager.getLogger(ModelController.class);

    @Autowired
    private ModelDAO modelDao;
    
    @RequestMapping(value = "/model/modellist", method = RequestMethod.GET)
    public ModelAndView modelList(HttpServletRequest request) {
    	logger.entry();
    	ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession(false);

        if (session != null) {
        	User user = (User) session.getAttribute("user");
            Admin admin = (Admin) session.getAttribute("admin");
            if (admin != null || user != null) {
                // Fetch all models for the admin
                List<Model> models = modelDao.getModels(null);
               
                mav.addObject("models", models);
                mav.setViewName("model/modellist");
            } else {
                logger.warn("Unauthorized access attempt to view model list");
                mav.setViewName("/error/error");
            }
        } else {
            logger.warn("Session not found");
            mav.setViewName("redirect:/login");
        }
        return mav;
        
    }

    @RequestMapping(value = "/model/modellist/filter", method = RequestMethod.POST, consumes = "application/json")
    public ModelAndView filterModelList(@RequestBody(required = false) Model filterParams) {
        logger.entry();
        ModelAndView mav = new ModelAndView();

        // 조건이 비어있는지 확인
        List<Model> filteredModels;
        if (filterParams == null || isEmptyFilter(filterParams)) {
            filteredModels = modelDao.getAllModels(); // 전체 모델 리스트 가져오기
        } else {
            filteredModels = modelDao.getModels(filterParams); // 조건에 맞는 모델 가져오기
        }

        mav.addObject("models", filteredModels);
        mav.setViewName(filterParams != null ? "/model/modelCard" : "/model/modellist");
        return mav;
    }

    // 필터가 비어있는지 확인하는 헬퍼 메서드
    private boolean isEmptyFilter(Model filterParams) {
        return (filterParams.getModelName() == null || filterParams.getModelName().isEmpty()) &&
               (filterParams.getVersion() == null || filterParams.getVersion().isEmpty()) &&
               (filterParams.getModelType() == null || filterParams.getModelType().isEmpty())&&
               (filterParams.getFramework() == null || filterParams.getFramework().isEmpty());
    }
  
    
}