/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.aaa.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Admin;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.User;
import kr.ac.kku.cs.wp.wsd.nsg716.support.sql.HibernateUtil;
import kr.ac.kku.cs.wp.wsd.nsg716.user.dao.AdminDAO;
import kr.ac.kku.cs.wp.wsd.nsg716.user.dao.UserDAO;
import kr.ac.kku.cs.wp.wsd.nsg716.user.dto.UserDTO;
/**
 * InfoController
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
@Controller
public class InfoController {
	public static Logger logger = LogManager.getLogger(LoginController.class);
    
	  @Autowired
	  private UserDAO userDAO;
	    
      @Autowired
      private AdminDAO adminDAO;
    
      @RequestMapping(value = "/user/myinfo", method = RequestMethod.GET)
      public ModelAndView myInfoGet(HttpServletRequest request) {
          logger.entry();
          ModelAndView mav = new ModelAndView();
          HttpSession session = request.getSession(false);
          
          if (session != null) {
              User user = (User) session.getAttribute("user");
              Admin admin = (Admin) session.getAttribute("admin");
              
              if (user != null) {
                  // 일반 사용자인 경우
                  User userInfo = userDAO.getUserById(user.getUserId());
                  mav.addObject("userInfo", userInfo);
                  mav.addObject("isAdmin", false);
                  mav.setViewName("/user/myinfo");
              } else if (admin != null) {
                  // 관리자인 경우 (필요한 경우 추가 권한 체크)
                  String targetUserId = request.getParameter("userId"); // URL 파라미터로 조회할 사용자 ID를 받을 수 있음
                  User userInfo;
                  
                  if (targetUserId != null) {
                      userInfo = userDAO.getUserById(targetUserId);
                  } else {
                      userInfo = userDAO.getUserById(admin.getId());
                  }
                  
                  mav.addObject("userInfo", userInfo);
                  mav.addObject("isAdmin", true);
                  mav.setViewName("/user/myinfo");
              } else {
                  logger.warn("Unauthorized access attempt to view user information");
                  mav.setViewName("redirect:/login");
              }
          } else {
              logger.warn("Session not found");
              mav.setViewName("redirect:/login");
          }
          return mav;
      }
      
      @RequestMapping(value = "/user/myinfo",  method = RequestMethod.POST)
      public ModelAndView myInfo(HttpServletRequest request) {
        logger.entry();
      	ModelAndView mav = new ModelAndView();
          HttpSession session = request.getSession(false);
          
          if (session != null) {
              User user = (User) session.getAttribute("user");
              if (user != null) {
                  // 사용자 정보를 가져와서 모델에 추가
                  User userInfo = userDAO.getUserById(user.getUserId());
                  mav.addObject("userInfo", userInfo);
                  mav.setViewName("/user/myinfo"); // myinfo.jsp로 포워딩
              } else {
                  mav.setViewName("redirect:/login"); // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
              }
          } else {
              mav.setViewName("redirect:/login");
          }
          return mav;
      }
      
      @RequestMapping(value = "/admin/admininfo",  method = RequestMethod.GET)
      public ModelAndView getAdminInfo(HttpServletRequest request) {
    	    logger.entry();
    	    ModelAndView mav = new ModelAndView();
    	    HttpSession session = request.getSession(false);

    	    try {
    	        if (session == null) {
    	            logger.warn("Session not found - possible unauthorized access attempt");
    	            return new ModelAndView("redirect:/login");
    	        }

    	        Admin admin = (Admin) session.getAttribute("admin");
    	        if (admin == null) {
    	            logger.warn("Admin object not found in session - possible unauthorized access attempt");
    	            return new ModelAndView("redirect:/login");
    	        }

    	        // XSS 방지를 위한 데이터 검증
    	        Admin adminInfo = adminDAO.getAdminById(admin.getId());
    	        if (adminInfo == null) {
    	            logger.error("Failed to retrieve admin information for ID: " + admin.getId());
    	            mav.addObject("errorMessage", "관리자 정보를 불러오는데 실패했습니다.");
    	            mav.setViewName("error/admin-error");
    	            return mav;
    	        }

    	        admin.setLastLogin(LocalDateTime.now());
    	        adminDAO.updateAdmin(admin);
    	        mav.addObject("adminInfo", adminInfo);
    	        mav.setViewName("admin/admininfo");
    	        
    	        return mav;

    	    } catch (Exception e) {
    	        logger.error("Error in getAdminInfo: " + e.getMessage(), e);
    	        mav.setViewName("error/admin-error");
    	        return mav;
    	    } finally {
    	        logger.exit();
    	    }
	  }
      
    @RequestMapping(value = "/user/editinfo", method = RequestMethod.POST)
    public ModelAndView editInfo(HttpServletRequest request) {
        logger.entry();
        ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession(false);

        if (session != null) {
            User user = (User) session.getAttribute("user");
            Admin admin = (Admin) session.getAttribute("admin");

            if (user != null || admin != null) {
            	User userInfo = userDAO.getUserById(user.getUserId());
                if (userInfo != null) {
                    // User 객체를 UserDTO로 변환
                    UserDTO userDTO = new UserDTO();
                    userDTO.setId(userInfo.getUserId());
                    userDTO.setName(userInfo.getName());
                    userDTO.setEmail(userInfo.getEmail());
                    userDTO.setPassword(userInfo.getPassword());
                    userDTO.setStatus(userInfo.getStatus());
                    userDTO.setPhoto(userInfo.getPhoto());
                    userDTO.setCreateDate(userInfo.getCreateDate());
                    userDTO.setUpdateDate(userInfo.getUpdateDate());

                    mav.addObject("userDTO", userDTO);
            	if (admin != null) {
                    // Admin의 경우             	
                    mav.addObject("isAdmin", true);                            
                    }                   
                    else {
                    	// 일반 사용자의 경우
                    mav.addObject("isAdmin", false);
                    }
                }
                mav.setViewName("/user/editinfo");
            } else {
                mav.setViewName("redirect:/login");
            }
        } else {
            mav.setViewName("redirect:/login");
        }
        return mav;
    }
    

    @RequestMapping(value = "/user/updateinfo", method = RequestMethod.POST)
    public ModelAndView updateInfo(HttpServletRequest request, @Valid @ModelAttribute("userDTO") UserDTO userDTO,BindingResult bindingResult) {
    	logger.entry();
        ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            User user = (User) session.getAttribute("user");
            Admin admin = (Admin) session.getAttribute("admin");

            // 세션에 유저나 관리자 정보가 없으면 로그인 페이지로 리다이렉트
            if (user == null && admin == null) {
                logger.warn("Unauthorized access attempt to update user information");
                mav.setViewName("redirect:/login");
                return mav;
            }

            // 관리자 여부 확인 및 사용자 ID 설정
            if (admin == null) {
                // 일반 사용자는 자신의 정보만 수정 가능
                userDTO.setId(user.getUserId());
            } else {
                logger.info("Admin {} is updating user information for user ID: {}", admin.getId(), userDTO.getId());
            }

            // ID 검증 - 'admin' 문자열 포함 여부 확인
            if (userDTO.getId() != null && userDTO.getId().toLowerCase().contains("admin")) {
                logger.warn("ID 검증 오류: admin은 포함할 수 없습니다. ID: {}", userDTO.getId());
                bindingResult.addError(new FieldError("userDTO", "id", "ID에 'admin'을 포함할 수 없습니다."));
            }

            // ID 중복 확인
            try (Session sqlSession = HibernateUtil.getSessionFactory().openSession()) {
                String query = "select u from User u where id = :userId";
                User existingUser = sqlSession.createQuery(query, User.class)
                                              .setParameter("userId", userDTO.getId())
                                              .uniqueResult();
                if (existingUser != null && (admin != null)) {
                    logger.warn("ID 중복 오류: 이미 사용 중인 ID입니다. ID: {}", userDTO.getId());
                    bindingResult.addError(new FieldError("userDTO", "id", "ID가 중복되었습니다."));
                }
            } catch (HibernateException e) {
                logger.error("ID 고유성 확인 중 오류 발생: {}", e.getMessage());
            }

            // 필드별 검증 오류 확인 및 값 초기화
            bindingResult.getFieldErrors().forEach(error -> {
                String fieldName = error.getField();
                logger.info("검증 오류 발생: 필드: {}, 메시지: {}", fieldName, error.getDefaultMessage());
                switch (fieldName) {
                    case "id":
                        userDTO.setId("");
                        break;
                    case "name":
                        userDTO.setName("");
                        break;
                    case "email":
                        userDTO.setEmail("");
                        break;
                    case "password":
                        userDTO.setPassword("");
                        break;
                }
                request.setAttribute(fieldName + "Error", error.getDefaultMessage());
            });

            // 검증 실패 시 수정 페이지로 이동
            if (bindingResult.hasErrors()) {
                logger.info("검증 오류 발생. 오류 수: {}", bindingResult.getErrorCount());
                mav.addObject("userInfo", userDTO);
                mav.setViewName("/user/editinfo");
                return mav;
            }

            // 유효성 검사를 통과한 경우 업데이트 진행
            User updatedUser = new User(); // 새로운 User 객체 생성
            updatedUser.setUserId(userDTO.getId());
            updatedUser.setName(userDTO.getName());
            updatedUser.setEmail(userDTO.getEmail());
            updatedUser.setPassword(userDTO.getPassword());
            
            updatedUser.setStatus("active");
            updatedUser.setPhoto(null);
            updatedUser.setUpdateDate(LocalDateTime.now());

            userDAO.updateUser(updatedUser); // 업데이트 메서드 호출
 
            if (session != null) {
                session.setAttribute("user", updatedUser);
            }
            mav.setViewName("redirect:/");
            return mav;
        }

        // 세션이 없는 경우 로그인 페이지로 리다이렉트
        logger.warn("Unauthorized access attempt to update user information");
        mav.setViewName("redirect:/login");
        return mav;
    }
    
    
    
    

    @RequestMapping(value = "/user/deleteinfo", method = RequestMethod.GET)
    public ModelAndView deleteInfo(HttpServletRequest request) {
    	logger.entry();
        ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession(false);

        if (session != null) {
            User user = (User) session.getAttribute("user");
            Admin admin = (Admin) session.getAttribute("admin");

            if (user != null || admin != null) {
                String targetUserId;

                if (admin != null) {
                    targetUserId = request.getParameter("userId");
                    if (targetUserId == null) {
                        targetUserId = admin.getId();
                    }
                    logger.info("Admin {} is deleting user information for user ID: {}", admin.getId(), targetUserId);
                    
                    User userToDelete = userDAO.getUserById(targetUserId);
                    if (userToDelete != null) {
                        userDAO.deleteUser(userToDelete); // 사용자 삭제 메서드 호출
                
                        logger.info("User {} has been deleted by admin {}", targetUserId, admin.getId());
                    } else {
                    	logger.info("User {} ", targetUserId);
                    }

                } else {
                
                    targetUserId = user.getUserId();
                    logger.info("User {} is deleting their information", targetUserId);
                    
                    // 사용자 정보 업데이트 (status를 inactive로 설정)
                    User userToUpdate = userDAO.getUserById(targetUserId);
                    if (userToUpdate != null) {
                        userToUpdate.setStatus("inactive");
                        userToUpdate.setUpdateDate(LocalDateTime.now());
                        userDAO.updateUser(userToUpdate);
                        
                        session.invalidate();
                        mav.setViewName("redirect:/");
                        return mav;
                    }
                }

                mav.setViewName("redirect:/");
                return mav;
            }
        }

        logger.warn("Unauthorized access attempt to delete user information");
        mav.setViewName("redirect:/login");
        return mav;
    }

}
