/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.resources;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 
 * MvcConfiguration
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 11. 23.
 * @version 1.0
 */
@Configuration
@EnableWebMvc
public class MvcConfiguration implements WebMvcConfigurer {
	
	@Override 
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		/* '/js/**'로 호출하는 자원은 '/static/js/' 폴더 아래에서 찾는다. */ 
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/").setCachePeriod(60 * 60 * 24 * 365); 
		/* '/css/**'로 호출하는 자원은 '/static/css/' 폴더 아래에서 찾는다. */ 
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/").setCachePeriod(60 * 60 * 24 * 365); 
		/* '/img/**'로 호출하는 자원은 '/static/image/' 폴더 아래에서 찾는다. */ 
        registry.addResourceHandler("/image/**").addResourceLocations("classpath:/static/image/").setCachePeriod(60 * 60 * 24 * 365); 
		/* '/font/**'로 호출하는 자원은 '/static/font/' 폴더 아래에서 찾는다. */ 
        registry.addResourceHandler("/font/**").addResourceLocations("classpath:/static/font/").setCachePeriod(60 * 60 * 24 * 365); 
        /* '/html/**'로 호출하는 자원은 '/static/html/' 폴더 아래에서 찾는다. */ 
        registry.addResourceHandler("/html/**").addResourceLocations("classpath:/static/html/").setCachePeriod(60 * 60 * 24 * 365); 
	}
	
}