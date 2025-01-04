/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * ViewController
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
@Controller
public class ViewController {
	public static Logger logger = LogManager.getLogger(ViewController.class);
	

   @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView homeGet() {
	   logger.entry();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/home");
        return mav;
    }
   
   @RequestMapping(value = "/home", method = RequestMethod.POST)
   public ModelAndView homePost() {
	   logger.entry();
       ModelAndView mav = new ModelAndView();
       mav.setViewName("/home");
       return mav;
   }
	
}
