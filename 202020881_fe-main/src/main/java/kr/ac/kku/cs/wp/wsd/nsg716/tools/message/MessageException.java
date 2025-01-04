/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.tools.message;


/**
 * MessageException
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
public class MessageException extends RuntimeException {
	// 생성자 생성
	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public MessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	    super(message, cause, enableSuppression, writableStackTrace);
	    // TODO Auto-generated constructor stub
	}
	
	/**
	 * @param message
	 * @param cause
	 */
	public MessageException(String message, Throwable cause) {
	    super(message, cause);
	    // TODO Auto-generated constructor stub
	}
	
	/**
	 * @param message
	 */
	public MessageException(String message) {
	    super(message);
	    // TODO Auto-generated constructor stub
	}
	
	/**
	 * @param cause
	 */
	public MessageException(Throwable cause) {
	    super(cause);
	    // TODO Auto-generated constructor stub
	}
}

	