/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.support.sql;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;

import jakarta.persistence.Entity;

/**
 * HibernateUtil
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 11. 19.
 * @version 1.0
 *
 */
public class HibernateUtil {

	private static final Logger logger = LogManager.getLogger(HibernateUtil.class);
	private static final SessionFactory sessionFactory = buildSessionFactory();

	
	
	private static SessionFactory buildSessionFactory() {
	    try {
	        Configuration configuration = new Configuration().configure();
	        String packageName = configuration.getProperty("entity.package.scan");
	        logger.debug("entity.package.scan: {}", packageName);
	        
	        if (packageName != null && !packageName.isBlank()) {
	            Set<Class<?>> entityClasses = findEntityClasses(packageName);
	            for (Class<?> entityClass : entityClasses) {
	                logger.debug(entityClass.getName());
	                configuration.addAnnotatedClass(entityClass);
	            }
	        }
	        
	        return configuration.buildSessionFactory();
	    } catch (Throwable ex) {
	        throw new ExceptionInInitializerError(ex);
	    }
	}

	private static Set<Class<?>> findEntityClasses(String packageName) {
	    Set<Class<?>> rtn = null;
	    // Reflections 라이브러리를 통해 특정 패키지의 모든 엔티티 클래스 찾기
	    Reflections reflections = new Reflections(packageName);
	    try {
	        rtn = reflections.getTypesAnnotatedWith(Entity.class).stream()
	                .filter(cls -> cls.isAnnotationPresent(Entity.class))
	                .collect(Collectors.toSet());
	    } catch (ReflectionsException e) {
	        // TODO Auto-generated catch block
	        logger.warn("No Entity Class: {}", e.getMessage());
	    }
	    return rtn;
	}

	public static SessionFactory getSessionFactory() {
	      return sessionFactory;
	}
	public static void shutdown() {
	      getSessionFactory().close();
	}

}