/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.user.dao;

import java.util.List;

import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.User;


/**
 * UserDAO
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
public interface UserDAO {
	void save(User user); // 메서드 정의 추가
	// 사용자, 관리자  아이디 불러오기 
	public User getUserById(String userId);
	
	// 사용자 기능 - 유저 정보 불러오기 
	public User getUser(User user);
	
	// 사용자,관리자 기능
	public User updateUser(User user);  
	public void deleteUser(User user);
	
	// 회원가입, 관리자 기능 - 추가로 회원가입을 하기 
	public User createUser(User user);
	
	// 관리자 기능 : 유저 리스트 불러오기  
	public List<User> getUsers(User user);
	
	// 로그인
	// ID와 비밀번호 비교 메서드 추가
	public boolean validateUserCredentials(String userId, String password); 
    
	// 회원가입
	// 아이디 중복 확인 메서드
    public boolean isUserIdDuplicate(String userId);
    // 비밀번호 조건 검증 메서드
    public boolean isPasswordValid(String password);
    
    
    // 전체 사용자 호출 
    public List<User> getAllUsers();
}