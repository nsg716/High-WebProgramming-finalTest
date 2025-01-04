/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.report.dao;

import java.util.List;

import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Report;
/**
 * ReportDAO
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
public interface ReportDAO {
	//리포트 정보 가져오기 
	public Report getReportById(String reportId);
	
	
	// 리포트 만들기
    public Report createReport(Report report);
    // 리포트 수정하기
    public Report updateReport(Report report);

    // 리포트 지우기
    public void deleteReport(Report report);
    
    public List<Report> getAllReports();
    
    
	public List<Report> getReports(Report report);
    // 특정 관리자 아이디로 모으기
    public List<Report> getReportsByAdminId(String adminId);
    // 특정 모델 ID로 모으기
    public List<Report> getReportsByModelId(String modelId);
    
	// 아이디 중복 확인 메서드
    public boolean isReportIdDuplicate(String reportId);

    public List<Report> filterReports(String adminId, String modelId, String status, String dateFrom, String dateTo);
}
