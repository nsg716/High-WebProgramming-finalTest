/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.support.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * LoggingAspect
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 06.
 * @version 1.0
 *
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LogManager.getLogger(LoggingAspect.class);

 // logging aspect 구현
    @Pointcut("within(@org.springframework.stereotype.Controller * )")
    public void callMethods() {}

    @Before("callMethods()")
    public void logBefore(JoinPoint joinPoint) {
        logger.trace("before {} {}", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName());
    }

    @After("callMethods()")
    public void logAfter(JoinPoint joinPoint) {
        logger.trace("after {} {}", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "callMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.trace("after returning {} result {}", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName(), result);
    }

    @AfterThrowing(pointcut = "callMethods()", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        logger.trace("after throwing {} {} Exception: {}", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName(), error);
    }

}
