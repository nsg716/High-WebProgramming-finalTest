/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.model.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.ac.kku.cs.wp.wsd.nsg716.model.dao.ModelDAO;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Admin;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Model;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.User;
/**
 * ModelControllerTest
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 06.
 * @version 1.0
 *
 */
@SpringJUnitConfig
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/app-context.xml")
@WebAppConfiguration
public class ModelControllerTest {

    // MockMvc: 스프링 MVC 컨트롤러 테스트를 위한 모의 환경 설정
    private MockMvc mockMvc;

    // 웹 애플리케이션 컨텍스트 주입
    @Autowired
    private WebApplicationContext context;

    // 모델 데이터 접근 객체 (DAO) 주입
    @Autowired
    private ModelDAO modelDAO;

    // 테스트에 사용될 유효한 사용자 및 관리자 객체
    private User validUser;
    private Admin validAdmin;

    // 샘플 모델 데이터 리스트
    private List<Model> sampleModels;

    // JSON 직렬화/역직렬화를 위한 ObjectMapper
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // MockMvc 설정 - 웹 애플리케이션 컨텍스트를 사용하여 테스트 환경 초기화
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        
        // ModelDAO 목 객체 생성 (실제 데이터베이스 대신 가상 데이터 사용)
        modelDAO = org.mockito.Mockito.mock(ModelDAO.class);

        // 테스트용 유효한 사용자 및 관리자 정보 생성
        validUser = new User();
        validUser.setUserId("user001");
        validUser.setName("alice");

        validAdmin = new Admin();
        validAdmin.setId("admin001");
        validAdmin.setName("Admin1");

        // 샘플 모델 데이터 생성 - 데이터베이스 INSERT 문과 동일한 데이터
        sampleModels = Arrays.asList(
            createModel("model001", "admin001", "Model A", "1.0", "Classification", "TensorFlow", "Classification model for images"),
            createModel("model002", "admin002", "Model B", "2.0", "Regression", "PyTorch", "Regression model for sales prediction"),
            createModel("model003", "admin003", "Model C", "1.5", "Clustering", "Scikit-learn", "Clustering model for customer segmentation")
        );

        // JSON 직렬화/역직렬화를 위한 ObjectMapper 초기화
        objectMapper = new ObjectMapper();

        // 목 모델 DAO의 메서드 동작 정의
        // getModels() 및 getAllModels() 호출 시 샘플 모델 데이터 반환
        when(modelDAO.getModels(null)).thenReturn(sampleModels);
        when(modelDAO.getAllModels()).thenReturn(sampleModels);
        when(modelDAO.getModels(any(Model.class))).thenReturn(sampleModels);
    }

    // 모델 객체 생성을 위한 헬퍼 메서드 - 코드 중복 방지 및 가독성 개선
    private Model createModel(String modelId, String adminId, String modelName, 
                               String version, String modelType, 
                               String framework, String description) {
        Model model = new Model();
        model.setModelId(modelId);
        model.setAdminId(adminId);
        model.setModelName(modelName);
        model.setVersion(version);
        model.setModelType(modelType);
        model.setFramework(framework);
        model.setDescription(description);
        return model;
    }

    // 관리자로 모델 리스트 접근 테스트
    // 예상 결과: 성공적인 접근, 모델 리스트 뷰, 모델 데이터 존재
    @Test
    void testModelListAccessByAdmin() throws Exception {
        mockMvc.perform(get("/model/modellist")
                .sessionAttr("admin", validAdmin))
                .andExpect(status().isOk())
                .andExpect(view().name("model/modellist"))
                .andExpect(model().attributeExists("models"));
    }

    // 일반 사용자로 모델 리스트 접근 테스트
    // 예상 결과: 성공적인 접근, 모델 리스트 뷰, 모델 데이터 존재
    @Test
    void testModelListAccessByUser() throws Exception {
        mockMvc.perform(get("/model/modellist")
                .sessionAttr("user", validUser))
                .andExpect(status().isOk())
                .andExpect(view().name("model/modellist"))
                .andExpect(model().attributeExists("models"));
    }

    // 미인증 사용자의 모델 리스트 접근 테스트
    // 예상 결과: 로그인 페이지로 리다이렉트
    @Test
    void testModelListAccessUnauthorized() throws Exception {
        mockMvc.perform(get("/model/modellist"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    // 모델 이름으로 필터링 테스트
    // 예상 결과: 모델 카드 뷰, 필터링된 모델 데이터
    @Test
    void testFilterModelListByModelName() throws Exception {
        Model filterParams = new Model();
        filterParams.setModelName("Model A");

        mockMvc.perform(post("/model/modellist/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filterParams)))
                .andExpect(status().isOk())
                .andExpect(view().name("/model/modelCard"))
                .andExpect(model().attributeExists("models"));
    }

    // 모델 프레임워크로 필터링 테스트
    // 예상 결과: 모델 카드 뷰, 필터링된 모델 데이터
    @Test
    void testFilterModelListByFramework() throws Exception {
        Model filterParams = new Model();
        filterParams.setFramework("TensorFlow");

        mockMvc.perform(post("/model/modellist/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filterParams)))
                .andExpect(status().isOk())
                .andExpect(view().name("/model/modelCard"))
                .andExpect(model().attributeExists("models"));
    }

    // 빈 필터 파라미터로 모델 리스트 테스트
    // 예상 결과: 전체 모델 리스트 뷰, 모든 모델 데이터
    @Test
    void testFilterModelListWithEmptyFilter() throws Exception {
        mockMvc.perform(post("/model/modellist/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Model())))
                .andExpect(status().isOk())
                .andExpect(view().name("/model/modelCard"))
                .andExpect(model().attributeExists("models"));
    }

    // Null 파라미터로 모델 리스트 테스트
    // 예상 결과: 전체 모델 리스트 뷰, 모든 모델 데이터
    @Test
    void testFilterModelListNullParams() throws Exception {
        mockMvc.perform(post("/model/modellist/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isOk())
                .andExpect(view().name("/model/modellist"))
                .andExpect(model().attributeExists("models"));
    }
}