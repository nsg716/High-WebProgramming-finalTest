/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.report.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import jakarta.servlet.http.HttpServletRequest;

import kr.ac.kku.cs.wp.wsd.nsg716.model.dao.ModelDAO;
import kr.ac.kku.cs.wp.wsd.nsg716.report.dao.ReportDAO;
import kr.ac.kku.cs.wp.wsd.nsg716.report.dto.ReportDTO;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Admin;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Model;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Report;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.User;

/**
 * ReportControllerTest
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 06.
 * @version 1.0
 *
 */
public class ReportControllerTest {


    private MockMvc mockMvc;

    @Mock
    private ModelDAO modelDao;

    @Mock
    private ReportDAO reportDao;

    @Mock
    private MockHttpSession session;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ReportController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    // 보고서 생성 페이지 접근 테스트 - 세션 없음
    // 예상 결과: 로그인 페이지로 리다이렉트
    @Test
    void testShowReportCreatePage_WithoutSession() throws Exception {
        when(request.getSession(false)).thenReturn(null);

        mockMvc.perform(get("/report/reportcreate"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
    
    // 보고서 생성 테스트 - 성공 시나리오WL
    // 예상 결과: 보고서 성공적으로 생성, 메인 페이지로 리다이렉트
    @Test
    void testCreateReport_Success() throws Exception {
        User user = new User();
        user.setUserId("user001");

        Model model = new Model();
        model.setModelId("model001");

        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReportId("report004");
        reportDTO.setReportName("Test Report");
        reportDTO.setModelName("Test Model");
        reportDTO.setAccuracy(BigDecimal.valueOf(95.5));
        reportDTO.setTotalProcessingAmount(1000); // Integer로 수정
        reportDTO.setAverageResponseTime(BigDecimal.valueOf(50.0));

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("modelId")).thenReturn("model001");
        when(request.getParameter("adminId")).thenReturn("admin001");
        when(request.getParameter("modelName")).thenReturn("Test Model");
        when(modelDao.getModelById("model001")).thenReturn(model);
        when(reportDao.isReportIdDuplicate(reportDTO.getReportId())).thenReturn(false);

        mockMvc.perform(post("/reportcreate")
                .session(session)
                .flashAttr("reportDTO", reportDTO)
                .param("modelId", "model001")
                .param("adminId", "admin001")
                .param("modelName", "Test Model"))
                .andExpect(status().isOk())
                .andExpect(view().name("/report/reportcreate"));
    }
    // 보고서 생성 테스트 - 중복된 보고서 ID
    // 예상 결과: 보고서 생성 페이지 다시 로드, 중복 ID 에러 메시지 표시
    @Test
    void testCreateReport_DuplicateReportId() throws Exception {
        User user = new User();
        user.setUserId("user001");

        Model model = new Model();
        model.setModelId("model001");

        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReportId("duplicate001");

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("modelId")).thenReturn("model001");
        when(modelDao.getModelById("model001")).thenReturn(model);
        when(reportDao.isReportIdDuplicate("duplicate001")).thenReturn(true);

        mockMvc.perform(post("/reportcreate")
                .session(session)
                .flashAttr("reportDTO", reportDTO)
                .param("modelId", "model001"))
                .andExpect(status().isOk())
                .andExpect(view().name("/report/reportcreate"));

    }
    // 보고서 목록 조회 테스트 - 관리자 세션
    // 예상 결과: 보고서 목록 페이지 로드, 보고서 목록 데이터 존재
    @Test
    void testReportList_WithAdminSession() throws Exception {
        Admin admin = new Admin();
        admin.setId("admin001");

        Report report1 = new Report();
        Report report2 = new Report();
        
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("admin")).thenReturn(admin);
        when(reportDao.getReports(null)).thenReturn(Arrays.asList(report1, report2));

        mockMvc.perform(get("/report/reportlist")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("report/reportlist"))
                .andExpect(model().attributeExists("reports"));
    }
    // 보고서 필터링 테스트 - 파라미터 사용
    // 예상 결과: 필터링된 보고서 목록 로드, 보고서 카드 뷰 표시
    @Test
    void testFilterReportList_WithParameters() throws Exception {
        Report filterParams = new Report();
        filterParams.setReportName("Test Report");
        filterParams.setModelName("Test Model");

        Report report1 = new Report();
        report1.setReportName("Test Report");
        report1.setModelName("Test Model");

        when(reportDao.getReports(filterParams)).thenReturn(Arrays.asList(report1));

        mockMvc.perform(post("/report/reportlist/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"reportName\":\"Test Report\",\"modelName\":\"Test Model\"}"))
                .andExpect(status().isOk())
                .andExpect(view().name("/report/reportCard"))
                .andExpect(model().attributeExists("reports"));
    }
    // 보고서 상세 정보 조회 테스트 - 성공
    // 예상 결과: 보고서 상세 정보 페이지 로드, 보고서 정보 모델 속성에 존재
    @Test
    void testReportInfo_Success() throws Exception {
        Admin admin = new Admin();
        admin.setId("admin001");

        Report report = new Report();
        report.setReportId("report001");
        report.setReportName("Detailed Report");

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("admin")).thenReturn(admin);
        when(request.getParameter("reportId")).thenReturn("report001");
        when(reportDao.getReportById("report001")).thenReturn(report);

        mockMvc.perform(get("/report/info")
                .session(session)
                .param("reportId", "report001"))
                .andExpect(status().isOk())
                .andExpect(view().name("/report/reportinfo"))
                .andExpect(model().attributeExists("reportInfo"));
    }
    // 보고서 정보 업데이트 테스트 - 성공
    // 예상 결과: 보고서 정보 성공적으로 업데이트, 메인 페이지로 리다이렉트
    @Test
    void testUpdateReportInfo_Success() throws Exception {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReportId("report001");
        reportDTO.setReportName("Updated Report");
        reportDTO.setModelName("Updated Model");
        reportDTO.setAccuracy(BigDecimal.valueOf(95.5));

        Report existingReport = new Report();
        existingReport.setReportId("report001");

        when(request.getSession(false)).thenReturn(session);
        when(reportDao.getReportById("report001")).thenReturn(existingReport);

        mockMvc.perform(post("/report/updateinfo")
                .session(session)
                .flashAttr("reportDTO", reportDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("/report/reporteditinfo"));
    }
    // 보고서 삭제 테스트 - 성공WL
    // 예상 결과: 보고서 성공적으로 삭제, 메인 페이지로 리다이렉트
    @Test
    void testDeleteReport_Success() throws Exception {
        Admin admin = new Admin();
        admin.setId("admin001");

        Report report = new Report();
        report.setReportId("report001");

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("admin")).thenReturn(admin);
        when(request.getParameter("reportId")).thenReturn("report001");
        when(reportDao.getReportById("report001")).thenReturn(report);

        mockMvc.perform(get("/report/delete")
                .session(session)
                .param("reportId", "report001"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
    
    // 보고서 필터링 테스트 - Null 파라미터
    // 예상 결과: 전체 보고서 목록 로드, 보고서 목록 뷰 표시
    @Test
    void testFilterReportList_NullParams() throws Exception {
        when(reportDao.getAllReports()).thenReturn(Arrays.asList(new Report(), new Report()));

        mockMvc.perform(post("/report/reportlist/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isOk())
                .andExpect(view().name("/report/reportlist"))
                .andExpect(model().attributeExists("reports"));
    }
}