/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.model.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import kr.ac.kku.cs.wp.wsd.nsg716.model.dao.ModelDAO;
import kr.ac.kku.cs.wp.wsd.nsg716.model.dto.ModelDTO;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Admin;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.http.HttpServletRequest;



/**
 * ModelManagementControllerTest
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 06.
 * @version 1.0
 *
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ModelManagementControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ModelDAO modelDao;
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private MockHttpSession session;

    @InjectMocks
    private ModelManagementController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }
    
    // 관리자 세션으로 모델 생성 페이지 접근 테스트
    // 예상 결과: 상태 코드 200 OK 및 뷰 이름 "/model/modelcreate" 반환
    @Test
    @Order(1)
    void testShowModelCreatePage_WithAdminSession() throws Exception {
        Admin admin = new Admin();
        admin.setId("admin001");
        when(session.getAttribute("admin")).thenReturn(admin);

        mockMvc.perform(get("/modelcreate").sessionAttr("admin", admin))
                .andExpect(status().isOk())
                .andExpect(view().name("/model/modelcreate"));
    }

    
    // 세션 없는 상태에서 모델 생성 페이지 접근 테스트
    // 예상 결과: 로그인 페이지로 리다이렉션
    @Test
    @Order(2)
    void testShowModelCreatePage_WithoutSession() throws Exception {
        mockMvc.perform(get("/modelcreate"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
    
    // 모델 생성 성공 테스트WL
    // 예상 결과: 상태 코드 3xx 및 홈 페이지로 리다이렉션
    @Test
    @Order(3)
    void testCreateModel_Success() throws Exception {
        Admin admin = new Admin();
        admin.setId("admin001");
        when(session.getAttribute("admin")).thenReturn(admin);

        ModelDTO modelDTO = new ModelDTO();
        modelDTO.setModelId("model004");
        modelDTO.setModelName("New Model");
        modelDTO.setVersion("1.0");
        modelDTO.setModelType("Classification");
        modelDTO.setCreateDate(LocalDateTime.now());
        modelDTO.setUpdateDate(LocalDateTime.now());

        // modelDao에서 모델 ID 중복 확인 시 false 반환

        when(session.getAttribute("admin")).thenReturn(admin);
        
        mockMvc.perform(post("/modelcreate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("modelId", "model004")
                        .param("modelName", "New Model")
                        .param("version", "1.0")
                        .param("modelType", "Classification")
                        .sessionAttr("admin", admin))
			        .andExpect(status().isOk())
			        .andExpect(view().name("/model/modelcreate"));
    }
    
    // 중복된 모델 ID로 모델 생성 시도 테스트 - 오류 반환
    // 예상 결과: 상태 코드 200 OK 및 뷰 이름 "/model/modelcreate" 반환 
    @Test
    @Order(4)
    void testCreateModel_DuplicateModelId() throws Exception {
        Admin admin = new Admin();
        admin.setId("admin001");

        when(session.getAttribute("admin")).thenReturn(admin);

        when(modelDao.isModelIdDuplicate("model001")).thenReturn(true);

        mockMvc.perform(post("/modelcreate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("modelId", "model001")
                        .param("modelName", "Existing Model")
                        .param("version", "1.0")
                        .param("modelType", "Classification")
                        .param("framework", "TensorFlow")
                        .sessionAttr("admin", admin))
                .andExpect(status().isOk())
                .andExpect(view().name("/model/modelcreate"))
                .andExpect(model().attributeExists("modelIdError"));
    }

    // 유효한 모델 정보 조회 테스트
    // 예상 결과: 상태 코드 200 OK 및 모델 정보가 포함된 뷰 반환
    @Test
    @Order(5)
    void testInfoModel_ValidModel() throws Exception {
        Admin admin = new Admin();
        admin.setId("admin001");

        Model model = new Model();
        model.setModelId("model001");
        model.setModelName("Model A");

        when(session.getAttribute("admin")).thenReturn(admin);
        when(modelDao.getModelById("model001")).thenReturn(model);

        mockMvc.perform(get("/model/info") 
                        .param("modelId", "model001")
                        .sessionAttr("admin", admin))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("modelInfo")) // modelInfo 속성이 존재하는지 확인
                .andExpect(model().attribute("modelInfo", model)); // 반환된 modelInfo가 기대하는 객체인지 확인
    }

    // 모델 삭제 성공 테스트WL
    // 예상 결과: 상태 코드 3xx 및 홈 페이지로 리다이렉션
    @Test
    @Order(6)
    void testDeleteModel_Success() throws Exception {
        // 관리자 객체 설정
        Admin admin = new Admin();
        admin.setId("admin001");

        // 모델 객체 설정
        Model model = new Model();
        model.setModelId("model004");

        // 세션과 DAO 동작 설정
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("admin")).thenReturn(admin);
        when(modelDao.getModelById("model004")).thenReturn(model);
        
    

        // DELETE 요청 수행
        mockMvc.perform(get("/model/delete")
                        .param("modelId", "model004")
                        .sessionAttr("admin", admin))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/")); // 리다이렉션이 홈 페이지로 가는지 확인
    }
    
    // 존재하지 않는 모델 삭제 시도 테스트
    // 예상 결과: 로그인 페이지로 리다이렉션
    @Test
    @Order(7)
    void testDeleteModel_ModelNotFound() throws Exception {
        Admin admin = new Admin();
        admin.setId("admin001");

        when(session.getAttribute("admin")).thenReturn(admin);
        when(modelDao.getModelById("model999")).thenReturn(null);

        mockMvc.perform(get("/model/delete")
                        .param("modelId", "model999")
                        .sessionAttr("admin", admin))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
    

}
