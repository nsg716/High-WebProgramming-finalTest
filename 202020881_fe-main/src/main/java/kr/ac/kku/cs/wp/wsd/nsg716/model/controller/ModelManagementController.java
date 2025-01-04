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
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.ac.kku.cs.wp.wsd.nsg716.model.dao.ModelDAO;
import kr.ac.kku.cs.wp.wsd.nsg716.model.dto.ModelDTO;
import kr.ac.kku.cs.wp.wsd.nsg716.support.sql.HibernateUtil;
import kr.ac.kku.cs.wp.wsd.nsg716.user.dto.UserDTO;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Admin;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Model;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;
/**
 * ModelManagementController
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
@Controller
public class ModelManagementController {
    private static final Logger logger = LogManager.getLogger(ModelManagementController.class);

    @Autowired
    private ModelDAO modelDao;

    @RequestMapping(value = "/modelcreate", method = RequestMethod.GET)
    public ModelAndView showModelCreatePage(HttpServletRequest request) {
    	logger.entry();
    	ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession(false);

        if (session != null) {
            Admin admin = (Admin) session.getAttribute("admin");
            if (admin != null) {
                mav.setViewName("/model/modelcreate");
            } else {
                logger.warn("Unauthorized access attempt to create model");
                mav.setViewName("redirect:/login");
            }
        } else {
            logger.warn("Session not found");
            mav.setViewName("redirect:/login");
        }
        return mav;
    }

    @RequestMapping(value = "/modelcreate", method = RequestMethod.POST)
    public ModelAndView createModel(@Valid @ModelAttribute("modelDTO") ModelDTO modelDTO, BindingResult bindingResult, HttpServletRequest request) {
    	 ModelAndView mav = new ModelAndView();
    	    HttpSession session = request.getSession(false);
    	    
    	    if (session != null) {
    	        Admin admin = (Admin) session.getAttribute("admin");
    	       
    	        if (admin != null) {
    	            // 모델 ID 중복 체크
    	            if (modelDao.isModelIdDuplicate(modelDTO.getModelId())) {
    	                mav.addObject("modelIdError", "이미 존재하는 모델 ID입니다.");
    	                mav.setViewName("/model/modelcreate");
    	                return mav;
    	            }
    	            
    	            Transaction tx = null;
    	            Session sqlSession = null;
    	            try {
    	                sqlSession = HibernateUtil.getSessionFactory().openSession();
    	                tx = sqlSession.beginTransaction();
    	                
    	                Model model = new Model();
    	                if (modelDTO.getModelId() == null || modelDTO.getModelId().isEmpty()) {
    	                    model.setModelId(UUID.randomUUID().toString());
    	                } else {
    	                    model.setModelId(modelDTO.getModelId());
    	                }
    	                model.setAdminId(admin.getId());
    	                model.setModelName(modelDTO.getModelName());
    	                model.setVersion(modelDTO.getVersion());
    	                model.setModelType(modelDTO.getModelType());
    	                model.setFramework(modelDTO.getFramework());
    	                model.setDescription(modelDTO.getDescription());
    	                model.setCreateDate(LocalDateTime.now());
    	                model.setUpdateDate(LocalDateTime.now());
    	                sqlSession.persist(model);
    	                tx.commit();
    	                mav.setViewName("redirect:/");
    	            } catch (Exception e) {
    	                if (tx != null) tx.rollback();
    	                logger.error("모델 생성 중 오류 발생", e);
    	                mav.addObject("errorMessage", "모델 생성 중 오류가 발생했습니다.");
    	                mav.setViewName("/model/modelcreate");
    	            } finally {
    	                if (sqlSession != null) {
    	                    sqlSession.close();
    	                }
    	            }
    	        } else {
    	            mav.setViewName("redirect:/login");
    	        }
    	    } else {
    	        mav.setViewName("redirect:/login");
    	    }
    	    
    	    return mav;
    	}
    @RequestMapping(value = "/model/info", method = RequestMethod.GET)
    public ModelAndView infoModel(HttpServletRequest request) {
        logger.entry();
        ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            Admin admin = (Admin) session.getAttribute("admin");
            
            if (admin != null) {
                // 관리자인 경우
                String targetModelId = request.getParameter("modelId"); // URL 파라미터로 조회할 모델 ID를 받음
                logger.debug("modelId : {},targetModelId");
                Model modelInfo;
                
                if (targetModelId != null) {
                    modelInfo = modelDao.getModelById(targetModelId);
                } else {
                    // 기본적으로 관리자 자신의 모델 정보를 조회 (필요에 따라 수정)
                	logger.debug("관리자 모델 정보 조회 방법");
                	modelInfo = modelDao.getModelById(admin.getId());
                	
                }
                
                if (modelInfo != null) {
                    mav.addObject("modelInfo", modelInfo);
                    mav.setViewName("/model/modelinfo");
                } else {
                    logger.warn("Model information not found");
                    mav.setViewName("/error/error");
                }
            } else {
                logger.warn("Unauthorized access attempt to view model information");
                mav.setViewName("redirect:/login");
            }
        } else {
            logger.warn("Session not found");
            mav.setViewName("redirect:/login");
        }
        return mav;
    }
    
    @RequestMapping(value = "/model/modeleditinfo", method = RequestMethod.POST)
    public ModelAndView editModelInfo(HttpServletRequest request) {
        logger.entry();
        ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession(false);

        if (session != null) {
            Admin admin = (Admin) session.getAttribute("admin");

            if (admin != null) {
                // 관리자로부터 ModelDTO 정보 가져오기
                String modelId = request.getParameter("modelId");
                if (modelId != null && !modelId.isEmpty()) {
                    Model modelDTO = modelDao.getModelById(modelId);

                    if (modelDTO != null) {
                        // ModelDTO 객체 설정
                        mav.addObject("modelDTO", modelDTO);
                        mav.addObject("isAdmin", true);
                        mav.setViewName("/model/modeleditinfo");
                    } 
                } 
            } else {
                mav.addObject("error", "관리자 권한이 필요합니다.");
                mav.setViewName("redirect:/login");
            }
        } else {
            mav.addObject("error", "세션이 만료되었습니다. 다시 로그인하세요.");
            mav.setViewName("redirect:/login");
        }

        logger.exit();
        return mav;
    }
    
    @RequestMapping(value = "/model/updateinfo", method = RequestMethod.POST)
    public ModelAndView updateModelInfo(HttpServletRequest request, 
                                        @Valid @ModelAttribute("modelDTO") ModelDTO modelDTO, 
                                        BindingResult bindingResult) {
        logger.entry();
        ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession(false);

        if (session != null) {
            Admin admin = (Admin) session.getAttribute("admin");

            // 관리자 세션 확인
            if (admin == null) {
                logger.warn("Unauthorized access attempt to update model information");
                mav.setViewName("redirect:/login");
                return mav;
            }

            // Model ID 검증
            if (modelDTO.getModelId() == null || modelDTO.getModelId().isEmpty()) {
                logger.warn("Invalid Model ID");
                bindingResult.addError(new FieldError("modelDTO", "modelId", "Model ID는 필수 입력값입니다."));
            }

            // 필드 검증 오류 처리
            bindingResult.getFieldErrors().forEach(error -> {
                String fieldName = error.getField();
                logger.info("Validation error: field: {}, message: {}", fieldName, error.getDefaultMessage());
                request.setAttribute(fieldName + "Error", error.getDefaultMessage());
            });

            if (bindingResult.hasErrors()) {
                logger.info("Validation errors found: {}", bindingResult.getErrorCount());
                mav.addObject("modelInfo", modelDTO);
                mav.setViewName("/model/modeleditinfo");
                return mav;
            }

            // 모델 정보 업데이트 처리
            try (Session sqlSession = HibernateUtil.getSessionFactory().openSession()) {
                Transaction tx = sqlSession.beginTransaction();

                Model model = modelDao.getModelById(modelDTO.getModelId());
                if (model != null) {
                    model.setModelName(modelDTO.getModelName());
                    model.setVersion(modelDTO.getVersion());
                    model.setModelType(modelDTO.getModelType());
                    model.setFramework(modelDTO.getFramework());
                    model.setDescription(modelDTO.getDescription());
   
                    model.setUpdateDate(LocalDateTime.now());

                    sqlSession.update(model);
                    tx.commit();

                    mav.setViewName("redirect:/");
                } else {
                    logger.warn("Model with ID {} not found", modelDTO.getModelId());
                    mav.addObject("errorMessage", "해당 모델 정보를 찾을 수 없습니다.");
                    mav.setViewName("/model/modeleditinfo");
                }
            } catch (Exception e) {
                logger.error("Error updating model information", e);
                mav.addObject("errorMessage", "모델 정보 업데이트 중 오류가 발생했습니다.");
                mav.setViewName("/model/modeleditinfo");
            }
        } else {
            logger.warn("Session not found");
            mav.setViewName("redirect:/login");
        }

        logger.exit();
        return mav;
    }

    @RequestMapping(value = "/model/delete", method = RequestMethod.GET)
    public ModelAndView deleteModel(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession(false);

        if (session != null) {
            Admin admin = (Admin) session.getAttribute("admin");
            String targetModelId;
            if (admin != null) {
                
                targetModelId = request.getParameter("modelId");
                Model model = modelDao.getModelById(targetModelId);
                
                // Ensure the model belongs to the current admin
                if (model != null) {
                    modelDao.deleteModel(model);
                    mav.setViewName("redirect:/");
                } else {
                	mav.setViewName("redirect:/login");
                }
            } else {
            	mav.setViewName("redirect:/login");
            }
        } else {
            mav.setViewName("redirect:/login");
        }
        
        return mav;
    }
    
    

}