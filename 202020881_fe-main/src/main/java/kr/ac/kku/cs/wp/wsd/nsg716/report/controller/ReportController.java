/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.report.controller;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.ac.kku.cs.wp.wsd.nsg716.model.dao.ModelDAO;
import kr.ac.kku.cs.wp.wsd.nsg716.model.dto.ModelDTO;
import kr.ac.kku.cs.wp.wsd.nsg716.report.dao.ReportDAO;
import kr.ac.kku.cs.wp.wsd.nsg716.report.dto.ReportDTO;
import kr.ac.kku.cs.wp.wsd.nsg716.support.sql.HibernateUtil;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Admin;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Model;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Report;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.User;
/**
 * ReportController
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
@Controller
public class ReportController {

    private static final Logger logger = LogManager.getLogger(ReportController.class);

    @Autowired
    private ModelDAO modelDao;
    
    @Autowired
    private ReportDAO reportDao;
   
    @RequestMapping(value = "/report/reportcreate", method = RequestMethod.GET)
    public ModelAndView showReportCreatePage(HttpServletRequest request) {
    	logger.entry();
    	ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession(false);

        if (session != null) {
          	User user = (User) session.getAttribute("user");
        	Admin admin = (Admin) session.getAttribute("admin");
        	 String targetModelId = request.getParameter("modelId"); // URL 파라미터로 조회할 모델 ID를 받음
             logger.debug("result :{}" ,targetModelId);
             Model model = modelDao.getModelById(targetModelId);
             
             
        		
            if (admin != null || user != null) {
            	mav.addObject("model", model); // 모델 객체 추가
                mav.setViewName("/report/reportcreate");
            } else {
                logger.warn("Unauthorized access attempt to create report");
                mav.setViewName("redirect:/login");
            }
        } else {
            logger.warn("Session not found");
            mav.setViewName("redirect:/login");
        }
        return mav;
    }

    @RequestMapping(value = "/reportcreate", method = RequestMethod.POST)
    public ModelAndView createReport(@Valid @ModelAttribute("reportDTO") ReportDTO reportDTO, 
                                     BindingResult bindingResult, HttpServletRequest request) {
    	logger.entry();
    	ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession(false);

        if (session != null) {
        	User user = (User) session.getAttribute("user");
        	Admin admin = (Admin) session.getAttribute("admin");
      
            if (admin != null || user != null) {
                // 리포트 ID 중복 체크
                if (reportDao.isReportIdDuplicate(reportDTO.getReportId())) {
                    mav.addObject("reportIdError", "이미 존재하는 리포트 ID입니다.");
                    mav.setViewName("/report/reportcreate");
                    return mav;
                }
	        	
                Transaction tx = null;
                Session sqlSession = null;
                Model model = modelDao.getModelById(request.getParameter("modelId"));
                
          
                
                String adminId = request.getParameter("adminId");
                String modelName = request.getParameter("modelName");
                
                
                try {
                    sqlSession = HibernateUtil.getSessionFactory().openSession();
                    tx = sqlSession.beginTransaction();

                    Report report = new Report();

	                 // Report ID 처리
	                 if (reportDTO.getReportId() == null || reportDTO.getReportId().isEmpty()) {
	                     report.setReportId(UUID.randomUUID().toString());
	                 } else {
	                     report.setReportId(reportDTO.getReportId());
	                 }
	                 report.setModel(model.getModelId());
	                 report.setAdmin(adminId);
	                 report.setUser(user.getUserId());
	                 
	                 report.setReportName(reportDTO.getReportName());
	                 report.setUsageDate(reportDTO.getUsageDate());
	                 report.setModelName(modelName);
	                 report.setAccuracy(reportDTO.getAccuracy());
	                 report.setTotalProcessingAmount(reportDTO.getTotalProcessingAmount());
	                 report.setAverageResponseTime(reportDTO.getAverageResponseTime());
	                 report.setCreateDate(LocalDateTime.now());
	                 report.setUpdateDate(LocalDateTime.now());

                    sqlSession.persist(report);
                    tx.commit();
                    mav.setViewName("redirect:/"); // 생성 완료 후 메인 페이지로 이동
                } catch (Exception e) {
                    if (tx != null) tx.rollback();
                    logger.error("리포트 생성 중 오류 발생", e);
                    mav.addObject("errorMessage", "리포트 생성 중 오류가 발생했습니다.");
                    mav.setViewName("/report/reportcreate");
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
    
    @RequestMapping(value = "/report/reportlist", method = RequestMethod.GET)
    public ModelAndView reportList(HttpServletRequest request) {
    	logger.entry();    
    	ModelAndView mav = new ModelAndView();
	        HttpSession session = request.getSession(false);
	
	        if (session != null) {
	        	User user = (User) session.getAttribute("user");
	            Admin admin = (Admin) session.getAttribute("admin");
	            if (admin != null || user != null) {
	                // Fetch all models for the admin
	                List<Report> Report = reportDao.getReports(null);
	               
	                mav.addObject("reports", Report);
	                mav.setViewName("report/reportlist");
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
    @RequestMapping(value = "/report/reportlist/filter", method = RequestMethod.POST, consumes = "application/json")
    public ModelAndView filterModelList(@RequestBody(required = false) Report filterParams) {
        logger.entry();
        ModelAndView mav = new ModelAndView();

        // 조건이 비어있는지 확인
        List<Report> filteredModels;
        if (filterParams == null || isEmptyFilter(filterParams)) {
            filteredModels = reportDao.getAllReports(); // 전체 모델 리스트 가져오기
        } else {
            filteredModels = reportDao.getReports(filterParams); // 조건에 맞는 모델 가져오기
        }

        mav.addObject("reports", filteredModels);
        mav.setViewName(filterParams != null ? "/report/reportCard" : "/report/reportlist");
        return mav;
    }


    // 필터가 비어있는지 확인하는 헬퍼 메서드
    private boolean isEmptyFilter(Report filterParams) {
        return (filterParams.getReportName() == null || filterParams.getReportName().isEmpty()) &&
        	   (filterParams.getModelName() == null || filterParams.getModelName().isEmpty()) &&
               (filterParams.getAccuracy() == null) &&
               (filterParams.getTotalProcessingAmount() == null) &&
               (filterParams.getAverageResponseTime() == null) ;
    }
    
    
    
    @RequestMapping(value = "/report/info", method = RequestMethod.GET)
    public ModelAndView infoModel(HttpServletRequest request) {
        logger.entry();
        ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession(false);
        
        if (session != null) {
        	User user = (User) session.getAttribute("user");
            Admin admin = (Admin) session.getAttribute("admin");
            
            if (admin != null || user != null) {
                // 관리자인 경우
                String targetreportId = request.getParameter("reportId"); // URL 파라미터로 조회할 모델 ID를 받음
                Report reportInfo;
                
                if (targetreportId != null) {
                	reportInfo = reportDao.getReportById(targetreportId);
                
	                if (reportDao.getReportById(targetreportId) != null) {
	                    mav.addObject("reportInfo", reportInfo);
	                    mav.setViewName("/report/reportinfo");
	                } else {
	                    logger.warn("Model information not found");
	                    mav.setViewName("/error/error");
	                }
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
    
    @RequestMapping(value = "/report/reporteditinfo", method = RequestMethod.POST)
    public ModelAndView editReportInfo(HttpServletRequest request) {
        logger.entry();
        ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession(false);
        
        if (session != null) {
        	 String reportId = request.getParameter("reportId");
        	 if (reportId != null && !reportId.isEmpty()) {
                 Report reportDTO = reportDao.getReportById(reportId);

                 if (reportDTO != null) {
                     // ModelDTO 객체 설정
                     mav.addObject("reportDTO", reportDTO);
                     mav.addObject("isAdmin", true);
                     mav.setViewName("/report/reporteditinfo");
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

    @RequestMapping(value = "/report/updateinfo", method = RequestMethod.POST)
    public ModelAndView updateReportInfo(HttpServletRequest request, 
							            @Valid @ModelAttribute("reportDTO") ReportDTO reportDTO, 
							            BindingResult bindingResult) {
        logger.entry();
        ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession(false);

        // 세션 존재 여부 확인
        if (session == null) {
            logger.warn("No active session found");
            mav.setViewName("redirect:/login");
            return mav;
        }

        // 필드 검증 오류 처리
        bindingResult.getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            logger.info("Validation error: field: {}, message: {}", fieldName, error.getDefaultMessage());
            request.setAttribute(fieldName + "Error", error.getDefaultMessage());
        });

        if (bindingResult.hasErrors()) {
            logger.info("Validation errors found: {}", bindingResult.getErrorCount());
            mav.addObject("reportInfo", reportDTO);
            mav.setViewName("/report/reporteditinfo");
            return mav;
        }

        // 리포트 정보 업데이트 처리
        try (Session sqlSession = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = sqlSession.beginTransaction();

            Report report = reportDao.getReportById(reportDTO.getReportId());
            if (report != null) {
//            	report.setReportId(reportDTO.getReportId());
//            	report.setModel(reportDTO.getModel());
//            	report.setAdmin(reportDTO.getAdmin());
                report.setReportName(reportDTO.getReportName());
                report.setModelName(reportDTO.getModelName());
                report.setUsageDate(reportDTO.getUsageDate());
                report.setAccuracy(reportDTO.getAccuracy());
                report.setTotalProcessingAmount(reportDTO.getTotalProcessingAmount());
                report.setAverageResponseTime(reportDTO.getAverageResponseTime());
                report.setUpdateDate(LocalDateTime.now());

                sqlSession.update(report);
                tx.commit();

                mav.setViewName("redirect:/");
            } else {
                logger.warn("Report with ID {} not found", reportDTO.getReportId());
                mav.addObject("errorMessage", "해당 보고서를 찾을 수 없습니다.");
                mav.setViewName("/report/reporteditinfo");
            }
        } catch (Exception e) {
            logger.error("Error updating report information", e);
            mav.addObject("errorMessage", "보고서 정보 업데이트 중 오류가 발생했습니다.");
            mav.setViewName("/report/reporteditinfo");
        }

        logger.exit();
        return mav;
    }
    
    @RequestMapping(value = "/report/delete", method = RequestMethod.GET)
    public ModelAndView deleteModel(HttpServletRequest request) {
    	logger.entry();
    	ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession(false);

        if (session != null) {
            Admin admin = (Admin) session.getAttribute("admin");
            String targetModelId;
            if (admin != null) {
                
                targetModelId = request.getParameter("reportId");
                Report report = reportDao.getReportById(targetModelId);
                
                // Ensure the model belongs to the current admin
                if (report != null) {
                    reportDao.deleteReport(report);
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
