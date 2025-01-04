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
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Admin;

/**
 * AdminDAO
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
public interface AdminDAO {
    // 관리자 정보 가져오기
    public Admin getAdminById(String adminId);

    // 관리자 정보 업데이트
    public Admin updateAdmin(Admin admin);

    // 관리자 삭제
    public void deleteAdmin(Admin admin);

    // 관리자 목록 가져오기
    public List<Admin> getAdmins(Admin admin);

    // 로그인
    // ID와 비밀번호 비교 메서드 추가
    public boolean validateAdminCredentials(String adminId, String password);

    // 관리자 아이디 중복 확인 메서드
    public boolean isAdminIdDuplicate(String adminId);

    // 비밀번호 조건 검증 메서드
    public boolean isPasswordValid(String password);


}
