/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.tools.secure;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import kr.ac.kku.cs.wp.wsd.nsg716.user.dao.UserDAOImpl;

import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * CryptoUtil
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
public class CryptoUtil {
	
	private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);
	/**
	* 메세지를 salt를 붙여서 SHA 256으로 해쉬하고 해쉬된 메세지 (base64) 와
	* salt (base64) 를 붙여서 반환
	* ...
	*/

	
	
	public static String hash(String message, byte[] salt) throws NoSuchAlgorithmException {
		//hashing 구현
		StringBuffer hasedMessageAndSalt = new StringBuffer();

		MessageDigest md = MessageDigest.getInstance("SHA-256");

		md.update(salt);

		byte[] hashedMessage = md.digest(message.getBytes());

		String base64Salt = Base64.getEncoder().encodeToString(salt);

		String base64HashedMessage = Base64.getEncoder().encodeToString(hashedMessage);

		hasedMessageAndSalt.append(base64HashedMessage).append(base64Salt);

		logger.debug("salt: {}", base64Salt);

		logger.debug("hashedMessage:{} ", base64HashedMessage);

		logger.debug("hasedMessageAndSalt:{}",hasedMessageAndSalt.toString());

		return hasedMessageAndSalt.toString();

		
	}

 

	/**
	* salt 생성 반환
	* ...
	*/
	public static byte[] genSalt() {
		// salt 생성 반환 구현
		SecureRandom sr = new SecureRandom();
		byte[] salt = new byte[16];
		sr.nextBytes(salt);

		return salt;

	}


	/**
	* hashed message + salt에서 salt 추출
	* ...
	*/
	public static byte[] extractSalt(String base64HassedMessageAndSalt) {
	       // salt 추출 구현
		byte[] salt;
		String base64Salt = base64HassedMessageAndSalt.substring(44);
		logger.debug("base64Salt: {}", base64Salt );
		salt = Base64.getDecoder().decode(base64Salt);
		return salt;
	}
	
	 
	
	/**
	* hashed message 와 새로운 Message 비교
	* ...
	*/
	
	public static boolean isMatch(String base64hasedMessageAndSalt, String message) throws NoSuchAlgorithmException{
	      // hashed message 와 새로운 Message 비교 구현	
		boolean isMatch = false;
		if (base64hasedMessageAndSalt == null || message == null) {
			throw new NullPointerException();
		}
		byte[] salt = extractSalt(base64hasedMessageAndSalt);
		
		String base64HasedMAS = hash(message, salt);
		
		if (base64hasedMessageAndSalt.equals(base64HasedMAS)) {
			isMatch = true;
		}

		return isMatch;
	}



	public static boolean verify(String inputPassword, String storedHash) throws NoSuchAlgorithmException {
	    // 입력된 비밀번호와 저장된 해시를 비교
	    if (inputPassword == null || storedHash == null) {
	        throw new NullPointerException("Input password or stored hash cannot be null");
	    }

	    // 저장된 해시에서 솔트를 추출
	    byte[] salt = extractSalt(storedHash);

	    // 입력된 비밀번호를 해시화
	    String inputHash = hash(inputPassword, salt);

	    // 해시 비교
	    return inputHash.equals(storedHash);
	}

 

}
